package com.atharv.postit.Activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.Adapter.Files_Adapter;
import com.atharv.postit.Model.File_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePost extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;

    RecyclerView fileListView;
    Files_Adapter files_adapter;
    EditText postTitle_editText, postDescription_editText;
    String postTitle, postDescription, username, channel_id;
    List<File_Model> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        username = getIntent().getStringExtra("username");
        channel_id = getIntent().getStringExtra("channel_id");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        fileListView = findViewById(R.id.uploadFiles_list);
        fileListView.setLayoutManager(new LinearLayoutManager(this));
        files_adapter = new Files_Adapter(fileList, new Files_Adapter.OnFileClickedListner() {
            @Override
            public void onFileClicked(File_Model file_model) {

            }
        });

        postTitle_editText = findViewById(R.id.postTitle_editText);
        postDescription_editText = findViewById(R.id.postDescription_editText);

        fileListView.setAdapter(files_adapter);
    }

    public void Create_Post(View view) {

        postTitle = postTitle_editText.getText().toString();
        postDescription = postDescription_editText.getText().toString();

        if (!(postTitle.equals("")) && !(postDescription.equals(""))) {
            try {
                Map<String, Object> post = new HashMap<>();
                post.put("title", postTitle);
                post.put("description", postDescription);
                post.put("channel_id", channel_id);
                post.put("owner", username);

                db.collection("Posts").add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();
                                for (File_Model file_model : fileList) {
                                    StorageReference fileRef = storageRef.child(id + "/" + file_model.getName());
                                    fileRef.putFile(file_model.getUri());

                                    try{

                                        Map<String,Object> fileMap = new HashMap<>();
                                        fileMap.put("name",file_model.getName());
                                        fileMap.put("reference",id + "/" + file_model.getName());

                                        db.collection("Posts").document(id)
                                                .collection("Files")
                                                .add(fileMap);
                                    } catch (Exception ex) {
                                        Log.e("FireStorage : ",ex.getMessage());
                                    }
                                }

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

        if (requestCode == 42) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String displayName;
                    File_Model file_model;
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item path = clipData.getItemAt(i);
                            Uri file = path.getUri();
                            Cursor cursor = this.getContentResolver().query(file, null, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                file_model = new File_Model(displayName, file);
                                fileList.add(file_model);
                                files_adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Uri file = data.getData();
                        Cursor cursor = this.getContentResolver().query(file, null, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            file_model = new File_Model(displayName, file);
                            fileList.add(file_model);
                            files_adapter.notifyDataSetChanged();
                        }
                    }
                }
            } else {
                Toast.makeText(this, "item not added", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

