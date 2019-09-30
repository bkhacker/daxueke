package com.baidu.bce.videoplayer.demo.home.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.home.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：yeweizheng on 2017/3/1 22:34
 * 包名：youdu.com.daxueke.home.fragment
 * 文件名：daxueke
 * 描述：
 */
public class CourseFragment extends BaseFragment {
    /**
     * UI
     */
    private View mContentView;


    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragement
    private List<Fragment> mFragement;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_course_layout, null, false);
        initData();
        initView();
        return mContentView;
    }

    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add("正在直播");
        mTitle.add("辅导视频");
        mTitle.add("文档资料");


        mFragement = new ArrayList<>();
        mFragement.add(new CourseZhiBoFragment());
        mFragement.add(new CourseVideoFragment());
        mFragement.add(new CourseDocFragment());
    }

    private void initView() {

        mTabLayout = (TabLayout)mContentView.findViewById(R.id.mTablayout);
        mViewPager = (ViewPager) mContentView.findViewById(R.id.mViewPager);
        //预加载
        mViewPager.setOffscreenPageLimit(mFragement.size());
        //mViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("TAG", "position:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragement.get(position);
            }

            //返回Item的个数
            @Override
            public int getCount() {
                return mFragement.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);


    }


}