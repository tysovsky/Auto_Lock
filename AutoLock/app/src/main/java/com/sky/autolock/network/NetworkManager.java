package com.sky.autolock.network;

import android.util.Log;

import com.sky.autolock.models.HouseObservable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetworkManager {
    private OkHttpClient httpClient;
    private ArrayList<OnObservablesReceivedListener> listeners;

    public interface OnObservablesReceivedListener{
        void onObservablesReceived(ArrayList<HouseObservable> observables);
    }

    private static NetworkManager manager;

    private NetworkManager(){
        httpClient = new OkHttpClient();
        listeners = new ArrayList<>();
    }

    public static NetworkManager getInstance(){
        if(manager == null)
            manager = new NetworkManager();

        return manager;
    }

    public void addListener(OnObservablesReceivedListener listener){
        listeners.add(listener);
    }

    public void lock(long id){
        httpClient.newCall(RequestProvider.getLockRequest(id)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ArrayList<HouseObservable> observables = HouseObservable.parseJson(response.body().string());

                for (OnObservablesReceivedListener listener: listeners)
                    listener.onObservablesReceived(observables);

            }
        });
    }

    public void unlock(long id){
        httpClient.newCall(RequestProvider.getUnlockRequest(id)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ArrayList<HouseObservable> observables = HouseObservable.parseJson(response.body().string());

                for (OnObservablesReceivedListener listener: listeners)
                    listener.onObservablesReceived(observables);

            }
        });
    }

    public void getObservables(){
        httpClient.newCall(RequestProvider.getHouseObservablesRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ArrayList<HouseObservable> observables = HouseObservable.parseJson(response.body().string());

                for (OnObservablesReceivedListener listener: listeners)
                    listener.onObservablesReceived(observables);

            }
        });
    }

    public void updateFirebaseToken(String token){
        httpClient.newCall(RequestProvider.getUpdateFirebaseRequest(token)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i("NetworkManager", response.body().string());
            }
        });
    }

}
