package com.example.yugenshtil.finalproject.Item;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditItem extends Activity {

    private String ItemId="";
    private String Title="";
    private String SellerId="";
    private String Description="";
    private String Price="";
    private String EDITITEMURL="http://senecafleamarket.azurewebsites.net/api/Item/";
    private String token="";

    String title = "";
    String description = "";
    String price = "0.0";
    private String type = "";
    private String program="";
    private String bookYear = "";
    private String bookPublisher = "";
    private String bookAuthor = "";
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


    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token","");

/*
        final EditText etTitle = (EditText) findViewById(R.id.etTitle_editItem);
        final EditText etDescription = (EditText) findViewById(R.id.etDescription_editItem);
        final EditText etPrice = (EditText) findViewById(R.id.etPrice_editItem);
        final Button btSaveChanges = (Button) findViewById(R.id.btSaveChanges_editItem);
*/

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("Type");


            if(type.equals("Book")){
                Log.d("Oleg", "Type is book");


                title = extras.getString("BookTitle");
                bookYear = extras.getString("BookYear");
                bookPublisher = extras.getString("BookPublisher");
                bookAuthor = extras.getString("BookAuthor").toString();
                SellerId = extras.getString("SellerId");
                ItemId = extras.getString("ItemId");
                Log.d("item id", ItemId);
                title = extras.getString("Title");
                description = extras.getString("Description");
                price = extras.getString("Price");

              /*




                etTitle  = (EditText) findViewById(R.id.etEditBook_Title);
                etDescription  = (EditText)findViewById(R.id.etEditBook_Desc);
                etPrice  = (EditText) findViewById(R.id.etEditBook_Price);
                program1 = (Spinner) findViewById(R.id.spinnerEditBook);
                etYear = (EditText) findViewById(R.id.etEditBook_Year);
                etPublisher = (EditText) findViewById(R.id.etEditBook_Publisher);
                etAuthor = (EditText) findViewById(R.id.etEditBook_Author);

                 etTitle.setText(title);
                etDescription.setText(description, TextView.BufferType.EDITABLE);
                          etPrice.setText(price);
                //        etYear.setText(bookYear, TextView.BufferType.EDITABLE);

                String Oleg = "Ole";
                etPublisher.setText(Oleg);
                etAuthor.setText(bookAuthor, TextView.BufferType.EDITABLE);

*/








             //   setBook();
            }else{
                Log.d("Oleg", "Type is item");
                SellerId = extras.getString("SellerId");
                ItemId = extras.getString("ItemId");
                Log.d("item id", ItemId);
                title = extras.getString("Title");
                description = extras.getString("Description");
                price = extras.getString("Price");
                setMaterial();

            }




            //The key argument here must match that used in the other activity
        }




/*

        btSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Title = etTitle.getText().toString();
                Description  = etDescription.getText().toString();
                Price = etPrice.getText().toString();

                updateItem(ItemId);
            }
        });
*/

    }

    void updateItem(String itemId){

        Map<String, String> params = new HashMap();
        params.put("ItemId", itemId);
        params.put("Title", Title);
        params.put("Description", Description);
        params.put("Price", Price);
        params.put("Status", "Available");

        JSONObject parameters = new JSONObject(params);
        Log.d("Oleg ", "JSON is " + parameters);

        JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, EDITITEMURL+itemId,parameters,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", " " +response);
                        Intent contactUsIntent = new Intent(EditItem.this,Sell.class);
                        startActivity(contactUsIntent);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "Error is " + error);
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

        MySingleton.getInstance(EditItem.this).addToRequestQueue(jsObjPutRequest);
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

            Intent mainMenuIntent = new Intent(EditItem.this,MainMenu.class);
            startActivity(mainMenuIntent);



        }

        return super.onOptionsItemSelected(item);
    }

    public void setBook(){






    }

    public void setMaterial(){



        etTitle  = (EditText) findViewById(R.id.etEditMaterial_Title);
        etDescription  = (EditText) findViewById(R.id.etEditMaterial_Desc);
        etPrice  = (EditText) findViewById(R.id.etEditMaterial_Price);
        program1 = (Spinner) findViewById(R.id.spinnerEditMaterial);

        etTitle.setText(title);
        etDescription.setText(description);
        etPrice.setText(price);





    }

    public void EditButtonOnClick(View v) {
        Log.d("OLeg","ID is " + v.getId());
        if(v.getId()==R.id.btSave_EditBook){
            Log.d("Oleg","clicked add book Save");
            saveBook();
        }

        else if(v.getId()==R.id.btSave_EditMaterial) {
            Log.d("Oleg", "clicked add Material Save");
            //addMaterial();
        }

    }

    public void saveBook() {
        Log.d("LOG :", "SaveBook was called ");
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
            price = etPrice.getText().toString();
            publisher = etPublisher.getText().toString();
            program = program1.getSelectedItem().toString();
            description = etDescription.getText().toString();
            author = etAuthor.getText().toString();


            Map<String, String> params = new HashMap();
            params.put("Title", title);
            params.put("Status", "Available");
            params.put("SellerId", SellerId);
            params.put("Description", description);
            params.put("Price", String.valueOf(price));
            params.put("Type", "Material");
            params.put("CourseName", "NA");
            params.put("CourseProgram",program);
            params.put("BookTitle", title);
            params.put("BookYear", "1990");
            params.put("BookPublisher", publisher);
            params.put("BookAuthor", author);

            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON is " + parameters);
            JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, EDITITEMURL+ItemId,parameters,
                    new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    // response
                    Log.d("Response", " " +response);
                    Intent contactUsIntent = new Intent(EditItem.this,Sell.class);
                    startActivity(contactUsIntent);
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Book was successfully updated", duration);
                    toast.show();

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
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

            MySingleton.getInstance(EditItem.this).addToRequestQueue(jsObjPutRequest);
        }



    }

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




}
