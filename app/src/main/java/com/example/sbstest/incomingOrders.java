//Project: SBS
//Author: Jose Ron Coka
//File: IncomingOrder.java
//Version: Working Prototype 1
//Date: 04/16/2024

package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class incomingOrders extends AppCompatActivity implements RecyclerViewInterface {

    //Start Array to Store Order Objects
    ArrayList<Order> incomingOrderList = new ArrayList<>();

    //Database references
    DatabaseReference bDatabase;
    DatabaseReference booksRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_incoming_orders);

        populateOrders();

    }

    public void populateOrders(){

        bDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders");

        booksRef = FirebaseDatabase.getInstance().getReference().child("Books");

        //Start Recycler View
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);
        io_RecyclerViewAdapter adapter = new io_RecyclerViewAdapter(this, incomingOrderList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TextView test = findViewById(R.id.bookDetailsTV);
        //Extract all incoming orders from database
        //mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
        //Using Value Event Listener instead of single so the list is updated in case something changes in firebase.
        bDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                incomingOrderList.clear(); // Clear the list before populating again

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    // Extract order data from the snapshot
                    String orderID = orderSnapshot.getKey();
                    //test.setText(orderID);
                    String name = orderSnapshot.child("customerName").getValue(String.class);
                    //test.setText(name);
                    String email = orderSnapshot.child("emailAddress").getValue(String.class);
                    String phone = orderSnapshot.child("customerPhone").getValue(String.class);
                    String address = orderSnapshot.child("customerAddress").getValue(String.class);
                    String book = orderSnapshot.child("bookISBN").getValue(String.class);
                    String title= orderSnapshot.child("bookTitle").getValue(String.class);
                    String price= orderSnapshot.child("bookPrice").getValue(String.class);
                    String author= orderSnapshot.child("bookAuthor").getValue(String.class);
                    String orderCost = orderSnapshot.child("orderCost").getValue(String.class);
                    //String quantityText = orderSnapshot.child("quantityBook").getValue(String.class);
                    int quantity = orderSnapshot.child("quantityBook").getValue(Integer.class);
                    //int quantity = Integer.parseInt(quantityText);
                    String orderDate= orderSnapshot.child("orderDate").getValue(String.class);
                    String orderStatus= orderSnapshot.child("status").getValue(String.class);

                    // Create an Order object with the data extracted
                    Order incomingOrder = new Order(orderID, name, email, phone, address, book, title, author, price, orderCost, quantity, orderDate);

                    //Set incoming order status
                    incomingOrder.setStatus(orderStatus);

                    //Used to extract book data from database, not needed anymore since book info is saved with order.
                    /*
                    booksRef.child(book).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String title = snapshot.child("name").getValue(String.class);
                            incomingOrder.setBookTitle(title);
                            String author = snapshot.child("author").getValue(String.class);
                            incomingOrder.setBookAuthor(author);

                            String price = snapshot.child("price").getValue(String.class);
                            incomingOrder.setBookPrice(price);
                            //adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });

                     */
                    //Add Order object to the list
                    incomingOrderList.add(incomingOrder);
                    //test.setText(incomingOrderList.toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Log.e("Firebase", "Error fetching incoming orders: " + databaseError.getMessage());
            }
        });

    }

    //Handle when order is clicked inside recycler view. Go to Process Order with that Order object info.
    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(incomingOrders.this, processOrders.class);

        intent.putExtra("Order", incomingOrderList.get(position));
        startActivity(intent);

    }
    /*
    @Override
    protected void onResume() {
        super.onResume();
       populateOrders(); // your method to fetch from Firebase
    }

     */
}