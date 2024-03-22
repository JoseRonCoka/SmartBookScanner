package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubmitOrder extends AppCompatActivity {

    Button submitOrderButton;

    EditText customerName;
    EditText emailAddress;

    EditText customerPhone;
    EditText customerAddress;
    EditText bookISBN;

    EditText quantityBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);

        submitOrderButton = findViewById(R.id.submitButton);

        //EditText input
        customerName= findViewById(R.id.nameInput);
        emailAddress= findViewById(R.id.emailInput);
        customerPhone= findViewById(R.id.phoneInput);
        customerAddress= findViewById(R.id.addressInput);
        bookISBN= findViewById(R.id.isbnInput);
        quantityBook= findViewById(R.id.quantityInput);

        TextView textView4 = findViewById(R.id.textView4);

        //Database reference
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Books");

        submitOrderButton.setOnClickListener(v->
        {
            String name = customerName.getText().toString();
            String email = emailAddress.getText().toString();
            String phone = customerPhone.getText().toString();
            String address = customerAddress.getText().toString();
            String isbn = bookISBN.getText().toString();
            String quantity = quantityBook.getText().toString();
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders"); // Reference to the "Orders" category
            //check database  for isbn before posting order
            mDatabase.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        Order customerOrder =new Order(name,email, phone,address,isbn,quantity);
                        //textView4.setText(customerOrder.toString());
                        String orderId = ordersRef.push().getKey(); // Generate unique key for order
                        ordersRef.child(orderId).setValue(customerOrder) //Saves order for that key
                        //ordersRef.push().setValue(customerOrder) // Save order under "Orders" category
                                .addOnSuccessListener(aVoid -> {
                                    textView4.setText("Order submitted successfully, your Order Number: "+orderId);
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
                    }
                    else
                    {
                        //Need to Implement Handling. Form Submission of books.
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
}