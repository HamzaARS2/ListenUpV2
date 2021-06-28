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

public class PlaylistRepository {
    private PlaylistDao playlistDao;
    private LiveData<List<Playlist>> allPlaylists;


    public PlaylistRepository(Application application){
        AudioDatabase database = AudioDatabase.getInstance(application);
        this.playlistDao = database.playlistDao();
        this.allPlaylists = this.playlistDao.getAllPlaylists();
    }

    public long insert(Playlist playlist){
     AudioDatabase.databaseExecutor.execute(() -> {
         playlistDao.insert(playlist);
     });
     return 0;
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
