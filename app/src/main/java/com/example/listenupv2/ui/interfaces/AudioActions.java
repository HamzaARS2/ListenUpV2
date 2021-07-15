package com.example.listenupv2.ui.interfaces;

import com.example.listenupv2.model.entities.Audio;

import java.util.List;

public interface AudioActions {

    void onPlayClick();
    void onPauseClick();
    void onNextClick();
    void onPreviousClick();
    void onAudioClick(int index);
    void onAudioClick(int index, List<Audio> audioList);

}
