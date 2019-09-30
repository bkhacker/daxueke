package com.baidu.bce.videoplayer.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.base.BaseActivity;
import com.baidu.bce.videoplayer.demo.entity.RecomPartJobEntity;
import com.baidu.bce.videoplayer.demo.entity.RecommendDocEntity;
import com.baidu.bce.videoplayer.demo.entity.RecommendVideoEntity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 作者：yeweizheng on 2017/3/26 16:24
 * 包名：com.baidu.bce.videoplayer.demo.activity
 * 文件名：daxueke
 * 描述：推荐资料 已完成
 */
public class RecommendActivity extends BaseActivity implements View.OnClickListener {
    private EditText recommenddoctitle;
    private EditText recommenddocauthor;
    private EditText recommenddocdesc;
    private EditText recommenddocurl;
    private Button btnrecommenddoc;

    private EditText recommendvideotitle;
    private EditText recommendvideoauthor;
    private EditText recommendvideodesc;
    private EditText recommendvideourl;
    private Button btnrecommendvideo;

    private EditText recommendjobtitle;
    private EditText recommendjobsalary;
    private EditText recommendjobdesc;
    private EditText recommendjobaddress;
    private Button btnrecommendjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        initView();

    }

    private void initView() {
        btnrecommenddoc = (Button) findViewById(R.id.btn_recommend_doc);
        btnrecommenddoc.setOnClickListener(this);
        recommenddocurl = (EditText) findViewById(R.id.recommend_doc_url);
        recommenddocdesc = (EditText) findViewById(R.id.recommend_doc_desc);
        recommenddocauthor = (EditText) findViewById(R.id.recommend_doc_author);
        recommenddoctitle = (EditText) findViewById(R.id.recommend_doc_title);

        btnrecommendvideo = (Button) findViewById(R.id.btn_recommend_video);
        btnrecommendvideo.setOnClickListener(this);
        recommendvideourl = (EditText) findViewById(R.id.recommend_video_url);
        recommendvideodesc = (EditText) findViewById(R.id.recommend_video_desc);
        recommendvideoauthor = (EditText) findViewById(R.id.recommend_video_author);
        recommendvideotitle = (EditText) findViewById(R.id.recommend_video_title);

        btnrecommendjob = (Button) findViewById(R.id.btn_recommend_job);
        btnrecommendjob.setOnClickListener(this);
        recommendjobaddress = (EditText) findViewById(R.id.recommend_job_address);
        recommendjobdesc = (EditText) findViewById(R.id.recommend_job_desc);
        recommendjobsalary = (EditText) findViewById(R.id.recommend_job_salary);
        recommendjobtitle = (EditText) findViewById(R.id.recommend_job_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recommend_video:
                String url = recommendvideourl.getText().toString();
                String desc = recommendvideodesc.getText().toString();
                String author = recommendvideoauthor.getText().toString();
                String title = recommendvideotitle.getText().toString();
                //2.判断是否为空 !TextUtils.isEmpty(username) &
                if (!TextUtils.isEmpty(url) & !TextUtils.isEmpty(desc) & !TextUtils.isEmpty(author) & !TextUtils.isEmpty(title)) {
                    RecommendDocEntity doc = new RecommendDocEntity();
                    doc.setTitle(title);
                    doc.setAuthor(author);
                    doc.setUrl(url);
                    doc.setDesc(desc);
                    doc.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                btnrecommendvideo.setVisibility(View.GONE);
                                Toast.makeText(RecommendActivity.this, "推荐成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RecommendActivity.this, "推荐失败，系统异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;

            case R.id.btn_recommend_doc:
                String url2 = recommenddocurl.getText().toString();
                String desc2 = recommenddocdesc.getText().toString();
                String author2 = recommenddocauthor.getText().toString();
                String title2 = recommenddoctitle.getText().toString();
                //2.判断是否为空 !TextUtils.isEmpty(username) &
                if (!TextUtils.isEmpty(url2) & !TextUtils.isEmpty(desc2) & !TextUtils.isEmpty(author2) & !TextUtils.isEmpty(title2)) {
                    RecommendVideoEntity doc = new RecommendVideoEntity();
                    doc.setTitle(title2);
                    doc.setAuthor(author2);
                    doc.setUrl(url2);
                    doc.setDesc(desc2);
                    doc.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                btnrecommenddoc.setVisibility(View.GONE);
                                Toast.makeText(RecommendActivity.this, "推荐成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RecommendActivity.this, "推荐失败，系统异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.btn_recommend_job:
                String address = recommendjobaddress.getText().toString();
                String desc3 = recommendjobdesc.getText().toString();
                String salary = recommendjobsalary.getText().toString();
                String title3 = recommendjobtitle.getText().toString();
                //2.判断是否为空 !TextUtils.isEmpty(username) &
                if (!TextUtils.isEmpty(address) & !TextUtils.isEmpty(desc3) & !TextUtils.isEmpty(salary) & !TextUtils.isEmpty(title3)) {
                    RecomPartJobEntity job = new RecomPartJobEntity();
                    job.setTitle(title3);
                    job.setAddress(address);
                    job.setDesc(desc3);
                    job.setSalary(salary);
                    job.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                btnrecommendvideo.setVisibility(View.GONE);
                                Toast.makeText(RecommendActivity.this, "推荐成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RecommendActivity.this, "推荐失败，系统异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }


    }


}
