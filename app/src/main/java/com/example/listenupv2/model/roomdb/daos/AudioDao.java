package com.example.listenupv2.model.roomdb.daos;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.relations.AudioWithPlaylists;
import com.example.listenupv2.model.entities.relations.PlaylistAudioCrossRef;

import java.util.List;

@Dao
public interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Audio audio);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlaylistAudioCrossRef(PlaylistAudioCrossRef crossRef);

    @Update
    void update(Audio audio);

    // Delete one audio
    @Delete
    void delete(Audio audio);

    // Delete all audios
    @Query("DELETE FROM audio_table")
    void deleteAllAudios();

    // return all audios
    @Query("SELECT * FROM audio_table")
    LiveData<List<Audio>> getAllAudios();

    @Transaction
    @Query("SELECT * FROM audio_table")
    List<AudioWithPlaylists> getAudiosWithPlaylists();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAudioList(List<Audio> audioList);



    @Query("DELETE FROM PLAYLISTAUDIOCROSSREF WHERE audio_id = :audioId AND playlist_id = :playlistId")
    void deleteAudioFromPlaylist(int audioId, int playlistId);









}
