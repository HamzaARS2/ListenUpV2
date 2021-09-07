package com.reddevx.listenup.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favorite_table", indices = {@Index(value = {"favorite_title","favorite_uri"},unique = true)})
public class Favorite implements Serializable , Parcelable {

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

    protected Favorite(Parcel in) {
        favorite_id = in.readInt();
        favorite_title = in.readString();
        favorite_uri = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(favorite_id);
        dest.writeString(favorite_title);
        dest.writeString(favorite_uri);
    }

    public static Favorite parse(Audio audio){
        return new Favorite(audio.getTitle(),audio.getUri());
    }
}
