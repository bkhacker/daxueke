package com.baidu.bce.videoplayer.demo.activity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.info.DownloadObserverManager;
import com.baidu.bce.videoplayer.demo.info.ListViewItemProgressListener;
import com.baidu.bce.videoplayer.demo.info.SampleObserver;
import com.baidu.bce.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.bce.videoplayer.demo.info.VideoInfo;
import com.baidu.bce.videoplayer.demo.view.CustomAlertWindow;
import com.baidu.cyberplayer.download.AbstractDownloadableVideoItem;
import com.baidu.cyberplayer.download.AbstractDownloadableVideoItem.DownloadStatus;
import com.baidu.cyberplayer.download.VideoDownloadManager;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *缓存列表Activity
 */
public class CacheListActivity extends AppCompatActivity {
    private static final String TAG = "CacheListActivity";
    private ListView listView;
    private ArrayList<VideoInfo> listData = new ArrayList<VideoInfo>();
    
    Handler handler = new Handler(Looper.getMainLooper());
    private VideoDownloadManager downloadManagerInstance;
    RelativeLayout rlEmpty;
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(0xfff7f7f7);
        }
        setContentView(R.layout.activity_cache_list);
        downloadManagerInstance = VideoDownloadManager.getInstance(CacheListActivity.this,
                MainActivity.SAMPLE_USER_NAME);

        listView = (ListView) this.findViewById(R.id.lv_video_list);
        listView.setAdapter(adapter);
        rlEmpty = (RelativeLayout) this.findViewById(R.id.rl_cachelist_empty);
        initOtherUI();
    }

    private void initOtherUI() {
        final ImageButton ibBack = (ImageButton) this.findViewById(R.id.ibtn_back);
        ibBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        RelativeLayout rlback = (RelativeLayout) this.findViewById(R.id.rl_back);
        rlback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ibBack.performClick();
            }

        });
    }

    @Override
    protected void onResume() {
        refreshData();
        super.onResume();

    }

    public void refreshData() {
        listData = SharedPrefsStore.getAllCacheVideoFromSP(this);
        adapter.notifyDataSetChanged();
        if (listData.size() < 1) {
            // show empty info
            rlEmpty.setVisibility(View.VISIBLE);
        } else {
            rlEmpty.setVisibility(View.GONE);
        }
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(CacheListActivity.this);
            }
            final ListViewItemProgressListener listener;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_of_list_cache, null);
            }
            final VideoInfo info = listData.get(position);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
            final TextView tvDesc = (TextView) convertView.findViewById(R.id.tv_item_desc);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            if (info.getImageUrl() != null && !info.getImageUrl().equals("")) {
                // fetch image from assets
                // if your image from url, you need to fetch image async
                InputStream ims;
                try {
                    ims = getAssets().open(info.getImageUrl());
                    Drawable iconDrawable = Drawable.createFromStream(ims, null);
                    ivIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } else {
                ivIcon.setImageResource(R.drawable.item_default_icon);
            }
            ImageButton ibtnDelete = (ImageButton) convertView.findViewById(R.id.ibtn_item_delete);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.pb_progress);
            final AbstractDownloadableVideoItem downItem = downloadManagerInstance
                    .getDownloadableVideoItemByUrl(info.getUrl());
            final SampleObserver obs = DownloadObserverManager.getExistObserver(info.getUrl());
            
            listener = new ListViewItemProgressListener() {

                @Override
                public void onStatusUpdate() {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            updateItemUI(progressBar, tvDesc, downItem);
                        }                        
                    });
                }
                
            };

            listener.setUrl(info.getUrl());
            if (obs != null) {
                obs.setListener(new WeakReference<ListViewItemProgressListener>(listener));
            }
            tvTitle.setText(info.getTitle());
            
            progressBar.setProgress((int) downItem.getProgress());
            tvDesc.setText(SampleObserver.getStatusForUI(downItem.getStatus()));
            ibtnDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String msgString = "确定要清除<" + info.getTitle() + ">这个视频资源的所有本地数据嘛？";
                    String yesString = "确定";
                    String noString = "取消";
                    View.OnClickListener yesListener = new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            SharedPrefsStore.deleteCacheVideo(CacheListActivity.this, info);
                            downloadManagerInstance.deleteDownloader(info.getUrl());
                            refreshData();
                        }
                    };
                    CustomAlertWindow.showAlertWindow(CacheListActivity.this, msgString, yesString, noString,
                            yesListener, null);
                }

            });
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (downItem.getStatus() == DownloadStatus.COMPLETED) {
                        Intent intent = null;
                        boolean isSimple = SharedPrefsStore.isControllBarSimple(CacheListActivity.this);
                        if (isSimple) {
                            // SimplePlayActivity简易播放窗口，便于快速了解播放流程
                            intent = new Intent(CacheListActivity.this, SimplePlayActivity.class);
                        } else {
                            // AdvancedPlayActivity高级播放窗口，内含丰富的播放控制逻辑
                            intent = new Intent(CacheListActivity.this, AdvancedPlayActivity.class);
                        }
                        info.setUrl("file://" + downItem.getLocalAbsolutePath()); // 播放本地
                        intent.putExtra("videoInfo", info);
                        startActivity(intent);
                    } else if (downItem.getStatus() == DownloadStatus.DOWNLOADING
                            || downItem.getStatus() == DownloadStatus.PENDING) {
                        downloadManagerInstance.pauseDownloader(downItem.getUrl());
                    } else {
                        // start to play
                        if (obs != null) {
                            downloadManagerInstance.startOrResumeDownloader(downItem.getUrl(), obs);
                        } else {
                            SampleObserver newObs = new SampleObserver();
                            DownloadObserverManager.addNewObserver(info.getUrl(), newObs);
                            newObs.setListener(new WeakReference<ListViewItemProgressListener>(listener));
                            downloadManagerInstance.startOrResumeDownloader(downItem.getUrl(), newObs);
                        }
                        
                    }

                }

            });
            return convertView;
        }
        
        public void updateItemUI(ProgressBar bar, TextView descTv, AbstractDownloadableVideoItem item) {
            bar.setProgress((int) item.getProgress());
            descTv.setText(SampleObserver.getStatusForUI(item.getStatus()));
        }
    };
}
