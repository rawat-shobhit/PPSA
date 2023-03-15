package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.smit.ppsa.R;
import com.smit.ppsa.Response.AttendenceListData;

import java.util.List;

public class AttendenceListAdapter extends RecyclerView.Adapter<AttendenceListAdapter.AttendenceHolder> {
    private List<AttendenceListData> nList;
    private Context context;

    public AttendenceListAdapter(List<AttendenceListData> nList, Context context) {
        this.nList = nList;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendenceListAdapter.AttendenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendenceListAdapter.AttendenceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendencelist, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AttendenceListAdapter.AttendenceHolder holder, int position) {
        holder.date.setText(nList.get(position).getdRpt());
        holder.type.setText(nList.get(position).getcAttendTyp());
    }

    @Override
    public int getItemCount() {
        if (nList.size()==0){
            return 0;
        }else if (nList.size()<10){
            return nList.size();
        }else{
            return 10;
        }
    }

    public class AttendenceHolder extends RecyclerView.ViewHolder {

        private TextView date, type;

        public AttendenceHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.att_date);
            type = itemView.findViewById(R.id.att_type);
        }
    }


}
