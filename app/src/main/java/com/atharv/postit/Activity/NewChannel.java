package com.atharv.postit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewChannel extends AppCompatActivity {

    EditText editText_ChannelName,editText_ChannelSubject,editText_ChannelTopic,editText_ChannelTags;
    String channelName,channelSubject,channelTopic,username, channelTags;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);

        db = FirebaseFirestore.getInstance();

        editText_ChannelName = findViewById(R.id.Cname);
        editText_ChannelSubject = findViewById(R.id.Csubject);
        editText_ChannelTopic = findViewById(R.id.Ctopic);
        editText_ChannelTags = findViewById(R.id.Ctags);

        username = getIntent().getStringExtra("username");



    }

    public void create_new_channel(View view) {

        channelName = editText_ChannelName.getText().toString();
        channelSubject = editText_ChannelSubject.getText().toString();
        channelTopic = editText_ChannelTopic.getText().toString();
        channelTags = editText_ChannelTags.getText().toString();

        if (channelName.length() > 10){
            Toast.makeText(this, "Name should be less than 10 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!(channelTags.contains("#"))){
            Toast.makeText(this, "Tags should start with #", Toast.LENGTH_SHORT).show();
            return;
        }

        channelTags = channelTags.replace(" ","");
        channelTags = channelTags.substring(1);
        String[] IterateArray = channelTags.split("#");
        List<String> TagArray = new ArrayList<>();

        for(String i : IterateArray){

            TagArray.add(i);
        }

        List<String> searchArray = new ArrayList<>();
        String temp_str = "";
        for(int i=0; i< channelName.length() ; i++) {
            temp_str = temp_str + channelName.charAt(i);
            searchArray.add(temp_str);
        }

        if(!channelName.equals("") && !channelSubject.equals("") && !channelSubject.equals("")) {
            Map<String, Object> channel = new HashMap<>();
            channel.put("name", channelName);
            channel.put("owner", username);
            channel.put("subject", channelSubject);
            channel.put("topic", channelTopic);
            channel.put("tags",TagArray);
            channel.put("searchArray",searchArray);

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
