package com.android.priyanka.pushnotificationandroid;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


//for incoming messaging
public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification()!=null){

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            System.out.println("title.."+title);
            System.out.println("body.."+body);

            NotificationHelper.displayNotification(getApplicationContext(),title,body);

        }
    }
}
