package com.baidu.bce.videoplayer.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.base.BaseActivity;
import com.baidu.bce.videoplayer.demo.entity.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.baidu.bce.videoplayer.demo.R.id.et_age;
import static com.baidu.bce.videoplayer.demo.R.id.et_desc;

/**
 * 作者：yeweizheng on 2017/3/26 15:54
 * 包名：com.baidu.bce.videoplayer.demo.activity
 * 文件名：daxueke
 * 描述：资料修改
 */
public class EditMineActivity extends BaseActivity implements View.OnClickListener {
    //private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    private Button btn_update_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmine);
        initView();
    }

    private void initView() {

       // et_username = (EditText) findViewById(R.id.et_username);
        et_sex = (EditText) findViewById(R.id.et_sex);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        btn_update_ok = (Button) findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
       // et_username.setText(userInfo.getUsername());
        et_sex.setText(userInfo.isSex() ? getString(R.string.text_boy) : getString(R.string.text_girl_f));
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_ok:
                //1.拿到输入框的值
               // String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String sex = et_sex.getText().toString();
                String desc = et_desc.getText().toString();
                //2.判断是否为空 !TextUtils.isEmpty(username) &
                if ( !TextUtils.isEmpty(age) & !TextUtils.isEmpty(sex)) {
                    //3.更新属性
                    MyUser user = new MyUser();
                   // user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    //性别
                    if (sex.equals(getString(R.string.text_boy))) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    //简介
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc(desc);
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功

                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(EditMineActivity.this, R.string.text_editor_success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditMineActivity.this, R.string.text_editor_failure, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(EditMineActivity.this, getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
