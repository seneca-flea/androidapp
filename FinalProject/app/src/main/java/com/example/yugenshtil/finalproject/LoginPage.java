package com.example.yugenshtil.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends Activity {

    private String userName="";
    private String password="";
    private String LOGINUSERURL="http://senecaflea.azurewebsites.net/api/User";
    private boolean inputIsValid = false;
    private boolean userFound = false;
    private String id="";
    private JSONArray users= null;
    private String errors = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        final EditText etUserName = (EditText) findViewById(R.id.loginETemail);
        final EditText etPassword = (EditText) findViewById(R.id.loginETPassword);
        final Button bLogin = (Button) findViewById(R.id.loginBLogin);
        final TextView forgotPasswordLink = (TextView) findViewById(R.id.loginTVforgotPassword);
        final TextView registerLink = (TextView) findViewById(R.id.loginTVregister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(LoginPage.this,RegistrationPage.class);
                startActivity(registrationIntent);
            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginPage.this,ForgotPassword.class);
                startActivity(forgotPasswordIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();
                if(isInputValid()){
                    receiveJson();
                    if(isUserFound()){
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "User was successfully login. Redirection to user menu", duration);
                        toast.show();

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Intent userMenuIntent = new Intent(LoginPage.this, UserMenu.class);
                        userMenuIntent.putExtra("id", id);
                        startActivity(userMenuIntent);



                    }else{
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "User was not found", duration);
                        toast.show();

                    }



                }
                else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, errors, duration);
                    toast.show();
                }
            }
        });
    }


    public boolean isUserFound(){
        boolean userIsFound = false;

        if(users!=null) {
            Log.d("Oleg", "size " + users.length());
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = (JSONObject) users.get(i);
                    Log.d("Oleg", user.getString("Email") + "/" + userName);
                    if (user.getString("Email").equals(userName)) {
                        userIsFound = true;
                        id = user.getString("UserId");
                        i = users.length();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


        return userIsFound;
    }

    public void receiveJson(){

       JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, LOGINUSERURL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {




                if(response!=null){
                    users = response;

                }else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "JSON RETURNED NULL", duration);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Oleg","error" + error);

            }
        });

        MySingleton.getInstance(LoginPage.this).addToRequestQueue(jsObjRequest);

        Log.d("Oleg", "clicked");


    }

    public boolean isInputValid(){
        boolean inputIsValid  = true;
        errors="";
        if(userName.equals("") || !userName.contains("@")){
            errors += "Please, provide correct username\n";
            inputIsValid = false;

        }
        if(password.equals("")|| password.length()<3){
            errors += "Please, provide correct password\n";
            inputIsValid = false;

        }


        return inputIsValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
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
