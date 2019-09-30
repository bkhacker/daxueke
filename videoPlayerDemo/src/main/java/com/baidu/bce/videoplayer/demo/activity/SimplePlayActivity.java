package com.baidu.bce.videoplayer.demo.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.bar.SimpleMediaController;
import com.baidu.bce.videoplayer.demo.info.VideoInfo;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionWithParamListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SimplePlayActivity extends AppCompatActivity implements OnPreparedListener, OnCompletionListener, OnErrorListener,
        OnInfoListener, OnPlayingBufferCacheListener, OnCompletionWithParamListener {
    private static final String TAG = "SimplePlayActivity";

    /**
     * 您的AK 请到http://console.bce.baidu.com/iam/#/iam/accesslist获取
     */
    private String ak = ""; // 请录入您的AK !!!

    private VideoInfo info;
    private BVideoView mVV = null;
    private SimpleMediaController mediaController = null;
    private RelativeLayout mViewHolder = null;

    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    private final Object syncPlaying = new Object();
    private volatile boolean isReadyForQuit = true;
    private Timer barTimer;

    private static final int UI_EVENT_PLAY = 0;

    private Toast toast;

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
                                e.printStackTrace();
                            }
                        }
                    }
    
                    /**
                     * 设置播放url
                     */
    
                    // mVV.setVideoPath(info.getUrl(), drmToken);
                    mVV.setVideoPath(info.getUrl());
    
                    // mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_simple_video_playing);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        info = getIntent().getParcelableExtra("videoInfo");

        initUI();
        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());
    }

    /**
     * 初始化界面
     */
    private void initUI() {
        mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
        mediaController = (SimpleMediaController) findViewById(R.id.media_controller_bar);
        /**
         * 设置ak
         */
        BVideoView.setAK(ak);
        mVV = new BVideoView(this);
        mVV.setLogLevel(0);

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
         * 设置解码模式 为保证兼容性，当前仅支持软解
         */
        mVV.setDecodeMode(BVideoView.DECODE_SW);
        mVV.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);
    }

    @Override
    protected void onPause() {
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
                e.printStackTrace();
            }
        }
        Log.v(TAG, "onDestroy");
    }

    @Override
    public boolean onInfo(int what, int extra) {
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
        // used for self-define buffering percent

    }

    /**
     * 播放出错
     */
    @Override
    public boolean onError(int what, int extra) {
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
        Log.v(TAG, "onPrepared");
        hideOuterAfterFiveSeconds();
        changeStatus(PlayerStatus.PLAYER_PREPARED);
    }

    @Override
    public void OnCompletionWithParam(int param) {
        // param = 307 is end of stream
        Log.v(TAG, "OnCompletionWithParam=" + param);
    }

    private void changeStatus(PlayerStatus status) {
        mPlayerStatus = status;
        if (this.mediaController != null) {
            this.mediaController.changeStatus(status);
        }
    }

    /**
     * 检测'点击'空白区的事件，若播放控制控件未显示，设置为显示，否则隐藏。
     * 
     * @param v
     */
    public void onClickEmptyArea(View v) {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        if (this.mediaController != null) {
            if (mediaController.getVisibility() == View.VISIBLE) {
                mediaController.hide();
            } else {
                mediaController.show();
                hideOuterAfterFiveSeconds();
            }
        }
    }

    private void hideOuterAfterFiveSeconds() {
        if (barTimer != null) {
            barTimer.cancel();
            barTimer = null;
        }
        barTimer = new Timer();
        barTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (mediaController != null) {
                    mediaController.getMainThreadHandler().post(new Runnable() {

                        @Override
                        public void run() {
                            mediaController.hide();
                        }

                    });
                }
            }

        }, 5 * 1000);

    }
}
