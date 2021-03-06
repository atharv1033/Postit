package com.atharv.postit.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class ViewChannel extends AppCompatActivity {

    String channel_id, channel_name, channel_subject, channel_topic, channel_owner, username, tags;
    TextView name_textView, subject_textView, topic_textView, owner_textView, tags_textView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_view);

        db = FirebaseFirestore.getInstance();

        //Get Attributes of channel
        channel_id = this.getIntent().getExtras().getString("id");
        channel_name = this.getIntent().getExtras().getString("name");
        channel_subject = this.getIntent().getExtras().getString("subject");
        channel_topic = this.getIntent().getExtras().getString("topic");
        channel_owner = this.getIntent().getExtras().getString("owner");
        username = this.getIntent().getStringExtra("username");
        tags = this.getIntent().getStringExtra("tags");

        name_textView = findViewById(R.id.channelName_textView);
        subject_textView = findViewById(R.id.channelSubject_textView);
        topic_textView = findViewById(R.id.channelTopic_textView);
        owner_textView = findViewById(R.id.channelOwner_textView);
        tags_textView = findViewById(R.id.channelTags_textView);

        name_textView.setText(channel_name);
        subject_textView.setText(channel_subject);
        topic_textView.setText(channel_topic);
        owner_textView.setText(channel_owner);
        tags_textView.setText(tags);

    }

    public void subscribe_channel(View view) {

        try {
            db.collection("Users").document(username).collection("Added_Channels").document(channel_id).set(Collections.singletonMap("exists", true))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ViewChannel.this, "Channel Subscribed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch(Exception ex){
            Log.e("FireBase Error", ex.getMessage());
            Toast.makeText(this, "Already Subscribed", Toast.LENGTH_SHORT).show();
        }
    }
}
