package com.reddevx.listenup.ui.interfaces;

import com.reddevx.listenup.model.entities.Favorite;

public interface AudioActions {
    void onPlayClick();
    void onPauseClick();
    void onNextClick();
    void onPreviousClick();
    void onRepeatOff();
    void onRepeat();
    void onShuffle();
    void onFullHeartClick(Favorite favorite);
    void onEmptyHeartClick(Favorite favorite);
}
