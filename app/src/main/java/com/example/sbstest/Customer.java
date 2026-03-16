//Project: SBS
//Author: Jose Ron Coka
//File: BookStore.java
//Version: Working Prototype 2
//Date: 11/26/2025

package com.example.sbstest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Customer extends AppCompatActivity {

    //UI elements
    Button submitButton;
    Button checkButton;

    Button logOutButton;
    TextView customerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_customer);


        //Initilize UI elements
        customerTitle=findViewById(R.id.customerTitle);
        submitButton=findViewById(R.id.submitOrderButton);
        checkButton=findViewById(R.id.checkOrdersButton);
        logOutButton=findViewById(R.id.logOutButton);

        //Extract customer email.
        //Extract email from Database Authentication and set it into the Edit Text

        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        String userEmail = userAuth.getCurrentUser().getEmail();
        customerTitle.setText("Welcome \n"+userEmail);


        //Button to go to Submit Order Activity
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(Customer.this, SubmitOrder.class);
                startActivity(intent);
            }
        });


        //Button for check orders activity
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to the new view
                Intent intent = new Intent(Customer.this, checkYourOrders.class);
                startActivity(intent);
            }
        });

        //Button to log out
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log out and go back to the Home Screen/Main Activity
                FirebaseAuth.getInstance().signOut();   // 1. Log out user
                Intent intent = new Intent(Customer.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}