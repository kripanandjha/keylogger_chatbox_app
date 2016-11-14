package com.example.myapplication;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;
import java.util.HashMap;
/**
 * Created by jhakr on 05-Nov-2016.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2/Register2.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String username,  String MobileId , String password,Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("MobileId", MobileId);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
