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

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Order")) {

            //Extract Order object. Can access data inside order using getters
            orderToProcess = (Order) intent.getSerializableExtra("Order");

            orderID = findViewById(R.id.idTextView);
            orderID.setText("Order ID: " + orderToProcess.getOrderID());

            orderDetails = findViewById(R.id.orderDetailsTextView);
            orderDetails.setText(orderToProcess.toString());


            isbn = orderToProcess.getBookISBN();
            //bookToProcess = new SearchBook(isbn);
            //extractBook (isbn);


            //Set Book Details
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

        scanProcessButton= findViewById((R.id.processScan));

        scanProcessButton.setOnClickListener(v->
        {
            scanCode();

        });


    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn the flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }

    private void check(){

        if (scanned.equals(isbn)){
            copiesScanned++;
            scannedNum.setText(Integer.toString(copiesScanned));
        }
        else{
            orderID.setText("Wrong Book, scan again");
        }

        if(copiesToScan == copiesScanned){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(processOrders.this);
            alertDialogBuilder.setTitle("All elements for order have been Scanned");
            alertDialogBuilder.setMessage("Do you want to complete the order and Process Payment?");
            alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                orderID.setText("Order was completed. Good Job");


                //ADD Database moving order from Incoming to Processed.
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child("IncomingOrders"); // Reference to the "Orders" category
                DatabaseReference processRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("ProcessedOrders");
                //check database  for isbn before posting order
               mDatabase.child(orderToProcess.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Object data = snapshot.getValue();
                                // Write the data to the destination location
                                processRef.child(orderToProcess.getOrderID()).setValue(data);
                                // Delete the data from the source location. Make sure copying occurs first
                                snapshot.getRef().removeValue();

                                //Go back to Incoming Orders

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

    //Barcode Functionality
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            //AlertDialog.Builder builder = new AlertDialog.Builder(Inquiry.this);
            //builder.setTitle("Result");
            scanned = result.getContents();
            //builder.setMessage(scanned);
            /*
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            }).show();*/
        }
        check();
    });


    //Extract database book info
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
}