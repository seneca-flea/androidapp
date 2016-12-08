package com.example.yugenshtil.finalproject.ForTest;

import android.app.Activity;
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
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;

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

        final EditText etUsername = (EditText) findViewById(R.id.etRegistration_FirstName);
        final EditText etPassword = (EditText) findViewById(R.id.etLogin_Password);
        final Button bLogin = (Button) findViewById(R.id.btLogin_Login);
        final Button bGet = (Button) findViewById(R.id.bGet);
        final Button bPost = (Button) findViewById(R.id.bPost);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

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


                    }
                });

                MySingleton.getInstance(RESTExample.this).addToRequestQueue(jsObjRequest);

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

                            }
                        });

                MySingleton.getInstance(RESTExample.this).addToRequestQueue(jsObjRequest);

            }
        });

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
