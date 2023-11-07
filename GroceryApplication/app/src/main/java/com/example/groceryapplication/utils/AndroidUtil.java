package com.example.groceryapplication.utils;



import android.content.Context;
import android.widget.Toast;
import java.text.DecimalFormat;

public class AndroidUtil {
    //Show Toast message
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    //Format price
    public static String formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(price).replace(',', '.')+ " ₫";
    }
}
