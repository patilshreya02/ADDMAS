package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MainActivity5 extends AppCompatActivity {

    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private float xDelta = 0.0f;
    private float yDelta = 0.0f;

    private Button in, logout;
    private FirebaseUser user;
    private TextView textView, textView1, shift, time;
    private FirebaseFirestore fStore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main5);

        // Initialize the ScaleGestureDetector
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        initializeViews();
        setEventListeners();
        setupFirebase();
        updateShiftBasedOnTime();
    }

    private void initializeViews() {
        in = findViewById(R.id.b4);
        logout = findViewById(R.id.button7);
        textView = findViewById(R.id.user1);
        textView1 = findViewById(R.id.user2);
        imageView = findViewById(R.id.imageView3);
        shift = findViewById(R.id.shiftshow);
        time = findViewById(R.id.time);
    }

    private void setEventListeners() {
        imageView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    xDelta = X - imageView.getX();
                    yDelta = Y - imageView.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleImageMove(event);
                    break;
            }
            return true;
        });

        in.setOnClickListener(v -> {
            in.setBackgroundColor(getResources().getColor(R.color.blue));
            startActivity(new Intent(MainActivity5.this, MainActivityin.class));
        });

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity5.this, MainActivity.class));
            finish();
        });
    }

    private void setupFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            if (value != null) {
                textView1.setText(value.getString("fName"));
            }
        });
    }

    private void handleImageMove(MotionEvent event) {
        float newPosX = event.getRawX() - xDelta;
        float newPosY = event.getRawY() - yDelta;

        // Getting the bounds of the layout
        int[] layoutPos = new int[2];
        imageView.getLocationOnScreen(layoutPos);
        int layoutX = layoutPos[0];
        int layoutY = layoutPos[1];
        int layoutWidth = imageView.getWidth();
        int layoutHeight = imageView.getHeight();

        // Limiting movement within the layout boundaries
        newPosX = Math.max(layoutX, Math.min(newPosX, layoutX + layoutWidth - imageView.getWidth()));
        newPosY = Math.max(layoutY, Math.min(newPosY, layoutY + layoutHeight - imageView.getHeight()));

        imageView.setX(newPosX);
        imageView.setY(newPosY);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));
            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);
            return true;
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