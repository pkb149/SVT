package com.pkb149.SVT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class WaitForOTPActivity extends AppCompatActivity {
    @Bind(R.id.input_OTP) EditText _OTPText;
    @Bind(R.id.input_password_reset) EditText _password;
    @Bind(R.id.input_re_password_reset) EditText _passwordAgain;
    @Bind(R.id.btn_submitOTP) Button _submitOTPButton;
    @Bind(R.id.link_requestOTPAgain) TextView _requestOTPAgainLink;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_otp);
        phone=getIntent().getExtras().getString("mobile");
        //TODO perform request again with this phone number.
        ButterKnife.bind(this);
        _submitOTPButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(WaitForOTPActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Validating OTP and resetting password...");
                progressDialog.show();
                final AsyncHttpClient client = new AsyncHttpClient(true,80,443);
                client.addHeader("Content-Type", "application/json");
                String restApiUrl=getResources().getString(R.string.server_url)+"reset_password/";

                final String otp = _OTPText.getText().toString();
                final String password = _password.getText().toString();
                final JSONObject params = new JSONObject();
                try{
                    params.put("username", phone);
                    params.put("otp", otp);
                    params.put("password", password);

                } catch (Exception e){}
                StringEntity entity=null;
                try{
                    entity = new StringEntity(params.toString());
                }
                catch (Exception e){
                }
                client.post(getApplicationContext(), restApiUrl, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        intent.putExtra("mobile",phone);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        String doc2=null;
                        try{
                            doc2 = new String(responseBody, "UTF-8");
                        }
                        catch (Exception e){

                        }
                        Toast.makeText(getApplicationContext(),statusCode+"::::"+doc2,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String doc2=null;
                        try{
                            doc2 = new String(responseBody, "UTF-8");
                        }
                        catch (Exception e){

                        }
                        Toast.makeText(getApplicationContext(),statusCode+"::::"+doc2,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
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

    public boolean validate(){
        boolean valid = true;

        String otp = _OTPText.getText().toString();
        String password = _password.getText().toString();
        String reEnterPassword = _passwordAgain.getText().toString();

        if (otp.isEmpty() || otp.length()!= 6) {
            _OTPText.setError("Invalid OTP");
            valid = false;
        } else {
            _OTPText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _passwordAgain.setError("Password Do not match");
            //_reEnterPasswordText.setError(Html.fromHtml("<font color='green'>Password Do not match</font>"));
            valid = false;
        } else {
            _passwordAgain.setError(null);
        }
        return valid;
    }
}


