package com.example.indreshprakash.insta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.parse.Parse;
import com.parse.ParseACL;
import android.widget.ImageView;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseInstallation;


public class MainActivity extends AppCompatActivity {

    public void login(View view)
    {
        Intent intent=new Intent(this,ExistingUser.class);
        this.startActivity(intent);
    }
    public void signup(View view)
    {
        Intent intent=new Intent(this,NewUser.class);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(" APP ID ")
                .clientKey("YOUR CLIENT KEY")
                .server(" YOUR SERVER ADDRESS")
                .build()
        );



        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        getSupportActionBar().hide();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        if(ParseUser.getCurrentUser()!=null)
        {
         Intent intent=new Intent(MainActivity.this,ListUsers.class);
         this.startActivity(intent);
        }
    }
}
