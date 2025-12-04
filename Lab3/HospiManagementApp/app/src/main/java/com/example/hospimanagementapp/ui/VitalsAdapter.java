package com.example.hospimanagementapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.Vitals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VitalsAdapter extends RecyclerView.Adapter<VitalsAdapter.VH> {

    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);

    private List<Vitals> data;

    public void submit(List<Vitals> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vitals, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Vitals v = data.get(position);
        holder.tvLine1.setText("Temp " + v.temperature + "°C, HR " + v.heartRate + "bpm");
        holder.tvLine2.setText("Glucose " + v.glucose + "mmol/L, Oxygen " + v.oxygenLevel + "%");
        holder.tvLine3.setText("Time: " + displayFormat.format(new Date(v.timestamp)) +
                (v.synced ? " (synced)" : " (pending)")
        );
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvLine1, tvLine2, tvLine3;
        VH(View itemView) {
            super(itemView);
            tvLine1 = itemView.findViewById(R.id.tvVitalsLine1);
            tvLine2 = itemView.findViewById(R.id.tvVitalsLine2);
            tvLine3 = itemView.findViewById(R.id.tvVitalsLine3);
        }
    }
}
