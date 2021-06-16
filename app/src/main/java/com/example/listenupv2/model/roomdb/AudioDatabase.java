package com.example.listenupv2.model.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.entities.relations.PlaylistAudioCrossRef;
import com.example.listenupv2.model.roomdb.daos.AudioDao;
import com.example.listenupv2.model.roomdb.daos.FavoriteDao;
import com.example.listenupv2.model.roomdb.daos.PlaylistDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Audio.class, Favorite.class, Playlist.class, PlaylistAudioCrossRef.class}, version = 1,exportSchema = false)
public abstract class AudioDatabase extends RoomDatabase {
    public abstract AudioDao audioDao();
    public abstract FavoriteDao favoriteDao();
    public abstract PlaylistDao playlistDao();
    private static volatile AudioDatabase instance;
    private static final int NUMBER_OF_THREADS = 6;

    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static AudioDatabase getInstance(Context context){
        if (instance == null){
            synchronized (AudioDatabase.class){
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                             AudioDatabase.class,"audio.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

}

