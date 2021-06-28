package com.example.listenupv2.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class AudioPlayer {
    private MediaPlayer mp;
    private Context context;
    private MediaPlayer.OnCompletionListener listener;
    public AudioPlayer(Context context, Uri uri){
        this.context = context;
        if (mp == null) {
            mp = MediaPlayer.create(context, uri);
            mp.seekTo(0);
        }
    }

    public void play(){
        mp.start();
    }

    public void pause(){
        mp.pause();
    }

    public void stop(){
        mp.release();
        Toast.makeText(context, "released", Toast.LENGTH_SHORT).show();
    }


    public void seekTo(int progress){
        mp.seekTo(progress);
    }

    public void stopAudio(){
        mp.release();
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        this.listener = listener;
        mp.setOnCompletionListener(listener);
    }

    public int getCurrentPosition(){
        return mp.getCurrentPosition();
    }

    public int getAudioDuration() {
        return mp.getDuration();
    }

    public String getConvertedAudioDuration(){
        return convertTime(mp.getDuration());
    }

    @SuppressLint("DefaultLocale")
    public String convertTime(int milliseconds) {
        String convertedTime =  String.format("%02d:%02d"
                ,TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                ,TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return convertedTime;
    }
}
