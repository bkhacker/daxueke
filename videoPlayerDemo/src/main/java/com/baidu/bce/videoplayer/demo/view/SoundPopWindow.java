package com.baidu.bce.videoplayer.demo.view;

import com.baidu.bce.videoplayer.demo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SoundPopWindow extends PopupWindow {
    private View contentView;
    private DisplayMetrics dm = new DisplayMetrics();

    public SoundPopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_window_sound, null);
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int) (dm.density * 200));
        // // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (dm.density * 200));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(android.R.style.Animation_Dialog);

        SeekBar seekBar = (SeekBar) contentView.findViewById(R.id.sound_seekbar);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 显示popupWindow
     * 
     * @param parent
     */
    public void showPopupWindow(View view) {
        if (!this.isShowing()) {
            // dm.widthPixels - (int)(dm.density * 165)
            // 以下拉方式显示popupwindow
            this.showAtLocation(view, Gravity.BOTTOM, (int) (dm.widthPixels * 3 / 7), (int) (dm.density * 54));
        } else {
            this.dismiss();
        }
    }
}
