package com.example.listenupv2.model.roomdb.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.listenupv2.model.entities.Favorite;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Favorite favoriteAudio);

    @Update
    void update(Favorite FavoriteAudio);

    // Delete one favorite audio
    @Delete
    void delete(Favorite FavoriteAudio);

    // Delete all favorite audios
    @Query("DELETE FROM favorite_table")
    void deleteAllFavoriteAudios();

    // return all audios
    @Query("SELECT * FROM favorite_table")
    LiveData<List<Favorite>> getAllFavoriteAudios();
}
