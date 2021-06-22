package com.example.listenupv2.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.model.roomdb.DataReceiver;
import com.example.listenupv2.ui.adapters.RecyclerViewAdapter;
import com.example.listenupv2.ui.interfaces.AudioInteraction;
import com.example.listenupv2.viewmodels.AudioViewModel;
import com.example.listenupv2.viewmodels.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;


public class AudiosFragment extends Fragment implements AudioInteraction {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private AudioViewModel viewModel;
    private FavoriteViewModel favoriteViewModel;
    private List<Audio> audios;
    public AudiosFragment() {
        // Required empty public constructor
    }

    public static AudiosFragment newInstance(String param1, String param2) {
        AudiosFragment fragment = new AudiosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerViewAdapter();
        viewModel = new ViewModelProvider(this).get(AudioViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        //audios = new DataReceiver(getContext()).getAvailableAudioFiles();
        //viewModel.deleteAllAudios();


    }

    @Override
    public void onResume() {
        super.onResume();
        audios = new DataReceiver(getContext()).getAvailableAudioFiles();
        viewModel.insertAudioList(audios);
        viewModel.getAllAudios().observe(this, new Observer<List<Audio>>() {
            @Override
            public void onChanged(List<Audio> audioList) {
                Toast.makeText(getContext(), ""+audios.size(), Toast.LENGTH_SHORT).show();
                adapter.setAudios(audios);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audios, container, false);
        recyclerView = view.findViewById(R.id.audio_audios_recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Audio audio) {
                Toast.makeText(getContext(), audio.getUri(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPopupMenuClick(Audio audio, int position, View view) {
                showPopupMenu(view,position,audio);
            }
        });
    }

    public void showPopupMenu(View view,int position,Audio audio){
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.inflate(R.menu.audio_popup_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.audio_popup_addfavorite:
                        Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        Favorite favorite = new Favorite(audio.getTitle(),audio.getUri());
                        favoriteViewModel.insert(favorite);
                        return true;
                    case R.id.audio_popup_addplaylist:
                        Toast.makeText(getActivity(), "Added to playlist", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onAudioClick(Audio audio) {

    }

    @Override
    public void onPopupMenuClick(String name) {
        Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
    }


}