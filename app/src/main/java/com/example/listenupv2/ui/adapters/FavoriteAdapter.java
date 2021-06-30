package com.example.listenupv2.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Favorite> favoriteList = new ArrayList<>();
    public OnFavoriteClicklistener listener;
    private Context context;


    public interface OnFavoriteClicklistener {
        void onItemClick(Favorite favoriteAudio);
        void onPopupMenuClick(Favorite favoriteAudio,int position,View view);
    }

    public void setOnItemClickListener(OnFavoriteClicklistener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_layout, parent, false);
        return new Holder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            Favorite favorite = favoriteList.get(position);
            ((Holder) holder).imageView.setImageResource(R.drawable.audio_icon);
            ((Holder) holder).title.setText(favorite.getFavorite_title());


    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void setFavorites(List<Favorite> favorites){
        this.favoriteList = favorites;
        notifyDataSetChanged();
    }






    class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        ImageButton popupMenuBtn;

        public Holder(@NonNull View itemView, OnFavoriteClicklistener listener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.playlist_audio_img);
            this.title = itemView.findViewById(R.id.audio_tv_title);
            this.popupMenuBtn = itemView.findViewById(R.id.control_play_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        if (getAdapterPosition() != RecyclerView.NO_POSITION){
                            listener.onItemClick(favoriteList.get(getAdapterPosition()));
                        }
                    }
                }
            });
            popupMenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        if (getAdapterPosition() != RecyclerView.NO_POSITION){
                            listener.onPopupMenuClick(favoriteList.get(getAdapterPosition()),getAdapterPosition(),v);

                        }
                    }
                }
            });
        }
    }

}
