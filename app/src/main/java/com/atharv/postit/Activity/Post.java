package com.atharv.postit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.atharv.postit.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class Post extends AppCompatActivity {

    String post_id, post_title, post_content;
    TextView postTitle_textView,postDescription_textView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        post_id = this.getIntent().getExtras().getString("id");

        db = FirebaseFirestore.getInstance();

        postTitle_textView = findViewById(R.id.postTitle_textView);
        postDescription_textView = findViewById(R.id.postDescription_textView);



    }
}
