package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class incomingOrders extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<Order> incomingOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_orders);

        //Start Recycler View
        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        //Save into array of Order objects
        //ArrayList<Order> incomingOrderList = new ArrayList<>();

        //Extract all incoming orders from database
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders");
        DatabaseReference booksRef;
        booksRef = FirebaseDatabase.getInstance().getReference().child("Books");

        io_RecyclerViewAdapter adapter = new io_RecyclerViewAdapter(this, incomingOrderList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //TextView test = findViewById(R.id.bookDetailsTV);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //incomingOrderList.clear(); // Clear the list before populating again

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
                    String orderCost = orderSnapshot.child("orderCost").getValue(String.class);
                    String quantity = orderSnapshot.child("quantityBook").getValue(String.class);

                    // Create an Order object and add it to the list
                    Order incomingOrder = new Order(orderID, name, email, phone, address, book, orderCost, quantity);


                    booksRef.child(book).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String title = snapshot.child("name").getValue(String.class);
                            incomingOrder.setBookTitle(title);
                            String author = snapshot.child("author").getValue(String.class);
                            incomingOrder.setBookAuthor(author);

                            String price = snapshot.child("price").getValue(String.class);
                            incomingOrder.setBookPrice(price);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });


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

        /*
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //incomingOrderList.clear(); // Clear the list before populating again

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
                    String quantity = orderSnapshot.child("quantityBook").getValue(String.class);

                    // Create an Order object and add it to the list
                    Order incomingOrder = new Order(orderID, name, email, phone, address, book, quantity);

                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //incomingOrderList.clear(); // Clear the list before populating again

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
                                String quantity = orderSnapshot.child("quantityBook").getValue(String.class);

                                // Create an Order object and add it to the list
                                Order incomingOrder = new Order(orderID, name, email, phone, address, book, quantity);


                                booksRef.child(book).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String title = snapshot.child("name").getValue(String.class);
                                        incomingOrder.bookObject.setTitle(title);
                                        String author = snapshot.child("author").getValue(String.class);
                                        incomingOrder.bookObject.setAuthor(author);
                                        String price = snapshot.child("price").getValue(String.class);
                                        incomingOrder.bookObject.setPrice(price);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle error
                                    }
                                });



                                //test.setText(incomingOrder.toString());
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(incomingOrders.this, processOrders.class);

        intent.putExtra("Order", incomingOrderList.get(position));
        startActivity(intent);

    }
}