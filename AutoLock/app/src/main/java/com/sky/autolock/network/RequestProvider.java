package com.sky.autolock.network;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestProvider {
    private static String ServerAddress = "http://10.0.1.6:8080/api";

    public static Request getUpdateFirebaseRequest(String firebaseToken){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ServerAddress + "/add_device").newBuilder();

        RequestBody body = new FormBody.Builder()
                .add("firebase_token", firebaseToken)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .post(body)
                .build();

        return request;
    }

    public static Request getHouseObservablesRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ServerAddress + "/observables").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        return request;
    }

    public static Request getLockRequest(long id){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ServerAddress + "/" + id + "/lock").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        return request;
    }

    public static Request getUnlockRequest(long id){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(ServerAddress + "/" + id + "/unlock").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        return request;
    }
}
