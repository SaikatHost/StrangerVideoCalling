package com.example.strangervideocolling;

import android.webkit.JavascriptInterface;

public class InterfaceJava {
    call CallActivity;
    public InterfaceJava(call callActivity){
        this.CallActivity=callActivity;
    }
    @JavascriptInterface
    public void onPeerConnected(){
     CallActivity.onPeerConnected();
    }
}
