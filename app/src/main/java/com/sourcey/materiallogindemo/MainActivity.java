package com.sourcey.materiallogindemo;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
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

import com.sourcey.materiallogindemo.utility.PrefManager;

import java.util.Calendar;
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
    String[] from = { "Hyderabad","Bangalore", "Vijayawada", "Chennai","Ananthapur","Kavali","Guntur","Delhi"};
    String[] monthString= new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] capacity = { "1 Ton","5 Ton", "10 Ton"};
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Book A Truck");
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
            overridePendingTransition(R.anim.push_left_out,R.anim.push_left_in);
        }

        _search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // validate the information and submit
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

                //hour picker
                final NumberPicker hourPicker = (NumberPicker) npView.findViewById(R.id.hour_picker);
                hourPicker.setMaxValue(23);
                hourPicker.setMinValue(0);
                hourPicker.setValue(hour);
                //minute picker
                final NumberPicker minPicker = (NumberPicker) npView.findViewById(R.id.min_picker);
                minPicker.setMaxValue(60);
                minPicker.setMinValue(0);
                minPicker.setValue(min);

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
                        String date=Integer.toString(datePicker.getValue());
                        String hr=Integer.toString(hourPicker.getValue());
                        String min=Integer.toString(minPicker.getValue());
                        _pickTime.setText(date+"-"+monthString[month]+"-"+year+"  "+hr+":"+min);
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
            overridePendingTransition(R.anim.push_left_out,R.anim.push_left_in);
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
}
