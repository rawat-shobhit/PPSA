package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.BaseUtils;
import com.smit.ppsa.FormOne;
import com.smit.ppsa.HospitalFacility;
import com.smit.ppsa.R;
import com.smit.ppsa.Response.RegisterParentData;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.Mholder> {
    private List<RegisterParentData> registerParentData;
    private Context context;
    private String type;
    int prevPos = -1;

    public PatientAdapter(List<RegisterParentData> registerParentData, Context context, String type) {
        this.registerParentData = registerParentData;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_patient, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Mholder holder, @SuppressLint("RecyclerView") int position) {
        holder.radioButton.setClickable(false);
        holder.radioButton.setFocusable(false);

        holder.radioButtonOne.setClickable(false);
        holder.radioButtonOne.setFocusable(false);


        if (type == null || !type.equals("sample")) {
            holder.radioButton.setVisibility(View.VISIBLE);
        } else {
            holder.radioButtonOne.setVisibility(View.VISIBLE);
        }
      /*  if (type != null&&type.equals("sample")){

        }else {

        }*/

        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  BaseUtils.showToast(context, registerParentData.get(position).getId());
                Intent intent = new Intent(context, FormOne.class);
                intent.putExtra("pateintId", registerParentData.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.date.setText(registerParentData.get(position).getdRegDat());

        if (type == null) {
            holder.actualHospitalname.setVisibility(View.VISIBLE);
            holder.nikshayId.setVisibility(View.VISIBLE);
            holder.patientAge.setVisibility(View.VISIBLE);
            holder.patientType.setVisibility(View.VISIBLE);
            holder.doctorName.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(registerParentData.get(position).getdRegDat());

        } else if (type == "Patient") {

        } else if (type.equals("reportdelivery")) {
            holder.actualHospitalname.setVisibility(View.VISIBLE);
            holder.nikshayId.setVisibility(View.VISIBLE);

            holder.patientAge.setVisibility(View.GONE);
            holder.patientType.setVisibility(View.GONE);
            holder.patientImage.setVisibility(View.GONE);
            holder.sexImage.setVisibility(View.GONE);


            holder.doctorName.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(registerParentData.get(position).getdSpecmCol());
            if (registerParentData.get(position).getDiag_test() != null) {
                holder.doctorName.setText(registerParentData.get(position).getDiag_test() + String.format("(%s)", registerParentData.get(position).getTestReas()));
            }
        } else {
            if (registerParentData.get(position).getcDocNam() != null) {
                holder.doctorName.setText(registerParentData.get(position).getcDocNam());
            }
        }

        if (registerParentData.get(position).isChecked()) {
            holder.radioButton.setChecked(true);
            holder.radioButtonOne.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
            holder.radioButtonOne.setChecked(false);
        }
        if (((Activity) context).getIntent().hasExtra("hospitaltypeName")) {
            holder.actualHospitalname.setText(registerParentData.get(position).getC_hf_nam() + " / " + ((Activity) context).getIntent().getStringExtra("hospitaltypeName"));

        } else {
            holder.actualHospitalname.setText(registerParentData.get(position).getC_hf_nam());

        }

        //holder.date.setText(registerParentData.get(position).getdRegDat());
        holder.patientAge.setText(registerParentData.get(position).getnAge());

        if (registerParentData.get(position).getN_sex().equals("")) {
            holder.patientType.setText(registerParentData.get(position).getcTyp());
        } else if (registerParentData.get(position).getN_sex().equals("1")) {
            holder.patientType.setText("Male");
        } else if (registerParentData.get(position).getN_sex().equals("2")) {
            holder.patientType.setText("Female");
        } else {
            holder.patientType.setText("TG");
        }
        //holder.patientType.setText(registerParentData.get(position).getcTyp());
        // holder.address.setText(registerParentData.get(position).getcAdd());
        holder.nikshayId.setText(registerParentData.get(position).getnNkshId());
      //  holder.tvFromFDC.setText();
        //  holder.phone.setText(registerParentData.get(position).getC_mob());
        if (registerParentData.get(position).getC_mob() == null || registerParentData.get(position).getC_mob().equals("")) {
            holder.hospitalName.setText(registerParentData.get(position).getcPatNam());

        } else {
            holder.hospitalName.setText(registerParentData.get(position).getcPatNam() + " (" + registerParentData.get(position).getC_mob() + ") ");

        }
        holder.hospitalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + registerParentData.get(position).getC_mob()));
                context.startActivity(intent);
            }
        });

        if (registerParentData.get(position).getTrans_out() != null) {
            if (!registerParentData.get(position).getTrans_out().equals("")) {
                holder.transferShow.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                holder.transferShow2.setBackground(context.getDrawable(R.drawable.back_cardview2));
                holder.radioButton.setVisibility(View.GONE);
                holder.radioButtonOne.setVisibility(View.GONE);
            }

        } else {
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
                        Log.d("bysdgvtdg", "patient: niksh_id" + registerParentData.get(position).getnNkshId());
                        Log.d("bysdgvtdg", "patient: LST_DT" + registerParentData.get(position).getLST_DT());

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
                        intent.putExtra("diag_test", registerParentData.get(position).getDiag_test());
                        intent.putExtra("resn", registerParentData.get(position).getTestReas());
                        intent.putExtra("hf_nam", registerParentData.get(position).getC_hf_nam());
                        intent.putExtra("n_diag_cd", registerParentData.get(position).getN_diag_cd());
                        intent.putExtra("d_sample", registerParentData.get(position).getdSpecmCol());
                        intent.putExtra("smpl_col_id", registerParentData.get(position).getSmpl_col_id());
                        intent.putExtra("hospitaltypename", registerParentData.get(position).getC_hf_typ());
                        LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return registerParentData.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hospitalName,/*phone*//*address,*/
                nikshayId, patientAge, patientType, actualHospitalname, date, doctorName,tvFromFDC;
        CheckBox radioButton;
        RadioButton radioButtonOne;
        LinearLayout transferShow2, transferShow;
        ImageView sexImage, patientImage, editIcon;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.customerName);
            actualHospitalname = itemView.findViewById(R.id.hospitalnamett);
            transferShow = itemView.findViewById(R.id.transfer_show);
            transferShow2 = itemView.findViewById(R.id.transfer_show2);


            sexImage = itemView.findViewById(R.id.sexImage);

            //   phone = itemView.findViewById(R.id.phone);
            // address = itemView.findViewById(R.id.address);
            nikshayId = itemView.findViewById(R.id.regid);
            tvFromFDC=itemView.findViewById(R.id.tvFromFDC);
            doctorName = itemView.findViewById(R.id.doctorname);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButtonOne = itemView.findViewById(R.id.radioButtonOne);
            date = itemView.findViewById(R.id.datetxt);
            editIcon = itemView.findViewById(R.id.editIcon);
            patientAge = itemView.findViewById(R.id.agetxt);
            patientType = itemView.findViewById(R.id.typeCl);
            patientImage = itemView.findViewById(R.id.people_img);
        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        registerParentData = list;
        notifyDataSetChanged();
    }
}
