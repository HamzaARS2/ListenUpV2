package com.example.listenupv2.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.ui.AudioPlayer;
import com.example.listenupv2.ui.PlayerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;


public class AudioControllerFragment extends Fragment implements View.OnClickListener {

    public static final String PLAYING_AUDIO_KEY = "audioInBackground";
    public static final String PLAYING_AUDIO_SECOND_KEY = "isCurrentAudioPlaying";
    private TextView currentAudioTitle;
    private Button playBtn,pauseBtn;
    private Audio currentAudio;
    private boolean isPlaying;

    public AudioControllerFragment() {
        // Required empty public constructor
    }


    public static AudioControllerFragment newInstance(Audio audio, boolean isPlaying){
        AudioControllerFragment fragment = new AudioControllerFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYING_AUDIO_KEY,audio);
        args.putBoolean(PLAYING_AUDIO_SECOND_KEY,isPlaying);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            currentAudio = (Audio) getArguments().getSerializable(PLAYING_AUDIO_KEY);
            isPlaying = getArguments().getBoolean(PLAYING_AUDIO_SECOND_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_controller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentAudioTitle = view.findViewById(R.id.control_audio_title);
        playBtn = view.findViewById(R.id.control_play_btn);
        pauseBtn = view.findViewById(R.id.control_pause_btn);
        currentAudioTitle.setText(currentAudio.getTitle());
        currentAudioTitle.setSelected(true);
        playBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        setCurrentViewStatus();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.control_play_btn:
                //AudioPlayer.play();
                playingViewStatus();
                break;
            case R.id.control_pause_btn:
                //AudioPlayer.pause();
                pausedViewStatus();
                break;

        }
    }

    public void setCurrentViewStatus(){
        if (isPlaying){
            playingViewStatus();
        }else {
            pausedViewStatus();
        }
    }

    public void playingViewStatus(){
        playBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
    }

    public void pausedViewStatus(){
        playBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
    }
}