package com.reddevx.listenup.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.Favorite;
import com.reddevx.listenup.receivers.NotificationReceiver;
import com.reddevx.listenup.ui.MainActivity;
import com.reddevx.listenup.ui.adapters.FavoriteAdapter;
import com.reddevx.listenup.ui.adapters.RecyclerViewAdapter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.reddevx.listenup.receivers.NotificationReceiver.EMPTY_HEART_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.FULL_HEART_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.NEXT_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.PAUSE_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.PLAY_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.PREV_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.REPEAT_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.REPEAT_OFF_ACTION;
import static com.reddevx.listenup.receivers.NotificationReceiver.SHUFFLE_ACTION;
import static com.reddevx.listenup.ui.PlayerActivity.INTENT_AUDIO_INDEX_KEY;
import static com.reddevx.listenup.ui.PlayerActivity.INTENT_AUDIO_LIST_KEY;

public class AudioService extends Service {
    public static final String CURRENT_AUDIO = "RunningAudio";
    private static final String NOT_CHANNEL_ID = "ListenUp.ChannelID";
    public static final String PLAYING_AUDIO = "currentAudio";
    public static final int NOTIFICATION_ID = 9213;
    private static PlayBackViews playBackListener;
    public IBinder mBinder = new AudioBinder();
    public static MediaPlayer mp;
    public static int audioIndex;
    public static Audio audio;
    private int lastIndex = -1;
    public ArrayList<Audio> audioList;
    private  boolean isShufflingOn = false;
    public static OnStartNewAudio mListener;
    private boolean heart;




    private  NotificationCompat.Action mPlayAction;
    private  NotificationCompat.Action mPauseAction;
    private  NotificationCompat.Action mNextAction;
    private  NotificationCompat.Action mPrevAction;
    private  NotificationCompat.Action mRepeatAction;
    private  NotificationCompat.Action mShuffleAction;
    private  NotificationCompat.Action mRepeatOffAction;
    private  NotificationCompat.Action mFullHeartAction;
    private  NotificationCompat.Action mEmptyHeartAction;
    private String mCurrentAction = REPEAT_OFF_ACTION;
    private final BroadcastReceiver receiver = new NotificationReceiver();
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionCompat.Token token;




    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(getApplicationContext(),"TAG");
        token = mediaSession.getSessionToken();

