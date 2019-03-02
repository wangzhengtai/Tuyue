package com.example.tuyue.model;

import java.util.Date;
import java.util.List;

public class Categories {
    private String errno;
    private String errmsg;
    private String consume;
    private String total;
    private List<Category> data;

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public static class Category{
        private String id;
        private String name;
        private String totalcnt;
        private Date create_time;
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

        public Date getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Date create_time) {
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
}
