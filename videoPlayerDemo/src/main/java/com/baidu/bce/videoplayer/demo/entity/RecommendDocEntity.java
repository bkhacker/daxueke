package com.baidu.bce.videoplayer.demo.entity;

import cn.bmob.v3.BmobObject;

/**
 * 作者：yeweizheng on 2017/3/28 23:13
 * 包名：com.baidu.bce.videoplayer.demo.entity
 * 文件名：daxueke
 * 描述：推荐资料实体
 */
public class RecommendDocEntity extends BmobObject {
    private String url;
    private String title;
    private String desc;
    private String author;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
