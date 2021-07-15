package com.example.listenupv2.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.listenupv2.R;
import com.example.listenupv2.ui.AudioPlayer;

public class AudioService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "CHNL_ID";
    private AudioPlayer player;
    public AudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        player = (AudioPlayer) intent.getSerializableExtra("audio");
//        startForeground(-1,mGetNotification());
//        if (!player.isPlaying()){
//            player.play();
//            Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
//        }
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (player.isPlaying()) {
//            player.stopAudio();
//            Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private Notification mGetNotification(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"testNoti", NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription("testChannel");
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(),NOTIFICATION_CHANNEL_ID);
//        builder.setSmallIcon(R.drawable.audio_icon)
//                .setContentTitle("testing")
//                .setContentText("textTesting")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
//                .addAction(R.drawable.ic_play_arrow_24,"play",null)
//                .addAction(R.drawable.ic_pause_24,"pause",null)
//                .addAction(R.drawable.ic_skip_next_24,"next",null)
//                .addAction(R.drawable.ic_skip_previous_24,"prev",null);
//
//                return builder.build();
//    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//        if (player.isPlaying()){
//            player.stopAudio();
//        }
//    }
}