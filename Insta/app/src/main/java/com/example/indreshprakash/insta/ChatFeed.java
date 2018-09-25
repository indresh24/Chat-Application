package com.example.indreshprakash.insta;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.app.Application;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ListView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class ChatFeed extends AppCompatActivity {

    /*Intent intent=getIntent();
    String recepient=intent.getStringExtra("username");
    ArrayList<String> messages=new ArrayList<>();
    final ArrayAdapter chatAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);*/
    public void send(View view)
    {
        final ArrayList<String> messages=new ArrayList<>();
        final ArrayAdapter chatAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);
        final EditText chatText=(EditText)findViewById(R.id.ChatText);
        Intent intent=getIntent();
        final String sender=ParseUser.getCurrentUser().getUsername();
        final String recepient=intent.getStringExtra("username");
        final String messageContent = chatText.getText().toString();
        int SDK_INT= Build.VERSION.SDK_INT;
        if(SDK_INT>8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                String jsonResponse;

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", "Basic YOUR REST API");
                con.setRequestMethod("POST");
                String strJsonBody = "{"
                        +   "\"app_id\": \"YOUR APP ID\","
                        +   "\"filters\": [{\"field\": \"tag\", \"key\": \"username\", \"relation\": \"=\", \"value\": \""+recepient+"\"}],"
                        +   "\"data\": {\"foo\": \"bar\"},"
                        +   "\"contents\": {\"en\": \""+sender+":"+messageContent+"\"},"
                        +   "\"android_group\": \"recepient\","
                        +   "\"android_group_message\": {\"en\": \"You have $[notif_count] new messages\"}"
                        +   "}";



                System.out.println("strJsonBody:\n" + strJsonBody);

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();
                System.out.println("httpResponse: " + httpResponse);

                if (  httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);

            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
        ParseObject object=new ParseObject("Message");
        object.put("Sender", sender);
        object.put("Receiver",recepient);
        object.put("Message",messageContent);
        chatText.setText("");
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null)
                {
                    messages.add(messageContent);
                    chatAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error Occured! Please Try Again Later ",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ArrayList<String> messages=new ArrayList<>();
        final ArrayAdapter chatAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
            // TODO: Add OneSignal initialization here
        setContentView(R.layout.activity_chat_feed);
        Intent intent=getIntent();
        final String user=intent.getStringExtra("username");
        final String user2=intent.getStringExtra("username2");
        setTitle(user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        ListView chatHistory=(ListView) findViewById(R.id.ChatHistory);
        chatHistory.setAdapter(chatAdapter);
        ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("Sender",user2);
        query1.whereEqualTo("Receiver",user);
        ParseQuery<ParseObject> query2=new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("Receiver",user2);
        query2.whereEqualTo("Sender",user);
        List<ParseQuery<ParseObject>> queries=new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);
        ParseQuery<ParseObject> query=ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        messages.clear();
                        for(ParseObject message: objects)
                        {
                            String messageContent=message.getString("Message");
                            if(!message.getString("Sender").equals(user2))
                            {
                                messageContent=">" + messageContent;

                            }
                            messages.add(messageContent);
                        }

                        chatAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

                handler.postDelayed(this,5000);
            }
        },5000);
            }


    }

