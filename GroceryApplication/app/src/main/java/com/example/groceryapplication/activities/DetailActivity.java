package com.example.groceryapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Item;

public class DetailActivity extends AppCompatActivity {

    ImageView img,add,remove;
    TextView name, description, price;
    Button addToCart;
    Toolbar toolbar;

    Item item = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);




        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof Item){
            item = (Item) object;
        }


        img = findViewById(R.id.item_detail_image);
        add = findViewById(R.id.add_item);
        remove = findViewById(R.id.remove_item);
        name = findViewById(R.id.item_detail_name);
        description = findViewById(R.id.item_detail_description);
        price = findViewById(R.id.item_detail_price);
        addToCart = findViewById(R.id.add_to_cart);

        if(item != null){
            Glide.with(getApplicationContext()).load(item.getImg_url()).into(img);
            name.setText(item.getName());
            description.setText(item.getDescription());
            price.setText("Price: "+item.getPrice()+" $/kg");

        }


    }


}