//Project: SBS
//Author: Jose Ron Coka
//File: BookStore.java
//Version: Working Prototype 1
//Date: 04/16/2024


package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import androidx.appcompat.app.AlertDialog;

import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubmitOrder extends AppCompatActivity {

    //Initialize View Elements
    Button submitOrderButton;

    EditText customerName;
    EditText emailAddress;

    EditText customerPhone;
    EditText customerAddress;
    EditText bookISBN;

    EditText quantityBook;

    //Database reference  to Book side.
    DatabaseReference bDatabase;
    DatabaseReference ordersRef;

    TextView textView4;
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

        textView4 = findViewById(R.id.textView4);

        //Database reference  to Book side.
        //DatabaseReference mDatabase;
        bDatabase = FirebaseDatabase.getInstance().getReference().child("Books");
        //Database reference to Order side
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders"); // Reference to the "Orders" category

        //On CLick of submit button information in input fields is saved
        submitOrderButton.setOnClickListener(v->
        {
            String name = customerName.getText().toString();
            String email = emailAddress.getText().toString();
            String phone = customerPhone.getText().toString();
            String address = customerAddress.getText().toString();
            String isbn = bookISBN.getText().toString();
            String orderQText = quantityBook.getText().toString();

            //check database  for isbn before posting order
            bDatabase.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {

                        //Extract book info from database, name and price
                        String title = snapshot.child("title").getValue(String.class);
                        String p= snapshot.child("price").getValue(String.class);
                        String author= snapshot.child("author").getValue(String.class);
                        //String quantityText= snapshot.child("quantity").getValue(String.class);
                        int bQuantity= snapshot.child("quantity").getValue(Integer.class);

                        double price = Double.parseDouble(p);
                        int orderQ= Integer.parseInt(orderQText);

                        //Check if enough copies available
                        if (bQuantity < orderQ ){
                            textView4.setText("Oh no! There are not enough available copies for the book you ordered. There are "+bQuantity+" available copies.");
                            bookISBN.setText("");
                            quantityBook.setText("");
                        }
                        else{

                            //Calculate Total order price
                            String totalPrice = String.format("%.2f", calculatePrice(price,orderQ));

                            //Alert Dialog for User to confirm order
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitOrder.this);
                            alertDialogBuilder.setTitle("Confirm Order");
                            alertDialogBuilder.setMessage("Do you want to place an order for: '"+orderQText+ "' copies of '" + title + " by: "+author+" , with an individual price of $"+p+" for total order price of $"+totalPrice+"'?");
                            alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {

                                //Adjust Available copies number on database
                                /*
                                int updatedQ= quantity-orderQ;
                                //Test that new quantity is added to the right branch, not everything should be erased. 
                                bDatabase.child(isbn).child("quantity").setValue(updatedQ)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firebase", "Update of available book quantity successful!");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firebase", "Update of available book quantity failed", e);
                                        });

                                 */

                                //Add Transaction to ensure never -1 on quantity. Users cannot make an order at the same time.
                                tranSubmitOrder(isbn, orderQ, success -> {
                                    if (success == true) {
                                        submitOrderToDatabase(name, email, phone, address,isbn, title, author,p, totalPrice,orderQ);
                                    }
                                    else{
                                        Toast.makeText(SubmitOrder.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                /*
                                //ISSUE not getting date, shows up as null
                                LocalDate myDateObj = LocalDate.now();
                                //String subDate = objDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String formattedDate = myDateObj.format(myFormatObj);
                                //subDate= subDate + System.out.println(objDate);

                                //UPGRADE CHANGE SO GENERATES UNIQUE READABLE NUMBER, ONLY NUMBERS
                                //String orderId = ordersRef.push().getKey(); // Generate unique key for order

                                //ADD WAY TO CHECK IF NUMBER ALREADY IN DATABASE
                                String orderId=checkIfExists(generateOrderNumber());
                                //Create Order object from the input info
                                Order customerOrder = new Order(orderId, name, email, phone, address, isbn, title, author, p, totalPrice, orderQ, formattedDate);

                                //Push the order object into the database
                                ordersRef.child(orderId).setValue(customerOrder)
                                        .addOnSuccessListener(aVoid -> {
                                            //textView4.setText("Order submitted successfully, your Order Details: "+customerOrder.toString()+" Order Number: " + orderId);
                                            textView4.setText("Order submitted successfully on "+formattedDate+", your Order Number: " + orderId+" with total order price of $"+customerOrder.getOrderCost());
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

                                 */
                            });
                            alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                                textView4.setText("Order was not placed. Please place an order for the correct Book.");
                                bookISBN.setText("");
                                quantityBook.setText("");
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
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

    //Submit Order function. Takes order data and submits into the database using Transaction function.
    public void submitOrderToDatabase (String name, String email, String phone, String address, String isbn, String title, String author, String p, String totalPrice, int orderQ){

        LocalDate myDateObj = LocalDate.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = myDateObj.format(myFormatObj);

        //Generate random number and check if already on database.
        String orderId=checkIfExists(generateOrderNumber());
        //Create Order object from the input info
        Order customerOrder = new Order(orderId, name, email, phone, address, isbn, title, author, p, totalPrice, orderQ , formattedDate);

        //Push the order object into the database
        ordersRef.child(orderId).setValue(customerOrder)
                .addOnSuccessListener(aVoid -> {
                    //textView4.setText("Order submitted successfully, your Order Details: "+customerOrder.toString()+" Order Number: " + orderId);
                    textView4.setText("Order submitted successfully on "+formattedDate+", your Order Number: " + orderId+" with total order price of $"+customerOrder.getOrderCost());
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

    //Transaction Function to update book quantity
    public void tranSubmitOrder (String isbn, int orderQ, OnTransactionResultListener listener ){

        // Adjust available copies number in the database using a transaction
        //DatabaseReference quantityRef = bDatabase.child(isbn)

        // DEBUG: log the structure of the node before the transaction
        bDatabase.child(isbn).get().addOnSuccessListener(snapshot -> {
            Log.d("FirebaseDebug", "Snapshot exists: " + snapshot.exists());
            Log.d("FirebaseDebug", "Snapshot value:\n" + snapshot.getValue());
        }).addOnFailureListener(e -> {
            Log.e("FirebaseDebug", "Error getting snapshot", e);
        });

        bDatabase.child(isbn).child("quantity").get().addOnSuccessListener(snapshot -> {
            Log.d("FirebaseDebug", "Snapshot exists: " + snapshot.exists());
            Log.d("FirebaseDebug", "Snapshot value:\n" + snapshot.getValue());
        }).addOnFailureListener(e -> {
            Log.e("FirebaseDebug", "Error getting snapshot", e);
        });

        //Transaction
        bDatabase.child(isbn).child("quantity").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Log.d("Firebase", "Current data: " + currentData.getValue());
                Integer currentQuantity = currentData.getValue(Integer.class);
                //Number currentValue = currentData.getValue(Number.class);
                Log.d("Firebase", "Current data: " + currentQuantity);

                //Transactions check local cache for value which can lead for it being null on first run.
                //The following code skips it without returning to the main code.
                //Transaction will run again and check actual value on database, then we get real value.l
                if (currentQuantity == null) {
                    return Transaction.success(currentData);
                }


                int updatedQ = currentQuantity - orderQ;
                if (updatedQ < 0) {
                    // Prevent negative quantities
                    Log.w("Firebase", "Not enough stock to complete the order.");
                    return Transaction.abort();
                }

                // Update with the new quantity
                currentData.setValue(updatedQ);
                return Transaction.success(currentData);
            }


            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {
                if (error != null) {
                    Log.e("Firebase", "Transaction failed: " + error.getMessage());
                    listener.onTransactionResult(false);
                } else if (committed) {
                    Log.d("Firebase", "Book quantity updated successfully!");
                    listener.onTransactionResult(true);
                } else {
                    Log.w("Firebase", "Transaction aborted (possibly due to low stock).");
                    listener.onTransactionResult(false);
                }
            }
        });

    }

    //Helper function to return boolean when transaction is complete.
    public interface OnTransactionResultListener {
        void onTransactionResult(boolean success);
    }

    //Helper function to calculate price
    public  double calculatePrice(double price, double q) {
        return (price*q)+(price*q*tax)+handlingFee;
    }

    //Function to generate a random order number
    public String generateOrderNumber(){
        Random random = new Random();
        int orderNumber = 10000000 + random.nextInt(90000000); // 8 digits
        String orderId = String.valueOf(orderNumber);
        return orderId;
    }

    //Function to check if order number generated already on database as incoming or processed order.
    public String checkIfExists(String numberToCheck){
        DatabaseReference databaseCheckIncoming;
        databaseCheckIncoming = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders");
        databaseCheckIncoming.child(numberToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retry with a new ID
                    String newOrderNumber = generateOrderNumber();
                    checkIfExists(newOrderNumber);
                } else {
                    //Returns
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking ID: " + error.getMessage());
            }
        });

        DatabaseReference databaseCheckProcessed;
        databaseCheckProcessed = FirebaseDatabase.getInstance().getReference().child("Orders").child("ProcessedOrders");
        databaseCheckProcessed.child(numberToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retry with a new ID
                    String newOrderNumber = generateOrderNumber();
                    checkIfExists(newOrderNumber);
                } else {
                    //Returns
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking ID: " + error.getMessage());
            }
        });
        return numberToCheck;
    }
}