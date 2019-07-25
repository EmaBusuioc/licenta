package com.example.waterplants;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class NotificationHelper extends ContextWrapper {

    public static final String chnnl_id="channel1ID";
    public static final String chnnl_nm="Channel1";


    private NotificationManager mManager;
    private int channelId;

    public NotificationHelper(Context base, int channelId) {
        super(base);
        this.channelId = channelId;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channel1=new NotificationChannel(channelId+"",chnnl_nm, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager(){
        if(mManager==null){
            mManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return mManager;
    };

    public NotificationCompat.Builder getChannelNotification(String title, String msg){
        return new NotificationCompat.Builder(getApplicationContext(),channelId+"")
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.logo2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo2))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setGroup("meu");


    }
}
