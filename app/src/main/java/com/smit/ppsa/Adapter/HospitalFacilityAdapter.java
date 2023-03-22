package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.BaseUtils;
import com.smit.ppsa.ProviderEngagementViewModel;
import com.smit.ppsa.R;
import com.smit.ppsa.Response.RoomDoctorsList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HospitalFacilityAdapter extends RecyclerView.Adapter<HospitalFacilityAdapter.NotificationHolder> {
    private List<RoomDoctorsList> nList;
    private Activity context;
    private String type;
    private ProviderEngagementViewModel viewModel;
    int prevPos = -1;

    public HospitalFacilityAdapter(List<RoomDoctorsList> nList, Activity context, String type, ProviderEngagementViewModel viewModel) {
        this.nList = nList;
        this.context = context;
        this.type = type;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hospitalfacility, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.radioButton.setClickable(false);
        holder.radioButton.setFocusable(false);
        holder.radioButtonOne.setClickable(false);
        holder.radioButtonOne.setFocusable(false);

      //  nList.get(0).setChecked(true);

        if (type.equals("provider")) {
            holder.radioButton.setVisibility(View.VISIBLE);
        } else holder.radioButtonOne.setVisibility(View.VISIBLE);

        if (nList.get(position).isChecked()) {
            holder.radioButton.setChecked(true);
            holder.radioButtonOne.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
            holder.radioButtonOne.setChecked(false);
        }

        holder.doctorname.setText(nList.get(position).getDocname());
        holder.qualificationone.setText(nList.get(position).getQualf());
        holder.qualificationtwo.setText(nList.get(position).getQual());
        holder.conatactno.setText(nList.get(position).getMob());

        try{
            int difference = (int) getDateDiff(new SimpleDateFormat("yyyy/MM/dd"), nList.get(position).getlst_vst().toString(), getDateTime());
            Log.d("dateDifference",difference+" done");

            if(difference<=30){
                holder.borderLayout.setBackgroundTintList(context.getResources().getColorStateList(R.color.teal_700));
            }
        }catch (Exception e){}


        holder.lastvisit.setText(nList.get(position).getlst_vst());
//
//        if(type.equals("hospital") || type.equals("hospitald") || type.equals("provider")){
//
//        }else{
//            prevPos = position;
//            Intent intent = new Intent();
//            intent.setAction("doctors");
//            intent.putExtra("checked", true);
//            intent.putExtra("doc_id", nList.get(position).getIdd());
//            intent.putExtra("position", String.valueOf(position));
//            intent.putExtra("hf_id", nList.get(position).getHf_id());
//            intent.putExtra("docName",nList.get(position).getDocname());
//            LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
//            notifyDataSetChanged();
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("type",type);

                if (nList.get(position).isChecked()) {
                    nList.get(position).setChecked(false);
                    viewModel.removeList(nList.get(position));
                } else {
                    nList.get(position).setChecked(true);
                    viewModel.addList(nList.get(position));
                }



                if (type.equals("hospital") || type.equals("hospitald")) {
                    if (prevPos == -1) {

                    } else {
                        nList.get(prevPos).setChecked(false);
                    }
                    BaseUtils.showToast(context,"1");
                    prevPos = position;
                    Log.d("jko", "onClick: " + nList.get(position).getIdd());
                    Intent intent = new Intent();
                    intent.setAction("doctors");
                    intent.putExtra("checked", true);
                    intent.putExtra("doc_id", nList.get(position).getIdd());
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("counsel", "");
                    intent.putExtra("hospitalName", context.getIntent().getStringExtra("hospitalName"));
                    intent.putExtra("hf_id", nList.get(position).getHf_id());
                   /* context.startActivity(new Intent(context, FormTwo.class)
                            .putExtra("doc_id", nList.get(position).getIdd())
                            .putExtra("counsel", "")
                            .putExtra("hospitalName", context.getIntent().getStringExtra("hospitalName"))
                            .putExtra("hf_id", nList.get(position).getHf_id())
                    );*/

                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();
                    //  context.finish();
                } else if (type.equals("provider")) {
                    BaseUtils.showToast(context,"2");

                    Log.d("jko", "onClick: " + nList.get(position).getIdd());

                    /*  context.startActivity(new Intent(context, *//*FormOne*//*ProviderEngagement.class)
                            .putExtra("doc_id", nList.get(position).getIdd()).putExtra("provider", "")
                            .putExtra("hospitalName", context.getIntent().getStringExtra("hospitalName"))
                            .putExtra("hospitallocation", context.getIntent().getStringExtra("hospitallocation"))
                            .putExtra("hospitaltypeName", context.getIntent().getStringExtra("hospitaltypeName"))
                            .putExtra("docName", nList.get(position).getDocname())


                            .putExtra("hf_id", nList.get(position).getHf_id())
                    );*/

                    Log.d("dmkidjio", "onReceive: " + context.getIntent().getStringExtra("lastvisit"));


                    Intent intent = new Intent();
                    intent.setAction("doctors");
                    intent.putExtra("checked", true);
                    intent.putExtra("doc_id", nList.get(position).getIdd()).putExtra("provider", "");
                    intent.putExtra("hospitalName", context.getIntent().getStringExtra("hospitalName"));
                    intent.putExtra("hospitallocation", context.getIntent().getStringExtra("hospitallocation"));
                    intent.putExtra("hospitaltypeName", context.getIntent().getStringExtra("hospitaltypeName"));
                    intent.putExtra("lastvisit", context.getIntent().getStringExtra("lastvisit"));
                    intent.putExtra("docName", nList.get(position).getDocname());
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("hf_id", nList.get(position).getHf_id());
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();

                    //  context.finish();
                } else {
                    if (prevPos == -1) {

                    } else {
                        nList.get(prevPos).setChecked(false);
                    }
                    BaseUtils.showToast(context,"3");

                    prevPos = position;
                    Intent intent = new Intent();
                    intent.setAction("doctors");
                    intent.putExtra("checked", true);
                    intent.putExtra("doc_id", nList.get(position).getIdd());
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("hf_id", nList.get(position).getHf_id());
                    intent.putExtra("docName",nList.get(position).getDocname());
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();
                   /* context.startActivity(new Intent(context, FormOne.class)
                    );*/

                    //  context.finish();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {

        private TextView doctorname, qualificationone, qualificationtwo, conatactno,lastvisit;
        CheckBox radioButton;
        RadioButton radioButtonOne;
        private LinearLayout borderLayout;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            doctorname = itemView.findViewById(R.id.doctor_name);
            qualificationone = itemView.findViewById(R.id.qualification_one);
            qualificationtwo = itemView.findViewById(R.id.qualification_two);
            conatactno = itemView.findViewById(R.id.conatact_number);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButtonOne = itemView.findViewById(R.id.radioButtonone);
            lastvisit= itemView.findViewById(R.id.lastvisit);
            borderLayout=itemView.findViewById(R.id.border);
        }
    }


    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateList(ArrayList<RoomDoctorsList> list) {
        nList = list;
        notifyDataSetChanged();
    }

}
