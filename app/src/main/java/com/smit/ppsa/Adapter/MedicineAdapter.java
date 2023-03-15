package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.MedicinesViewModel;
import com.smit.ppsa.R;
import com.smit.ppsa.Response.RoomMedicines;
import com.smit.ppsa.StockReceiving;

import java.util.ArrayList;
import java.util.List;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.Mholder> {
    private List<RoomMedicines> registerParentData;
    private Context context;
    MedicinesViewModel mViewModel;
    int prevPos = -1;
    String section;


    public MedicineAdapter(List<RoomMedicines> registerParentData, Context context, String section,MedicinesViewModel mViewModel) {
        this.registerParentData = registerParentData;
        this.context = context;
        this.section = section;
        this.mViewModel = mViewModel;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_medicine,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Mholder holder, @SuppressLint("RecyclerView") int position) {
        holder.radioButton.setClickable(false);
        holder.radioButton.setFocusable(false);
        if (registerParentData.get(position).getChecked()) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        holder.medicineName.setText(registerParentData.get(position).getC_val());
        holder.medicineDesc.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (registerParentData.get(position).getnHfId().equals("")){
                    Toast.makeText(view.getContext(), "Can not add or view doctors because it is added in local due to no internet connection", Toast.LENGTH_SHORT).show();
                }else {*/
                    if (prevPos == -1) {

                    } else {
                        registerParentData.get(prevPos).setChecked(false);
                    }
                registerParentData.get(position).setChecked(true);
                prevPos = position;

                    //LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                if (section.equals("activity")){
                    context.startActivity(new Intent(
                            context, StockReceiving.class
                    ));
                }else {
                    Intent intent = new Intent();
                    intent.setAction("patient");
                    intent.putExtra("checked", true);
                    mViewModel.addList(registerParentData.get(prevPos));
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();
                }
                    notifyDataSetChanged();
                }
           // }
        });
    }

    @Override
    public int getItemCount() {
        return registerParentData.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView medicineName,medicineDesc,address;
        RadioButton radioButton;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medName);
            medicineDesc = itemView.findViewById(R.id.meddesc);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
    public void updateList(ArrayList<RoomMedicines> list) {
        registerParentData = list;
        notifyDataSetChanged();
    }
}
