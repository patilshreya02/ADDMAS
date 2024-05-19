package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private List<RecordModel> recordList;

    public RecordAdapter(Context context, List<RecordModel> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singlerow, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordModel record = recordList.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.CauseBDview);
            textView2 = itemView.findViewById(R.id.SparesBDview);
            textView3 = itemView.findViewById(R.id.TimeBDview);
            textView4 = itemView.findViewById(R.id.DateBDview);
            textView5 = itemView.findViewById(R.id.OPnametext); // Corrected assignment
            textView6 = itemView.findViewById(R.id.TotalBDview); // Corrected assignment
            textView7 = itemView.findViewById(R.id.NatureBDview); // Corrected assignment
            textView8 = itemView.findViewById(R.id.OPIDview); // Corrected assignment
        }

        public void bind(RecordModel record) {
            textView1.setText(record.getCause_of_bd());
            textView2.setText(record.getSpares());
            textView3.setText(record.getTimetext());
            textView4.setText(record.getDate());
            textView5.setText(record.getBreakdown_ID());
            textView6.setText(record.getTotalHrOfBreakdown());
            textView7.setText(record.getNature_of_problem());
            textView8.setText(record.getUser_ID());
        }
    }
}
