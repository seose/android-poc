package seoft.co.kr.mp3test;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

public class AudioApplication extends Application {
    private static AudioApplication mInstance;
    private AudioServiceInterface mInterface;

    @Override
    public void onCreate() {
        super.onCreate();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(SC.channelId, "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        mInstance = this;
        mInterface = new AudioServiceInterface(getApplicationContext());
    }

    public static AudioApplication getInstance() {
        return mInstance;
    }

    public AudioServiceInterface getServiceInterface() {
        return mInterface;
    }
}