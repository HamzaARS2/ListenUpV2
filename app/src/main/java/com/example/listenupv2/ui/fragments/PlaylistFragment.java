package com.example.listenupv2.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Playlist;
import com.example.listenupv2.model.entities.relations.PlaylistWithAudios;
import com.example.listenupv2.ui.PlaylistAudiosActivity;
import com.example.listenupv2.ui.adapters.PlaylistAdapter;
import com.example.listenupv2.viewmodels.PlaylistViewModel;

import java.io.Serializable;
import java.util.List;


public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnSavedPlaylistClick<PlaylistWithAudios>{

    public static final String PLAYLIST_INTENT_CODE = "playlist";
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private PlaylistViewModel viewModel;


    public PlaylistFragment() {
        // Required empty public constructor
    }


    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PlaylistAdapter(R.layout.audio_playlist_layout);
        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getPlaylistsWithAudios().observe(this, new Observer<List<PlaylistWithAudios>>() {
            @Override
            public void onChanged(List<PlaylistWithAudios> playlistWithAudios) {
                adapter.setPlaylistsWithAudios(playlistWithAudios);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = view.findViewById(R.id.playlist_recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }



    // To open a playlist
    @Override
    public void onItemClick(PlaylistWithAudios playlist) {
        Intent intent = new Intent(getActivity(), PlaylistAudiosActivity.class);
        intent.putExtra(PLAYLIST_INTENT_CODE,  playlist);
        startActivity(intent);
        Toast.makeText(getContext(), ""+playlist.getAudios().size(), Toast.LENGTH_SHORT).show();
    }
}