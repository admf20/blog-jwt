package com.andres.blog_jwt_mysql.util.response;

public class ApiResponse <T>{
    private int status;
    private String messages;
    private T Data;

    public ApiResponse(int status, String messages, T data) {
        this.status = status;
        this.messages = messages;
        Data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessage(String messages) {
        this.messages = messages;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
