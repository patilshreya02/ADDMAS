package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewRecordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<RecordModel> recordList;
    private FirebaseFirestore firestore;
    private Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        recyclerView = findViewById(R.id.recyclerView);
        back =findViewById(R.id.backView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewRecordActivity.this, MainActivity_breakdown.class);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recordList = new ArrayList<>();
        adapter = new RecordAdapter(this, recordList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        loadRecords();
    }

    private void loadRecords() {
        CollectionReference recordsRef = firestore.collection("breakdown");
        recordsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RecordModel record = documentSnapshot.toObject(RecordModel.class);
                    recordList.add(record);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ViewRecordActivity", "Error fetching records: " + e.getMessage());
            }
        });
    }
}

