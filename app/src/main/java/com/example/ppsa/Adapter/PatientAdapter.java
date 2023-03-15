package com.example.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppsa.R;
import com.example.ppsa.Response.RegisterParentData;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.Mholder> {
    private List<RegisterParentData> registerParentData;
    private Context context;
    int prevPos = -1;

    public PatientAdapter(List<RegisterParentData> registerParentData, Context context) {
        this.registerParentData = registerParentData;
        this.context = context;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hospitallist,parent,false));
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
        holder.hospitalName.setText(registerParentData.get(position).getcPatNam());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("hf_id", registerParentData.get(position).getnHfId());
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return registerParentData.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hospitalName;
        RadioButton radioButton;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            radioButton = itemView.findViewById(R.id.radio);
        }
    }
}
