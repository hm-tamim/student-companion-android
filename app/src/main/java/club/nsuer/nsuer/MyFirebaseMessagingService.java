package club.nsuer.nsuer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final String TAG = "Firebase";

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //showNotification(remoteMessage.getNotification().getBody());

        Context context = getApplicationContext();


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            SessionManager user = new SessionManager(context);

            String memberID = user.getMemberID();

            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String senderMemID = remoteMessage.getData().get("senderMemID");


            String type = remoteMessage.getData().get("type");
            String typeExtra = remoteMessage.getData().get("typeExtra");

            String typeExtra2 = remoteMessage.getData().get("typeExtra2");


            if (type.equals("message")) {

                String notifyMessageZ = remoteMessage.getData().get("typeExtra3");

                int id = Integer.parseInt(typeExtra2);
                String from = senderMemID;
                String to = memberID;
                String message = body;
                long time = Long.parseLong(remoteMessage.getData().get("typeExtra4"));

                ChatEntity chatItem = new ChatEntity();

                chatItem.setMsg_id(id);
                chatItem.setUser_from(from);
                chatItem.setUser_to(to);
                chatItem.setMessage(message);
                chatItem.setTime(time);

                String dbName = "chat_" + from;

                ChatDatabase chatDb = Room.databaseBuilder(getApplicationContext(),
                        ChatDatabase.class, dbName).allowMainThreadQueries().build();

                chatDb.chatDao().insertAll(chatItem);


            } else {

                if (!senderMemID.equals(memberID)) {
                    sendNotification(body, title, senderMemID, type, typeExtra, "MainActivity");
                    saveNotification(title, body, senderMemID, type, typeExtra, typeExtra2);
                }

            }


        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


    public void saveNotification(String title, String msg, String senderMemID, String type, String typeExtra, String typeExtra2) {

        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(title);
        entity.setBody(msg);
        entity.setType(type);
        entity.setTypeExtra(typeExtra);
        entity.setTypeExtra2(typeExtra2);
        entity.setSenderMemID(senderMemID);
        long time = Calendar.getInstance().getTimeInMillis() / 1000L;

        entity.setTime(time);
        entity.setSeen(false);

        NotificationDatabase db = Room.databaseBuilder(getApplicationContext(),
                NotificationDatabase.class, "notifications").allowMainThreadQueries().build();

        db.notificationDao().insertAll(entity);


    }


    private void sendNotification(String messageBody, String messageTitle, String senderMemID, String type, String typeExtra, String actvityName) {


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "NSUER_NOTIFICATION";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Get important notifications from Student Companion App");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 250});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("senderMemID", senderMemID);
        intent.putExtra("type", type);
        intent.putExtra("typeExtra", typeExtra);
        intent.putExtra("fromService", "true");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
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


}
