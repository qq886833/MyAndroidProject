package com.bsoft.module_main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.base.XbaseActivity;

import com.bsoft.common.constant.ConstantUrl;
import com.bsoft.common.widget.viewpager.LRViewPager;
import com.bsoft.constant.Constants;
import com.bsoft.module_main.fragment.HomeFragment;
import com.bsoft.module_main.fragment.MessageFragment;
import com.bsoft.module_main.fragment.MyFragment;
import com.bsoft.module_main.fragment.ServiceFragment;
import com.bsoft.network_api.net.NetClient;
import com.bsoft.utils.StringUtil;
import com.bsoft.utils.TPreferences;
import com.nineoldandroids.view.ViewHelper;


import java.util.ArrayList;
import java.util.List;


import io.reactivex.functions.Consumer;

/**
 * Created by diwang on 2018/2/5.
 */
@Route(path = ConstantUrl.MAINTAB)
public class MainTabActivity extends XbaseActivity {
    LRViewPager mViewPager;
    RelativeLayout lay1, lay2, lay3, lay4;
    MyPagerAdapter myPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    ArrayList<ImageView> normalFoots = new ArrayList<ImageView>();
    ArrayList<ImageView> selectedFoots = new ArrayList<ImageView>();
    ArrayList<TextView> normalFoots2 = new ArrayList<TextView>();
    ArrayList<TextView> selectedFoots2 = new ArrayList<TextView>();
    LayoutInflater mInflater;
    public boolean isLoad = true;
    public static boolean isForeground = false;
    boolean isLogout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long startTime = System.currentTimeMillis();
        //  StatusBarUtils.getStatusBarHeight(this);
        setContentView(R.layout.activity_main_tab);
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        Log.d("SplashActivity1", "time:" + time);

        this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isLoad = getIntent().getBooleanExtra("isLoad", isLoad);


        findView();
        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.HomeMessageCount_ACTION);
//        filter.addAction(Constants.MessageCountClear_ACTION);
//        this.registerReceiver(this.broadcastReceiver, filter);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                updateDeviceInfo();
            }
        });

        // 自动更新
         //  updateVersion();

        //   ARouter.getInstance().build(ConstantUrl.CONSULT).navigation();
//        LoginUserService  mLoginUserService = ((LoginUserService) ARouter.getInstance().build("/service/login/user").navigation());
//        Log.e("LoginUser==", JSON.toJSONString(mLoginUserService.getLoginUser()));
    }

    private void updateVersion() {

//        CheckVersionCall call = new CheckVersionCall(baseContext, application.getStoreDir());
//        call.enqueue(false, false);
    }

//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (Constants.HomeMessageCount_ACTION.equals(intent.getAction())) {
////                if (AppApplication.messageCount <= 0) {
////                    numText.setVisibility(View.GONE);
////                } else {
////                    numText.setCount(application.messageCount);
////                    numText.setVisibility(View.VISIBLE);
////                }
//            } else if (Constants.MessageCountClear_ACTION.equals(intent
//                    .getAction())) {
////                AppApplication.messageCount = 0;
////                numText.setVisibility(View.GONE);
//
//            }
//        }
//    };

    @Override
    public void findView() {

        mViewPager = (LRViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
        lay1 = (RelativeLayout) findViewById(R.id.lay1);
        lay2 = (RelativeLayout) findViewById(R.id.lay2);
        lay3 = (RelativeLayout) findViewById(R.id.lay3);
        lay4 = (RelativeLayout) findViewById(R.id.lay4);
        TopTabClickListener clickListener = new TopTabClickListener();
        lay1.setOnClickListener(clickListener);
        lay2.setOnClickListener(clickListener);
        lay3.setOnClickListener(clickListener);
        lay4.setOnClickListener(clickListener);

        normalFoots.add((ImageView) findViewById(R.id.f1_n));
        normalFoots.add((ImageView) findViewById(R.id.f2_n));
        normalFoots.add((ImageView) findViewById(R.id.f3_n));
        normalFoots.add((ImageView) findViewById(R.id.f4_n));

        normalFoots2.add((TextView) findViewById(R.id.f1_t_n));
        normalFoots2.add((TextView) findViewById(R.id.f2_t_n));
        normalFoots2.add((TextView) findViewById(R.id.f3_t_n));
        normalFoots2.add((TextView) findViewById(R.id.f4_t_n));
        selectedFoots.add((ImageView) findViewById(R.id.f1_p));
        selectedFoots.add((ImageView) findViewById(R.id.f2_p));
        selectedFoots.add((ImageView) findViewById(R.id.f3_p));
        selectedFoots.add((ImageView) findViewById(R.id.f4_p));
        selectedFoots2.add((TextView) findViewById(R.id.f1_t_p));
        selectedFoots2.add((TextView) findViewById(R.id.f2_t_p));
        selectedFoots2.add((TextView) findViewById(R.id.f3_t_p));
        selectedFoots2.add((TextView) findViewById(R.id.f4_t_p));

        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new ServiceFragment());
        mFragmentList.add(new MyFragment());


        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setChangeViewCallback(new LRViewPager.ChangeViewCallback() {

            @Override
            public void getCurrentPageIndex(int index) {
                changeIndex(index);
            }

            @Override
            public void changeView(boolean left, boolean right, int position,
                                   float positionOffset) {
                if (positionOffset > 0) {
                    ViewHelper.setAlpha(normalFoots.get(position),
                            positionOffset);
                    ViewHelper.setAlpha(selectedFoots.get(position),
                            1 - positionOffset);
                    ViewHelper.setAlpha(normalFoots.get(position + 1),
                            1 - positionOffset);
                    ViewHelper.setAlpha(selectedFoots.get(position + 1),
                            positionOffset);

                    ViewHelper.setAlpha(normalFoots2.get(position),
                            positionOffset);
                    ViewHelper.setAlpha(selectedFoots2.get(position),
                            1 - positionOffset);
                    ViewHelper.setAlpha(normalFoots2.get(position + 1),
                            1 - positionOffset);
                    ViewHelper.setAlpha(selectedFoots2.get(position + 1),
                            positionOffset);
                }
            }
        });
    }

    void updateDeviceInfo() {
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            //updateDeviceCall();
                        } else {
                            showToast("获取权限失败");
                        }
                    }
                });

    }

