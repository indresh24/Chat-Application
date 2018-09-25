package com.example.indreshprakash.insta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class List_chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        Intent intent=getIntent();
        final String user=intent.getStringExtra("username");
        setTitle("Chat");
        final ArrayList<String> usernames=new ArrayList<String>();
        final ListView listView=(ListView) findViewById(R.id.listChat);
        final ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,usernames);
        ParseQuery<ParseUser> userquery=ParseUser.getQuery();
        userquery.whereNotEqualTo("username",user);
        userquery.orderByAscending("username");
        userquery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseUser users:objects)
                        {
                            usernames.add(users.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(List_chat.this,ChatFeed.class);
                intent.putExtra("username",usernames.get(i));
                intent.putExtra("username2",user);
                startActivity(intent);
            }
        });

    }
}
