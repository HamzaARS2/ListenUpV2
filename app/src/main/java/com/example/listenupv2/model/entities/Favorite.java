package com.example.listenupv2.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_table")
public class Favorite {

    @PrimaryKey(autoGenerate = false)
    private int id;
    @NonNull
    private String favorite_title;
    @NonNull
    private String favorite_uri;

    public Favorite(int id, String favorite_title, String favorite_uri) {
        this.id = id;
        this.favorite_title = favorite_title;
        this.favorite_uri = favorite_uri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFavorite_title() {
        return favorite_title;
    }

    public String getFavorite_uri() {
        return favorite_uri;
    }
}
