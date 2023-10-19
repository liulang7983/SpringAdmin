package com.bean;

import java.io.Serializable;

/**
 * @author ming.li
 * @date 2023/10/18 10:10
 */
public class MessageResult<T>{
    private Integer code;
    private boolean status;
    private String message;
    private transient T data;

    public MessageResult(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public MessageResult(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public MessageResult(Integer code, boolean status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public MessageResult(boolean status) {
        this.status = status;
    }

    public MessageResult(boolean status, T data) {
        this.status = status;
        this.data = data;
    }
    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static MessageResult error(Integer code, String msg)
    {
        return new MessageResult(code,false, msg, null);
    }

}

