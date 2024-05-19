package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MainActivity4 extends AppCompatActivity {

    Button logout, submit, CheckAll;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView, textView1, shift;
    FirebaseFirestore fStore;
    String userId;
    CheckBox c2, c3, c4, c5, c6, c7, c8, c9, c10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main4);

        logout = findViewById(R.id.button7);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        textView = findViewById(R.id.user1);
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        textView1 = findViewById(R.id.user2);
        c2 = findViewById(R.id.c12);
        c3 = findViewById(R.id.c13);
        c4 = findViewById(R.id.c14);
        c5 = findViewById(R.id.c15);
        c6 = findViewById(R.id.c16);
        c7 = findViewById(R.id.c17);
        c8 = findViewById(R.id.c18);
        c9 = findViewById(R.id.c19);
        c10 = findViewById(R.id.c20);
        shift=findViewById(R.id.shiftshow);
        submit = findViewById(R.id.sub);
        CheckAll = findViewById(R.id.CheckAll);
        submit.setEnabled(false);
        updateShiftBasedOnTime();

        DocumentReference documentReference = fStore.collection("users").document(userId);

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                textView1.setText(documentSnapshot.getString("fName"));
            } else {
                // Handle the case when document does not exist or has null data
                // For example, set a default value for textView1
                textView1.setText("Default Name");
            }
        }).addOnFailureListener(e -> {
            // Handle any errors that occurred during the query
            Log.e(TAG, "Error fetching document: " + e.getMessage());
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        // Set CheckBoxChangeListener for all checkboxes
        c2.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c3.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c4.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c5.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c6.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c7.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c8.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c9.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));
        c10.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxChangeListener(buttonView, isChecked));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity4.this, MainActivity5.class);
                startActivity(intent1);
            }
        });
        CheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set all checkboxes to checked state
                c2.setChecked(true);
                c3.setChecked(true);
                c4.setChecked(true);
                c5.setChecked(true);
                c6.setChecked(true);
                c7.setChecked(true);
                c8.setChecked(true);
                c9.setChecked(true);
                c10.setChecked(true);
            }
        });
    }

    // Implement CheckBoxChangeListener
    private void checkBoxChangeListener(CompoundButton buttonView, boolean isChecked) {
        // Check if all checkboxes are checked
        boolean allChecked = c2.isChecked() && c3.isChecked()
                && c4.isChecked() && c5.isChecked() && c6.isChecked()
                && c7.isChecked() && c8.isChecked() && c9.isChecked() && c10.isChecked();

        // Enable or disable the submit button based on the condition
        submit.setEnabled(allChecked);

        // Change color if all checkboxes are checked
        if (allChecked) {
            submit.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            // Reset the color if not all checkboxes are checked
            submit.setBackgroundColor(getResources().getColor(android.R.color.black));
        }
    }
    private void updateShiftBasedOnTime() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 8 && hourOfDay < 16) {
            shift.setText("1st Shift");
        } else if (hourOfDay >= 16 && hourOfDay < 22) {
            shift.setText("2nd Shift");
        } else {
            shift.setText("3rd Shift");
        }
    }
}
