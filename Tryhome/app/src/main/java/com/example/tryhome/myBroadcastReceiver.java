package com.example.tryhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class myBroadcastReceiver extends BroadcastReceiver {
    /**
     *
     * @param context v
     * @param intent v
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if(noConnectivity){
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                ;
            }
        }/*
        if(Intent.ACTION_BATTERY_LOW.equals(intent.getAction())){
                Toast.makeText(context, "LOW", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Not low", Toast.LENGTH_SHORT).show();
        }
        */
    }
}
