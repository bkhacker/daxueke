package com.baidu.bce.videoplayer.demo.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者：yeweizheng on 2017/4/25 15:51
 * 包名：com.baidu.bce.videoplayer.demo.entity
 * 文件名：daxueke
 * 描述：
 */
public class DocEntity extends BmobObject {
    private int id;
    private String docName;
    private String docId;
    private String desc;
    private String docImg;
    private BmobFile downLink;
    private String webLink;
    private String downUrl;


    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDocImg() {
        return docImg;
    }

    public void setDocImg(String docImg) {
        this.docImg = docImg;
    }

    public BmobFile getDownLink() {
        return downLink;
    }

    public void setDownLink(BmobFile downLink) {
        this.downLink = downLink;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
