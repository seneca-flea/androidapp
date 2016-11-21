package com.example.yugenshtil.finalproject.item;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

import com.example.yugenshtil.finalproject.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AddImage extends AppCompatActivity {
    private ImageView upload, camera, gallery, image;
    private  final String TAG = this.getClass().getName();
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13342;
    final int GALLERY_REQUEST = 13343;
    final int MY_REQUEST_CODE =123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Oleeg","NOT GRANTED");
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        }
        else
            Log.d("Oleeg","GRANTED");
        int permissionCheck = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.INTERNET);
        Log.d("Oleg","Permission for INTERNET " + permissionCheck+"");
        int permissionCheckCamera = ContextCompat.checkSelfPermission(AddImage.this,
                Manifest.permission.CAMERA);
        Log.d("Oleg","Permission for CAMERA " + permissionCheck+"");
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        upload = (ImageView) findViewById(R.id.ivUpload);
        camera = (ImageView) findViewById(R.id.ivCamera);
        gallery = (ImageView) findViewById(R.id.ivGallery);
        image = (ImageView) findViewById(R.id.ivImage);
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
