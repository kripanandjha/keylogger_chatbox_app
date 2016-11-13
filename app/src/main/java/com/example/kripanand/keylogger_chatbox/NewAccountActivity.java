package com.example.kripanand.keylogger_chatbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.dd.processbutton.FlatButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class NewAccountActivity extends AppCompatActivity {
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText userName;
    private EditText password1;
    private EditText password2;
    private FlatButton newAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        emailAddress = (EditText) findViewById(R.id.EmailId);
        phoneNumber = (EditText) findViewById(R.id.MobileId);
        userName = (EditText) findViewById(R.id.NameId);
        password1 = (EditText) findViewById(R.id.Password1Id);
        password2 = (EditText) findViewById(R.id.Password2Id);
        newAccountBtn = (FlatButton) findViewById(R.id.CreateIdBtn);

        newAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAccount();
            }
        });

    }

    private void newAccount(){
        final String uEmail = emailAddress.getText().toString();
        final String uPhone = phoneNumber.getText().toString();
        final String uName = userName.getText().toString();
        final String uPassword1 = password1.getText().toString();
        final String uPassword2 = password2.getText().toString();


        if (uEmail.equals("") || uPhone.equals("") || uName.equals("") || uPassword1.equals("") || uPassword2.equals("")){
            AlertDialog.Builder dialog = new AlertDialog.Builder(NewAccountActivity.this);
            dialog.setTitle("Empty Fields");
            dialog.setMessage("Please enter all fields");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else if(!uPassword1.equals(uPassword2)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(NewAccountActivity.this);
            dialog.setTitle("Password missmatch");
            dialog.setMessage("The two passwords do not match!");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else{

            ParseUser user = new ParseUser();

            //set core properties
            user.setUsername(uPhone);
            user.setPassword(uPassword1);
            user.setEmail(uEmail);

            //set custom properties
            user.put("Name",uName);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null){
                        emailAddress.setEnabled(false);
                        phoneNumber.setEnabled(false);
                        userName.setEnabled(false);
                        password1.setEnabled(false);
                        password2.setEnabled(false);
                        newAccountBtn.setEnabled(false);
                        //loguserin(uPhone,uPassword1);
                    }else{

                    }
                }
            });
        }

    }

    private void loguserin(String uPhone, String uPassword1) {

        if (!uPhone.equals("") || !uPassword1.equals("")){
            ParseUser.logInInBackground(uPhone, uPassword1, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null){

                        startActivity(new Intent(NewAccountActivity.this, ChatActivity.class));

                    }else{

                    }
                }
            });
        }else{

        }
    }
}
