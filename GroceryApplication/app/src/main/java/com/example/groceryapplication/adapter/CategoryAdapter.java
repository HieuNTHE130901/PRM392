package com.example.groceryapplication.adapter;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Cart> list;
    double totalPrice=0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public CategoryAdapter(Context context, List<Cart> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getProduct_img_url()).into(holder.img);
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getProductPrice());
        double totalPrice = Double.parseDouble(list.get(position).getTotalPrice());
        String roundedTotalPrice = String.format("%.2f", totalPrice);
        holder.totalprice.setText(roundedTotalPrice);
        holder.quantity.setText(list.get(position).getTotalQuantity());
        holder.date.setText(list.get(position).getCurrentDate());
        holder.time.setText(list.get(position).getCurrentTime());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("AddToCart").document(list.get(position).getDocummentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            list.remove(list.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();


                        }else{
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
            deleteItem = itemView.findViewById(R.id.delete);
        }
    }
}
