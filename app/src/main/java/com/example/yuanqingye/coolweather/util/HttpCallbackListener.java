package com.example.yuanqingye.coolweather.util;

/**
 * Created by yuanqing.ye on 2015/3/3.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
