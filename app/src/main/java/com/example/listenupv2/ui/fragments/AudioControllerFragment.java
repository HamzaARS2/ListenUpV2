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


public class AudioControllerFragment extends Fragment implements View.OnClickListener {

    public static final String PLAYING_AUDIO_KEY = "audioInBackground";
    private TextView currentAudioTitle;
    private Button playBtn,pauseBtn;
    private Audio currentAudio;

    public AudioControllerFragment() {
        // Required empty public constructor
    }


    public static AudioControllerFragment newInstance( Audio audio){
        AudioControllerFragment fragment = new AudioControllerFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLAYING_AUDIO_KEY,audio);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            currentAudio = (Audio) getArguments().getSerializable(PLAYING_AUDIO_KEY);
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "terset", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra(AudiosFragment.INTENT_AUDIO_CODE,currentAudio);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.control_play_btn:
                AudioPlayer.play();
                playBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.control_pause_btn:
                AudioPlayer.pause();
                playBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
                break;

        }
    }
}