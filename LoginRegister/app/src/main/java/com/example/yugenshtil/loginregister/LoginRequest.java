package com.example.yugenshtil.loginregister;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yugenshtil on 11/10/16.
 */
public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://omytryniuk.net23.net/Login.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener){

        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        Log.d("Oleg", "Login");
        params = new HashMap<>();
        params.put("username",username);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        Log.d("Oleg", "Getting params");
        Log.d("Oleg", "Getti name" + params.get("name"));
        Log.d("Oleg", "Getting uname" + params.get("username"));
        return params;
    }
}
