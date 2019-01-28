package com.bsoft.network_api.net;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import com.bsoft.base.XbaseActivity;
import com.bsoft.constant.Constants;
import com.bsoft.network_api.bean.Parser;
import com.bsoft.network_api.bean.ResultModel;
import com.bsoft.utils.LiveDataBus;
import com.bsoft.utils.MD5;
import com.bsoft.utils.NetworkUtil;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;


import org.reactivestreams.Subscription;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by diwang on 2018/6/12.
 */

public class NetClient {

    public static ArrayMap<String, Integer> signMap = new ArrayMap<>();

//    static {
//        signMap.put(Constants.Person_Service, 1);
//        signMap.put(Constants.Family_Service, 1);
//        signMap.put(Constants.Card_Service, 1);
//        signMap.put(Constants.Health_Record_Service, 1);
//        signMap.put(Constants.Report_Service, 1);
//        signMap.put(Constants.Appoint_Service, 1);
//        signMap.put(Constants.Queue_Service, 1);
//    }

    public static void post(final XbaseActivity activity, String url,
                            ArrayMap<String, String> header, Object body,
                            final Class clazz,
                            final Listener listener) {
        post(activity, url, header, body, clazz, Parser.DATA, true, listener);

    }

    public static void post(final XbaseActivity activity, String url,
                            ArrayMap<String, String> header, Object body,
                            final Class clazz, final String dataId, boolean needCode,
                            final Listener listener) {

        if (!NetworkUtil.isNetworkAvailable()) {
            activity.showToast("网络未打开");
            if(listener != null) {
                listener.onFaile(null);
            }
            return;
        }
        if (header != null && signMap.containsKey(header.get(Constants.Head_Id))) {
            String accessToken = header.get(Constants.Head_Token);
            if (!TextUtils.isEmpty(accessToken) && accessToken.length() >= 8) {
                String salt = accessToken.substring(4, 8);
                String[] signs = new String[]{JSON.toJSONString(body), salt};
                Arrays.sort(signs);
                String digest = MD5.getMD5(signs[0] + signs[1]).toLowerCase();
                header.put("X-Signature", digest);
            }
        }
        header = addProductName(header);
        Logger.d(JSON.toJSONString(body));
        handelFlowable(activity, clazz, dataId, needCode, listener,
                RetrofitClient.getInstance().post(url, header, body));
    }
    @android.support.annotation.NonNull
    private static ArrayMap<String, String> addProductName(ArrayMap<String, String> header) {
        if(header == null) {
            header = new ArrayMap<>();
        }
        header.put("B-Product-Code", Constants.Product_Name);
        return header;
    }
    public static void uploadHead(final XbaseActivity activity, String url,
                                  String filePath, String serviceFileName,
                                  ArrayMap<String, String> header,
                                  final Class clazz, final String dataId,
                                  final Listener listener) {

        if (!NetworkUtil.isNetworkAvailable()) {
            activity.showToast("网络未打开");
            if(listener != null) {
                listener.onFaile(null);
            }
            return;
        }

        header = addProductName(header);

        handelFlowable(activity, clazz, dataId, true, listener,
                RetrofitClient.getInstance().postHeader(url, filePath, serviceFileName, header));
    }
    public static void uploadFile(final XbaseActivity activity, String url, ArrayMap<String, String> maps, List<String> filePaths,
                                  ArrayMap<String, String> header,
                                  final Class clazz, final String dataId,
                                  final Listener listener) {

        if (!NetworkUtil.isNetworkAvailable()) {
            activity.showToast("网络未打开");
            if(listener != null) {
                listener.onFaile(null);
            }
            return;
        }

        header = addProductName(header);
        handelFlowable(activity, clazz, dataId, true, listener,
                RetrofitClient.getInstance().postFiles(url, maps, filePaths, header));
    }
    private static void handelFlowable(final XbaseActivity activity, final Class clazz, final String dataId, final boolean needCode, final Listener listener, Flowable<String> stringFlowable) {
        stringFlowable.map(new Function<String, ResultModel>() {
            @Override
            public ResultModel apply(@NonNull String s) throws Exception {
                return Parser.getInstance().parserSpData(s, clazz, dataId, needCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Subscription>() {//subscribe()调用后而且在事件发送前执行(默认情况下subcribe发生的线程决定，可以通过最近的跟在后面的subscribeOn指定)
                    @Override
                    public void accept(@NonNull Subscription subscription) throws Exception {
                        if (listener != null) {
                            listener.onPrepare();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//指定doOnSubscribe的线程
                .compose(RxLifecycle.bindUntilEvent(activity.lifecycle(), ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        ResultModel result = (ResultModel) o;
                        if (listener != null) {
                            listener.onSuccess(result);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        if (t instanceof SocketTimeoutException) {
                            activity.showToast("请求超时");
                        } else {
                            activity.showToast("请求失败");
                        }
                        if (listener != null) {
                            listener.onFaile(t);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static void get(final XbaseActivity activity, String url,
                           ArrayMap<String, String> header,
                           final Class clazz,
                           final Listener listener) {
        get(activity, url, header, clazz, Parser.DATA, true, listener);
    }

    public static void get(final XbaseActivity activity, String url,
                           ArrayMap<String, String> header,
                           final Class clazz, final String dataId, boolean needCode,
                           final Listener listener) {

        if (!NetworkUtil.isNetworkAvailable()) {
            activity.showToast("网络未打开");
            if(listener != null) {
                listener.onFaile(null);
            }
            return;
        }

        header = addProductName(header);
        handelFlowable(activity, clazz, dataId, needCode, listener,
                RetrofitClient.getInstance().get(url, header));
    }










    public interface Listener<T> {
        void onPrepare();

        void onSuccess(ResultModel<T> result);

        void onFaile(Throwable t);
    }

}
