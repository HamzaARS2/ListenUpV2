package com.reddevx.listenup.ui.fragments;

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

import com.reddevx.listenup.R;
import com.reddevx.listenup.service.AudioService;
import com.reddevx.listenup.ui.PlayerActivity;
import com.reddevx.listenup.ui.interfaces.OnAudioChangedInterface;


public class AudioControllerFragment extends Fragment implements View.OnClickListener, OnAudioChangedInterface , AudioService.PlayBackViews {

    public static final String PLAYING_AUDIO_KEY = "audioInBackground";
    public static final String IS_PLAYING = "isCurrentAudioPlaying";
    private static AudioService.PlayBackViews playbackListener;
    private TextView currentAudioTitle;
    private Button playBtn,pauseBtn;
    private int currentAudioIndex;
    private boolean isPlaying;

    public AudioControllerFragment() {
        // Required empty public constructor
    }


    public static AudioControllerFragment newInstance(int audioIndex, boolean isPlaying){
        AudioControllerFragment fragment = new AudioControllerFragment();
        Bundle args = new Bundle();
        args.putInt(PLAYING_AUDIO_KEY,audioIndex);
        args.putBoolean(IS_PLAYING,isPlaying);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            currentAudioIndex = getArguments().getInt(PLAYING_AUDIO_KEY);
            isPlaying = getArguments().getBoolean(IS_PLAYING);
        }
        PlayerActivity.setOnAudioChanged(this);
        AudioService.setPlaybackViewListener(this);
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
        currentAudioTitle.setText(AudioService.audio.getTitle());
        currentAudioTitle.setSelected(true);
        playBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        setCurrentViewStatus();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(PlayerActivity.INTENT_AUDIO_INDEX_KEY,currentAudioIndex);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.control_play_btn:
                AudioService.play();
                playingViewStatus();
                playbackListener.onPlayAudio();
                break;
            case R.id.control_pause_btn:
                AudioService.pause();
                pausedViewStatus();
                playbackListener.onPauseAudio();
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


    @Override
    public void onAudioChanged() {
        currentAudioTitle.setText(AudioService.audio.getTitle());
        currentAudioIndex = AudioService.audioIndex;
    }

    @Override
    public void onPlayAudio() {
        playingViewStatus();
    }

    @Override
    public void onPauseAudio() {
        pausedViewStatus();
    }

    public static void setPlayBackListener(AudioService.PlayBackViews listener){
        playbackListener = listener;
    }
}