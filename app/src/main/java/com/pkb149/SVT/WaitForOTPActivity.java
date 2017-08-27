package com.pkb149.SVT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WaitForOTPActivity extends AppCompatActivity {
    @Bind(R.id.input_OTP) EditText _OTPText;
    @Bind(R.id.btn_submitOTP) Button _submitOTPButton;
    @Bind(R.id.link_requestOTPAgain) TextView _requestOTPAgainLink;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_otp);
        phone=getIntent().getExtras().getString("phone");
        //TODO perform request again with this phone number.
        ButterKnife.bind(this);
        _submitOTPButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //TODO validate OTP and then if got success do below.
                // If failed ask to enter again. or request new.
                Intent intent = new Intent(getApplicationContext(), resetPasswordActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _requestOTPAgainLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //TODO request for OTP
                Toast.makeText(getBaseContext(), "Request for OTP submitted again", Toast.LENGTH_LONG).show();
            }
        });

    }
}
