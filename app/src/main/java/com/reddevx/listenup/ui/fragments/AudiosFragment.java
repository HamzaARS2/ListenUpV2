package com.reddevx.listenup.ui.fragments;

import android.content.Intent;
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

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.Favorite;
import com.reddevx.listenup.model.roomdb.DataReceiver;
import com.reddevx.listenup.ui.PlayerActivity;
import com.reddevx.listenup.ui.adapters.RecyclerViewAdapter;
import com.reddevx.listenup.ui.interfaces.AudioActions;
import com.reddevx.listenup.ui.interfaces.FavoriteActionListener;
import com.reddevx.listenup.viewmodels.AudioViewModel;
import com.reddevx.listenup.viewmodels.FavoriteViewModel;
import com.reddevx.listenup.viewmodels.PlaylistViewModel;

import java.util.ArrayList;
import java.util.List;


public class AudiosFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener, FavoriteActionListener {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private AudioViewModel viewModel;
    private FavoriteViewModel favoriteViewModel;
    private PlaylistViewModel playlistViewModel;
    private ArrayList<Audio> audios;
    public List<Favorite> favoriteList;
    private int currentAudioIndex;
    private static AudioActions mActionListener;
    private AdView adView;

    public AudiosFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new RecyclerViewAdapter();
        viewModel = new ViewModelProvider(this).get(AudioViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        PlayerActivity.setFavoriteListener(this);
        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                 favoriteList = favorites;
            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();
        audios = new DataReceiver(getContext()).getAvailableAudioFiles();
        viewModel.insertAudioList(audios);
        viewModel.getAllAudios().observe(this, new Observer<List<Audio>>() {
            @Override
            public void onChanged(List<Audio> audioList) {
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


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
                        Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT).show();
                        Favorite favorite = Favorite.parse(audio);

                        if (mActionListener!= null)
                            mActionListener.onEmptyHeartClick(favorite);
                        else
                            favoriteViewModel.insert(favorite);

                        return true;
                    case R.id.audio_popup_addplaylist:
                        SavedPlaylistFragment fragment = SavedPlaylistFragment.newInstance(viewModel.getAllAudios().getValue().get(position));
                        fragment.show(getActivity().getSupportFragmentManager(),"SavedPlaylistFragment");
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onItemClick(int audioIndex) {
        currentAudioIndex = audioIndex;
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putParcelableArrayListExtra(PlayerActivity.INTENT_AUDIO_LIST_KEY,audios);
        intent.putExtra(PlayerActivity.INTENT_AUDIO_INDEX_KEY,audioIndex);
        startActivity(intent);
    }




    @Override
    public void onPopupMenuClick(Audio audio, int position, View view) {
        showPopupMenu(view,position,audio);
    }



    @Override
    public void insertAction(Favorite favorite) {
        favoriteViewModel.insert(favorite);
    }

    @Override
    public void deleteAction(Favorite favorite) {
        for (int i = 0; i < favoriteList.size(); i++) {
            if (favorite.getFavorite_uri().equals(favoriteList.get(i).getFavorite_uri()))
                favoriteViewModel.delete(favoriteList.get(i));
        }
    }

    public static void setOnAudioActionChanged(AudioActions action){
        mActionListener = action;
    }



}