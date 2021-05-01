package com.example.travelapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ServerRequestsQueue {
    private static ServerRequestsQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;


    private ServerRequestsQueue (Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ServerRequestsQueue getInstance(Context context) {
        if(instance == null) {
            instance = new ServerRequestsQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
