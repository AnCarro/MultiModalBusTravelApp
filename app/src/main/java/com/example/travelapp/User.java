package com.example.travelapp;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class User {

    private LatLng CurLoc = null;
    private LatLng PrevLoc = null;
    //private Marker UserMarker;
    private float CurrentVelocity = 0;
    private float PreviousVelocity = 0;
    private Marker Marker;
    private static GoogleMap Map;
    public String Title = "You";
    private static VelocityConverter Converter = new VelocityConverter();

    private VelocityConverter.Units Units;
    private String StringUnits;

    public User (VelocityConverter.Units units, LatLng currentLoc, float currentV, GoogleMap map){
        Units = units;
        CurrentVelocity = Converter.ConvertMPSTo(Units,currentV);
        PreviousVelocity = 0;
        CurLoc = currentLoc;
        PrevLoc = CurLoc;
        Map = map;
        Marker = Map.addMarker(new MarkerOptions()
                .position(CurLoc)
                .title(this.Title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );

        StringUnits = Converter.unitsToString(Units);

    }

    public User (VelocityConverter.Units units, LatLng currentLoc, String currentV, GoogleMap map){
        try {
            Units = units;
            CurrentVelocity = Converter.ConvertMPSTo(Units,currentV);
            PreviousVelocity = 0;
            CurLoc = currentLoc;
            PrevLoc = CurLoc;
            Map = map;
            Marker = Map.addMarker(new MarkerOptions()
                    .position(currentLoc)
                    .title(this.Title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );

            StringUnits = Converter.unitsToString(Units);

        }
        catch (Exception e) {
            System.out.println("User creation failed!");
            e.printStackTrace();
        }
    }

    public User (LatLng currentLoc, float currentV, GoogleMap map){
        Units = VelocityConverter.Units.MPH;
        CurrentVelocity = Converter.ConvertMPSTo(Units,currentV);
        PreviousVelocity = 0;
        CurLoc = currentLoc;
        PrevLoc = CurLoc;
        Map = map;
        Marker = Map.addMarker(new MarkerOptions()
                .position(CurLoc)
                .title(this.Title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );

        StringUnits = Converter.unitsToString(Units);

    }

    public User (LatLng currentLoc, String currentS, GoogleMap map){
        try {
            Units = VelocityConverter.Units.MPH;
            CurrentVelocity = Float.parseFloat(currentS);
            PreviousVelocity = 0;
            CurLoc = currentLoc;
            PrevLoc = CurLoc;
            Map = map;
            Marker = Map.addMarker(new MarkerOptions()
                    .position(currentLoc)
                    .title(this.Title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );

            StringUnits = Converter.unitsToString(Units);

        }
        catch (Exception e) {
            System.out.println("User creation failed!");
            e.printStackTrace();
        }
    }

    public void move(LatLng newLoc) {
        PrevLoc = CurLoc;
        CurLoc = newLoc;
        if(Marker != null) {
            Marker.setPosition(CurLoc);
        }
    }

    public void refreshMarker(GoogleMap map){
        Map = map;
        if(Marker != null) {
            Marker = Map.addMarker(new MarkerOptions()
                    .position(CurLoc)
                    .title(this.Title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }


    public void setVelocity(float vel) {
        PreviousVelocity = CurrentVelocity;
        CurrentVelocity = Converter.ConvertMPSTo(Units, vel);
    }

    public void setUserUnits(String sUnits) {
        Units = Converter.StringToUnits(sUnits);
        StringUnits = sUnits;
        //System.out.println("String units: " + StringUnits);
    }

    public void setUserUnits (VelocityConverter.Units units) {
        Units = units;
        StringUnits = Converter.unitsToString(units);
        //System.out.println("String units: " + StringUnits);
    }


    public float getVelocity(){
        return CurrentVelocity;
    }

    public LatLng getCurLoc(){
        return CurLoc;
    }

    public String getStringUnits() {
        return StringUnits;
    }

    public VelocityConverter.Units getUnits() { return Units; }


}
