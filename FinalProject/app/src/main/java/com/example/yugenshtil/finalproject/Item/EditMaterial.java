package com.example.yugenshtil.finalproject.Item;

import android.app.ProgressDialog;
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

public class EditMaterial extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public ProgressDialog pd;
    private String EDITITEMURL="http://senecafleamarket.azurewebsites.net/api/Item/";
    private String token="";
    private String title = "";
    private String description = "";
    private String price = "0.0";
    private String type = "Material";
    private String program="";
    private String sellerId = "";
    private String itemId = "";


    //Edit Fields
    EditText etTitle;
    EditText etDescription;
    EditText etPrice;
    Spinner programMaterial;
    Button btSaveMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_material);


        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token","");

        Bundle extras = getIntent().getExtras();

        // Get All Extras
        if (extras != null) {

            title = extras.getString("Title");
            sellerId = extras.getString("SellerId");
            itemId = extras.getString("ItemId");
            description = extras.getString("Description");
            price = extras.getString("Price");
            program = extras.getString("CourseProgram");

        }

        //Get all values in layout

        etTitle  = (EditText) findViewById(R.id.etEditMaterial_Title);
        etDescription  = (EditText)findViewById(R.id.etEditMaterial_Desc);
        etPrice  = (EditText) findViewById(R.id.etEditMaterial_Price);
        programMaterial = (Spinner) findViewById(R.id.spinnerEditMaterial);
        btSaveMaterial = (Button) findViewById(R.id.btSave_EditMaterial);

        //Set all values

        etTitle.setText(title);
        etDescription.setText(description);
        etPrice.setText(price);

        btSaveMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateMaterial()){

                    title = etTitle.getText().toString();
                    description = etDescription.getText().toString();
                    price = etPrice.getText().toString();
                    program = programMaterial.getSelectedItem().toString();



                    updateMaterial();
                }

            }
        });
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


    void updateMaterial(){
        Log.d("Oleg","Validated");
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Map<String, String> params = new HashMap();

        params.put("Title", title);
        params.put("ItemId",itemId);
        params.put("Price", String.valueOf(price));
        params.put("Description", description);
        params.put("Status", "Available");
        params.put("CourseName", "NA");
        params.put("CourseProgram",program);

 /* Check later
        */
        // params.put("SellerId", sellerId);

        JSONObject parameters = new JSONObject(params);
        Log.d("LOG : ", "JSON is " + parameters);
        JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, EDITITEMURL+itemId,parameters,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        // response
                        Log.d("Response", " " +response);
                        Intent contactUsIntent = new Intent(EditMaterial.this,Sell.class);
                        startActivity(contactUsIntent);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, "Item was successfully updated", duration);
                        toast.show();

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, "Item was not updated", duration);
                toast.show();
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

        MySingleton.getInstance(EditMaterial.this).addToRequestQueue(jsObjPutRequest);


    }


}
