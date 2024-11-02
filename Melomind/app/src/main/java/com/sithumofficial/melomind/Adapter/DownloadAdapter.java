package com.sithumofficial.melomind.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sithumofficial.melomind.LocalPlayActivity;
import com.sithumofficial.melomind.R;

import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyViewHolder> {

    private Context context;
    private ArrayList speech_id,speech_title,speech_speakers,speech_category,speech_duration,speech_cover_image,speech_filepath;

    public DownloadAdapter(Context context,
                           ArrayList speech_id,
                           ArrayList speech_title,
                           ArrayList speech_speakers,
                           ArrayList speech_category,
                           ArrayList speech_duration,
                           ArrayList speech_cover_image,
                           ArrayList speech_filepath) {
        this.context = context;
        this.speech_id = speech_id;
        this.speech_title = speech_title;
        this.speech_speakers = speech_speakers;
        this.speech_category = speech_category;
        this.speech_duration = speech_duration;
        this.speech_cover_image = speech_cover_image;
        this.speech_filepath = speech_filepath;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.speech_title_txt.setText(String.valueOf(speech_title.get(position)));
        holder.speech_speakers_txt.setText(String.valueOf(speech_speakers.get(position)));
        holder.speech_category_txt.setText(String.valueOf(speech_category.get(position)));
        holder.speech_duration_txt.setText(String.valueOf(speech_duration.get(position)));

        // Use Glide to load and display the cover image
        Glide.with(context)
                .load(String.valueOf(speech_cover_image.get(position))) // Assuming speech_cover_image is a list of image URLs
                .placeholder(R.drawable.dummy_item_cover) // You can specify a placeholder image
                .into(holder.speech_cover_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click here, e.g., navigate to PlayActivity
                Intent intent = new Intent(context, LocalPlayActivity.class);

                // Pass data to PlayActivity, if needed
                intent.putExtra("speech_id",  String.valueOf(speech_id.get(position)));
                intent.putExtra("speech_filepath",  String.valueOf(speech_filepath.get(position)));
                intent.putExtra("speech_title",  String.valueOf(speech_title.get(position)));
                intent.putExtra("speech_coverimage",  String.valueOf(speech_cover_image.get(position)));
                intent.putExtra("speech_category",  String.valueOf(speech_category.get(position)));
                intent.putExtra("speech_duration",  String.valueOf(speech_duration.get(position)));
                intent.putExtra("speech_speakers",  String.valueOf(speech_speakers.get(position)));
                intent.putExtra("speech_count", speech_id.size());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return speech_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView speech_title_txt,speech_speakers_txt,speech_category_txt,speech_duration_txt;
        ImageView speech_cover_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            speech_title_txt = itemView.findViewById(R.id.item_name);
            speech_speakers_txt = itemView.findViewById(R.id.speakers);
            speech_category_txt = itemView.findViewById(R.id.category_name);
            speech_duration_txt = itemView.findViewById(R.id.duration);
            speech_cover_image = itemView.findViewById(R.id.item_cover_image);
        }
    }
}
