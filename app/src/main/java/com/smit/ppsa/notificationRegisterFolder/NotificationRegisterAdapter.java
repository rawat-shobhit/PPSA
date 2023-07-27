package com.smit.ppsa.notificationRegisterFolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;

import java.util.ArrayList;

public class NotificationRegisterAdapter extends RecyclerView.Adapter<NotificationRegisterAdapter.Mholder> {
    private ArrayList<NotificationRegisterList> list;
    private final Context context;


    public NotificationRegisterAdapter(ArrayList<NotificationRegisterList> registerParentData, Context context) {
        this.list = registerParentData;
        this.context = context;

    }

    @NonNull
    @Override
    public NotificationRegisterAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationRegisterAdapter.Mholder(LayoutInflater.from(context).inflate(R.layout.layout_notification_leadger, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRegisterAdapter.Mholder holder, int position) {

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

            if(list.get(position).getBnk().equals("1"))
            {
                Glide.with(context)
                        .load(R.drawable.ic_check_)
                        .into(holder.dstImg);
            }else{
                Glide.with(context)
                        .load(R.drawable.ic_cross)
                        .into(holder.dstImg);
            }


        }catch (Exception e){

        }

        try{
            if(list.get(position).getUdst().equals("1"))
            {
                Glide.with(context)
                        .load(R.drawable.ic_check_)
                        .into(holder.udstImg);
            }else
            {
                Glide.with(context)
                        .load(R.drawable.ic_cross)
                        .into(holder.udstImg);
            }
        }catch (Exception e){

        }

        try{
            if(list.get(position).getNotf().equals("1"))
            {
                Glide.with(context)
                        .load(R.drawable.ic_check_)
                        .into(holder.niImg);
            }else
            {
                Glide.with(context)
                        .load(R.drawable.ic_cross)
                        .into(holder.niImg);
            }
        }catch (Exception e){

        }




    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hfName,nikshayaID, patientName ,modeOfollowUp,date;

        ImageView niImg,dstImg,udstImg;
        LinearLayout linearLayout;



        public Mholder(@NonNull View itemView) {
            super(itemView);

//            date=itemView.findViewById(R.id.date);
//            linearLayout=itemView.findViewById(R.id.linearLayoutProvider);
//            hfName = itemView.findViewById(R.id.hfName);
//            nikshayaID= itemView.findViewById(R.id.nikshayaID);
//            patientName=itemView.findViewById(R.id.patientName);
//            modeOfollowUp=itemView.findViewById(R.id.modeOfollowUp);

            patientName= itemView.findViewById(R.id.patientName);
            hfName=itemView.findViewById(R.id.hfName);
            nikshayaID=itemView.findViewById(R.id.nikshayaID);
            niImg=itemView.findViewById(R.id.niImg);
            dstImg=itemView.findViewById(R.id.dstImg);
            udstImg=itemView.findViewById(R.id.udstImg);

        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        list = list;
        notifyDataSetChanged();
    }
}
