package com.example.listenupv2.model.roomdb.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.model.roomdb.AudioDatabase;
import com.example.listenupv2.model.roomdb.daos.FavoriteDao;

import java.util.List;

public class FavoriteRepository {

    private FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> allFavorites;

    public FavoriteRepository(Application application){
        AudioDatabase database = AudioDatabase.getInstance(application);
        this.favoriteDao = database.favoriteDao();
        this.allFavorites = this.favoriteDao.getAllFavoriteAudios();
    }


    public void insert(Favorite favoriteAudio){
        AudioDatabase.databaseExecutor.execute(() -> {
            favoriteDao.insert(favoriteAudio);
        });
    }

    public void update(Favorite favoriteAudio){
        AudioDatabase.databaseExecutor.execute(() -> {
            favoriteDao.update(favoriteAudio);
        });
    }

    public void delete(Favorite favoriteAudio){
        AudioDatabase.databaseExecutor.execute(() -> {
            favoriteDao.delete(favoriteAudio);
        });
    }

    public void deleteAllFavoriteAudios(){
        AudioDatabase.databaseExecutor.execute(() -> {
            favoriteDao.deleteAllFavoriteAudios();
        });
    }

    public LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }
}
