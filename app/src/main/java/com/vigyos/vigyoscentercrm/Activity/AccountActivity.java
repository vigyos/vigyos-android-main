package com.vigyos.vigyoscentercrm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.BuildCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vigyos.vigyoscentercrm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@BuildCompat.PrereleaseSdkCheck
public class AccountActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Button download;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private String pdfUrl = "https://vigyos-upload-files.s3.amazonaws.com/149a347d-835f-40bd-9f65-015458be05a6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialization();
        declaration();
        checkStoragePermission();
    }

    private void initialization(){
        ivBack = findViewById(R.id.ivBack);
        download = findViewById(R.id.download);
    }

    private void declaration(){
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAndPrintPDF();
            }
        });
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE
            );
        } else {
            downloadAndPrintPDF();
        }
    }

    private void downloadAndPrintPDF() {
        if (isExternalStorageWritable()) {
//            new DownloadTask().execute(pdfUrl);
            new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pdfUrl);

        } else {
            Toast.makeText(this, "External storage is not writable", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private class DownloadTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String pdfUrl = params[0];

            try {
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                String fileName = "your_file_name.pdf";
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getApplicationContext().getPackageName());

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir, fileName);
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutput.write(buffer, 0, bytesRead);
                }

                fileOutput.close();
                inputStream.close();

                return true;
            } catch (IOException e) {
                Log.e("DownloadTask", "Error downloading PDF", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                printPDF();
            } else {
                Toast.makeText(getApplicationContext(), "Error downloading PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void printPDF() {
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "your_file_name.pdf");
        Log.d("FilePath", pdfFile.getAbsolutePath());

        // Create a content URI using FileProvider
        Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdfFile);

        // Create an intent to print the PDF using an appropriate app
        Intent printIntent = new Intent(Intent.ACTION_VIEW);
        printIntent.setDataAndType(pdfUri, "application/pdf");
        printIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Check if there's an app to handle the intent before starting it
        if (printIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(printIntent);
        } else {
            Toast.makeText(this, "No PDF viewer app installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}