package com.example.tryhome;
/**
 *ShowPdf is a class used for utilisation page.
 * @version 1.1
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

public class showPdf extends AppCompatActivity {
    /**
     *onCreate : display the pdf
     * @param savedInstanceState b
     */
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
            /**
             *
             * @param v v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showPdf.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
