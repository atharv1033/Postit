package com.atharv.postit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.atharv.postit.Adapter.Channels_Adapter;
import com.atharv.postit.Model.Channels_Model;
import com.firebase.ui.auth.AuthUI;
import com.atharv.postit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Channels_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Here we are using FireBase Authentication
    List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String username , email ;

    //TextView header_username, header_email;

    // RecyclerView Global Variables
    RecyclerView channels_RecyclerView;
    List<Channels_Model> channels_modelList = new ArrayList<>();
    Channels_Adapter channels_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_channels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Main Code For authentication starts here

        if (user == null) {
            // Authenticate if user is not added
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            startActivityForResult(signInIntent, 9999);
        } else {
            email = user.getEmail();
            // Checking if the user Already authenticated but username doesn't exist
            //if all ok then get username
                db.collection("Users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(DocumentSnapshot doc : task.getResult()) {
                                    username = doc.getId();
                                    Toast.makeText(Channels_MainActivity.this, username, Toast.LENGTH_SHORT).show();
                                }

                                if(username == null) {
                                    //Create new User with already authenticated email
                                    Intent intent = new Intent(Channels_MainActivity.this, NewUser.class);
                                    startActivity(intent);
                                }
                            }
                        });





            /*NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
            View headerView1 = (View) navigationView1.inflateHeaderView(R.layout.nav_header_channels);

            header_username = headerView1.findViewById(R.id.header_username);
            header_email = headerView1.findViewById(R.id.header_email);

            header_username.setText(username);
            header_email.setText(email);*/

        }

        // Main Code for Channels_MainActivity list

        channels_RecyclerView = findViewById(R.id.channels_recyclerView);
        channels_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        channels_adapter = new Channels_Adapter(channels_modelList, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            //Send user to the channel he clicked
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(Channels_MainActivity.this,Channel.class);
                intent.putExtra("id",channels_model.getId());
                intent.putExtra("name",channels_model.getName());
                intent.putExtra("subject",channels_model.getSubject());
                intent.putExtra("topic",channels_model.getTopic());
                intent.putExtra("owner",channels_model.getOwner());
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        channels_RecyclerView.setAdapter(channels_adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        channels_modelList.clear();
        try {
                db.collection("Users").document(username).collection("Added_Channels").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String id;
                                for(DocumentSnapshot doc : task.getResult()) {
                                    id = doc.getId();

                                    db.collection("Channels").document(id).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot doc) {
                                                    Channels_Model channel = doc.toObject(Channels_Model.class);
                                                    channel.setId(doc.getId());
                                                    channels_modelList.add(channel);
                                                }
                                            });
                                }
                            }
                        });
                channels_adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.e("onResume FireBase",ex.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9999) {
            if(resultCode == RESULT_OK) {

                //Check if user with authenticated email id already exists
                email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    db.collection("Users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for(DocumentSnapshot doc : task.getResult()) {
                                        username = doc.getId();
                                        Toast.makeText(Channels_MainActivity.this, username, Toast.LENGTH_SHORT).show();
                                    }

                                    if(username == null) {
                                        //Create new User with authenticated email
                                        Intent intent = new Intent(Channels_MainActivity.this, NewUser.class);
                                        startActivity(intent);
                                    }
                                }
                            });


                            }else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.channels, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_channels) {
            startActivity(new Intent(this,MyChannels.class).putExtra("username",username));
        } else if (id == R.id.nav_pinned_posts) {
            startActivity(new Intent(this,PinnedPost.class).putExtra("username",username));
        } else if (id == R.id.nav_following) {
            startActivity(new Intent(this,Following.class).putExtra("username",username));
        } else if (id == R.id.nav_new_channel) {
            startActivity(new Intent(this,NewChannel.class).putExtra("username",username));
        } else if (id == R.id.nav_channel_search) {
            startActivity(new Intent(this,ChannelSearch.class).putExtra("username",username));
        } else if (id == R.id.nav_post_search) {

        } else if (id == R.id.nav_person_search) {
            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
