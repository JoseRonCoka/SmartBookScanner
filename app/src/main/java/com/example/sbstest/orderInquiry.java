//Project: SBS
//Author: Jose Ron Coka
//File: processedOrders.java
//Version: Working Prototype 2
//Date: 09/01/2025

package com.example.sbstest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class orderInquiry extends AppCompatActivity {

    //Initialize View Elements

    Button searchOrderButton;
    TextView orderInquiryText;
    EditText orderNumberInput;

    //Initialize Order found object
    Order orderFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_inquiry);

        searchOrderButton = findViewById(R.id.searchButton);
        orderNumberInput = findViewById(R.id.orderNumberInput);
        orderInquiryText = findViewById(R.id.orderInquiryText);

        //Database reference to Processed Orders Node
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("ProcessedOrders");

        searchOrderButton.setOnClickListener(v->
        {
            String orderNumber = orderNumberInput.getText().toString();
            ordersRef.child(orderNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    orderFound = snapshot.getValue(Order.class);
                    displayOrder();
                }
                else {
                    orderInquiryText.setText("Order Not Found. Please check the number and try again:");
                   orderNumberInput.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            });
        });

    }


    //Pass Order information to new Activity Display Order
    //@Override
    public void displayOrder() {

        Intent intent = new Intent(orderInquiry.this, displayOrder.class);
        intent.putExtra("Order", orderFound);
        startActivity(intent);

    }
}