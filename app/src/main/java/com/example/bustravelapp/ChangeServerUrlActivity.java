package com.example.bustravelapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.nio.MappedByteBuffer;
import java.util.Map;
import java.util.Set;

public class ChangeServerUrlActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_server_url);

        //add toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText et = (EditText) findViewById(R.id.editServerURL);
        et.setText(StaticBusInfoActivity.ServerURL);

        final Button serverChangeButton = findViewById(R.id.SubtmitServerURLChangeBtn);
        serverChangeButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ServerURL = et.getText().toString();
                StaticBusInfoActivity.ServerURL = ServerURL;

            }
        });
        String url = et.getText().toString();

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
            case R.id.static_bus_info:
                changeToStaticPage();
                return true;
            case R.id.dynamic_bus_info:
                changeToDynamicPage();
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

   private void changeToDynamicPage() {
        Intent intent = new Intent(this, DynamicBusInfoActivity.class);
        startActivity(intent);
    }

    private void changeToStaticPage() {
        Intent intent = new Intent(this, StaticBusInfoActivity.class);
        startActivity(intent);
    }

}
