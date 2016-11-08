package com.example.yugenshtil.finalproject.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yugenshtil.finalproject.LoginPage;
import com.example.yugenshtil.finalproject.MySingleton;
import com.example.yugenshtil.finalproject.R;
//import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.model.DerpData;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddItem extends Activity {

    private String id = "";
    private String title = "";
    private String description = "";
    private double price = 0.0;
    private String errors = "";
    private String ADDITEMURL="http://senecaflea.azurewebsites.net/api/Item";
    private static final String PROTOCOL_CHARSET = "utf-8";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("Oleg", "Preferences " + sharedpreferences);
        if (sharedpreferences.contains("id")) {
            id = sharedpreferences.getString("id", "");
        }

        final Button btAddItem = (Button) findViewById(R.id.addItemBTaddItem);
        final EditText etTitle = (EditText) findViewById(R.id.addItemETtitle);
        final EditText etDescription = (EditText) findViewById(R.id.addItemETDescription);
        final EditText etPrice = (EditText) findViewById(R.id.addItemETprice);

        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = etTitle.getText().toString();
                description = etDescription.getText().toString();
                price = Double.parseDouble(etPrice.getText().toString());

                if (validateInput()) {
                    Map<String, String> params = new HashMap();
                    params.put("Title", title);
                    params.put("Status", "Available");
                    params.put("Description", description);
                    params.put("Price", String.valueOf(price));
                    params.put("SellerId", String.valueOf(id));

                    JSONObject parameters = new JSONObject(params);
                    Log.d("Oleg ", "JSON is " + parameters);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {



                            Log.d("Oleg", "Response is " + response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("Oleg", "Error is " + error);
                            // TODO Auto-generated method stub

                        }



                    });

                    MySingleton.getInstance(AddItem.this).addToRequestQueue(jsObjRequest);

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Item was added. Redirection to Items page", duration);
                    toast.show();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent sellIntent = new Intent(AddItem.this,Sell.class);
                    // RegistrationPage.this.startActivity(loginIntent);
                    startActivity(sellIntent);

                } else {

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, errors, duration);
                    toast.show();

                }
            }
        });

    }

    public boolean validateInput(){
        boolean inputIsValid = true;
        // Log.d("Oleg","firstNAme va" +firstName);
        errors="";
        if(title.equals("")){
            //  Log.d("Oleg","firstNAme" +firstName);
            errors+="Please, do not leave title blank\n";
            inputIsValid = false;
        }
        if(description.equals("")){
            //  Log.d("Oleg","lastName" +lastName);
            errors+="Please, do not leave description blank\n";
            inputIsValid = false;
        }
        if(price==0 || price==0.0){
            //  Log.d("Oleg","email" +email);
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

        return super.onOptionsItemSelected(item);
    }
}
