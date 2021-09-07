package com.reddevx.listenup.ui.interfaces;

import com.reddevx.listenup.model.entities.Favorite;

public interface FavoriteActionListener {
    void insertAction(Favorite favorite);
    void deleteAction(Favorite favorite);


}
