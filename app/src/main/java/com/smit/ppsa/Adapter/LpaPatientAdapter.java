package com.smit.ppsa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Parcelable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.FdcForm;
import com.smit.ppsa.FormTwo;
import com.smit.ppsa.LpaPatient;
import com.smit.ppsa.R;
import com.smit.ppsa.RefillPatientList;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.TransferPatient;
import com.smit.ppsa.TuSearchPatientList;
import com.smit.ppsa.UploadDoc;
import com.smit.ppsa.locationHelper.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LpaPatientAdapter extends RecyclerView.Adapter<LpaPatientAdapter.Mholder> {
    private List<RegisterParentData> registerParentData;
    private Context context;
    private boolean isComingForReport;
    public LpaPatientAdapter(List<RegisterParentData> registerParentData, Context context) {
        this.registerParentData = registerParentData;
        this.context = context;
    }

    @NonNull
    @Override
    public LpaPatientAdapter.Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(context).inflate(R.layout.row_patient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LpaPatientAdapter.Mholder holder, int position) {
        RegisterParentData parentData = registerParentData.get(position);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);

        holder.nikshayId.setText(parentData.getnNkshId());
        holder.patientAge.setText(parentData.getnAge());
        holder.patientType.setText(parentData.getcTyp());
        holder.actualHospitalname.setText(parentData.getC_hf_nam());
        holder.doctorName.setText(parentData.getcDocNam());

        holder.hospitalName.setText(parentData.getcPatNam());
        String newDate="";
        if (context instanceof RefillPatientList) {
             newDate = parentData.getD_lst_dispn();
        } else {
             newDate = parentData.getdRegDat();
        }


        holder.doctorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+parentData.getC_mob()));
               context.startActivity(intent);
            }
        });
       /* if (context instanceof TuSearchPatientList &&
                !((TuSearchPatientList) context).getIntent().hasExtra("upload") &&
                parentData.getTrans_out() != null) {
            holder.istransfer.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
            holder.istransfer2.setBackground(context.getDrawable(R.drawable.back_cardview2));
        }*/
        if (context instanceof RefillPatientList) {
            Toast.makeText(context, "this", Toast.LENGTH_SHORT).show();
            holder.date.setText(parentData.getD_lst_dispn());
            holder.ddayLeft.setText((Utils.Companion.getDateDiff(df,formattedDate,newDate)+" Day Left"));
            holder.doctorName.setText(parentData.getC_mob());
            holder.doctor_img.setImageResource( R.drawable.ic_baseline_phone_24);

        } else {
            holder.date.setText(parentData.getdRegDat());
            holder.ddayLeft.setVisibility(View.GONE);
            Toast.makeText(context, "this2", Toast.LENGTH_SHORT).show();

        }


        if (context instanceof TuSearchPatientList) {
            if (((TuSearchPatientList) context).getIntent().hasExtra("upload")) {
                holder.user_img.setImageResource(R.drawable.new_user_img);
                holder.hospital_img.setImageResource(R.drawable.new_hospital_img);
                holder.doctor_img.setImageResource(R.drawable.new_doctor_img);
                holder.people_img.setImageResource(R.drawable.new_people_img);
            }}

        if (parentData.getTrans_out()!=null){
            if (!parentData.getTrans_out().equals("")){
                holder.istransfer.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                holder.istransfer2.setBackground(context.getDrawable(R.drawable.back_cardview2));
            }
        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof TuSearchPatientList) {
                        if (((TuSearchPatientList) context).getIntent().hasExtra("upload")) {
                            Intent intent = new Intent(context, UploadDoc.class);
                            intent.putExtra("pat_ob", parentData);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, TransferPatient.class);
                            intent.putExtra("pat_ob", parentData);
                            context.startActivity(intent);
                        }

                    } else if (context instanceof LpaPatient) {
                        Intent intent = new Intent(context, LPAForm.class);
                        intent.putExtra("pat_ob", parentData);
                        context.startActivity(intent);
                    } else if (context instanceof RefillPatientList) {
                        Intent intent = new Intent(context, FdcForm.class);
                        intent.putExtra("tu_id", parentData.getnTuId());
                        intent.putExtra("hf_id", parentData.getnHfId());
                        intent.putExtra("hospitalName", parentData.getC_hf_nam());
                        intent.putExtra("docName", parentData.getcDocNam());
                        intent.putExtra("doc_id", parentData.getnDocId());
                        intent.putExtra("issued", "patient");
                        intent.putExtra("patient_name", parentData.getcPatNam());
                        intent.putExtra("enroll_id", parentData.getN_enroll_id());
                        context.startActivity(intent);
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
                nikshayId, patientAge, patientType, actualHospitalname, date, doctorName,ddayLeft;
        CheckBox radioButton;
        ImageView user_img,people_img,doctor_img,hospital_img;
        RadioButton radioButtonOne;
        LinearLayout istransfer, istransfer2;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.customerName);
            actualHospitalname = itemView.findViewById(R.id.hospitalnamett);
            istransfer = itemView.findViewById(R.id.transfer_show);
            istransfer2 = itemView.findViewById(R.id.transfer_show2);
            user_img = itemView.findViewById(R.id.user_img);
            people_img = itemView.findViewById(R.id.people_img);
            doctor_img = itemView.findViewById(R.id.doctor_img);
            hospital_img = itemView.findViewById(R.id.hospital_img);
            ddayLeft = itemView.findViewById(R.id.ddayLeft);
            //   phone = itemView.findViewById(R.id.phone);
            // address = itemView.findViewById(R.id.address);
            nikshayId = itemView.findViewById(R.id.regid);
            doctorName = itemView.findViewById(R.id.doctorname);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButtonOne = itemView.findViewById(R.id.radioButtonOne);
            date = itemView.findViewById(R.id.datetxt);
            patientAge = itemView.findViewById(R.id.agetxt);
            patientType = itemView.findViewById(R.id.typeCl);
        }
    }

    public void updateList(ArrayList<RegisterParentData> list) {
        registerParentData = list;
        notifyDataSetChanged();
    }
}
