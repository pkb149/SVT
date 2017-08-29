package com.pkb149.SVT;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pkb149.SVT.utility.PrefManager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ChooseTruck extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener{
    PrefManager prefManager;
    String[] from = { "Hyderabad","Bangalore", "Vijayawada", "Chennai","Ananthapur","Kavali","Guntur","Delhi"};
    @Bind(R.id.tv_from_result_page) TextView _fromResult;
    @Bind(R.id.tv_to_result_page) TextView _toResult;
    @Bind(R.id.recycler_view) RecyclerView _recyclerView;
    @Bind(R.id.loader) ProgressBar _loader;
    @Bind(R.id.pickedTime_tv) TextView _pickedTime;
    @Bind(R.id.capacity_tv) TextView _capacity;
    @Bind(R.id.expectedTravelTime_tv) TextView _travelTime;


    RecyclerViewAdapter adapter;
    List<CardViewData> data;
    int fromLocation;
    int toLocation;
    String capacity;
    String time;
    String travelTime;
    ArrayList steps;
    String distance;
    String distanceInMtr;
    Date date;
    String[] amPm=new String[]{"AM","PM"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_truck);
        prefManager = new PrefManager(this);
        ButterKnife.bind(this);
        //getIntent().getExtras().getString("to");
        fromLocation=getIntent().getExtras().getInt("from");
        toLocation=getIntent().getExtras().getInt("to");
        capacity=getIntent().getExtras().getString("capacity");
        travelTime=getIntent().getExtras().getString("duration");
        steps=getIntent().getParcelableArrayListExtra("steps");
        distance=getIntent().getStringExtra("distance");
        distanceInMtr=getIntent().getStringExtra("distanceInMtr");
        date = (Date)getIntent().getSerializableExtra("time");
        _fromResult.append(from[fromLocation]);
        _toResult.append(from[toLocation]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        time=String.format("%02d",cal.get(Calendar.HOUR))+":"+String.format("%02d",date.getMinutes())+" "+amPm[cal.get(Calendar.AM_PM)]+", "+String.format("%02d",date.getDate())+"-"+new DateFormatSymbols().getShortMonths()[date.getMonth()];
        _pickedTime.setText(time);
        _capacity.setText(capacity);
        _travelTime.setText(travelTime);

        CardViewData cardViewData= new CardViewData(Parcel.obtain());
        cardViewData.setAvailableFrom(time);
        cardViewData.setNumberPlate("MH 12 DE\n1433");
        cardViewData.setOnwerId("Owner 123");
        data= new ArrayList<>();
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        adapter = new RecyclerViewAdapter(data, getApplicationContext(),this);
        _recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        _recyclerView.setLayoutManager(linearLayoutManager);

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
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(getApplicationContext(), PreBookActivity.class);
        intent.putExtra("steps",steps);
        intent.putExtra("distance",distance);
        intent.putExtra("duration",travelTime);
        intent.putExtra("distanceInMtr",distanceInMtr);
        intent.putExtra("from",fromLocation);
        intent.putExtra("to",toLocation);
        intent.putExtra("capacity",capacity);
        intent.putExtra("time",date);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

    }
}
