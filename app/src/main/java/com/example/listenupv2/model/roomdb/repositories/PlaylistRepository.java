package com.example.listenupv2.model.roomdb.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.roomdb.AudioDatabase;
import com.example.listenupv2.model.roomdb.daos.PlaylistDao;

import java.util.List;

public class PlaylistRepository {
    private PlaylistDao playlistDao;
    private LiveData<List<Playlist>> allPlaylists;

    public PlaylistRepository(Application application){
        AudioDatabase database = AudioDatabase.getInstance(application);
        this.playlistDao = database.playlistDao();
        this.allPlaylists = this.playlistDao.getAllPlaylists();
    }

    public void insert(Playlist playlist){
        AudioDatabase.databaseExecutor.execute(() -> {
            playlistDao.insert(playlist);
        });
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

    public LiveData<List<Playlist>> getAllPlaylists() {
        return allPlaylists;
    }
}
