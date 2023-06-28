package com.smit.ppsa.PatientsFollowFolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.providerStatusFolder.ProviderVisitAdapter;
import com.smit.ppsa.providerStatusFolder.ProviderVisitList;

import java.util.ArrayList;

public class PatientFollowUpAdapter extends RecyclerView.Adapter<PatientFollowUpAdapter.Mholder> {
    private ArrayList<PatientFollowUpList> list;
    private final Context context;


    public PatientFollowUpAdapter(ArrayList<PatientFollowUpList> registerParentData, Context context) {
        this.list = registerParentData;
        this.context = context;

    }

    @NonNull
    @Override
    public PatientFollowUpAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientFollowUpAdapter.Mholder(LayoutInflater.from(context).inflate(R.layout.layout_patients_followup, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientFollowUpAdapter.Mholder holder, int position) {

//
//        if(position%2==0)
//        {
//            holder.hfName.setTextColor(Color.parseColor("#ff1e07"));
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#d8e2f2"));
//
//        }

        try{
            holder.hfName.setText(list.get(position).getCHfNam());
        }catch (Exception e){

        }
        try{
            holder.nikshayaID.setText(list.get(position).getNNkshId());
        }catch (Exception e){

        }

        try{
            holder.patientName.setText(list.get(position).getCPatNam());
        }catch (Exception e){

        }

        try{
            holder.modeOfollowUp.setText(list.get(position).getCVal());
        }catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hfName,nikshayaID, patientName ,modeOfollowUp;
        LinearLayout linearLayout;



        public Mholder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linearLayoutProvider);
            hfName = itemView.findViewById(R.id.hfName);
            nikshayaID= itemView.findViewById(R.id.nikshayaID);
            patientName=itemView.findViewById(R.id.patientName);
            modeOfollowUp=itemView.findViewById(R.id.modeOfollowUp);

        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        list = list;
        notifyDataSetChanged();
    }
}
