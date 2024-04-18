//Project: SBS
//Author: Jose Ron Coka
//File: Inquiry.java
//Version: Working Prototype 1
//Date: 04/16/2024



package com.example.sbstest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
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
    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inquiry);

        //Start Scan Button
        scanButton=findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v->
        {
            scanCode();

        });
    }

    //scanCode function starts the Scanner
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn the flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
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
        search();
    });

    //Search is called after scan. Initiates a SearchBoook object, launches query to database based on isbn
    public void search() {

        //Initialize SearchBook Object
        SearchBook scannedBook = new SearchBook(scanned);

        //Database Reference
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Books");
        TextView textView2 = findViewById(R.id.textView2);

        //Database extraction
        mDatabase.child(scanned).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    //Extract and set SearchBook Values
                    String title = snapshot.child("name").getValue(String.class);
                    scannedBook.setTitle(title);
                    String author = snapshot.child("author").getValue(String.class);
                    scannedBook.setAuthor(author);
                    String price = snapshot.child("price").getValue(String.class);
                    scannedBook.setPrice(price);

                    //Set ToString Result to View.
                    textView2.setText(scannedBook.toString());


                }
                else
                {
                    //Handling if Book not inside Database
                    textView2.setText("Book not inside Database");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textView2.setText("Error Accesing the Database Please Try Again");
            }
        });

    }
}