        notificationManager = NotificationManagerCompat.from(this);
    }



    public class AudioBinder extends Binder {
        public AudioService getAudioSService(){
            return AudioService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        audioIndex = intent.getIntExtra(CURRENT_AUDIO,-1);
        if (!isShufflingOn)
        audioList = intent.getParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY);
        if (mp != null){
            if (audioIndex != lastIndex){
                lastIndex = audioIndex;
                audio = audioList.get(audioIndex);
                playAudio(audio);

            }
        }else {
            lastIndex = audioIndex;
            audio = audioList.get(audioIndex);
            initMediaPlayer();
            playAudio(audio);



        }

        return START_NOT_STICKY;
    }

    public void playAudio(Audio audio){
        mp.reset();
        Uri uri = Uri.fromFile(new File(audio.getUri()));

        try {
            mp.setDataSource(getApplicationContext(), uri);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.start();
                    startForeground(NOTIFICATION_ID,getNotification());
                    if (mListener != null)
                        mListener.onAudioPrepared(audio, mp.getDuration());



                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void initMediaPlayer() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        mp = new MediaPlayer();
        mp.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build());
    }


    public Audio startNextAudio(){
            audioIndex--;
            if (audioIndex <= -1)
                audioIndex = audioList.size() - 1;
            audio = audioList.get(audioIndex);
            playAudio(audio);
            lastIndex = audioIndex;
            return audio;
    }

    public void startPreviousAudio(){
            audioIndex++;
            if (audioIndex >= audioList.size())
                audioIndex = 0;
            audio = audioList.get(audioIndex);
            playAudio(audio);
            lastIndex = audioIndex;
    }




    public static void play() {
        if (!mp.isPlaying()) {
            mp.start();
            if (playBackListener != null)
            playBackListener.onPlayAudio();
            if (RecyclerViewAdapter.animationView != null)
            RecyclerViewAdapter.animationView.resumeAnimation();
            if (FavoriteAdapter.animationView != null)
                FavoriteAdapter.animationView.resumeAnimation();
        }

    }

    public static void pause(){
        if (mp.isPlaying()) {
            mp.pause();
            if (playBackListener != null)
            playBackListener.onPauseAudio();
            if (RecyclerViewAdapter.animationView != null)
                RecyclerViewAdapter.animationView.pauseAnimation();
            if (FavoriteAdapter.animationView != null)
                FavoriteAdapter.animationView.pauseAnimation();
        }
    }

    public interface OnStartNewAudio {
        void onAudioPrepared(Audio audio, int duration);
    }

    public interface PlayBackViews {
        void onPlayAudio();
        void onPauseAudio();
    }

    public static void setPlaybackViewListener(PlayBackViews listener){
        playBackListener = listener;
    }




    public static void setOnStartNewAudio(OnStartNewAudio listener){
        mListener = listener;
    }

    public void setOnCompletion(MediaPlayer.OnCompletionListener listener){
        mp.setOnCompletionListener(listener);
    }






    public Notification getNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(NOT_CHANNEL_ID,"AudioNOT", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Audio channel1");
             getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }


        Intent serviceIntent = new Intent(this, MainActivity.class);
        serviceIntent.putExtra(INTENT_AUDIO_INDEX_KEY, audioIndex);
        serviceIntent.putParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY,audioList);

        PendingIntent mPlayIntent = PendingIntent.getBroadcast(this,0,new Intent(PLAY_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mPauseIntent = PendingIntent.getBroadcast(this,0,new Intent(PAUSE_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mNextIntent = PendingIntent.getBroadcast(this,0,new Intent(NEXT_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mPrevIntent = PendingIntent.getBroadcast(this,0,new Intent(PREV_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mRepeatIntent = PendingIntent.getBroadcast(this,0,new Intent(REPEAT_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mShuffleIntent = PendingIntent.getBroadcast(this,0,new Intent(SHUFFLE_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mRepeatOffIntent = PendingIntent.getBroadcast(this,0,new Intent(REPEAT_OFF_ACTION),PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mFullHeartIntent = PendingIntent.getBroadcast(this,0,new Intent(FULL_HEART_ACTION).putExtra(PLAYING_AUDIO, (Serializable) audio)
                ,PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent mEmptyHeartIntent = PendingIntent.getBroadcast(this,0,new Intent(EMPTY_HEART_ACTION).putExtra(PLAYING_AUDIO, (Serializable) audio)
                ,PendingIntent.FLAG_CANCEL_CURRENT);

        mPlayAction = new NotificationCompat.Action(R.drawable.play_icon, "play", mPlayIntent);
        mPauseAction = new NotificationCompat.Action(R.drawable.pause_icon, "pause", mPauseIntent);
        mNextAction = new NotificationCompat.Action(R.drawable.next_icon, "next", mNextIntent);
        mPrevAction = new NotificationCompat.Action(R.drawable.previous_icon, "previous", mPrevIntent);
        mRepeatAction = new NotificationCompat.Action(R.drawable.repeat_test,"repeat",mRepeatIntent);
        mShuffleAction = new NotificationCompat.Action(R.drawable.shuffle_test,"shuffle",mShuffleIntent);
        mRepeatOffAction = new NotificationCompat.Action(R.drawable.repeat_off_icon,"repeatOff",mRepeatOffIntent);
        mFullHeartAction = new NotificationCompat.Action(R.drawable.heart,"fullHeart",mFullHeartIntent);
        mEmptyHeartAction = new NotificationCompat.Action(R.drawable.empty_heart,"emptyHeart",mEmptyHeartIntent);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(PLAY_ACTION);
        filter.addAction(PAUSE_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREV_ACTION);
        filter.addAction(REPEAT_ACTION);
        filter.addAction(SHUFFLE_ACTION);
        filter.addAction(REPEAT_OFF_ACTION);
        filter.addAction(FULL_HEART_ACTION);
        filter.addAction(EMPTY_HEART_ACTION);
        this.registerReceiver(receiver,filter);
        builder = new NotificationCompat.Builder(getBaseContext(),NOT_CHANNEL_ID);

        builder.setSmallIcon(R.drawable.audio_icon)
                .setContentTitle(audio.getTitle())
                .setContentIntent(createContentIntent())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.audio_icon))
                .setOngoing(mp.isPlaying())
                .setShowWhen(mp.isPlaying())
                .setUsesChronometer(mp.isPlaying())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(0)
                .addAction(mCurrentAction.equals(SHUFFLE_ACTION) ? mShuffleAction : mRepeatOffAction)
                .addAction(mPrevAction)
                .addAction(mPauseAction)
                .addAction(mNextAction)
                .addAction(heart ? mFullHeartAction : mEmptyHeartAction);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(token))
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        }


        return builder.build();
    }


    public void setRepeatOffAction(){
        builder.clearActions();
        if (mp.isPlaying()){
            builder.addAction(mRepeatOffAction)
                    .addAction(mPrevAction)
                    .addAction(mPauseAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }else {
            builder.addAction(mRepeatOffAction)
                    .addAction(mPrevAction)
                    .addAction(mPlayAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }
        mCurrentAction = REPEAT_OFF_ACTION;
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

    public void setRepeatAction(){
        builder.clearActions();
        if (mp.isPlaying()){
            builder.addAction(mRepeatAction)
                    .addAction(mPrevAction)
                    .addAction(mPauseAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }else {
            builder.addAction(mRepeatAction)
                    .addAction(mPrevAction)
                    .addAction(mPlayAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }
        mCurrentAction = REPEAT_ACTION;
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

    public void setShuffleAction(){
        builder.clearActions();
        if (mp.isPlaying()){
            builder.addAction(mShuffleAction)
                    .addAction(mPrevAction)
                    .addAction(mPauseAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }else {
            builder.addAction(mShuffleAction)
                    .addAction(mPrevAction)
                    .addAction(mPlayAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }
        mCurrentAction = SHUFFLE_ACTION;
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }


    public void setHeartAction(boolean heart){
        this.heart = heart;
        if (builder != null) {
            builder.clearActions();
            if (mp == null)
                return;
            if (mp.isPlaying()) {
                builder.addAction(getActionOf(mCurrentAction))
                        .addAction(mPrevAction)
                        .addAction(mPauseAction)
                        .addAction(mNextAction)
                        .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
            } else {
                builder.addAction(getActionOf(mCurrentAction))
                        .addAction(mPrevAction)
                        .addAction(mPlayAction)
                        .addAction(mNextAction)
                        .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public void updatePlaybacks(){
        builder.clearActions();
        if (mp.isPlaying()){
            builder.addAction(getActionOf(mCurrentAction))
                    .addAction(mPrevAction)
                    .addAction(mPauseAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }else {
            builder.addAction(getActionOf(mCurrentAction))
                    .addAction(mPrevAction)
                    .addAction(mPlayAction)
                    .addAction(mNextAction)
                    .addAction(heart ? mFullHeartAction : mEmptyHeartAction);
        }
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

    private NotificationCompat.Action getActionOf(String actionName){
        switch (actionName){
            case REPEAT_OFF_ACTION:
                return mRepeatOffAction;
            case REPEAT_ACTION:
                return mRepeatAction;
            case SHUFFLE_ACTION:
                return mShuffleAction;
        }
        return mRepeatOffAction;
    }

    private PendingIntent createContentIntent() {
        Intent openUI = new Intent(this, MainActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0, openUI,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void setShufflingOn(ArrayList<Audio> shuffledList){
        String t = audioList.get(audioIndex).getUri();
        Collections.shuffle(audioList);
        isShufflingOn = true;
        for (int i = 0; i < audioList.size(); i++){
            if (t.equals(audioList.get(i).getUri())){
                audioIndex = i;
            }
        }

    }

    public void setShufflingOff(ArrayList<Audio> normalList){
        String temp = audioList.get(audioIndex).getUri();
        audioList.clear();
        audioList.addAll(normalList);
        isShufflingOn = false;
        for (int i = 0; i < audioList.size(); i++){
            if (temp.equals(audioList.get(i).getUri()))
                audioIndex = i;
        }
    }

    private boolean checkIsFavorite(List<Favorite> favorites){
        for (int i = 0; i < favorites.size(); i++){
            if (audio.getUri().equals(favorites.get(i).getFavorite_uri()))
                return true;
        }
        return false;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (mp != null) {
            mp.release();
            mp = null;
            audio = null;
            audioIndex = -1;
        }
        // Stop service
        stopForeground(true);
        stopSelf();
        unregisterReceiver(receiver);

    }
}