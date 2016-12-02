package com.example.yugenshtil.finalproject.Item;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.useCases.Sell;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddImage extends AppCompatActivity {
    private ImageView upload, camera, gallery, image,load, loaded,imageFromServer;
    private Button submit,getFromServer;
    private  final String TAG = this.getClass().getName();
    List<String> images = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    String token = "";
    private String SENDIMAGEURL="http://senecafleamarket.azurewebsites.net/api/Item/";
    byte[] imageInBytes=null;

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
        loaded = (ImageView) findViewById(R.id.decod);
        submit = (Button) findViewById(R.id.btnSubmit_addImage);
        getFromServer = (Button) findViewById(R.id.btGetFromServer_AddImage);
        imageFromServer = (ImageView) findViewById(R.id.imageFromServer);
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
                Log.d("Oleg","Uploading");
                try {
                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024,1024).getBitmap();
                    imageDecoded = ImageBase64.encode(bitmap);
                   // images.add(imageDecoded);
                    Log.d(TAG,"List size is " + images.size());
                    Log.d(TAG,imageDecoded);
                    Toast.makeText(getApplicationContext(),"Picture was uploaded",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    Log.d("Oleg",e.toString());
                    Toast.makeText(getApplicationContext(),"Something wrong with upload",Toast.LENGTH_SHORT).show();
                }
            }
        });


        getFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Uploading");

                   getImageFromServer();

            }
        });

        load.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.d(TAG,"Load");

                // DONOT DELETE!!!
             //   byte[] decodedString = Base64.decode(images.get(1), Base64.DEFAULT);
                imageDecoded = "neZZ";
               imageInBytes = Base64.decode(imageDecoded, Base64.DEFAULT);
                Log.d("Oleg","Decoded BYTES " + imageInBytes );

            sendPicture();



           //    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //   loaded.setImageBitmap(decodedByte);
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



    // NEW

    void sendPicture(){

        Log.d("BYte","getting picture in bytes " + imageInBytes);

        StringRequest myRequest = new StringRequest(Request.Method.POST, SENDIMAGEURL+"9/addimage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LOG :", "Response is " + response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse.data!=null) {

                    String statusCode = String.valueOf(error.networkResponse.statusCode);

                    try {
                       String body = new String(error.networkResponse.data,"UTF-8");
                        Log.d("ERROR ",""+body);
                    } catch (UnsupportedEncodingException e) {
                        Log.d("Oleg","Exception Error response (Message) is " + e.getMessage());
                        e.printStackTrace();
                    }
                }



                Log.d("LOG :", "Error is " + error);
            }
        })




        {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Log.d("Oleg","PLEASE BYTES");
                return imageInBytes;
            };/**/


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.d("Oleg","I will add token " + token);
                headers.put("Authorization","Bearer "+token);
              //  headers.put("Content-Type","image/jpeg");
                // params.put("username",email);
                //params.put("password", password);

              //  Log.d("Token ", headers.toString());
                return headers;
            }

           public String getBodyContentType()
            {
                return "image/jpeg";
            }
        };

        MySingleton.getInstance(AddImage.this).addToRequestQueue(myRequest);





    }


    public void getImageFromServer(){


        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, "http://senecafleamarket.azurewebsites.net/api/Item/9/", null, new Response.Listener<JSONArray>() {


            String myItemsList="";

            @Override
            public void onResponse(JSONArray response) {

    String photoString ="";
                if (response != null) {

                    JSONArray items = response;

                    if (items != null) {
                        Log.d("Oleg", "size " + items.length());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                              photoString= item.getString("Photo");
                                Log.d("PHOTOSTRING",""+photoString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);
                    }

                   // byte[] decodedString = Base64.decode(images.get(1), Base64.DEFAULT);
                   // imageDecoded = "";
                    byte[] decodedString = Base64.decode(photoString, Base64.DEFAULT);
                    Log.d("Oleg","Decoded BYTES " + decodedString );

                    //   sendPicture(decodedString);



                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageFromServer.setImageBitmap(decodedByte);


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                Log.d("Oleg","error" + error);

            }
        }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.d("Oleg","I will add token " + token);
                headers.put("Authorization","Bearer "+token);
                headers.put("Accept","image/*");

                // params.put("username",email);
                //params.put("password", password);

                Log.d("Token ", headers.toString());
                return headers;
            }



        };

        MySingleton.getInstance(AddImage.this).addToRequestQueue(jsObjRequest);



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
