package com.example.listenupv2.model.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Playlist;

import java.util.List;

public class AudioWithPlaylists {
    @Embedded public Audio audio;
    @Relation(
            parentColumn = "audio_id",
            entityColumn = "playlist_id",
            associateBy = @Junction(PlaylistAudioCrossRef.class)
    )
    public List<Playlist> playlists;
}
