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


        inquiryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(BookStore.this, Inquiry.class);
                startActivity(intent);
            }
        });

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