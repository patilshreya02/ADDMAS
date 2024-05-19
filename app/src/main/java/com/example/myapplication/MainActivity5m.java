package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity5m extends AppCompatActivity {

    Button next,date;
    TextView datetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_activity5m);


        next=findViewById(R.id.submit);
        datetext=findViewById(R.id.date);
        date=findViewById(R.id.dateb);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i= new Intent(MainActivity5m.this, MainActivity.class);
                startActivity(i);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    private void openDialog() {
        DatePickerDialog dialog = new DatePickerDialog(MainActivity5m.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datetext.setText(String.valueOf(dayOfMonth) + "." + String.valueOf(month + 1) + "." + String.valueOf(year));
            }
        }, 2024, 2, 1); // Month is zero-based, so 2 is March

        dialog.show();
    }
}
