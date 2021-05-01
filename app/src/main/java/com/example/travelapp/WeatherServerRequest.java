package com.example.travelapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bustravelapp.StaticBusInfoActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherServerRequest extends AsyncTask<Void, Void, String> {
    private Context Context;
    private TextView TV;
    private RequestQueue Queue;
    private String Server;

    public WeatherServerRequest(Context context, TextView TV) {
        this.Context = context;
        this.TV = TV;
        Queue = ServerRequestsQueue.getInstance(context.getApplicationContext()).getRequestQueue();
        Server = StaticBusInfoActivity.ServerURL;
        //System.out.println("Server: " + Server);
    }


    @Override
    protected String doInBackground(Void... voids) {
        GetWeatherData();
        return null;
    }



    @Override
    protected void onPostExecute(String result) {
    }


    private void GetWeatherData() {
        int SendMethod = Request.Method.GET;
        String apiURL = "https://api.openweathermap.org/data/2.5/weather";
        String id = "2ebbc94a5ac2c80703fe967f70b8161e";
        String citycode = "4815352";  //Morgantown
        String weatherURL = apiURL+"?id="+citycode+"&units=imperial"+"&appid="+id;
        JsonObjectRequest JOR = new JsonObjectRequest(SendMethod, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                //textE.setText(resp.toString());
                //System.out.println("Response is: " + resp.toString());
                //for each object in JSON Array
                try {
                    // System.out.println(resp);

                    //Get main temperature weather data
                    JSONObject main = resp.getJSONObject("main");
                    //System.out.println("Main" + main.toString());
                    String temp = main.getString("temp");
                    String feels_like = main.getString("feels_like");
                    String humidity = main.getString("humidity");
                    String pressure = main.getString("pressure");
                    //get Main weather descriptors
                    JSONArray weather = resp.getJSONArray("weather");
                    // System.out.println("Weather: " + weather.toString());
                    JSONObject weatherObj = weather.getJSONObject(0);
                    String main_weather = weatherObj.getString("main");
                    String description = weatherObj.getString("description");
                    description = description.substring(0, 1).toUpperCase() + description.substring(1);

                    TV.setText("Temp (deg F): " + temp + " | " + "Feels-Like (deg F): " + feels_like + "\n" +
                            description + "\n" +
                            "Humidity (%): " + humidity + " | " + "Pressure: " + pressure);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println("Ouch, Failure: " + error.getLocalizedMessage() + " " + error.getMessage());
            }
        });
        Queue.add(JOR);
    }

}

