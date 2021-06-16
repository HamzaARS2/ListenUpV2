package com.example.listenupv2.model.entities.relations;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"playlist_id","audio_id"})
public class PlaylistAudioCrossRef {

    public int playlist_id;
    @ColumnInfo(index = true)
    public int audio_id;
}
