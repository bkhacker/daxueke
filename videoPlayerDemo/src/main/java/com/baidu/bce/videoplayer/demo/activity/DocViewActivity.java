package com.baidu.bce.videoplayer.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bdocreader.BDocInfo;
import com.baidu.bdocreader.BDocView;

/**
 * 作者：yeweizheng on 2017/4/23 09:42
 * 包名：com.baidu.bce.videoplayer.demo.activity
 * 文件名：daxueke
 * 描述：
 */
public class DocViewActivity extends AppCompatActivity{

    BDocView mDocView;
    float currentSize = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.doc_view_activity);
        final BDocInfo docInfo = this.getIntent().getParcelableExtra("ONE_DOC");
        final Handler mHandler = new Handler();
        mDocView = (BDocView) findViewById(R.id.dv_doc);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);

        try {
            mDocView.setOnDocLoadStateListener(new BDocView.OnDocLoadStateListener() {
                @Override
                public void onDocLoadComplete() {
                    Log.d("test", "onDocLoadComplete");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setVisibility(View.GONE);
                            mDocView.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onDocLoadFailed(String errorDesc) {
                    Log.d("test", "onDocLoadFailed errorDesc=" + errorDesc);
                    // errorDesc format: ERROR_XXXX_DESC(code=xxxxx)
                    if (errorDesc.startsWith(BDocView.ERROR_DESC_BDOCINFO_CHECK_FAILED)) {
                        Log.d("test", "bdocInfo is invalid");
                    } else if (errorDesc.startsWith(BDocView.ERROR_DESC_LOAD_RENDER_FALED)) {
                        Log.d("test", "load render failed, please connect to Baidu Cloud");
                    } else if (errorDesc.startsWith(BDocView.ERROR_DESC_RENDER_INTERNAL_ERROR)) {
                        Log.d("test", "render error, may be the token is expired");
                        String code = errorDesc.split("=")[1].replace(")", "");
                        Log.d("test", "render error, code=" + code);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onCurrentPageChanged(int currentPage) {
                    // 记录当前页面
                    Log.i("test", "currentPage = " + currentPage);
                }
            });
            mDocView.loadDoc(docInfo);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // 设置字号大小
        TextView tvBigger = (TextView) findViewById(R.id.tv_bigger);
        TextView tvSmaller = (TextView) findViewById(R.id.tv_smaller);
        tvBigger.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // demo步长0.5f, 实际使用时可根据自己需要设置步长(每次点击的增减值)
                currentSize = Math.min(2.0f, currentSize + 0.5f);
                /**
                 * setFontSize 可以接受float值的取值范围为(0,2]
                 * 即：0到2之间的float值，不包含0，包含2。
                 */
                mDocView.setFontSize(currentSize);
            }

        });
        tvSmaller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // demo步长0.5f, 实际使用时可根据自己需要设置步长(每次点击的增减值)
                currentSize = Math.max(0.5f, currentSize - 0.5f);
                /**
                 * setFontSize 可以接受float值的取值范围为(0,2]
                 * 即：0到2之间的float值，不包含0，包含2。
                 */
                mDocView.setFontSize(currentSize);

            }

        });
    }

}
