package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button LoginButton;
    private Button NewAccountButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
