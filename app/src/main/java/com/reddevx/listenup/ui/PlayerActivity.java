package com.reddevx.listenup.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.reddevx.listenup.R;
import com.reddevx.listenup.databinding.ActivityPlayerBinding;
import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.Favorite;
import com.reddevx.listenup.receivers.NotificationReceiver;
import com.reddevx.listenup.service.AudioService;
import com.reddevx.listenup.ui.fragments.AudioControllerFragment;
import com.reddevx.listenup.ui.fragments.AudiosFragment;
import com.reddevx.listenup.ui.fragments.FavoriteFragment;
import com.reddevx.listenup.ui.interfaces.AnimationInterface;
import com.reddevx.listenup.ui.interfaces.AudioActions;
import com.reddevx.listenup.ui.interfaces.FavoriteActionListener;
import com.reddevx.listenup.ui.interfaces.OnAudioChangedInterface;
import com.reddevx.listenup.viewmodels.FavoriteViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ServiceConnection, AudioService.OnStartNewAudio, AudioActions,AudioService.PlayBackViews {

    public static final String INTENT_AUDIO_CODE = "audioToPlay";
    public static final String INTENT_AUDIO_LIST_KEY = " listToBeQueued";
    public static final String INTENT_AUDIO_INDEX_KEY = " audioIndexToPlay";
    private static final String INTENT_AUDIO_FAVORITES_KEY = "favorite.key.list";
    private ActivityPlayerBinding binding;
    private final Handler handler = new Handler();
    public Runnable runnable;
    public int currentAudioIndex;
    public Audio currentAudio;
    private static String lastTitle;
    private static int lastDuration;
    public static ArrayList<Audio> shuffleList;
    public static ArrayList<Audio> mainList;
    private List<Favorite> favoriteList;
    public AudioService audioSService;
    private static OnAudioChangedInterface mListener;
    private static FavoriteActionListener mFavoriteListener;
    private static boolean isLoopPressed = false;
    public static boolean isShufflePressed = false;


    private static AnimationInterface animListener;
    private static AnimationInterface favAnimListener;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_player);
        binding.playerBtnPlay.setOnClickListener(this);
        binding.playerBtnPause.setOnClickListener(this);
        binding.playerBtnSkipNext.setOnClickListener(this);
        binding.playerBtnSkipPrevious.setOnClickListener(this);
        binding.imgBtnLoop.setOnClickListener(this);
        binding.imgBtnShuffle.setOnClickListener(this);
        binding.seekBar.setOnSeekBarChangeListener(this);
        binding.activityPlayerAudioName.setSelected(true);
        getAudioList();
        startAudioService();
        setLastAudio();
        AudioService.setOnStartNewAudio(this);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoopPressed)
            binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_pressed_icon);
        else
            binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_icon);

        if (isShufflePressed)
            binding.imgBtnShuffle.setBackgroundResource(R.drawable.rounded_shuffle_pressed_icon);
        else
            binding.imgBtnShuffle.setBackgroundResource(R.drawable.rounded_shuffle_icon);


    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLastAudio();
    }

    private void saveLastAudio(){
        if (AudioService.mp != null){
            lastTitle = AudioService.audio.getTitle();
            lastDuration = AudioService.mp.getDuration();
        }
    }

    private void setLastAudio(){
        if (AudioService.mp != null){
                if (currentAudio.getTitle().equals(lastTitle) || isShufflePressed) {
                    binding.activityPlayerAudioName.setText(lastTitle);
                    binding.durationTv.setText(convertTime(lastDuration));
                    binding.seekBar.setMax(lastDuration);
                    startSeekBarTask();
                    if (!AudioService.mp.isPlaying())
                        pausedView();
                    else
                        playingView();
                } else {
                    isLoopPressed = false;
                    binding.activityPlayerAudioName.setText(currentAudio.getTitle());
                    binding.durationTv.setText(convertTime(AudioService.mp.getDuration()));
                    binding.seekBar.setMax(AudioService.mp.getDuration());
                    startSeekBarTask();
                }
        }
    }

    private void getAudioList(){
        int index = getIntent().getIntExtra(INTENT_AUDIO_INDEX_KEY,-1);
        ArrayList<Audio> list = getIntent().getParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY);

        if (list != null) {
            mainList = list;
        }
        if (index != -1) {
            currentAudioIndex = index;
            currentAudio = mainList.get(currentAudioIndex);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn_play:
                AudioService.play();
                playingView();
                audioSService.updatePlaybacks();
                break;
            case R.id.player_btn_pause:
                AudioService.pause();
                pausedView();
                audioSService.updatePlaybacks();
                break;
            case R.id.player_btn_skip_next:
                audioSService.startNextAudio();
                binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_icon);
                if(animListener != null)
                    animListener.onAudioAnimStart();
                break;
            case R.id.player_btn_skip_previous:
                audioSService.startPreviousAudio();
                binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_icon);
                if(animListener != null)
                    animListener.onAudioAnimStart();
                break;
            case R.id.img_btn_shuffle:
                isShufflePressed = setShuffleState();
                if (isShufflePressed) {
                    stopRepeat();
                    startShuffling(shuffleList);
                }else {
                    stopShuffling();
                }
                break;
            case R.id.img_btn_loop:
                startRepeat();
                stopShuffling();
                break;
        }

    }


    private void startShuffling(ArrayList<Audio> listToShuffle){
        audioSService.setShufflingOn(listToShuffle);
        audioSService.setShuffleAction();
    }

    private void stopShuffling(){
        audioSService.setShufflingOff(mainList);
        isShufflePressed = false;
        binding.imgBtnShuffle.setBackgroundResource(R.drawable.rounded_shuffle_icon);
        if (!isLoopPressed)
        audioSService.setRepeatOffAction();
    }

    private void startRepeat(){
        isLoopPressed = setLoopingState();
        AudioService.mp.setLooping(isLoopPressed);
        if (isLoopPressed)
            audioSService.setRepeatAction();
         else
            audioSService.setRepeatOffAction();

    }

    private void stopRepeat(){
        isLoopPressed = false;
        AudioService.mp.setLooping(isLoopPressed);
        binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_icon);
        audioSService.setRepeatOffAction();
    }

    private boolean setLoopingState(){
            if (isLoopPressed) {
                binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_icon);
                return false;
            } else {
                binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_pressed_icon);
                return true;
            }
    }

    private boolean setShuffleState(){
            if (isShufflePressed) {
                binding.imgBtnShuffle.setBackgroundResource(R.drawable.rounded_shuffle_icon);
                return false;
            } else {
                binding.imgBtnShuffle.setBackgroundResource(R.drawable.rounded_shuffle_pressed_icon);
                return true;
            }
    }



    private void startSeekBarTask(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if (AudioService.mp != null) {
                    binding.seekBar.setProgress(AudioService.mp.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable,0);
    }

    private void startAudioService(){
        Intent intent = new Intent(getBaseContext(), AudioService.class);
        intent.putExtra(AudioService.CURRENT_AUDIO, currentAudioIndex);
        intent.putParcelableArrayListExtra(INTENT_AUDIO_LIST_KEY, mainList);
        ContextCompat.startForegroundService(getBaseContext(),intent);
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
            AudioService.mp.seekTo(progress);
        }
        binding.positionTimeTv.setText(convertTime(progress));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        AudioService.AudioBinder binder = (AudioService.AudioBinder) service;
        audioSService = binder.getAudioSService();
        audioSService.setOnCompletion(mp -> {
            currentAudio = audioSService.startNextAudio();
            binding.imgBtnLoop.setBackgroundResource(R.drawable.rounded_loop_icon);
                if (mListener != null)
                    mListener.onAudioChanged();
                if (animListener != null)
                    animListener.onAudioAnimStart();
                if (favAnimListener != null)
                    favAnimListener.onAudioAnimStart();
        });
        NotificationReceiver.setAudioActionListener(this);
        FavoriteFragment.setOnAudioActionChanged(this);
        AudiosFragment.setOnAudioActionChanged(this);
        AudioControllerFragment.setPlayBackListener(this);

        new ViewModelProvider(this).get(FavoriteViewModel.class).getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                audioSService.setHeartAction(isFavorite(favorites));
            }
        });




    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
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
    public static void setOnAudioAnimStart(AnimationInterface listener){
        animListener = listener;
    }

    public static void setOnFavAudioAnimStart(AnimationInterface listener){
        favAnimListener = listener;
    }

    @Override
    public void onAudioPrepared(Audio audio,int duration) {
        //currentAudio = audio;
        binding.seekBar.setMax(duration);
        binding.durationTv.setText(convertTime(duration));
        binding.activityPlayerAudioName.setText(audio.getTitle());
        startSeekBarTask();
    }


    @Override
    public void onPlayClick() {
        AudioService.play();
        playingView();
        audioSService.updatePlaybacks();
    }

    @Override
    public void onPauseClick() {
        AudioService.pause();
        pausedView();
        audioSService.updatePlaybacks();
    }

    @Override
    public void onNextClick() {
        audioSService.startNextAudio();
    }

    @Override
    public void onPreviousClick() {
        audioSService.startPreviousAudio();
    }

    @Override
    public void onRepeatOff() {
        isShufflePressed = setShuffleState();
        stopShuffling();
        audioSService.setRepeatOffAction();
    }

    @Override
    public void onRepeat() {
        isLoopPressed = setLoopingState();
        AudioService.mp.setLooping(isLoopPressed);
        audioSService.setRepeatAction();

    }

    @Override
    public void onShuffle() {
        isLoopPressed =setLoopingState();
        AudioService.mp.setLooping(isLoopPressed);
        isShufflePressed = setShuffleState();
        startShuffling(shuffleList);
        audioSService.setShuffleAction();
    }

    @Override
    public void onFullHeartClick(Favorite favorite) {
        audioSService.setHeartAction(false);
        if (mFavoriteListener != null)
        mFavoriteListener.deleteAction(favorite);
    }

    @Override
    public void onEmptyHeartClick(Favorite favorite) {
        audioSService.setHeartAction(true);
        if (mFavoriteListener != null)
        mFavoriteListener.insertAction(favorite);

    }

    public static void setFavoriteListener(FavoriteActionListener listener){
        mFavoriteListener = listener;
    }

    private boolean isFavorite(List<Favorite> favorites){
        for (int i = 0; i < favorites.size(); i++){
            if (AudioService.audio.getUri().equals(favorites.get(i).getFavorite_uri()))
                return true;
        }
        return false;
    }

    @Override
    public void onPlayAudio() {
        audioSService.updatePlaybacks();
    }

    @Override
    public void onPauseAudio() {
        audioSService.updatePlaybacks();
    }

    // -> Optional objective :
    // The problem is if we have 3 audios in a playlist that the user is playing right now
    // if we are playing the second audio and before the currentAudio finished the user deleted the next audio of this currentAudio
    // then we shouldn't start the deleted audio because if we did the app will crash

}