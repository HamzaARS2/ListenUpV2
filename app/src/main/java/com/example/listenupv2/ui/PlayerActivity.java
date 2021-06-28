package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.databinding.ActivityPlayerBinding;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.ui.fragments.AudiosFragment;

import java.io.File;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private ActivityPlayerBinding binding;
    private Audio audio;
    private AudioPlayer player;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        binding.playerBtnPlay.setOnClickListener(this);
        binding.playerBtnPause.setOnClickListener(this);
        binding.playerBtnSkipNext.setOnClickListener(this);
        binding.playerBtnSkipPrevious.setOnClickListener(this);
        binding.seekBar.setOnSeekBarChangeListener(this);
        prepareAudio();
        binding.seekBar.setMax(player.getAudioDuration());
        player.setOnCompletionListener(this::onCompletion);

        runnable = new Runnable() {
            @Override
            public void run() {
                binding.seekBar.setProgress(player.getCurrentPosition());
                handler.postDelayed(this,1000);
            }
        };



    }

    @Override
    protected void onResume() {
        super.onResume();
        player.play();
        handler.postDelayed(runnable,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                player.play();
                handler.postDelayed(runnable,0);
                binding.playerBtnPause.setVisibility(View.VISIBLE);
                binding.playerBtnPlay.setVisibility(View.GONE);
                break;
            case R.id.player_btn_pause:
                player.pause();
                handler.removeCallbacks(runnable);
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
            player = new AudioPlayer(getBaseContext(),Uri.fromFile(new File(audio.getUri())));
            binding.activityPlayerAudioName.setText(audio.getTitle());
            binding.durationTv.setText(player.getConvertedAudioDuration());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            player.seekTo(progress);
        }
            binding.positionTimeTv.setText(player.convertTime(progress));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
        binding.playerBtnPause.setVisibility(View.GONE);
        binding.playerBtnPlay.setVisibility(View.VISIBLE);
        binding.seekBar.setProgress(0);
        player.seekTo(0);
    }
}