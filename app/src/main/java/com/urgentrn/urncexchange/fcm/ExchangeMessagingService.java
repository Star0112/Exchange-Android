package com.urgentrn.urncexchange.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.ui.MainActivity_;
import com.urgentrn.urncexchange.ui.SplashActivity_;

public class ExchangeMessagingService extends FirebaseMessagingService {

    private static final String TAG = "ExchMessagingService";
    public static final String DEFAULT_TOPIC = "wallet-notification-topic";
    private static final int NOTIFICATION_ID = 1;

    private String title, message;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        ExchangeApplication.getApp().getPreferences().setPushToken(null);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        title = "Exchange Notification";
        message = null;
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // TODO
            for (String key : remoteMessage.getData().keySet()) {
                message = remoteMessage.getData().get(key);
                break;
            }
        }
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message notification body: " + message);
        }
        if (ExchangeApplication.getApp().activityReferences > 0) { // app is active
            new Handler(getMainLooper()).post(() -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            });
        } else { // app is inactive
            sendUserNotification();
        }
    }

    private void sendUserNotification() {
        final Intent intent = new Intent(this, ExchangeApplication.getApp().getUser() == null ? SplashActivity_.class : MainActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final String CHANNEL_ID = getPackageName();
        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH).setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_VIBRATE);
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Exchange Notification", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
