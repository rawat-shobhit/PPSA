package com.smit.ppsa.dailyVisitOutputFolder;

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
import com.smit.ppsa.healthFacilityFolder.HealthFacilityAdapter;
import com.smit.ppsa.healthFacilityFolder.HealthFacilityList;

import java.util.ArrayList;

public class DailyVisitAdaper extends RecyclerView.Adapter<DailyVisitAdaper.Mholder> {
    private ArrayList<DailyVisitList> list;
    private final Context context;


    public DailyVisitAdaper(ArrayList<DailyVisitList> registerParentData, Context context) {
        this.list = registerParentData;
        this.context = context;

    }

    @NonNull
    @Override
    public DailyVisitAdaper.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyVisitAdaper.Mholder(LayoutInflater.from(context).inflate(R.layout.layout_daily_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyVisitAdaper.Mholder holder, int position) {


//        if(position%2==0)
//        {
//            try{
//                holder.date.setTextColor(Color.parseColor("#ff1e07"));
//            }catch (Exception e){}
//
//
//
//            try{
//                holder.linearLayout.setBackgroundColor(Color.parseColor("#d8e2f2"));
//            }catch (Exception e){}
//
//
//
//        }

        try{
            holder.date.setText(list.get(position).getPrd());
        }catch (Exception e){

        }



        try{
            holder.dv.setText(list.get(position).getDv());
        }catch (Exception e){
            Log.d("crash",e.toString());
        }

        try{
            holder.pf.setText(list.get(position).getPf());
        }catch (Exception e){

        }

        try{
            holder.sc.setText(list.get(position).getSc());

        }catch (Exception e){

        }

        try{
            holder.nc.setText(list.get(position).getNc());
        }catch (Exception e){

        }

        try{
            holder.ne.setText(list.get(position).getNe());

        }catch (Exception e){

        }

        try{
            holder.be.setText(list.get(position).getBe());

        }catch (Exception e){

        }




    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView date,dv, pf, sc, nc ,ne,be;
        LinearLayout linearLayout;



        public Mholder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linearLayout);
            date = itemView.findViewById(R.id.tvDate);
            dv= itemView.findViewById(R.id.tvDV);
            pf=itemView.findViewById(R.id.tvPF);
            sc= itemView.findViewById(R.id.tvSc);
            nc=itemView.findViewById(R.id.tvNC);
            ne=itemView.findViewById(R.id.tvNE);
            be=itemView.findViewById(R.id.tvBE);

        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        list = list;
        notifyDataSetChanged();
    }
}
