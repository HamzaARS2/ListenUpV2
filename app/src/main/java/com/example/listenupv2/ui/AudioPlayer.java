package com.example.listenupv2.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;


public class AudioPlayer {
    private MediaPlayer mp;
    private Context context;

    public AudioPlayer(Context context, Uri uri){
        this.context = context;
        if (mp == null)
            mp = MediaPlayer.create(context, uri);
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

    public void stopAudio(){
        mp.release();
    }
}
