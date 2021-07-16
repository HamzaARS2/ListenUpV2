package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.relations.PlaylistWithAudios;
import com.example.listenupv2.service.AudioService;
import com.example.listenupv2.ui.adapters.RecyclerViewAdapter;
import com.example.listenupv2.ui.fragments.AudioControllerFragment;
import com.example.listenupv2.ui.fragments.PlaylistFragment;
import com.example.listenupv2.viewmodels.AudioViewModel;
import com.example.listenupv2.viewmodels.PlaylistViewModel;

import java.util.ArrayList;

public class PlaylistAudiosActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private View view;
    private TextView playlistName, totalAudios;
    private PlaylistWithAudios playlist;
    private RecyclerViewAdapter adapter;
    private FragmentContainerView containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_audios);
        view = findViewById(R.id.include);
        playlistName = view.findViewById(R.id.playlist_audio_title);
        totalAudios = view.findViewById(R.id.total_audios_tv);
        recyclerView = findViewById(R.id.playlistAudiosRV);
        containerView = findViewById(R.id.playlists_audios_container);
        playlist = (PlaylistWithAudios) getIntent().getSerializableExtra(PlaylistFragment.PLAYLIST_INTENT_CODE);
        buildDataList();
        setPlaylistInfo();



    }

    @Override
    protected void onResume() {
        super.onResume();
        showRunningAudio();
    }

    private void showRunningAudio(){
        if (AudioService.mp != null){
            containerView.setVisibility(View.VISIBLE);
            AudioControllerFragment fragment = AudioControllerFragment.newInstance(AudioService.audioIndex, AudioService.mp.isPlaying());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setReorderingAllowed(true);
            ft.add(R.id.playlists_audios_container, fragment);
            ft.commit();
        }else
            containerView.setVisibility(View.GONE);
    }

    public void buildDataList(){
        adapter = new RecyclerViewAdapter();
        adapter.setAudios(playlist.getAudios());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void setPlaylistInfo(){
        playlistName.setText(playlist.getPlaylist().getPlaylist_name());
        totalAudios.setText(adapter.getItemCount()+" Audios");
    }

    @Override
    public void onItemClick(int index) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.INTENT_AUDIO_INDEX_KEY,index);
        intent.putExtra(PlayerActivity.INTENT_AUDIO_LIST_KEY,(ArrayList<Audio>)playlist.getAudios());
        startActivity(intent);
    }


    @Override
    public void onPopupMenuClick(Audio audio, int position, View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.favorite_popup_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               deleteAudio(audio,position);
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void deleteAudio(Audio audio, int position){
        new ViewModelProvider(PlaylistAudiosActivity.this)
                .get(AudioViewModel.class)
                .deleteAudioFromPlaylist(audio.getAudio_id(),playlist.getPlaylist().getPlaylist_id());
        adapter.removeItem(position);
        if (adapter.getItemCount() == 0) {
            new ViewModelProvider(PlaylistAudiosActivity.this)
                    .get(PlaylistViewModel.class).delete(playlist.getPlaylist());
            finish();
        }
        setPlaylistInfo();
    }
}



