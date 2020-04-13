package com.example.controlrobot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

public class showPdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);

        Button back;
        PDFView apdfView;
        apdfView = findViewById(R.id.pdfView);
        apdfView.fromAsset("example.pdf").load();

        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showPdf.this, About.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
