package com.smit.ppsa.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.PrevVisitsData;
import com.smit.ppsa.Response.RoomPrevVisitsData;

import java.util.List;

public class PrevFdcVisitAdapter extends RecyclerView.Adapter<PrevFdcVisitAdapter.Mholder> {
    private List<PrevVisitsData> prevVisitsData;
    public PrevFdcVisitAdapter(List<PrevVisitsData> prevVisitsData) {
        this.prevVisitsData = prevVisitsData;
    }

    @NonNull
    @Override
    public PrevFdcVisitAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_prev_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PrevFdcVisitAdapter.Mholder holder, int position) {
        if (prevVisitsData.get(position).getD_issue()==null||prevVisitsData.get(position).getD_issue().equals("")){
            holder.dateTime.setText(prevVisitsData.get(position).getD_disp());
        }else{
            holder.dateTime.setText(prevVisitsData.get(position).getD_issue());
        }

        holder.purpose.setText(prevVisitsData.get(position).getTot_drug());
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
