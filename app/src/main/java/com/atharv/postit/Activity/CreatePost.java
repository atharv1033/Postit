package com.atharv.postit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity {

    FirebaseFirestore db;

    EditText postTitle_editText,postContent_editText;
    String postTitle,postContent,username,channel_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        username = getIntent().getStringExtra("username");
        channel_id = getIntent().getStringExtra("channel_id");

        db = FirebaseFirestore.getInstance();
        postTitle_editText = findViewById(R.id.postTitle_editText);
        postContent_editText = findViewById(R.id.postContent_editText);
    }

    public void Create_Post(View view) {

        postTitle = postTitle_editText.getText().toString();
        postContent = postContent_editText.getText().toString();

        if(!(postTitle.equals("")) && !(postContent.equals(""))) {
            try {
                Map<String,Object> post = new HashMap<>();
                post.put("title",postTitle);
                post.put("content",postContent);
                post.put("channel_id",channel_id);
                post.put("owner",username);

                db.collection("Posts").add(post);

                Toast.makeText(this, "Post Created", Toast.LENGTH_SHORT).show();
                super.onBackPressed();

            } catch (Exception ex) {
                Log.e("Firebase", ex.getMessage());
                Toast.makeText(this, "FireBase Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter all Details", Toast.LENGTH_SHORT).show();
        }

    }
}
