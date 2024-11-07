package com.example.se171676_finalproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.se171676_finalproject.Domain.ItemsDomain;
import com.example.se171676_finalproject.R;
import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private ArrayList<ItemsDomain> items;
    private Context context;
    private OnItemClickListener listener;

    // Constructor
    public ItemsAdapter(ArrayList<ItemsDomain> items, Context context) {
        this.items = items != null ? items : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position < items.size()) {
            ItemsDomain item = items.get(position);

            if (item != null) {
                holder.title.setText(item.getTitle());
                holder.description.setText(item.getDescription());
                holder.price.setText(String.valueOf(item.getPrice()) + " VND");
                holder.oldPrice.setText(String.valueOf(item.getOldPrice()) + " VND");
                holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.rating.setText("Rating: " + item.getRating());
                holder.review.setText("Reviews: " + item.getReview());

                if (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) {
                    Glide.with(context)
                            .load(item.getPicUrl().get(0))
                            .into(holder.image);
                } else {
                    holder.image.setImageResource(R.drawable.star);
                }

                holder.itemView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onDeleteClick(position);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price, oldPrice, rating, review;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            price = itemView.findViewById(R.id.item_price);
            oldPrice = itemView.findViewById(R.id.item_oldPrice);
            rating = itemView.findViewById(R.id.item_rating);
            review = itemView.findViewById(R.id.item_review);
            image = itemView.findViewById(R.id.item_image);
        }
    }

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
}
