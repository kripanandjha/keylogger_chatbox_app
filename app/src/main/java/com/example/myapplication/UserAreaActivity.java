package com.example.myapplication;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView etmsg = (TextView) findViewById(R.id.tvWelcomMsg);
        final ImageButton btsend = (ImageButton) findViewById(R.id.buttonSend);
        Intent intent = getIntent();
        //String name = intent.getStringExtra("name");
        String name = intent.getStringExtra("name");
        //String MobileId = intent.getStringExtra("MobileId");
/*
        String message = name + "Welcome to your area";

        etusername.setText(message);
*/
        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etmsg.getText().toString().equals("")) {

                    final String name = etmsg.getText().toString();
                    Message msg = new Message();
                }

            }
        });
    }
}
