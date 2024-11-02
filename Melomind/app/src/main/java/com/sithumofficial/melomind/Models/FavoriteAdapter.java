package com.sithumofficial.melomind.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sithumofficial.melomind.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FavoriteAdapter extends FirebaseRecyclerAdapter<ItemModel, FavoriteAdapter.MyViewHolder> {

    private static OnItemClickListener mListener;

    public FavoriteAdapter(@NonNull FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ItemModel model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorititem_layout, parent, false);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(ItemModel item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView item_cover_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_cover_image = itemView.findViewById(R.id.item_cover_image);

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
            Glide.with(item_cover_image.getContext())
                    .load(model.getCover_image_url())
                    .into(item_cover_image);
        }
    }
}
