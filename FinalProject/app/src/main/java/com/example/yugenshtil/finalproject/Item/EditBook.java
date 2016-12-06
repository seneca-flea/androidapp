package com.example.yugenshtil.finalproject.Item;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditBook extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private String EDITITEMURL="http://senecafleamarket.azurewebsites.net/api/Item/";
    private String token="";
    private String title = "";
    private String description = "";
    private String price = "0.0";
    private String type = "Book";
    private String program="";
    private String bookYear = "";
    private String bookPublisher = "";
    private String bookAuthor = "";
    private String sellerId = "";
    private String itemId = "";


    //Edit Fields
    EditText etTitle;
    EditText etDescription;
    EditText etPrice;
    EditText etYear;
    EditText etPublisher;
    EditText etAuthor;
    Spinner programBook;
    Button btSaveBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token","");

        Bundle extras = getIntent().getExtras();

        // Get All Extras
        if (extras != null) {

            title = extras.getString("BookTitle");
            bookYear = extras.getString("BookYear");
            bookPublisher = extras.getString("BookPublisher");
            bookAuthor = extras.getString("BookAuthor").toString();
            sellerId = extras.getString("SellerId");
            itemId = extras.getString("ItemId");
            description = extras.getString("Description");
            price = extras.getString("Price");
            program = extras.getString("CourseProgram");
        }

        //Get all values in layout

        etTitle  = (EditText) findViewById(R.id.etEditBook_Title);
        etDescription  = (EditText)findViewById(R.id.etEditBook_Desc);
        etPrice  = (EditText) findViewById(R.id.etEditBook_Price);
        etYear = (EditText) findViewById(R.id.etEditBook_Year);
        etPublisher = (EditText) findViewById(R.id.etEditBook_Publisher);
        etAuthor = (EditText) findViewById(R.id.etEditBook_Author);
        programBook = (Spinner) findViewById(R.id.spinnerEditBook);
        btSaveBook = (Button) findViewById(R.id.btSave_EditBook);

        //Set all values

        etTitle.setText(title);
        etDescription.setText(description);
        etPrice.setText(price);
        etYear.setText(bookYear);
        etPublisher.setText(bookPublisher);
        etAuthor.setText(bookAuthor);

        btSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateBook()){

                    bookYear = etYear.getText().toString();
                    title = etTitle.getText().toString();
                    description = etDescription.getText().toString();
                    price = etPrice.getText().toString();
                    bookAuthor = etAuthor.getText().toString();
                    program = programBook.getSelectedItem().toString();



                    updateBook();
                }

            }
        });



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


    void updateBook(){
        Log.d("Oleg","Validated");



        Map<String, String> params = new HashMap();




        params.put("Title", title);
        params.put("ItemId",itemId);
        params.put("Price", String.valueOf(price));
        params.put("Description", description);
        params.put("Status", "Available");



        params.put("Type", "Book");
        params.put("CourseName", "NA");
        params.put("CourseProgram",program);
        params.put("BookTitle", title);
        params.put("BookYear", "1990");
        params.put("BookPublisher", bookPublisher);
        params.put("BookAuthor", bookAuthor);
 /* Check later
        */
       // params.put("SellerId", sellerId);




        JSONObject parameters = new JSONObject(params);
        Log.d("LOG : ", "JSON is " + parameters);
        JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, EDITITEMURL+itemId,parameters,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", " " +response);
                        Intent contactUsIntent = new Intent(EditBook.this,Sell.class);
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

        MySingleton.getInstance(EditBook.this).addToRequestQueue(jsObjPutRequest);


    }







}



                   /* mySpinner.setSelection(arrayAdapter.getPosition("Category 2")

                    String[] albums = getResources().getStringArray(R.array.program_array);
                    int position = 0;
                    for(int i = 0;i<albums.length;i++){
                        if(albums[i].contains(program))
                            position = i;
                        break;

                    }

                    Log.d("Oleg","position of " + program + " is " + position);
                    */