package com.joey.expresscall.protocol;

/**
 * 调用Jar中的方法的结果处理
 *
 * @param <T>
 */
public interface ResponseListener<T> {

    public void onSuccess(T json);

    public void onError(RequestError error);
}
