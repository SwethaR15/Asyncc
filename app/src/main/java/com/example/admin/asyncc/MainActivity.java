package com.example.admin.asyncc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    ImageView imageView;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission())

        {
            Intent i=new Intent( Intent.ACTION_SEND );
         Uri u=Uri.parse("file:///sdcard/name.jpg");
            i.setType("imageView/jpeg");
            i.putExtra(Intent.EXTRA_STREAM,u);
            startActivity(Intent.createChooser(i,"share"));

            startActivity( i );
        }
        else{
            requestPermissions( );
        }
            }
        });

        String url = "http://www.wallpapers13.com/wp-content/uploads/2016/03/Mickey-Mouse-Cartoon-Wallpaper-HD-for-mobile-phones-and-laptops-915x515.jpg";

        //   Picasso.get().load("http://www.wallpapers13.com/wp-content/uploads/2016/03/Mickey-Mouse-Cartoon-Wallpaper-HD-for-mobile-phones-and-laptops-915x515.jpg").into(imageView);

        new DownloadImage().execute(url);

    }

    private boolean checkPermission() {
        int FIRSTPERMISSIONRESULT = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
        if (FIRSTPERMISSIONRESULT == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                       {

                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else
                    Log.e("value", "Permission Denied, You cannot use local drive .");

                break;
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(MainActivity.this);
                mProgressDialog.setTitle("Download Image Tutorial");
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.show();
            }

            @Override
            protected Bitmap doInBackground(String... url) {
            String imageURL = url[0];
            Bitmap bitmap = null;
            try { InputStream input = new URL(imageURL).openStream();

                bitmap = BitmapFactory.decodeStream(input);


                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream os = new FileOutputStream(new File(storagePath,"name.jpg"));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }



            @Override
            protected void onPostExecute(Bitmap result) {
                imageView.setImageBitmap(result);
                mProgressDialog.dismiss();

            }
        }
    }


