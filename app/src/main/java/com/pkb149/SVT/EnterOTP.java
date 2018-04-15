package com.pkb149.SVT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.katepratik.msg91api.MSG91;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;




public class EnterOTP extends AppCompatActivity {
    @Bind(R.id.input_OTP) EditText _OTPText;
    @Bind(R.id.btn_submitOTP) Button _submitOTPButton;
    @Bind(R.id.link_requestOTPAgain) TextView _requestOTPAgainLink;
    @Bind(R.id.change_mob_no) TextView _changeMobNo;
    String restApiUrl;


    String TAG="EnterOTP";
    MSG91 msg91;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        ButterKnife.bind(this);
        final String mobile=getIntent().getExtras().getString("mobile");
        final String name=getIntent().getExtras().getString("first_name");
        final String userType=getIntent().getExtras().getString("last_name");
        final String password=getIntent().getExtras().getString("password");
        restApiUrl=getResources().getString(R.string.server_url)+"validate_otp/";
        //TODO perform request again with this phone number.
        final AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.addHeader("Content-Type", "application/json");
        _submitOTPButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String submittedOTP=_OTPText.getText().toString();
                final JSONObject params = new JSONObject();
                try{
                    params.put("username",mobile);
                    params.put("otp", submittedOTP);
                    params.put("first_name", name);
                    params.put("last_name", userType);
                    params.put("password", password);
                }
                catch (Exception e){}
                StringEntity entity=null;
                try{
                    entity = new StringEntity(params.toString());
                }
                catch (Exception e){}
                final ProgressDialog progressDialog = new ProgressDialog(EnterOTP.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                client.post(getApplicationContext(), restApiUrl, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        onSignupSuccess();
                        String doc2=null;
                        try{
                            doc2 = new String(responseBody, "UTF-8");
                        }
                        catch (Exception e){

                        }
                        Log.d(TAG,statusCode+"::::"+doc2);
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
                        Log.d(TAG,statusCode+"::::"+doc2);
                        Toast.makeText(getApplicationContext(),statusCode+"::::"+doc2,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

        _requestOTPAgainLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //TODO new api creation and call
            }
        });
        _changeMobNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    public void onSignupSuccess() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}

