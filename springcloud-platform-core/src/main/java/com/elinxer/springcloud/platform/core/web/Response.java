package com.elinxer.springcloud.platform.core.web;


import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 响应消息返回此对象，响应请求结果给客户端
 *
 * @author elinx
 */
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 8992436576262574064L;

    /**
     * 错误消息
     */
    protected String message;

    /**
     * 响应数据
     */
    protected T data;

    /**
     * HTTP状态（必选）
     */
    protected int status;

    /**
     * 时间戳 13位（ms）
     */
    private Long timestamp;

    /**
     * 响应接口编码（非必选）
     */
    private String code;


    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code;
    }

    public static <T> Response<T> error(String message) {
        return error(500, message);
    }

    public static <T> Response<T> error(int status, String message) {
        Response<T> msg = new Response<>();
        msg.message = message;
        msg.status(status);
        return msg.putTimeStamp();
    }

    public static <T> Response<T> ok() {
        return ok(null);
    }

    private Response<T> putTimeStamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public static <T> Response<T> ok(T result) {
        return new Response<T>()
                .data(result)
                .putTimeStamp()
                .status(200);
    }

    public Response<T> data(T data) {
        this.data = data;
        return this;
    }


    public Response<T> code(String code) {
        this.code = code;
        return this;
    }

    public Response() {
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss");
    }

    public Response<T> status(int status) {
        this.status = status;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
