package com.example.yugenshtil.finalproject.ItemSell;

/*

Created by Oleg Mytryniuk
This class was using
com.kosalgeek.android.photoutil API

https://github.com/kosalgeek/PhotoUtil

 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;


public class AddImage extends AppCompatActivity {
    private ImageView camera, gallery, image;
    private Button saveImage;
    private  final String TAG = this.getClass().getName();
    SharedPreferences sharedpreferences;
    String token = "";

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13342;
    final int GALLERY_REQUEST = 13343;
    final int MY_REQUEST_CODE =123;

    String selectedPhoto;
    String imageDecoded="iII=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token","");

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            int permissionCheckCamera = ContextCompat.checkSelfPermission(AddImage.this,
                    Manifest.permission.CAMERA);

            int permissionCheckEXTERNAL = ContextCompat.checkSelfPermission(AddImage.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        }

        else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            int permissionCheckEXTERNAL = ContextCompat.checkSelfPermission(AddImage.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE);
        }


        int permissionCheck = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.INTERNET);
        Log.d("TAG","Permission for INTERNET " + permissionCheck+"");
        int permissionCheckCamera = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.CAMERA);
        Log.d("TAG","Permission for CAMERA " + permissionCheckCamera+"");


        int permissionCheckEXTERNAL = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("TAG","Permission for External " + permissionCheckEXTERNAL+"");


        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        // Buttons to upload image
        camera = (ImageView) findViewById(R.id.ivCamera_AddImage);
        gallery = (ImageView) findViewById(R.id.ivGallery_AddImage);

        // Dipslay the uploaded image
        image = (ImageView) findViewById(R.id.ivUploadedImage_AddImage);

        //Save Image
        saveImage = (Button) findViewById(R.id.btSaveImage_AddImage);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent in = cameraPhoto.takePhotoIntent();
                    startActivityForResult(in, CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Something wrong with camera",Toast.LENGTH_SHORT).show();
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);


            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Base64", imageDecoded);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {

            }
        }
    }


    // The code was inspired by https://github.com/kosalgeek/PhotoUtil

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        if(resultCode==RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    image.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),"Something wrong with image loader",Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, photoPath);
                getBase64();
            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),"Something wrong with gallery loader",Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, photoPath);
                getBase64();
            }
        }
    }


   void getBase64(){

            try {
                Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024,1024).getBitmap();
                imageDecoded = ImageBase64.encode(bitmap);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("imageBase64", imageDecoded);
                editor.commit();

            } catch (FileNotFoundException e) {
                 Toast.makeText(getApplicationContext(),"Something wrong with upload",Toast.LENGTH_SHORT).show();
            }
   }
}
