package com.android.priyanka.pushnotificationandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendNotificationActivity extends AppCompatActivity {

    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.edTitle)
    EditText edTitle;
    @BindView(R.id.edBody)
    EditText edBody;
    @BindView(R.id.btnSend)
    Button btnSend;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        ButterKnife.bind(this);

        user = (User) getIntent().getSerializableExtra("user");
        tvUser.setText("Sending notification to:  "+user.email);
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        sendNotification(user);
    }

    private void sendNotification(User user) {
        String title = edTitle.getText().toString().trim();
        String body = edBody.getText().toString().trim();

        if(title.isEmpty()){
            edTitle.setError("Title required");
            edTitle.requestFocus();
            return;
        }

        if(body.isEmpty()){
            edBody.setError("Body required");
            edBody.requestFocus();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https:androidnotificationtutorial.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface = retrofit.create(APIInterface.class);

        Call<ResponseBody> call = apiInterface.sendNotification(user.token,title,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                   // Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
                    System.out.println("response.."+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
}
