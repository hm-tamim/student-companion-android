package club.nsuer.nsuer;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {


    String TAG = "FCMid";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("FCMtoken",s);
    }

}
