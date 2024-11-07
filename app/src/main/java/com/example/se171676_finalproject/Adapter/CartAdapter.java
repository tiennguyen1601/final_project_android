package com.example.se171676_finalproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.se171676_finalproject.Domain.ItemsDomain;
import com.example.se171676_finalproject.Helper.ChangeNumberItemsListener;
import com.example.se171676_finalproject.Helper.ManagmentCart;
import com.example.se171676_finalproject.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    ArrayList<ItemsDomain> listItemSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(ChangeNumberItemsListener changeNumberItemsListener,
                       Context context, ArrayList<ItemsDomain> listItemSelected) {
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.listItemSelected = listItemSelected;
        this.managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        ItemsDomain currentItem = listItemSelected.get(position);

        holder.binding.titleTxt.setText(currentItem.getTitle());
        holder.binding.feeEachItem.setText( currentItem.getPrice()+"VND");
        holder.binding.totalEachItem.setText("VND" +
                Math.round(currentItem.getNumberInCart() * currentItem.getPrice() * 100.0) / 100.0);
        holder.binding.numberItemTxt.setText(String.valueOf(Math.round(currentItem.getNumberInCart())));

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());
        Glide.with(holder.binding.pic.getContext())
                .load(currentItem.getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                managmentCart.plusItem(listItemSelected, currentPosition, () -> {
                    notifyDataSetChanged();
                    changeNumberItemsListener.changed();
                });
            }
        });

        holder.binding.minusCartBtn.setOnClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                managmentCart.minusItem(listItemSelected, currentPosition, () -> {
                    notifyDataSetChanged();
                    changeNumberItemsListener.changed();
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size(); // Trả về kích thước của danh sách
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
