package com.example.listenupv2.model.roomdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.entities.relations.PlaylistAudioCrossRef;
import com.example.listenupv2.model.roomdb.daos.AudioDao;
import com.example.listenupv2.model.roomdb.daos.FavoriteDao;
import com.example.listenupv2.model.roomdb.daos.PlaylistDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Audio.class, Favorite.class, Playlist.class, PlaylistAudioCrossRef.class}, version = 11,exportSchema = false)
public abstract class AudioDatabase extends RoomDatabase {
    public abstract AudioDao audioDao();
    public abstract FavoriteDao favoriteDao();
    public abstract PlaylistDao playlistDao();
    private static volatile AudioDatabase instance;
    private static final int NUMBER_OF_THREADS = 12;

    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static AudioDatabase getInstance(Context context){
        if (instance == null){
            synchronized (AudioDatabase.class){
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                             AudioDatabase.class,"ListenUp.db")
                            .fallbackToDestructiveMigration()
                            //.addCallback(roomCallback)
                            .build();

                }
            }
        }
        return instance;
    }

//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//           PopulateDb(instance);
//        }
//
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            //PopulateDb(instance);
//        }
//    };
//
//    private static void PopulateDb(AudioDatabase instance){
//
//        AudioDao audioDao = instance.audioDao();
//        AudioDatabase.databaseExecutor.execute(()->{
//            audioDao.insert(new Audio("test1","uri1"));
//            audioDao.insert(new Audio("test2","uri2"));
//            audioDao.insert(new Audio("test3","uri3"));
//                });
//
//    }




}

