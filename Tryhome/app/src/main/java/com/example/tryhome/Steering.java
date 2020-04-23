package com.example.tryhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class Steering extends AppCompatActivity {
    private ImageButton moveForward;
    private ImageButton moveBackward;
    private ImageButton moveLeft;
    private ImageButton moveRight;
    private SeekBar velocity;
    public BluetoothConnectionService bluetoothConnection;
    private String left, right, up, down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steering);
        velocity = findViewById(R.id.velocity);
        up = "TEST UP";
        down = "TEST DOWN";
        left = "TEST LEFT";
        right = "TEST RIGHT";
        bluetoothConnection = new BluetoothConnectionService(Steering.this);
        moveBackward = findViewById(R.id.imageButtonBack);
        moveForward = findViewById(R.id.imageButtonForward);
        moveRight = findViewById(R.id.imageButtonRight);
        moveLeft = findViewById(R.id.imageButtonLeft);
        moveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(left.getBytes());
            }
        });
        moveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(right.getBytes());
            }
        });
        moveForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(up.getBytes());
            }
        });
        moveBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(down.getBytes());
            }
        });
    }
}
