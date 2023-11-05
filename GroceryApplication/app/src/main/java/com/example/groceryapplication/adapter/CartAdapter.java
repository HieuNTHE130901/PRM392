package com.example.groceryapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<Cart> list;
    private CartFragment cartFragment;

    public CartAdapter(Context context, List<Cart> list, CartFragment cartFragment) {
        this.context = context;
        this.list = list;
        this.cartFragment = cartFragment;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        Cart cartItem = list.get(position);

        Glide.with(context).load(cartItem.getProduct_img_url()).into(holder.img);
        holder.name.setText(cartItem.getProductName());

        // Parse total price and quantity
        double totalPrice = cartItem.getTotalPrice();
        double productPrice = cartItem.getProductPrice();
        holder.price.setText(AndroidUtil.formatPrice(productPrice));
        holder.totalprice.setText(AndroidUtil.formatPrice(totalPrice));
        DecimalFormat quantityFormat = new DecimalFormat("##.#");
        String formattedQuantity = quantityFormat.format(cartItem.getTotalQuantity())+" kg";
        holder.quantity.setText(formattedQuantity);


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the total quantity and update the UI
                cartItem.setTotalQuantity(cartItem.getTotalQuantity() + 0.1);
                DecimalFormat quantityFormat = new DecimalFormat("##.#");
                String formattedQuantity = quantityFormat.format(cartItem.getTotalQuantity())+" kg";
                holder.quantity.setText(formattedQuantity);

                // Update the total price and UI
                double totalPrice = cartItem.getTotalQuantity() * cartItem.getProductPrice();
                cartItem.setTotalPrice(totalPrice);
                holder.totalprice.setText(AndroidUtil.formatPrice(totalPrice));

                // Update the cart item in Firestore or your data source here
                FirebaseUtil.updateCartItem(cartItem);

                // Update the total price in CartFragment
                cartFragment.updateTotalPrice();

            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItem.getTotalQuantity() > 0.1) {
                    // Decrement the total quantity and update the UI
                    cartItem.setTotalQuantity(cartItem.getTotalQuantity() - 0.1);
                    DecimalFormat quantityFormat = new DecimalFormat("##.#");
                    String formattedQuantity = quantityFormat.format(cartItem.getTotalQuantity())+" kg";
                    holder.quantity.setText(formattedQuantity);

                    // Update the total price and UI
                    double totalPrice = cartItem.getTotalQuantity() * cartItem.getProductPrice();
                    cartItem.setTotalPrice(totalPrice);
                    holder.totalprice.setText(AndroidUtil.formatPrice(totalPrice));

                    // Update the cart item in Firestore or your data source here
                    FirebaseUtil.updateCartItem(cartItem);

                    // Update the total price in CartFragment
                    cartFragment.updateTotalPrice();
                } else {
                    // If the total quantity is 1, consider deleting the item
                    FirebaseUtil.userCartCollection().document(cartItem.getDocummentId())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        list.remove(cartItem);
                                        notifyDataSetChanged();
                                        // Update the total price by calling a method in the CartFragment
                                        cartFragment.updateTotalPrice();
                                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


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
        TextView name, price, totalprice, quantity;

        ImageButton add, remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.nav_cat_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            price = itemView.findViewById(R.id.nav_cat_price);
            totalprice = itemView.findViewById(R.id.nav_cat_total_price_of_product);
            quantity = itemView.findViewById(R.id.nav_cat_totalquantity);
            deleteItem = itemView.findViewById(R.id.delete);
            add = itemView.findViewById(R.id.btn_add);
            remove = itemView.findViewById(R.id.btn_remove);

        }
    }
}
