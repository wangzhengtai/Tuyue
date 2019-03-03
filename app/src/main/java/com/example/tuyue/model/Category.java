package com.example.tuyue.model;

public class Category {
    private String id;
    private String name;
    private String totalcnt;
    private String create_time;
    private String displaytype;
    private String tempdata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalcnt() {
        return totalcnt;
    }

    public void setTotalcnt(String totalcnt) {
        this.totalcnt = totalcnt;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDisplaytype() {
        return displaytype;
    }

    public void setDisplaytype(String displaytype) {
        this.displaytype = displaytype;
    }

    public String getTempdata() {
        return tempdata;
    }

    public void setTempdata(String tempdata) {
        this.tempdata = tempdata;
    }
}
