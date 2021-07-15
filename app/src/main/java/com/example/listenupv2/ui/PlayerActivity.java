package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.icu.text.RelativeDateTimeFormatter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.databinding.ActivityPlayerBinding;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.service.AudioSService;
import com.example.listenupv2.ui.interfaces.AudioActions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ServiceConnection, MediaPlayer.OnCompletionListener, AudioSService.OnStartNewAudio {

    public static final String INTENT_AUDIO_CODE = "audioToPlay";
    public static final String INTENT_AUDIO_LIST_KEY = " listToBeQueued";
    public static final String INTENT_AUDIO_INDEX_KEY = " audioIndexToPlay";
    public static final String SP_FILE_NAME = "lastAudioFile";
    private ActivityPlayerBinding binding;
    private final Handler handler = new Handler();
    public Runnable runnable;
    public int currentAudioIndex;
    public Audio currentAudio;
    private Audio mLastAudio;
    public ArrayList<Audio> audioList;
    public AudioSService audioSService;
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
        getAudioList();
        setLastAudio();


    }

    @Override
    protected void onResume() {
        super.onResume();
        startAudioService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLastAudio();
    }

    public void saveLastAudio(){
        SharedPreferences sp = getSharedPreferences(SP_FILE_NAME,MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
        editor.putString("title",AudioSService.audio.getTitle());
        editor.putInt("duration",AudioSService.mp.getDuration());
        editor.putBoolean("isPlaying",AudioSService.mp.isPlaying());
        editor.commit();
    }

    public void setLastAudio(){
        SharedPreferences sp = getSharedPreferences(SP_FILE_NAME,MODE_PRIVATE);
        if (sp != null) {
            String title = sp.getString("title", "Unknown");
            int duration = sp.getInt("duration", 0);
            boolean isPlaying = sp.getBoolean("isPlaying",false);
            binding.activityPlayerAudioName.setText(title);
            binding.durationTv.setText(convertTime(duration));
            binding.seekBar.setMax(duration);
            if (!isPlaying)
            pausedView();
            startSeekBarTask();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                audioSService.play();
                playingView();
                handler.postDelayed(runnable,0);
                break;
            case R.id.player_btn_pause:
                audioSService.pause();
                pausedView();
                handler.removeCallbacks(runnable);
                break;
            case R.id.player_btn_skip_next:
                audioSService.startNextAudio();
                break;
            case R.id.player_btn_skip_previous:
                audioSService.startPreviousAudio();
                break;
        }
    }

    private void getAudioList(){
        int index = getIntent().getIntExtra(INTENT_AUDIO_INDEX_KEY,-1);
        ArrayList<Audio> list = getIntent().getParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY);
        if (list != null)
            audioList = list;
        if (index != -1) {
            currentAudioIndex = index;
            currentAudio = audioList.get(currentAudioIndex);
        }
    }

    private void startSeekBarTask(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if (AudioSService.mp != null) {
                    binding.seekBar.setProgress(AudioSService.mp.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }

            }
        };
        handler.postDelayed(runnable,0);
    }

    private void startAudioService(){
        Intent intent = new Intent(getBaseContext(),AudioSService.class);
        intent.putExtra(AudioSService.CURRENT_AUDIO, currentAudioIndex);
        intent.putParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY,audioList);
        startService(intent);
        bindService(intent,this,BIND_AUTO_CREATE);
    }





    public void playingView(){
        binding.playerBtnPause.setVisibility(View.VISIBLE);
        binding.playerBtnPlay.setVisibility(View.GONE);
    }

    public void pausedView(){
        binding.playerBtnPlay.setVisibility(View.VISIBLE);
        binding.playerBtnPause.setVisibility(View.GONE);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            AudioSService.mp.seekTo(progress);
        }
        binding.positionTimeTv.setText(convertTime(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        AudioSService.AudioBinder binder = (AudioSService.AudioBinder) service;
        audioSService = binder.getAudioSService();
        audioSService.setOnStartNewAudio(this);
        audioSService.setOnCompletion(this);
        //Toast.makeText(audioSService, ""+convertTime(audioSService.mp.getDuration()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Toast.makeText(audioSService, "Disconnected", Toast.LENGTH_SHORT).show();
        audioSService = null;
    }




    @SuppressLint("DefaultLocale")
    public  String convertTime(int milliseconds) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                ,TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        audioSService.startNextAudio();
        Toast.makeText(audioSService, "completed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioPrepared(Audio audio,int duration) {
        binding.seekBar.setMax(duration);
        binding.durationTv.setText(convertTime(duration));
        binding.activityPlayerAudioName.setText(audio.getTitle());
        startSeekBarTask();
    }

    // -> Optional objective :
    // The problem is if we have 3 audios in a playlist that the user is playing right now
    // if we are playing the second audio and before the currentAudio finished the user deleted the next audio of this currentAudio
    // then we shouldn't start the deleted audio

}