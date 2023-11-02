package com.example.groceryapplication.activities;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.groceryapplication.common.Service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    Service service = new Service();
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
                                service.showNotification("Your cart is empty", "Go buy some product now!");
                            } else {
                                // The cart has products
                                //Toast.makeText(HomeActivity.this, "Your cart has "+categoryList.size()+" items", Toast.LENGTH_SHORT).show();
                                service.showNotification("Your cart have some items", "Go buy it now!");
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

}
