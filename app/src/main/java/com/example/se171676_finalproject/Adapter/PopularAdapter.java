package com.example.se171676_finalproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.se171676_finalproject.Activity.DetailActivity;
import com.example.se171676_finalproject.Domain.ItemsDomain;
import com.example.se171676_finalproject.databinding.ViewholderPopListBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<ItemsDomain> items;
    Context context;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderPopListBinding binding=ViewholderPopListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        holder.binding.title.setText(items.get(position).getTitle());
        holder.binding.reviewTxt.setText(""+items.get(position).getReview());
        holder.binding.priceTxt.setText(items.get(position).getPrice()+"VND");
        holder.binding.oldPriceTxt.setText(items.get(position).getOldPrice()+"VND");
        holder.binding.ratingBar.setRating(Float.parseFloat(""+items.get(position).getRating()+""));
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.ratingTxt.setText("("+items.get(position).getRating()+")");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop());
        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ViewholderPopListBinding binding;
        public ViewHolder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
