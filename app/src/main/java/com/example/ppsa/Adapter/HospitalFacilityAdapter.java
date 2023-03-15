package com.example.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppsa.FormOne;
import com.example.ppsa.FormTwo;
import com.example.ppsa.R;
import com.example.ppsa.Response.DoctorsList;
import com.example.ppsa.Response.RoomDoctorsList;

import java.util.List;

public class HospitalFacilityAdapter extends RecyclerView.Adapter<HospitalFacilityAdapter.NotificationHolder> {
    private List<RoomDoctorsList> nList;
    private Context context;

    public HospitalFacilityAdapter(List<RoomDoctorsList> nList, Context context) {
        this.nList = nList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hospitalfacility, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.doctorname.setText(nList.get(position).getDocname());
        holder.qualificationone.setText(nList.get(position).getQualf());
        holder.qualificationtwo.setText(nList.get(position).getQual());
        holder.conatactno.setText(nList.get(position).getMob());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, FormOne.class)
                        .putExtra("doc_id", nList.get(position).getIdd())
                        .putExtra("hf_id", nList.get(position).getHf_id()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        private TextView doctorname, qualificationone, qualificationtwo, conatactno;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            doctorname = itemView.findViewById(R.id.doctor_name);
            qualificationone = itemView.findViewById(R.id.qualification_one);
            qualificationtwo = itemView.findViewById(R.id.qualification_two);
            conatactno = itemView.findViewById(R.id.conatact_number);
        }
    }


}
