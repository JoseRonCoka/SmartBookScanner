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

import com.google.firebase.auth.FirebaseAuth;

public class BookStore extends AppCompatActivity {

    //UI elements
    Button bookInquiryButton;
    Button ordersButton;
    Button orderInquiryButton;
    Button submitBookActivityButton;

    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_store);

        //Setup UI elements
        bookInquiryButton=findViewById(R.id.inquiryButton);
        ordersButton=findViewById(R.id.incomingOrdersButtton);
        orderInquiryButton=findViewById(R.id.orderInquiryButton);
        submitBookActivityButton=findViewById(R.id.submitBookActivityButton);
        logOutButton=findViewById(R.id.logOutBookButton);

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

        //Button for order inquiry

        orderInquiryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, orderInquiry.class);
                startActivity(intent);
            }
        });

        //Button to Submit a Book to the database

        submitBookActivityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, SubmitBook.class);
                startActivity(intent);
            }
        });

        //Log Out Button

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log out and go back to the Home Screen/Main Activity
                FirebaseAuth.getInstance().signOut();   // 1. Log out user
                Intent intent = new Intent(BookStore.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}