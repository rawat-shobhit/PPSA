package com.smit.ppsa.healthFacilityFolder;

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



public class HealthFacilityAdapter extends RecyclerView.Adapter<HealthFacilityAdapter.Mholder> {
    private ArrayList<HealthFacilityList> list;
    private final Context context;


    public HealthFacilityAdapter(ArrayList<HealthFacilityList> registerParentData, Context context) {
        this.list = registerParentData;
        this.context = context;

    }

    @NonNull
    @Override
    public HealthFacilityAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HealthFacilityAdapter.Mholder(LayoutInflater.from(context).inflate(R.layout.layout_health_facility_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HealthFacilityAdapter.Mholder holder, int position) {


//        if(position%2==0)
//        {
//            holder.hfName.setTextColor(Color.parseColor("#ff1e07"));
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#fae4d4"));
//
//        }

        try{
            holder.hfName.setText(list.get(position).getCHfNam());
        }catch (Exception e){

        }



        try{
            holder.visits.setText(list.get(position).getVisit());
        }catch (Exception e){
            Log.d("crash",e.toString());
        }



        try{
            holder.tvNC.setText(list.get(position).getNc());
        }catch (Exception e){

        }



        try{
            holder.tvSc.setText(list.get(position).getSc());

        }catch (Exception e){

        }

        try{
            holder.tvFdc.setText(list.get(position).getFdc());
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hfName,visits, tvNC, tvSc, tvFdc ;
        LinearLayout linearLayout;



        public Mholder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.linearLayout);
            hfName = itemView.findViewById(R.id.hfName);
            visits= itemView.findViewById(R.id.visit);
            tvNC=itemView.findViewById(R.id.tvNc);
            tvSc= itemView.findViewById(R.id.tvSc);
            tvFdc=itemView.findViewById(R.id.tvFdc);

        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        list = list;
        notifyDataSetChanged();
    }
}
