package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;

import java.util.ArrayList;
import java.util.List;

public class TestedPatientAdapter extends RecyclerView.Adapter<TestedPatientAdapter.Mholder> {
    private List<RegisterParentData> registerParentData;
    private Context context;
    int prevPos = -1;

    public TestedPatientAdapter(List<RegisterParentData> registerParentData, Context context) {
        this.registerParentData = registerParentData;
        this.context = context;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Mholder holder, @SuppressLint("RecyclerView") int position) {
        holder.radioButton.setClickable(false);
        holder.radioButton.setFocusable(false);
        if (registerParentData.get(position).isChecked()) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
        holder.address.setText(registerParentData.get(position).getcAdd());
      //  holder.phone.setText(registerParentData.get(position).p);
        holder.hospitalName.setText(registerParentData.get(position).getcPatNam());
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerParentData.get(position).getnHfId().equals("")){
                    Toast.makeText(view.getContext(), "Can not add or view doctors because it is added in local due to no internet connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (prevPos == -1) {

                    } else {
                        registerParentData.get(prevPos).setChecked(false);
                    }
                    registerParentData.get(position).setChecked(true);
                    prevPos = position;
                    Intent intent = new Intent();
                    intent.setAction("patient");
                    intent.putExtra("checked", true);
                    intent.putExtra("enroll_id", registerParentData.get(position).getId());
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return registerParentData.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hospitalName,phone,address;
        RadioButton radioButton;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.qualification_one);
           // phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
    public void updateList(ArrayList<RegisterParentData> list) {
        registerParentData = list;
        notifyDataSetChanged();
    }
}
