//Project: SBS
//Author: Jose Ron Coka
//File: SubmitBook.java
//Version: Working Prototype 2
//Date: 09/18/2025



package com.example.sbstest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubmitBook extends AppCompatActivity {


    //Initialize View Elements

    Button scanInputButton;
    Button submitBookButton;

    EditText isbnInput;

    EditText bookTitleInput;
    EditText authorInput;
    EditText priceInput;
    EditText availableCopies;

    TextView submitBookTitle;

    private Scanner scannerHelper;
    String scanned;
    String isbn ;
    String title ;
    String author ;
    String price ;
    String qText ;

    int quantity;

    DatabaseReference bDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_book);

        //Start Id elements
        scanInputButton= findViewById(R.id.scanButtonSubmitBook);
        submitBookButton= findViewById(R.id.submitBookButton);
        isbnInput = findViewById(R.id.bookIsbnInput);
        bookTitleInput= findViewById(R.id.bookTitleInput);
        authorInput = findViewById(R.id.bookAuthorInput);
        priceInput= findViewById(R.id.bookPriceInput);
        availableCopies= findViewById(R.id.availableCopiesInput);
        submitBookTitle= findViewById(R.id.submitBookTitle);

        //Database reference  to Book side.
        bDatabase = FirebaseDatabase.getInstance().getReference().child("Books");

        //Set Scan button functionality
        scanInputButton.setOnClickListener(v->
        {
            //scanCode();
            //scannerHelper.scanCode();
            //search(scannerHelper.getIsbn());
            scannerHelper = new Scanner();
            barLauncher.launch( scannerHelper.getScanOptions());
        });

        //Set Submit button functionality
        submitBookButton.setOnClickListener(v->
        {
            isbn = isbnInput.getText().toString().trim();
            title = bookTitleInput.getText().toString().trim();
            author = authorInput.getText().toString().trim();
            price = priceInput.getText().toString().trim();
            qText = availableCopies.getText().toString().trim();
            quantity = Integer.parseInt(qText);

            //Sentinel

            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || price.isEmpty() || qText.isEmpty()) {
                submitBookTitle.setText("Book information is not complete. Please check.");
            }
            else if(isbn.length()!=13){
                submitBookTitle.setText("ISBN is invalid. Please make sure its a valid ISBN 13 code and Try Again.");
            }
            else{
                checkBook();
            }
        });
    }

    //Function that adds the input book into database
    public void checkBook(){
        //Check if Book is inside Database before submittng Book
        bDatabase.child(isbn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Book is already on database, display message to inform user.
                    submitBookTitle.setText("Book with the ISBN inputed is already in database. Please check.");
                }
                else
                {
                    addBook();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Adds books into database
    public void addBook(){
        //Alert Dialog for User to confirm order
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubmitBook.this);
        alertDialogBuilder.setTitle("Confirm Book Submission:");
        alertDialogBuilder.setMessage("Do you want to submit the Book '"+title+"' by: '"+author+"' with the ISBN: '"+isbn+"'. With a price of $"+price+" and '"+qText+"' available copies?");
        alertDialogBuilder.setPositiveButton("YES", (dialogInterface, i) -> {

            //Create Book object from the input info
            SearchBook submitBook = new SearchBook(isbn, title, author, price, quantity);

            //Push the order object into the database
            bDatabase.child(isbn).setValue(submitBook)
                    .addOnSuccessListener(aVoid -> {
                        //textView4.setText("Order submitted successfully, your Order Details: "+customerOrder.toString()+" Order Number: " + orderId);
                        submitBookTitle.setText("The Book '"+title+"' by: '"+author+"' with the ISBN: '"+isbn+"'. With a price of $"+price+" and '"+qText+"' available copies has been added to the Database");
                        // Clear input fields if needed
                        isbnInput.setText("");
                        bookTitleInput.setText("");
                        authorInput.setText("");
                        priceInput.setText("");
                        availableCopies.setText("");
                    })
                    .addOnFailureListener(e -> {
                        submitBookTitle.setText("Error submitting book.");
                    });
        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            submitBookTitle.setText("Book was not added to the Database. Please enter again.");
            isbnInput.setText("");
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    //Barcode Functionality. To put ISBN into isbnInput
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            scanned = result.getContents();
        }
        else{
            return;
        }
        isbnInput.setText(scanned);
    });
}