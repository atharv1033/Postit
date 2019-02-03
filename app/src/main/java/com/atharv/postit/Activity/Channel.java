package com.atharv.postit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atharv.postit.Adapter.Posts_Adapter;
import com.atharv.postit.Model.Posts_Model;
import com.atharv.postit.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class Channel extends Activity {

    //All Attributes of a channel
    String channel_id, channel_name, channel_subject, channel_topic, channel_owner, username;

    RecyclerView post_RecyclerView;
    Posts_Adapter posts_adapter;
    List<Posts_Model> posts_modelList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        //Get Attributes of channel
        channel_id = this.getIntent().getExtras().getString("id");
        channel_name = this.getIntent().getExtras().getString("name");
        channel_subject = this.getIntent().getExtras().getString("subject");
        channel_topic = this.getIntent().getExtras().getString("topic");
        channel_owner = this.getIntent().getExtras().getString("owner");
        username = this.getIntent().getStringExtra("username");

        //Main Code for recyclerView List for posts
        post_RecyclerView = findViewById(R.id.posts_recyclerView);
        post_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        posts_adapter = new Posts_Adapter(posts_modelList, new Posts_Adapter.OnPostClickListener() {
            @Override
            public void onPostClickListener(Posts_Model posts_model) {
                Intent intent = new Intent(Channel.this,Post.class);
                intent.putExtra("id",posts_model.getId());
                startActivity(intent);
            }

            @Override
            public void onPostLongClickListener(Posts_Model posts_model) {

            }
        });
        post_RecyclerView.setAdapter(posts_adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();


        posts_adapter.notifyDataSetChanged();
    }

    public void addPost(View view) {
    }
}
