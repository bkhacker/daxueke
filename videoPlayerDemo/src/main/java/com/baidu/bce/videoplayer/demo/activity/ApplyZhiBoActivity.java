package com.baidu.bce.videoplayer.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.base.BaseActivity;

/**
 * 作者：yeweizheng on 2017/3/26 16:18
 * 包名：com.baidu.bce.videoplayer.demo.activity
 * 文件名：daxueke
 * 描述：
 */
public class ApplyZhiBoActivity extends BaseActivity implements View.OnClickListener {
    private EditText minezhibotitle;
    private EditText minezhiboteachername;
    private EditText minezhibodesc;
    private EditText minezhiboemail;
    private Button btnzhiboapply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyzhibo);
        initView();
    }

    private void initView() {
        btnzhiboapply = (Button) findViewById(R.id.btn_zhibo_apply);
        btnzhiboapply.setOnClickListener(this);
        minezhiboemail = (EditText) findViewById(R.id.mine_zhibo_email);
        minezhibodesc = (EditText) findViewById(R.id.mine_zhibo_desc);
        minezhiboteachername = (EditText) findViewById(R.id.mine_zhibo_teachername);
        minezhibotitle = (EditText) findViewById(R.id.mine_zhibo_title);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zhibo_apply:
                String title = minezhibotitle.getText().toString().trim();
                String teacherName = minezhiboteachername.getText().toString().trim();
                String desc = minezhibodesc.getText().toString().trim();
                String email = minezhiboemail.getText().toString().trim();



                break;
        }
    }
}
