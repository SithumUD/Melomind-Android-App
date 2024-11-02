package com.sithumofficial.melomind.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sithumofficial.melomind.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainAdapter extends FirebaseRecyclerAdapter<ItemModel, MainAdapter.myViewHolder> {

    private static OnItemClickListener mListener;

    public MainAdapter(@NonNull FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
    }

    private boolean reverseOrder = false;

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ItemModel model) {
        holder.bind(model);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_layout, parent, false);
        return new myViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(ItemModel item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView item_cover_image;
        TextView item_name, speakers, category_name, duration;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            item_cover_image = itemView.findViewById(R.id.item_cover_image);
            item_name = itemView.findViewById(R.id.item_name);
            speakers = itemView.findViewById(R.id.speakers);
            category_name = itemView.findViewById(R.id.category_name);
            duration = itemView.findViewById(R.id.duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(getItem(position));
                        }
                    }
                }
            });
        }

        public void bind(ItemModel model) {
            item_name.setText(model.getTitle());
            speakers.setText(model.getSpeakers());
            category_name.setText(model.getSelectedValue());
            duration.setText(model.getDuration());

            Glide.with(item_cover_image.getContext())
                    .load(model.getCover_image_url())
                    .into(item_cover_image);
        }
    }
}
