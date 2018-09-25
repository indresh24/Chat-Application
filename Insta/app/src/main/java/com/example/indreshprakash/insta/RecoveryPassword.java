package com.example.indreshprakash.insta;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class RecoveryPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        getSupportActionBar().hide();
    }
    public void confirm(View view)
    {
        final EditText EnterPassword=(EditText)findViewById(R.id.EnterPassword);
        EditText ConfirmPassword=(EditText)findViewById(R.id.ConfirmPassword);
        if(EnterPassword.getText().toString().equals(ConfirmPassword.getText().toString()))
        {
            ParseQuery<ParseUser> user=ParseUser.getQuery();
            user.whereEqualTo("email",ParseUser.getCurrentUser().getEmail());
            user.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null) {
                        if (objects.size() > 0) {
                            for(final  ParseUser user:objects) {
                                user.setPassword(EnterPassword.getText().toString());
                                Log.i("Successful","Successful");
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException f) {
                                        if(f==null)
                                        {
                                            Intent intent = new Intent(getApplicationContext(), ExistingUser.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),f.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


    }
}
