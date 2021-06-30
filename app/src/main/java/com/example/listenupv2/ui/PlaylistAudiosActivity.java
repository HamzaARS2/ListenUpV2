package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.relations.PlaylistWithAudios;
import com.example.listenupv2.ui.adapters.RecyclerViewAdapter;
import com.example.listenupv2.ui.fragments.AudiosFragment;
import com.example.listenupv2.ui.fragments.PlaylistFragment;

public class PlaylistAudiosActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private View view;
    private TextView playlistName, totalAudios;
    private PlaylistWithAudios playlist;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_audios);
        view = findViewById(R.id.include);
        playlistName = view.findViewById(R.id.playlist_audio_title);
        totalAudios = view.findViewById(R.id.total_audios_tv);
        recyclerView = findViewById(R.id.playlistAudiosRV);
        playlist = (PlaylistWithAudios) getIntent().getSerializableExtra(PlaylistFragment.PLAYLIST_INTENT_CODE);
        buildDataList();
        setPlaylistInfo();



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
        totalAudios.setText(playlist.getAudios().size()+" Audios");
    }

    @Override
    public void onItemClick(Audio audio) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(AudiosFragment.INTENT_AUDIO_CODE,audio);
        startActivity(intent);
    }

    @Override
    public void onPopupMenuClick(Audio audio, int position, View view) {

    }
}



