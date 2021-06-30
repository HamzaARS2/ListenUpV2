package com.example.listenupv2.model.entities;

import androidx.annotation.NonNull;
import androidx.core.text.util.LinkifyCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "audio_table", indices = {@Index(value = {"title","uri"},unique = true)})
public class Audio implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int audio_id;
    @NonNull
    private String title;
    @NonNull
    private String uri;

    public Audio(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public int getAudio_id() {
        return audio_id;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public static Audio parse(Favorite favorite){
        Audio audio = new Audio(favorite.getFavorite_title(),favorite.getFavorite_uri());
        audio.setAudio_id(favorite.getFavorite_id());
        return audio;
    }
}
