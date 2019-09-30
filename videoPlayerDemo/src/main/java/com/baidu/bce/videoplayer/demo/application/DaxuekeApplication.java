package com.baidu.bce.videoplayer.demo.application;

import android.app.Application;

import com.baidu.bce.videoplayer.demo.util.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;


/**
 * 作者：yeweizheng on 2017/3/1 22:07
 * 包名：youdu.com.daxueke.application
 * 文件名：daxueke
 * 描述：App容器
 */
public class DaxuekeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        //初始化Bugly  APP_ID自己去注册申请
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

    }
}
