package com.baidu.bce.videoplayer.demo.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.adapter.HuDongAdapter;
import com.baidu.bce.videoplayer.demo.bar.AdvancedMediaController;
import com.baidu.bce.videoplayer.demo.entity.Chat;
import com.baidu.bce.videoplayer.demo.info.DownloadObserverManager;
import com.baidu.bce.videoplayer.demo.info.SampleObserver;
import com.baidu.bce.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.bce.videoplayer.demo.info.VideoInfo;
import com.baidu.bce.videoplayer.demo.view.FullScreenUtils;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionWithParamListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.download.AbstractDownloadableVideoItem;
import com.baidu.cyberplayer.download.VideoDownloadManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * 完整版播放器Activity   直播
 */
public class AdvancedPlayActivity extends AppCompatActivity implements OnPreparedListener, OnCompletionListener, OnErrorListener,
        OnInfoListener, OnPlayingBufferCacheListener, OnCompletionWithParamListener {
    private static final String TAG = "AdvancedPlayActivity";

    /**
     * 您的AK 请到http://console.bce.baidu.com/iam/#/iam/accesslist获取
     */
    private String ak = ""; // 请录入您的AK !!!

    private VideoInfo info;
    private ArrayList<VideoInfo> playList;
    /**
     * drm版权保护 drmToken的生成与使用方式，请联系百度客服。 使用时，搜索该类中涉及drmToken的地方，酌情修改。
     */
    // private String drmToken =
    // "100a44b2c672011782cc64152dd71b4f9ca7821ea46191ce71e003eb5039468b_vod-gaqrms5ix6xad5yk_1471004113";

    private BVideoView mVV = null;
    private AdvancedMediaController mediaController = null;
    private RelativeLayout headerBar = null;
    private RelativeLayout fullHeaderRl = null;
    private RelativeLayout fullControllerRl = null;
    private RelativeLayout normalHeaderRl = null;
    private RelativeLayout normalControllerRl = null;

    private RelativeLayout mViewHolder = null;

    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    private final Object syncPlaying = new Object();
    private volatile boolean isReadyForQuit = true;
    private Timer barTimer;
    private volatile boolean isFullScreen = false;

    private static final int UI_EVENT_PLAY = 0;

    private Toast toast;
    //互动
    private ListView lv_data;
    private Button btn_send;
    private EditText et_name, et_content;

  private  HuDongAdapter huDongAdapter;
    private List<Chat> messages = new ArrayList<Chat>();
    BmobRealTimeData data = new BmobRealTimeData();


    /**
     * 播放状态
     */
    public enum PlayerStatus {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED, PLAYER_COMPLETED
    }

    private PlayerStatus mPlayerStatus = PlayerStatus.PLAYER_IDLE;

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UI_EVENT_PLAY:

                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */
                    if (mPlayerStatus == PlayerStatus.PLAYER_PREPARING
                            || mPlayerStatus == PlayerStatus.PLAYER_PREPARED) {
                        synchronized (syncPlaying) {
                            try {
                                Log.v(TAG, "waiting for notify invoke or 2s expires");
                                syncPlaying.wait(2 * 1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 设置播放url
                     */

                    // mVV.setVideoPath(info.getUrl(), drmToken);
                    mVV.setVideoPath(info.getUrl());

                    if (SharedPrefsStore.isPlayerFitModeCrapping(getApplicationContext())) {
                        mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    } else {
                        mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    }

                    /**
                     * 续播，如果需要如此
                     */
                    if (mLastPos > 0) {

                        mVV.seekTo(mLastPos);
                        mLastPos = 0;
                    }

                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    mVV.showCacheInfo(true);
                    /**
                     * 开始播放
                     */
                    mVV.start();

                    /**
                     * 已经开启播放，必须有结束消息后才能结束
                     */
                    isReadyForQuit = false;

                    changeStatus(PlayerStatus.PLAYER_PREPARING);
                    break;
                default:
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(0xff282828);
        }
        /**
         * 防闪屏
         */
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.activity_advanced_zhibo_playing);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        info = getIntent().getParcelableExtra("videoInfo");

        initUI();
        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());

        /**
         * 互动
         */
      init();
        et_content = (EditText) findViewById(R.id.et_content);
        lv_data = (ListView) findViewById(R.id.lv_data);


        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser();
                String name = bmobUser.getUsername();
                // String name = et_name.getText().toString();
                String content = et_content.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(content)) {
                    Toast.makeText(AdvancedPlayActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    sendMsg(name, content);
                }
            }
        });


       huDongAdapter = new HuDongAdapter(getApplicationContext(),messages);
        lv_data.setAdapter(huDongAdapter);
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        String name = BmobUser.getCurrentUser().getUsername().toString();
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "用户名和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            sendMsg(name, content);
        }
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (AdvancedMediaController) findViewById(R.id.media_controller_bar);
        fullHeaderRl = (RelativeLayout) findViewById(R.id.rl_fullscreen_header);
        fullControllerRl = (RelativeLayout) findViewById(R.id.rl_fullscreen_controller);
        normalHeaderRl = (RelativeLayout) findViewById(R.id.rl_normalscreen_header);
        normalControllerRl = (RelativeLayout) findViewById(R.id.rl_normalscreen_controller);
        headerBar = (RelativeLayout) findViewById(R.id.rl_header_bar);

        /**
         * 设置ak
         */
        BVideoView.setAK(ak);
        mVV = new BVideoView(this);
        mVV.setLogLevel(0); // 0:No Log; 1:Error; .... ; 4: Debug
        // CustomHttpHeader每加一个字段，就要加一个 \r\n 来换行
