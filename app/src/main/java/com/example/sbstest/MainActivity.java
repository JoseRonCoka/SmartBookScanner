//Project: SBS
//Author: Jose Ron Coka
//Version: Inquiry Implemented, App Flow created.
//Date: 03/17/2024



package com.example.sbstest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity {

    Button placeOrderButton;
    Button bookstoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        placeOrderButton=findViewById(R.id.placeOrderButton);
        bookstoreButton=findViewById(R.id.bookStoreButton);
        bookstoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(MainActivity.this, BookStore.class);
                startActivity(intent);
            }
        });

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
