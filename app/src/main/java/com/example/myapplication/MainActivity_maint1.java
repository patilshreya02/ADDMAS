package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity_maint1 extends AppCompatActivity {

    Button next, logout; // Added logout button
    CheckBox c1, c2, c3, c4;
    private CheckBox.OnCheckedChangeListener checkBoxChangeListener; // Move the initialization here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_maint1);

        next = findViewById(R.id.next1);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        logout = findViewById(R.id.button7); // Initialize logout button with assumed ID "button7"
        next.setEnabled(false);
        next.setBackground(getDrawable(R.color.black));

        // Initialize checkBoxChangeListener before setting it
        checkBoxChangeListener = (buttonView, isChecked) -> {
            // Check if all checkboxes are checked
            boolean allChecked = c1.isChecked() && c2.isChecked() && c3.isChecked()
                    && c4.isChecked();
            next.setBackground(getDrawable(R.color.blue));
            // Enable or disable the submit button based on the condition
            next.setEnabled(allChecked);

        };

        c1.setOnCheckedChangeListener(checkBoxChangeListener);
        c2.setOnCheckedChangeListener(checkBoxChangeListener);
        c3.setOnCheckedChangeListener(checkBoxChangeListener);
        c4.setOnCheckedChangeListener(checkBoxChangeListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity_maint1.this, MainActivity2m.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity_maint1.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}