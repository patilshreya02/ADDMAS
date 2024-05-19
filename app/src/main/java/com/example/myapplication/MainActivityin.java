package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivityin extends AppCompatActivity {

    private Button comp, logout, submitRecord;
    private FirebaseUser user;
    TextView textView, textView1, shift1, todayDateTextView, time, target;
    private FirebaseFirestore fStore;
    private String userId;
    private DatabaseReference databaseReference;

    // EditText references for data collection
    private EditText e, e1, e3, e4, e5, e6, e13, e14, e15, e16, e17, e18, e20, e21, e22, e26;

    // CheckBox references for Yes/No data
    private CheckBox e2, e7, e8, e9, e10, e11, e12, e19, e23, e24, e25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_activityin);

        // Check if the user is logged in, if not, start MainActivity
        setupFirebase();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }

        // Initialize button, text view, and checkbox references
        comp = findViewById(R.id.b2);
        logout = findViewById(R.id.button7);
        textView = findViewById(R.id.user1);
        textView1 = findViewById(R.id.user2);
        shift1 = findViewById(R.id.shiftshow);
        time = findViewById(R.id.time1);
        submitRecord = findViewById(R.id.SubmitRecord);
        todayDateTextView = findViewById(R.id.todaysDateTextView);
        target = findViewById(R.id.targetshow);
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    textView1.setText(value.getString("fName"));
                } else {
                    // Handle the case when document does not exist or has null data
                    // For example, set a default value for textView1
                    textView1.setText("Default Name");
                }
            }
        });

        // Set the user's email in the textView
        textView.setText(user.getEmail());
        retrieveTarget();

        // Get today's date and format it
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(todayDate);
        todayDateTextView.setText(formattedDate);

        // Initialize edit text and check box references
        e = findViewById(R.id.e);
        e1 = findViewById(R.id.e1);
        e3 = findViewById(R.id.e3);
        e4 = findViewById(R.id.e4);
        e5 = findViewById(R.id.e5);
        e6 = findViewById(R.id.e6);
        e13 = findViewById(R.id.e13);
        e14 = findViewById(R.id.e14);
        e15 = findViewById(R.id.e15);
        e16 = findViewById(R.id.e16);
        e17 = findViewById(R.id.e17);
        e18 = findViewById(R.id.e18);
        e20 = findViewById(R.id.e20);
        e21 = findViewById(R.id.e21);
        e22 = findViewById(R.id.e22);
        e26 = findViewById(R.id.e26);

        e2 = findViewById(R.id.e2);
        e7 = findViewById(R.id.e7);
        e8 = findViewById(R.id.e8);
        e9 = findViewById(R.id.e9);
        e10 = findViewById(R.id.e10);
        e11 = findViewById(R.id.e11);
        e12 = findViewById(R.id.e12);
        e19 = findViewById(R.id.e19);
        e23 = findViewById(R.id.e23);
        e24 = findViewById(R.id.e24);
        e25 = findViewById(R.id.e25);

        updateShiftBasedOnTime();

        logout.setOnClickListener(v -> showEndShiftDialog());

        comp.setOnClickListener(v -> {
            comp.setBackgroundColor(getResources().getColor(R.color.blue));
            startActivity(new Intent(MainActivityin.this, MainActivity5.class));
        });

        submitRecord.setOnClickListener(v -> {
            saveReportToFirestore();
            clearFields();
        });
    }
    private void retrieveTarget() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

