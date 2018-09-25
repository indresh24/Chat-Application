package com.example.indreshprakash.insta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Forgot_Password extends AppCompatActivity {

    public void next(View view)
    {
        final EditText email=(EditText)findViewById(R.id.ForgotEmail);
        email.setText(ParseUser.getCurrentUser().getEmail());
        ParseQuery<ParseUser> user=ParseUser.getQuery();
        user.whereEqualTo("email",email.getText().toString());
        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null) {
                    if (objects.size() > 0) {
                        Intent intent = new Intent(getApplicationContext(), otp.class);
                        intent.putExtra("email", email.getText().toString());
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        getSupportActionBar().hide();
        final EditText email=(EditText)findViewById(R.id.ForgotEmail);
        email.setText(ParseUser.getCurrentUser().getEmail());
    }
}
