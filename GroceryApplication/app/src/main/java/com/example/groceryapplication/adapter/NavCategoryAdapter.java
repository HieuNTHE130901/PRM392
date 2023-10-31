package com.example.groceryapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryapplication.R;
import com.example.groceryapplication.models.NavCategory;

import java.util.List;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {
    private Context context;
    private List<NavCategory> list;
    double totalPrice=0;

    public NavCategoryAdapter(Context context, List<NavCategory> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NavCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavCategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getProduct_img_url()).into(holder.img);
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getProductPrice());
        holder.totalprice.setText(list.get(position).getTotalPrice());
        holder.quantity.setText(list.get(position).getTotalQuantity());
        holder.date.setText(list.get(position).getCurrentDate());
        holder.time.setText(list.get(position).getCurrentTime());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name,price,totalprice,quantity,date,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.nav_cat_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            price = itemView.findViewById(R.id.nav_cat_price);
            totalprice = itemView.findViewById(R.id.nav_cat_total_price_of_product);
            quantity = itemView.findViewById(R.id.nav_cat_totalquantity);
            date = itemView.findViewById(R.id.nav_cat_date);
            time = itemView.findViewById(R.id.nav_cat_time);
        }
    }
}
