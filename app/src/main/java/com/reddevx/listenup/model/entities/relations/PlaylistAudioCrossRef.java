package com.reddevx.listenup.model.entities.relations;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = {"playlist_id","audio_id"})
public class PlaylistAudioCrossRef {

    public long playlist_id;
    @ColumnInfo(index = true)
    public int audio_id;

    public PlaylistAudioCrossRef(int audio_id) {
        this.audio_id = audio_id;
    }

    @Ignore
    public PlaylistAudioCrossRef(int playlist_id, int audio_id) {
        this.playlist_id = playlist_id;
        this.audio_id = audio_id;
    }

    public void setPlaylist_id(long playlist_id) {
        this.playlist_id = playlist_id;
    }

    public long getPlaylist_id() {
        return playlist_id;
    }

    public int getAudio_id() {
        return audio_id;
    }
}
