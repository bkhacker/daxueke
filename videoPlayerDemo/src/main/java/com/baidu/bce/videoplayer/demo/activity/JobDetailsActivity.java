package com.baidu.bce.videoplayer.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.base.BaseActivity;

/**
 * 作者：yeweizheng on 2017/3/31 19:17
 * 包名：com.baidu.bce.videoplayer.demo.activity
 * 文件名：daxueke
 * 描述：实习职位详情Activity
 */
public class JobDetailsActivity extends BaseActivity {
    private TextView tvjobdetailsaddress;
    private TextView tvjobdetailsdesc;
    private TextView tvjobdetailssalary;
    private TextView tvjobdetailsname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdetails);
        initView();
    }



    //初始化
    private void initView() {
        tvjobdetailsaddress = (TextView) findViewById(R.id.tv_jobdetails_address2);
        tvjobdetailsdesc = (TextView) findViewById(R.id.tv_jobdetails_desc);
        tvjobdetailssalary = (TextView) findViewById(R.id.tv_jobdetails_salary);
        tvjobdetailsname = (TextView) findViewById(R.id.tv_jobdetails_name);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String salary = intent.getStringExtra("salary");
        String desc = intent.getStringExtra("desc");
        String address = intent.getStringExtra("address");
        //设置标题
        //getSupportActionBar().setTitle(name);
        tvjobdetailsaddress.setText(address);
        tvjobdetailssalary.setText(salary);
        tvjobdetailsname.setText(title);
        tvjobdetailsdesc.setText(desc);

    }
}
