package com.example.listenupv2.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.example.listenupv2.model.entities.Audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.listenupv2.ui.PlayerActivity.INTENT_AUDIO_LIST_KEY;

public class AudioService extends Service  {
    public static final String PLAY_ACTION = "listenUp.PLAY";
    public static final String CURRENT_AUDIO = "CRNT_AUDIO";
    IBinder mBinder = new AudioBinder();
    public static MediaPlayer mp;
    public static int audioIndex;
    public static Audio audio;
    private int lastIndex=-1;
    private ArrayList<Audio> audioList;
    public OnStartNewAudio listener;


    @Override
    public void onCreate() {
        super.onCreate();

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
        return START_STICKY;
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
                    if (listener != null)
                        listener.onAudioPrepared(audio,mp.getDuration());
                    mp.start();
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


    public void startNextAudio(){
        audioIndex--;
        if (audioIndex <= -1)
            audioIndex = audioList.size()-1;
        audio = audioList.get(audioIndex);
        playAudio(audio);
        lastIndex = audioIndex;
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
        }

    }

    public static void pause(){
        if (mp.isPlaying())
            mp.pause();
    }

    public interface OnStartNewAudio {
        void onAudioPrepared(Audio audio, int duration);
    }


    public void setOnStartNewAudio(OnStartNewAudio listener){
        this.listener = listener;
    }

    public void setOnCompletion(MediaPlayer.OnCompletionListener listener){
        mp.setOnCompletionListener(listener);
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (mp != null) {
            mp.release();
            mp = null;
        }
        // Stop service
        stopSelf();
    }
}