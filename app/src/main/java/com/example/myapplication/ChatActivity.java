package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by jhakr on 14-Nov-2016.
 */

public class ChatActivity extends AppCompatActivity{
    private static final String REQUEST_URL = "http://10.0.2.2/db_query.php";
    private static final String REQUEST_ROW_URL = "http://10.0.2.2/db_showrow.php";
    private static final Integer DELAY = 10000;
    String name = null;
    String toname = null;
    RSA rsa = null;
    ArrayList<ChatMessage> mMessages = new ArrayList<ChatMessage>();
    ChatAdapter messageAdapter = null;
    ListView listView = null;
    private Handler handler = new Handler();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_logout: /* To add logout operation here */
                Toast.makeText(this,"you are logging out",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                ChatActivity.this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final EditText etmsg = (EditText) findViewById(R.id.newMessage);
        final ImageButton btsend = (ImageButton) findViewById(R.id.buttonSend);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        toname = intent.getStringExtra("toname");
        PrivateKey privateKey = (PrivateKey) intent.getSerializableExtra("privateKey");
        final int users_id = intent.getIntExtra("users_id", 0);
        final String id = Integer.toString(users_id);

        String message = name + "Welcome to your area";

        try {
            rsa = new RSA(privateKey);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //etmsg.setText(message);
        displayChatMessage(name,toname,rsa);

        handler.postDelayed(runnable,DELAY);

        final RSA finalRsa = rsa;
        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etmsg.getText().toString().equals("")) {

                    final String msg_send = etmsg.getText().toString();
                    /* send text to function which will further to server*/
                    /*along with data also send timestamp and username to and from */
                    final String senders_name = name;
                    final Date date = new Date();
                    final String encMsg;
                    //encMsg = finalRsa.encrypt(etmsg.getText().toString());
                    encMsg = AESMessage.encrypt(etmsg.getText().toString());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    /* validated server response */
                                    try {

                                        System.out.println("--------response1. " + response);
                                        JSONObject jsonresponse = new JSONObject(response);

                                        boolean success = jsonresponse.getBoolean("success");

                                        if (success) {
                                            System.out.println("-------successful------------ ");

                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                                            builder.setMessage("No Server response")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(ChatActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("msg_send", encMsg);
                           // params.put("signature",Signatures.sign(finalRsa,encMsg));
                            params.put("signature","");
                            params.put("senders_name", senders_name);
                            params.put("users_id", id);
                            params.put("sent_to",toname);
                            params.put("sentat", date.toString());
                            /* need to add additional msg info like timestamp etc. */
                            return params;
                        }
                    };


                    MySIngleton.getInstance(ChatActivity.this).addTorequest(stringRequest);

                    //displayChatMessage();
                    if (listView == null)
                        listView = (ListView) findViewById(R.id.listview_chat);
                    messageAdapter = (ChatAdapter) listView.getAdapter();
                    if (messageAdapter == null)
                        messageAdapter = new ChatAdapter(ChatActivity.this,mMessages);
                    messageAdapter.add(new ChatMessage(senders_name,senders_name,msg_send,date.toString(),true));
                    messageAdapter.notifyDataSetChanged();
                    listView.invalidate();
                    etmsg.setText("");
                }

            }
        });
    }

    private void displayChatMessage(final String name, final String toname, final RSA rsa){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_ROW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                    /* validated server response */
                        try {

                            System.out.println("--------response2. " + response);
                            //JSONObject jsonresponse = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray(response);
                            String username,message,sentat,signature;
                            //int count = jsonArray.length();
                            List<String> listContents = new ArrayList<String>();
                            mMessages.clear();

                            int i = 0;
                            while(i<jsonArray.length()) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                username = jo.getString("username");
                                message = jo.getString("message");
                                signature = jo.getString("signature");
                                sentat = jo.getString("sentat");
                                //System.out.println("2" +"username:-- "+username+"  Message :"+message+"  sentat: "+sentat);
                                listContents.add(message+" By "+username+" at "+sentat);
                                ChatMessage newMessage;
                                //if(!name.equals(username))
                                    //newMessage = new ChatMessage(username,username,rsa.decrypt1(message),sentat,name.equals(username));
                                    message = AESMessage.decrypt(message);
                               // else
                                    //newMessage = new ChatMessage(username,username,rsa.decrypt2(message),sentat,name.equals(username));

                                newMessage = new ChatMessage(username,username,message,sentat,name.equals(username));
                                newMessage.signature = signature;

                                mMessages.add(newMessage);
                                i++;
                            }

                            //ListView myListView = (ListView) findViewById(R.id.listview_chat);
                            //myListView.setAdapter(new ArrayAdapter<String>(ChatActivity.this, android.R.layout.simple_expandable_list_item_1, listContents));

                            if (messageAdapter == null)
                                messageAdapter = new ChatAdapter(ChatActivity.this,mMessages);
                            if (listView == null) {
                                listView = (ListView) findViewById(R.id.listview_chat);
                                listView.setAdapter(messageAdapter);
                            }
                            //listView.invalidate();
                            messageAdapter.notifyDataSetChanged();

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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ChatActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("senders_name", name);
                params.put("sent_to", toname);
                return params;
            }
        };
        MySIngleton.getInstance(ChatActivity.this).addTorequest(stringRequest);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           // displayChatMessage(name,toname,rsa);
            handler.postDelayed(this,DELAY);

        }
    };

}
