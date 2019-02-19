package com.atharv.postit.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.atharv.postit.Adapter.Users_Adapter;
import com.atharv.postit.Model.Channels_Model;
import com.atharv.postit.Model.Users_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Following extends AppCompatActivity {

    RecyclerView user_search_recyclerView;
    List<Users_Model> users_List = new ArrayList<>();
    Users_Adapter users_adapter;
    FirebaseFirestore db;
    String username , UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        db = FirebaseFirestore.getInstance();

        username = getIntent().getStringExtra("username");

        user_search_recyclerView = findViewById(R.id.following_recyclerView);
        user_search_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users_adapter = new Users_Adapter(users_List, new Users_Adapter.OnUserClickedListener() {
            @Override
            public void onUserClicked(Users_Model users_model) {
                Intent intent = new Intent(Following.this,ViewUser.class);
                intent.putExtra("UserName", users_model.getUsername());
                intent.putExtra("email", users_model.getEmail());
                intent.putExtra("Full_Name", users_model.getFullName());
                intent.putExtra("username", username);
                intent.putExtra("callingActivity","Following");
                startActivity(intent);
            }
        },
                new Users_Adapter.OnUserLongClickedListener() {
                    @Override
                    public void onUserLongClicked(Users_Model users_model) {
                        db.collection("Users").document(username).collection("Following").document(users_model.getUsername());
                        users_List.remove(users_model);
                        users_adapter.notifyDataSetChanged();
                    }
                }
        );

        user_search_recyclerView.setAdapter(users_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            db.collection("Users").document(username).collection("Following").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            users_List.clear();
                            for(DocumentSnapshot doc : task.getResult()) {
                                UserName = doc.getId();
                                db.collection("Users").document(UserName).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot doc = task.getResult();
                                                try {
                                                    Users_Model users_model = new Users_Model();
                                                    users_model.setUsername(doc.getId());
                                                    users_model.setFullName(doc.get("Full_Name").toString());
                                                    users_model.setEmail(doc.get("email").toString());
                                                    users_List.add(users_model);
                                                    users_adapter.notifyDataSetChanged();
                                                } catch (Exception ex){
                                                    Log.e("Firebase error",ex.getMessage());
                                                    db.collection("Users").document(username).collection("Following").document(UserName).delete();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.e("onResume FireBase",ex.getMessage());
        }
    }
}
