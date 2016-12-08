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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.OtherUseCases.ForgotPassword;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.OtherUseCases.UserMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity {

    private String email="";
    private String password="";
    private String LOGINUSERURL="http://senecafleamarket.azurewebsites.net/api/User";

    private boolean inputIsValid = false;
    private boolean userFound = false;
    private String id="";
    private String fullName="";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public ProgressDialog pd;
    public String token = "";
    private EditText etPassword;
    private EditText etEmail;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        etPassword = (EditText) findViewById(R.id.etLogin_Password);
        etEmail = (EditText) findViewById(R.id.etLogin_Email);

        // Setup Buttons
        final Button btLogin = (Button) findViewById(R.id.btLogin_Login);
        final TextView tvForgotPassword = (TextView) findViewById(R.id.tvLogin_ForgotPassword);
        final TextView tvRegister = (TextView) findViewById(R.id.tvLogin_SignIn);

        // Set Buttons on Click
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
                }
                else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Please check provided information", duration);
                    toast.show();
                }
            }
        });
    }



    //Check whether email/password combination works
    public void checkUserIsValid(){
        final boolean isValid = true;
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://senecafleaia.azurewebsites.net/token",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    token  = jsonObject.get("access_token").toString();
                    getUserId();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, "Combination email/password does not match", duration);
                toast.show();
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
    }



    // Validation for user input
    public boolean isInputValid(){
        boolean inputIsValid  = true;


        //Email Validation
        if(!email.equals("") ){
            Pattern pattern;
            Matcher matcher;

            // http://stackoverflow.com/questions/8204680/java-regex-email
            pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(email);
            if(!matcher.matches()) {
                etEmail.setError("Please, provide correct email");
                inputIsValid = false;
            }

        }
        else {
            etEmail.setError("Email can't be blank");
            inputIsValid = false;
        }

        // Password Validation
        if(!password.equals("")){
            password = password.trim();
            if(password.length()<6){
                inputIsValid = false;
                etPassword.setError("Password length can't be less than 6");
            }

        }
        else{
            etPassword.setError("Password can't be blank");
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


    // Getting user ID, after the user is successfully login
    public void getUserId(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        StringRequest sr = new StringRequest(Request.Method.GET,"http://senecafleamarket.azurewebsites.net/api/User/CurrentUser",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("token", token);
                    editor.putString("PriceMin", "0");
                    editor.putString("PriceMax", "400");
                    editor.putString("UserId", jsonObject.getString("UserId"));
                    editor.putString("Email", jsonObject.getString("Email"));
                    editor.commit();

                    Intent userMenuIntent = new Intent(Login.this, UserMenu.class);
                    startActivity(userMenuIntent);
                    Log.d("LOG : ","Token is " + token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };


        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        MySingleton.getInstance(Login.this).addToRequestQueue(sr);
    }
}


