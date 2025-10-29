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

    Button bookInquiryButton;
    Button ordersButton;

    Button orderInquiryButton;

    Button submitBookActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_store);

        bookInquiryButton=findViewById(R.id.inquiryButton);
        ordersButton=findViewById(R.id.incomingOrdersButtton);
        orderInquiryButton=findViewById(R.id.orderInquiryButton);
        submitBookActivityButton=findViewById(R.id.submitBookActivityButton);

        //Button for Inquiry Activity
        bookInquiryButton.setOnClickListener(new View.OnClickListener() {
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

        orderInquiryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, orderInquiry.class);
                startActivity(intent);
            }
        });

        submitBookActivityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, SubmitBook.class);
                startActivity(intent);
            }
        });


    }
}