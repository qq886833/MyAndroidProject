package com.bsoft.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import com.bsoft.config.AppContext;
import com.bsoft.utils.Installation;
import com.bsoft.utils.StringUtil;
import com.bsoft.utils.TPreferences;

import java.io.File;

/**
 * 基础类
 *
 * @author zkl
 */
public abstract class BaseApplication extends Application {

    private static LocalBroadcastManager mLocalBroadcastManager;

    public abstract String getTag();

    // 保存地址
    public String storeDir, storeImageUrl;

    public ArrayMap<String, Object> serviceMap;

    // 分辨率-宽带
    private static int widthPixels;
    private static int heightPixels;

    /**
     * 获取分辨率的宽度
     *
     * @return
     */
    public static int getWidthPixels() {
        if (0 == widthPixels) {
            WindowManager localWindowManager = (WindowManager) AppContext
                    .getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            localWindowManager.getDefaultDisplay().getMetrics(
                    localDisplayMetrics);
            widthPixels = localDisplayMetrics.widthPixels;
            heightPixels = localDisplayMetrics.heightPixels;
        }
        return widthPixels;
    }

    /**
     * 获取分辨率的高度
     *
     * @return
     */
    public static int getHeightPixels() {
        if (0 == heightPixels) {
            WindowManager localWindowManager = (WindowManager) AppContext
                    .getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            localWindowManager.getDefaultDisplay().getMetrics(
                    localDisplayMetrics);
            widthPixels = localDisplayMetrics.widthPixels;
            heightPixels = localDisplayMetrics.heightPixels;
        }
        return heightPixels;
    }


    /**
     * 得到分辨率的ga高宽比
     *
     * @return
     */
    public float getWH() {
        int w = getWidthPixels();
        if (w <= 0) {
            return 1;
        }
        int h = getHeightPixels();
        if (h <= 0) {
            return 1;
        }
        return (float) w / h;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.initialize(this);
       // Utils.init(this);
        serviceMap = new ArrayMap<String, Object>();
        TPreferences preferences = new TPreferences(this);
    //    Constants.DEBUG = preferences.getBooleanData(TPreferences.KEY_DEBUG, BuildConfig.DEBUG);
     //   ModelCache modelCache = new ModelCache();
        serviceMap.put("com.bsoft.app.preferences", preferences);
     //   serviceMap.put("com.bsoft.app.service.modelcache", modelCache);

        if (StringUtil.isEmpty(preferences.getStringData(
                "deviceId"))) {
            // 设置设备唯一号
            preferences.setStringData("deviceId",
                    Installation.id(getApplicationContext()));
        }

    }



    /**
     * 获取服务，自定义的服务
     *
     * @param key
     * @return
     */
    @Override
    public Object getSystemService(String key) {
        if (null != serviceMap) {
            if (this.serviceMap.containsKey(key)) {
                return serviceMap.get(key);
            }
        }
        return super.getSystemService(key);
    }

    /**
     * 获取存储空间地址
     *
     * @return
     */
    public String getStoreDir() {
        if (null == storeDir) {
//            File f = Environment.getExternalStorageDirectory();
            File f = this.getExternalFilesDir("");
//            File f = this.getExternalCacheDir();
            if (null != f) {
                StringBuffer path = new StringBuffer();
                path.append(f.getPath()).append(File.separator).append(getTag()).append(File.separator)
                        ;
                File dir = new File(path.toString());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                this.storeDir = path.toString();
            }
        }
        return storeDir;
    }

    public String getCacheDir(String dirStr) {
        File f = this.getExternalCacheDir();
        if (null != f) {
            StringBuffer path = new StringBuffer();
            path.append(f.getPath()).append(File.separator).append(getTag())
                    .append(File.separator).append(dirStr);
            File dir = new File(path.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return path.toString();
        }
        return null;
    }

    /**
     * 获取图片保存目录
     * @return
     */
    public String getStoreImageDir() {
        if (null == storeImageUrl) {
            if (null == getStoreDir()) {
                return null;
            } else {
                this.storeImageUrl = new StringBuffer(getStoreDir()).append(
                        "image").append(File.separator).toString();
            }
            File dir = new File(storeImageUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return this.storeImageUrl;
    }

    /**
     * 获取对应名字的图片保存目录
     * @param dirName
     * @return
     */
    public String getStoreImageDir(String dirName) {
        String path;
        if (null == getStoreImageDir()) {
            return null;
        } else {
            path = new StringBuffer(getStoreDir()).append(
                    "image").append(File.separator).append(dirName)
                    .append(File.separator).toString();
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }


    public static void sendProcessBroadcast(Intent intent) {
        if (null == mLocalBroadcastManager) {
            mLocalBroadcastManager = LocalBroadcastManager
                    .getInstance(AppContext.getContext());
        }
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    public static void registerProcessReceiver(BroadcastReceiver mReceiver,
                                               IntentFilter filter) {
        if (null == mLocalBroadcastManager) {
            mLocalBroadcastManager = LocalBroadcastManager
                    .getInstance(AppContext.getContext());
        }
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    public static void unregisterProcessReceiver(BroadcastReceiver mReceiver) {
        if (null == mLocalBroadcastManager) {
            mLocalBroadcastManager = LocalBroadcastManager
                    .getInstance(AppContext.getContext());
        }
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.serviceMap.clear();
        AppContext.clear();
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }



}