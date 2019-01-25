package com.bsoft.network_api.bean;

import java.util.Collection;

public class ResultModel<T> {

    public T data;

    //状态值
    public int statue = Statue.ERROR;

    public String message;

    public String pro;

    public int count;

    public int code;

    public String json;//json数据

    public boolean isSuccess() {
        return statue == Statue.SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isEmpty() {
        if (data instanceof Collection) {
            return data == null || ((Collection) data).isEmpty();
        }
        return data == null;
    }

    /*
     *是否有权限
     */
    public boolean isPermission() {
        return code != 830;
    }


    public String getToast() {
        switch (statue) {

            case Statue.NET_ERROR:
                return "网络加载失败";
            case Statue.ERROR:
                return null != message && message.length() > 0 ? message : "请求失败";
            case Statue.PARSER_ERROR:
                return "解析失败";
            default:
                return "";
        }
    }
}
