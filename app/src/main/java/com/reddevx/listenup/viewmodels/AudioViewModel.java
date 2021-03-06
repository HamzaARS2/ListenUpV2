package com.reddevx.listenup.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.relations.PlaylistAudioCrossRef;
import com.reddevx.listenup.model.roomdb.repositories.AudioRepository;

import java.util.List;

public class AudioViewModel extends AndroidViewModel {

    private AudioRepository repository;
    private LiveData<List<Audio>> allAudios;
    //public MutableLiveData<List<Audio>> audios = new MutableLiveData<>();

    public AudioViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AudioRepository(application);
        this.allAudios = this.repository.getAllAudios();
    }





    public void insert(Audio audio){
        repository.insert(audio);
    }

    public void insertPlaylistAudioCrossRef(PlaylistAudioCrossRef crossRef){
        repository.insertPlaylistAudioCrossRef(crossRef);
    }

    public void update(Audio audio){
        repository.update(audio);
    }

    public void delete(Audio audio){
        repository.delete(audio);
    }

    public void deleteAudioFromPlaylist(int audioId, int playlistId){
        repository.deleteAudioFromPlaylist(audioId,playlistId);
    }
    public void deleteAllAudios(){
        repository.deleteAllAudios();
    }

    public void insertAudioList(List<Audio> audioList){
        repository.insertAudioList(audioList);
    }




    public LiveData<List<Audio>> getAllAudios() {
        return allAudios;
    }


}
