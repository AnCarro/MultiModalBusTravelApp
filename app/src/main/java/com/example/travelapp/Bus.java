package com.example.travelapp;

import com.example.bustravelapp.StaticBusInfoActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Bus {
    public String id;
    public LatLng location;
    public LatLng prevLocation = null;
    public Marker marker;
    public String title;
    private GoogleMap Map;
    private float PreviousVelocity;
    private float CurrentVelocity;
    private static VelocityConverter Converter = new VelocityConverter();
    private VelocityConverter.Units Units;
    private String StringUnits;

    public Bus (String busId, double busLat, double busLng, GoogleMap map) {
        id = busId;
        location = new LatLng(busLat, busLng);
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location));

        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus(String busId, LatLng busLoc, GoogleMap map) {
        id = busId;
        location = busLoc;
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus(int busId, LatLng busLoc, GoogleMap map) {
        id = Integer.toString(busId);
        location = busLoc;
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus(int busId, double busLat, double busLng, GoogleMap map) {
        id = Integer.toString(busId);
        location = new LatLng(busLat, busLng);
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus (String busId, double busLat, double busLng, String title, GoogleMap map) {
        id = busId;
        location = new LatLng(busLat, busLng);
        this.title = title;
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location).title(this.title));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus(String busId, LatLng busLoc, String title, GoogleMap map) {
        id = busId;
        location = busLoc;
        this.title = title;
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location).title(this.title));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus(int busId, LatLng busLoc, String title, GoogleMap map) {
        id = Integer.toString(busId);
        location = busLoc;
        this.title = title;
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location).title(this.title));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public Bus(int busId, double busLat, double busLng, String title, GoogleMap map) {
        id = Integer.toString(busId);
        location = new LatLng(busLat, busLng);
        this.title = title;
        Map = map;
        marker = Map.addMarker(new MarkerOptions().position(location).title(this.title));
        setUserUnits(StaticBusInfoActivity.User.getUnits());

    }

    public void Move(LatLng newLoc) {
        prevLocation = location;
        location = newLoc;
        marker.setPosition(location);
    }

    public void refreshMarker(GoogleMap map){
        Map = map;
        if(marker != null) {
            marker = Map.addMarker(new MarkerOptions().position(location).title(this.title));
        }
    }

    public void setVelocity(float vel) {
        PreviousVelocity = CurrentVelocity;
        CurrentVelocity = Converter.ConvertMPSTo(Units, vel);
        marker.setSnippet("Current Speed: " + vel + " " + StringUnits);
    }
 
    public void setUserUnits(String sUnits) {
        Units = Converter.StringToUnits(sUnits);
        StringUnits = sUnits;
        //System.out.println("String units: " + StringUnits);
    }

    private void setUserUnits (VelocityConverter.Units units) {
        Units = units;
        StringUnits = Converter.unitsToString(units);
        //System.out.println("String units: " + StringUnits);
    }


    public float getVelocity(){
        return CurrentVelocity;
    }

    public LatLng getCurLoc(){
        return location;
    }

    public String getStringUnits() {
        return StringUnits;
    }

    public VelocityConverter.Units getUnits() { return Units; }

}
