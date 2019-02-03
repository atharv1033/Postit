package com.atharv.postit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewChannel extends AppCompatActivity {

    EditText editText_ChannelName,editText_ChannelSubject,editText_ChannelTopic;
    String channelName,channelSubject,channelTopic,username;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);

        db = FirebaseFirestore.getInstance();

        editText_ChannelName = findViewById(R.id.Cname);
        editText_ChannelSubject = findViewById(R.id.Csubject);
        editText_ChannelTopic = findViewById(R.id.Ctopic);

        username = getIntent().getStringExtra("username");



    }

    public void create_new_channel(View view) {

        channelName = editText_ChannelName.getText().toString();
        channelSubject = editText_ChannelSubject.getText().toString();
        channelTopic = editText_ChannelTopic.getText().toString();

        if(!channelName.equals("") && !channelSubject.equals("") && !channelSubject.equals("")) {
            Map<String, Object> channel = new HashMap<>();
            channel.put("name", channelName);
            channel.put("owner", username);
            channel.put("subject", channelSubject);
            channel.put("topic", channelTopic);

            try {
                db.collection("Channels").add(channel);
            } catch (Exception ex) {
                Log.e("FireBase Error", ex.getMessage());
            }
        }else {

            Toast.makeText(this, "Enter Values", Toast.LENGTH_SHORT).show();
        }
    }
}
