package com.example.indreshprakash.insta;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.onesignal.OneSignal;
import com.parse.LogOutCallback;
import com.parse.SaveCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
public class ListUsers extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        }
    }

    public void addphoto(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }
    public void logout(View view) {
        ParseUser user=ParseUser.getCurrentUser();
        OneSignal.deleteTag(user.getUsername());
        user.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(getApplicationContext(),"Logged Out Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NewUser.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void chat(View view)
    {
        String username=ParseUser.getCurrentUser().getUsername();
        Intent intent=new Intent(getApplicationContext(),List_chat.class);
        intent.putExtra("username",username);
        this.startActivity(intent);
    }
    public void change(View view)
    {
        Intent intent=new Intent(getApplicationContext(),Forgot_Password.class);
        this.startActivity(intent);
    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!= null)
        {
            Uri selectedimage=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedimage);
                //ImageView imageview=(ImageView)findViewById(R.id.bitimage);
                //bitimage.setImageBitmap(bitmap);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray=stream.toByteArray();
                ParseFile file=new ParseFile("image.png",byteArray);
                ParseObject object=new ParseObject("Image");
                object.put("image",file);
                object.put("username",ParseUser.getCurrentUser().getUsername());
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            Toast.makeText(ListUsers.this,"Upload Successful To Database",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ListUsers.this,e.getMessage().toString()+"Unable to add image to Database",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        setTitle("Photos");
        OneSignal.sendTag("username",ParseUser.getCurrentUser().getUsername());
        final ArrayList<String> usernames=new ArrayList<>();
        final ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,usernames);
        final ListView userList=(ListView) findViewById(R.id.userList);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),UserFeed.class);
                intent.putExtra("username",usernames.get(i));
                startActivity(intent);
            }
        });
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseUser user:objects)
                        {
                            usernames.add(user.getUsername());
                        }
                        userList.setAdapter(arrayAdapter);
                    }
                }
            }
        });
    }
}
