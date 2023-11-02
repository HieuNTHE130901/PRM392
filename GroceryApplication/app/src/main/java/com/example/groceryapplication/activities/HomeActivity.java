package com.example.groceryapplication.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.CategoryAdapter;
import com.example.groceryapplication.models.Cart;
import com.example.groceryapplication.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<Cart> categoryList;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home
        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Initialize categoryList and navCategoryAdapter
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList);

        // Check if the cart has products
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("AddToCart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categoryList.clear(); // Clear the list before adding items
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String docummentId = documentSnapshot.getId();
                                Cart cart = documentSnapshot.toObject(Cart.class);
                                cart.setDocummentId(docummentId);
                                categoryList.add(cart);
                            }
                            categoryAdapter.notifyDataSetChanged();

                            // Calculate the total and check if the cart has products

                            if (categoryList.isEmpty()) {
                                // The cart is empty
                                //Toast.makeText(HomeActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                                showNotification("Your cart is empty", "Go buy some product now!");
                            } else {
                                // The cart has products
                                //Toast.makeText(HomeActivity.this, "Your cart has "+categoryList.size()+" items", Toast.LENGTH_SHORT).show();
                                showNotification("Your cart have some items", "Go buy it now!");
                            }
                        }
                    }
                });
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private static final int NOTIFICATION_ID = 1;

    public void showNotification(String title, String message) {
        // Create a notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("app_channel", "App Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        // Create a notification with a custom layout
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "app_channel")
                .setSmallIcon(R.drawable.ic_grocery)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(0)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setAutoCancel(true);
        //.setContentIntent(pendingIntent); // Set the PendingIntent for the notification

        // Show the notification with a fixed notification ID
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
