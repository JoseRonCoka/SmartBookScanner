
package com.example.sbstest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



public class MainActivity extends AppCompatActivity {

    Button submitOrderButton;
    Button bookstoreButton;
    String scanned;

    //Importing database
    private static DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        bookstoreButton=findViewById(R.id.bookStoreButton);
        bookstoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(MainActivity.this, BookStore.class);
                startActivity(intent);
            }
        });
        //scanbutton=findViewById(R.id.scanButton);
        /*
        gotoscanButton.setOnClickListener(v->
        {
            setContentView(R.layout.home_screen);
            //setContentView(R.layout.activity_main);
            //Can call from any button and it will work
            //View does not matter.
            //scanCode();
            //scanButton=findViewById(R.id.scanButton);

        });*/

        /*
        scanButton.setOnClickListener(v->
        {
            scanCode();

        });*/
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn the flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);


    }

    //Search is called after scan. Initiates a SearchBoook object, launches query to database based on isbn
    //Extract information from result pass to object. The To String displayed on App.
    //HAVE TO HANDLE NEW BOOK ADD TO DATABASE
    private void search() {

        SearchBook scannedBook = new SearchBook(scanned);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        TextView textView2 = findViewById(R.id.textView2);

        mDatabase.child(scanned).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String title = snapshot.child("name").getValue(String.class);
                    scannedBook.setTitle(title);
                    String author = snapshot.child("author").getValue(String.class);
                    scannedBook.setAuthor(author);
                    String price = snapshot.child("price").getValue(String.class);
                    scannedBook.setPrice(price);

                    //String data= snapshot.getValue().toString();
                    textView2.setText(scannedBook.toString());


                }
                else
                {
                    //Need to Implement Handling. Form Submission of books.
                    textView2.setText("Book not inside Database");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       }


    //Barcode Functionality
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Result");
            scanned = result.getContents();
            builder.setMessage(scanned);
            /*
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            }).show();*/
        }
        search();
    });
}
