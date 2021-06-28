package com.example.listenupv2.model.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Playlist;

import java.io.Serializable;
import java.util.List;

public class PlaylistWithAudios implements Serializable {
    @Embedded private Playlist playlist;
    @Relation(
            parentColumn = "playlist_id",
            entityColumn = "audio_id",
            associateBy = @Junction(PlaylistAudioCrossRef.class)
    )
    private List<Audio> audios;

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setAudios(List<Audio> audios) {
        this.audios = audios;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public List<Audio> getAudios() {
        return audios;
    }
}
