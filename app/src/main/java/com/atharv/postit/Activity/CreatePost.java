package com.atharv.postit.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.Model.File_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePost extends AppCompatActivity {

    FirebaseFirestore db;

    EditText postTitle_editText,postContent_editText;
    String postTitle,postContent,username,channel_id;
    List<File_Model> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        username = getIntent().getStringExtra("username");
        channel_id = getIntent().getStringExtra("channel_id");

        db = FirebaseFirestore.getInstance();
        postTitle_editText = findViewById(R.id.postTitle_editText);
        postContent_editText = findViewById(R.id.postDescription_editText);
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

                db.collection("Posts").add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();
                            }
                        });

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

    public void add_UploadFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 42);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 42) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    String displayName;
                    File_Model file_model;
                    ClipData clipData = data.getClipData();
                    for(int i = 0; i < clipData.getItemCount(); i++)
                    {
                        ClipData.Item path = clipData.getItemAt(i);
                        Uri file = path.getUri();
                        Cursor cursor = this.getContentResolver().query(file,null, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            file_model = new File_Model(displayName,file);
                            fileList.add(file_model);
                        }
                    }
                }
            } else {
                Toast.makeText(this, "item not added", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
