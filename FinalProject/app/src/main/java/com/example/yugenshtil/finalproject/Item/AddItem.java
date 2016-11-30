package com.example.yugenshtil.finalproject.Item;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//NEW!
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;

//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.useCases.Sell;
//import com.example.yugenshtil.finalproject.adapter.DerpAdapter;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONObject;

import layout.AddBook;
import layout.AddMaterial;
import layout.NoItemAdded;

import java.util.HashMap;
import java.util.Map;


public class AddItem extends Activity {

    private String id = "";
    private String token = "";
    public String type = "Book";
    private String title = "";
    private String description = "";
    private double price = 0.0;
    private String program ="";
    private String pickUpDate = null;
    private String course ="";
    private String publisher ="";
    private String errors = "";
    private String ADDITEMURL="http://senecafleamarket.azurewebsites.net/api/Item";
    private String ADDMATERIALMURL="http://senecafleamarket.azurewebsites.net/api/Item";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private String imageCode ="";
    Fragment fragment;
    ArrayList<String> difficultyLevelOptionsList = new ArrayList<String>();
    SharedPreferences sharedpreferences;

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

        final Button btSave = (Button) findViewById(R.id.btAddItem_Save);
        //button so save image
        final ImageButton ibImage = (ImageButton) findViewById(R.id.ibAddItem_Image);

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
                // TODO Auto-generated method stub

            }

        });

        // Waiting for Save Button to be clicked
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemType == "empty") {
                    Log.d("LOG : ", "Adding item: attempted to save item on empty fragment");

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
        });
    }
    public void addBook() {
        Log.d("LOG :", "addBook was called ");
        TextView bookTitle = (TextView) findViewById(R.id.tv_addBookTitle);
        TextView bookPrice = (TextView) findViewById(R.id.tv_addBookPrice);
        TextView bookCourse = (TextView) findViewById(R.id.tv_addBookCourse);
        TextView bookPublisher = (TextView) findViewById(R.id.tv_addBookPublisher);
        TextView bookProgram = (TextView) findViewById(R.id.tv_addBookProgram);
        TextView bookDate = (TextView) findViewById(R.id.tv_addBookPickUpDate);
        TextView bookDescription = (TextView) findViewById(R.id.tv_AddBookDesc);


        title = bookTitle.getText().toString();
        course = bookCourse.getText().toString();
        price = Double.parseDouble(bookPrice.getText().toString());
        publisher = bookPublisher.getText().toString();
        pickUpDate = bookDate.getText().toString();
        program = bookProgram.getText().toString();
        description = bookDescription.getText().toString();
        if (validateInput()) {
            pd = ProgressDialog.show(this, "", "Adding item, please wait..", true);

            Map<String, String> params = new HashMap();
            params.put("SellerId", String.valueOf(id));
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("Price", String.valueOf(price));
            params.put("Program",program);
            params.put("Course", course);
            params.put("Publisher", publisher);
            params.put("PickUpDate", pickUpDate);
            params.put("Description", description);
            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON is " + parameters);
            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.cancel();
                    Log.d("LOG :", "Response is " + response);
                    Intent itemIntent = new Intent(AddItem.this, Sell.class);
                    startActivity(itemIntent);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();
                    Log.d("LOG :", "Error is " + error);
                }
            }){
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

        } else {

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, errors, duration);
            toast.show();

        }
    }
    public void addMaterial() {
        Log.d("LOG :", "addMaterial was called");
        TextView MaterialTitle = (TextView) findViewById(R.id.tv_addMaterialTitle);
        TextView MaterialPrice = (TextView) findViewById(R.id.tv_addMaterialPrice);
        TextView MaterialDate = (TextView) findViewById(R.id.tv_addMaterialPickUpDate);
        TextView MaterialProgram = (TextView) findViewById(R.id.tv_addMaterialProgram);
        TextView MaterialDescription = (TextView) findViewById(R.id.tv_AddMaterialDesc);

        title = MaterialTitle.getText().toString();
        program = MaterialProgram.getText().toString();
        price = Double.parseDouble(MaterialPrice.getText().toString());
        pickUpDate = MaterialDate.getText().toString();
        program = MaterialProgram.getText().toString();
        description = MaterialDescription.getText().toString();
        if (validateInput()) {
            pd = ProgressDialog.show(this, "", "Adding item, please wait..", true);

            Map<String, String> params = new HashMap();
            params.put("SellerId", String.valueOf(id));
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("Price", String.valueOf(price));
            params.put("Program",program);
            params.put("PickUpDate", pickUpDate);
            params.put("Description", description);
            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON is " + parameters);
            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.cancel();
                    Log.d("LOG :", "Response is " + response);
                    Intent itemIntent = new Intent(AddItem.this, Sell.class);
                    startActivity(itemIntent);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();
                    Log.d("LOG :", "Error is " + error);
                }
            }){
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

        } else {

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, errors, duration);
            toast.show();

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
                }
                break;
            }
        }
    }


}
