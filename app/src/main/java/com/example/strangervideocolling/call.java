package com.example.strangervideocolling;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.strangervideocolling.databinding.ActivityCallBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class call extends AppCompatActivity {
 ActivityCallBinding binding;
 String uniqueId="";
 FirebaseAuth auth;

    String useername="";
    String friendsUsename="";
    String createdBy;
    String incoming;
    boolean isPeerConnected=false;
    boolean isAudio= true;
    boolean isVideo= true;
    boolean pageExit=false;
    DatabaseReference firbaseRef;
    FirebaseDatabase database;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//Saikat Monda1

      useername=getIntent().getStringExtra("userName");
      incoming=getIntent().getStringExtra("incoming");
      createdBy=getIntent().getStringExtra("createdBy");


      uniqueId=getUniqueId();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        firbaseRef= FirebaseDatabase.getInstance().getReference().child("users");

        friendsUsename="";




      setupWebView();

        //Mick Btn

    binding.mickBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isAudio=!isAudio;
            callJavaScriptFunction("javascript:toggleAudio(\""+isAudio+"\")");//toggleVideo
          if(isAudio){
              binding.mickBtn.setImageResource(R.drawable.btn_unmute_normal);
          }else {
              binding.mickBtn.setImageResource(R.drawable.btn_mute_normal);
          }
        }
    });


    //Video Btn

      binding.videoBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              isVideo=!isVideo;
              callJavaScriptFunction("javascript:toggleVideo(\""+isVideo+"\")");
              if(isAudio){
                  binding.videoBtn.setImageResource(R.drawable.btn_video_normal);
              }else {
                  binding.videoBtn.setImageResource(R.drawable.btn_video_muted);
              }
          }
      });

binding.endCall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        callJavaScriptFunction("javascript:endCall()");
        finish();
    }
});

    }
void setupWebView(){
        binding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(PermissionRequest request) {
               if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){ request.grant(request.getResources());}

            }
        });

     binding.webView.getSettings().setJavaScriptEnabled(true);
     binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
     binding.webView.addJavascriptInterface(new InterfaceJava(this),"Android");
     loadVideoCall();
    }
    public  void  loadVideoCall(){
    String filePath="file:android_asset/call.html";
    binding.webView.loadUrl(filePath);

   binding.webView.setWebViewClient(new WebViewClient(){
       @Override
       public void onPageFinished(WebView view, String url) {
           super.onPageFinished(view, url);
           insitializerPeer();
       }
   });
    }

    void insitializerPeer(){
        uniqueId=getUniqueId();
        callJavaScriptFunction("javascript:init(\""+uniqueId+"\")");
        if(createdBy.equalsIgnoreCase(useername)){
             if(pageExit)
                return;
             firbaseRef.child(useername).child("connId").setValue(uniqueId);
             firbaseRef.child(useername).child("isAvailable").setValue(true);


            binding.controls.setVisibility(View.VISIBLE);
            Log.e("err","Not  available");
            friendsUsename=incoming;
           //
            FirebaseDatabase.getInstance().getReference()
                    .child("profiles")
                    .child(incoming)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user users =snapshot.getValue(user.class);
                            Glide.with(call.this).load(users.getProfile())
                                  .into(binding.frindProfile);
                            binding.frindName.setText(users.getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            sendCallRequest();
        }else {
           /* database.getReference()
                    .child("users")
                    .child(useername)
                    .child("connId")
                    .setValue(uniqueId);
            database.getReference()
                    .child("users")
                    .child(useername)
                    .child("isAvailable")
                    .setValue(true);*/

            //firbaseRef.child("profiles").child(useername).child("isAvailable").setValue(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    friendsUsename=createdBy;
                    firbaseRef.child(friendsUsename).child("connId").setValue(uniqueId);
                    //Toast.makeText(call.this, "Shreya 1", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("profiles")
                            .child(createdBy)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    user Users =snapshot.getValue(user.class);
                                    Glide.with(call.this).load(Users.getProfile())
                                            .into(binding.frindProfile);
                                    binding.frindName.setText(Users.getName());

                                    //if(snapshot.getValue()!=null){
                                      //  sendCallRequest();
                                  //  }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(friendsUsename)
                            .child("connId")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.getValue()!=null){
                                      sendCallRequest();
                                      }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                }
            },3000);
        }
    }
  public void  onPeerConnected(){
      isPeerConnected=true;
  }
    void sendCallRequest(){
        Toast.makeText(call.this, "Shreya 1", Toast.LENGTH_SHORT).show();
      if(!isPeerConnected){
          Toast.makeText(this, "You are not connected to your internet", Toast.LENGTH_SHORT).show();
           return;}
   listenConnId();
  }
  void  listenConnId(){
    firbaseRef.child(friendsUsename).child("connId")
              .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()==null)
                    return;

                binding.controls.setVisibility(View.VISIBLE);
                String connId=snapshot.getValue(String.class);
                    //database.getReference().child("users").child(useername).child("connId").setValue(uniqueId);
                    //database.getReference().child("users").child(useername).child("isAvailable").setValue(true);
                    //firbaseRef.child(useername).child("isAvailable").setValue(true);

                    callJavaScriptFunction("javascript:startCall(\""+connId+"\")");
                    callJavaScriptFunction("javascript:toggleAudio(\""+isAudio+"\")");//toggleVideo
                    callJavaScriptFunction("javascript:toggleVideo(\""+isVideo+"\")");
                    Toast.makeText(call.this, "Shreya i", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
  }
    void callJavaScriptFunction(String functio){
        binding.webView.post(new Runnable() {
            @Override
            public void run() {
                binding.webView.evaluateJavascript(functio,null);
            }
        });
    }

    String getUniqueId(){
        return UUID.randomUUID().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageExit=true;
        firbaseRef.child(createdBy).setValue(null);
        callJavaScriptFunction("javascript:endCall()");
        finish();
    }
}