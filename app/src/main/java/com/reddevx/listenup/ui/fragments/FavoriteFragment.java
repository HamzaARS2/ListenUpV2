package com.reddevx.listenup.ui.fragments;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.Favorite;
import com.reddevx.listenup.model.roomdb.DataReceiver;
import com.reddevx.listenup.ui.PlayerActivity;
import com.reddevx.listenup.ui.adapters.FavoriteAdapter;
import com.reddevx.listenup.ui.interfaces.AudioActions;
import com.reddevx.listenup.viewmodels.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment implements FavoriteAdapter.OnFavoriteClickListener  {


    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private FavoriteViewModel viewModel;
    private static AudioActions mActionListener;
    private TextView emptyText;


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

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                updateList(favorites);
                adapter.setFavorites(favorites);
                if (favorites.size() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                }else
                    emptyText.setVisibility(View.GONE);

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // Adding audios to top of the recycler view
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        emptyText = view.findViewById(R.id.emptyFavoriteTv);
    }

    // Checks deleted audios from external storage
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
    public void onItemClick(int favoriteIndex, ArrayList<Favorite> favoriteList) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(PlayerActivity.INTENT_AUDIO_INDEX_KEY,favoriteIndex);
        intent.putParcelableArrayListExtra(PlayerActivity.INTENT_AUDIO_LIST_KEY, Audio.parseList(favoriteList));
        startActivity(intent);

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
               if (mActionListener != null)
                   mActionListener.onFullHeartClick(favorite);
               Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
               return false;
           }
       });
    }


    public static void setOnAudioActionChanged(AudioActions action){
        mActionListener = action;
    }


}