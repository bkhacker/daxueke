package com.baidu.bce.videoplayer.demo.entity;

import cn.bmob.v3.BmobObject;

/**
 * 作者：yeweizheng on 2017/3/19 12:35
 * 包名：com.baidu.bce.videoplayer.demo.entity
 * 文件名：daxueke
 * 描述：
 */
public class HomeEntity extends BmobObject {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
