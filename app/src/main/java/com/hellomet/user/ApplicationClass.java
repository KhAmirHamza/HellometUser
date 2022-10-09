package com.hellomet.user;

import android.app.Application;
import android.widget.Toast;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import static com.hellomet.user.Constants.ONE_SIGNAL_APP_ID;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONE_SIGNAL_APP_ID);
//        OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
//            @Override
//            public void notificationWillShowInForeground(OSNotificationReceivedEvent notificationReceivedEvent) {
//                // Get custom additional data you sent with the notification
//                JSONObject data = notificationReceivedEvent.getNotification().getAdditionalData();
//
//                if (data!=null) {
//                    // Complete with a notification means it will show
//                    notificationReceivedEvent.complete(notificationReceivedEvent.getNotification());
//                }
//                else {
//                    // Complete with null means don't show a notification.
//                    notificationReceivedEvent.complete(notificationReceivedEvent.getNotification());
//                }
//            }
//        });


//
//        OneSignal.setNotificationOpenedHandler(
//                new OneSignal.OSNotificationOpenedHandler() {
//                    @Override
//                    public void notificationOpened(OSNotificationOpenedResult result) {
//                        String actionId = result.getAction().getActionId();
//                        String type = result.getAction().getType().toString(); // "ActionTaken" | "Opened"
//
//                        String title = result.getNotification().getTitle();
//                        Toast.makeText(ApplicationClass.this, title, Toast.LENGTH_SHORT).show();
//                    }
//                });

    }
}
