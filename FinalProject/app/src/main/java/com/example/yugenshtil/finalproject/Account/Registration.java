/*
The class was designed by Oleg Mytryniuk
omytryniuk@myseneca.ca

The class is responsible to manage Account Registration in the system
 */

package com.example.yugenshtil.finalproject.Account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends Activity {

    private String firstName="";
    private String lastName="";
    private String email="";
    private String password="";
    private String passwordConfirmation="";
    private String phone="";
    private String errors="";
    private String USERAUTHENTICATIONURL="http://senecafleaia.azurewebsites.net/api/Account/Register";
    private String USERREGISTRATIONURL="http://senecafleamarket.azurewebsites.net/api/User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        final EditText etFirstName = (EditText) findViewById(R.id.etRegistration_FirstName);
        final EditText etLastName = (EditText) findViewById(R.id.etRegistration_LastName);
        final EditText etEmail = (EditText) findViewById(R.id.etLogin_Email);
        final EditText etPassword = (EditText) findViewById(R.id.etLogin_Password);
        final EditText etPasswordConfirmation = (EditText) findViewById(R.id.etRegistration_PasswordConfirmation);
        final EditText etPhone = (EditText) findViewById(R.id.etLogin_PhoneNumber);

        final Button btRegistration = (Button) findViewById(R.id.btRegistration_Registration);
        final TextView tvLogin = (TextView) findViewById(R.id.tvRegistration_Login);


        // Go to the Login page
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Registration.this,Login.class);
                startActivity(loginIntent);
            }
        });


        btRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                passwordConfirmation = etPasswordConfirmation.getText().toString();
                phone = etPhone.getText().toString();

                if (validateInput()) {
                    Map<String, String> params = new HashMap();
                    params.put("Email", email);
                    params.put("Password", password);
                    params.put("ConfirmPassword", passwordConfirmation);
                    params.put("SurName", lastName);
                    params.put("GivenName", firstName);

                    JSONObject parameters = new JSONObject(params);
                    Log.d("Oleg","JSON parameters"+ parameters);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, USERAUTHENTICATIONURL, parameters, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Oleg", "Registration Response is " + response);
                            addUserToSenecaFlea();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String body;
                            String errorToString = error.toString();

                            if(errorToString.contains("End of input at character 0 of ")){
                                addUserToSenecaFlea();
                            }
                            else {

                               // The code was taken from http://stackoverflow.com/questions/26167631/how-to-access-the-contents-of-an-error-response-in-volley
                               if(error.networkResponse.data!=null) {

                                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                                    Log.d("Oleg","Status Code is " + statusCode);

                                    try {
                                        body = new String(error.networkResponse.data,"UTF-8");
                                        if(body.contains("is already taken")){
                                            Toast toast = Toast.makeText(getApplicationContext(), "This User name is already taken. Please use another one, or login", Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                        Log.d("Oleg","Error response is " + body);
                                    } catch (UnsupportedEncodingException e) {
                                        Log.d("Oleg","Error response (Message) is " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    });

                    MySingleton.getInstance(Registration.this).addToRequestQueue(jsObjRequest);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


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

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        boolean inputIsValid = true;
       // Log.d("Oleg","firstNAme va" +firstName);
        errors="";
        if(firstName.equals("")){
          //  Log.d("Oleg","firstNAme" +firstName);
            errors+="Please, do not leave userName blank\n";
            inputIsValid = false;
        }
        if(lastName.equals("")){
          //  Log.d("Oleg","lastName" +lastName);
            errors+="Please, do not leave lastName blank\n";
            inputIsValid = false;
        }
        if(!email.contains("@")){
          //  Log.d("Oleg","email" +email);
            errors+="Please, provide correct email\n";
            inputIsValid = false;
        }

        if(password.length()>5) {
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            if(!matcher.matches()) {
                errors += "Please, provide correct password\n";
                inputIsValid = false;
            }
        }else{
            errors += "Please, provide correct password. The length should be more than 5\n";
            inputIsValid = false;
        }

        if(passwordConfirmation.length()>5) {
            if(!password.equals(passwordConfirmation)){
                errors += "Password and Confirmation Passwords should match\n";
                inputIsValid = false;
            }
            else{
                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(passwordConfirmation);
                if(!matcher.matches()){
                    errors += "Please, provide correct Confirmation password\n";
                    inputIsValid = false;
                }

            }
        }else{
            errors += "Please, provide correct Password Confirmation. The length should be more than 5\n";
            inputIsValid = false;
        }

        return inputIsValid;
    }


    public boolean addUserToSenecaFlea(){
        boolean isSuccessful = true;

        Map<String, String> addUserParams = new HashMap();
        addUserParams.put("Email", email);
        addUserParams.put("FirstName", firstName);
        addUserParams.put("LastName", lastName);
        addUserParams.put("PhoneNumber",phone);


        JSONObject parameters = new JSONObject(addUserParams);
        Log.d("Oleg","Add JSON parameters"+ parameters);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, USERREGISTRATIONURL, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Oleg", "Registration Response is " + response);
                Toast toast = Toast.makeText(getApplicationContext(), "User was successfully registered. Redirection to login page", Toast.LENGTH_LONG);
                toast.show();

                Intent loginIntent = new Intent(Registration.this,Login.class);
                startActivity(loginIntent);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                String errorToString = error.toString();
                Log.d("OLEG","ERROR" + errorToString);
                if(errorToString.contains("End of input at character 0 of ")){
                    Toast toast = Toast.makeText(getApplicationContext(), "User was successfully registered. Redirection to login page", Toast.LENGTH_LONG);
                    toast.show();

                    Intent loginIntent = new Intent(Registration.this,Login.class);
                    startActivity(loginIntent);
                }
               /* else {
                    if(com.android.volley.NetworkResponse==true){
                        Log.d("jk","jk");
                    }
                    if(error.networkResponse.data!=null) {

                        String statusCode = String.valueOf(error.networkResponse.statusCode);

                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            Log.d("Oleg","Error response (Message) is " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }*/
            }

        });

        MySingleton.getInstance(Registration.this).addToRequestQueue(jsObjRequest);

        return isSuccessful;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_page, menu);
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
