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
import com.pkb149.SVT.LorryOwnerFlow.FleetDetails;
import com.pkb149.SVT.utility.PrefManager;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.Bind;


public class SignupActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "SignupActivity";
    String[] userType = { "", "Lorry Owner", "Agent"};
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
        msg91 = new MSG91(getString(R.string.sendotp_key));

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

        /*final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending OTP...");
        progressDialog.show();*/

        String name = _nameText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String userTypes=_userType.getSelectedItem().toString();
        char otp_char[]=OTP(4);
        String otp= new String(otp_char);
        Log.d(TAG,otp);
        msg91.composeMessage("iPOOLA", "Your OTP is "+otp);
        msg91.to(mobile);
        String sendStatus = msg91.send();
        Log.d(TAG, sendStatus);
        Intent intent=new Intent(SignupActivity.this,EnterOTP.class);
        intent.putExtra("name",name);
        intent.putExtra("phone",mobile);
        intent.putExtra("password",password);
        intent.putExtra("userType",userTypes);
        intent.putExtra("OTP",otp);

        //progressDialog.dismiss();
        startActivity(intent);

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


    static char[] OTP(int len)
    {
        // Using numeric values
        String numbers = "0123456789";
        // Using random method
        Random rndm_method = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
                    numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }

    void sendOTP(){
        String name = _nameText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String userTypes=_userType.getSelectedItem().toString();
        char otp_char[]=OTP(4);
        String otp= new String(otp_char);
        Log.d(TAG,otp);
        msg91.composeMessage("iPOOLA", "Your OTP is "+otp);
        msg91.to(mobile);
        String sendStatus = msg91.send();
        Log.d(TAG, sendStatus);
        Intent intent=new Intent(SignupActivity.this,EnterOTP.class);
        intent.putExtra("name",name);
        intent.putExtra("phone",mobile);
        intent.putExtra("password",password);
        intent.putExtra("userType",userTypes);
        intent.putExtra("OTP",otp);
        startActivity(intent);
    }

}