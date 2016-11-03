package com.example.yugenshtil.finalproject;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends Activity {

    private String firstName="";
    private String lastName="";
    private String email="";
    private String password="";
    private String phoneNumber="";
    private String errors="";
    private String CREATEUSERURL="http://senecaflea.azurewebsites.net/api/User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText) findViewById(R.id.etLastName);
        final EditText etEmail = (EditText) findViewById(R.id.loginETemail);
        final EditText etPassword = (EditText) findViewById(R.id.loginETPassword);
        final EditText etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        final Button bRegistration = (Button) findViewById(R.id.bRegistration);
        final TextView loginLink = (TextView) findViewById(R.id.tvLogin);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","LOGIN was clicked");
                Intent loginIntent = new Intent(RegistrationPage.this,LoginPage.class);
               // RegistrationPage.this.startActivity(loginIntent);
                startActivity(loginIntent);
            }
        });

        bRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = etFirstName.getText().toString();
                Log.d("Oleg","First name " + firstName);
                lastName = etLastName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();

                if (validateInput()) {
                    Map<String, String> params = new HashMap();
                    params.put("FirstName", firstName);
                    params.put("LastName", lastName);
                    params.put("Email", email);
                    params.put("PhoneNumber", phoneNumber);

                    JSONObject parameters = new JSONObject(params);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, CREATEUSERURL, parameters, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Oleg", "Response is " + response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });

                    MySingleton.getInstance(RegistrationPage.this).addToRequestQueue(jsObjRequest);

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "User was registered. Redirection to login page", duration);
                    toast.show();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent loginIntent = new Intent(RegistrationPage.this,LoginPage.class);
                    // RegistrationPage.this.startActivity(loginIntent);
                    startActivity(loginIntent);


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
        if(phoneNumber.length()!=10) {
           // Log.d("Oleg","phoneNumber" +phoneNumber.length() + phoneNumber);
            errors += "Please, provide correct phoneNumber\n";
            inputIsValid = false;
        }
        if(password.length()<3) {
           // Log.d("Oleg","password" +password.length());
            errors += "Please, provide correct password\n";
            inputIsValid = false;
        }

        return inputIsValid;
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
