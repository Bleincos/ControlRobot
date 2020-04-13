package com.example.controlrobot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView;

        textView = findViewById(R.id.TextViewAbout);
        textView.setText("App developped by : \n\r Marwane Ould-Ahmed \n\r And \n\r Olivier Bleinc \n\r This app allows the user to control a robot directly from his phone to the robot via bluetooth. Click How to use to show instructions");
    }
}
