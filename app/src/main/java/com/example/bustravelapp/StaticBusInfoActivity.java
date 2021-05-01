package com.example.bustravelapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.travelapp.ServerRequestsQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("ALL")
public class StaticBusInfoActivity extends AppCompatActivity     {

    //public static HashMap<String, Bus> Buses = new HashMap<String, Bus>();
    //public static Weather Weather = new Weather();

    //private GoogleMap mMap;
    private Context Context;
    private RequestQueue Queue;

    /*______________________________________________*/
// location stuff
    private FusedLocationProviderClient FusedLocationClient;
    private LocationRequest LocationRequest;
    private LocationCallback LocationCallback;
    private LatLng CurLoc = null;
  //  private Marker UserLoc;
    private float CurSpeed = 0;
  //  private float PrevSpeed;


    /*__________________________________________________*/
    //Variables important to entire app
    public static com.example.travelapp.User User;

    public static String ServerURL = "http://192.168.1.133:8080/";
    public static int BusId = -1;
    public static int BusCapacity = -1;
    public static boolean BikeRack = false;
    public static int BikeRackCapacity = -1;
    public static boolean Tracking = false;

    Toast toast;


   // public static VelocityConverter Converter = new VelocityConverter();

   //public static VelocityConverter.Units MeasurementUnits = VelocityConverter.Units.MPH;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context = this.getApplicationContext();    //get context of app
        FusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // set Fused Location
        Queue = ServerRequestsQueue.getInstance(Context.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.static_bus_info);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //calls to server for buses
        //callAsynchronousTask();

    //each button/input action is in its own scope

        /****************************BusID*****************************/
        {
            EditText etId = (EditText) findViewById(R.id.IDInput);
            if (BusId != -1) {
                etId.setText(String.valueOf(BusId));
            }
        }

