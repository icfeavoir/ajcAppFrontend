package com.ajc.project.ajc;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by pierre on 2017-11-10.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    private int MY_ID;

    @Override
    public void onTokenRefresh() {
        SharedPreferences prefs = this.getSharedPreferences("AJC_VAR", MODE_PRIVATE);
        this.MY_ID = prefs.getInt("MY_ID", 0);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Api api = new Api("user/update");
        api.addData("user_id", this.MY_ID);
        api.addData("firebase_id", token);
        api.call();
    }
}