// Get the current date and shift
        String currentDate = todayDateTextView.getText().toString();
        String currentShift = shift1.getText().toString();
        CollectionReference targetsRef = fStore.collection("targets");

        targetsRef
                .whereEqualTo("date", currentDate)
                .whereEqualTo("shift", currentShift)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                        if (document.exists()) {
                            int targetValue = document.getLong("targetValue").intValue();
                            target.setText(String.valueOf(targetValue));
                        } else {
                            target.setText("No target found for this date and shift.");
                        }
                    } else {
                        Toast.makeText(MainActivityin.this, R.string.report_submitted_success, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setupFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
    }

    private void updateShiftBasedOnTime() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 8 && hourOfDay < 16) {
            shift1.setText(R.string.first_shift);
        } else if (hourOfDay >= 16 && hourOfDay < 22) {
            shift1.setText(R.string.second_shift);
        } else {
            shift1.setText(R.string.third_shift);
        }
    }

    private void saveReportToFirestore() {
        // Create a Map to store report data
        Map<String, Object> reportData = new HashMap<>();

        // Get values from EditTexts and format checkboxes
        reportData.put("time", time.getText().toString().trim());
        reportData.put("Shift", shift1.getText().toString().trim());
        reportData.put("Date", todayDateTextView.getText().toString().trim());
        reportData.put("OpName", textView1.getText().toString().trim());
        reportData.put("JobNumber", e.getText().toString().trim());
        reportData.put("BoreDiameter_70", e1.getText().toString().trim());
        reportData.put("BoreDiameter_180", e2.isChecked() ? "Yes" : "No");
        reportData.put("BoreDiameter_32", e3.getText().toString().trim());
        reportData.put("OuterDiameter_0215", e4.getText().toString().trim());
        reportData.put("OuterDiameter_0198", e5.getText().toString().trim());
        reportData.put("BossOD", e6.getText().toString().trim());
        reportData.put("Chamfer_1x45", e7.isChecked() ? "Yes" : "No");
        reportData.put("Chamfer_0_5x45", e8.isChecked() ? "Yes" : "No");
        reportData.put("Chamfer_1x30", e9.isChecked() ? "Yes" : "No");
        reportData.put("Radius_R0_3", e10.isChecked() ? "Yes" : "No");
        reportData.put("Radius_R0_5", e11.isChecked() ? "Yes" : "No");
        reportData.put("Radius_R10", e12.isChecked() ? "Yes" : "No");
        reportData.put("Depth_19_2", e13.getText().toString().trim());
        reportData.put("Depth_5_0", e14.getText().toString().trim());
        reportData.put("Depth_1_8", e15.getText().toString().trim());
        reportData.put("Depth_10", e16.getText().toString().trim());
        reportData.put("Depth_6", e17.getText().toString().trim());
        reportData.put("Depth_26_8", e18.getText().toString().trim());
        reportData.put("SurfaceFinish_3_2", e19.isChecked() ? "Yes" : "No");
        reportData.put("RunOut_0_03", e20.getText().toString().trim());
        reportData.put("RunOut_0_05", e21.getText().toString().trim());
        reportData.put("RunOut_0_3", e22.getText().toString().trim());
        reportData.put("Squareness_0_03", e23.isChecked() ? "Yes" : "No");
        reportData.put("Squareness_0_05", e24.isChecked() ? "Yes" : "No");
        reportData.put("Squareness_0_3", e25.isChecked() ? "Yes" : "No");
        reportData.put("TotalLength_90", e26.getText().toString().trim());

        // Get a reference to the "InProcessReport" collection
        CollectionReference reportsCollection = fStore.collection("InProcessReport");

        // Add the report data to the collection
        reportsCollection.add(reportData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivityin.this, R.string.report_submitted_success, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivityin.this, getString(R.string.report_submit_failure, e.getMessage()), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        e.setText("");
        e1.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
        e13.setText("");
        e14.setText("");
        e15.setText("");
        e16.setText("");
        e17.setText("");
        e18.setText("");
        e20.setText("");
        e21.setText("");
        e22.setText("");
        e26.setText("");

        e2.setChecked(false);
        e7.setChecked(false);
        e8.setChecked(false);
        e9.setChecked(false);
        e10.setChecked(false);
        e11.setChecked(false);
        e12.setChecked(false);
        e19.setChecked(false);
        e23.setChecked(false);
        e24.setChecked(false);
        e25.setChecked(false);
    }
    private void showEndShiftDialog() {
        // Create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.end_shift);

        // Create an EditText to get the number of jobs completed
        EditText jobsCompletedInput = new EditText(this);
        jobsCompletedInput.setHint(R.string.num);
        jobsCompletedInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(jobsCompletedInput);

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            // Get the number of jobs completed from the input
            int jobsCompleted = Integer.parseInt(jobsCompletedInput.getText().toString());
            saveJobsCompletedToFirestore(jobsCompleted);
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void saveJobsCompletedToFirestore(int jobsCompleted) {
        // Get the current user's name and shift
        String operatorName = textView1.getText().toString();
        String shift = shift1.getText().toString();
        String date = todayDateTextView.getText().toString();

        // Create a map to store the data
        Map<String, Object> jobCompletedData = new HashMap<>();
        jobCompletedData.put("operatorName", operatorName);
        jobCompletedData.put("shift", shift);
        jobCompletedData.put("jobsCompleted", jobsCompleted);
        jobCompletedData.put("date", date);

        // Get a reference to the "JobCompleted" collection
        CollectionReference jobCompletedCollection = fStore.collection("JobCompleted");

        // Add the data to the collection
        jobCompletedCollection.add(jobCompletedData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MainActivityin.this, R.string.jobs_completed_saved, Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent= new Intent(MainActivityin.this,MainActivity.class);
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivityin.this, getString(R.string.jobs_completed_save_failure, e.getMessage()), Toast.LENGTH_SHORT).show();
                });

    }
}