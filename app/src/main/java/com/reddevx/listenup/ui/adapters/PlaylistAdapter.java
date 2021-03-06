package com.reddevx.listenup.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Playlist;
import com.reddevx.listenup.model.entities.relations.PlaylistWithAudios;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {
    public static final int PLAYLISTS_INSTANCE = 0;
    public static final int PLAYLISTS_WITH_AUDIOS_INSTANCE = 1;
    private List<Playlist> playlists = new ArrayList<>();
    private List<PlaylistWithAudios> playlistsWithAudios = new ArrayList<>();
    private int resource;
    public OnSavedPlaylistClick listener;
    private int currentData;

    public PlaylistAdapter(int resource){
        this.resource = resource;
    }
    public interface OnSavedPlaylistClick<T> {
        void onItemClick(T playlist);
    }

    public void setOnItemClickListener(OnSavedPlaylistClick listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaylistHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(resource,parent,false)
        ,listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {
        switch (currentData){
            case PLAYLISTS_INSTANCE:
                holder.bind(playlists.get(position).getPlaylist_name());
                break;
            case PLAYLISTS_WITH_AUDIOS_INSTANCE:
                holder.bind(playlistsWithAudios.get(position).getPlaylist().getPlaylist_name());
                holder.totalAudios.setText(playlistsWithAudios.get(position).getAudios().size()+" Audios");
                break;
        }
    }



    @Override
    public int getItemCount() {
        if (playlists.size() != 0)
            return playlists.size();
        return playlistsWithAudios.size();
    }


    public void setPlaylists(List<Playlist> playlists){
        this.playlists = playlists;
        currentData = PLAYLISTS_INSTANCE;
        notifyDataSetChanged();
    }

    public void setPlaylistsWithAudios(List<PlaylistWithAudios> playlistsWithAudios){
        Collections.reverse(playlistsWithAudios);
        this.playlistsWithAudios = playlistsWithAudios;
        currentData = PLAYLISTS_WITH_AUDIOS_INSTANCE;
        notifyDataSetChanged();
    }

    class PlaylistHolder extends RecyclerView.ViewHolder {
        ImageView playlistImage;
        TextView playlistName;
        TextView totalAudios;

        public PlaylistHolder(@NonNull View itemView, OnSavedPlaylistClick listener) {
            super(itemView);
            this.playlistImage = itemView.findViewById(R.id.playlist_audio_icon);
            this.playlistName = itemView.findViewById(R.id.playlist_audio_title);
            this.totalAudios = itemView.findViewById(R.id.total_audios_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                            if (currentData == PLAYLISTS_INSTANCE)
                            listener.onItemClick(playlists.get(getAdapterPosition()));
                            else
                                listener.onItemClick(playlistsWithAudios.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }

        public void bind(String name){
            playlistImage.setImageResource(R.drawable.audio_playlist_icon);
            playlistName.setText(name);
        }
    }

}
