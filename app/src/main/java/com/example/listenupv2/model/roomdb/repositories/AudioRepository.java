package com.example.listenupv2.model.roomdb.repositories;

import android.app.Application;
import android.icu.text.RelativeDateTimeFormatter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.roomdb.AudioDatabase;
import com.example.listenupv2.model.roomdb.DataReceiver;
import com.example.listenupv2.model.roomdb.daos.AudioDao;

import java.util.List;

public class AudioRepository {

    private AudioDao audioDao;
    private static int lastSize = -1;
    private LiveData<List<Audio>> allAudios;

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

    public LiveData<Audio> getAudio(String audio_uri){
        LiveData<Audio> audioLiveData = audioDao.getAudio(audio_uri);
        return audioLiveData;
    }

    public LiveData<List<Audio>> getAllAudios() {
        return allAudios;
    }










}
