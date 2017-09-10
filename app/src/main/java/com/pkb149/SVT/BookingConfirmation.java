package com.pkb149.SVT;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pkb149.SVT.utility.PrefManager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookingConfirmation extends AppCompatActivity  implements
        OnMapReadyCallback{
    PrefManager prefManager;
    ArrayList steps;
    String capacity;
    String time;
    String distance;
    String duration;
    int fromLocation;
    int toLocation;
    Date date;
    String[] amPm=new String[]{"AM","PM"};
    String[] from = { "Hyderabad","Bangalore", "Vijayawada", "Chennai","Ananthapur","Kavali","Guntur","Delhi"};
    @Bind(R.id.tv_from_conf) TextView _fromBC;
    @Bind(R.id.tv_to_conf) TextView _toBC;
    @Bind(R.id.capacity_tv_conf) TextView _capacityBC;
    @Bind(R.id.booking_from_tv_conf) TextView _bookingTimeBC;
    @Bind(R.id.travel_time_tv_conf) TextView _travelTimeBC;
    @Bind(R.id.call_driver_button) Button _callDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        prefManager = new PrefManager(this);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        steps=getIntent().getParcelableArrayListExtra("steps");
        duration=getIntent().getStringExtra("duration");
        fromLocation=getIntent().getExtras().getInt("from");
        toLocation=getIntent().getExtras().getInt("to");
        _fromBC.setText(from[fromLocation]);
        _toBC.setText(from[toLocation]);
        capacity=getIntent().getExtras().getString("capacity");
        date = (Date)getIntent().getSerializableExtra("time");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        time=String.format("%02d",cal.get(Calendar.HOUR))+":"+String.format("%02d",date.getMinutes())+" "+amPm[cal.get(Calendar.AM_PM)]+", "+String.format("%02d",date.getDate())+"-"+new DateFormatSymbols().getShortMonths()[date.getMonth()];
        _capacityBC.setText(capacity);
        _travelTimeBC.setText(duration);
        _bookingTimeBC.append(time);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_conf);

        if(isGooglePlayServicesAvailable(this)){
            mapFragment.getMapAsync(this);
        }
        else{
            Toast.makeText(getApplicationContext(),"Please install google play services to proceed",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            //TODO Log user Out
            prefManager.clearLoggedIn();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return true;
        }
        else if(id==android.R.id.home){
                onBackPressed();
                finish();
                return true;
        }
        else if(id==R.id.action_history){
            Intent intent = new Intent(getApplicationContext(), BookingHistory.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        PolylineOptions polylineOptions=new PolylineOptions();
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .addAll(steps));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include((LatLng) steps.get(0));
        builder.include((LatLng) steps.get(steps.size()-1));
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        Log.e("Height",Integer.toString(height));

        int padding = (int) (height * 0.20); // offset from edges of the map 20% of screen
        CameraUpdate cu;
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height/2, padding/2);
        }
        else{
            cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height/4, padding/4);
        }
        googleMap.moveCamera(cu);

    }
}
