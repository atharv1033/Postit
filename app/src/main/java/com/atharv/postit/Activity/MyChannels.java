package com.atharv.postit.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.atharv.postit.Adapter.Channels_Adapter;
import com.atharv.postit.Model.Channels_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyChannels extends AppCompatActivity {

    String username;
    FirebaseFirestore db;
    RecyclerView channels_RecyclerView;
    List<Channels_Model> channels_modelList = new ArrayList<>();
    Channels_Adapter channels_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_channels);

        username = getIntent().getStringExtra("username");
        db = FirebaseFirestore.getInstance();
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();

        channels_RecyclerView = findViewById(R.id.recyclerView_myChannels);
        channels_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        channels_adapter = new Channels_Adapter(channels_modelList, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            //Send user to the channel he clicked
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(MyChannels.this,Channel.class);
                intent.putExtra("id",channels_model.getId());
                intent.putExtra("name",channels_model.getName());
                intent.putExtra("subject",channels_model.getSubject());
                intent.putExtra("topic",channels_model.getTopic());
                intent.putExtra("owner",channels_model.getOwner());
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        channels_RecyclerView.setAdapter(channels_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            db.collection("Channels").whereEqualTo("owner",username).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            channels_modelList.clear();
                            Toast.makeText(MyChannels.this, "Inside", Toast.LENGTH_SHORT).show();
                            for(DocumentSnapshot doc : task.getResult()) {
                                Channels_Model channel = doc.toObject(Channels_Model.class);
                                channel.setId(doc.getId());
                                channels_modelList.add(channel);
                            }
                            channels_adapter.notifyDataSetChanged();
                        }
                    });
        } catch(Exception ex){
            Toast.makeText(this, "Firebase error", Toast.LENGTH_SHORT).show();
            Log.e("FireBase Error", ex.getMessage());
        }
    }
}