        /****************************Load Data button***********************/
        {
            final Button LoadDataButton = (Button) findViewById(R.id.LoadDataButton);
            LoadDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText etId = (EditText) findViewById(R.id.IDInput);
                    String id = etId.getText().toString();
                    if (id.isEmpty()) {
                        showUserMessage("No valid ID");
                    } else {
                        try {
                            int iId = Integer.parseInt(id);
                            if (iId > 0) {
                                BusId = iId;
                                getBusDataFromServer();
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            System.out.println("Cannot format number!");
                            showUserMessage("No valie ID");
                        }
                    }
                }
            });

        }


        /****************************BusCapacity*****************************/
        {
            EditText etc = (EditText) findViewById(R.id.CapacityInput);
            if (BusCapacity != -1) {
                etc.setText(String.valueOf(BusCapacity));
            }

        }

        /****************************BikeRack*****************************/

        {
            RadioButton bt = (RadioButton) findViewById(R.id.radioButtonBikeRackYes);
            EditText etb = (EditText) findViewById(R.id.BikeRackCapacityInput);
            if(BikeRack) {
                bt.setChecked(true);
                etb.setEnabled(true);
            }
            else {
                bt.setChecked(false);
                etb.setEnabled(false);
                etb.setText("");
            }

            RadioGroup rgBRack = (RadioGroup) findViewById(R.id.radioButtonBikeRack);
            rgBRack.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.radioButtonBikeRackYes) {
                        BikeRack = true;
                        etb.setEnabled(true);
                    }
                    else {
                        BikeRack = false;
                        etb.setEnabled(false);
                    }
                }
            });


        /****************************BikeRackCapacity*****************************/
            if (BikeRackCapacity != -1) {
                etb.setText(String.valueOf(BikeRackCapacity));
            }

        }


        /*************************OnSaveButton****************************/
        {
            final Button ChangeStaticDataButton = (Button) findViewById(R.id.StaticChange);

            ChangeStaticDataButton.setOnClickListener(new View.OnClickListener() {
                EditText etId = (EditText) findViewById(R.id.IDInput);
                EditText etCapacity = (EditText) findViewById(R.id.CapacityInput);
                EditText etBikeCapacity = (EditText) findViewById(R.id.BikeRackCapacityInput);

                @Override
                public void onClick(View v) {

                    String ID = etId.getText().toString();
                    String capacity = etCapacity.getText().toString();
                    String bikeRackCap = etBikeCapacity.getText().toString();

                    //test if empty before continuing
                    if (ID.isEmpty()) {
                        BusId = -1;
                        System.out.println("We need a new ID!");
                        showUserMessage("Improper ID!");
                    } else {
                        try {
                            int intID = Integer.parseInt(ID);
                            //only if we have a good ID
                            if (intID > 0) {
                                BusId = intID;

                            //Get capacity -- test if empty first
                                if (capacity.isEmpty()) {
                                    System.out.println("Capacity empty");
                                    BusCapacity = -1;
                                }
                                else {
                                    int intCap = Integer.parseInt(capacity);
                                    if (intCap > 0) {
                                        BusCapacity = intCap;
                                    } else {
                                        BusCapacity = -1;
                                        showUserMessage("Capacity number too low");
                                    }
                                }
                            //Get BikeRack capacity -- test if empty first
                                if (bikeRackCap.isEmpty()) {
                                    System.out.println("Bike Capacity empty");
                                    BikeRackCapacity = -1;
                                } else {
                                    int intbikeRackCap = Integer.parseInt(bikeRackCap);
                                    if (intbikeRackCap > 0) {
                                        BikeRackCapacity = intbikeRackCap;
                                    } else {
                                        BikeRackCapacity = -1;
                                        showUserMessage("Bike capacity number too low");
                                    }
                                }

                                //send static data to server
                                sendStaticData();

                            } else {
                                System.out.println("We need a new ID!");
                                showUserMessage("Improper ID!");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            showUserMessage("Improper number!");
                            BikeRackCapacity = -1;
                            BusCapacity = -1;
                        }
                    }
                }
            });
        }
        /*********************************Switch*******************************/
        {
            Switch ToggleTracking = findViewById(R.id.TrackingSwitch);
            if(Tracking && BusId > 0) {
                ToggleTracking.setChecked(true);
            } else {
                ToggleTracking.setChecked(false);
            }
            ToggleTracking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        if (BusId > 0) {
                            ToggleTracking.setTextOn("Tracking on");
                            startLocationUpdates();
                        } else {
                            ToggleTracking.setChecked(false);
                            System.out.println("We must have BusID before tracking");
                            showUserMessage("Improper ID");
                        }
                    } else {
                        ToggleTracking.setTextOff("Tracking off");
                    }
                }
            });
        }


    /*************************End of buttons/texts/gui stuff*****************************/

        //check and ask for permissions
        if (checkPermissions()) {
            requestPermissions();
        }
        FusedLocationClient.getLastLocation()  //get location data
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        //System.out.println("lat: " + lat + "lng: "+ lng);
                        CurLoc = new LatLng(lat, lng);
                        CurSpeed = location.getSpeed();

                    } else {
                        System.out.println("Loc = null");
                        requestNewLocationData();
                    }
                }
            });

        LocationRequest = LocationRequest.create();
        LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequest.setInterval(8*1000);
        LocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {


                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    CurLoc = new LatLng(lat, lng);
                    CurSpeed = location.getSpeed();
                    System.out.println("lat: " + lat + "lng: "+ lng);

                    sendBusLocInfoToServer(lat, lng, CurSpeed);


                }
            }
        };
    }
    /*******************************************End of On Create**************************************/

    //adds life to back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
            case R.id.server_url:
                changeToServerURL();
                return true;
            case R.id.dynamic_bus_info:
                changeToDynamicBusInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void changeToDynamicBusInfo() {
        Intent intent = new Intent(this, DynamicBusInfoActivity.class);
        startActivity(intent);
    }

    private void changeToServerURL() {
        Intent intent = new Intent(this, ChangeServerUrlActivity.class);
        startActivity(intent);
    }



    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission
                                .ACCESS_COARSE_LOCATION,
                        Manifest.permission
                                .ACCESS_FINE_LOCATION },
                44);
    }


    private boolean checkPermissions()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                                                            != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                                                            != PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        /* ActivityCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission
                        .ACCESS_BACKGROUND_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        */
    }

    private void startLocationUpdates() {
        //System.out.println("Inside: " );
        FusedLocationClient.requestLocationUpdates(LocationRequest,
                LocationCallback,
                Looper.getMainLooper());
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData()
    {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest = new LocationRequest();
        LocationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequest.setInterval(50000);
        LocationRequest.setFastestInterval(5000);
        LocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        FusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        FusedLocationClient.requestLocationUpdates(LocationRequest,mLocationCallback,Looper.myLooper());
    }

    //responsible for adding call back for periodic callbacks to get user loc
    //User object? Unclear
    private LocationCallback
            mLocationCallback
            = new LocationCallback() {

        @Override
        public void onLocationResult(
                LocationResult locationResult)
        {
            Location mLastLocation
                    = locationResult
                    .getLastLocation();
            //System.out.println("Latitude: " + mLastLocation.getLatitude()+ "");
            System.out.println("Longitude: " + mLastLocation.getLongitude()+ "");
            CurLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
           // mMap.addMarker(new MarkerOptions().position(CurLoc).title("you"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(CurLoc));
        }
    };

    private void sendBusLocInfoToServer (double lat, double lng, float speed){
        System.out.println("Sending Data");
        String sLat = String.valueOf(lat);
        String sLng = String.valueOf(lng);
        String sSpeed = String.valueOf(speed);
        int sendMethod = Request.Method.GET;
        String context = "WebApplication1/ReceiveData?PK=" + BusId + "&lat=" + sLat + "&lng=" + sLng + "&speed=" + sSpeed;
        String ServerUrl = ServerURL+context;
        StringRequest StringReq = new StringRequest(sendMethod, ServerUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Server Response on Sending Bus Info: " + response);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println("Ouch, Failure: " + error.getLocalizedMessage() + " " + error.getMessage());
            }
        });
        Queue.add(StringReq);
    }

    private void sendStaticData() {
        String sPK = String.valueOf(BusId);
        String sCapacity = String.valueOf(BusCapacity);
        String sbikeRack = String.valueOf(BikeRackCapacity);
        int sendMethod = Request.Method.GET;
        String context = "WebApplication1/ReceiveStaticBusData?"+"PK="+sPK+"&capacity="+sCapacity+"&bikeRackCapacity="+sbikeRack;

        String ServerUrl = ServerURL+context;
        StringRequest StringReq = new StringRequest(sendMethod, ServerUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Server Response on sending static: " + response);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println("Ouch, Failure: " + error.getLocalizedMessage() + " " + error.getMessage());
            }
        });
        Queue.add(StringReq);
    }

    private void getBusDataFromServer() {
        String sPK = String.valueOf(BusId);
        int sendMethod = Request.Method.GET;
        String context = "WebApplication1/GetBusData?"+"PK="+sPK;

        String ServerUrl = ServerURL+context;
        JsonObjectRequest JOR = new JsonObjectRequest(sendMethod, ServerUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Server Response on getting bus data: " + response);
                try {
                    BusCapacity = response.getInt("Capacity");
                    BikeRackCapacity = response.getInt("BikeRackCapacity");
                    DynamicBusInfoActivity.Occupancy = response.getInt("Occupancy");
                    DynamicBusInfoActivity.BikeRackOccupancy = response.getInt("BikeRackOccupancy");

                            EditText etc = (EditText) findViewById(R.id.CapacityInput);
                                etc.setText(String.valueOf(BusCapacity));
                            EditText etb = (EditText) findViewById(R.id.BikeOccupancyInput);
                                etc.setText(String.valueOf(BikeRackCapacity));
                            if(DynamicBusInfoActivity.Occupancy != -1) {
                                BikeRack = true;
                                RadioButton bt = (RadioButton) findViewById(R.id.radioButtonBikeRackYes);
                                bt.setEnabled(true);

                                EditText eto = (EditText) findViewById(R.id.OccupancyInput);
                                    etc.setText(String.valueOf(DynamicBusInfoActivity.Occupancy));
                                EditText etob = (EditText) findViewById(R.id.BikeOccupancyInput);
                                    etc.setText(String.valueOf(DynamicBusInfoActivity.BikeRackOccupancy));
                            }

                } catch ( JSONException e) {
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



    public void showUserMessage(String message) {
        toast = Toast.makeText(Context, message, Toast.LENGTH_SHORT);
        toast.show();
    }


}
