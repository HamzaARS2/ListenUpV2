package com.example.listenupv2.model.roomdb.repositories;

import android.app.Application;
import android.icu.text.RelativeDateTimeFormatter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.relations.PlaylistAudioCrossRef;
import com.example.listenupv2.model.roomdb.AudioDatabase;
import com.example.listenupv2.model.roomdb.DataReceiver;
import com.example.listenupv2.model.roomdb.daos.AudioDao;

import java.util.List;

public class AudioRepository {

    private AudioDao audioDao;
    private LiveData<List<Audio>> allAudios;
    private LiveData<Audio> audio;

    public AudioRepository(Application application){
        AudioDatabase database = AudioDatabase.getInstance(application);
        this.audioDao = database.audioDao();
        this.allAudios = this.audioDao.getAllAudios();


    }

    public void insert(Audio audio){
        AudioDatabase.databaseExecutor.execute(() ->{
            audioDao.insert(audio);
                });

    }

    public void insertPlaylistAudioCrossRef(PlaylistAudioCrossRef crossRef){
        AudioDatabase.databaseExecutor.execute(() -> {
            audioDao.insertPlaylistAudioCrossRef(crossRef);
        });
    }

    public void update(Audio audio){
        AudioDatabase.databaseExecutor.execute(() ->{
            audioDao.update(audio);
        });
    }

    public void delete(Audio audio){
        AudioDatabase.databaseExecutor.execute(() ->{
            audioDao.delete(audio);
        });
    }

    public void deleteAudioFromPlaylist(int audioId, int playlistId){
        AudioDatabase.databaseExecutor.execute(() -> {
            audioDao.deleteAudioFromPlaylist(audioId,playlistId);
        });
    }

    public void deleteAllAudios(){
        AudioDatabase.databaseExecutor.execute(() ->{
            audioDao.deleteAllAudios();
        });
    }

    public void insertAudioList(List<Audio> audioList){
        AudioDatabase.databaseExecutor.execute(() -> {
            audioDao.insertAudioList(audioList);
        });
    }

    public LiveData<List<Audio>> getAllAudios() {
        return allAudios;
    }










}
