package com.baidu.bce.videoplayer.demo.util;

import android.util.Log;

/**
 * 作者：yeweizheng on 2017/1/22 13:55
 * 包名：smartbutler.imooc.com.smartbutler.utils
 * 文件名：SmartButler
 * 描述：
 */
public class L {
    //开关
    public static final boolean DEBUG=true;
    //TAG
    public static final String TAG="SmartButler";
    //五个等级DIWE  //F
    public static void d(String text){
        if(DEBUG){
            Log.d(TAG, text);
        }
    }
    public static void i(String text){
        if(DEBUG){
            Log.i(TAG, text);
        }
    }  public static void w(String text){
        if(DEBUG){
            Log.w(TAG, text);
        }
    }
    public static void e(String text){
        if(DEBUG){
            Log.e(TAG, text);
        }
    }


 /*   public static void f(String text){
        if(DEBUG){
            Log.wtf(TAG, text);
        }
    }*/
}
