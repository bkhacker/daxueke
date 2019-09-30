package com.baidu.bce.videoplayer.demo.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.baidu.bce.videoplayer.demo.util.Util;


/**
 * 作者：yeweizheng on 2017/3/1 22:11
 * 包名：youdu.com.daxueke.activity.base
 * 文件名：daxueke
 * 描述：所有Activity的基类，用来处理一些公共事件，如：数据统计
 */
public class BaseActivity extends AppCompatActivity {
    public String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getComponentName().getShortClassName();
        getSupportActionBar().setElevation(0);
        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 改变状态栏颜色
     *
     * @param color
     */
    public void changeStatusBarColor(@ColorRes int color) {
        Util.setStatusBarColor(this, color);
    }
    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://android自带的id
                finish();//finish（）就是直接返回了
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
