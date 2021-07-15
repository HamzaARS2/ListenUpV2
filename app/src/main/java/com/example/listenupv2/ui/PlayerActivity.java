package com.example.listenupv2.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.listenupv2.R;
import com.example.listenupv2.databinding.ActivityPlayerBinding;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.service.AudioSService;
import com.example.listenupv2.ui.interfaces.OnAudioChangedInterface;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ServiceConnection, AudioSService.OnStartNewAudio, CompoundButton.OnCheckedChangeListener {

    public static final String INTENT_AUDIO_CODE = "audioToPlay";
    public static final String INTENT_AUDIO_LIST_KEY = " listToBeQueued";
    public static final String INTENT_AUDIO_INDEX_KEY = " audioIndexToPlay";
    private ActivityPlayerBinding binding;
    private final Handler handler = new Handler();
    public Runnable runnable;
    public int currentAudioIndex;
    public Audio currentAudio;
    private static String title;
    private static int duration;
    public static ArrayList<Audio> audioList;
    public AudioSService audioSService;
    private static OnAudioChangedInterface mListener;
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
        startAudioService();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setLastAudio();
        if (AudioSService.mp != null) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLastAudio();
    }

    private void saveLastAudio(){
        if (AudioSService.mp != null){
            title = AudioSService.audio.getTitle();
            duration = AudioSService.mp.getDuration();
        }
    }

    private void setLastAudio(){
        if (AudioSService.mp != null){
            if (AudioSService.audio.getTitle().equals(title)){
                binding.activityPlayerAudioName.setText(title);
                binding.durationTv.setText(convertTime(duration));
                binding.seekBar.setMax(duration);
                if (!AudioSService.mp.isPlaying())
                    pausedView();
                startSeekBarTask();
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                AudioSService.play();
                playingView();
                handler.postDelayed(runnable,0);
                break;
            case R.id.player_btn_pause:
                AudioSService.pause();
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

        if (list != null) {
            audioList = list;
            Log.d("TAG", "getAudioList: Not null");
        }
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
        audioSService.setOnCompletion(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audioSService.startNextAudio();
                mListener.onAudioChanged();
                Toast.makeText(audioSService, "completed", Toast.LENGTH_SHORT).show();
            }
        });
        binding.loopCheckbox.setChecked(AudioSService.mp.isLooping());
        binding.loopCheckbox.setOnCheckedChangeListener(this);

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

    public static void setOnAudioChanged(OnAudioChangedInterface listener){
        mListener = listener;
    }







    @Override
    public void onAudioPrepared(Audio audio,int duration) {
        binding.seekBar.setMax(duration);
        binding.durationTv.setText(convertTime(duration));
        binding.activityPlayerAudioName.setText(audio.getTitle());
        startSeekBarTask();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        AudioSService.mp.setLooping(isChecked);
    }


    // -> Optional objective :
    // The problem is if we have 3 audios in a playlist that the user is playing right now
    // if we are playing the second audio and before the currentAudio finished the user deleted the next audio of this currentAudio
    // then we shouldn't start the deleted audio because if we did the app will crash

}