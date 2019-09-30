package com.baidu.bce.videoplayer.demo.activity;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.base.BaseActivity;
import com.baidu.bce.videoplayer.demo.home.fragment.CourseFragment;
import com.baidu.bce.videoplayer.demo.home.fragment.HomeFragment;
import com.baidu.bce.videoplayer.demo.home.fragment.MineFragment;
import com.baidu.bce.videoplayer.demo.home.fragment.PartJobFragment;
import com.baidu.bce.videoplayer.demo.util.Util;


/**
 * 作者：yeweizheng on 2017/3/1 22:11
 * 包名：youdu.com.daxueke.activity
 * 文件名：daxueke
 * 描述：
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private long mLastPressBackTime = 0;
    private static final int INTERVAL_OF_TWO_CLICK_TO_QUIT = 1000; // 1 seconde

    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private Fragment mCommonFragmentOne;
    private CourseFragment mCourseFragment;
    private MineFragment mMineFragment;
    private PartJobFragment mPartJobFragment;
    private Fragment mCurrent;

    private RelativeLayout mHomeLayout;
    private RelativeLayout mCourseLayout;
    private RelativeLayout mPartJobLayout;
    private RelativeLayout mMineLayout;

    private TextView mHomeView;
    private TextView mCoursedView;
    private TextView mPartJobView;
    private TextView mMineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        //初始化
        initView();
        mHomeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, mHomeFragment);
        fragmentTransaction.commit();
    }

    private void initView() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mCourseLayout = (RelativeLayout) findViewById(R.id.course_layout_view);
        mCourseLayout.setOnClickListener(this);
        mPartJobLayout = (RelativeLayout) findViewById(R.id.partjob_layout_view);
        mPartJobLayout.setOnClickListener(this);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);
        mMineLayout.setOnClickListener(this);

        mHomeView = (TextView) findViewById(R.id.home_image_view);
        mCoursedView = (TextView) findViewById(R.id.course_image_view);
        mPartJobView = (TextView) findViewById(R.id.partjob_image_view);
        mMineView = (TextView) findViewById(R.id.mine_image_view);
        mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
    }

    /**
     * 隐藏fragment
     *
     * @param fragment
     * @param ft
     */
    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (v.getId()) {
            /*首页*/
            case R.id.home_layout_view:
                changeStatusBarColor(R.color.color_fed952);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home_selected);
                mCoursedView.setBackgroundResource(R.drawable.comui_tab_kecheng);
                mPartJobView.setBackgroundResource(R.drawable.comui_tab_shixi);
                mMineView.setBackgroundResource(R.drawable.comui_tab_mine);

                hideFragment(mCommonFragmentOne, fragmentTransaction);
                hideFragment(mCourseFragment, fragmentTransaction);
                hideFragment(mPartJobFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                } else {
                    mCurrent = mHomeFragment;
                    fragmentTransaction.show(mHomeFragment);
                }
                break;

            /*课程*/
            case R.id.course_layout_view:
                changeStatusBarColor(R.color.color_e3e3e3);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
                mCoursedView.setBackgroundResource(R.drawable.comui_tab_kecheng_selected);
                mPartJobView.setBackgroundResource(R.drawable.comui_tab_shixi);
                mMineView.setBackgroundResource(R.drawable.comui_tab_mine);

                hideFragment(mCommonFragmentOne, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mPartJobFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mCourseFragment == null) {
                    mCourseFragment = new CourseFragment();
                    fragmentTransaction.add(R.id.content_layout, mCourseFragment);
                } else {
                    mCurrent = mCourseFragment;
                    fragmentTransaction.show(mCourseFragment);
                }
                break;

           /*实习*/
            case R.id.partjob_layout_view:
                changeStatusBarColor(R.color.color_e3e3e3);
                mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
                mCoursedView.setBackgroundResource(R.drawable.comui_tab_kecheng);
                mPartJobView.setBackgroundResource(R.drawable.comui_tab_shixi_selected);
                mMineView.setBackgroundResource(R.drawable.comui_tab_mine);

                hideFragment(mCommonFragmentOne, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mCourseFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mPartJobFragment == null) {
                    mPartJobFragment = new PartJobFragment();
                    fragmentTransaction.add(R.id.content_layout, mPartJobFragment);
                } else {
                    mCurrent = mPartJobFragment;
                    fragmentTransaction.show(mPartJobFragment);
                }
                break;

            /*我的*/
            case R.id.mine_layout_view:
                changeStatusBarColor(R.color.white);

                mHomeView.setBackgroundResource(R.drawable.comui_tab_home);
                mCoursedView.setBackgroundResource(R.drawable.comui_tab_kecheng);
                mPartJobView.setBackgroundResource(R.drawable.comui_tab_shixi);
                mMineView.setBackgroundResource(R.drawable.comui_tab_mine_selected);

                hideFragment(mCommonFragmentOne, fragmentTransaction);
                hideFragment(mCourseFragment, fragmentTransaction);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mPartJobFragment, fragmentTransaction);

                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mMineFragment);
                } else {
                    mCurrent = mMineFragment;
                    fragmentTransaction.show(mMineFragment);
                }
                break;
        }

        fragmentTransaction.commit();

    }
    public void changeStatusBarColor(@ColorRes int color) {
        Util.setStatusBarColor(this, color);
    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastPressBackTime < INTERVAL_OF_TWO_CLICK_TO_QUIT) {
            finish();
        } else {
            Toast.makeText(this, "再次按下返回键将退出应用！", Toast.LENGTH_SHORT).show();
            mLastPressBackTime = System.currentTimeMillis();
        }
    }
}
