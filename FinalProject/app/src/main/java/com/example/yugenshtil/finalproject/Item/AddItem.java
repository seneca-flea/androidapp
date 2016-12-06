package com.example.yugenshtil.finalproject.Item;

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

//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.useCases.Sell;
//import com.example.yugenshtil.finalproject.adapter.DerpAdapter;

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
    //Fields for Book
    private int year=2016;
    private String publisher ="";
    private String author ="";

    //Edit Fields
    EditText etTitle;
    EditText etDescription;
    EditText etPrice;
    EditText etYear;
    EditText etPublisher;
    EditText etAuthor;
    Spinner program1;
    Spinner programMaterial1;

    private String errors = "";
    private String ADDITEMURL="http://senecafleamarket.azurewebsites.net/api/Item";
    private String SENDIMAGEURL="http://senecafleamarket.azurewebsites.net/api/Item/";
    private String ADDMATERIALMURL="http://senecafleamarket.azurewebsites.net/api/Item";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private String imageCode ="";
    private Spinner spinner1;
    Fragment fragment;
    ArrayList<String> difficultyLevelOptionsList = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    ImageView imageView;
    byte[] decodedString = null;
  //  private Button ivSaveBook;

    //ADDED!
    //itemType assigned to test what we are adding when the add button is clicked(a book or an item)
    private String itemType="";

    public ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("UserId","");
        token = sharedpreferences.getString("token","");

     //   final Button btSave = (Button) findViewById(R.id.btAddItem_Save);
        //button so save image
        final ImageButton ibImage = (ImageButton) findViewById(R.id.ibAddItem_Image);

