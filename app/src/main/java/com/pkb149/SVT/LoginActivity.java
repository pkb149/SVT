package com.pkb149.SVT;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pkb149.SVT.LorryOwnerFlow.FleetDetails;
import com.pkb149.SVT.utility.PrefManager;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    PrefManager prefManager;
    String restApiUrl;
    //SVT/api/OwnerAPI/getTrucks

    String[] userTypeKey={"","O","A","M"};

    @Bind(R.id.input_phone) EditText _phoneText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.link_forgotPassword) TextView _forgotPasswordLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        restApiUrl=getResources().getString(R.string.server_url)+"login/";

        if (prefManager.isLoggedIn()) {
            if(prefManager.getUserType().equals("Agent")){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), FleetDetails.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        }
        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _forgotPasswordLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        final AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        //client.addHeader("Content-Type", "application/json");
        final JSONObject params = new JSONObject();
        try{
            params.put("username", phone);
            params.put("password", password);

        }
        catch (Exception e){

        }
        StringEntity entity=null;
        try{
            entity = new StringEntity(params.toString());
        }
        catch (Exception e){

        }

        Log.d(TAG,entity.toString());
        client.post(getApplicationContext(), restApiUrl, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String role=null;
                String sessionId=null;
                try{
                    role=response.getString("group");
                    sessionId=response.getString("token");
                }
                catch (Exception e){

                }
                onLoginSuccess(role,sessionId);
                Toast.makeText(getApplicationContext(),statusCode+"::::"+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(TAG,statusCode+"::::"+errorResponse);
                _loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(),statusCode+"::::"+errorResponse,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            /*@Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String doc2=null;
                try{
                    doc2 = new String(responseBody, "UTF-8");
                }
                catch (Exception e){

                }
                _loginButton.setEnabled(true);
                Log.d(TAG,statusCode+"::::"+doc2);
                Toast.makeText(getApplicationContext(),statusCode+"::::"+doc2,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }*/
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String role, String sessionId) {
        PrefManager prefManager = new PrefManager(this);

        _loginButton.setEnabled(false);
        if(role.charAt(0)=='o'){
            prefManager.setLoggedIn("Owner_"+sessionId);
            Intent intent = new Intent(getApplicationContext(), FleetDetails.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        else if(role.charAt(0)=='a'){
            prefManager.setLoggedIn("Agent_"+sessionId);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()||(phone.length()!=10)) {
            _phoneText.setError("Enter a valid Phone Number without 0 or +91");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            //_passwordText.setError("between 4 and 10 alphanumeric characters");

            _passwordText.setError(Html.fromHtml("<font color='green'>Password Do not match</font>"));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
