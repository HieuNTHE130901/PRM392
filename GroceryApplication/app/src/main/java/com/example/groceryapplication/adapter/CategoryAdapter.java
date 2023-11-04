package com.example.groceryapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Cart;
import com.example.groceryapplication.ui.cart.CartFragment;
import com.example.groceryapplication.utils.AndroidUtil;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.protobuf.StringValue;

import java.text.DecimalFormat;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Cart> list;
    private CartFragment cartFragment;

    public CategoryAdapter(Context context, List<Cart> list, CartFragment cartFragment) {
        this.context = context;
        this.list = list;
        this.cartFragment = cartFragment; // Initialize the CartFragment reference
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cartItem = list.get(position);

        Glide.with(context).load(cartItem.getProduct_img_url()).into(holder.img);
        holder.name.setText(cartItem.getProductName());

        // Parse total price and quantity
        double totalPrice = cartItem.getTotalPrice();
        int totalQuantity = cartItem.getTotalQuantity();
        double productPrice = cartItem.getProductPrice();
        holder.price.setText(AndroidUtil.formatPrice(productPrice));
        holder.totalprice.setText(AndroidUtil.formatPrice(totalPrice));
        holder.quantity.setText(String.valueOf(totalQuantity));
        holder.date.setText(cartItem.getCurrentDate());
        holder.time.setText(cartItem.getCurrentTime());

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.userCartCollection().document(cartItem.getDocummentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            list.remove(cartItem);
                            notifyDataSetChanged();

                            // Update the total price by calling a method in the CartFragment
                            cartFragment.updateTotalPrice(); // Call the method in the CartFragment

                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, deleteItem;
        TextView name, price, totalprice, quantity, date, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.nav_cat_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            price = itemView.findViewById(R.id.nav_cat_price);
            totalprice = itemView.findViewById(R.id.nav_cat_total_price_of_product);
            quantity = itemView.findViewById(R.id.nav_cat_totalquantity);
            date = itemView.findViewById(R.id.nav_cat_date);
            time = itemView.findViewById(R.id.nav_cat_time);
            deleteItem = itemView.findViewById(R.id.delete);
        }
    }

}