//        addListenerOnButton();
 //       addListenerOnSpinnerItemSelection();


        ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addImage = new Intent(AddItem.this, AddImage.class);
                startActivityForResult(addImage, 1990);
            }
        });

        // Spinner for selecting item
        Spinner spinner = (Spinner) findViewById(R.id.sp_itemtype);

        difficultyLevelOptionsList.add("Please select type");
        difficultyLevelOptionsList.add("Book");
        difficultyLevelOptionsList.add("Item");
        //difficultyLevelOptionsList.add("Too Hard");

        // Create the ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddItem.this, android.R.layout.simple_spinner_item, difficultyLevelOptionsList);

        // Set the Adapter
        spinner.setAdapter(arrayAdapter);

        // Set the ClickListener for Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                Fragment fragment;


                if (i == 0) {
                    Log.d("LOG :", "No choosen");
                    //itemType assigned to test for when added button is clicked
                    itemType = "empty";
                    fragment = new NoItemAdded();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();

                }

                if (i == 1) {
                    itemType = "Book";
                    Log.d("LOG :", "Book");
                    //itemType assigned to test for when added button is clicked
                    fragment = new AddBook();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();

                }

                if (i == 2) {
                    itemType = "Material";
                    Log.d("LOG :", "Meterial");
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

        // Waiting for Save Button to be clicked
   /*     btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemType == "empty") {
                    Log.d("LOG : ", "Adding item: attempted to save item on empty fragment");
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "You should choose type before saving", duration);
                    toast.show();

                } else if (itemType == "Book") {
                    Log.d("LOG :", "Adding book");
                    addBook();

                } else if (itemType == "Material") {
                    Log.d("LOG :", "Adding Material");
                    addMaterial();

                } else {
                    Log.d("LOG :", "Something went seriously wrong");
                }
            }
        });*/
    }

    // Adding a book
    public void addBook() {
        Log.d("LOG :", "addBook was called ");
        // Get values
        etTitle = (EditText) findViewById(R.id.et_addBookTitle);
        etDescription = (EditText) findViewById(R.id.et_AddBookDesc);
        etPrice= (EditText) findViewById(R.id.et_addBookPrice);
        etYear= (EditText) findViewById(R.id.et_AddBookYear);
        etPublisher= (EditText) findViewById(R.id.et_addBookPublisher);
        etAuthor= (EditText) findViewById(R.id.et_addBookAuthor);
        program1 = (Spinner) findViewById(R.id.spinner1);

        if(!validateBook()){
            Log.d("Oleg","Not Validated");
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, "Please check all fields", duration);
            toast.show();
        }
        else{
            Log.d("Oleg","Validated");
            title = etTitle.getText().toString();
            price = Double.parseDouble(etPrice.getText().toString());
            publisher = etPublisher.getText().toString();
            program = program1.getSelectedItem().toString();
            description = etDescription.getText().toString();
            author = etAuthor.getText().toString();

            pd = ProgressDialog.show(this, "", "Adding item, please wait..", true);

            Map<String, String> params = new HashMap();
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("SellerId", String.valueOf(id));
            params.put("Description", description);
            params.put("Price", String.valueOf(price));
            params.put("Type", itemType);
            params.put("CourseName", "NA");
            params.put("CourseProgram",program);
            params.put("BookTitle", title);
            params.put("BookYear", "1990");
            params.put("BookPublisher", publisher);
            params.put("BookAuthor", author);

            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON is " + parameters);
            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.cancel();
                    Log.d("LOG :", "Response is " + response);



                    if(decodedString == null){
                        Log.d("Oleg","No images");
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
                    Log.d("LOG :", "Error is " + error);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    Log.d("Oleg","I will add token " + token);
                    headers.put("Authorization","Bearer "+token);
                    // params.put("username",email);
                    //params.put("password", password);

                    Log.d("Token ", headers.toString());
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
            // RegistrationPage.this.startActivity(loginIntent);
            startActivity(sellIntent);
       }



    }


    public void addMaterial() {
        Log.d("LOG :", "addMaterial was called");

        //Get values
        etTitle = (EditText) findViewById(R.id.et_addMaterialTitle);
        etDescription = (EditText) findViewById(R.id.et_addMaterialDesc);
        etPrice= (EditText) findViewById(R.id.et_addMaterialPrice);
        programMaterial1 = (Spinner) findViewById(R.id.spinner1Material);



        if(!validateMaterial()){
            Log.d("Oleg","Not Validated");
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, "Please check all fields", duration);
            toast.show();
        }

        else{
            Log.d("Oleg","Validated");
            title = etTitle.getText().toString();
            price = Double.parseDouble(etPrice.getText().toString());
            publisher = "NA";
            program = programMaterial1.getSelectedItem().toString();
            description = etDescription.getText().toString();
            author = "NA";

            pd = ProgressDialog.show(this, "", "Adding item, please wait..", true);

            Map<String, String> params = new HashMap();
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("SellerId", String.valueOf(id));
            params.put("Description", description);
            params.put("Price", String.valueOf(price));
            params.put("Type", itemType);
            params.put("CourseName", "NA");
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
                        Log.d("Oleg","No images");
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
                    Log.d("LOG :", "Error is " + error);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    Log.d("Oleg","I will add token " + token);
                    headers.put("Authorization","Bearer "+token);
                    // params.put("username",email);
                    //params.put("password", password);

                    Log.d("Token ", headers.toString());
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
            // RegistrationPage.this.startActivity(loginIntent);
            startActivity(sellIntent);



        }

    }

    public boolean validateInput(){
        boolean inputIsValid = true;
        errors="";
        if(title.equals("")){
            errors+="Please, do not leave title blank\n";
            inputIsValid = false;
        }
        if(description.equals("")){
            errors+="Please, do not leave description blank\n";
            inputIsValid = false;
        }
        //NEW! test for price < 0
        if(price==0 || price==0.0 || price < 0){
            errors+="Please, provide correct price for the item\n";
            inputIsValid = false;
        }

        return inputIsValid;
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
        Log.d("oleg","we got");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1990) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("Base64");
                    imageCode = newText;
                    Log.d("Oleg","Base64 is " + newText);
                    if(type.equals("Book")){
                        Log.d("oleg","Type is Book");
                        decodedString = Base64.decode(imageCode, Base64.DEFAULT);
                        Log.d("Oleg","Decoded BYTES " + decodedString );

                        //   sendPicture(decodedString);



                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView = (ImageView)findViewById(R.id.ivImage_AddBook);
                        if(decodedByte!=null){
                            imageView.setImageBitmap(decodedByte);


                        }





                    }
                    if(type.equals("Material")){

                        Log.d("oleg","Type is Material");

                        decodedString = Base64.decode(imageCode, Base64.DEFAULT);
                        Log.d("Oleg","Decoded BYTES " + decodedString );

                        //   sendPicture(decodedString);



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
        Log.d("BYte","ItemId is " + id);
        Log.d("BYte","getting picture in bytes " + decodedString);

        StringRequest myRequest = new StringRequest(Request.Method.POST, SENDIMAGEURL+id + "/addimage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LOG :", "Response is " + response);
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
                return decodedString;
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

        MySingleton.getInstance(AddItem.this).addToRequestQueue(myRequest);






    }


    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
           Log.d("Oleg","Somethimg was clicked" + v);

        }
    };

    public void ButtonOnClick(View v) {
        Log.d("OLeg","ID is " + v.getId());
       if(v.getId()==R.id.btSave_AddBook){
           Log.d("Oleg","clicked add book Save");
           addBook();
       }
       else if(v.getId()==R.id.ivCamera_AddBook){
           Log.d("Oleg","clicked add book Camera");
           Intent addImage = new Intent(AddItem.this, AddImage.class);
           startActivityForResult(addImage, 1990);
       }

       else if(v.getId()==R.id.btSave_AddMaterial){
           Log.d("Oleg","clicked add Material Save");
           addMaterial();
        }
       else if(v.getId()==R.id.ivCamera_AddMaterial){
           Log.d("Oleg","clicked Material Camera");
           Intent addImageToMaterial = new Intent(AddItem.this, AddImage.class);
           startActivityForResult(addImageToMaterial, 1990);
       }

    }

   /* public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(MyAndroidAppActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }


*/

    public boolean validateBook(){
        boolean isValid = true;

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

        if(etPrice.getText().toString().equals("")){
            etPrice.setError("Do not leave price blank");
            isValid = false;
        }


        return isValid;
    }


}
