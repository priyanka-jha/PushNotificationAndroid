package com.android.priyanka.pushnotificationandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private List<User> userList;
    RecyclerView recyclerUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = firebaseAuth.getInstance();

        //to fetch the  values from database and show in recyclerview
        loadUsers();

        //if the topic is already created then this app will subscribe to this topic,otherwise it will cfreate this topic
        FirebaseMessaging.getInstance().subscribeToTopic("updates");

         /* to identify the device to which notification is to be sent,
         we need regsistration token id*/
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("task successful");
                            String token = task.getResult().getToken();
                            saveToken(token);
                            System.out.println("token...." + token);
                        } else {
                            System.out.println("token not generated");
                            System.out.println(task.getException().getMessage());
                        }
                    }
                });
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        userList = new ArrayList<>();
        recyclerUser = findViewById(R.id.recycler_user);
        recyclerUser.setLayoutManager(new LinearLayoutManager(this));
        recyclerUser.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.HORIZONTAL));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifyuser");
        //if dont want realtime updates of the node notifyuser,then use addListenerForSingleValueEvent
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                //check if we have some values in this node
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
                        User user = dataSnapshot1.getValue(User.class);
                        userList.add(user);
                    }

                    UserAdapter userAdapter = new UserAdapter(getApplicationContext(),userList);
                    recyclerUser.setAdapter(userAdapter);
                }
                else {
                    Toast.makeText(getApplicationContext(),"no user found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveToken(String token) {
        String email = firebaseAuth.getCurrentUser().getEmail();

        User user = new User(email,token);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifyuser");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Token saved",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    //if user is not logged in then send to mainactivity
    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser()==null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }
    }
}
