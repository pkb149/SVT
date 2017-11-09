package com.pkb149.SVT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pkb149.SVT.LorryOwnerFlow.FleetDetails;
import com.pkb149.SVT.utility.PrefManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class EnterOTP extends AppCompatActivity {
    @Bind(R.id.input_OTP) EditText _OTPText;
    @Bind(R.id.btn_submitOTP) Button _submitOTPButton;
    @Bind(R.id.link_requestOTPAgain) TextView _requestOTPAgainLink;
    String phone;
    String name;
    String password;
    String userType;
    String OTP;
    int sessionId=191;
    String restApiUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enter_otp);
        ButterKnife.bind(this);
        phone=getIntent().getExtras().getString("phone");
        name=getIntent().getExtras().getString("name");
        password=getIntent().getExtras().getString("password");
        userType=getIntent().getExtras().getString("userType");
        OTP=getIntent().getExtras().getString("OTP");
        //TODO perform request again with this phone number.
        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("mobileNumber", phone);
        params.put("userName", name);
        params.put("userRole", userType);
        params.put("password", password);

        _submitOTPButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String submittedOTP=_OTPText.getText().toString();
                if(submittedOTP.equals(OTP)){
                    //TODO
                    //submit the details to server
                    final ProgressDialog progressDialog = new ProgressDialog(EnterOTP.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Creating Account...");
                    progressDialog.show();
                    client.post(restApiUrl, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            onSignupSuccess();
                            // onSignupFailed();
                            Toast.makeText(getApplicationContext(),responseBody.toString(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getApplicationContext(),responseBody.toString(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                    _OTPText.setText("");
                }

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

    public void onSignupSuccess() {
        PrefManager prefManager = new PrefManager(this);
        if(userType.equals("Agent")){
            prefManager.setLoggedIn("Agent_"+sessionId);
            _submitOTPButton.setEnabled(false);
            setResult(RESULT_OK, null);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else if(userType.equals("Lorry Owner")){
            prefManager.setLoggedIn("Lorry Owner_"+sessionId);
            _submitOTPButton.setEnabled(false);
            setResult(RESULT_OK, null);
            Intent intent = new Intent(getApplicationContext(), FleetDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }


    }
}

