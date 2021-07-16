package com.example.listenupv2.model.roomdb.repositories;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;

import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.entities.relations.PlaylistWithAudios;
import com.example.listenupv2.model.roomdb.AudioDatabase;
import com.example.listenupv2.model.roomdb.daos.PlaylistDao;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PlaylistRepository {
    private PlaylistDao playlistDao;
    private LiveData<List<Playlist>> allPlaylists;
    private long lastInsertedRow ;



    public PlaylistRepository(Application application){
        AudioDatabase database = AudioDatabase.getInstance(application);
        this.playlistDao = database.playlistDao();
        this.allPlaylists = this.playlistDao.getAllPlaylists();
    }



    public long insert(Playlist playlist){
        Callable<Long> insertCallable = () -> playlistDao.insert(playlist);
        long rowId = 0;

        Future<Long> future = AudioDatabase.databaseExecutor.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        return rowId;
    }

    public void update(Playlist playlist){
        AudioDatabase.databaseExecutor.execute(() -> {
            playlistDao.update(playlist);
        });
    }

    public void delete(Playlist playlist){
        AudioDatabase.databaseExecutor.execute(() -> {
            playlistDao.delete(playlist);
        });
    }

    public void deleteAllPlaylists(){
        AudioDatabase.databaseExecutor.execute(() -> {
            playlistDao.deleteAllPlaylists();
        });
    }

    public LiveData<List<PlaylistWithAudios>> getPlaylistsWithAudios(){
        return playlistDao.getPlaylistsWithAudios();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return allPlaylists;
    }
}
