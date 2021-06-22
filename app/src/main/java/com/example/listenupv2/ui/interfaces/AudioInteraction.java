package com.example.listenupv2.ui.interfaces;

import com.example.listenupv2.model.entities.Audio;

public interface AudioInteraction {
    void onAudioClick(Audio audio);
    void onPopupMenuClick(String name);
}
