package com.baidu.bce.videoplayer.demo.bar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.activity.AdvancedVideoPlayActivity.PlayerStatus;
import com.baidu.bce.videoplayer.demo.info.SharedPrefsStore;
import com.baidu.bce.videoplayer.demo.view.ResolutionListPopWindow;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnNetworkSpeedListener;
import com.baidu.cyberplayer.core.BVideoView.OnPositionUpdateListener;
import com.baidu.cyberplayer.core.BVideoView.OnSeekCompleteListener;
import com.baidu.cyberplayer.core.BVideoView.OnTotalCacheUpdateListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义播放控制类
 * @author baidu
 *
 */
public class AdvancedVideoMediaController extends RelativeLayout
        implements OnPositionUpdateListener, OnTotalCacheUpdateListener, OnNetworkSpeedListener,
        OnSeekCompleteListener, OnClickListener {

    private static final String TAG = "AdvancedMediaController";

    private boolean isPrepared = false;

    private ImageButton playButton;
    private ImageButton snapshotButton;
    private Button fitButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private Button resolutionButton;
    private ImageButton downloadButton;
    private TextView positionView;
    private SeekBar seekBar;
    private TextView durationView;
    private TextView infoResolutionView;
    // private TextView infoBitrateView;
    private TextView infoNetworkView;

    private String[] availableResolution = null;

    public String[] getAvailableResolution() {
        return availableResolution;
    }

    public void setAvailableResolution(String[] fetchResolution) {
        String[] availableResolutionDesc = new String[fetchResolution.length];
        for (int i = 0; i < fetchResolution.length; ++i) {
            availableResolutionDesc[i] = getDescriptionOfResolution(fetchResolution[i]);
        }
        this.availableResolution = availableResolutionDesc;

        // set bitrate value
        // mainThreadHandler.post(new Runnable() {
        //
        // @Override
        // public void run() {
        // infoBitrateView.setText(BVideoView.getSupportedBitrateKb()[0] / 1000
        // + "Kbps");
        // }
        //
        // });
    }

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public Handler getMainThreadHandler() {
        return mainThreadHandler;
    }

    private BVideoView mVideoView = null;

    boolean mbIsDragging = false;

    // private View.OnClickListener playListener;
    private OnClickListener preListener;
    private OnClickListener nextListener;
    private OnClickListener downloadListener;

    private PlayerStatus currentStatus = PlayerStatus.PLAYER_IDLE;

    public AdvancedVideoMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public AdvancedVideoMediaController(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {

        // inflate controller bar
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.bar_advanced_media_controller, this);

        playButton = (ImageButton) layout.findViewById(R.id.ibtn_play);
        playButton.setOnClickListener(this);

        snapshotButton = (ImageButton) layout.findViewById(R.id.ibtn_snapshot);
        snapshotButton.setOnClickListener(this);

        fitButton = (Button) layout.findViewById(R.id.btn_fitmode);
        if (SharedPrefsStore.isPlayerFitModeCrapping(this.getContext())) {
            fitButton.setText("裁剪");
        } else {
            fitButton.setText("填充");
        }
        fitButton.setOnClickListener(this);

        previousButton = (ImageButton) layout.findViewById(R.id.ibtn_previous);
        previousButton.setOnClickListener(this);

        nextButton = (ImageButton) layout.findViewById(R.id.ibtn_next);
        nextButton.setOnClickListener(this);

        resolutionButton = (Button) layout.findViewById(R.id.btn_resolution);
        resolutionButton.setOnClickListener(this);

        downloadButton = (ImageButton) layout.findViewById(R.id.ibtn_download);
        downloadButton.setOnClickListener(this);

        positionView = (TextView) layout.findViewById(R.id.tv_position);
        seekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        seekBar.setMax(0);
        durationView = (TextView) layout.findViewById(R.id.tv_duration);

        infoResolutionView = (TextView) layout.findViewById(R.id.tv_resolution);
        // infoBitrateView = (TextView) layout.findViewById(R.id.tv_bitrate);
        infoNetworkView = (TextView) layout.findViewById(R.id.tv_netspeed);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePostion(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mbIsDragging = true;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoView.getDuration() > 0) {
                    // 仅非直播的视频支持拖动
                    currentPositionInSeconds = seekBar.getProgress();
                    if (mVideoView != null) {
                        mVideoView.seekTo(seekBar.getProgress());
                    }

                    if (currentStatus == PlayerStatus.PLAYER_COMPLETED) {
                        mVideoView.start();
                        changeStatus(PlayerStatus.PLAYER_PREPARING);
                    }
                }

                mbIsDragging = false;
            }
        });
        enableControllerBar(false);
    }

    /**
     *
     * @param status
     */
    public void changeStatus(final PlayerStatus status) {
        Log.d(TAG, "mediaController: changeStatus=" + status.name());
        currentStatus = status;
        isMaxSetted = false;
        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                if (status == PlayerStatus.PLAYER_IDLE) {
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(false);
                    resolutionButton.setEnabled(false);
                    updatePostion(mVideoView == null ? 0 : mVideoView.getCurrentPosition());
                    updateDuration(mVideoView == null ? 0 : mVideoView.getDuration());
                    isPrepared = false;
                } else if (status == PlayerStatus.PLAYER_PREPARING) {
                    playButton.setEnabled(false);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    seekBar.setEnabled(false);
                    resolutionButton.setEnabled(false);
                    isPrepared = false;
                } else if (status == PlayerStatus.PLAYER_PREPARED) {
                    isPrepared = true;
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                    seekBar.setEnabled(true);

                    // set width
                    String resolutionStr = mVideoView.getVideoWidth() + "x" + mVideoView.getVideoHeight();
                    if (mVideoView.getVideoWidth() > 0) { // 可能为音频文件，width=-1
                        infoResolutionView.setText(resolutionStr);
                    }

                    resolutionButton.setEnabled(true);
                    resolutionButton.setText(getDescriptionOfResolution(resolutionStr));
                } else if (status == PlayerStatus.PLAYER_COMPLETED) {
                    playButton.setEnabled(true);
                    playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                    resolutionButton.setEnabled(false);
                    isPrepared = false;
                }
            }

        });

    }

    /**
     * BVideoView implements VideoViewControl, so it's a BVideoView object
     * actually
     *
     * @param player
     */
    public void setMediaPlayerControl(BVideoView player) {
        mVideoView = player;
        mVideoView.setOnPositionUpdateListener(this);
        mVideoView.setOnTotalCacheUpdateListener(this);
        mVideoView.setOnNetworkSpeedListener(this);
    }

    /**
     * Show the controller on screen. It will go away automatically after 3
     * seconds of inactivity.
     */
    public void show() {
        if (mVideoView == null) {
            return;
        }
        if (mVideoView.getDuration() > 0) {
            setProgress((int) currentPositionInSeconds);
        }

        this.setVisibility(View.VISIBLE);
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        this.setVisibility(View.GONE);
    }

    /**
     * @hide
     */
    public boolean getIsDragging() {
        return mbIsDragging;
    }

    private void updateDuration(int second) {
        if (durationView != null) {
            durationView.setText(formatSecond(second));
        }
    }

    private void updatePostion(int second) {
        if (positionView != null) {
            positionView.setText(formatSecond(second));
        }
    }

    private String formatSecond(int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        return strTemp;
    }

    private boolean isMaxSetted = false;

    /**
     * Set the max process for the seekBar, usually the lasting seconds
     *
     * @hide
     */
    public void setMax(int maxProgress) {
        if (isMaxSetted) {
            return;
        }
        if (seekBar != null) {
            seekBar.setMax(maxProgress);
        }
        updateDuration(maxProgress);
        if (maxProgress > 0) {
            isMaxSetted = true;
        }
    }

    /**
     * @hide
     * @param progress
     */
    public void setProgress(int progress) {
        if (seekBar != null) {
            seekBar.setProgress(progress);
        }
    }

    /**
     * @hide
     * @param progress
     */
    public void setCache(int cache) {
        if (seekBar != null && cache != seekBar.getSecondaryProgress()) {
            seekBar.setSecondaryProgress(cache);
        }
    }

    /**
     * @hide
     * @param isEnable
     */
    public void enableControllerBar(boolean isEnable) {
        seekBar.setEnabled(isEnable);
        playButton.setEnabled(isEnable);
    }

    private long currentPositionInSeconds = 0L;

    /**
     * onPositionUpdate is invoked per 200ms
     */
    @Override
    public boolean onPositionUpdate(long newPositionInMilliSeconds) {
        long newPositionInSeconds = newPositionInMilliSeconds / 1000;
        long previousPosition = currentPositionInSeconds;
        if (newPositionInSeconds > 0 && !getIsDragging()) {
            currentPositionInSeconds = newPositionInSeconds;
        }
        if (getVisibility() != View.VISIBLE) {
            // 如果控制条不可见，则不设置进度
            return false;
        }
        if (!getIsDragging()) {
            int duration = mVideoView.getDuration();
            if (duration > 0) {
                this.setMax(duration);
                // 直播视频的duration为0，此时不设置进度
                if (newPositionInSeconds > 0 && previousPosition != newPositionInSeconds) {
                    this.setProgress((int) newPositionInSeconds);
                }
            }
        }
        return false;
    }

    @Override
    public void onTotalCacheUpdate(final long arg0) {
        mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                setCache((int) arg0 + 10);
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ibtn_play:
            if (mVideoView == null) {
                Log.d(TAG, "playButton checkstatus changed, but bindView=null");
            } else {
                if (!isPrepared) {
                    Log.d(TAG, "playButton: Will invoke start()");
                    mVideoView.start();
                    changeStatus(PlayerStatus.PLAYER_PREPARING);
                } else {
                    if (mVideoView.isPlaying()) {
                        Log.d(TAG, "playButton: Will invoke pause()");
                        playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                        mVideoView.pause();
                    } else {
                        Log.d(TAG, "playButton: Will invoke resume()");
                        playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                        mVideoView.resume();
                    }
                }
            }
            break;
        case R.id.ibtn_snapshot:
            // take snapshot
            File sdDir = null;
            String strpath = null;
            Bitmap bitmap = null;
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS");
            String time = formatter.format(date);
            sdDir = Environment.getExternalStorageDirectory();
            strpath = sdDir.toString() + "/" + time + ".jpg";
            Log.d(TAG, "snapshot save path=" + strpath);

            bitmap = mVideoView.takeSnapshot();
            if (bitmap != null) {
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(strpath, false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Throwable t) {
                            Log.e(TAG, "Error occurred while saving snapshot!");
                        }
                    }
                }
                os = null;
            }

            Toast.makeText(this.getContext(), "截图保存在sd卡根目录下, 文件名为" + strpath, Toast.LENGTH_SHORT).show();
            break;
        case R.id.btn_fitmode:
            if (fitButton.getText().equals("填充")) {
                // 转为 裁剪模式：视频保持比例缩放，不留黑边，填满显示区域的两边
                mVideoView.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                fitButton.setText("裁剪");
                SharedPrefsStore.setPlayerFitMode(getContext(), true);
            } else {
                // 转为 填充模式：视频保持比例缩放，至少一边与显示区域相同，另一边有黑边
                mVideoView.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                fitButton.setText("填充");
                SharedPrefsStore.setPlayerFitMode(getContext(), false);
            }
            break;
        case R.id.ibtn_previous:
            if (preListener != null) {
                preListener.onClick(v);
            }
            break;
        case R.id.ibtn_next:
            if (nextListener != null) {
                nextListener.onClick(v);
            }
            break;
        case R.id.btn_resolution:
            // select a resolution
            Log.v(TAG, "Show resolution clicked");
            if (this.availableResolution != null && this.availableResolution.length > 0) {

                OnItemClickListener listener = new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mVideoView != null) {
                            mVideoView.selectResolutionType(position);
                            resolutionButton.setText(availableResolution[position]);
                            // infoBitrateView.setText(BVideoView.getSupportedBitrateKb()[position]
                            // / 1000 + "Kbps");
                        }
                    }

                };
                // popup window
                ResolutionListPopWindow popWindow = new ResolutionListPopWindow((Activity) getContext(),
                        availableResolution, listener);
                popWindow.showPopupWindow(((Activity) getContext()).findViewById(R.id.root));
            } else {
                Toast.makeText(this.getContext(), "该视频不是多码率视频(m3u8 master url)", Toast.LENGTH_SHORT).show();
            }
            break;
        case R.id.ibtn_download:
            // SoundPopWindow popWindow = new
            // SoundPopWindow((Activity)getContext());
            // popWindow.showPopupWindow(((Activity)getContext()).findViewById(R.id.root));
            if (downloadListener != null) {
                downloadListener.onClick(v);
            }
            break;
        default:
            break;
        }
    }

    public void clearViewContent() {
        currentPositionInSeconds = 0L;
        positionView.setText("00:00");
        durationView.setText("00:00");
        seekBar.setMax(0);
        seekBar.setProgress(0);
        this.availableResolution = null;
    }

    public OnClickListener getPreListener() {
        return preListener;
    }

    public void setPreListener(OnClickListener preListener) {
        this.preListener = preListener;
    }

    public OnClickListener getNextListener() {
        return nextListener;
    }

    public void setNextListener(OnClickListener nextListener) {
        this.nextListener = nextListener;
    }

    @Override
    public void onNetworkSpeedUpdate(final int arg0) {
//        Log.d(TAG, "onNetworkSpeedUpdate speed=" + arg0);
        // 右移10位，即：除以1024。因为arg0的单位为 Byte per second
        this.mainThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                infoNetworkView.setText((arg0 >> 10) + "KB/s");
            }
        });

    }

    public String getDescriptionOfResolution(String resolutionType) {
        String result = "未知";
        try {
            String[] cuts = resolutionType.trim().split("x");
            if (cuts.length == 2) {
                int iResult = Integer.parseInt(cuts[1]);
                if (iResult <= 0) {
                    result = "未知";
                } else if (iResult <= 120) {
                    result = "120P";
                } else if (iResult <= 240) {
                    result = "240P";
                } else if (iResult <= 360) {
                    result = "360P";
                } else if (iResult <= 480) {
                    result = "480P";
                } else if (iResult <= 800) {
                    result = "720P";
                } else {
                    result = "1080P";
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "getDescriptionOfResolution exception:" + e.getMessage());
        }
        Log.d(TAG, "getDescriptionOfResolution orig=" + resolutionType + ";result=" + result);

        return result;
    }

    public OnClickListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(OnClickListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public void onSeekComplete() {
        
    }
}
