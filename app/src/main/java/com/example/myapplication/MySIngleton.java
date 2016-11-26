package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jhakr on 24-Nov-2016.
 */

public class MySIngleton {
    private static MySIngleton minstance;
    private RequestQueue requestQueue;
    private static Context ctx;

    public MySIngleton(Context context) {

        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;

    }


    public static synchronized MySIngleton getInstance (Context context) {

        if (minstance == null) {
            minstance = new MySIngleton(context);
        }
        return minstance;
    }

    public <T>void addTorequest(Request<T> request) {

        requestQueue.add(request);

    }


}