//        mVV.setCustomHttpHeader("Referer: http://www.testme.com/test?aa=0" + "\r\n");

        // Getting media-info, as well as the supported resolutions
        tryFetchMediaInfo();

        /**
         * 创建BVideoView和BMediaController
         */

        // mVVCtl = new BMediaController(this);
        mViewHolder.addView(mVV);

        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnCompletionWithParamListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);

        mediaController.setMediaPlayerControl(mVV);
        /**
         * 关联BMediaController
         */
        // mVV.setMediaController(mVVCtl);
        // disable dolby audio effect
        // mVV.setEnableDolby(false);
        /**
         * 设置解码模式 为保证兼容性，当前仅支持软解
         */
        mVV.setDecodeMode(BVideoView.DECODE_SW);
        mVV.setCacheTime(3);
        mVV.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);

        initOtherUI();


    }

    private void initOtherUI() {
        // header
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
        TextView tvTitle = (TextView) this.findViewById(R.id.tv_top_title);
        tvTitle.setText(info.getTitle());
        final ImageButton ibScreen = (ImageButton) this.findViewById(R.id.ibtn_screen_control);
        ibScreen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //这里是全屏按钮
                if (isFullScreen) {//全屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    FullScreenUtils.toggleHideyBar(AdvancedPlayActivity.this);
                    // to mini size, to portrait
                    fullHeaderRl.removeAllViews();
                    fullControllerRl.removeAllViews();
                    normalHeaderRl.addView(headerBar);
                    normalControllerRl.addView(mediaController);
                    isFullScreen = false;
                    ibScreen.setBackgroundResource(R.drawable.btn_to_fullscreen);
                } else {//没有按全屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FullScreenUtils.toggleHideyBar(AdvancedPlayActivity.this);
                    normalHeaderRl.removeAllViews();
                    normalControllerRl.removeAllViews();
                    fullHeaderRl.addView(headerBar);
                    fullControllerRl.addView(mediaController);

                    isFullScreen = true;
                    ibScreen.setBackgroundResource(R.drawable.btn_to_mini);
                    hideOuterAfterFiveSeconds();
                }
            }

        });
        mediaController.setNextListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // fetch the next video. 为了demo的简便性，仅从主页列表获取
                if (playList == null) {
                    playList = SharedPrefsStore.getAllMainVideoFromSP(getApplicationContext());
                }
                int i = 0;
                int length = playList.size();
                for (i = 0; i < length; ++i) {
                    VideoInfo fInfo = playList.get(i);
                    if (fInfo.getUrl().equals(info.getUrl()) && fInfo.getTitle().equals(info.getTitle())) {
                        break;
                    }
                }
                if (i == length - 1) {
                    // is already the last one
                    Toast.makeText(getApplicationContext(), "已经是最后一个", Toast.LENGTH_SHORT).show();
                } else if (i < length - 1) {
                    // set the next info
                    info = playList.get(i + 1);
                    tryToPlayOther();
                    mediaController.clearViewContent();
                } else {
                    // i >= length, should not come in
                    Log.d(TAG, "i >= length, should not come in");
                }
            }

        });
        mediaController.setPreListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // fetch the pre video. 为了demo的简便性，仅从主页列表获取
                if (playList == null) {
                    playList = SharedPrefsStore.getAllMainVideoFromSP(getApplicationContext());
                }
                int i = 0;
                int length = playList.size();
                for (i = 0; i < length; ++i) {
                    VideoInfo fInfo = playList.get(i);
                    if (fInfo.getUrl().equals(info.getUrl()) && fInfo.getTitle().equals(info.getTitle())) {
                        break;
                    }
                }
                if (i == 0) {
                    // is already the first one
                    Toast.makeText(getApplicationContext(), "已经是第一个", Toast.LENGTH_SHORT).show();
                } else if (i < length) {
                    // set the next info
                    info = playList.get(i - 1);
                    tryToPlayOther();
                    mediaController.clearViewContent();
                } else {
                    // i >= length, should not come in
                    Log.d(TAG, "i >= length, should not come in");
                }
            }

        });

        mediaController.setDownloadListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (info.getUrl().startsWith("file://")) {
                    Toast.makeText(AdvancedPlayActivity.this, "该资源已经是本地文件", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!info.getUrl().endsWith(".m3u8")) {
                    Toast.makeText(AdvancedPlayActivity.this, "抱歉，当前仅支持m3u8资源的下载", Toast.LENGTH_SHORT).show();
                    return;
                }
                VideoDownloadManager downloadManagerInstance = VideoDownloadManager
                        .getInstance(AdvancedPlayActivity.this, MainActivity.SAMPLE_USER_NAME);
                AbstractDownloadableVideoItem item = downloadManagerInstance
                        .getDownloadableVideoItemByUrl(info.getUrl());
                if (item != null) {
                    // already
                    Toast.makeText(AdvancedPlayActivity.this, "该资源已经在缓存列表，请到「本地缓存」查看", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPrefsStore.addToCacheVideo(AdvancedPlayActivity.this, info);
                    SampleObserver sampleObs = new SampleObserver();
                    DownloadObserverManager.addNewObserver(info.getUrl(), sampleObs);
                    downloadManagerInstance.startOrResumeDownloader(info.getUrl(), sampleObs);
                    Toast.makeText(AdvancedPlayActivity.this, "开始缓存，可到「本地缓存」查看进度", Toast.LENGTH_SHORT).show();
                }
            }

        });

        if (!SharedPrefsStore.isDefaultPortrait(this)) {
            ibScreen.performClick();
        }
    }

    private void tryToPlayOther() {
        tryFetchMediaInfo();
        /**
         * 如果已经开发播放，先停止播放
         */
        if (mPlayerStatus != PlayerStatus.PLAYER_IDLE) {
            mVV.stopPlayback();
        }

        /**
         * 发起一次新的播放任务
         */
        if (mEventHandler.hasMessages(UI_EVENT_PLAY)) {
            mEventHandler.removeMessages(UI_EVENT_PLAY);
        }
        mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
    }

    public void tryFetchMediaInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                // BVideoView.getMediaInfo(VideoViewPlayingActivity.this,
                // info.getUrl(), drmToken);
                BVideoView.getMediaInfo(AdvancedPlayActivity.this, info.getUrl());
                String[] fetchResolution = BVideoView.getSupportedResolution();
                if (fetchResolution != null && fetchResolution.length > 0) {
                    if (mediaController != null) {
                        mediaController.setAvailableResolution(fetchResolution);
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.v(TAG, "onPause");

        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (mVV.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            mLastPos = (int) mVV.getCurrentPosition();
            // when scree lock,paus is good select than stop
            // don't stop pause
            // mVV.stopPlayback();
            mVV.pause();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.v(TAG, "onResume");

        // 发起一次播放任务,当然您不一定要在这发起
        if (!mVV.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            mVV.resume();
        } else {
            mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        // 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
        if (mVV.isPlaying() && (mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            mLastPos = (int) mVV.getCurrentPosition();
            // don't stop pause
            // mVV.stopPlayback();
            mVV.pause();
        }
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((mPlayerStatus != PlayerStatus.PLAYER_IDLE)) {
            mLastPos = (int) mVV.getCurrentPosition();
            mVV.stopPlayback();
        }
        if (toast != null) {
            toast.cancel();
        }
        /**
         * 结束后台事件处理线程
         */
        mHandlerThread.quit();
        synchronized (syncPlaying) {
            try {
                if (!isReadyForQuit) {
                    Log.v(TAG, "waiting for notify invoke or 2s expires");
                    syncPlaying.wait(2 * 1000);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Log.v(TAG, "onDestroy");
    }

    @Override
    public boolean onInfo(int what, int extra) {
        // TODO Auto-generated method stub
        switch (what) {
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                Log.i(TAG, "caching start,now playing url : " + mVV.getCurrentPlayingUrl());

                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                Log.i(TAG, "caching start,now playing url : " + mVV.getCurrentPlayingUrl());

                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     */
    @Override
    public void onPlayingBufferCache(int percent) {
        // TODO Auto-generated method stub

    }

    /**
     * 播放出错
     */
    @Override
    public boolean onError(int what, int extra) {
        // TODO Auto-generated method stub
        Log.v(TAG, "onError");
        synchronized (syncPlaying) {
            isReadyForQuit = true;
            syncPlaying.notifyAll();
        }
        changeStatus(PlayerStatus.PLAYER_IDLE);
        return true;
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onCompletion");

        synchronized (syncPlaying) {
            isReadyForQuit = true;
            syncPlaying.notifyAll();
        }
        changeStatus(PlayerStatus.PLAYER_COMPLETED);
    }

    /**
     * 播放准备就绪
     */
    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onPrepared");
        changeStatus(PlayerStatus.PLAYER_PREPARED);
    }

    @Override
    public void OnCompletionWithParam(int param) {
        // param = 307 is end of stream
        // TODO Auto-generated method stub
        Log.v(TAG, "OnCompletionWithParam=" + param);
    }

    private void changeStatus(PlayerStatus status) {
        mPlayerStatus = status;
        this.mediaController.changeStatus(status);
        ;
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     *
     * @param v
     */
    public void onClickEmptyArea(View v) {
        if (!isFullScreen) {
            return;
        }
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        if (this.mediaController != null) {
            if (mediaController.getVisibility() == View.VISIBLE) {
                mediaController.hide();
                headerBar.setVisibility(View.GONE);
            } else {
                mediaController.show();
                headerBar.setVisibility(View.VISIBLE);
                hideOuterAfterFiveSeconds();
            }
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (!isFullScreen) {
            return;
        }
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!isFullScreen) {
                    return;
                }
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                            headerBar.setVisibility(View.GONE);
                        }

                    });
                }
            }

        }, 5 * 1000);

    }

    /**
     * 互动初始化
     */
    private void init() {
        Bmob.initialize(this, "9e2c41c917e8ec2970c4400fb1cbe890");
        data.start(new ValueEventListener() {

            @Override
            public void onDataChange(JSONObject arg0) {
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(arg0.optString("action"))) {
                    JSONObject data = arg0.optJSONObject("data");
                    messages.add(new Chat(data.optString("name"), data.optString("content")));
                   huDongAdapter.notifyDataSetChanged();
                    lv_data.setSelection(lv_data.getBottom());
                }

            }

            @Override
            public void onConnectCompleted(Exception e) {
                if (data.isConnected()) {
                    data.subTableUpdate("Chat");

                }
            }
        });


    }

    private void sendMsg(String name, String msg) {
        Chat chat = new Chat(name, msg);
        chat.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                et_content.setText("");
            }
        });

    }


}