package com.baidu.bce.videoplayer.demo.loader;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * 作者：yeweizheng on 2017/4/15 10:53
 * 包名：com.baidu.bce.videoplayer.demo.loader
 * 文件名：daxueke
 * 描述：
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Picasso.with(context).load((String)path).into(imageView);
       // Picasso.with(context).load("http://static1.yingjiesheng.net/attachments/company/201704/c_51job_xy240_1704011_40793.jpg").into(imageView);

    }
}
