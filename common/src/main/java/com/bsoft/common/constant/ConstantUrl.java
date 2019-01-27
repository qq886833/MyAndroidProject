package com.bsoft.common.constant;



import java.util.HashMap;
import java.util.Map;

/**
 * Created by diwang on 2018/6/14.
 */

public class ConstantUrl {
    //各个模块首页和跳转地址的映射
    public static Map<String,String> activityRouterMap = new HashMap<>();

    ///每个模块  第一个必须不同
    public static final String LOGINSERVICE = "/service/login/user";
        /*登录*/
    public static final String LOGIN = "/account/view/login";
    /*登录*/
    public static final String LOGIN1 = "/account/view/login1";
    /*注册*/
    public static final String REGISTER = "/account/view/register";
    /*主页*/
    public static final String MAINTAB = "/mian/view/tab";
    /*主页*/
    public static final String MAINTAB1 = "/mian/view/tab1";
    /*咨询*/
    public static final String CONSULT = "/consult/view/Initiate";
    /*咨询历史*/
    public static final String CONSULTHIS = "/consult/view/his";
    /*上门咨询*/
    public static final String VISITCONSULTHIS = "/consult/view/visithis";
    public static final String SCHEDULE = "/consult/view/schedule";

    public static final String STATUSBEHAVAR = "/demotest/view/statusbe";
    public static final String BANDG = "/demotest/view/bandg";
    public static final String IMAGECHOOSE = "/demotest/view/imagechoose";
    public static final String SETTINGOUT = "/setting/view/settingout";
    public static final String LOADGIN = "/account/view/loading";
    public static final String STATUS = "/demotest/view/status";

    public static final String SEGMENTGROUP="/demotest/view/segmentgroup";
    public static final String TABLAYOUT = "/demotest/view/tablayout";
    public static final String IPC= "/demotest/view/ipc";
    public static final String LIVEDATA= "/demotest/view/livedata";
    public static final String STRPER = "/demotest/view/steper";
    public static final String APPLIYHOME = "/demotest/view/appliyhome";
    public static final String POPWINDOW = "/demotest/view/popwindow";
    public static final String RXPERMISSON = "/demotest/view/rxpermission";
    public static final String INDEXBAR = "/demotest/view/indexbar";
    public static final String MYVIEW = "/demotest/view/myview";
    public static final String WHEELVIEW = "/demotest/view/wheelview";
    public static final String COORDINATORTABLAYOUT = "/demotest/view/coordinatortablayout";


    public static final String NDK = "/nndk/ndk";




    static{
        /*获取应用程序名称*/
//        activityRouterMap.put(AppApplication.getApplication().getString(R.string.label_app),MAINTAB);
//        activityRouterMap.put(AppApplication.getApplication().getString(R.string.label_account),LOGIN);
//        activityRouterMap.put(AppApplication.getApplication().getString(R.string.label_demo),CONSULT);
//        activityRouterMap.put(AppApplication.getApplication().getString(R.string.label_consult),CONSULTHIS);
    }


    public static String getCurRouter(String label){
        return activityRouterMap.get(label);
    }
}
