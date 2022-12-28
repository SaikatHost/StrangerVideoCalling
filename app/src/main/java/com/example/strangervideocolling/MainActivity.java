package com.example.strangervideocolling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.strangervideocolling.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
FirebaseAuth auth;
FirebaseUser currentUser;
FirebaseDatabase database;
long coin;
user getObject;
String [] permissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
private  int requestCode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();

        database=FirebaseDatabase.getInstance();

        database.getReference().child("profiles")
                                .child(currentUser.getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                      getObject=snapshot.getValue(user.class);
                                       coin=getObject.getCoin();
                                      binding.coin.setText("You have: "+coin);
                                        Glide.with(MainActivity.this)
                                                .load(getObject.getProfile())
                                                .into(binding.profileImage);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
        binding.findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()) {
                    if (coin > 5) {
                      Intent intent=new Intent(MainActivity.this,connectingActivity.class);
                      intent.putExtra("profile",getObject.getProfile());
                      intent.putExtra("user_name",getObject.getName());
                      startActivity(intent);

                        //  startActivity(new Intent(MainActivity.this, connectingActivity.class));
                        Toast.makeText(MainActivity.this, "Call finding......", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Insufficient coins", Toast.LENGTH_SHORT).show();
                    }
                }else {  askPermission();  }
            }
        });

    }
    void  askPermission(){
        ActivityCompat.requestPermissions(this,permissions,requestCode);
    }

    private boolean isPermissionGranted() {
          for(String permission:permissions){
              if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                  return false;
              }
          }
    return true;
    }
}