package com.baidu.bce.videoplayer.demo.entity;

import cn.bmob.v3.BmobObject;

/**
 * 作者：yeweizheng on 2017/3/25 16:06
 * 包名：com.baidu.bce.videoplayer.demo.entity
 * 文件名：daxueke
 * 描述：
 */
public class VideoEntity extends BmobObject {
    private String title;
    private String videoUrl;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
