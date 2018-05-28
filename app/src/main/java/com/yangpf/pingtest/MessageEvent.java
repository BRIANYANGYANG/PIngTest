package com.yangpf.pingtest;

/**
 * Created by Administrator on 2018/5/28 0028.
 */

public class MessageEvent {
    private String message;
    private int id;

    public MessageEvent(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
