package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText etpassword = (EditText) findViewById(R.id.password);
        final Button btlogin = (Button) findViewById(R.id.email_sign_in_button);
        final int KEY_LENGTH = 256;


        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = email.getText().toString();
                final String password = etpassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonresponse = new JSONObject(response);
                            //System.out.println("email "+username+"--------response. "+response+"--username "+jsonresponse.getString("username"));


                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {
                                System.out.println("-------successful------------ ");
                                String name1 = jsonresponse.getString("name");
                                int users_id = jsonresponse.getInt("users_id");

                                PrivateKey privateKey = new PrivateKey();

                                privateKey.setPrivateKey(jsonresponse.getString("privateKey"));
                                privateKey.setPublicKey1(jsonresponse.getString("publicKey"));
                                privateKey.setSalt(jsonresponse.getString("salt"));
                                privateKey.setIv(jsonresponse.getString("iv"));
                                AES aes = new AES();
                                aes.decrypt(KEY_LENGTH,password.toCharArray(),privateKey);
                                Intent intent = new Intent(LoginActivity.this,ContactActivity.class);
                                //Intent intent = new Intent(LoginActivity.this,UserAreaActivity.class);
                                intent.putExtra("name",name1);
                                intent.putExtra("users_id",users_id);
                                intent.putExtra("privateKey", (Serializable) privateKey);

                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (AES.InvalidPasswordException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AES.StrongEncryptionNotAvailableException e) {
                            e.printStackTrace();
                        } catch (AES.InvalidAESStreamException e) {
                            e.printStackTrace();
                        }

                    }
                };
                LoginRequest loginrequest = new LoginRequest(username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginrequest);
            }

        });

    }

}
