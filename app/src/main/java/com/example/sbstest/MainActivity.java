//Project: SBS
//Author: Jose Ron Coka
//File: MainActivity
//Version: Working Prototype 1
//Date: 04/16/2024


package com.example.sbstest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity {

    //Initialize Buttons
    Button placeOrderButton;
    Button bookstoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        placeOrderButton=findViewById(R.id.placeOrderButton);
        bookstoreButton=findViewById(R.id.bookStoreButton);

        //Button to go to Bookstore Side of App
        bookstoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(MainActivity.this, BookStore.class);
                startActivity(intent);
            }
        });

        //Button to go to Submit Order Activity
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(MainActivity.this, SubmitOrder.class);
                startActivity(intent);
            }
        });

    }
}
