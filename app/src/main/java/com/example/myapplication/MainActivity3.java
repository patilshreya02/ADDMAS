package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity3 extends AppCompatActivity {
    Button loginButton, backButton;
    EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity6.class);
            startActivity(intent);
            finish(); // Finish the current activity to prevent going back to it from MainActivity6
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main3);

        loginButton = findViewById(R.id.loginb);
        emailEditText = findViewById(R.id.editTextText4);
        passwordEditText = findViewById(R.id.editTextText5);
        backButton = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressbar2);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance(); // Initialize Firestore instance

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity3.this, "Enter both email and password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity3.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    userId = user.getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(userId);
                                    documentReference.addSnapshotListener(MainActivity3.this, new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value != null && value.exists()) {
                                                Toast.makeText(MainActivity3.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity6.class);
                                                startActivity(intent);
                                                finish(); // Finish the current activity;
                                            } else {
                                                // Handle the case when document does not exist or has null data
                                                // For example, set a default value for textView1;
                                                Toast.makeText(MainActivity3.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(MainActivity3.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this, MainActivity6.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }
}
