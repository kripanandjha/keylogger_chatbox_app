package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vinuth on 04/12/16.
 */

public class ContactActivity extends AppCompatActivity{
    private static final String REQUEST_CONTACT_URL = "http://10.0.2.2/get_recent.php";
    private static final String REQUEST_PUBLICKEY_URL = "http://10.0.2.2/get_publickey.php";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_newChat: /* To add logout operation here */
                Toast.makeText(this,"Starting new chat",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ContactActivity.this, LoginActivity.class);
                ContactActivity.this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        final String name = getIntent().getStringExtra("name");
        final PrivateKey privateKey = (PrivateKey) getIntent().getSerializableExtra("privateKey");
        final ArrayList<String> contacts = new ArrayList<String>();
        final Intent intent = new Intent(ContactActivity.this,ChatActivity.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_CONTACT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                                    /* validated server response */
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            String username,message,sentat;
                            List<String> listContents = new ArrayList<String>();
                            int i = 0;
                            while(i<jsonArray.length()) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                username = jo.getString("username");
                                listContents.add(username);
                                i++;
                            }

                            //ListView myListView = (ListView) findViewById(R.id.listview_chat);
                            //myListView.setAdapter(new ArrayAdapter<String>(ChatActivity.this, android.R.layout.simple_expandable_list_item_1, listContents));

                            final ListView listView = (ListView) findViewById(R.id.listciew_chat);

                            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(ContactActivity.this,android.R.layout.simple_expandable_list_item_1, listContents);

                            listView.setAdapter(mAdapter);

                            listView.invalidate();

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    final String toName = adapterView.getItemAtPosition(i).toString();
                                    getPublicKey(privateKey,toName);



                                }

                                private void getPublicKey(final PrivateKey privateKey,final String toName) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_PUBLICKEY_URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                    /* validated server response */
                                                    try {
                                                        JSONObject jsonresponse = new JSONObject(response);
                                                        String publicKey = jsonresponse.getString("publicKey");
                                                        privateKey.setPublicKey2(publicKey);
                                                        intent.putExtra("name",name);
                                                        intent.putExtra("toname",toName);
                                                        intent.putExtra("privateKey", (Serializable) privateKey);
                                                        ContactActivity.this.startActivity(intent);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                    Toast.makeText(ContactActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                                                    error.printStackTrace();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            System.out.println(toName);
                                            params.put("toname", toName);
                                            return params;
                                        }
                                    };
                                    MySIngleton.getInstance(ContactActivity.this).addTorequest(stringRequest);
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ContactActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("senders_name", name);
                return params;
            }
        };
        MySIngleton.getInstance(ContactActivity.this).addTorequest(stringRequest);


    }
}
