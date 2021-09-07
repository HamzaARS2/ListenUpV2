package com.reddevx.listenup.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.model.entities.Favorite;
import com.reddevx.listenup.ui.interfaces.AudioActions;

import static com.reddevx.listenup.service.AudioService.PLAYING_AUDIO;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String PLAY_ACTION = "audio.play";
    public static final String PAUSE_ACTION = "audio.pause";
    public static final String NEXT_ACTION = "audio.next";
    public static final String PREV_ACTION = "audio.previous";
    public static final String REPEAT_ACTION = "audio.repeat";
    public static final String SHUFFLE_ACTION = "audio.shuffle";
    public static final String REPEAT_OFF_ACTION = "audio.repeatOff";
    public static final String FULL_HEART_ACTION = "audio.fullHeart";
    public static final String EMPTY_HEART_ACTION = "audio.emptyHeart";
    private static AudioActions mActionListener ;


    @Override
    public void onReceive(Context context, Intent intent) {


       if (intent.getAction() != null){
           switch (intent.getAction()){
               case PLAY_ACTION:
                   mActionListener.onPlayClick();
                   break;
               case PAUSE_ACTION:
                   mActionListener.onPauseClick();
                   break;
               case NEXT_ACTION:
                   mActionListener.onNextClick();
                   break;
               case PREV_ACTION:
                   mActionListener.onPreviousClick();
                   break;
               case REPEAT_OFF_ACTION:
                   mActionListener.onRepeat();
                   break;
               case REPEAT_ACTION:
                   mActionListener.onShuffle();
                   break;
               case SHUFFLE_ACTION:
                   mActionListener.onRepeatOff();
                   break;
               case FULL_HEART_ACTION:
                   if (intent != null) {
                       Audio audio = (Audio) intent.getSerializableExtra(PLAYING_AUDIO);
                       Favorite favorite = Favorite.parse(audio);
                       mActionListener.onFullHeartClick(favorite);
                   }
                   break;
               case EMPTY_HEART_ACTION:
                   if (intent != null) {
                       Audio audio = (Audio) intent.getSerializableExtra(PLAYING_AUDIO);
                       Favorite favorite = Favorite.parse(audio);
                       mActionListener.onEmptyHeartClick(favorite);
                   }
                   break;
           }
       }
    }

    public static void setAudioActionListener(AudioActions listener){
        mActionListener = listener;
    }



}