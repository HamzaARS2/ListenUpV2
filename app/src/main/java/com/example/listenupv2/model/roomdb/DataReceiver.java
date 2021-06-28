package com.example.listenupv2.model.roomdb;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.listenupv2.model.entities.Audio;

import java.util.ArrayList;

public class DataReceiver {

    private Context context;
    public DataReceiver(Context context){
        this.context = context;
    }


    @SuppressLint("Recycle")
    public ArrayList<Audio> getAvailableAudioFiles(){
        ArrayList<Audio> audioList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor audioCursor = null;
        audioCursor = resolver.query(audioUri,null,null,null,null);
        while (audioCursor != null && audioCursor.moveToNext()){
            String title = audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//            String artist = audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//            String duration = audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String url = audioCursor.getString(audioCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Audio audio = new Audio(title,url);
            audioList.add(audio);
        }
        return audioList;
    }

}
