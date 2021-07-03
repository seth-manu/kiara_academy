package com.kiaraacademy.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.kiaraacademy.R;

public class PdfViewActivity extends AppCompatActivity {

    private PDFView pdfView;
    private String pdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        initializeView();
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        pdfUrl = intent.getStringExtra("pdf_url");


        //new DownloadFileFromURL(this,pdfUrl, pdfView).execute();

    }

    private void initializeView() {
        pdfView = findViewById(R.id.pdfView);
    }
}
