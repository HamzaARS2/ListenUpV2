package com.reddevx.listenup.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Favorite;
import com.reddevx.listenup.service.AudioService;
import com.reddevx.listenup.ui.PlayerActivity;
import com.reddevx.listenup.ui.interfaces.AnimationInterface;
import com.reddevx.listenup.ui.interfaces.SelectedViewAnim;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AnimationInterface {

    private List<Favorite> favoriteList = new ArrayList<>();
    public OnFavoriteClickListener listener;
    public static LottieAnimationView animationView;
    private Context context;
    private SelectedViewAnim mSelectedViewListener;


    public FavoriteAdapter(){
        PlayerActivity.setOnFavAudioAnimStart(this);
    }

    @Override
    public void onAudioAnimStart() {
        notifyDataSetChanged();
    }


    public interface OnFavoriteClickListener {
        void onItemClick(int favoriteIndex, ArrayList<Favorite> favorites);
        void onPopupMenuClick(Favorite favoriteAudio,int position,View view);
    }

    public void setOnItemClickListener(OnFavoriteClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_layout, parent, false);
        context = parent.getContext();
        return new Holder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Favorite favorite = favoriteList.get(position);
            ((Holder) holder).imageView.setImageResource(R.drawable.audio_icon);
            ((Holder) holder).title.setText(favorite.getFavorite_title());

        // set animation to current audio
        if (AudioService.audio != null){
            if (AudioService.audio.getUri().equals(favorite.getFavorite_uri())){
                holder.itemView.findViewById(R.id.lottieAnimationView).setVisibility(View.VISIBLE);
            }else
                holder.itemView.findViewById(R.id.lottieAnimationView).setVisibility(View.GONE);
        }

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

        public Holder(@NonNull View itemView, OnFavoriteClickListener listener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.playlist_audio_img);
            this.title = itemView.findViewById(R.id.audio_tv_title);
            this.popupMenuBtn = itemView.findViewById(R.id.control_play_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        if (getAdapterPosition() != RecyclerView.NO_POSITION){
                            listener.onItemClick(getAdapterPosition(),(ArrayList<Favorite>) favoriteList);
                            notifyDataSetChanged();
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
