package com.example.groceryapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Order;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrderAdapter extends  FirestoreRecyclerAdapter<Order, OrderAdapter.OrderViewHolder> {

    public OrderAdapter(@NonNull FirestoreRecyclerOptions<Order> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position, @NonNull Order model) {
        holder.orderId.setText(model.getOrderId());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.address.setText(model.getAddress());
        holder.time.setText(model.getTime());
        holder.address.setText(model.getAddress());
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, date, time, address, orderValue, orderStatus;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            date = itemView.findViewById(R.id.order_date);
            time = itemView.findViewById(R.id.order_time);
            address = itemView.findViewById(R.id.order_value);
            orderValue =  itemView.findViewById(R.id.order_value);
            orderStatus =  itemView.findViewById(R.id.order_status);

        }
    }
}
