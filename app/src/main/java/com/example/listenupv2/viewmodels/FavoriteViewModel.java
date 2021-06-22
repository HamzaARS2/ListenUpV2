package com.example.listenupv2.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.model.roomdb.repositories.FavoriteRepository;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository repository;
    private final LiveData<List<Favorite>> allFavorites;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        this.repository = new FavoriteRepository(application);
        this.allFavorites = this.repository.getAllFavorites();
    }

    public void insert(Favorite favoriteAudio){
        repository.insert(favoriteAudio);
    }

    public void update(Favorite favoriteAudio){
        repository.update(favoriteAudio);
    }

    public void delete(Favorite favoriteAudio){
        repository.delete(favoriteAudio);
    }

    public void deleteAllFavoriteAudios(){
        repository.deleteAllFavoriteAudios();
    }


    public LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }
}
