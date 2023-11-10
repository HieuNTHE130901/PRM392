package com.example.groceryapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Order;
import com.example.groceryapplication.utils.AndroidUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrderAdapter extends FirestoreRecyclerAdapter<Order, OrderAdapter.ViewHolder> {

    public OrderAdapter(@NonNull FirestoreRecyclerOptions<Order> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Order model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, date, time, address, orderValue, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_name);
            date = itemView.findViewById(R.id.order_date);
            time = itemView.findViewById(R.id.order_time);
            address = itemView.findViewById(R.id.order_address);
            orderValue = itemView.findViewById(R.id.order_value);
            status = itemView.findViewById(R.id.order_status);
        }

        public void bind(Order order) {
            name.setText(order.getCustomerName());
            date.setText(order.getDate());
            time.setText(order.getTime());
            address.setText(order.getAddress());
            status.setText(order.getStatus());
            orderValue.setText(AndroidUtil.formatPrice(order.getOrderValue()));
        }
    }
}
