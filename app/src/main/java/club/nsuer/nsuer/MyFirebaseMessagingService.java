package club.nsuer.nsuer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final String TAG = "Firebase";

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //showNotification(remoteMessage.getNotification().getBody());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {


            Context context = getApplicationContext();

            // SqLite database handler
            SQLiteHandler db = new SQLiteHandler(context);

            HashMap<String, String> user = db.getUserDetails();

            String memberID = user.get("memberID");

            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String senderMemID = remoteMessage.getData().get("senderMemID");


            String type = remoteMessage.getData().get("type");
            String typeExtra = remoteMessage.getData().get("typeExtra");

            // if(!senderMemID.equals(memberID))

            sendNotification(body,title,type,typeExtra,"MainActivity");




        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }







    private void sendNotification(String messageBody, String messageTitle, String type, String typeExtra, String actvityName) {


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "NSUER_NOTIFICATION";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Get important notifications from NSUer App");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 250});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }




        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("typeExtra", typeExtra);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);



        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_status_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, notificationBuilder.build());
    }






    private void showNotification(String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "NSUER_NOTIFICATION";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Get important notifications from NSUer App");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_status_icon)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("NSUer App")
                .setContentText(messageBody);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }


}
