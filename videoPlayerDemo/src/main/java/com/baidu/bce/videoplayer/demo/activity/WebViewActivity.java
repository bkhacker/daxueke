package com.baidu.bce.videoplayer.demo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.base.BaseActivity;
import com.baidu.bce.videoplayer.demo.util.L;


import java.io.File;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;


/**
 * 作者：yeweizheng on 2017/2/17 20:59
 * 包名：smartbutler.imooc.com.smartbutler.ui
 * 文件名：SmartButler
 * 描述：新闻详情
 */
public class WebViewActivity extends BaseActivity {
    /* SD卡根目录 */
    private File rootDie;

    //进度
    private ProgressBar mProgressBar;
    //网页
    private WebView mWebView;
    //下载按钮
    private Button btn_doc_download;


    private String path;
    private String downUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    //初始化View
    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mWebView = (WebView) findViewById(R.id.mWebView);
        btn_doc_download = (Button) findViewById(R.id.btn_doc_download);
        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        final String url1 = intent.getStringExtra("url");
        final String url =  url1;
        final BmobFile file = (BmobFile) intent.getSerializableExtra("file");
        final String downUrl = intent.getStringExtra("downUrl");
        //设置标题
        getSupportActionBar().setTitle(title);

        //进行加载网页的逻辑
        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);
        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //我接受这个事件
                return true;
            }
        });


        //文档下载

        btn_doc_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MyLoadAsyncTask().execute(title, downUrl);
            }
        });

    }


    public class WebViewClient extends WebChromeClient {

        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}


class MyLoadAsyncTask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {

        BmobFile bmobfile = new BmobFile(params[0], "", params[1]);
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bmob/",params[0]);
        bmobfile.download(saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {

                    Log.i("bmob", "下载成功：" + savePath + ",");
                } else {

                    Log.i("bmob", "下载失败：" + e + ",");
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
            }
        });

        return null;

    }
}