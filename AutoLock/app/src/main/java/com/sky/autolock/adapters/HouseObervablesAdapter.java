package com.sky.autolock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sky.autolock.R;
import com.sky.autolock.models.Door;
import com.sky.autolock.models.HouseObservable;
import com.sky.autolock.network.NetworkManager;

import java.util.ArrayList;
import java.util.HashMap;

public class HouseObervablesAdapter extends ArrayAdapter<HouseObservable> {

    private HashMap<Long, HouseObservable> observableMap;
    private ArrayList<HouseObservable> observables;

    public HouseObervablesAdapter(@NonNull Context context, @NonNull ArrayList<HouseObservable> objects) {
        super(context, 0, objects);

        observables = objects;
        observableMap = new HashMap<>();

        for (int i = 0; i < observables.size(); i++){
            observableMap.put(observables.get(i).getId(), observables.get(i));
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final HouseObservable ho = getItem(position);

        if (ho instanceof Door){
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_door, parent, false);
        }
        else{
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_window, parent, false);
        }

        TextView name = convertView.findViewById(R.id.tv_observable_name);
        TextView locked = convertView.findViewById(R.id.tv_observable_locked);
        TextView open = convertView.findViewById(R.id.tv_observable_open);

        name.setText(ho.getName());

        if (ho.isLocked()){
            locked.setText("Locked");
            locked.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
        }
        else{
            locked.setText("Unlocked");
            locked.setTextColor(getContext().getResources().getColor(R.color.colorRed));
        }

        if (ho.isClosed()){
            open.setText("Closed");
            open.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
        }
        else{
            open.setText("Open");
            open.setTextColor(getContext().getResources().getColor(R.color.colorRed));
        }

        if (ho instanceof Door){
            Button btnLock = convertView.findViewById(R.id.btn_lock);

            if (ho.isLocked()){
                btnLock.setText("Unlock");
            }
            else{
                btnLock.setText("Lock");
            }

            btnLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ho.isLocked()){
                        NetworkManager.getInstance().unlock(ho.getId());
                    }
                    else{
                        NetworkManager.getInstance().lock(ho.getId());
                    }
                }
            });
        }

        return convertView;
    }

    public boolean updateObservable(HouseObservable ho){
        if (observableMap.containsKey(ho.getId())){
            observableMap.get(ho.getId()).update(ho);
            notifyDataSetChanged();
            return false;
        }
        else{
            observables.add(ho);
            observableMap.put(ho.getId(), ho);
            notifyDataSetChanged();
            return true;
        }
    }

}
