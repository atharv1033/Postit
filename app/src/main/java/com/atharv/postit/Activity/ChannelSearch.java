package com.atharv.postit.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.Adapter.Channels_Adapter;
import com.atharv.postit.Model.Channels_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChannelSearch extends AppCompatActivity {

    EditText channel_search_editText;
    RecyclerView channel_search_recyclerView;
    List<Channels_Model> channels_List = new ArrayList<>();
    Channels_Adapter channels_adapter;
    FirebaseFirestore db;
    String username , searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_search);

        db = FirebaseFirestore.getInstance();

        username = getIntent().getStringExtra("username");
        channel_search_editText = findViewById(R.id.channel_search_editText);
        channel_search_recyclerView = findViewById(R.id.channel_search_recyclerView);
        channel_search_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        channels_adapter = new Channels_Adapter(channels_List, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(ChannelSearch.this,ChannelView.class);
                intent.putExtra("id",channels_model.getId());
                intent.putExtra("name",channels_model.getName());
                intent.putExtra("subject",channels_model.getSubject());
                intent.putExtra("topic",channels_model.getTopic());
                intent.putExtra("owner",channels_model.getOwner());
                intent.putExtra("username",username);
                intent.putExtra("CallingActivity","ChannelSearch");
                startActivity(intent);
            }
        });

        channel_search_recyclerView.setAdapter(channels_adapter);
    }

    public void channel_search(View view) {

        searchText = channel_search_editText.getText().toString();

        if(searchText.contains("@") && searchText.endsWith(".com")) {
            db.collection("Users").whereEqualTo("email", searchText).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                searchText = doc.getId();
                            }
                                db.collection("Channels").whereEqualTo("owner", searchText).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                channels_List.clear();
                                                for (DocumentSnapshot doc : task.getResult()) {
                                                    Channels_Model channel = doc.toObject(Channels_Model.class);
                                                    channel.setId(doc.getId());
                                                    Toast.makeText(ChannelSearch.this, "some", Toast.LENGTH_SHORT).show();
                                                    channels_List.add(channel);
                                                }

                                                channels_adapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                    });
        } else {

            db.collection("Channels").whereEqualTo("owner", searchText).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            channels_List.clear();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Channels_Model channel = doc.toObject(Channels_Model.class);
                                channel.setId(doc.getId());
                                Toast.makeText(ChannelSearch.this, "some", Toast.LENGTH_SHORT).show();
                                channels_List.add(channel);
                            }

                            channels_adapter.notifyDataSetChanged();
                        }
                    });
        }

    }
}
