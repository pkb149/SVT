package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;


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
                    Intent intent = new Intent(getApplicationContext(), WaitForOTPActivity.class);
                    intent.putExtra("phone",_phoneText.getText().toString());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
