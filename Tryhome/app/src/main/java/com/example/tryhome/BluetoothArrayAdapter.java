package com.example.tryhome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BluetoothArrayAdapter extends ArrayAdapter {
    /**
     *
     * @param context The context of the call of the function
     * @param ressource the ressource used
     * @param devices the bluetooth device we want to act on
     */
     public BluetoothArrayAdapter(@NonNull Context context, int ressource, List<BluetoothDevice> devices) {
        super(context, 0, devices);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BTAspect aspect = null;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bt_devices, parent, false);
        }
        aspect = new BTAspect();
        aspect.Name = (TextView) convertView.findViewById(R.id.Name);
        aspect.Adress = (TextView) convertView.findViewById(R.id.Adress);

        BluetoothDevice item = (BluetoothDevice) getItem(position);

        if (item != null) {
            aspect.Name.setText(item.getName());
            aspect.Adress.setText(item.getAddress());
        }


        return convertView;
    }

    /**
     *
     */
    private class BTAspect{
        public TextView Name;
        public TextView Adress;
    }
}