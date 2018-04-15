package com.pkb149.SVT.LorryOwnerFlow;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pkb149.SVT.BookingHistory;
import com.pkb149.SVT.CardViewData;
import com.pkb149.SVT.LoginActivity;
import com.pkb149.SVT.R;
import com.pkb149.SVT.RecyclerViewAdapter;
import com.pkb149.SVT.utility.FleetDetailsAdapter;
import com.pkb149.SVT.utility.FleetDetailsCardViewData;
import com.pkb149.SVT.utility.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.entity.StringEntity;

public class FleetDetails extends AppCompatActivity implements FleetDetailsAdapter.ListItemClickListener{
    PrefManager prefManager;
    @Bind(R.id.add_a_truck)    FloatingActionButton _addTruck;
    @Bind(R.id.fleet_details_rv) RecyclerView _fleetDetails;
    @Bind(R.id.loader_fleet_details) ProgressBar _loaderFleetDetails;

    List<FleetDetailsCardViewData> data;
    FleetDetailsAdapter adapter;
    String restApiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_details);
        prefManager = new PrefManager(this);
        ButterKnife.bind(this);


        data= new ArrayList<>();
        final FleetDetailsAdapter.ListItemClickListener listItemClickListener=this;
        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity entity=null;
        final JSONObject params = new JSONObject();
        restApiUrl=getResources().getString(R.string.server_url)+"SVT/api/OwnerAPI/getTrucks";
        try{
            params.put("source", "abc");
        }
        catch (Exception e){

        }
        try{
            entity = new StringEntity(params.toString());
        }
        catch (Exception e){

        }
        client.post(getApplicationContext(), restApiUrl, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                Log.e("Response Body",response.toString());
                Log.e("length",String.valueOf(response.length()));
                try {
                    for (int i = 0; i < response.length(); i++) {
                        FleetDetailsCardViewData cardViewData= new FleetDetailsCardViewData(Parcel.obtain());
                        cardViewData.setNumberPlate(response.getJSONObject(i).getString("truckNo"));
                        cardViewData.setDriverName(response.getJSONObject(i).getString("driverName"));
                        cardViewData.setDriverMobileNumber(response.getJSONObject(i).getString("driverNumber"));
                        data.add(cardViewData);
                    }
                    adapter = new FleetDetailsAdapter(data, getApplicationContext(),listItemClickListener);
                    _fleetDetails.setAdapter(adapter);
                    LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
                    _fleetDetails.setLayoutManager(linearLayoutManager);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                _loaderFleetDetails.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject jsonObject) {
                super.onFailure(statusCode, headers, throwable, jsonObject);
                Toast.makeText(getApplicationContext(),"couldn't load data, Please try again",Toast.LENGTH_LONG).show();
                _loaderFleetDetails.setVisibility(View.INVISIBLE);
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                Log.e("TAG","OnFailure!",throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });




        /*data= new ArrayList<>();
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        adapter = new FleetDetailsAdapter(data, getApplicationContext(),this);
        _fleetDetails.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        _fleetDetails.setLayoutManager(linearLayoutManager);*/

        _addTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTruck.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_history);
        item.setVisible(false);
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
    public void onListItemClick(int clickedItemIndex, int callingFnCode) {
        if(callingFnCode==1) {
            Intent intent = new Intent(getApplicationContext(), SingleLorryDetail.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else if(callingFnCode==2) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+91"+data.get(clickedItemIndex).getDriverMobileNumber()));
            startActivity(intent);
        }
    }
}

