package com.smit.ppsa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RoomTestData;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestHolder> {
    private List<RoomTestData> roomTestData;

    public TestAdapter(List<RoomTestData> roomTestData) {
        this.roomTestData = roomTestData;
    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_test,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        holder.ts_date.setText(roomTestData.get(position).getdSmplRecd());
        holder.ts_diagnostic.setText(roomTestData.get(position).getDiagTest());
        holder.ts_dsttest.setText(roomTestData.get(position).getDstTest());
        holder.ts_dateDsttest.setText(roomTestData.get(position).getdTstDst());
    }

    @Override
    public int getItemCount() {
        return roomTestData.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder {
        TextView ts_date,ts_diagnostic,ts_dsttest,ts_dateDsttest;
        public TestHolder(@NonNull View itemView) {
            super(itemView);
            ts_date = itemView.findViewById(R.id.ts_date);
            ts_diagnostic = itemView.findViewById(R.id.ts_diagnostic);
            ts_dsttest = itemView.findViewById(R.id.ts_dsttest);
            ts_dateDsttest = itemView.findViewById(R.id.ts_dateDsttest);
        }
    }
}
