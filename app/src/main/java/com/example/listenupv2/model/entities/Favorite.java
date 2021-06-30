package com.example.listenupv2.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favorite_table", indices = {@Index(value = {"favorite_title","favorite_uri"},unique = true)})
public class Favorite implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int favorite_id;
    @NonNull
    private String favorite_title;
    @NonNull
    private String favorite_uri;

    public Favorite( @NonNull String favorite_title, @NonNull String favorite_uri) {
        this.favorite_title = favorite_title;
        this.favorite_uri = favorite_uri;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getFavorite_id() {
        return favorite_id;
    }

    public String getFavorite_title() {
        return favorite_title;
    }

    public String getFavorite_uri() {
        return favorite_uri;
    }
}
