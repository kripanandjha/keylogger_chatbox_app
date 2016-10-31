package com.example.kripanand.keylogger_chatbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button LoginButton;
    private Button NewAccountButton;
    public static final String YOUR_APPLICATION_ID = "";
    public static final String YOUR_CLIENT_KEY = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseObject.registerSubclass(Message.class);

        NewAccountButton = (Button) findViewById(R.id.NewAccountId);
        LoginButton = (Button) findViewById(R.id.LoginId);

        NewAccountButton.setOnClickListener(this);
        LoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.LoginId:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.NewAccountId:
                startActivity(new Intent(MainActivity.this, NewAccountActivity.class));
                break;
        }

    }
}
