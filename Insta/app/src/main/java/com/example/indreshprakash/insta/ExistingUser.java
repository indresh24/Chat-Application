package com.example.indreshprakash.insta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import com.onesignal.OneSignal;
import com.parse.LogInCallback;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

public class ExistingUser extends AppCompatActivity {

    public void signupnewuser(View view)
    {
        Intent intent=new Intent(this,NewUser.class);
        this.startActivity(intent);

    }
    public void redirectactivity()
    {
        Intent intent=new Intent(this,ListUsers.class);
        this.startActivity(intent);
    }
    public void login(View view)
    {
        EditText UserName=(EditText)findViewById(R.id.UserName);
        EditText UserPassword=(EditText)findViewById(R.id.UserPassword);
        ParseUser user=new ParseUser();
        user.logInInBackground(UserName.getText().toString(), UserPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null && user!=null)
                {
                    redirectactivity();
                }
                else
                {
                    Toast.makeText(ExistingUser.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);
        getSupportActionBar().hide();
    }
}
