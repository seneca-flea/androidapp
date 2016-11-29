package com.example.yugenshtil.finalproject.Item;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private String EDITITEMURL="http://senecaflea.azurewebsites.net/api/Item/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        final EditText etTitle = (EditText) findViewById(R.id.etTitle_editItem);
        final EditText etDescription = (EditText) findViewById(R.id.etDescription_editItem);
        final EditText etPrice = (EditText) findViewById(R.id.etPrice_editItem);
        final Button btSaveChanges = (Button) findViewById(R.id.btSaveChanges_editItem);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            SellerId = extras.getString("SellerId");
            ItemId = extras.getString("ItemId");
            Log.d("item id", ItemId);
            Title = extras.getString("Title");
            Description = extras.getString("Description");
            Price = extras.getString("Price");


            //The key argument here must match that used in the other activity
        }

        etTitle.setText(Title);
        etDescription.setText(Description);
        etPrice.setText(Price);

        btSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Title = etTitle.getText().toString();
                Description  = etDescription.getText().toString();
                Price = etPrice.getText().toString();

                updateItem(ItemId);
            }
        });


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
        );

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
}
