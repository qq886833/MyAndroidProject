package com.bsoft.bean;

/**
 * Created by 83990 on 2018/2/7.
 */


/*
 public static final String HTTP_URL = "http://218.92.200.226:13360/hcn-web/";// 正式
    public static final String HTTP_URL2 = "http://218.92.200.226:13360/pcn-core/";// 正式

    //图片地址
    public static String HttpImgUrl=HTTP_URL + "upload/";
* */
public class NetAddressVo extends BaseVo{
    private String environment;
    private String environmentText;
    private String HTTP_URL;
    private String HTTP_URL2;
    private String HttpImgUrl;


    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironmentText() {
        return environmentText;
    }

    public void setEnvironmentText(String environmentText) {
        this.environmentText = environmentText;
    }

    public String getHTTP_URL() {
        return HTTP_URL;
    }

    public void setHTTP_URL(String HTTP_URL) {
        this.HTTP_URL = HTTP_URL;
    }

    public String getHTTP_URL2() {
        return HTTP_URL2;
    }

    public void setHTTP_URL2(String HTTP_URL2) {
        this.HTTP_URL2 = HTTP_URL2;
    }


    public String getHttpImgUrl() {
        return HttpImgUrl;
    }

    public void setHttpImgUrl(String httpImgUrl) {
        HttpImgUrl = httpImgUrl;
    }


}
