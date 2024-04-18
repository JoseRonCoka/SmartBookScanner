//Project: SBS
//Author: Jose Ron Coka
//File: ProcessOrders.java
//Version: Working Prototype 1
//Date: 04/16/2024

package com.example.sbstest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class processOrders extends AppCompatActivity {

    //Initialize UI elements and variables
    TextView orderID;
    TextView orderDetails;

    TextView bookDetails;

    EditText toScanNum;

    EditText scannedNum;

    Button scanProcessButton;

    String scanned;


    int copiesToScan=0;
    int copiesScanned=0;
    String isbn="";

    Order orderToProcess;
    SearchBook bookToProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_orders);

        //Extract Order object data passed on intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Order")) {

            //Extract Order object. Can access data inside order using getters
            orderToProcess = (Order) intent.getSerializableExtra("Order");

            //Set OrderID UI
            orderID = findViewById(R.id.idTextView);
            orderID.setText("Order ID: " + orderToProcess.getOrderID());

            //Set OrderDetails in UI
            orderDetails = findViewById(R.id.orderDetailsTextView);
            orderDetails.setText(orderToProcess.toString());

            //Set Book Details
            isbn = orderToProcess.getBookISBN();
            bookDetails = findViewById(R.id.bookDetailsTV);
            bookDetails.setText(orderToProcess.displayBookInfo());

            //Set Num Copies to scan
            toScanNum = findViewById((R.id.toScanNum));
            toScanNum.setText(orderToProcess.getQuantityBook());
            copiesToScan=Integer.parseInt(orderToProcess.getQuantityBook());


            //Set Num scanned copies
            scannedNum= findViewById((R.id.scannedNum));
            scannedNum.setText("0");
        }

        //Set Scanner Functionality
        scanProcessButton= findViewById((R.id.processScan));
        scanProcessButton.setOnClickListener(v->
        {
            scanCode();
        });


    }

    //ScanCode function sets scanner functionality
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn the flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }

    //Barcode Functionality if something is scanned calls check function
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            scanned = result.getContents();
        }
        else{
            return;
        }
        check();
    });

    //Check function validates the scan
    private void check(){

        //Compares scanned string with isbn of book
        if (scanned.equals(isbn)){
            copiesScanned++;
            scannedNum.setText(Integer.toString(copiesScanned));
        }
        else{
            orderID.setText("Wrong Book, scan again");
        }

        //Once the number of Copies scanned matches the number of socpies to scan then the Order can be finalized
        if(copiesToScan == copiesScanned){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(processOrders.this);
            alertDialogBuilder.setTitle("All elements for order have been Scanned");
            alertDialogBuilder.setMessage("Do you want to complete the order and Process Payment?");
            alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                orderID.setText("Order was completed. Good Job!");

                //Database references
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders"); // Reference to the "Orders" category
                DatabaseReference processRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("ProcessedOrders");

                //Acces Database to Move Order from Incoming Orders to Processed Orders
               mDatabase.child(orderToProcess.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Object data = snapshot.getValue();
                                // Write the data to the destination location
                                processRef.child(orderToProcess.getOrderID()).setValue(data);
                                // Delete the data from the source location.
                                snapshot.getRef().removeValue();

                                //Intent changed so App goes back to Incoming Orders and cannot return to order
                                Intent intent = new Intent(processOrders.this, incomingOrders.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();

                            } else {
                                //Code will never get here
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            });
            alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                orderID.setText("Order was not completed. Start Scanning again");
                copiesScanned=0;
                 scannedNum.setText(copiesScanned);
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


    }




    /*Extract database book info
            public void extractBook (String isbn) {


                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Books");

                mDatabase.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            String title = snapshot.child("name").getValue(String.class);
                            bookToProcess.setTitle(title);
                            String author = snapshot.child("author").getValue(String.class);
                            bookToProcess.setAuthor(author);
                            String price = snapshot.child("price").getValue(String.class);
                            bookToProcess.setPrice(price);

                        }
                        else
                        {
                            //Code will never reach here since order is only submitted if book in database.

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

     */
}