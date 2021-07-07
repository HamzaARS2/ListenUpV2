package com.example.listenupv2.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.listenupv2.model.entities.Audio;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class AudioPlayer {
    public  static MediaPlayer mp;
    private Context context;
    public static Audio audio;
    public AudioPlayer(Context context, Audio audio){
        this.context = context;
        AudioPlayer.audio = audio;
        if (mp == null) {
            mp = MediaPlayer.create(context, Uri.fromFile(new File(audio.getUri())));
            mp.seekTo(0);
        }
    }

    public static void play(){
        mp.start();
    }


    public static void pause(){
        mp.pause();
    }


    public static void seekTo(int progress){
        mp.seekTo(progress);
    }

    public static void stopAudio(){
        mp.release();
        mp = null;
    }

    public static void setLooping(boolean  bool){
        mp.setLooping(bool);
    }

    public static boolean isLooping(){
        return mp.isLooping();
    }

    public static void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mp.setOnCompletionListener(listener);
    }

    public static int getCurrentPosition(){
        return mp.getCurrentPosition();
    }

    public static int getAudioDuration() {
        return mp.getDuration();
    }

    public static String getConvertedAudioDuration(){
        return convertTime(mp.getDuration());
    }

    public static boolean isPlaying(){
        return mp.isPlaying();
    }

    @SuppressLint("DefaultLocale")
    public static String convertTime(int milliseconds) {
        String convertedTime =  String.format("%02d:%02d"
                ,TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                ,TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return convertedTime;
    }
}
