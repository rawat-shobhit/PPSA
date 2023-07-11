package com.smit.ppsa.sampleCollectionVisitFolder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smit.ppsa.PatientsFollowFolder.PatientFollowUpAdapter;
import com.smit.ppsa.PatientsFollowFolder.PatientFollowUpList;
import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;

import java.util.ArrayList;

public class SampleCollectionVisitAdapter extends RecyclerView.Adapter<SampleCollectionVisitAdapter.Mholder> {
    private ArrayList<SampleCollectionVisitList> list;
    private final Context context;


    public SampleCollectionVisitAdapter(ArrayList<SampleCollectionVisitList> registerParentData, Context context) {
        this.list = registerParentData;
        this.context = context;

    }

    @NonNull
    @Override
    public SampleCollectionVisitAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SampleCollectionVisitAdapter.Mholder(LayoutInflater.from(context).inflate(R.layout.layout_sample_collection_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SampleCollectionVisitAdapter.Mholder holder, int position) {

        Log.d("checkingData",list.toString());

//        if(position%2==0)
//        {
//
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#e2efd9"));
//
//        }else{
//            holder.hfName.setTextColor(Color.parseColor("#71ad46"));
//            holder.nikshayaID.setTextColor(Color.parseColor("#71ad46"));
//            holder.sampleType.setTextColor(Color.parseColor("#71ad46"));
//            holder.reportDate.setTextColor(Color.parseColor("#71ad46"));
//        }

        try{
            holder.hfName.setText(list.get(position).getCPatNam());
        }catch (Exception e){
        }

        try{
            holder.nikshayaID.setText(list.get(position).getNhfName());
        }catch (Exception e){
        }

        try{
            holder.sampleType.setText(list.get(position).getCVal());
        }catch (Exception e){
        }

        try{
            holder.reportDate.setText(list.get(position).getPrd());
        }catch (Exception e){
        }

        try{

            if(list.get(position).getRptColl().toString().equals("1")){
                Glide.with(context)
                        .load(R.drawable.ic_baseline_check_24)
                        .into(holder.imageView);
            }else
            {
                Glide.with(context)
                        .load(R.drawable.ic_baseline_cancel_24)
                        .into(holder.imageView);
            }


        }catch (Exception e)
        {

        }

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hfName,nikshayaID, sampleType ,reportDate;
        LinearLayout linearLayout;
        ImageView imageView;



        public Mholder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linearLayoutProvider);
            hfName = itemView.findViewById(R.id.hfName);
            nikshayaID= itemView.findViewById(R.id.nikshayaID);
            sampleType=itemView.findViewById(R.id.sampleType);
            reportDate=itemView.findViewById(R.id.reportDate);
            imageView=itemView.findViewById(R.id.imageView);

        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        list = list;
        notifyDataSetChanged();
    }
}
