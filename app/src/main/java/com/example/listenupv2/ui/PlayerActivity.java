package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.databinding.ActivityPlayerBinding;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.service.AudioService;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    public static final String INTENT_AUDIO_CODE = "audioToPlay";
    public static final String INTENT_AUDIO_LIST_KEY = " listToBeQueued";
    public static final String INTENT_AUDIO_INDEX_KEY = " audioIndexToPlay";
    private ActivityPlayerBinding binding;
    private static Audio currentAudio;
    private static List<Audio> audioList;
    public static int currentAudioIndex;
    private static AudioPlayer player;
    private Handler handler = new Handler();
    private Runnable runnable;
    public AudioService audioService;
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
        runnable = new Runnable() {
            @Override
            public void run() {
                binding.seekBar.setProgress(AudioPlayer.getCurrentPosition());
                handler.postDelayed(this,1000);
            }
        };

        prepareAudio(currentAudio);



    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.loopCheckbox.setChecked(AudioPlayer.isLooping());
        binding.loopCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AudioPlayer.setLooping(isChecked);
            }
        });

    }

    private void startAudioService(){
        Intent intent = new Intent(getBaseContext(),AudioService.class);
        ContextCompat.startForegroundService(getBaseContext(),intent);
    }

    private void stopAudioService(){
        Intent intent = new Intent(getBaseContext(),AudioService.class);
        stopService(intent);
    }

    public void prepareAudio(Audio currentAudio){
        if(player != null){
            if (!currentAudio.getUri().equals(AudioPlayer.audio.getUri())) {
                AudioPlayer.stopAudio();
                handler.removeCallbacks(runnable);
                player = null;
            }else {
                if (!AudioPlayer.isPlaying()){
                    pausedView();
                }
                binding.activityPlayerAudioName.setText(currentAudio.getTitle());
                binding.durationTv.setText(AudioPlayer.getConvertedAudioDuration());
                binding.seekBar.setMax(AudioPlayer.getAudioDuration());
                handler.postDelayed(runnable,0);
                return;
            }
        }
        setCurrentAudio(currentAudio);
    }


    private void setCurrentAudio(Audio audio){
        player = new AudioPlayer(getBaseContext(), audio);
        binding.activityPlayerAudioName.setText(audio.getTitle());
        binding.durationTv.setText(AudioPlayer.getConvertedAudioDuration());
        binding.seekBar.setMax(AudioPlayer.getAudioDuration());
        AudioPlayer.setOnCompletionListener(this::onCompletion);
        startAudio();
    }


    public void startAudio(){
        //AudioPlayer.play();
        startAudioService();
        handler.postDelayed(runnable, 0);
        playingView();
    }

    public void pauseAudio(){
        AudioPlayer.pause();
        handler.removeCallbacks(runnable);
        pausedView();
    }

    public void releaseAudio(){
        stopAudioService();
        handler.removeCallbacks(runnable);
        pausedView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                if (AudioPlayer.isPlaying()){
                    AudioPlayer.stopAudio();
                }else if (AudioPlayer.mp == null){
                    setCurrentAudio(currentAudio);
                }
               startAudio();
                break;
            case R.id.player_btn_pause:
                pauseAudio();
                break;
            case R.id.player_btn_skip_next:
                restAudio();
                startNextAudio();
                break;
            case R.id.player_btn_skip_previous:
                restAudio();
                startPreviousAudio();
                break;
        }
    }



    public void playingView(){
        binding.playerBtnPause.setVisibility(View.VISIBLE);
        binding.playerBtnPlay.setVisibility(View.GONE);
    }

    public void pausedView(){
        binding.playerBtnPlay.setVisibility(View.VISIBLE);
        binding.playerBtnPause.setVisibility(View.GONE);
    }

    private void getAudioList(){
        int audioIndex = getIntent().getIntExtra(INTENT_AUDIO_INDEX_KEY,-1);
        ArrayList<Audio> queueList= getIntent().getParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY);
        if (audioIndex != -1)
            currentAudioIndex = audioIndex;
        if (queueList != null)
            audioList = getIntent().getParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY);
            currentAudio = audioList.get(currentAudioIndex);
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
        Toast.makeText(this, "completed", Toast.LENGTH_SHORT).show();
        pausedView();
        restAudio();
        if (!AudioPlayer.isLooping()) {
            // Next audio index
            startNextAudio();
        }
    }

    private void startNextAudio(){
        releaseAudio();
        player = null;
        currentAudioIndex++;
            if (currentAudioIndex <= audioList.size() - 1) {
                // Continue audio list if next audio exist
                prepareAudio(audioList.get(currentAudioIndex));
            } else {
                // replay audio list if the next audio not exist
                currentAudioIndex = 0;
                prepareAudio(audioList.get(currentAudioIndex));
            }
        }

        private void startPreviousAudio(){
            releaseAudio();
            player = null;
            currentAudioIndex--;
            if (currentAudioIndex > -1) {
                // Continue audio list if next audio exist
                prepareAudio(audioList.get(currentAudioIndex));
            } else {
                // replay audio list if the next audio not exist
                currentAudioIndex = audioList.size()-1;
                prepareAudio(audioList.get(currentAudioIndex));
            }
        }

        private void restAudio(){
            binding.seekBar.setProgress(0);
            AudioPlayer.seekTo(0);
            handler.removeCallbacks(runnable);
        }

    // -> Optional objective :
    // The problem is if we have 3 audios in a playlist that the user is playing right now
    // if we are playing the second audio and before the currentAudio finished the user deleted the next audio of this currentAudio
    // then we shouldn't start the deleted audio

}