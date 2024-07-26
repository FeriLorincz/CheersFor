package com.example.cheersfor.interfaces;

public interface Callback<T>{

    void onResponse(T result);
    void onFailure(Exception e);
}
