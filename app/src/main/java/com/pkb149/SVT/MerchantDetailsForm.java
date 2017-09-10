package com.pkb149.SVT;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pkb149.SVT.utility.PrefManager;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MerchantDetailsForm extends AppCompatActivity {
    PrefManager prefManager;
    @Bind(R.id.proceed_to_payment_button) TextView _proceedToPayment;
    @Bind(R.id.pick_up_address_et) TextView _pickUpAddress;
    @Bind(R.id.drop_address_et) TextView _dropAddress;
    @Bind(R.id.merchant_name_et) TextView _merchantName;
    @Bind(R.id.merchant_mobile_number_et) TextView _merchantMobileNumber;
    ArrayList steps;
    String capacity;
    String time;
    String distance;
    String duration;
    int fromLocation;
    int toLocation;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details_form);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        steps=getIntent().getParcelableArrayListExtra("steps");
        distance=getIntent().getStringExtra("distance");
        duration=getIntent().getStringExtra("duration");
        fromLocation=getIntent().getExtras().getInt("from");
        toLocation=getIntent().getExtras().getInt("to");
        capacity=getIntent().getExtras().getString("capacity");
        date = (Date)getIntent().getSerializableExtra("time");
        _proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    Toast.makeText(getApplicationContext(),"One or more field has invalid Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), BookingConfirmation.class);
                intent.putExtra("steps",steps);
                intent.putExtra("distance",distance);
                intent.putExtra("duration",duration);
                intent.putExtra("from",fromLocation);
                intent.putExtra("to",toLocation);
                intent.putExtra("capacity",capacity);
                intent.putExtra("time",date);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
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

    public boolean validate(){
        boolean valid = true;

        String pickUpAddress = _pickUpAddress.getText().toString();
        String dropAddress = _dropAddress.getText().toString();
        String merchantName = _merchantName.getText().toString();
        String merchantMobileNumber = _merchantMobileNumber.getText().toString();

        if (pickUpAddress.isEmpty()) {
            _pickUpAddress.setError("Pick Up Address Cannot be blank");
            valid = false;
        } else {
            _pickUpAddress.setError(null);
        }

        if (dropAddress.isEmpty()) {
            _dropAddress.setError("Drop Address Cannot be blank");
            valid = false;
        } else {
            _dropAddress.setError(null);
        }

        if (merchantName.isEmpty() || merchantName.length() < 3) {
            _merchantName.setError("at least 3 characters");
            valid = false;
        } else {
            _merchantName.setError(null);
        }

        if (merchantMobileNumber.isEmpty() ||  merchantMobileNumber.length()!=10) {
            _merchantMobileNumber.setError("Enter Valid Mobile Number without 0 or +91");
            //_reEnterPasswordText.setError(Html.fromHtml("<font color='green'>Password Do not match</font>"));
            valid = false;
        } else {
            _merchantMobileNumber.setError(null);
        }
        return valid;
    }
}
