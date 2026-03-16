//Project: SBS
//Author: Jose Ron Coka
//File: MainActivity
//Version: Working Prototype 1
//Date: 04/16/2024


package com.example.sbstest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    //Initialize Global Variables
    String userTypeSignIn;
    String emailSignInText;
    String passwordSignInText;

    //Initialize UI objects

    Button signInButton;
    Button signUpButton;
    EditText emailSignIn;
    EditText passwordSignIn;

    RadioGroup radioGroupOptions;


    //Initialize Buttons
    Button placeOrderButton;
    Button bookstoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        signIn();
        //placeOrderButton=findViewById(R.id.placeOrderButton);
        //bookstoreButton=findViewById(R.id.bookStoreButton);
        /*
        //Button to go to Bookstore Side of App
        bookstoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(MainActivity.this, BookStore.class);
                startActivity(intent);
            }
        });

        //Button to go to Submit Order Activity
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the new view
                Intent intent = new Intent(MainActivity.this, SubmitOrder.class);
                startActivity(intent);
            }
        });

         */

    }

    private void signIn(){

        //Initialize UI elements
        signInButton=findViewById(R.id.signInButton);
        signUpButton=findViewById(R.id.signUpButton);
        emailSignIn=findViewById(R.id.emailSignIn);
        passwordSignIn=findViewById(R.id.passwordSignIn);


        //String userTypeSignIn;

        radioGroupOptions = findViewById(R.id.radioGroupUser);

        radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    Toast.makeText(MainActivity.this, "Selected: " + selectedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                    userTypeSignIn=selectedRadioButton.getText().toString();
                }
            }
        });

        signUpButton.setOnClickListener(v->
        {
            emailSignInText= emailSignIn.getText().toString().trim();
            passwordSignInText = passwordSignIn.getText().toString().trim();

            if (emailSignInText.isEmpty() || passwordSignInText.isEmpty() || userTypeSignIn.isEmpty()) {
                Toast.makeText(this, "Please fill out the Email and Password and select your User type in order to Sign Up", Toast.LENGTH_SHORT).show();
            }
            else if(passwordSignInText.length()<6){
                Toast.makeText(this, "Password has to be at least 6 characters long", Toast.LENGTH_SHORT).show();
            }
            else{
                FirebaseAuth userAuth = FirebaseAuth.getInstance();
                DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                userAuth.createUserWithEmailAndPassword(emailSignInText, passwordSignInText)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = userAuth.getCurrentUser().getUid();
                                // Save role
                                userDatabase.child(uid).child("role").setValue(userTypeSignIn);
                                userDatabase.child(uid).child("email").setValue(emailSignInText);

                                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        signInButton.setOnClickListener(v->
        {
            emailSignInText= emailSignIn.getText().toString().trim();
            passwordSignInText = passwordSignIn.getText().toString().trim();

            if (emailSignInText.isEmpty() || passwordSignInText.isEmpty() || userTypeSignIn.isEmpty()) {
                Toast.makeText(this, "Please fill out the Email and Password and select your User type in order to Sign In.", Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth userAuth = FirebaseAuth.getInstance();
                DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                userAuth.signInWithEmailAndPassword(emailSignInText, passwordSignInText)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = userAuth.getCurrentUser().getUid();
                                userDatabase.child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String role = snapshot.getValue(String.class);

                                        if (role.equals("Customer")) {
                                            //startActivity(new Intent(MainActivity.this, Customer.class));
                                            // User logged in successfully
                                            Intent intent = new Intent(MainActivity.this, Customer.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            //startActivity(new Intent(MainActivity.this, SubmitOrder.class));
                                        } else if (role.equals("Bookstore")) {
                                            //startActivity(new Intent(MainActivity.this, BookStore.class));
                                            Intent intent = new Intent(MainActivity.this, BookStore.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        finish(); // prevents going back to login
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Login failed. Check your Password and try again.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this,
                                        "Login failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });


    }
}
