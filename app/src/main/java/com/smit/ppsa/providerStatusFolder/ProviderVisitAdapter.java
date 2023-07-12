package com.smit.ppsa.providerStatusFolder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;


import java.util.ArrayList;

public class ProviderVisitAdapter  extends RecyclerView.Adapter<ProviderVisitAdapter.Mholder> {
    private ArrayList<ProviderVisitList> list;
    private final Context context;


    public ProviderVisitAdapter(ArrayList<ProviderVisitList> registerParentData, Context context) {
        this.list = registerParentData;
        this.context = context;

    }

    @NonNull
    @Override
    public ProviderVisitAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProviderVisitAdapter.Mholder(LayoutInflater.from(context).inflate(R.layout.layout_provider_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderVisitAdapter.Mholder holder, int position) {

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
            holder.providerName.setText(list.get(position).getCDocNam());
        }catch (Exception e){

        }

        try{
            holder.purposeOfVisit.setText(list.get(position).getCVal());
        }catch (Exception e){

        }

        try{
            holder.date.setText(list.get(position).getPrd());
        }catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hfName,providerName, purposeOfVisit ,date;
        LinearLayout linearLayout;



        public Mholder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linearLayoutProvider);
            hfName = itemView.findViewById(R.id.hfName);
            providerName= itemView.findViewById(R.id.providerName);
            purposeOfVisit=itemView.findViewById(R.id.purposeOfVisit);
            date = itemView.findViewById(R.id.date);

        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        list = list;
        notifyDataSetChanged();
    }
}
