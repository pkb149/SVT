package com.pkb149.SVT;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class SignupActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "SignupActivity";
    String[] userType = { "", "Agent","Lorry Owner",};
    String[] userTypeKey={"","agent","owner","merchant"};
    MSG91 msg91;

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_phoneSignUp) EditText _mobileText;
    @Bind(R.id.input_passwordSignUp) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.spinner_userType) Spinner _userType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        _userType.setOnItemSelectedListener(this);


        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,userType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _userType.setAdapter(aa);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending OTP...");
        progressDialog.show();
        final AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.addHeader("Content-Type", "application/json");
        String restApiUrl=getResources().getString(R.string.server_url)+"register/";

        final String name = _nameText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String userType=userTypeKey[_userType.getSelectedItemPosition()];


        final JSONObject params = new JSONObject();
        try{
            params.put("username", mobile);
            params.put("first_name", name);
            params.put("last_name", userType);
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
                Intent intent=new Intent(SignupActivity.this,EnterOTP.class);
                intent.putExtra("mobile",mobile);
                intent.putExtra("first_name",name);
                intent.putExtra("last_name",userType);
                intent.putExtra("password",password);
                startActivity(intent);
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




    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign Up failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String userTypes=_userType.getSelectedItem().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number without 0 or +91");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            //_reEnterPasswordText.setError(Html.fromHtml("<font color='green'>Password Do not match</font>"));
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        if (userTypes.isEmpty() || userTypes.length() == 0 || userTypes.equals("")) {
            valid = false;
            TextView errorText = (TextView)_userType.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please pick a user Type");
        } else {

        }

        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(getApplicationContext(),userType[position] ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}