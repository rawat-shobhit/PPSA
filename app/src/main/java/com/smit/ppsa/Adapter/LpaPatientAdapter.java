package com.smit.ppsa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.FdcForm;
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
import java.util.Objects;

public class LpaPatientAdapter extends RecyclerView.Adapter<LpaPatientAdapter.Mholder> {
    private List<RegisterParentData> registerParentData;
    private final Context context;
    private boolean isComingForReport;
    private final String type;

    public LpaPatientAdapter(List<RegisterParentData> registerParentData, Context context, String type) {
        this.registerParentData = registerParentData;
        this.context = context;
        this.type = type;
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

        if (Objects.equals(type, "photo")) {
            holder.details_Img.setVisibility(View.VISIBLE);
        }


        try {
            if (parentData.getAadhar_img().equals("0") || parentData.getAadhar_img().equals("null")) {
                holder.adhar.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if (parentData.getAadhar_img().equals("1")) {
                holder.adhar.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }


        try {
            if (parentData.getBnk_img().equals("0") || parentData.getBnk_img().equals("null")) {
                holder.bank.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else  if(parentData.getBnk_img().equals("1")){
                holder.bank.setBackgroundColor(Color.parseColor("#5EC362"));
            }

        } catch (Exception e) {

        }


        try {
            if (parentData.getPrescImg().equals("0") || parentData.getPrescImg().equals("null")) {

                holder.pres.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if(parentData.getPrescImg().equals("1")) {

                holder.pres.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }


        try {
            if (parentData.getTstRptImg().equals("0") || parentData.getTstRptImg().equals("null")) {
                holder.tst.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if (parentData.getTstRptImg().equals("1")) {
                holder.tst.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }


        try {
            if (parentData.getC_udst_img().equals("0") || parentData.getC_udst_img().equals("null")) {
                holder.udst.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if(parentData.getC_udst_img().equals("1")) {
                holder.udst.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }


        try {
            if (parentData.getC_diab_img().equals("0") || parentData.getC_diab_img().equals("null")) {
                holder.diab.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if(parentData.getC_diab_img().equals("1")) {
                holder.diab.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }


        try {
            if (parentData.getC_hiv_img().equals("0") || parentData.getC_hiv_img().equals("null")) {
                holder.hiv.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if(parentData.getC_hiv_img().equals("1")) {
                holder.hiv.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }

        try {
            Log.d("checkingData", "andhar->"+parentData.getAadhar_img() + " bank->" + parentData.getBnk_img() + " PrescImg->"+parentData.getPrescImg() + " TstRptImg->" + parentData.getTstRptImg() + " getC_udst_img()->" + parentData.getC_udst_img() + " diabetic->" + parentData.getC_diab_img() + " notification->" + parentData.getC_not_img());


        } catch (Exception e) {
            Log.d("checkingDataCrash", e.toString());
        }


        try {
            if (parentData.getC_add_presc_img().equals("0") || parentData.getC_add_presc_img().equals("null")) {
                holder.additionalprescription.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if(parentData.getC_add_presc_img().equals("1")) {
                holder.additionalprescription.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }

        try {
            if (parentData.getC_con_frm_img().equals("0") || parentData.getC_con_frm_img().equals("null")) {
                holder.consent.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if (parentData.getC_con_frm_img().equals("1")) {
                holder.consent.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }

        try {
            if (parentData.getC_not_img().equals("0") || parentData.getC_not_img().equals("null")) {
                holder.notification.setBackgroundColor(Color.parseColor("#FFFF5151"));
            } else if(parentData.getC_not_img().equals("1")) {
                holder.notification.setBackgroundColor(Color.parseColor("#5EC362"));
            }
        } catch (Exception e) {
        }


        holder.hospitalName.setText(parentData.getcPatNam());
        String newDate = "";
        if (context instanceof RefillPatientList) {
            newDate = parentData.getD_lst_dispn();
        } else {
            newDate = parentData.getdRegDat();
        }


        holder.doctorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + parentData.getC_mob()));
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
            holder.ddayLeft.setText((Utils.Companion.getDateDiff(df, formattedDate, newDate) + " Day Left"));
            holder.doctorName.setText(parentData.getC_mob());
            holder.doctor_img.setImageResource(R.drawable.ic_baseline_phone_24);

        } else {
            holder.date.setText(parentData.getdRegDat());
            //   holder.ddayLeft.setVisibility(View.GONE);
            //  Toast.makeText(context, "this2", Toast.LENGTH_SHORT).show();

        }

        if (context instanceof LpaPatient) {
            holder.hospitalName.setText(parentData.getcPatNam() + "(" + parentData.getC_mob() + ")");
        }


        if (context instanceof TuSearchPatientList) {
            if (((TuSearchPatientList) context).getIntent().hasExtra("upload")) {
                holder.user_img.setImageResource(R.drawable.new_user_img);
                holder.hospital_img.setImageResource(R.drawable.new_hospital_img);
                holder.doctor_img.setImageResource(R.drawable.new_doctor_img);
                holder.people_img.setImageResource(R.drawable.new_people_img);
            }
        }

        if (parentData.getTrans_out() != null) {
            if (!parentData.getTrans_out().equals("")) {
                holder.istransfer.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
                holder.istransfer2.setBackground(context.getDrawable(R.drawable.back_cardview2));
            }
        } else {
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

        return Math.min(registerParentData.size(), 100);
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hospitalName,/*phone*//*address,*/
                nikshayId, patientAge, patientType, actualHospitalname, date, doctorName, ddayLeft, adhar, bank, pres, tst, udst, diab, hiv, additionalprescription, consent, notification;
        CheckBox radioButton;
        ImageView user_img, people_img, doctor_img, hospital_img;
        RadioButton radioButtonOne;
        LinearLayout istransfer, istransfer2, details_Img;

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
            adhar = itemView.findViewById(R.id.adhaar);
            bank = itemView.findViewById(R.id.bank);
            pres = itemView.findViewById(R.id.pres);
            tst = itemView.findViewById(R.id.tst);
            udst = itemView.findViewById(R.id.udst);
            diab = itemView.findViewById(R.id.diab);
            hiv = itemView.findViewById(R.id.hiv);
            additionalprescription = itemView.findViewById(R.id.additionalprescription);
            consent = itemView.findViewById(R.id.consent);
            notification = itemView.findViewById(R.id.notification);

            details_Img = itemView.findViewById(R.id.details_Img);

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
