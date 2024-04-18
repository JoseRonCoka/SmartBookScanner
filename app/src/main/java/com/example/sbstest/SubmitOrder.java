//Project: SBS
//Author: Jose Ron Coka
//File: BookStore.java
//Version: Working Prototype 1
//Date: 04/16/2024


package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AlertDialog;


public class SubmitOrder extends AppCompatActivity {

    //Initialize View Elements
    Button submitOrderButton;

    EditText customerName;
    EditText emailAddress;

    EditText customerPhone;
    EditText customerAddress;
    EditText bookISBN;

    EditText quantityBook;

    //Hanldling Fee set to 5 USD
    double handlingFee=5.00;
    double tax=0.07;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        submitOrderButton = findViewById(R.id.submitButton);

        customerName= findViewById(R.id.nameInput);
        emailAddress= findViewById(R.id.emailInput);
        customerPhone= findViewById(R.id.phoneInput);
        customerAddress= findViewById(R.id.addressInput);
        bookISBN= findViewById(R.id.isbnInput);
        quantityBook= findViewById(R.id.quantityInput);

        TextView textView4 = findViewById(R.id.textView4);

        //Database reference  to Book side.
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Books");
        //Database reference to Order side
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders"); // Reference to the "Orders" category

        //On CLick of submit button information in input fields is saved
        submitOrderButton.setOnClickListener(v->
        {
            String name = customerName.getText().toString();
            String email = emailAddress.getText().toString();
            String phone = customerPhone.getText().toString();
            String address = customerAddress.getText().toString();
            String isbn = bookISBN.getText().toString();
            String quantity = quantityBook.getText().toString();

            //check database  for isbn before posting order
            mDatabase.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {

                        //Extract book info from database, name and price
                        String title = snapshot.child("name").getValue(String.class);
                        String p= snapshot.child("price").getValue(String.class);

                        double price = Double.parseDouble(p);
                        double q= Double.parseDouble(quantity);

                        //Calculate Total order price
                        String totalPrice = String.format("%.2f", calculatePrice(price,q));

                        //Alert Dialog for User to confirm order
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitOrder.this);
                        alertDialogBuilder.setTitle("Confirm Order");
                        alertDialogBuilder.setMessage("Do you want to place an order for: '"+quantity+ "' copies of '" + title + " with an individual price of $"+p+" for total order price of $"+totalPrice+"'?");
                        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {

                            String orderId = ordersRef.push().getKey(); // Generate unique key for order

                            //Create Order object from the input info
                            Order customerOrder = new Order(orderId, name, email, phone, address, isbn, totalPrice, quantity);

                            //Push the order object into the database
                            ordersRef.child(orderId).setValue(customerOrder)
                                    .addOnSuccessListener(aVoid -> {
                                        //textView4.setText("Order submitted successfully, your Order Details: "+customerOrder.toString()+" Order Number: " + orderId);
                                        textView4.setText("Order submitted successfully, your Order Number: " + orderId+" with total order price of $"+customerOrder.getOrderCost());
                                        // Clear input fields if needed
                                        customerName.setText("");
                                        emailAddress.setText("");
                                        customerPhone.setText("");
                                        customerAddress.setText("");
                                        bookISBN.setText("");
                                        quantityBook.setText("");
                                    })
                                    .addOnFailureListener(e -> {
                                        textView4.setText("Error submitting order.");
                                    });
                        });
                        alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                            textView4.setText("Order was not placed. Please place an order for the correct Book.");
                            bookISBN.setText("");
                            quantityBook.setText("");
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                    else
                    {

                        textView4.setText("Book not inside Database. Please place an order for a different Book.");
                        bookISBN.setText("");
                        quantityBook.setText("");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });




    }

    //Helper function to calculate price
    public  double calculatePrice(double price, double q) {
        return (price*q)+(price*q*tax)+handlingFee;
    }
}