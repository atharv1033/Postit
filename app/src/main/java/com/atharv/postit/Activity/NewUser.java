package com.atharv.postit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atharv.postit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewUser extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText userName_editText , fullName_editText , mobileNumber_editText ;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        userName_editText = findViewById(R.id.editText_userName);
        fullName_editText = findViewById(R.id.editText_fullName);
        mobileNumber_editText = findViewById(R.id.editText_mobileNumber);

        email = user.getEmail();
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        fullName_editText.setText(user.getDisplayName());
        mobileNumber_editText.setText(user.getPhoneNumber());

    }

    public void create_new_user(View view) {
        String userName , fullName , mobileNumber;
        userName = userName_editText.getText().toString();
        fullName = fullName_editText.getText().toString();
        mobileNumber = mobileNumber_editText.getText().toString();

        if(userName.contains("@") || userName.contains(".com")){
            Toast.makeText(this, "Cannot Contain @ or .com", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,Object> userData = new HashMap<>();
        userData.put("Full Name",fullName);
        userData.put("Mob no.",mobileNumber);
        userData.put("email",email);

        try{
            db.collection("Users").document(userName).set(userData);

            Intent intent = new Intent(this,Channels_MainActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this, "Username already taken try another", Toast.LENGTH_SHORT).show();
        }
    }
}
