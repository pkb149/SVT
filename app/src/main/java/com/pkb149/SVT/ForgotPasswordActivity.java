package com.pkb149.SVT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class ForgotPasswordActivity extends AppCompatActivity {
    @Bind(R.id.input_phoneForgotPassword) EditText _phoneText;
    @Bind(R.id.btn_requstOTP) Button _requestOTPButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        _requestOTPButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(validate()){
                    _requestOTPButton.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sending OTP...");
                    progressDialog.show();
                    final AsyncHttpClient client = new AsyncHttpClient(true,80,443);
                    client.addHeader("Content-Type", "application/json");
                    String restApiUrl=getResources().getString(R.string.server_url)+"send_otp_to_reset_password/";

                    final String mobile = _phoneText.getText().toString();


                    final JSONObject params = new JSONObject();
                    try{
                        params.put("username", mobile);

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
                            Intent intent=new Intent(getApplicationContext(),WaitForOTPActivity.class);
                            intent.putExtra("mobile",mobile);
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
                            _requestOTPButton.setEnabled(true);
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
                else {
                    _requestOTPButton.setEnabled(true);
                    return;
                }

            }
        });
    }

    public boolean validate() {
        boolean valid = true;
        String phone = _phoneText.getText().toString();

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches() || (phone.length() != 10)) {
            _phoneText.setError("Enter a valid Phone Number without 0 or +91");
            valid = false;
        } else {
            _phoneText.setError(null);
        }
        return valid;
    }
}
