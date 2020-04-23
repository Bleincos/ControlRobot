package com.example.tryhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Steering extends AppCompatActivity {
    private ImageButton moveForward;
    private ImageButton moveBackward;
    private ImageButton moveLeft;
    private ImageButton moveRight;
    private SeekBar velocity;
    public BluetoothConnectionService bluetoothConnection;
    private String left, right, up, down;
    private TextView speed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steering);
        speed = findViewById(R.id.velocityValue);
        velocity = findViewById(R.id.velocity);
        up = "go";
        down = "back";
        left = "TEST LEFT";
        right = "TEST RIGHT";
        bluetoothConnection = new BluetoothConnectionService(Steering.this);
        moveBackward = findViewById(R.id.imageButtonBack);
        moveForward = findViewById(R.id.imageButtonForward);
        moveRight = findViewById(R.id.imageButtonRight);
        moveLeft = findViewById(R.id.imageButtonLeft);
        velocity.setMax(100); // the speed limit is set to 100 which correspond to 100%
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
        velocity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setVelocity(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        velocity.setProgress(0);
    }
    public void setVelocity(int value){
        String theValue = ""+value;
        bluetoothConnection.write(theValue.getBytes());
        speed.setText("The speed is set to :" +theValue);
    }
}
