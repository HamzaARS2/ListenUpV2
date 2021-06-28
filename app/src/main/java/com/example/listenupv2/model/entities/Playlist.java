package com.example.listenupv2.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "playlist_table",indices = {@Index(value = {"playlist_name"} , unique = true)})
public class Playlist implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int playlist_id;
    @NonNull
    private String playlist_name;
    private int playlist_total;

    public Playlist(String playlist_name, int playlist_total) {
        this.playlist_name = playlist_name;
        this.playlist_total = playlist_total;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public int getPlaylist_id() {
        return playlist_id;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public int getPlaylist_total() {
        return playlist_total;
    }
}
