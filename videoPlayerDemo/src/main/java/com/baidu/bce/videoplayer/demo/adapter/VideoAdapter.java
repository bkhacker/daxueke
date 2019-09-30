package com.baidu.bce.videoplayer.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.activity.AdvancedPlayActivity;
import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.SimplePlayActivity;
import com.baidu.bce.videoplayer.demo.info.DownloadObserverManager;
import com.baidu.bce.videoplayer.demo.info.SampleObserver;
import com.baidu.bce.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.bce.videoplayer.demo.info.VideoInfo;
import com.baidu.cyberplayer.download.AbstractDownloadableVideoItem;
import com.baidu.cyberplayer.download.VideoDownloadManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 作者：yeweizheng on 2017/3/25 18:20
 * 包名：com.baidu.bce.videoplayer.demo.adapter
 * 文件名：daxueke
 * 描述：
 */
public class VideoAdapter extends BaseAdapter {
    VideoDownloadManager downloadManagerInstance;
    private Context mContext;
    private LayoutInflater inflater;
    private List<VideoInfo> mList;
    private VideoInfo data;
    private int width, height;
    private WindowManager wm;

    public VideoAdapter(Context mContext, List<VideoInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_of_list_video, null);
        }
        final VideoInfo info = mList.get(position);
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
        ibtnDelete.setVisibility(View.INVISIBLE);
    /*   if (info.isCanDelete()) {
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
        }*/

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = null;
                boolean isSimple = SharedPrefsStore.isControllBarSimple(mContext);
                if (isSimple) {
                    // SimplePlayActivity简易播放窗口，便于快速了解播放流程
                    intent = new Intent(mContext, SimplePlayActivity.class);
                } else {
                    // AdvancedPlayActivity高级播放窗口，内含丰富的播放控制逻辑
                    intent = new Intent(mContext, AdvancedPlayActivity.class);
                }
                intent.putExtra("videoInfo", info);
               // startActivity(intent);
                mContext.startActivity(intent);
            }

        });

        if (info.getUrl() != null && info.getUrl().endsWith(".m3u8")) {
            ibtnDownload.setVisibility(View.GONE);
            ibtnDownload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //
                    AbstractDownloadableVideoItem item = downloadManagerInstance
                            .getDownloadableVideoItemByUrl(info.getUrl());
                    if (item != null) {
                        // already
                        Toast.makeText(mContext, "该资源已经在缓存列表，请点击右上角「本地缓存」查看", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPrefsStore.addToCacheVideo(mContext, info);
                        SampleObserver sampleObs = new SampleObserver();
                        DownloadObserverManager.addNewObserver(info.getUrl(), sampleObs);
                        downloadManagerInstance.startOrResumeDownloader(info.getUrl(), sampleObs);
                        Toast.makeText(mContext, "开始缓存，请点击右上角「本地缓存」查看进度", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        } else {
            ibtnDownload.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
}
