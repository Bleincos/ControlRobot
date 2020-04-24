package com.example.tryhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
    private ImageButton imageButtonStart;
    private ImageButton imageButtonStop;
    private SeekBar velocity;
    public BluetoothConnectionService bluetoothConnection;
    private String left, right, go, stop, back, forward;
    private TextView speed;
    private Button backButton;

//DataBase reference
   // DataBaseReference mRootRef = FireBaseDataBase.getInstance().getReference();*
    //DataBaseReference mySmileyRef = mRootRef.child("smiley");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steering);
        // Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);
    backButton = findViewById(R.id.buttonBack3);

        speed = findViewById(R.id.velocityValue);
        velocity = findViewById(R.id.velocity);
        go = "go";
        stop = "stop";
        back = "back";
        forward = "forward";
        left = "left";
        right = "right";
        bluetoothConnection = new BluetoothConnectionService(Steering.this);
        moveBackward = findViewById(R.id.imageButtonBack);
        moveForward = findViewById(R.id.imageButtonForward);
        moveRight = findViewById(R.id.imageButtonRight);
        moveLeft = findViewById(R.id.imageButtonLeft);
        imageButtonStart = findViewById(R.id.imageButtonStart);
        imageButtonStop = findViewById(R.id.imageButtonStop);
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
                bluetoothConnection.write(forward.getBytes());
            }
        });
        moveBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(back.getBytes());
            }
        });
       imageButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(go.getBytes());
            }
        });
        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothConnection.write(stop.getBytes());
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
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v  vvvv
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Steering.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void setVelocity(int value){
        String theValue = ""+value;
        bluetoothConnection.write(theValue.getBytes());
        speed.setText("The speed is set to :" +theValue);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu:
                Intent intent2 = new Intent(Steering.this, MainActivity.class);
                startActivity(intent2);
                finish();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                //return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
