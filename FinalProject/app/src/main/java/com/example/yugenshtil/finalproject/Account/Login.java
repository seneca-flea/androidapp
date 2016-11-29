package com.example.yugenshtil.finalproject.Account;

/*
The class was designed by Oleg Mytryniuk
omytryniuk@myseneca.ca

The class is responsible to manage user login to the system

 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.ForgotPassword;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {

    private String email="";
    private String password="";
    private String LOGINUSERURL="http://senecaflea.azurewebsites.net/api/User";
    private boolean inputIsValid = false;
    private boolean userFound = false;
    private String id="";
    private String fullName="";
    private JSONArray users= null;
    private String errors = "";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        final EditText etEmail = (EditText) findViewById(R.id.etLogin_Email);
        final EditText etPassword = (EditText) findViewById(R.id.etLogin_Password);
        final Button btLogin = (Button) findViewById(R.id.btLogin_Login);
        final TextView tvForgotPassword = (TextView) findViewById(R.id.tvLogin_ForgotPassword);
        final TextView tvRegister = (TextView) findViewById(R.id.tvLogin_SignIn);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(Login.this,Registration.class);
                startActivity(registrationIntent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(Login.this,ForgotPassword.class);
                startActivity(forgotPasswordIntent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if(isInputValid()){
                    checkUserIsValid();
                 //   receiveJson();



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
                    Log.d("Oleg", user.getString("Email") + "/" + email);
                    if (user.getString("Email").equals(email)) {
                        userIsFound = true;
                        id = user.getString("UserId");
                        fullName=user.getString("FirstName") + " " + user.getString("LastName");
                        i = users.length();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


        return userIsFound;
    }

    public void checkUserIsValid(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);

       JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, LOGINUSERURL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {



                pd.cancel();
                if(response!=null){
                    users = response;
                    if(isUserFound()){
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("id", id);
                        editor.putString("fullName", fullName);
                        editor.commit();

                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "User was successfully login. Redirection to user menu", duration);
                        toast.show();

                        Intent userMenuIntent = new Intent(Login.this, UserMenu.class);
                        userMenuIntent.putExtra("id", id);
                        userMenuIntent.putExtra("fullName", fullName);
                        startActivity(userMenuIntent);



                    }else{
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "User was not found", duration);
                        toast.show();

                    }











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
                pd.cancel();
                Log.d("Oleg","error" + error);

            }
        });

        MySingleton.getInstance(Login.this).addToRequestQueue(jsObjRequest);

        Log.d("Oleg", "clicked");


    }


    public void checkUserIsValid1(){
      //  pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("Oleg","Check User Valid");
        StringRequest sr = new StringRequest(Request.Method.POST,"http://senecafleaia.azurewebsites.net/token", new Response.Listener<String>() {


    //    JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, LOGINUSERURL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(String response) {

                Log.d("Oleg","We have received: " + response.toString());

             }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             //   pd.cancel();

                if(error.networkResponse.data!=null) {

                 //   String statusCode = String.valueOf(error.networkResponse.statusCode);

                    try {
                       String body = new String(error.networkResponse.data,"UTF-8");
                        Log.d("Oleg","Body error" + body);
                     //   Log.d("Oleg","Status c" + body);
                    } catch (UnsupportedEncodingException e) {
                        Log.d("Oleg","Error response (Message) is " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                Log.d("Oleg","error" + error);

            }
        }){
            @Override
            protected Map<String,String> getParams(){

                Log.d("Oleg","email " + email);
                Log.d("Oleg","password " + password);
                Map<String,String> params = new HashMap<String, String>();
                params.put("grant_type","password");
                params.put("username",email);
                params.put("password", password);
             //   params.put("comment_post_ID",String.valueOf(postId));
            //    params.put("blogId",String.valueOf(blogId));

                return params;}
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","x-www-form-urlencoded");
                    return params;
                }
            };

        MySingleton.getInstance(Login.this).addToRequestQueue(sr);

        Log.d("Oleg", "clicked");


    }

    // THE RIGHT ONE!!!

    public void checkUserIsValid2(){
        //  pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("Oleg","Check User Valid");
        StringRequest sr = new StringRequest(Request.Method.POST,"http://senecafleaia.azurewebsites.net/token",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("oleg","response " + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String token  = jsonObject.get("access_token").toString();

                    Log.d("Oleg","Token is " + token);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
              //  MyFunctions.croutonAlert(LoginActivity.this,
                  //      MyFunctions.parseVolleyError(error));
              //  loading.setVisibility(View.GONE);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type","password");
                params.put("username",email);
                params.put("password", password);
                return params;
            }

        };


        MySingleton.getInstance(Login.this).addToRequestQueue(sr);

        Log.d("Oleg", "clicked");


    }















    public boolean isInputValid(){
        boolean inputIsValid  = true;
        errors="";
        if(email.equals("") || !email.contains("@")){
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
