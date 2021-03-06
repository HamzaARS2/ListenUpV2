package com.reddevx.listenup.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.Playlist;
import com.reddevx.listenup.model.entities.relations.PlaylistAudioCrossRef;
import com.reddevx.listenup.ui.adapters.PlaylistAdapter;
import com.reddevx.listenup.viewmodels.AudioViewModel;
import com.reddevx.listenup.viewmodels.PlaylistViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class SavedPlaylistFragment extends BottomSheetDialogFragment implements PlaylistAdapter.OnSavedPlaylistClick<Playlist>, View.OnClickListener {
    public static final String SAVING_AUDIO_ARG = "save_AUDIO";
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private Button newPlaylistBtn,createPlaylistBtn;
    private TextInputLayout textInputLayout;
    private PlaylistViewModel plViewModel;
    private AudioViewModel audioViewModel;
    private PlaylistAudioCrossRef crossRef;
    private RelativeLayout saved_playlists,create_playlist;


    public static SavedPlaylistFragment newInstance( Audio audio){
        SavedPlaylistFragment fragment = new SavedPlaylistFragment();
        Bundle args = new Bundle();
        args.putSerializable(SAVING_AUDIO_ARG,audio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Audio audio = (Audio) getArguments().getSerializable(SAVING_AUDIO_ARG);
            crossRef = new PlaylistAudioCrossRef(audio.getAudio_id());
        }

        audioViewModel = new ViewModelProvider(getActivity()).get(AudioViewModel.class);
        adapter = new PlaylistAdapter(R.layout.saved_playlists_layout);
        plViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        plViewModel.getAllPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                adapter.setPlaylists(playlists);
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_playlist_fragment,container,false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newPlaylistBtn = view.findViewById(R.id.newplaylist_btn);
        recyclerView = view.findViewById(R.id.saved_recyclerview);
        saved_playlists = view.findViewById(R.id.saved_playlists);
        create_playlist = view.findViewById(R.id.create_playlist_layout);
        createPlaylistBtn = view.findViewById(R.id.create_playlist_btn);
        textInputLayout = view.findViewById(R.id.textinput_et);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        newPlaylistBtn.setOnClickListener(this);
        createPlaylistBtn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(Playlist playlist) {
        crossRef.setPlaylist_id(playlist.getPlaylist_id());
            audioViewModel.insertPlaylistAudioCrossRef(crossRef);
            Toast.makeText(getActivity(), "Added successfully", Toast.LENGTH_SHORT).show();
            dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newplaylist_btn:
                saved_playlists.setVisibility(View.GONE);
                create_playlist.setVisibility(View.VISIBLE);
                break;
            case R.id.create_playlist_btn:
                addNewPlaylistWithAudio();
                break;
        }
    }

    private void addNewPlaylistWithAudio(){
        String playlistName = textInputLayout.getEditText().getText().toString();
        long insert = plViewModel.insert(new Playlist(playlistName, 1));
        crossRef.setPlaylist_id(insert);
        audioViewModel.insertPlaylistAudioCrossRef(crossRef);
        dismiss();
        Toast.makeText(getActivity(), "Added successfully ", Toast.LENGTH_SHORT).show();
    }
}
