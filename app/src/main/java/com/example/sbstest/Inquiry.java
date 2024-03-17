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

    String scanned;
    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        scanButton=findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v->
        {
            scanCode();

        });
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


    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn the flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
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
        search();
    });
}