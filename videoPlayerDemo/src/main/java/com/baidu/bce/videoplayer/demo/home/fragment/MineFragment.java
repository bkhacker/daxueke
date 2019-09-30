package com.baidu.bce.videoplayer.demo.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.activity.CacheListActivity;
import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.ApplyZhiBoActivity;
import com.baidu.bce.videoplayer.demo.activity.DocCacheListActivity;
import com.baidu.bce.videoplayer.demo.activity.EditMineActivity;
import com.baidu.bce.videoplayer.demo.activity.LoginActivity;
import com.baidu.bce.videoplayer.demo.activity.PushActivity;
import com.baidu.bce.videoplayer.demo.activity.RecommendActivity;
import com.baidu.bce.videoplayer.demo.entity.MyUser;
import com.baidu.bce.videoplayer.demo.home.BaseFragment;

import cn.bmob.v3.BmobUser;


/**
 * 作者：yeweizheng on 2017/3/1 22:34
 * 包名：youdu.com.daxueke.home.fragment
 * 文件名：daxueke
 * 描述：
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    /**
     * UI
     */
    private View mContentView;
    private TextView login_info_view;//登陆信息
    private TextView mine_info_edit;//资料修改
    private TextView teacher_zhibo;//教师直播
    private TextView apply_zhibo;//申请直播
    private TextView mine_tuijiansp;//推荐视频
    private TextView mine_tuijianzl;//推荐资料
    private TextView mine_tuijiansx;//推荐实习
    private TextView mine_videocache;//视频缓存
    private TextView mine_doccache;//文档缓存
    private Button exit_login;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_mine_layout, null, false);
        initView();
        return mContentView;
    }

    private void initView() {

        login_info_view = (TextView) mContentView.findViewById(R.id.login_info_view);
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        login_info_view.setText("欢迎您：" + userInfo.getUsername());

        mine_info_edit = (TextView) mContentView.findViewById(R.id.mine_info_edit);
        mine_info_edit.setOnClickListener(this);

        teacher_zhibo = (TextView) mContentView.findViewById(R.id.teacher_zhibo);
        teacher_zhibo.setOnClickListener(this);

        apply_zhibo = (TextView) mContentView.findViewById(R.id.apply_zhibo);
        apply_zhibo.setOnClickListener(this);


        mine_tuijianzl = (TextView) mContentView.findViewById(R.id.mine_tuijianzl);
        mine_tuijianzl.setOnClickListener(this);



        exit_login = (Button) mContentView.findViewById(R.id.exit_login);
        exit_login.setOnClickListener(this);

        mine_videocache = (TextView) mContentView.findViewById(R.id.mine_videocache);
        mine_videocache.setOnClickListener(this);
        mine_doccache = (TextView) mContentView.findViewById(R.id.mine_doccache);
        mine_doccache.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_zhibo:
                startActivity(new Intent(getActivity(), PushActivity.class));
                break;

            case R.id.exit_login:
                //清除缓存用户对象
                BmobUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = BmobUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.mine_info_edit:
                startActivity(new Intent(getActivity(), EditMineActivity.class));
                break;
            case R.id.apply_zhibo:
                startActivity(new Intent(getActivity(), ApplyZhiBoActivity.class));
                break;
            case R.id.mine_tuijianzl:
                startActivity(new Intent(getActivity(), RecommendActivity.class));
                break;
            case R.id.mine_videocache:
                startActivity(new Intent(getActivity(), CacheListActivity.class));
                break;
            case R.id.mine_doccache:
                startActivity(new Intent(getActivity(), DocCacheListActivity.class));
                break;


        }
    }
}
