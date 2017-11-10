package com.ajc.project.ajc;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by pierre on 2017-11-10.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Api api = new Api("user/update");
        api.addData("user_id", 1);
        api.addData("firebase_id", token);
        api.call();
    }
}