package com.example.listenupv2.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listenupv2.R;
import com.example.listenupv2.model.entities.Audio;
import com.example.listenupv2.model.entities.Favorite;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private List<Audio> audioList = new ArrayList<>();
   private List<Favorite> favoriteList = new ArrayList<>();
   public onItemClickListener listener;
   private Context context;

   public interface onItemClickListener {
      void onItemClick(Audio audio);
      void onPopupMenuClick(Audio audio,int position,View view);
   }






   public void setOnItemClickListener(onItemClickListener listener){
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
         Audio audio = audioList.get(position);
         ((Holder)holder).imageView.setImageResource(R.drawable.audio_icon);
         ((Holder)holder).title.setText(audio.getTitle());

         if (favoriteList.size() != 0) {
            Favorite favorite = favoriteList.get(position);
            ((Holder) holder).imageView.setImageResource(R.drawable.audio_icon);
            ((Holder) holder).title.setText(favorite.getFavorite_title());
         }

   }

   @Override
   public int getItemCount() {
      return audioList.size();
   }

   public void setAudios(List<Audio> audios){
      this.audioList = audios;
      notifyDataSetChanged();
   }
   public void setFavorites(List<Favorite> favorites){
      this.favoriteList = favorites;
      notifyDataSetChanged();
   }

//   public void addFavorite(Audio audio,int position){
//      dbManager.open();
//      dbManager.insertFavoriteAudio(audio);
//      dbManager.close();
//   }
//   public void deleteFavorite(Audio audio,int position){
//      dbManager.open();
//      dbManager.deleteFromFavorite(audio);
//      dbManager.close();
//      audioList.remove(position);
//      notifyDataSetChanged();
//   }





    class Holder extends RecyclerView.ViewHolder{
      ImageView imageView;
      TextView title;
      ImageButton popupMenuBtn;

      public Holder(@NonNull View itemView, onItemClickListener listener) {
         super(itemView);
         this.imageView = itemView.findViewById(R.id.audio_img_view);
         this.title = itemView.findViewById(R.id.audio_tv_title);
         this.popupMenuBtn = itemView.findViewById(R.id.popup_menu_btn);

         itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (listener != null){
                  if (getAdapterPosition() != RecyclerView.NO_POSITION){
                     listener.onItemClick(audioList.get(getAdapterPosition()));
                  }
               }
            }
         });
         popupMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (listener != null){
                  if (getAdapterPosition() != RecyclerView.NO_POSITION){
                     listener.onPopupMenuClick(audioList.get(getAdapterPosition()),getAdapterPosition(),v);
                  }
               }
            }
         });
      }
   }



}
