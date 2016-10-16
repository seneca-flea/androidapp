package com.example.yugenshtil.loginregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                       @Override
                    public void onResponse(String response) {
                          try {
                              JSONObject jsonResponse = new JSONObject(response);
                              boolean success = jsonResponse.getBoolean("success");
                              if(success){

                                  String name = jsonResponse.getString("name");
                                  int age = jsonResponse.getInt("age");

                                  Intent intent = new Intent(LoginActivity.this,UserAreaActivity.class);
                                  intent.putExtra("name", name);
                                  intent.putExtra("username", username);
                                  Log.d("oleg", "name is" + username);
                                  intent.putExtra("age", age);

                                  LoginActivity.this.startActivity(intent);
                              }
                              else{
                                  AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                  builder.setMessage("Login Failed").setNegativeButton("Retry",null).create().show();

                              }

                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                         }
                     };
                        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        queue.add(loginRequest);

                }



                });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
