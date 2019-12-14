package com.sky.autolock.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class HouseObservable {
    private String Name;
    private long Id;
    private boolean Locked;
    private boolean Closed;

    public String getName(){
        return Name;
    }

    public void setName(String name){
        Name = name;
    }

    public long getId(){
        return Id;
    }

    public void setId(long id){
        this.Id = id;
    }

    public boolean isLocked(){
        return Locked;
    }
    public boolean isClosed(){
        return Closed;
    }

    public void setClosed(boolean closed){
        Closed = closed;
    }
    public void setLocked(boolean locked){
        Locked = locked;
    }


    public void update(HouseObservable observable){
        Name = observable.getName();
        Locked = observable.isLocked();
        Closed = observable.isClosed();
    }

    public static ArrayList<HouseObservable> parseJson(String response){

        ArrayList<HouseObservable> observables = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject observableJson = jsonArray.getJSONObject(i);

                String type = observableJson.getString("type");

                HouseObservable observable;

                if(type.equals("door")){
                    observable = new Door();
                }
                else{
                    observable = new Window();
                }

                observable.setId(observableJson.getLong("id"));
                observable.setName(observableJson.getString("name"));
                observable.setLocked(observableJson.getBoolean("locked"));
                observable.setClosed(observableJson.getBoolean("closed"));

                observables.add(observable);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return observables;
    }

}
