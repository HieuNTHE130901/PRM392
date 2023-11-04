package com.example.groceryapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.groceryapplication.R;
import com.example.groceryapplication.models.Item;
import com.example.groceryapplication.utils.AndroidUtil;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private TextView quantity;
    private double totalQuantity = 0.1;
    private double totalPrice = 0;
    private ImageView img, add, remove;
    private TextView name, description, price;
    private Button addToCart;
    private Item item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Item) {
            item = (Item) object;
        }
        img = findViewById(R.id.item_detail_image);
        add = findViewById(R.id.add_item);
        remove = findViewById(R.id.remove_item);
        name = findViewById(R.id.item_detail_name);
        description = findViewById(R.id.item_detail_description);
        price = findViewById(R.id.item_detail_price);
        addToCart = findViewById(R.id.add_to_cart);
        quantity = findViewById(R.id.quantity);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 10) {
                    totalQuantity = totalQuantity + 0.1;
                    // Format totalQuantity to one decimal place
                    String formattedQuantity = decimalFormat.format(totalQuantity);
                    quantity.setText(formattedQuantity + " kg");
                    updateTotalPrice();
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 0.1) {
                    totalQuantity = totalQuantity - 0.1;
                    // Format totalQuantity to one decimal place
                    String formattedQuantity = decimalFormat.format(totalQuantity);
                    quantity.setText(formattedQuantity + " kg");
                    updateTotalPrice();
                }
            }
        });
        if (item != null) {
            Glide.with(getApplicationContext()).load(item.getImg_url()).into(img);
            name.setText(item.getName());
            description.setText(item.getDescription());
            price.setText(String.valueOf(item.getPrice()));
            updateTotalPrice();
        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });
    }

    private void addedToCart() {
        if (item == null) {
            Toast.makeText(this, "Item details are missing.", Toast.LENGTH_LONG).show();
            return;
        }
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("product_img_url", item.getImg_url());
        cartMap.put("productName", item.getName());
        cartMap.put("productPrice", item.getPrice());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", totalQuantity);
        cartMap.put("totalPrice", totalPrice);

        // Get the reference to the user's cart collection
        FirebaseUtil.userCartCollection().add(cartMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            AndroidUtil.showToast(DetailActivity.this, "Added to cart!");
                            finish();
                        } else {
                            // Handle the error if the cart item could not be added.
                            AndroidUtil.showToast(DetailActivity.this, "Failed to add to cart");
                        }
                    }
                });
    }


    private void updateTotalPrice() {
        if (item != null) {
            totalPrice = item.getPrice() * totalQuantity;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            // Open CartFragment
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
            navController.navigate(R.id.nav_cart);
            return true;
        } else if (itemId == R.id.action_home) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
