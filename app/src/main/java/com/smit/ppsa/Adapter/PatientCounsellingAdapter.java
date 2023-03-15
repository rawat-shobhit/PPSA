package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;

import java.util.ArrayList;
import java.util.List;

public class PatientCounsellingAdapter extends RecyclerView.Adapter<PatientCounsellingAdapter.Mholder> {
    private List<RegisterParentData> registerParentData;
    private Context context;
    int prevPos = -1;
    private String type;

    public PatientCounsellingAdapter(List<RegisterParentData> registerParentData, Context context, String type) {
        this.registerParentData = registerParentData;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient_counselling, parent, false));
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

        if (type.equals("counsel")) {
            holder.actualHospitalname.setVisibility(View.VISIBLE);
            holder.nikshayId.setVisibility(View.VISIBLE);
            holder.patientAge.setVisibility(View.VISIBLE);
            holder.patientType.setVisibility(View.VISIBLE);
            holder.doctorName.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
        }

        if (registerParentData.get(position).isChecked()) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
        //holder.actualHospitalname.setText(registerParentData.get(position).getC_hf_nam() + " / " + ((Activity)context).getIntent().getStringExtra("hospitaltypeName"));
        holder.actualHospitalname.setText(registerParentData.get(position).getC_hf_nam());
        holder.doctorName.setText(registerParentData.get(position).getcDocNam());
        holder.date.setText(registerParentData.get(position).getdRegDat());
        holder.patientAge.setText(registerParentData.get(position).getnAge());
        holder.patientType.setText(registerParentData.get(position).getcTyp());
        holder.lastvisit.setText(registerParentData.get(position).getNo_days() + " Days");
        // holder.address.setText(registerParentData.get(position).getcAdd());
        holder.nikshayId.setText("#" + registerParentData.get(position).getnNkshId());
        //  holder.phone.setText(registerParentData.get(position).getC_mob());
        holder.hospitalName.setText(registerParentData.get(position).getcPatNam() + " (" + registerParentData.get(position).getC_mob() + " ) ");
        if (registerParentData.get(position).getTrans_out() != null) {
            if (!registerParentData.get(position).getTrans_out().equals("")) {
                holder.transferShow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                holder.transferShow2.setBackground(context.getDrawable(R.drawable.back_cardview2));
                holder.radioButton.setVisibility(View.GONE);
                //   holder.radioButtonOne.setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerParentData.get(position).getnHfId().equals("")) {
                    Toast.makeText(view.getContext(), "Can not add or view doctors because it is added in local due to no internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (prevPos == -1) {

                    } else {
                        registerParentData.get(prevPos).setChecked(false);
                    }
                    registerParentData.get(position).setChecked(true);
                    prevPos = position;
                    Intent intent = new Intent();
                    intent.setAction("patient");
                    intent.putExtra("checked", true);
                    Log.d("bysdgvtdg", "patient: ids " + registerParentData.get(position).getId());
                    Log.d("bysdgvtdg", "patient: RegDat" + registerParentData.get(position).getdRegDat());
                    Log.d("bysdgvtdg", "patient: nNkshId " + registerParentData.get(position).getnNkshId());
                    Log.d("bysdgvtdg", "patient: cPatNam" + registerParentData.get(position).getcPatNam());
                    Log.d("bysdgvtdg", "patient: nAge" + registerParentData.get(position).getnAge());
                    Log.d("bysdgvtdg", "patient: cTyp" + registerParentData.get(position).getcTyp());
                    Log.d("bysdgvtdg", "patient: nWght" + registerParentData.get(position).getnWght());
                    Log.d("bysdgvtdg", "patient: nHgh" + registerParentData.get(position).getnHght());
                    Log.d("bysdgvtdg", "patient: cAdd" + registerParentData.get(position).getcAdd());
                    Log.d("bysdgvtdg", "patient: cTaluka" + registerParentData.get(position).getcTaluka());
                    Log.d("bysdgvtdg", "patient: cTown" + registerParentData.get(position).getcTown());
                    Log.d("bysdgvtdg", "patient: cWard" + registerParentData.get(position).getcWard());
                    Log.d("bysdgvtdg", "patient: cLndMrk" + registerParentData.get(position).getcLndMrk());
                    Log.d("bysdgvtdg", "patient: nPin" + registerParentData.get(position).getnPin());
                    Log.d("bysdgvtdg", "patient: cDocNam" + registerParentData.get(position).getcDocNam());
                    Log.d("bysdgvtdg", "patient: Id" + registerParentData.get(position).getId());
                    Log.d("bysdgvtdg", "patient: StId" + registerParentData.get(position).getnStId());
                    Log.d("juuyttrtr", "patient: TuId" + registerParentData.get(position).getnTuId());
                    Log.d("bysdgvtdg", "patient: HfId" + registerParentData.get(position).getnHfId());
                    Log.d("bysdgvtdg", "patient: DocId" + registerParentData.get(position).getnDocId());
                    Log.d("bysdgvtdg", "patient: UserId" + registerParentData.get(position).getnUserId());
                    Log.d("bysdgvtdg", "patient: no_days" + registerParentData.get(position).getNo_days());
                    Log.d("bysdgvtdg", "patient: LST_DT" + registerParentData.get(position).getLST_DT());
                    Log.d("bysdgvtdg", "patient: LST_DT" + registerParentData.get(position).getC_mob());
                    //Log.d("bysdgvtdg", "patient: LST_DT" + registerParentData.get(position).getC_mob());

                    intent.putExtra("tu_id", registerParentData.get(position).getnTuId());
                    intent.putExtra("enroll_id", registerParentData.get(position).getId());
                    intent.putExtra("st_id", registerParentData.get(position).getnStId());
                    intent.putExtra("dis_id", registerParentData.get(position).getnDisId());
                    intent.putExtra("doc_id", registerParentData.get(position).getnDocId());
                    intent.putExtra("doc_name", registerParentData.get(position).getcDocNam());
                    intent.putExtra("patient_name", registerParentData.get(position).getcPatNam());
                    intent.putExtra("patient_age", registerParentData.get(position).getnAge());
                    intent.putExtra("reg_date", registerParentData.get(position).getdRegDat());
                    intent.putExtra("patient_type", registerParentData.get(position).getcTyp());
                    intent.putExtra("hf_id", registerParentData.get(position).getnHfId());
                    intent.putExtra("niksh_id", registerParentData.get(position).getnNkshId());
                    intent.putExtra("patient_phone", registerParentData.get(position).getC_mob());
                    intent.putExtra("hospital_name", registerParentData.get(position).getC_hf_nam());
                    /// intent.putExtra("hf_type_id", registerParentData.get(position).t());

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
        TextView hospitalName, phone,/*address,*/
                nikshayId, patientAge, patientType, actualHospitalname, date, doctorName;
        LinearLayout transferShow, transferShow2;
        CheckBox radioButton;
        TextView lastvisit;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.customerName);
            actualHospitalname = itemView.findViewById(R.id.hospitalnamett);
            transferShow = itemView.findViewById(R.id.transfer_show);
            transferShow2 = itemView.findViewById(R.id.transfer_show2);
            //phone = itemView.findViewById(R.id.phone);
            // address = itemView.findViewById(R.id.address);
            nikshayId = itemView.findViewById(R.id.regid);
            doctorName = itemView.findViewById(R.id.doctorname);
            radioButton = itemView.findViewById(R.id.radioButton);
            date = itemView.findViewById(R.id.datetxt);
            patientAge = itemView.findViewById(R.id.agetxt);
            patientType = itemView.findViewById(R.id.typeCl);
            lastvisit = itemView.findViewById(R.id.lastvisit);

            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        registerParentData = list;
        notifyDataSetChanged();
    }
}
