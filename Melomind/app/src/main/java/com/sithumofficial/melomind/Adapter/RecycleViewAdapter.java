package com.sithumofficial.melomind.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter {
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView coverimage;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

        }
    }
}
