package com.example.listenupv2.model.roomdb.daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.relations.AudioWithPlaylists;

import java.util.List;

@Dao
public interface AudioDao {

    @Insert
    void insert(Audio audio);

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

    @Query("SELECT * FROM audio_table WHERE uri = :audio_uri ")
    LiveData<Audio> getAudio(String audio_uri);





}
