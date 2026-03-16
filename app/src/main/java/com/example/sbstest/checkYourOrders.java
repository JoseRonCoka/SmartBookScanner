//Project: SBS
//Author: Jose Ron Coka
//File: checkYourOrders.java
//Version: Working Prototype 2
//Date: 11/26/2025


package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

public class checkYourOrders extends AppCompatActivity implements RecyclerViewInterface {

    //Start Array to Store Order Objects
    ArrayList<String> submittedNumList= new ArrayList<>();
    ArrayList<Order> previousOrderList = new ArrayList<>();

    //Database references
    DatabaseReference incomingRef;
    DatabaseReference processedRef;

    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_your_orders);
        //Get submitted order numbers
        extractOrderNum();

        //Populate the recycler view
        populateOrders();

    }

    public void extractOrderNum(){


        //Extract email from Database Authentication and set it into the Edit Text
        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        String uid = userAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("submittedOrders");

        //Extract the order numbers from database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submittedNumList.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Extract order numbers
                    String number = orderSnapshot.getKey();
                    submittedNumList.add(number);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Log.e("Firebase", "Error fetching incoming orders: " + databaseError.getMessage());
            }
        });

    }

    //Function to populate the recycler view. 
    public void populateOrders() {

        incomingRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders");
        processedRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("ProcessedOrders");

        //Start Recycler View
        RecyclerView recyclerView = findViewById(R.id.cRecyclerView);
        io_RecyclerViewAdapter adapter = new io_RecyclerViewAdapter(this, previousOrderList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Extract order data based on order numbers
        //Using Value Event Listener instead of single so the list is updated in case something changes in firebase.
        incomingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                previousOrderList.clear(); // Clear the list before populating again

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {

                    String orderId = orderSnapshot.getKey();
                    if (submittedNumList.contains(orderId)) {
                        // Extract order data from the snapshot
                        //String orderID = orderSnapshot.getKey();
                        //test.setText(orderID);
                        String name = orderSnapshot.child("customerName").getValue(String.class);
                        //test.setText(name);
                        String email = orderSnapshot.child("emailAddress").getValue(String.class);
                        String phone = orderSnapshot.child("customerPhone").getValue(String.class);
                        String address = orderSnapshot.child("customerAddress").getValue(String.class);
                        String book = orderSnapshot.child("bookISBN").getValue(String.class);
                        String title = orderSnapshot.child("bookTitle").getValue(String.class);
                        String price = orderSnapshot.child("bookPrice").getValue(String.class);
                        String author = orderSnapshot.child("bookAuthor").getValue(String.class);
                        String orderCost = orderSnapshot.child("orderCost").getValue(String.class);
                        //String quantityText = orderSnapshot.child("quantityBook").getValue(String.class);
                        int quantity = orderSnapshot.child("quantityBook").getValue(Integer.class);
                        //int quantity = Integer.parseInt(quantityText);
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderStatus = orderSnapshot.child("status").getValue(String.class);

                        // Create an Order object with the data extracted
                        Order incomingOrder = new Order(orderId, name, email, phone, address, book, title, author, price, orderCost, quantity, orderDate);

                        //Set incoming order status
                        incomingOrder.setStatus(orderStatus);
                        //Add Order object to the list
                        previousOrderList.add(incomingOrder);
                        //test.setText(incomingOrderList.toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Log.e("Firebase", "Error fetching incoming orders: " + databaseError.getMessage());
            }
        });

        processedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //previousOrderList.clear(); // Clear the list before populating again

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {

                    String orderId = orderSnapshot.getKey();
                    if (submittedNumList.contains(orderId)) {
                        // Extract order data from the snapshot
                        //String orderID = orderSnapshot.getKey();
                        //test.setText(orderID);
                        String name = orderSnapshot.child("customerName").getValue(String.class);
                        //test.setText(name);
                        String email = orderSnapshot.child("emailAddress").getValue(String.class);
                        String phone = orderSnapshot.child("customerPhone").getValue(String.class);
                        String address = orderSnapshot.child("customerAddress").getValue(String.class);
                        String book = orderSnapshot.child("bookISBN").getValue(String.class);
                        String title = orderSnapshot.child("bookTitle").getValue(String.class);
                        String price = orderSnapshot.child("bookPrice").getValue(String.class);
                        String author = orderSnapshot.child("bookAuthor").getValue(String.class);
                        String orderCost = orderSnapshot.child("orderCost").getValue(String.class);
                        //String quantityText = orderSnapshot.child("quantityBook").getValue(String.class);
                        int quantity = orderSnapshot.child("quantityBook").getValue(Integer.class);
                        //int quantity = Integer.parseInt(quantityText);
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderStatus = orderSnapshot.child("status").getValue(String.class);

                        // Create an Order object with the data extracted
                        Order incomingOrder = new Order(orderId, name, email, phone, address, book, title, author, price, orderCost, quantity, orderDate);

                        //Set incoming order status
                        incomingOrder.setStatus(orderStatus);
                        //Add Order object to the list
                        previousOrderList.add(incomingOrder);
                        //test.setText(incomingOrderList.toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Log.e("Firebase", "Error fetching incoming orders: " + databaseError.getMessage());
            }
        });

        //adapter.notifyDataSetChanged();

    }

    //Handle when order is clicked inside recycler view. Go to Display Order with that Order object info.
    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(checkYourOrders.this, displayOrder.class);

        intent.putExtra("Order", previousOrderList.get(position));
        startActivity(intent);

    }
}