package com.atharv.postit.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.atharv.postit.Adapter.Posts_Adapter;
import com.atharv.postit.Model.Posts_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PinnedPost extends AppCompatActivity {

    String username;
    RecyclerView Pinned_post_RecyclerView;
    Posts_Adapter Pinned_posts_adapter;
    List<Posts_Model> posts_modelList = new ArrayList<>();

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_post);

        username = this.getIntent().getStringExtra("username");

        Pinned_post_RecyclerView = findViewById(R.id.pinned_posts_recyclerView);
        Pinned_post_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Pinned_posts_adapter = new Posts_Adapter(posts_modelList, new Posts_Adapter.OnPostClickListener() {
            @Override
            public void onPostClickListener(Posts_Model posts_model) {
                Intent intent = new Intent(PinnedPost.this,Post.class);
                intent.putExtra("id",posts_model.getId());
                intent.putExtra("title",posts_model.getTitle());
                intent.putExtra("description",posts_model.getdescription());
                intent.putExtra("CallingActivity","PinnedPost");
                startActivity(intent);
            }

            @Override
            public void onPostLongClickListener(Posts_Model posts_model) {

            }
        });
        Pinned_post_RecyclerView.setAdapter(Pinned_posts_adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("Users").document(username).collection("PinnedPosts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()){
                            db.collection("Posts").document(doc.getId()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot doc) {
                                            Posts_Model post = doc.toObject(Posts_Model.class);
                                            post.setId(doc.getId());
                                            posts_modelList.add(post);
                                        }
                                    });
                        }
                    }
                });
    }
}
