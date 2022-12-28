package com.example.strangervideocolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.strangervideocolling.databinding.ActivityMainBinding;
import com.example.strangervideocolling.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_welcome extends AppCompatActivity {
ActivityWelcomeBinding binding;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            gotoNextPage();
        }
        binding.getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNextPage();
            }
        });
    }
    void gotoNextPage(){
        startActivity(new Intent(Activity_welcome.this,login.class));
        finish();
    }
}