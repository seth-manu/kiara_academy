package com.kiaraacademy.utils;

/*
 * Interface for callback to presenter from handler.
 */
public interface HandlerCallback {

    void onStart();

    void onSuccess(Object object, Integer tag);

    void onError(Object object, Integer tag);
}
