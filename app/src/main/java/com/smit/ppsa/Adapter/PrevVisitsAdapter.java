package com.smit.ppsa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RoomPrevVisitsData;

import java.util.List;

public class PrevVisitsAdapter extends RecyclerView.Adapter<PrevVisitsAdapter.Mholder> {
    private List<RoomPrevVisitsData> prevVisitsData;

    public PrevVisitsAdapter(List<RoomPrevVisitsData> prevVisitsData) {
        this.prevVisitsData = prevVisitsData;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_prev_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Mholder holder, int position) {
        holder.purpose.setText(prevVisitsData.get(position).getcVal());
        holder.dateTime.setText(prevVisitsData.get(position).getdVisit());
        if (position%2!=0){
            holder.layout.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }


    }

    @Override
    public int getItemCount() {
        return prevVisitsData.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView dateTime,purpose;
        LinearLayout layout;
        public Mholder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.date_time);
            purpose = itemView.findViewById(R.id.purpose);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
