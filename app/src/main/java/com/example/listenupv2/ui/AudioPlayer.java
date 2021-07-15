package com.example.listenupv2.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.listenupv2.model.entities.Audio;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public class AudioPlayer implements Serializable {
    public  MediaPlayer mp;
    private Context context;
    public  Audio audio;
    public AudioPlayer(Context context, Audio audio){
        this.context = context;
        this.audio = audio;
        if (mp == null) {
            mp = MediaPlayer.create(context, Uri.fromFile(new File(audio.getUri())));
            mp.seekTo(0);
        }
    }

    public  void play(){
        mp.start();
    }


    public  void pause(){
        mp.pause();
    }


    public  void seekTo(int progress){
        mp.seekTo(progress);
    }

    public  void stopAudio(){
        mp.release();
        mp = null;
    }

    public  void setLooping(boolean  bool){
        mp.setLooping(bool);
    }

    public  boolean isLooping(){
        return mp.isLooping();
    }

    public  void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        mp.setOnCompletionListener(listener);
    }

    public  int getCurrentPosition(){
        return mp.getCurrentPosition();
    }

    public  int getAudioDuration() {
        return mp.getDuration();
    }

    public  String getConvertedAudioDuration(){
        return convertTime(mp.getDuration());
    }



    @SuppressLint("DefaultLocale")
    public  String convertTime(int milliseconds) {
        String convertedTime =  String.format("%02d:%02d"
                ,TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                ,TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return convertedTime;
    }
}
