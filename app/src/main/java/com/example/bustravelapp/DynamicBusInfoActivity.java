package com.example.bustravelapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.travelapp.ServerRequestsQueue;
import com.google.android.gms.location.LocationServices;

public class DynamicBusInfoActivity extends AppCompatActivity {

    public static int Occupancy = -1;
    public static int BikeRackOccupancy = -1;
    private Context Context;
    private RequestQueue Queue;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context = this.getApplicationContext();
        Queue = ServerRequestsQueue.getInstance(Context.getApplicationContext()).getRequestQueue();


        setContentView(R.layout.dynamic_bus_info);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /****************************Occupancy*****************************/
        {
            EditText etId = (EditText) findViewById(R.id.OccupancyInput);
            if (Occupancy != -1) {
                etId.setText(String.valueOf(Occupancy));
            }

        }
        /****************************BikeRackOccupancy*****************************/
        {
            EditText etId = (EditText) findViewById(R.id.BikeOccupancyInput);
            if (BikeRackOccupancy != -1) {
                etId.setText(String.valueOf(BikeRackOccupancy));
            }

        }
        /**********************************OnSaveButton**************************************/
        {
            final Button ChangeStaticDataButton = findViewById(R.id.DyanmicChange);

            ChangeStaticDataButton.setOnClickListener(new View.OnClickListener() {
                EditText etOccupancy = (EditText) findViewById(R.id.OccupancyInput);
                EditText etBikeRackOccupancy = (EditText) findViewById(R.id.BikeOccupancyInput);

                @Override
                public void onClick(View v) {

                    String sOcc = etOccupancy.getText().toString();
                    String sBikeOcc = etBikeRackOccupancy.getText().toString();

                    //test ID

                    try {
                        if (StaticBusInfoActivity.BusId != -1) {
                            int intOcc = Integer.parseInt(sOcc);
                            if (intOcc > 0) {
                                Occupancy = intOcc;
                            } else {
                                Occupancy = -1;
                                //showUserMessage("Occupancy number too low");

                            }
                            int intBikeOcc = Integer.parseInt(sBikeOcc);
                            if (intBikeOcc > 0) {
                                BikeRackOccupancy = intBikeOcc;
                            } else {
                                BikeRackOccupancy = -1;
                                // showUserMessage("BikeOccupancy number too low");
                            }

                            //send static data to server
                            sendDynamicData();
                        }
                        else {
                            System.out.println("We need a new ID!" );
                            //showUserMessage("Improper ID!");
                        }

                    } catch(NumberFormatException e){
                        e.printStackTrace();
                        //showUserMessage("Improper number!");
                        BikeRackOccupancy = -1;
                        Occupancy = -1;
                    }
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.travelapp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
  /*          case R.id.travel_info:
                changeTravelMode();
                return true;*/
            case R.id.server_url:
                changeToServerPage();
                return true;
            case R.id.static_bus_info:
                changeToStaticPage();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //makes back button work
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void changeToStaticPage() {
        Intent intent = new Intent(this, StaticBusInfoActivity.class);
        startActivity(intent);
    }

    private void changeToServerPage() {
        Intent intent = new Intent(this, ChangeServerUrlActivity.class);
        startActivity(intent);
    }

    private void sendDynamicData() {
        String sPK = String.valueOf(StaticBusInfoActivity.BusId);
        String sOcc = String.valueOf(Occupancy);
        String sBikeRackOcc = String.valueOf(BikeRackOccupancy);
        int sendMethod = Request.Method.GET;
        String context = "WebApplication1/ReceiveDynamicBusData?"+"PK="+sPK+"&occupancy="+sOcc+"&bikeRackOccupancy="+sBikeRackOcc;

        String ServerUrl = StaticBusInfoActivity.ServerURL+context;
        StringRequest StringReq = new StringRequest(sendMethod, ServerUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Server Response on sending dynamic data: " + response);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println("Ouch, Failure: " + error.getLocalizedMessage() + " " + error.getMessage());
            }
        });
        Queue.add(StringReq);
    }

/*    public void showUserMessage(String message) {
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }*/


}
