package com.example.yugenshtil.finalproject.ItemSell;

/*
Class was created by Oleg Mytryniuk

 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//NEW!
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;


import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.OtherUseCases.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import layout.AddBook;
import layout.AddMaterial;
import layout.NoItemAdded;

import java.util.HashMap;
import java.util.Map;


public class AddItem extends Activity {

    private String id = "";
    private String token = "";


    // Common fields
    private String title = "";
    private String description = "";
    private double price = 0.0;
    private String type = "Book";
    private String program="";
    private String course="";
    //Fields for Book
    private String year="";
    private String publisher ="";
    private String author ="";


    //Edit Fields
    EditText etTitle;
    EditText etDescription;
    EditText etPrice;
    EditText etYear;
    EditText etCourse;
    EditText etPublisher;
    EditText etAuthor;
    Spinner program1;
    Spinner programMaterial1;


    private String ADDITEMURL="http://senecafleamarket.azurewebsites.net/api/Item";
    private String SENDIMAGEURL="http://senecafleamarket.azurewebsites.net/api/Item/";
    private String imageCode ="";

    ArrayList<String> itemTypeOptionsList = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    ImageView imageView;
    byte[] decodedString = null;

    private String itemType="";

    public ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("UserId","");
        token = sharedpreferences.getString("token","");


        // Spinner for selecting item
        Spinner spinner = (Spinner) findViewById(R.id.sp_itemtype);

        itemTypeOptionsList.add("Please select type");
        itemTypeOptionsList.add("Book");
        itemTypeOptionsList.add("Item");

        // Create the ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddItem.this, android.R.layout.simple_spinner_item, itemTypeOptionsList);

        // Set the Adapter
        spinner.setAdapter(arrayAdapter);

        // Set the ClickListener for Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                Fragment fragment;


                if (i == 0) {
                    Log.d("LOG :", "No choosen");
                    itemType = "empty";
                    fragment = new NoItemAdded();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();

                }

                if (i == 1) {
                    itemType = "Book";
                    //itemType assigned to test for when added button is clicked
                    fragment = new AddBook();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();

                }

                if (i == 2) {
                    itemType = "Material";
                    //itemType assigned to test for when added button is clicked
                    fragment = new AddMaterial();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();
                }


            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    // Adding a book
    public void addBook() {
         // Get values
        etTitle = (EditText) findViewById(R.id.et_addBookTitle);
        etDescription = (EditText) findViewById(R.id.et_AddBookDesc);
        etPrice= (EditText) findViewById(R.id.et_addBookPrice);
        etYear= (EditText) findViewById(R.id.et_AddBookYear);
        etCourse = (EditText) findViewById(R.id.et_addBook_course);
        etPublisher= (EditText) findViewById(R.id.et_addBookPublisher);
        etAuthor= (EditText) findViewById(R.id.et_addBookAuthor);
        program1 = (Spinner) findViewById(R.id.spinner1);

        if(!validateBook()){
             Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, "Please check all fields", duration);
            toast.show();
        }
        else{
            title = etTitle.getText().toString();
            price = Double.parseDouble(etPrice.getText().toString());
            publisher = etPublisher.getText().toString();
            program = program1.getSelectedItem().toString();
            description = etDescription.getText().toString();
            author = etAuthor.getText().toString();
            course = etCourse.getText().toString();
            year = etYear.getText().toString();

            pd = ProgressDialog.show(this, "", "Adding item, please wait..", true);

            Map<String, String> params = new HashMap();
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("SellerId", String.valueOf(id));
            params.put("Description", description);
            params.put("Price", String.valueOf(price));
            params.put("Type", itemType);
            params.put("CourseName", course);
            params.put("CourseProgram",program);
            params.put("BookTitle", title);
            params.put("BookYear", year);
            params.put("BookPublisher", publisher);
            params.put("BookAuthor", author);

            JSONObject parameters = new JSONObject(params);
            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.cancel();

                    if(decodedString == null){
                        Intent itemIntent = new Intent(AddItem.this, Sell.class);
                        startActivity(itemIntent);

                    }else{
                        JSONObject res = (JSONObject) response;
                        try {
                            String createdItemId = res.getString("ItemId");

                            addImageToItem(createdItemId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Book was successfully added", duration);
                    toast.show();
              }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();

                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization","Bearer "+token);
                    return headers;
                }
            };

            MySingleton.getInstance(AddItem.this).addToRequestQueue(jsObjPostRequest);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Item was added.", duration);
            toast.show();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent sellIntent = new Intent(AddItem.this, Sell.class);
             startActivity(sellIntent);
       }



    }


    public void addMaterial() {

        //Get values
        etTitle = (EditText) findViewById(R.id.et_addMaterialTitle);
        etDescription = (EditText) findViewById(R.id.et_addMaterialDesc);
        etPrice= (EditText) findViewById(R.id.et_addMaterialPrice);
        programMaterial1 = (Spinner) findViewById(R.id.spinner1Material);
        etCourse = (EditText) findViewById(R.id.et_addMaterialCourse);



        if(!validateMaterial()){

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, "Please check all fields", duration);
            toast.show();
        }

        else{

            title = etTitle.getText().toString();
            price = Double.parseDouble(etPrice.getText().toString());
            publisher = "NA";
            program = programMaterial1.getSelectedItem().toString();
            description = etDescription.getText().toString();
            author = "NA";
            course = etCourse.getText().toString();

            pd = ProgressDialog.show(this, "", "Adding item, please wait..", true);

            Map<String, String> params = new HashMap();
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("SellerId", String.valueOf(id));
            params.put("Description", description);
            params.put("Price", String.valueOf(price));
            params.put("Type", itemType);
            params.put("CourseName", course);
            params.put("CourseProgram",program);
            params.put("BookTitle", title);
            params.put("BookYear", "2016");
            params.put("BookPublisher", "NA");
            params.put("BookAuthor", "NA");

            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON is " + parameters);
            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.cancel();
                    Log.d("LOG :", "Response is " + response);



                    if(decodedString == null){
                        Intent itemIntent = new Intent(AddItem.this, Sell.class);
                        startActivity(itemIntent);

                    }else{
                        JSONObject res = (JSONObject) response;
                        try {
                            String createdItemId = res.getString("ItemId");

                            addImageToItem(createdItemId);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Item was successfully added", duration);
                    toast.show();



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    Log.d("LOG : ","I will add token " + token);
                    headers.put("Authorization","Bearer "+token);
                    return headers;
                }



            };

            MySingleton.getInstance(AddItem.this).addToRequestQueue(jsObjPostRequest);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Item was added.", duration);
            toast.show();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent sellIntent = new Intent(AddItem.this, Sell.class);
            startActivity(sellIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences("MyPrefs", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent mainMenuIntent = new Intent(AddItem.this,MainMenu.class);
            startActivity(mainMenuIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1990) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("Base64");
                    imageCode = newText;
                    if(itemType.equals("Book")){
                        decodedString = Base64.decode(imageCode, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView = (ImageView)findViewById(R.id.ivImage_AddBook);
                        if(decodedByte!=null){
                          imageView.setImageBitmap(decodedByte);
                        }
                    }
                    if(itemType.equals("Material")){
                        decodedString = Base64.decode(imageCode, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView = (ImageView)findViewById(R.id.ivImage_AddMaterial);
                        imageView.setImageBitmap(decodedByte);
                    }
                }
                break;
            }
        }
    }



    void addImageToItem(String id){

        StringRequest myRequest = new StringRequest(Request.Method.POST, SENDIMAGEURL+id + "/addimage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Intent itemIntent = new Intent(AddItem.this, Sell.class);
                startActivity(itemIntent);
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
                        e.printStackTrace();
                    }
                }

            }
        })




        {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
               return decodedString;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }

            public String getBodyContentType()
            {
                return "image/jpeg";
            }
        };

        MySingleton.getInstance(AddItem.this).addToRequestQueue(myRequest);






    }


    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
           Log.d("Oleg","Somethimg was clicked" + v);

        }
    };

    public void ButtonOnClick(View v) {

       if(v.getId()==R.id.btSave_AddBook){

           addBook();
       }
       else if(v.getId()==R.id.ivCamera_AddBook){
           Intent addImage = new Intent(AddItem.this, AddImage.class);
           startActivityForResult(addImage, 1990);
       }

       else if(v.getId()==R.id.btSave_AddMaterial){

           addMaterial();
        }
       else if(v.getId()==R.id.ivCamera_AddMaterial){
           Intent addImageToMaterial = new Intent(AddItem.this, AddImage.class);
           startActivityForResult(addImageToMaterial, 1990);
       }

    }

    public boolean validateBook(){
        boolean isValid = true;
        ArrayList<String> badWords = new ArrayList<>();
        badWords.add("heroin");
        badWords.add("crack");
        badWords.add("cocain");

        String title = etTitle.getText().toString();
        String content = etDescription.getText().toString();

        for (int i = 0; i < badWords.size(); i++) {
            String badWord = badWords.get(i);
            if(title.toLowerCase().contains(badWord)){
                etTitle.setError("No drugs please!");
                isValid = false;
            }
        }
        for (int i = 0; i < badWords.size(); i++) {
            String badWord = badWords.get(i);
            if(content.toLowerCase().contains(badWord)){
                etDescription.setError("No drugs please!");
                isValid = false;
            }
        }

        if(etTitle.getText().toString().equals("")){
            etTitle.setError("Do not leave title blank");
            isValid = false;
        }
        if(etDescription.getText().toString().equals("")){
           etDescription.setError("Do not leave description blank");
           isValid = false;
        }
        if(etAuthor.getText().toString().equals("")){
            etAuthor.setError("Do not leave Author blank");
            isValid = false;
        }
        if(!etYear.getText().toString().equals("")){
            int year = Integer.parseInt(etYear.getText().toString());
            if(year < 1900 || year >2016){
                etYear.setError("Wrong year. It should be between 1900-2016");
                isValid = false;
            }

        }else{
            etYear.setError("Do not leave Year blank");
            isValid = false;
        }

        return isValid;
    }

    public boolean validateMaterial(){
        boolean isValid = true;

        if(etTitle.getText().toString().equals("")){
            etTitle.setError("Do not leave title blank");
            isValid = false;
        }
        if(etDescription.getText().toString().equals("")){
            etDescription.setError("Do not leave description blank");
            isValid = false;
        }
        if(etCourse.getText().toString().equals("")){
            etCourse.setError("Do not leave course blank");
            isValid = false;
        }

        if(etPrice.getText().toString().equals("")){
            etPrice.setError("Do not leave price blank");
            isValid = false;
        }


        return isValid;
    }


}
