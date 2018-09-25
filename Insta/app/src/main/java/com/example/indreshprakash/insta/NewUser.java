package com.example.indreshprakash.insta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.SignUpCallback;
import android.content.Intent;
import com.parse.ParseUser;

public class NewUser extends AppCompatActivity {

    public void login(View view)
    {
        Intent intent=new Intent(this,ExistingUser.class);
        this.startActivity(intent);

    }
    public void redirectactivity()
    {
        Intent intent=new Intent(this,ListUsers.class);
        this.startActivity(intent);
    }
    public void started(View view)
    {
        EditText NewUserName=(EditText)findViewById(R.id.UserName);
        EditText NewUserEmail=(EditText)findViewById(R.id.NewUserEmail);
        EditText NewUserPassword=(EditText)findViewById(R.id.NewUserPassword);
        EditText NewUserMobile=(EditText)findViewById(R.id.NewUserMobile);
        ParseUser user=new ParseUser();
        user.setUsername(NewUserName.getText().toString());
        user.setEmail(NewUserEmail.getText().toString());
        user.setPassword(NewUserPassword.getText().toString());
        ParseACL acl=new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        user.setACL(acl);
        
        user.put("Mobile",NewUserMobile.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Log.i("Signup","Successful");
                    redirectactivity();
                }
                else
                {
                    Toast.makeText(NewUser.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        getSupportActionBar().hide();
    }
}