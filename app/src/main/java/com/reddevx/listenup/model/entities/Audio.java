package com.reddevx.listenup.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "audio_table", indices = {@Index(value = {"title","uri"},unique = true)})
public class Audio implements Parcelable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int audio_id;
    @NonNull
    private String title;
    @NonNull
    private String uri;

    public Audio(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    protected Audio(Parcel in) {
        audio_id = in.readInt();
        title = in.readString();
        uri = in.readString();
    }

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public int getAudio_id() {
        return audio_id;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public static Audio parse(Favorite favorite){
        Audio audio = new Audio(favorite.getFavorite_title(),favorite.getFavorite_uri());
        audio.setAudio_id(favorite.getFavorite_id());
        return audio;
    }

    public static ArrayList<Audio> parseList(ArrayList<Favorite> favorites){
        ArrayList<Audio> parsedAudios = new ArrayList<>();
        for (int i = 0; i < favorites.size(); i++){
            parsedAudios.add(Audio.parse(favorites.get(i)));
        }
        return parsedAudios;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(audio_id);
        dest.writeString(title);
        dest.writeString(uri);
    }
}
