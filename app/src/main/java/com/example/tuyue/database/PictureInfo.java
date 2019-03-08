package com.example.tuyue.database;

/*
* 图片信息类
* 保存在数据库中
* 在无网络时提供信息
* */
public class PictureInfo {

    private int cid;                 //图片种类id
    private String url;
    private int width;              //原始宽高
    private int height;
    private int inSimpleSize;           //采样率

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getInSimpleSize() {
        return inSimpleSize;
    }

    public void setInSimpleSize(int inSimpleSize) {
        this.inSimpleSize = inSimpleSize;
    }
}
