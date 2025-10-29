package com.example.sbstest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class displayOrder extends AppCompatActivity {


    //Initialize UI elements and variables
    TextView orderID;
    TextView orderDetails;
    TextView bookDetails;

    Order orderFound;

    //Displays order Information for order inquiry.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_order);

        //Extract Order object data passed on intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Order")) {

            //Extract Order object. Can access data inside order using getters
            orderFound = (Order) intent.getSerializableExtra("Order");

            //Set OrderID UI
            orderID = findViewById(R.id.idTextView2);
            orderID.setText("Order ID: " + orderFound.getOrderID());

            //Set OrderDetails in UI
            orderDetails = findViewById(R.id.orderDetailsTextView2);
            orderDetails.setText(orderFound.toString());

            //Set Book Details
            bookDetails = findViewById(R.id.bookDetailsTV2);
            bookDetails.setText(orderFound.displayBookInfo());

        }
    }
}