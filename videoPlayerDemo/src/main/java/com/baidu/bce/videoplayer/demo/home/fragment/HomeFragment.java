package com.baidu.bce.videoplayer.demo.home.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.adapter.HomeAdapter;

import com.baidu.bce.videoplayer.demo.entity.HomeEntity;
import com.baidu.bce.videoplayer.demo.entity.LunBoEntity;
import com.baidu.bce.videoplayer.demo.home.BaseFragment;
import com.baidu.bce.videoplayer.demo.loader.GlideImageLoader;
import com.baidu.bce.videoplayer.demo.util.L;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 作者：yeweizheng on 2017/3/1 22:02
 * 包名：youdu.com.daxueke.home.fragment
 * 文件名：daxueke
 * 描述：首页Fragment
 */
public class HomeFragment extends BaseFragment {
    /**
     * UI
     */
    //private View mContentView;
    private ImageView mLoadingView;//加载
    private TextView title_view;//标题
    private ListView list_view;
    private TextView titleview;
    private ImageView loadingview;
    private ListView listview;

    private RelativeLayout fanqiezhong_layout_view;
    private RelativeLayout zuixincourse_layout_view;
    private RelativeLayout remenpartjob_layout_view;
    private RelativeLayout wendangziliao_layout_view;

    private TextView fanqiezhong_image_view;
    private TextView zuixincourse_image_view;
    private TextView remenpartjob_image_view;
    private TextView wendangziliao_image_view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);


        initView(mContentView);
        return mContentView;
    }

    private void initView(final View mContentView) {


        BmobQuery<LunBoEntity> queryDuoTu = new BmobQuery<LunBoEntity>();

//返回50条数据，如果不加上这条语句，默认返回10条数据
        queryDuoTu.setLimit(50);
//执行查询方法
        queryDuoTu.findObjects(new FindListener<LunBoEntity>() {
            @Override
            public void done(List<LunBoEntity> list, BmobException e) {
                if (e == null) {
                    //多图轮播
                    Banner banner = (Banner) mContentView.findViewById(R.id.banner);
                    //设置图片加载器
                    banner.setImageLoader(new GlideImageLoader());
                    //设置图片集合
                    List urlList = new ArrayList();
                    for(LunBoEntity image:list){
                        String url=image.getImageUrl();
                        urlList.add(url);
                    }
                    banner.setImages(urlList);
                    //banner设置方法全部调用完毕时最后调用
                    banner.start();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

      /*  //多图轮播
        Banner banner = (Banner) mContentView.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List list = new ArrayList();
        list.add("http://img03.51jobcdn.com/im/images/2017/bj/zhongyi0411a_7179wh.gif");
        list.add("http://img03.51jobcdn.com/im/images/2017/bj/zhongyi0411a_7179wh.gif");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492236842529&di=a705bf7b03a7bddd544ec13f697b74e6&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F18%2F62%2F50%2F45q58PICGfv_1024.jpg");
        list.add("http://img03.51jobcdn.com/im/images/2017/sh/shangjiao0317_7147wh.gif");
        banner.setImages(list);
        //banner设置方法全部调用完毕时最后调用
        banner.start();*/

        //四个按钮
        fanqiezhong_layout_view = (RelativeLayout) mContentView.findViewById(R.id.fanqiezhong_layout_view);
        zuixincourse_layout_view = (RelativeLayout) mContentView.findViewById(R.id.zuixincourse_layout_view);
        remenpartjob_layout_view = (RelativeLayout) mContentView.findViewById(R.id.remenpartjob_layout_view);
        wendangziliao_layout_view = (RelativeLayout) mContentView.findViewById(R.id.wendangziliao_layout_view);

        fanqiezhong_image_view = (TextView) mContentView.findViewById(R.id.fanqiezhong_image_view);
        zuixincourse_image_view = (TextView) mContentView.findViewById(R.id.zuixincourse_image_view);
        remenpartjob_image_view = (TextView) mContentView.findViewById(R.id.remenpartjob_image_view);
        wendangziliao_image_view = (TextView) mContentView.findViewById(R.id.wendangziliao_image_view);


        list_view = (ListView) mContentView.findViewById(R.id.list_view);
        BmobQuery<HomeEntity> query = new BmobQuery<HomeEntity>();
        query.setLimit(50);
        query.findObjects(new FindListener<HomeEntity>() {
            @Override
            public void done(List<HomeEntity> mList, BmobException e) {
                if (e == null) {
                    HomeAdapter adapter = new HomeAdapter(getActivity(), mList);
                    list_view.setAdapter(adapter);

                    L.i("查询成功：共" + mList.size() + "条数据。");
                    for (HomeEntity home : mList) {
                        //获得playerName的信息
                        String title = home.getTitle();
                        L.i("查询成功：名字为：" + title);
                        //获得数据的objectId信息
                        // gameScore.getObjectId();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        // gameScore.getCreatedAt();
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }
}
