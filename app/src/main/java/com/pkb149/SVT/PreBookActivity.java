package com.pkb149.SVT;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pkb149.SVT.utility.PrefManager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PreBookActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener{
    PrefManager prefManager;
    ArrayList steps;
    String capacity;
    String time;
    String travelTime;
    String[] amPm=new String[]{"AM","PM"};
    String[] from = { "Hyderabad","Bangalore", "Vijayawada", "Chennai","Ananthapur","Kavali","Guntur","Delhi"};
    @Bind(R.id.distance) TextView _distance;
    @Bind(R.id.tv_from_prebook_page) TextView _from;
    @Bind(R.id.tv_to_prebook_page) TextView _to;
    @Bind(R.id.capacity_tv_prebook) TextView _capacity;
    @Bind(R.id.pickedTime_tv_prebook) TextView _pickedTime;
    @Bind(R.id.availableFrom_tv_prebook) TextView _availableFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_book);
        prefManager = new PrefManager(this);
        ButterKnife.bind(this);
        Log.e("Inside on Create"," of preBook Activity");
        steps=getIntent().getParcelableArrayListExtra("steps");
        _distance.setText(getIntent().getStringExtra("distance")+", "+getIntent().getStringExtra("duration"));
        _from.setText(from[getIntent().getExtras().getInt("from")]);
        _to.setText(from[getIntent().getExtras().getInt("to")]);
        capacity=getIntent().getExtras().getString("capacity");
        Date date = (Date)getIntent().getSerializableExtra("time");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        time=String.format("%02d",cal.get(Calendar.HOUR))+":"+String.format("%02d",date.getMinutes())+" "+amPm[cal.get(Calendar.AM_PM)]+", "+String.format("%02d",date.getDate())+"-"+new DateFormatSymbols().getShortMonths()[date.getMonth()];
        _capacity.setText(capacity);
        travelTime=getIntent().getExtras().getString("duration");
        _availableFrom.setText(time);
        _pickedTime.append(time);
//        int distanceMtr=Integer.parseInt(getIntent().getStringExtra("distanceInMtr"));
  //      Double cost=(distanceMtr/83.33);
    //    _cost.append(Integer.toString(cost.intValue()));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);

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
}
