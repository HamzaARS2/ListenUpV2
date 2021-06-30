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
import com.example.listenupv2.model.entities.Favorite;
import com.example.listenupv2.ui.fragments.AudiosFragment;

import java.io.File;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private ActivityPlayerBinding binding;
    private Audio audio;
    private static AudioPlayer player;
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
        binding.activityPlayerAudioName.setSelected(true);
        prepareAudio();

        AudioPlayer.setOnCompletionListener(this::onCompletion);

        runnable = new Runnable() {
            @Override
            public void run() {
                binding.seekBar.setProgress(AudioPlayer.getCurrentPosition());
                handler.postDelayed(this,1000);
            }
        };

        AudioPlayer.play();
        handler.postDelayed(runnable,0);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                if (AudioPlayer.isPlaying()){
                    AudioPlayer.stopAudio();
                }
                AudioPlayer.play();
                handler.postDelayed(runnable,0);
                binding.playerBtnPause.setVisibility(View.VISIBLE);
                binding.playerBtnPlay.setVisibility(View.GONE);
                break;
            case R.id.player_btn_pause:
                AudioPlayer.pause();
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
            Object file = getIntent().getSerializableExtra(AudiosFragment.INTENT_AUDIO_CODE);
            if (file instanceof Favorite){
                audio = Audio.parse((Favorite) file);
            }else
            audio = (Audio) file;
                if (player != null){
                    if (!audio.getUri().equals(AudioPlayer.audio.getUri())) {
                        AudioPlayer.stopAudio();
                        handler.removeCallbacks(runnable);
                        player = null;
                    }else {
                        binding.activityPlayerAudioName.setText(audio.getTitle());
                        binding.durationTv.setText(AudioPlayer.getConvertedAudioDuration());
                        binding.seekBar.setMax(AudioPlayer.getAudioDuration());
                        return;
                    }
                }

                player = new AudioPlayer(getBaseContext(), audio);
                binding.activityPlayerAudioName.setText(audio.getTitle());
                binding.durationTv.setText(AudioPlayer.getConvertedAudioDuration());
                binding.seekBar.setMax(AudioPlayer.getAudioDuration());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            AudioPlayer.seekTo(progress);
        }
            binding.positionTimeTv.setText(AudioPlayer.convertTime(progress));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        binding.playerBtnPause.setVisibility(View.GONE);
        binding.playerBtnPlay.setVisibility(View.VISIBLE);
        binding.seekBar.setProgress(0);
        AudioPlayer.seekTo(0);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}