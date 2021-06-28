package com.example.listenupv2.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.model.roomdb.DataReceiver;
import com.example.listenupv2.ui.adapters.FavoriteAdapter;
import com.example.listenupv2.viewmodels.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment implements FavoriteAdapter.OnFavoriteClicklistener {


    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private FavoriteViewModel viewModel;



    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FavoriteAdapter();
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

//        favoriteArrayList = new ArrayList<>();
//        favoriteArrayList.add(new Favorite(1,"favorite1","uri1"));
//        favoriteArrayList.add(new Favorite(2,"favorite2","uri2"));
//        favoriteArrayList.add(new Favorite(3,"favorite3","uri3"));


        Toast.makeText(getContext(), "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                updateList(favorites);
                adapter.setFavorites(favorites);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.audio_favorite_recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    public void updateList(List<Favorite> favorites) {
        ArrayList<Audio> audios = new DataReceiver(getContext()).getAvailableAudioFiles();
                for (int i = 0; i < favorites.size(); i++){
                    boolean isExist = false;
                    int j = 0;
                    while (j < audios.size() && !isExist){
                        if (favorites.get(i).getFavorite_uri().equals(audios.get(j).getUri())){
                            isExist = true;
                        }
                        j++;
                    }
                    if (!isExist)
                       viewModel.delete(favorites.get(i));
                }
    }

    @Override
    public void onItemClick(Favorite favoriteAudio) {
        Toast.makeText(getActivity(), favoriteAudio.getFavorite_title(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPopupMenuClick(Favorite favoriteAudio, int position, View view) {
        showFavoritePopupMenu(view,favoriteAudio);
    }

    public void showFavoritePopupMenu(View view, Favorite favorite){
       PopupMenu popupMenu = new PopupMenu(getActivity(),view);
       popupMenu.inflate(R.menu.favorite_popup_menu);
       popupMenu.show();
       popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               viewModel.delete(favorite);
               Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
               return false;
           }
       });
    }


}