package com.bsoft.network_api.bean;


import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bsoft.constant.Constants;
import com.bsoft.push.PushInfo;
import com.bsoft.utils.AppLogger;
import com.bsoft.utils.LiveDataBus;
import com.orhanobut.logger.Logger;

public class Parser {

    public static final String CODE = "code";
    public static final String DATA = "body";
    public static final String MSG = "msg";
    public static final String PRO = "properties";

    private static final int LIST = 1;
    private static final int OBJECT = 2;
    private static final int BASE = 3;

    private Parser() {

    }

    private static class SingletonHolder {
        private static final Parser INSTANCE = new Parser();
    }

    public static Parser getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* *
     *
     * @param json
     * @param clazz
     * @param dataId data节点 (接口数据节点不统一)
     * @param needCode 是否需要code判断（有些接口没有返回code）
     * @return
     */
    public ResultModel parserSpData(String json, Class clazz, String dataId, boolean needCode) {
        if (json != null) {
            Logger.d(json);
            Logger.json(json);
        } else {
            AppLogger.e(json);
        }
        ResultModel mList = new ResultModel();
        if (null != json) {
            try {
                mList.json = json;
                JSONObject obj = JSON.parseObject(json);
                int code = getCode(obj);
                mList.setCode(code);
                if (obj.containsKey(MSG)) {
                    mList.message = obj.getString(MSG);
                }
                ResultModel fList = null;
                fList = doFilter(code, obj);
                if (null != fList) {
                    fList.setCode(code);
                    return fList;
                }
                if (code == 200 || !needCode) {
                    mList.statue = Statue.SUCCESS;
                    if (obj.containsKey(PRO)) {
                        mList.pro = obj.getString(PRO);
                    }
                    if (obj.containsKey(dataId)) {
                        int type = getDataType(obj.getString(dataId));
                        switch (type) {
                            case LIST:
                                mList.data = JSON.parseArray(obj.getString(dataId));
                                break;
                            case OBJECT:
                                mList.data = JSON.parseObject(obj.getString(dataId));
                                break;
                            case BASE:
                                mList.data = obj.get(dataId);
                                break;
                        }
                        return mList;
                    } else {
                        if (TextUtils.isEmpty(mList.message)) {
                            mList.message = "数据为空";
                        }
                        return mList;
                    }
                } else {
                    mList.statue = Statue.ERROR;
                    if (obj.containsKey(MSG)) {
                        mList.message = obj.getString(MSG);
                    }
                    return mList;
                }
            } catch (Exception e) {
                e.printStackTrace();
                mList.statue = Statue.PARSER_ERROR;
//                mList.data = data;
                return mList;
            }

        }
        return mList;
    }

    private int getDataType(String s) {
        if (s.startsWith("{")) {
            return OBJECT;
        }
        if (s.startsWith("[")) {
            return LIST;
        }
        return BASE;
    }

    private int getCode(JSONObject ob) {
        if (null != ob) {
            if (ob.containsKey(CODE)) {
                return ob.getIntValue(CODE);
            }
            return -88;
        }
        return -88;
    }

    private ResultModel doFilter(int code, JSONObject ob) {
        ResultModel mList = null;
        if (code == -6) {
            mList = new ResultModel();
            mList.statue = Statue.FORBID;
            if (ob.containsKey(MSG)) {
                mList.message = ob.getString(MSG);
            }
            return mList;
        }
        if (code == 403) {
            //TODO  使用LiveDataBus  代替广播
            PushInfo info = new PushInfo();
            info.description = "您的账号在其他设备上登陆,请重新登录!";
            info.title = "提示";
            LiveDataBus.get().with(Constants.Logout_ACTION).postValue(info);
            mList = new ResultModel();
            mList.statue = Statue.DEVICEID_ERROR;

            return mList;
        }
        if (code == -501) {
            //TODO
            PushInfo info = new PushInfo();
            info.description = "账号验证失败，请重新登录!";
            info.title = "提示";
            LiveDataBus.get().with(Constants.Logout_ACTION).postValue(info);
            mList = new ResultModel();
            mList.statue = Statue.DEVICEID_ERROR;
            return mList;
        }

        return mList;
    }
}
