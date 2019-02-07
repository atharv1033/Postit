package com.atharv.postit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.atharv.postit.Adapter.Channels_Adapter;
import com.atharv.postit.Model.Channels_Model;
import com.atharv.postit.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChannelSearch extends AppCompatActivity {

    EditText channel_search_editText;
    RecyclerView channel_search_recyclerView;
    List<Channels_Model> channels_List = new ArrayList<>();
    Channels_Adapter channels_adapter;
    FirebaseFirestore db;
    String username , channel_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_search);

        username = getIntent().getStringExtra("username");
        channel_search_editText = findViewById(R.id.channel_search_editText);
        channel_search_recyclerView = findViewById(R.id.channel_search_recyclerView);
        channel_search_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        channels_adapter = new Channels_Adapter(channels_List, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            public void onChannelClicked(Channels_Model channels_model) {

            }
        });
    }

    public void channel_search(View view) {

        channel_name = channel_search_editText.getText().toString();
    }
}
