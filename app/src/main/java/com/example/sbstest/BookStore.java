//Project: SBS
//Author: Jose Ron Coka
//File: BookStore.java
//Version: Working Prototype 1
//Date: 04/16/2024


package com.example.sbstest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BookStore extends AppCompatActivity {

    Button inquiryButton;
    Button ordersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_store);

        inquiryButton=findViewById(R.id.inquiryButton);
        ordersButton=findViewById(R.id.incomingOrdersButtton);

        //Button for Inquiry Activity
        inquiryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, Inquiry.class);
                startActivity(intent);
            }
        });
        //Button for incoming order activity
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, incomingOrders.class);
                startActivity(intent);
            }
        });


    }
}