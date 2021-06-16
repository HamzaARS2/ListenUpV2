package com.example.listenupv2.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.roomdb.repositories.PlaylistRepository;

import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    private PlaylistRepository repository;
    private final LiveData<List<Playlist>> allPlaylists;

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PlaylistRepository(application);
        this.allPlaylists = this.repository.getAllPlaylists();
    }


    public void insert(Playlist playlist){
        repository.insert(playlist);
    }

    public void update(Playlist playlist){
        repository.update(playlist);
    }

    public void delete(Playlist playlist){
        repository.delete(playlist);
    }

    public void deleteAllPlaylists(){
        repository.deleteAllPlaylists();
    }

    public LiveData<List<Playlist>> getAllPlaylists() {
        return allPlaylists;
    }
}
