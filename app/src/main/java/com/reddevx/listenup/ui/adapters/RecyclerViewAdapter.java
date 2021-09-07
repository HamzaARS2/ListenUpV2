package com.reddevx.listenup.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.reddevx.listenup.R;
import com.reddevx.listenup.model.entities.Audio;
import com.reddevx.listenup.service.AudioService;
import com.reddevx.listenup.ui.PlayerActivity;
import com.reddevx.listenup.ui.interfaces.AnimationInterface;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AnimationInterface {
   private List<Audio> audioList = new ArrayList<>();
   public OnItemClickListener listener;
   private int selectedItemPosition = -1;
   public static LottieAnimationView animationView;
   private Context context;


   public RecyclerViewAdapter(){
      PlayerActivity.setOnAudioAnimStart(this);
   }

   @Override
   public void onAudioAnimStart() {
      notifyDataSetChanged();
   }




   public interface OnItemClickListener {
      void onItemClick(int index);
      void onPopupMenuClick(Audio audio,int position,View view);

   }




   public void setOnItemClickListener(OnItemClickListener listener){
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
      Audio audio = audioList.get(position);
            ((Holder) holder).imageView.setImageResource(R.drawable.audio_icon);
            ((Holder) holder).title.setText(audio.getTitle());


            // set animation to current audio

      if (AudioService.audio != null){
         if (AudioService.audio.getUri().equals(audio.getUri())){
            holder.itemView.findViewById(R.id.lottieAnimationView).setVisibility(View.VISIBLE);
         }else
            holder.itemView.findViewById(R.id.lottieAnimationView).setVisibility(View.GONE);
      }


   }

   @Override
   public int getItemCount() {
      return audioList.size();
   }

   @Override
   public int getItemViewType(int position) {
      return super.getItemViewType(position);
   }

   public void setAudios(List<Audio> audios){
      this.audioList = audios;
      notifyDataSetChanged();
   }

   public void removeItem(int position){
      audioList.remove(position);
      notifyDataSetChanged();
   }


    class Holder extends RecyclerView.ViewHolder{
      ImageView imageView;
      TextView title;
      ImageButton popupMenuBtn;

      public Holder(@NonNull View itemView, OnItemClickListener listener) {
         super(itemView);
         this.imageView = itemView.findViewById(R.id.playlist_audio_img);
         this.title = itemView.findViewById(R.id.audio_tv_title);
         this.popupMenuBtn = itemView.findViewById(R.id.control_play_btn);

         itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (listener != null){
                  if (getAdapterPosition() != RecyclerView.NO_POSITION){
                     listener.onItemClick(getAdapterPosition());
                     selectedItemPosition = getAdapterPosition();
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
                     listener.onPopupMenuClick(audioList.get(getAdapterPosition()),getAdapterPosition(),v);
                  }
               }
            }
         });


      }
   }



}
