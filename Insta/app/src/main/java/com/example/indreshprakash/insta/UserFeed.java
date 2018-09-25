package com.example.indreshprakash.insta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import android.view.ViewGroup.LayoutParams;
public class UserFeed extends AppCompatActivity {

    public void goback(View view)
    {
        Intent intent=new Intent(getApplicationContext(),ListUsers.class);
        this.startActivity(intent);
    }
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2357aa")));
        Intent intent=getIntent();
        final LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        final String user=intent.getStringExtra("username");
        ParseQuery<ParseObject> images=new ParseQuery<ParseObject>("Image");
        images.whereEqualTo("username",user);
        images.orderByDescending("createdAt");
        images.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseObject object:objects)
                        {
                            ParseFile file=(ParseFile) object.get("image");
                            final String time=object.getCreatedAt().toString();
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null && data!=null)
                                    {
                                        Bitmap bitmap=BitmapFactory.decodeByteArray(data,0,data.length);
                                        ImageView image=new ImageView(getApplicationContext());
                                        image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                                        image.setImageBitmap(bitmap);
                                        image.setAdjustViewBounds(true);
                                        linearLayout.addView(image);
                                        TextView textView2 = new TextView(getApplicationContext());
                                        textView2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                        textView2.setText(time);
                                        textView2.setTextColor(Color.GRAY);
                                        textView2.setPadding(20, 20, 20, 20);
                                        linearLayout.addView(textView2);
                                    }
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"The User Has Not Uploaded Any Photos",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        setTitle("Photos");
    }
}
