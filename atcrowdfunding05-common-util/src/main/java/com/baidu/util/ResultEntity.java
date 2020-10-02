package com.baidu.util;

/**
 * @Author Administrator
 * @create 2020/7/14 0014 11:10
 */

public class ResultEntity<T> {

    public static final String success = "SUCCESS";
    public static final String failed = "FAILED";

    //用来封装当前请求处理的结果是成功还是失败
    private String result;

    //请求处理失败时返回的错误消息
    private String message;

    //要返回的数据
    private T data;

    /**
     * 请求处理成功不需要返回数据时使用的工具方法
     */
    public static <E> ResultEntity<E> successWithoutData() {

        return new ResultEntity<E>(success,null,null);
    }

    /**
     * 请求处理成功需要返回数据时使用的工具方法
     */
    public static <E> ResultEntity<E> successWithData(E data) {

        return new ResultEntity<E>(success,null,data);
    }

    /**
     * 请求处理失败需要返回数据时使用的工具方法
     */
    public static <E> ResultEntity<E> failed(String message) {

            return new ResultEntity<E>(failed,message,null);
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
