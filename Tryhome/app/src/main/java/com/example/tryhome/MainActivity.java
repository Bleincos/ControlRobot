package com.example.tryhome;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /**
     *
     */
    public Boolean success =false, bool;
    private Button showInstruction;

    private myBroadcastReceiver abroadcastReceiverTEST = new myBroadcastReceiver();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button about, roundCorner;
        showInstruction = findViewById(R.id.buttonInstructions2);
        roundCorner = findViewById(R.id.rou);
        about = findViewById(R.id.buttonAbout);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                finish();
            }
        });
        roundCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
        showInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, showPdf.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(abroadcastReceiverTEST, filter);;
        IntentFilter filterHeadset = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(abroadcastReceiverTEST, filterHeadset);
        //IntentFilter filterBattery = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        //registerReceiver(abroadcastReceiverTEST, filterBattery);
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(abroadcastReceiverTEST);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck  =  this.checkSelfPermission("Manisfest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
            else{
                Log.d("Settings", "Check permission for BT");
            }
        }
    }

}
