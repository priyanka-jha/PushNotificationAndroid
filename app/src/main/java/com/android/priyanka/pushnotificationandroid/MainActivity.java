package com.android.priyanka.pushnotificationandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edEmail)
    EditText edEmail;
    @BindView(R.id.edPwd)
    EditText edPwd;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String email,pwd;
    private FirebaseAuth firebaseAuth;

    public static String CHANNEL_ID = "notifychannelid";
    //for os version >= oreo
    public static String CHANNEL_NAME = "notifychannelname";
    //public static String CHANNEL_ID = "notifychannelid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.btnSignUp)
    public void onViewClicked() {
        //to get unique device id of registration token we need authentication
        createUser();
    }

    private void createUser() {
        email = edEmail.getText().toString().trim();
        pwd = edPwd.getText().toString().trim();

        if(email.isEmpty()){
            edEmail.setError("Email required");
            edEmail.requestFocus();
            return;
        }

        if(pwd.length()<6){
            edPwd.setError("Password should be aleast 6 char long");
            edPwd.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   startProfileActivity();

                }
                else {
                    //if the email,pwd already exist
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        userLogin(email,pwd);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void startProfileActivity(){
        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    private void userLogin(String email, String pwd){
        firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startProfileActivity();

                }
                else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    //if user is already logged in then send to profile activity
    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser()!=null){
           startProfileActivity();

        }
    }
}
