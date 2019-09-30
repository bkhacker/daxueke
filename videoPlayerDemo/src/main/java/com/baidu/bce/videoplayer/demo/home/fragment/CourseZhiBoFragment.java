package com.baidu.bce.videoplayer.demo.home.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.AdvancedPlayActivity;
import com.baidu.bce.videoplayer.demo.activity.MainActivity;
import com.baidu.bce.videoplayer.demo.activity.SimplePlayActivity;
import com.baidu.bce.videoplayer.demo.adapter.VideoAdapter;
import com.baidu.bce.videoplayer.demo.adapter.ZhiBoAdapter;
import com.baidu.bce.videoplayer.demo.entity.ZhiboInfo;
import com.baidu.bce.videoplayer.demo.home.BaseFragment;
import com.baidu.bce.videoplayer.demo.info.DownloadObserverManager;
import com.baidu.bce.videoplayer.demo.info.SampleObserver;
import com.baidu.bce.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.bce.videoplayer.demo.info.VideoInfo;
import com.baidu.bce.videoplayer.demo.view.CustomAlertWindow;
import com.baidu.cyberplayer.download.AbstractDownloadableVideoItem;
import com.baidu.cyberplayer.download.VideoDownloadManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者：yeweizheng on 2017/3/23 09:11
 * 包名：com.baidu.bce.videoplayer.demo.home.fragment
 * 文件名：daxueke
 * 描述：
 */
public class CourseZhiBoFragment  extends BaseFragment{


  /*  *//**
     * UI
     *//*
    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_partjob_layout, null, false);
        initView();
        return mContentView;
    }

    private void initView() {

        //partjob_listview = (ListView) mContentView.findViewById(R.id.partjob_listview);


    }*/

    private static final String TAG = "MainActivity";
    private ListView listView;
    private static ArrayList<ZhiboInfo> listData = new ArrayList<ZhiboInfo>();

    public static final String SAMPLE_USER_NAME = "sampleUser";
    VideoDownloadManager downloadManagerInstance;


    private View mContentView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(0xfff7f7f7);
        }
        mContentView = inflater.inflate(R.layout.fragment_zhibo, null, false);
        downloadManagerInstance = VideoDownloadManager.getInstance(getActivity(), MainActivity.SAMPLE_USER_NAME);

        listView = (ListView) mContentView.findViewById(R.id.fragment_lv_zhibo_list);
        listView.setAdapter(adapter);
        initOtherUI();

        return mContentView;
    }


    private void initOtherUI() {
       /* ImageButton ibMore = (ImageButton) mContentView.findViewById(R.id.ibtn_more);
        ibMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
           *//*     MorePopWindow popWindow = new MorePopWindow(getActivity());
                popWindow.showPopupWindow(findViewById(R.id.rl_top_bar));
            *//*}

        });*/
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        refreshData();
        super.onResume();
    }
    /*   @Override
        protected void onResume() {
            Log.d(TAG, "onResume");
            refreshData();
            super.onResume();

        }*/
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ArrayList<ZhiboInfo> listData= (ArrayList<ZhiboInfo>) msg.obj;
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };
    public void refreshData() {
        BmobQuery<ZhiboInfo> query = new BmobQuery<ZhiboInfo>();
        query.setLimit(50);
        query.findObjects(new FindListener<ZhiboInfo>() {
            @Override
            public void done(List<ZhiboInfo> mList, BmobException e) {
                if (e == null) {
                    ZhiBoAdapter adapter=new ZhiBoAdapter(getActivity(), mList);
                    listView.setAdapter(adapter);
                } else {
                    Log.i("bmob", "video失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


        listData = SharedPrefsStore.getAllMainVideoFromSP2(getActivity());
        adapter.notifyDataSetChanged();
    }



    /**
     * 注：以下adapter仅为简单实现，实际项目中需要进行优化
     */
    private BaseAdapter adapter = new BaseAdapter() {

        private LayoutInflater mInflater;

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(getActivity());
            }
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_of_list_video, null);
            }
            final ZhiboInfo info = listData.get(position);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_item_desc);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            ImageButton ibtnDelete = (ImageButton) convertView.findViewById(R.id.ibtn_item_delete);
            ImageButton ibtnDownload = (ImageButton) convertView.findViewById(R.id.ibtn_item_download);

            if (info.getImageUrl() != null && !info.getImageUrl().equals("")) {
                // fetch image from assets
                // if your image from url, you need to fetch image async
                InputStream ims;
                try {
                    ims = mContext.getAssets().open(info.getImageUrl());
                    Drawable iconDrawable = Drawable.createFromStream(ims, null);
                    ivIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                ivIcon.setImageResource(R.drawable.item_default_icon);
            }
            tvTitle.setText(info.getTitle());
            //这是设置描述的
            tvDesc.setText("大学客1.0测试版发布");
            if (info.isCanDelete()) {
                ibtnDelete.setVisibility(View.INVISIBLE);
                ibtnDelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String msgString = "确定要删除<" + info.getTitle() + ">这个视频资源嘛？";
                        String yesString = "确定";
                        String noString = "取消";
                        View.OnClickListener yesListener = new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                SharedPrefsStore.deleteMainVideo(getActivity(), info);
                                refreshData();
                            }
                        };
                        CustomAlertWindow.showAlertWindow(getActivity(), msgString, yesString, noString,
                                yesListener, null);
                    }

                });
            } else {
                ibtnDelete.setVisibility(View.INVISIBLE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    boolean isSimple = SharedPrefsStore.isControllBarSimple(getActivity());
                    if (isSimple) {
                        // SimplePlayActivity简易播放窗口，便于快速了解播放流程
                        intent = new Intent(getActivity(), SimplePlayActivity.class);
                    } else {
                        // AdvancedPlayActivity高级播放窗口，内含丰富的播放控制逻辑
                        intent = new Intent(getActivity(), AdvancedPlayActivity.class);
                    }
                    intent.putExtra("videoInfo", info);
                    startActivity(intent);
                }

            });

            if (info.getUrl() != null && info.getUrl().endsWith(".m3u8")) {
                ibtnDownload.setVisibility(View.VISIBLE);
                ibtnDownload.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //
                        AbstractDownloadableVideoItem item = downloadManagerInstance
                                .getDownloadableVideoItemByUrl(info.getUrl());
                        if (item != null) {
                            // already
                            Toast.makeText(getActivity(), "该资源已经在缓存列表，请点击右上角「本地缓存」查看", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPrefsStore.addToCacheVideo(getActivity(), info);
                            SampleObserver sampleObs = new SampleObserver();
                            DownloadObserverManager.addNewObserver(info.getUrl(), sampleObs);
                            downloadManagerInstance.startOrResumeDownloader(info.getUrl(), sampleObs);
                            Toast.makeText(getActivity(), "开始缓存，请点击右上角「本地缓存」查看进度", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            } else {
                ibtnDownload.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

    };


}
