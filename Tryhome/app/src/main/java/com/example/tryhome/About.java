package com.example.tryhome;
/**
 * About is a class used for About page.
 *
 * @version 1.1
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState fff
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Button showInstruction, back;


        showInstruction = findViewById(R.id.buttonInstructions);
        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v fff
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        showInstruction.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v  vvvv
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, showPdf.class);
                startActivity(intent);
                finish();
            }
        });
    }
}