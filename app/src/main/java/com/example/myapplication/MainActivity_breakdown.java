package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity_breakdown extends AppCompatActivity {

    Button back, submit, time, date, logout, history;
    TextView datetext, timetext, textView1, textView;
    EditText nature_of_problem, cause_of_bd, spares, hrstext;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_breakdown);

        submit = findViewById(R.id.submitbg);
        back = findViewById(R.id.loginb);
        time = findViewById(R.id.timeshowb);
        date = findViewById(R.id.dateb);
        timetext = findViewById(R.id.timeshow);
        datetext = findViewById(R.id.date);
        logout = findViewById(R.id.button7);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        textView = findViewById(R.id.user1);
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        textView1 = findViewById(R.id.user2);
        history = findViewById(R.id.history);
        nature_of_problem = findViewById(R.id.userNameEditText);
        cause_of_bd = findViewById(R.id.entercausebd);
        spares = findViewById(R.id.enterspare);
        hrstext = findViewById(R.id.totalbdtime);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity_breakdown.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                openTimePickerDialog();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInsert();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity_breakdown.this, MainActivity6.class);
                startActivity(i1);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_breakdown.this, ViewRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(MainActivity_breakdown.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datetext.setText(String.valueOf(dayOfMonth) + "." + String.valueOf(month + 1) + "." + String.valueOf(year));
                    }
                }, year, month, dayOfMonth);

        dialog.show();
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(MainActivity_breakdown.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timetext.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hourOfDay, minute, true);

        dialog.show();
    }

    private void processInsert() {
        Map<String, Object> map = new HashMap<>();
        map.put("Breakdown_ID", user.getUid()); // Use UID as a more secure identifier
        map.put("cause_of_bd", cause_of_bd.getText().toString());
        map.put("spares", spares.getText().toString());
        map.put("timetext", timetext.getText().toString());
        map.put("User_ID", textView.getText().toString());
        map.put("TotalHrOfBreakdown", hrstext.getText().toString()); // Fixed the typo in the key
        map.put("Date", datetext.getText().toString());
        map.put("nature_of_problem", nature_of_problem.getText().toString());

        // Get a reference to the Firestore collection "breakdown"
        CollectionReference breakdownRef = fStore.collection("breakdown");

        // Add the document to the collection
        breakdownRef.add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_SHORT).show();

                        // Move the logout code here
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity_breakdown.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}