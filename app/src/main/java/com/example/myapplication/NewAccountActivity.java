package com.example.myapplication;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        final EditText etEmailId = (EditText) findViewById(R.id.EmailId);
        final EditText etMobileId = (EditText) findViewById(R.id.MobileId);
        final EditText etNameId = (EditText) findViewById(R.id.NameId);
        final EditText etPassword1Id = (EditText) findViewById(R.id.Password1Id);

        final Button bRegister = (Button) findViewById(R.id.CreateId);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etNameId.getText().toString();
                final String username = etEmailId.getText().toString();
                final String password = etPassword1Id.getText().toString();
                final String MobileId = etMobileId.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean successs = jsonResponse.getBoolean("success");


                            if(successs) {
                                Intent intent = new Intent(NewAccountActivity.this,LoginActivity.class);
                                NewAccountActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewAccountActivity.this);
                                builder.setMessage("Registration Failed")
                                       .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, username, MobileId ,password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(NewAccountActivity.this);
                queue.add(registerRequest);
            }
        });

    }
}
