package com.sky.autolock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.sky.autolock.adapters.HouseObervablesAdapter;
import com.sky.autolock.models.Door;
import com.sky.autolock.models.HouseObservable;
import com.sky.autolock.models.Window;
import com.sky.autolock.network.NetworkManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkManager.OnObservablesReceivedListener {

    HouseObervablesAdapter adapter;
    ListView observablesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        observablesListView = findViewById(R.id.lv_observable_list);

        /*`
        ArrayList<HouseObservable> observables = new ArrayList<>();

        Door d = new Door();
        d.setName("Main Door");
        d.setId(0);
        observables.add(d);

        Window w = new Window();
        w.setId(1);
        w.setName("Living Room Window");

        observables.add(w);
        */


        adapter = new HouseObervablesAdapter(this, new ArrayList<HouseObservable>());
        observablesListView.setAdapter(adapter);

        NetworkManager.getInstance().addListener(this);
        NetworkManager.getInstance().getObservables();

    }

    @Override
    public void onObservablesReceived(final ArrayList<HouseObservable> observables) {

        if(observables == null)
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (HouseObservable ho: observables){
                    adapter.updateObservable(ho);
                }
            }
        });

    }
}
