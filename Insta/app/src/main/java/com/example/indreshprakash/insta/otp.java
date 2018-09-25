package com.example.indreshprakash.insta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class otp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().hide();
        otp();

    }

    public String OTP="";

    public void otp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.SEND_SMS)) != PackageManager.PERMISSION_GRANTED) {
                   if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 1);
            }
        }else {
                final String message=generateotp();
                OTP=message;
                ParseQuery<ParseUser> user=ParseUser.getQuery();
                user.whereEqualTo("email",ParseUser.getCurrentUser().getEmail());
                user.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e==null)
                        {
                            if(objects.size()>0) {
                                for (ParseUser object : objects) {
                                    String mobile = (object.getString("Mobile"));
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(mobile, null, "Your OTP is "+message, null, null);
                                }
                            }
                        }

                    }
                });
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                final String message=generateotp();
                OTP=message;
                Intent intent = getIntent();
                ParseQuery<ParseUser> user=ParseUser.getQuery();
                user.whereEqualTo("email",ParseUser.getCurrentUser().getEmail());
                user.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                for (ParseUser object : objects) {
                                    String mobile = (object.getString("Mobile"));
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(mobile, null, "Your OTP is "+message, null, null);
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
            }
        }
            }

    public void Continue(View view)
    {
        EditText CheckOTP=(EditText)findViewById(R.id.EnterOtp);
        if(CheckOTP.getText().toString().equals(OTP))
        {
            Intent intent=new Intent(getApplicationContext(),RecoveryPassword.class);
            this.startActivity(intent);
        }

    }
    public String generateotp()
    {
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        String otp = "";

        for (int i = 0; i < 6; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp +=numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;

    }
}
