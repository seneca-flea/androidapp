package com.example.yugenshtil.loginregister;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;

/**
 * Created by yugenshtil on 03/10/16.
 */
public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://omytryniuk.net23.net/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username, int age, String password, Response.Listener<String> listener){

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        Log.d("Oleg", "Registration");
        params = new HashMap<>();
        params.put("name", name);
        params.put("username",username);
        params.put("password", password);
        params.put("age", age+"");
    }

    @Override
    public Map<String, String> getParams() {
        Log.d("Oleg", "Getting params");
        Log.d("Oleg", "Getti name" + params.get("name"));
        Log.d("Oleg", "Getting uname" + params.get("username"));
        return params;
    }
}
