//Project: SBS
//Author: Jose Ron Coka
//File: Inquiry.java
//Version: Working Prototype 1
//Date: 04/16/2024



package com.example.sbstest;

import androidx.activity.result.ActivityResultLauncher;
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
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Inquiry extends AppCompatActivity {

    //Start Scan Button and Scanned String
    String scanned;
    String isbnInput;
    Button scanButton;

    Button submitInquiryButton;

    EditText isbnTextInput;

    TextView displayText;

    private Scanner scannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inquiry);

        //Start Edittext
        isbnTextInput=findViewById(R.id.isbnInquiryInput);

        //Start Textview
        displayText=findViewById(R.id.displayText);

        //Start Scan Button
        scanButton=findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v->
        {
            //scanCode();
            //scannerHelper.scanCode();
            //search(scannerHelper.getIsbn());
            scannerHelper = new Scanner();
            barLauncher.launch( scannerHelper.getScanOptions());
        });

        //Start Submit Inquiry Button
        submitInquiryButton=findViewById(R.id.submitInquiryButton);
        //Set submit button functionality
        submitInquiryButton.setOnClickListener(v->
        {
            isbnInput= isbnTextInput.getText().toString().trim();
            if (isbnInput.length()==13) {
                search(isbnInput);
            }
            else{
                displayText.setText("ISBN is invalid. Please make sure its a valid ISBN 13 code and Try Again.");
            }
        });
    }
    //Search function initiates a SearchBoook object, launches query to database based on isbn
    public void search(String scanned) {

        //Initialize SearchBook Object
        SearchBook scannedBook = new SearchBook(scanned);

        //Database Reference
        DatabaseReference bDatabase;
        bDatabase = FirebaseDatabase.getInstance().getReference().child("Books");

        //Database extraction
        bDatabase.child(scanned).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    //Extract and set SearchBook Values
                    String title = snapshot.child("title").getValue(String.class);
                    scannedBook.setTitle(title);
                    String author = snapshot.child("author").getValue(String.class);
                    scannedBook.setAuthor(author);
                    String price = snapshot.child("price").getValue(String.class);
                    scannedBook.setPrice(price);
                    int bQuantity= snapshot.child("quantity").getValue(Integer.class);
                    scannedBook.setQuantity(bQuantity);

                    //Set ToString Result to View.
                    displayText.setText(scannedBook.toString());
                }
                else
                {
                    //Handling if Book not inside Database
                    displayText.setText("Book not inside Database");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayText.setText("Error Accesing the Database Please Try Again");
            }
        });

    }

    //Barcode Functionality. Calls search function if something is scanned.
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            scanned = result.getContents();
        }
        else{
            return;
        }
        //search(scanned);
        isbnTextInput.setText(scanned);
    });
}