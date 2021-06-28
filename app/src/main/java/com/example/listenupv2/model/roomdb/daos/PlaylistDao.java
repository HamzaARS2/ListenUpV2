package com.example.listenupv2.model.roomdb.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.entities.relations.PlaylistWithAudios;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    // Delete one playlist
    @Delete
    void delete(Playlist playlist);

    // Delete all playlists
    @Query("DELETE FROM playlist_table")
    void deleteAllPlaylists();

    // return all playlists
    @Query("SELECT * FROM playlist_table")
    LiveData<List<Playlist>> getAllPlaylists();

    @Transaction
    @Query("SELECT * FROM playlist_table")
    LiveData<List<PlaylistWithAudios>> getPlaylistsWithAudios();

//    @Transaction
//    @Query("SELECT * FROM playlist_table WHERE playlist_id = :id")
//    PlaylistWithAudios getOnePlaylistWithAudios(int id);
}
