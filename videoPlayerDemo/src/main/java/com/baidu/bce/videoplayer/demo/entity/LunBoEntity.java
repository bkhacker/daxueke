package com.baidu.bce.videoplayer.demo.entity;

import cn.bmob.v3.BmobObject;

/**
 * 作者：yeweizheng on 2017/4/15 16:16
 * 包名：com.baidu.bce.videoplayer.demo.entity
 * 文件名：daxueke
 * 描述：这他妈是轮播的实体啊！
 */
public class LunBoEntity extends BmobObject {
    private String imageName;
    private String imageUrl;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
