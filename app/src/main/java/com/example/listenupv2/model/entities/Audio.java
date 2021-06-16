package com.example.listenupv2.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "audio_table")
public class Audio {

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
}
