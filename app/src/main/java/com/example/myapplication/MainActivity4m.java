package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity4m extends AppCompatActivity {

    Button next,logout;
    CheckBox c1, c2, c3, c4, c5, c6;
    private CheckBox.OnCheckedChangeListener checkBoxChangeListener; // Move the initialization here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_activity4m);

        next = findViewById(R.id.next1);
        logout = findViewById(R.id.button7);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        next.setEnabled(false);

        checkBoxChangeListener = (buttonView, isChecked) -> {
            // Check if all checkboxes are checked
            boolean allChecked = c1.isChecked() && c2.isChecked() && c3.isChecked()
                    && c4.isChecked() && c5.isChecked() && c6.isChecked();

            // Enable or disable the submit button based on the condition
            next.setEnabled(allChecked);
        };

        c1.setOnCheckedChangeListener(checkBoxChangeListener);
        c2.setOnCheckedChangeListener(checkBoxChangeListener);
        c3.setOnCheckedChangeListener(checkBoxChangeListener);
        c4.setOnCheckedChangeListener(checkBoxChangeListener);
        c5.setOnCheckedChangeListener(checkBoxChangeListener);
        c6.setOnCheckedChangeListener(checkBoxChangeListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity4m.this, MainActivity5m.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i1 = new Intent(MainActivity4m.this, MainActivity.class);
                startActivity(i1);
                finish();
            }
        });
    }
}
