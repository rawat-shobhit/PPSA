package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RoomPreviousSamplesCollection;

import java.util.ArrayList;
import java.util.List;

public class PreviousSamplesCollectionAdapter extends RecyclerView.Adapter<PreviousSamplesCollectionAdapter.Mholder> {
    private List<RoomPreviousSamplesCollection> ParentData = new ArrayList<>();
    private Context context;
    int prevPos = -1;

    public PreviousSamplesCollectionAdapter(List<RoomPreviousSamplesCollection> ParentData, Context context) {
        this.ParentData = ParentData;
        this.context = context;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_prev_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Mholder holder, @SuppressLint("RecyclerView") int position) {

        holder.date.setText(ParentData.get(position).getD_specm_col());
        //  holder.phone.setText(registerParentData.get(position).p);
        holder.sampleCollected.setText(ParentData.get(position).getSpecm_typ());
        if (position % 2 != 0) {
            holder.layout.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return ParentData.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView date, sampleCollected;
        LinearLayout layout;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_time);
            sampleCollected = itemView.findViewById(R.id.purpose);
            layout = itemView.findViewById(R.id.layout);

        }
    }

    public void updateList(ArrayList<RoomPreviousSamplesCollection> list) {
        ParentData = list;
        notifyDataSetChanged();
    }
}
