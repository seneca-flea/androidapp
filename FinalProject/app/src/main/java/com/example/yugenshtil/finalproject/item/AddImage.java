package com.example.yugenshtil.finalproject.item;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yugenshtil.finalproject.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddImage extends AppCompatActivity {
    private ImageView upload, camera, gallery, image,load, loaded;
    private Button submit;
    private  final String TAG = this.getClass().getName();
    List<String> images = new ArrayList<String>();

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13342;
    final int GALLERY_REQUEST = 13343;
    final int MY_REQUEST_CODE =123;

    String selectedPhoto;
    String imageDecoded="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Oleeg","NOT GRANTED");
            int permissionCheckCamera = ContextCompat.checkSelfPermission(AddImage.this,
                    Manifest.permission.CAMERA);
            Log.d("Oleg","Permission for CAMERA " + permissionCheckCamera+"");

            int permissionCheckEXTERNAL = ContextCompat.checkSelfPermission(AddImage.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.d("Oleg","Permission for External " + permissionCheckEXTERNAL+"");


            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        }

        else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Oleeg","NOT GRANTED EXTERNAL");

            int permissionCheckEXTERNAL = ContextCompat.checkSelfPermission(AddImage.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.d("Oleg","Permission for External " + permissionCheckEXTERNAL+"");


            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE);
        }
        else
            Log.d("Oleeg","GRANTED");
        int permissionCheck = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.INTERNET);
        Log.d("Oleg","Permission for INTERNET " + permissionCheck+"");
        int permissionCheckCamera = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.CAMERA);
        Log.d("Oleg","Permission for CAMERA " + permissionCheckCamera+"");


        int permissionCheckEXTERNAL = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.d("Oleg","Permission for External " + permissionCheckEXTERNAL+"");


        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        upload = (ImageView) findViewById(R.id.ivUpload);
        camera = (ImageView) findViewById(R.id.ivCamera);
        gallery = (ImageView) findViewById(R.id.ivGallery);
        image = (ImageView) findViewById(R.id.ivImage);
        load = (ImageView) findViewById(R.id.ivLoad);
        loaded = (ImageView) findViewById(R.id.ivLoaded);
        submit = (Button) findViewById(R.id.btnSubmit_addImage);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent = new Intent(AddImage.this,SubActivity.class);
                //startActivityForResult();
                try {
                    //    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    Log.d("Oleg","Start Camera");
                    Intent in = cameraPhoto.takePhotoIntent();
                    startActivityForResult(in, CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Log.d("Oleg",e.toString());
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024,1024).getBitmap();
                    imageDecoded = ImageBase64.encode(bitmap);
                    images.add(imageDecoded);
                    Log.d(TAG,"List size is " + images.size());
                    Log.d(TAG,imageDecoded);
                } catch (FileNotFoundException e) {
                    Log.d("Oleg",e.toString());
                    Toast.makeText(getApplicationContext(),"Something wrong with upload",Toast.LENGTH_SHORT).show();
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.d(TAG,"Load");
             //   byte[] decodedString = Base64.decode(images.get(1), Base64.DEFAULT);
                byte[] decodedString = Base64.decode(imageDecoded, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                loaded.setImageBitmap(decodedByte);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
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
        Log.d("oleg","ONREQUEST");
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode == CAMERA_REQUEST){

                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.d("Oleg",e.toString());
                    Toast.makeText(getApplicationContext(),"Something wrong with image loader",Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, photoPath);
            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Log.d("Oleg",e.toString());
                    Toast.makeText(getApplicationContext(),"Something wrong with gallery loader",Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, photoPath);
            }
        }
    }
}
