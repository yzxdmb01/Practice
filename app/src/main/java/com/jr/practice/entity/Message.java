package com.jr.practice.entity;

import java.util.Date;

/**
 * Created by yzxdm on 2017/11/24.
 */

public class Message {
    private boolean self;
    private String content;
    private Date date;

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
