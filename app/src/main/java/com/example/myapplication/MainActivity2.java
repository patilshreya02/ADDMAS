package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

// Your imports here

public class MainActivity2 extends AppCompatActivity {

    private Button loginButton, backButton;
    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Find all views
        loginButton = findViewById(R.id.loginb);
        emailEditText = findViewById(R.id.editTextText4);
        passwordEditText = findViewById(R.id.editTextText5);
        backButton = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressbar);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity2.this, "Enter both email and password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                                                Toast.makeText(MainActivity2.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                                                startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity2.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Back button click listener
        backButton.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, MainActivity.class)));
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                startActivity(new Intent(this, MainActivity4.class));
                finish();
            }
        } catch (NullPointerException e) {
            // Handle potential NullPointerException
            e.printStackTrace();
        }
    }
}

