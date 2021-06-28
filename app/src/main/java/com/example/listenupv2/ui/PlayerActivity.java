package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.databinding.ActivityPlayerBinding;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.ui.fragments.AudiosFragment;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityPlayerBinding binding;
    private Audio audio;
    private AudioPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        binding.playerBtnPlay.setOnClickListener(this);
        binding.playerBtnPause.setOnClickListener(this);
        binding.playerBtnSkipNext.setOnClickListener(this);
        binding.playerBtnSkipPrevious.setOnClickListener(this);
        prepareAudio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.play();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                player.play();
                binding.playerBtnPause.setVisibility(View.VISIBLE);
                binding.playerBtnPlay.setVisibility(View.GONE);
                break;
            case R.id.player_btn_pause:
                player.pause();
                binding.playerBtnPlay.setVisibility(View.VISIBLE);
                binding.playerBtnPause.setVisibility(View.GONE);
                break;
            case R.id.player_btn_skip_next:
                Toast.makeText(this, "Next audio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.player_btn_skip_previous:
                Toast.makeText(this, "Previous audio", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void prepareAudio(){
        if (getIntent() != null){
            audio = (Audio) getIntent().getSerializableExtra(AudiosFragment.INTENT_AUDIO_CODE);
            player = new AudioPlayer(getBaseContext(), Uri.parse(audio.getUri()));

            binding.acivityPlayerAudioName.setText(audio.getTitle());

        }
    }
}