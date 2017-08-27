package com.pkb149.SVT;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pkb149.SVT.utility.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity implements
        AdapterView.OnItemSelectedListener{
    Toast toast;
    PrefManager prefManager;
    @Bind(R.id.spinner_from) Spinner _from;
    @Bind(R.id.spinner_to) Spinner _to;
    @Bind(R.id.spinner_capacity) Spinner _capacity;
    @Bind(R.id.tv_pickTime) TextView _pickTime;
    @Bind(R.id.btn_search) Button _search;
    Date parsedDate=null;
    String[] from = { "Hyderabad","Bangalore", "Vijayawada", "Chennai","Ananthapur","Kavali","Guntur","Delhi"};
    String[] monthString= new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] capacity = { "10 Ton","5 Ton", "1 Ton"};
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setTitle("Book A Truck");
        ButterKnife.bind(this);
        _from.setOnItemSelectedListener(this);
        _to.setOnItemSelectedListener(this);
        _capacity.setOnItemSelectedListener(this);

        ArrayAdapter fromAA = new ArrayAdapter(this,android.R.layout.simple_spinner_item,from);
        fromAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _from.setAdapter(fromAA);
        _to.setAdapter(fromAA);

        ArrayAdapter capacityAA = new ArrayAdapter(this,android.R.layout.simple_spinner_item,capacity);
        capacityAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _capacity.setAdapter(capacityAA);

        prefManager = new PrefManager(this);
        if (!prefManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }

        _search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // validate the information and submit
                if (!validate()) {
                    return;
                }


                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Details from Server...");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                progressDialog.show();
                AsyncHttpClient client = new AsyncHttpClient();
                String url="http://maps.googleapis.com/maps/api/directions/json?origin="+from[_from.getSelectedItemPosition()]+"&destination="+from[_to.getSelectedItemPosition()]+"&sensor=false";
                client.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        Log.e("Response Body",response.toString());
                        String distance="";
                        String duration="";
                        String distanceInMtr="";
                        ArrayList<LatLng> listOfStep= new ArrayList<LatLng>();
                        try {
                            JSONObject legs= response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                            distance=legs.getJSONObject("distance").getString("text");
                            distanceInMtr=legs.getJSONObject("distance").getString("value");
                            duration=legs.getJSONObject("duration").getString("text");
                            JSONArray steps=legs.getJSONArray("steps");
                            for (int i = 0; i < steps.length(); i++) {
                                Double latitude=steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat");
                                Double longitude=steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng");
                                Log.e("latitude: ",latitude.toString());
                                listOfStep.add(new LatLng(latitude,longitude));
                            }
                            Log.e("Response Distance",distance);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        Intent intent = new Intent(getApplicationContext(), ChooseTruck.class);
                        intent.putExtra("steps",listOfStep);
                        intent.putExtra("distance",distance);
                        intent.putExtra("duration",duration);
                        intent.putExtra("distanceInMtr",distanceInMtr);
                        intent.putExtra("from",_from.getSelectedItemPosition());
                        intent.putExtra("to",_to.getSelectedItemPosition());
                        intent.putExtra("time",parsedDate);
                        intent.putExtra("capacity",_capacity.getSelectedItem().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject jsonObject) {
                        super.onFailure(statusCode, headers, throwable, jsonObject);
                        Toast.makeText(getApplicationContext(),"couldn't load data, Please try again",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        Log.e("TAG","OnFailure!",throwable);
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });

            }
        });

        _pickTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //CustomDialog cd=new CustomDialog(MainActivity.this);
                //cd.setNumberDialog();
                Calendar calendar= Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int date=calendar.get(Calendar.DATE);
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);

                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View npView = inflater.inflate(R.layout.number_picker_dialog, null);
                //getApplicationContext().setTheme(R.style.AppTheme);
                //month picker
                final NumberPicker monthPicker=(NumberPicker)npView.findViewById(R.id.month_picker);

                monthPicker.setMinValue(0);
                monthPicker.setMaxValue(monthString.length-1);
                monthPicker.setDisplayedValues(monthString);
                monthPicker.setValue(month);



                //yr picker
                final NumberPicker yearPicker=(NumberPicker)npView.findViewById(R.id.year_picker);
                yearPicker.setMinValue(year);
                yearPicker.setValue(year);
                yearPicker.setMaxValue(year+2);
                //date picker
                Calendar mycal = new GregorianCalendar(yearPicker.getValue(), monthPicker.getValue(), 1);
                    // Get the number of days in that month
                int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
                final NumberPicker datePicker=(NumberPicker)npView.findViewById(R.id.date_picker);
                datePicker.setMinValue(1);
                datePicker.setMaxValue(daysInMonth);
                datePicker.setValue(date);
                datePicker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int value) {
                        return String.format("%02d",value);
                    }
                });

                monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        Calendar mycal = new GregorianCalendar(yearPicker.getValue(), monthPicker.getValue(), 1);
                        // Get the number of days in that month
                        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
                        datePicker.setMaxValue(daysInMonth);
                    }
                });

                yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        Calendar mycal = new GregorianCalendar(yearPicker.getValue(), monthPicker.getValue(), 1);
                        // Get the number of days in that month
                        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
                        datePicker.setMaxValue(daysInMonth);
                    }
                });

                datePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        if((oldVal==datePicker.getMaxValue())&&(newVal==datePicker.getMinValue())){
                            monthPicker.setValue(monthPicker.getValue()+1);
                        }
                        if((oldVal==datePicker.getMinValue())&&(newVal==datePicker.getMaxValue())){
                            monthPicker.setValue(monthPicker.getValue()-1);
                        }
                    }
                });

                //hour picker
                final NumberPicker hourPicker = (NumberPicker) npView.findViewById(R.id.hour_picker);
                hourPicker.setMaxValue(23);
                hourPicker.setMinValue(0);
                hourPicker.setValue(hour);
                hourPicker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int value) {
                        return String.format("%02d",value);
                    }
                });

                //minute picker
                final NumberPicker minPicker = (NumberPicker) npView.findViewById(R.id.min_picker);
                minPicker.setMaxValue(59);
                minPicker.setMinValue(0);
                minPicker.setValue(min);

                minPicker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int value) {
                        return String.format("%02d",value);
                    }
                });

                minPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        if((oldVal==59)&&(newVal==0)){
                            hourPicker.setValue(hourPicker.getValue()+1);
                        }
                        if((oldVal==0)&&(newVal==59)){
                            hourPicker.setValue(hourPicker.getValue()-1);
                        }

                    }
                });

                //ampm picker
                //NumberPicker ampmPicker = (NumberPicker) npView.findViewById(R.id.min_picker);


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(npView);
                final AlertDialog ad=builder.create();
                ad.getWindow().setBackgroundDrawableResource(R.color.primary_dark);
                ad.show();


                Button cancel=(Button) npView.findViewById(R.id.btn_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });

                Button done=(Button) npView.findViewById(R.id.btn_done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String year=Integer.toString(yearPicker.getValue());
                        int month=monthPicker.getValue();
                        String date=String.format("%02d",datePicker.getValue());
                        String hr=String.format("%02d",hourPicker.getValue());
                        String min=String.format("%02d",minPicker.getValue());
                        //String dateInString = new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss")
                       //         .format(cal.getTime());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            parsedDate = formatter.parse(date+"-"+(month+1)+"-"+year+" "+hr+":"+min+":"+"00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //_pickTime.setText(date+"-"+monthString[month]+"-"+year+"  "+hr+":"+min);
                        _pickTime.setText(parsedDate.toString());
                        ad.cancel();

                    }
                });
            }
        });
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
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean validate() {
        boolean valid = true;

        String from = _from.getSelectedItem().toString();
        String to= _to.getSelectedItem().toString();
        String time=_pickTime.getText().toString();

        if (from.equals(to)) {
            Toast.makeText(getApplicationContext(),"From and To cannot be same",Toast.LENGTH_LONG).show();
            valid = false;
        } else {

        }

        if (!time.contains(":")) {
            _pickTime.setError("click to pick start time");
            valid = false;
        } else {
            _pickTime.setError(null);
        }

        return valid;
    }
}
