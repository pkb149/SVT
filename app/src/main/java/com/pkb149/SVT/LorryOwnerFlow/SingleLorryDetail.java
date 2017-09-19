package com.pkb149.SVT.LorryOwnerFlow;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.pkb149.SVT.LoginActivity;
import com.pkb149.SVT.R;
import com.pkb149.SVT.utility.BookingDetailsAdapter;
import com.pkb149.SVT.utility.BookingDetailsCardViewData;
import com.pkb149.SVT.utility.FleetDetailsAdapter;
import com.pkb149.SVT.utility.FleetDetailsCardViewData;
import com.pkb149.SVT.utility.PrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleLorryDetail extends AppCompatActivity implements BookingDetailsAdapter.ListItemClickListener {
    PrefManager prefManager;
    List<BookingDetailsCardViewData> data;
    BookingDetailsAdapter adapter;
    @Bind(R.id.single_lorry_booking_details_rv)    RecyclerView _singleLorryBookingDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_lorry_detail);
        prefManager = new PrefManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);


        BookingDetailsCardViewData cardViewData= new BookingDetailsCardViewData(Parcel.obtain());
        cardViewData.setBookingId("booking_id_123");
        cardViewData.setMerchant("Prashant");
        cardViewData.setMerchantMobile("8179021658");
        cardViewData.setFromLocation("Bangalore");
        cardViewData.setToLocation("Hyderabad");
        cardViewData.setFromTime("08:00 PM, 17-Sep");
        cardViewData.setToTime("10:00 AM, 18-Sep");
        data= new ArrayList<>();
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        data.add(cardViewData);
        adapter = new BookingDetailsAdapter(data, getApplicationContext(),this);
        _singleLorryBookingDetails.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        _singleLorryBookingDetails.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lorry_owner_flow, menu);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, int code) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+91"+data.get(clickedItemIndex).getMerchantMobile()));
        startActivity(intent);
    }
}
