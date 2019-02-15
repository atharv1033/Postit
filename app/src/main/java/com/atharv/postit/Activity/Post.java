package com.atharv.postit.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.atharv.postit.Adapter.Files_Adapter;
import com.atharv.postit.Model.File_Model;
import com.atharv.postit.Model.Posts_Model;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Post extends AppCompatActivity {

    String post_id, post_title, post_description;
    TextView postTitle_textView,postDescription_textView;
    RecyclerView fileListView;
    Files_Adapter files_adapter;
    List<File_Model> fileList = new ArrayList<>();

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        post_id = this.getIntent().getStringExtra("id");
        post_title = this.getIntent().getStringExtra("title");
        post_description = this.getIntent().getStringExtra("description");


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        postTitle_textView = findViewById(R.id.postTitle_textView);
        postDescription_textView = findViewById(R.id.postDescription_textView);

        postTitle_textView.setText(post_title);
        postDescription_textView.setText(post_description);

        fileListView = findViewById(R.id.postFiles_list);
        fileListView.setLayoutManager(new LinearLayoutManager(this));
        files_adapter = new Files_Adapter(fileList, new Files_Adapter.OnFileClickedListner() {
            @Override
            public void onFileClicked(File_Model file_model) {
                StorageReference pathReference = storageRef.child(file_model.getReference());

                String DisplayName = file_model.getName();
                int lastDot = DisplayName.lastIndexOf('.');
                String extension = DisplayName.substring(lastDot + 1);
                String fileNameWithOutExt = DisplayName.replaceFirst("[.][^.]+$", "");

                File localFile = null;
                try {
                    localFile = File.createTempFile(fileNameWithOutExt, extension);
                }catch (IOException io) {
                    Log.e("File Reading Error", io.getMessage());
                }
                final File localFileForInitializeError = localFile;
                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        openFile(localFileForInitializeError);
                    }
                });
            }
        });

        fileListView.setAdapter(files_adapter);

    }

    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            db.collection("Posts").document(post_id).collection("Files").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            fileList.clear();
                            for (DocumentSnapshot doc : task.getResult()) {

                                File_Model file = doc.toObject(File_Model.class);
                                fileList.add(file);
                            }
                            files_adapter.notifyDataSetChanged();
                        }
                    });
        }catch (Exception ex) {

        }
    }
}
