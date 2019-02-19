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

public class SearchUser extends AppCompatActivity {

    EditText user_search_editText;
    RecyclerView user_search_recyclerView;
    List<Users_Model> users_List = new ArrayList<>();
    Users_Adapter users_adapter;
    FirebaseFirestore db;
    String username , searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        db = FirebaseFirestore.getInstance();

        username = getIntent().getStringExtra("username");

        user_search_editText = findViewById(R.id.user_search_editText);

        user_search_recyclerView = findViewById(R.id.user_search_recyclerView);
        user_search_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        users_adapter = new Users_Adapter(users_List, new Users_Adapter.OnUserClickedListener() {
            @Override
            public void onUserClicked(Users_Model users_model) {
                Intent intent = new Intent(SearchUser.this,ViewUser.class);
                intent.putExtra("UserName", users_model.getUsername());
                intent.putExtra("email", users_model.getEmail());
                intent.putExtra("Full_Name", users_model.getFullName());
                intent.putExtra("username", username);
                intent.putExtra("callingActivity","SearchUser");
                startActivity(intent);
            }
        },
                new Users_Adapter.OnUserLongClickedListener() {
                    @Override
                    public void onUserLongClicked(Users_Model users_model) {
                    }
                }
        );

        user_search_recyclerView.setAdapter(users_adapter);
    }

    public void user_search(View view) {

        searchText = user_search_editText.getText().toString();

        db.collection("Users").whereArrayContains("searchArray",searchText).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        users_List.clear();
                        for(DocumentSnapshot doc : task.getResult()){
                            Users_Model users_model = new Users_Model();
                            users_model.setUsername(doc.getId());
                            users_model.setFullName(doc.get("Full_Name").toString());
                            users_model.setEmail(doc.get("email").toString());
                            users_List.add(users_model);
                        }
                        users_adapter.notifyDataSetChanged();
                    }
                });
    }
}
