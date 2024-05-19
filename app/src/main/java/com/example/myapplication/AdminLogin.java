package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
    Button login, back;
    EditText e4,e5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        login = findViewById(R.id.login);
        e4 = findViewById(R.id.id);
        e5 = findViewById(R.id.pass);
        back = findViewById(R.id.ab1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredID = e4.getText().toString();
                String enteredPassword = e5.getText().toString();

                String ADMIN_ID = "admin";
                String ADMIN_PASSWORD = "Admin123";

                if (enteredID.isEmpty() || enteredPassword.isEmpty()) {
                    // If either field is empty, show a message and prevent login
                    Toast.makeText(AdminLogin.this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
                } else if (enteredID.equals(ADMIN_ID) && enteredPassword.equals(ADMIN_PASSWORD)) {
                    // Successful login, start the desired activity (e.g., SignUp)
                    Intent intent = new Intent(AdminLogin.this, SignUp.class);
                    startActivity(intent);
                } else {
                    // Invalid credentials, show a message or take appropriate action
                    Toast.makeText(AdminLogin.this, "Invalid ID or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}