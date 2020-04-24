package com.example.tryhome;

import android.util.Log;

<<<<<<< Updated upstream
=======
import com.google.firebase.messaging;
import com.google.firebase.messaging.FirebaseMessagingService;
>>>>>>> Stashed changes
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIDSer {

    @Override
    public void OnTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase_refresh", "creating an instance");
    }
}