//    void updateDeviceCall() {
//        String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
//        if (StringUtil.isEmpty(registrationID)) {
//            return;
//        }
//        DeviceVo deviceVo = DeviceUtil.getDeviceInfo(getApplicationContext());
//        ArrayMap<String, String> head = new ArrayMap<>();
//        head.put(Constants.Head_Id, Constants.Device_Service);
//        head.put(Constants.Head_Method, "submitDevice");
//        head.put(Constants.Head_Token, ((LoginUserService) ARouter.getInstance().build("/service/login/user").navigation()).getAccessToken());
//
//        ArrayList body = new ArrayList();
//        ArrayMap<String, String> map = new ArrayMap<String, String>();
//        map.put("userId", ((LoginUserService) ARouter.getInstance().build("/service/login/user")
//                .navigation()).getLoginUser().userId);
//
//        map.put("roleId", "patient");
//        map.put("jpushRegId", JPushInterface.getRegistrationID(getApplicationContext()));
//        map.put("manufacturer", deviceVo.manufacturer);
//        map.put("model", deviceVo.model);
//        map.put("platform", deviceVo.platform);
//        map.put("version", deviceVo.version);
//        map.put("uuid", TPreferences.getInstance().getStringData("deviceId"));
//        map.put("imei", deviceVo.imei);
//        map.put("operator", deviceVo.operator);
//        map.put("network", deviceVo.network);
//        map.put("appVersion", AppUtil.getVersionName(getApplicationContext()));
//        body.add(map);
//        NetClient.post(baseActivity, Constants.Json_Request,
//                head, body, NullModel.class,
//                new NetClient.Listener<NullModel>() {
//                    @Override
//                    public void onPrepare() {
//
//                    }
//
//                    @Override
//                    public void onSuccess(ResultModel<NullModel> result) {
//                        if (result.isSuccess()) {
//
//
//                        } else {
//                            showToast(result.getToast());
//                        }
//                    }
//
//                    @Override
//                    public void onFaile(Throwable t) {
//
//                    }
//                });
//    }

    private class TopTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.lay1) {
                mViewPager.setCurrentItem(0, false);
               // StatusBarUtils.setStatusTextColor(true, MainTabActivity.this);
            } else if (view.getId() == R.id.lay2) {
                mViewPager.setCurrentItem(1, false);
              //  StatusBarUtils.setStatusTextColor(true, MainTabActivity.this);
            } else if (view.getId() == R.id.lay3) {
                mViewPager.setCurrentItem(2, false);
             //   StatusBarUtils.setStatusTextColor(true, MainTabActivity.this);
            } else if (view.getId() == R.id.lay4) {
                mViewPager.setCurrentItem(3, false);
              //  StatusBarUtils.setStatusTextColor(false, MainTabActivity.this);
            }
            changeIndex(mViewPager.getCurrentItem());
        }
    }

    private void changeIndex(int index) {
        for (int i = 0; i < mFragmentList.size(); i++) {
            if (i == index) {
                ViewHelper.setAlpha(normalFoots.get(index), 0);
                ViewHelper.setAlpha(selectedFoots.get(index), 1);

                ViewHelper.setAlpha(normalFoots2.get(index), 0);
                ViewHelper.setAlpha(selectedFoots2.get(index), 1);
            } else {
                ViewHelper.setAlpha(normalFoots.get(i), 1);
                ViewHelper.setAlpha(selectedFoots.get(i), 0);

                ViewHelper.setAlpha(normalFoots2.get(i), 1);
                ViewHelper.setAlpha(selectedFoots2.get(i), 0);
            }
        }
    }

    static private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
            super(fm);
            this.fragmentList = mFragmentList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null
                    : fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mHomeFragment.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    /* //计算未读的消息数
     private void setMessageNum(int messageCount) {
         if (messageCount > 0) {
             unReadCount.setVisibility(View.VISIBLE);
             if (messageCount > 99) {
                 unReadCount.setText("...");
             } else {
                 unReadCount.setText(String.valueOf(messageCount));
             }
         } else {
             unReadCount.setVisibility(View.GONE);
         }
     }
     //计算未读的消息数
     private int caculateMsgCount(List<MessageTypeBean> list){
         int count = 0;
         for(int i=0;i<list.size();i++){
             count = count+list.get(i).count;
         }
         return count;
     }
 */
    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    public void onDestroy() {
        isForeground = false;

        if (!isLogout) {
//            Intent intent = new Intent();
//            intent.setAction(Constants.CLOSE_ACTION); // 说明动作
//            sendBroadcast(intent);// 该函数用于发送广播
        }
        System.gc();
        super.onDestroy();
    }

}
