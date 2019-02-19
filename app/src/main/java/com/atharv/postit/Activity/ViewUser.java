package com.atharv.postit.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.List;

public class ViewUser extends AppCompatActivity {

    String UserName,Full_Name,email;
    TextView UserName_textView,Full_Name_textView,email_textView;
    Button follow_button;
    RecyclerView UserView_RecyclerView;
    Channels_Adapter channels_adapter;
    List<Channels_Model> channels_List = new ArrayList<>();

    FirebaseFirestore db;
    String username, callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        UserName_textView = findViewById(R.id.UserName_textView);
        Full_Name_textView = findViewById(R.id.FullName_textView);
        email_textView = findViewById(R.id.email_textView);
        follow_button = findViewById(R.id.follow_button);

        UserName = getIntent().getStringExtra("UserName");
        Full_Name = getIntent().getStringExtra("Full_Name");
        email = getIntent().getStringExtra("email");
        callingActivity = getIntent().getStringExtra("callingActivity");

        if (!(callingActivity.equals("SearchUser"))){
            follow_button.setVisibility(View.GONE);
        }

        username = getIntent().getStringExtra("username");

        db = FirebaseFirestore.getInstance();

        UserName_textView.setText(UserName);
        Full_Name_textView.setText(Full_Name);
        email_textView.setText(email);

        UserView_RecyclerView = findViewById(R.id.ViewUser_recyclerView);
        UserView_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        channels_adapter = new Channels_Adapter(channels_List, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(ViewUser.this, Channel.class);
                intent.putExtra("id", channels_model.getId());
                intent.putExtra("name", channels_model.getName());
                intent.putExtra("subject", channels_model.getSubject());
                intent.putExtra("topic", channels_model.getTopic());
                intent.putExtra("owner", channels_model.getOwner());
                intent.putExtra("username", username);
                intent.putExtra("CallingActivity", "ViewUser");
                startActivity(intent);
            }
        }, new Channels_Adapter.OnChannelLongClickedListener() {
            @Override
            public void onChannelLongClicked(Channels_Model channels_model) {

            }
        });

        UserView_RecyclerView.setAdapter(channels_adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            db.collection("Channels").whereEqualTo("owner",username).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            channels_List.clear();
                            for(DocumentSnapshot doc : task.getResult()) {
                                Channels_Model channel = doc.toObject(Channels_Model.class);
                                channel.setId(doc.getId());
                                channels_List.add(channel);
                            }
                            channels_adapter.notifyDataSetChanged();
                        }
                    });
        } catch(Exception ex){
            Log.e("FireBase Error", ex.getMessage());
        }
    }

    public void follow_user(View view) {

        try{

            db.collection("Users").document(username).collection("Following").document(UserName).set(Collections.singletonMap("exists",true))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ViewUser.this, "you are now following "+UserName, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch(Exception ex){
            Log.e("FireBase Error", ex.getMessage());
            Toast.makeText(ViewUser.this, "you are already following "+UserName, Toast.LENGTH_SHORT).show();
        }

    }
}
