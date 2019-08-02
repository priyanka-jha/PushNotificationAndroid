package com.android.priyanka.pushnotificationandroid;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper {

    public static void displayNotification(Context context,String title,String body) {

        //to handle click on notification
        Intent intent = new Intent(context,ProfileActivity.class);
        //Flag PendingIntent.FLAG_CANCEL_CURRENT will cancel the current pending intent if it is already created before creating new pending intent
       //PEnding intent is used to handle click on notification(i.e to execute any code outside our application)
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        //to get notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID).
                setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)   // to handle click on notification
                .setAutoCancel(true)    //when clicked on notification,the notification will be cancelled
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());
    }
}
