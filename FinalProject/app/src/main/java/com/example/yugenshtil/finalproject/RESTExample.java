package com.example.yugenshtil.finalproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class RESTExample extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restexample);

        final EditText etUsername = (EditText) findViewById(R.id.etFirstName);
        final EditText etPassword = (EditText) findViewById(R.id.loginETPassword);
        final Button bLogin = (Button) findViewById(R.id.loginBLogin);
        final Button bGet = (Button) findViewById(R.id.bGet);
        final Button bPost = (Button) findViewById(R.id.bPost);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);
/*
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginPage.this,RegisterActivity.class);
                LoginPage.this.startActivity(registerIntent);

            }
        });
*/


        // JSONObject receive
        /*
        bGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tx = (TextView) findViewById(R.id.jsonRes);
                String url = "https://jsonplaceholder.typicode.com/posts/1";
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Oleg","Response is " + response);
                                tx.setText(response.toString());
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
        });*/

        // JSONArray receive

        bGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tx = (TextView) findViewById(R.id.jsonRes);
                String url = "http://senecaflea.azurewebsites.net/api/User";
                JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {


                    // You can use FAKE JSON
                    /*
                    String fakeJSON = "[{\"name\":\"John Johnson\",\"street\":\"Oslo West 16\",\"phone\":\"555 1234567\"},{\"name\":\"John Johnson\",\"street\":\"Oslo West 16\",\"phone\":\"555 1234567\"}]";
                     try {
                            JSONArray jsonArray = new JSONArray(fakeJSON);
                             res+="size is " + jsonArray.length()+"\n";
                     } catch (JSONException e) {
                               e.printStackTrace();
                       }


                    */
                    @Override
                    public void onResponse(JSONArray response) {
                        String res = "";
                        res+="size is " + response.length()+"\n";
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject user = (JSONObject) response.get(i);
                                String firstName = user.getString("FirstName");
                                String lastName = user.getString("LastName");
                                String email = user.getString("Email");
                                res+=firstName +" " + lastName + " " + email+"\n";

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        tx.setText(res);

                        //   tx.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Oleg","error" + error);

                    }
                });

                MySingleton.getInstance(RESTExample.this).addToRequestQueue(jsObjRequest);

                Log.d("Oleg", "clicked");
            }
        });


        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tx = (TextView) findViewById(R.id.jsonRes);
                String url = "http://omytryniuk.net23.net/test.php";

                Map<String, String> params = new HashMap();
                params.put("first_param", "Seneca");
                params.put("second_param", "2");

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Oleg", "Response is " + response);
                                tx.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }
                        });

                MySingleton.getInstance(RESTExample.this).addToRequestQueue(jsObjRequest);

                Log.d("Oleg", "clickedPOST");
            }
        });
/*
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
                            if (success) {

                                String name = jsonResponse.getString("name");
                                int age = jsonResponse.getInt("age");

                                Intent intent = new Intent(LoginPage.this, UserAreaActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                Log.d("oleg", "name is" + username);
                                intent.putExtra("age", age);

                                LoginPage.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();

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
*/





    }


    public class ActorsAsyncTask extends AsyncTask<String, Void,Boolean>{


        @Override
        protected Boolean doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();



            return null;
        }

        @Override
        protected void onPostExecute(Boolean result){

            super.onPostExecute(result);
        }
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
