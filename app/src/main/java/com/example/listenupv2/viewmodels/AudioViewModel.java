package com.example.listenupv2.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.roomdb.repositories.AudioRepository;

import java.util.List;

public class AudioViewModel extends AndroidViewModel {

    private AudioRepository repository;
    private final LiveData<List<Audio>> allAudios;

    public AudioViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AudioRepository(application);
        this.allAudios = this.repository.getAllAudios();
    }

    public void insert(Audio audio){
        repository.insert(audio);
    }

    public void update(Audio audio){
        repository.update(audio);
    }

    public void delete(Audio audio){
        repository.delete(audio);
    }

    public void deleteAllAudios(){
        repository.deleteAllAudios();
    }


    public LiveData<List<Audio>> getAllAudios() {
        return allAudios;
    }
}
