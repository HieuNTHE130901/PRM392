package com.example.groceryapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Cart;
import com.example.groceryapplication.models.Order;
import com.example.groceryapplication.ui.cart.CartFragment;
import com.example.groceryapplication.ui.order.OrderFragment;
import com.example.groceryapplication.utils.AndroidUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<Order> list;
    private OrderFragment orderFragment;

    public OrderAdapter(Context context, List<Order> list, OrderFragment orderFragment) {
        this.context = context;
        this.list = list;
        this.orderFragment = orderFragment;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        Order orderItem = list.get(position);

        holder.time.setText(orderItem.getTime());
        holder.date.setText(orderItem.getDate());
        holder.address.setText(orderItem.getAddress());
        holder.status.setText(orderItem.getStatus());
        holder.orderValue.setText(AndroidUtil.formatPrice(orderItem.getOrderValue()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date, time, address, orderValue, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.order_date);
            time = itemView.findViewById(R.id.order_time);
            address = itemView.findViewById(R.id.order_address);
            orderValue = itemView.findViewById(R.id.order_value);
            status = itemView.findViewById(R.id.order_status);
        }
    }
}
