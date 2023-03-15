package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.BaseUtils;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.FdcDispensationToHf;
import com.smit.ppsa.FdcForm;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.R;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.PrevVisitsData;
import com.smit.ppsa.Response.PrevVisitsResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FdcHospitalsAdapter extends RecyclerView.Adapter<FdcHospitalsAdapter.Mholder> {
    private List<HospitalList> hospitalLists;
    private List<HospitalList> newhospitalLists;
    private Context context;
    private String type, hfID;
    int prevPos = -1;
    AppDataBase dataBase;


    public FdcHospitalsAdapter(List<HospitalList> nList, Context context, String type, String hfID, AppDataBase dataBase) {
        this.hospitalLists = nList;
        newhospitalLists = hospitalLists;
        this.context = context;
        this.type = type;
        this.dataBase = dataBase;
        this.hfID = hfID;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fdc_hospital_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Mholder holder, @SuppressLint("RecyclerView") int position) {


        holder.radioButton.setClickable(false);
        holder.radioButton.setFocusable(false);
        /*if ( hospitalLists.get(position).isChecked()){


            //  hospitalLists.get(position).setChecked(false);
        }else {
            // hospitalLists.get(position).setChecked(true);


            // hospitalLists.get(position).setChecked(true);
        }*/

        if (hospitalLists.get(position).isChecked()) {
            holder.radioButton.setChecked(true);

            holder.container.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.contone.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.contthree.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.contfr.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.contfv.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"));
            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"));

        } else {
            holder.container.setBackgroundColor(Color.WHITE);
            holder.contone.setBackgroundColor(Color.WHITE);
            holder.conttwo.setBackgroundColor(Color.parseColor("#DFDFDE"));
            holder.contthree.setBackgroundColor(Color.WHITE);
            holder.contfr.setBackgroundColor(Color.parseColor("#DFDFDE"));
            holder.contfv.setBackgroundColor(Color.WHITE);
            holder.contsix.setBackgroundColor(Color.WHITE);
            holder.radioButton.setChecked(false);
        }

        holder.hospitalName.setText(hospitalLists.get(position).getcHfNam());
        holder.location.setText(hospitalLists.get(position).getcDisNam() + "," + hospitalLists.get(position).getcTuName());
        holder.doctorName.setText(hospitalLists.get(position).getcContPer());
        holder.typeClinic.setText(hospitalLists.get(position).getcHfTyp());
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater1 = new SimpleDateFormat("yyyy/MM");
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //holder.table.setVisibility(View.GONE);
        if (type.equals("fdc")) {
            holder.bagImg.setVisibility(View.VISIBLE);
            //holder.table.setVisibility(View.GONE);
        } else if (type.equals("provider")) {
            holder.contfr.setVisibility(View.GONE);
            holder.contthree.setVisibility(View.GONE);
            if (!hospitalLists.get(position).isChecked()) {
                if (hospitalLists.get(position).getLst_visit() == null || hospitalLists.get(position).getLst_visit().isEmpty()) {

                } else {
                    try {
                        currentTime = curFormater.parse(hospitalLists.get(position).getLst_visit());
                        if (BaseUtils.sameMonth(holder.itemView.getContext(), curFormater1.format(currentTime))) {
                            holder.container.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.contone.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.contthree.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.contfr.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.contfv.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFE3"));
                            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFE3"));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        holder.hospitalNameTt.setText(hospitalLists.get(position).getcHfNam());
        holder.hospitalNameLocation.setText(hospitalLists.get(position).getcDisNam() + ", " + hospitalLists.get(position).getcStNam());
        holder.doctorNameTv.setText(hospitalLists.get(position).getcContPer());
        holder.hospitalTypeTitle.setText(hospitalLists.get(position).getcHfTyp());


        String formattedDate = "";
        if (!hospitalLists.isEmpty()) {
            // for (int i = 0; i < hospitalLists.size(); i++) {
            if (hospitalLists.get(position).getLst_visit() != null) {

                for (int j = 0; j < hospitalLists.get(position).getLst_visit().length(); j++) {
                    if (j < 10) {
                        formattedDate = formattedDate + hospitalLists.get(position).getLst_visit().charAt(j);
                    }
                    String day = "Day";
                    if (Integer.parseInt(hospitalLists.get(position).getNo_of_days()) > 2) {
                        day = "Days";
                    }
                    holder.currentDate.setText(formattedDate + " (" + hospitalLists.get(position).getNo_of_days() + " " + day + ")");
                    holder.currentDate.setVisibility(View.VISIBLE);
                }

            } else {
                holder.currentDate.setVisibility(View.INVISIBLE);
            }

            //}
        }


        String ext = "";
        if (hospitalLists.get(position).getNo_of_days() != null) {
            if (Integer.valueOf(hospitalLists.get(position).getNo_of_days()) < 2) {
                ext = "Day";
            } else {
                ext = "Days";
            }
        }
        if (hospitalLists.get(position).getNo_of_days() != null) {
            holder.lastVisit.setVisibility(View.GONE);
            holder.lastVisit.setText(hospitalLists.get(position).getNo_of_days() + " " + ext);
        }/*else  {
            holder.lastVisit.setVisibility(View.GONE);
        }*/

        holder.bagImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prevPos == -1) {

                } else {
                    hospitalLists.get(prevPos).setChecked(false);
                    //   holder.table.setVisibility(View.GONE);
                }
                hospitalLists.get(position).setChecked(true);
                // holder.table.setVisibility(View.GONE);
                prevPos = position;
                if (hospitalLists.get(position).getnHfId().equals("")) {
                    Toast.makeText(view.getContext(), "Can not add or view doctors because it is added in local due to no internet connection", Toast.LENGTH_SHORT).show();
                } else {


                    Intent intent = new Intent(context, FdcForm.class);
                    // Intent intent = new Intent(context, MedicineListActivity.class);
                    intent.setAction("Selected");
                    intent.putExtra("op", "");
                    intent.putExtra("tu_id", hospitalLists.get(position).getnTuId());
                    intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                    intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());
                    intent.putExtra("docName", hospitalLists.get(position).getcContPer());
                    //   intent.putExtra("docid", hospitalLists.get(position).get());
                    context.startActivity(intent);

                      /*  Intent intent = new Intent();
                        intent.setAction("");
                        intent.putExtra("checked", true);
                       *//* intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                        intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());*//*
                    intent.putExtra("tu_id", hospitalLists.get(position).getnTuId());
                    intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                    intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());
                    intent.putExtra("fdctype", "bag");
                    intent.putExtra("docName", hospitalLists.get(position).getcContPer());
                        LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                        notifyDataSetChanged();*/

                  /*  LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();*/
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prevPos == -1) {

                } else {
                    newhospitalLists.get(prevPos).setChecked(false);
                    //  holder.table.setVisibility(View.GONE);
                }

                notifyItemChanged(prevPos);

                prevPos = position;


            /*    if (hospitalLists.get(position).isChecked()) {
                    holder.radioButton.setChecked(true);

                    holder.container.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.contone.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.contthree.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.contfr.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.contfv.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"));
                    holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"));

                } else {
                    holder.container.setBackgroundColor(Color.WHITE);
                    holder.contone.setBackgroundColor(Color.WHITE);
                    holder.conttwo.setBackgroundColor(Color.parseColor("#DFDFDE"));
                    holder.contthree.setBackgroundColor(Color.WHITE);
                    holder.contfr.setBackgroundColor(Color.parseColor("#DFDFDE"));
                    holder.contfv.setBackgroundColor(Color.WHITE);
                    holder.contsix.setBackgroundColor(Color.WHITE);
                    holder.radioButton.setChecked(false);
                }*/
                /* if (hospitalLists.get(position).isChecked()) {*/
                //    holder.radioButton.setChecked(true);

            /*        holder.container.setBackgroundColor(Color.MAGENTA);
                    holder.contone.setBackgroundColor(Color.MAGENTA);
                    holder.conttwo.setBackgroundColor(Color.MAGENTA);
                    holder.contthree.setBackgroundColor(Color.MAGENTA);
                    holder.contfr.setBackgroundColor(Color.MAGENTA);
                    holder.contfv.setBackgroundColor(Color.MAGENTA);
                    holder.contsix.setBackgroundColor(Color.MAGENTA);
                    holder.contsix.setBackgroundColor(Color.MAGENTA);*/

              /*  } else {
                    holder.container.setBackgroundColor(Color.WHITE);
                    holder.contone.setBackgroundColor(Color.WHITE);
                    holder.conttwo.setBackgroundColor(Color.parseColor("#DFDFDE"));
                    holder.contthree.setBackgroundColor(Color.WHITE);
                    holder.contfr.setBackgroundColor(Color.parseColor("#DFDFDE"));
                    holder.contfv.setBackgroundColor(Color.WHITE);
                    holder.contsix.setBackgroundColor(Color.WHITE);
                    holder.radioButton.setChecked(false);
                }*/


                //  holder.table.setVisibility(View.VISIBLE);


                //  getPrevVisits(holder, hospitalLists.get(position).getnHfId());

                if (hospitalLists.get(position).getnHfId().equals("")) {
                    Toast.makeText(view.getContext(), "Can not add or view doctors because it is added in local due to no internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d("mkoi", "onClick: " + hospitalLists.get(position).getcHfTyp());
                    Log.d("mkoi", "onClick: " + hospitalLists.get(position).getnTuId());
                    newhospitalLists.get(position).setChecked(true);
                    if (type.equals("fdc")) {


                        Intent intent = new Intent(context, FdcDispensationToHf.class);

                        intent.setAction("Selected");
                        intent.putExtra("checked", true);

                        intent.putExtra("tu_id", hospitalLists.get(position).getnTuId());
                        intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                        intent.putExtra("hf_type_id", hospitalLists.get(position).getnHfTypId());
                        intent.putExtra("hospitaltypeName", hospitalLists.get(position).getcHfTyp());
                        intent.putExtra("hospitallocation", hospitalLists.get(position).getcDisNam() + ", " + hospitalLists.get(position).getcStNam());
                        intent.putExtra("fdctype", "dede");
                        intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());
                        intent.putExtra("docName", hospitalLists.get(position).getcContPer());
                        intent.putExtra("lastvisit", hospitalLists.get(position).getNo_of_days());
                        //   intent.putExtra("docid", hospitalLists.get(position).get());
                        //context.startActivity(intent);
                        LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                        notifyItemChanged(position);
                    } else {
                        // hospitalLists.get(position).setChecked(true);

                        Intent intent = new Intent();

                        new Handler().post(() -> {
                            intent.setAction("Selected");
                            intent.putExtra("checked", true);



                   /*     Log.d("muyu", "Hospital: tu_id " + hospitalLists.get(position).getnTuId());
                        Log.d("muyu", "Hospital: hf_id " + hospitalLists.get(position).getnHfId());*/
                            intent.putExtra("tu_id", hospitalLists.get(position).getnTuId());
                            intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                            intent.putExtra("hf_type_id", hospitalLists.get(position).getnHfTypId());
                            intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());
                            intent.putExtra("hospitaltypeName", hospitalLists.get(position).getcHfTyp());
                            intent.putExtra("hospitallocation", hospitalLists.get(position).getcDisNam() + ", " + hospitalLists.get(position).getcStNam());
                            intent.putExtra("docName", hospitalLists.get(position).getcContPer());
                            intent.putExtra("lastvisit", hospitalLists.get(position).getNo_of_days());

                            LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);

                        });


                        notifyItemChanged(position);


                    }
            /*        LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                    notifyDataSetChanged();*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitalLists.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        TextView hospitalName, location, doctorName, typeClinic;
        private TextView hospitalNameTt, hospitalNameLocation, doctorNameTv, hospitalTypeTitle,
                currentDate, hospitalNameTv, hospitalAddress, hospitalType, lastVisit, date;
        LinearLayout container, contone, conttwo, contthree, contfv, contsix;
        RelativeLayout contfr;
        ImageView bagImg;
        RadioButton radioButton;

        public Mholder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            location = itemView.findViewById(R.id.locationn);
            doctorName = itemView.findViewById(R.id.doctorname);
            typeClinic = itemView.findViewById(R.id.hospitaltype);
            bagImg = itemView.findViewById(R.id.bagimg);
            radioButton = itemView.findViewById(R.id.radioButton);
            container = itemView.findViewById(R.id.cont);
            contone = itemView.findViewById(R.id.contone);
            conttwo = itemView.findViewById(R.id.conttwo);
            contthree = itemView.findViewById(R.id.contthree);
            contfr = itemView.findViewById(R.id.contfr);
            contfv = itemView.findViewById(R.id.contfv);
            contsix = itemView.findViewById(R.id.contsixx);


            hospitalNameTt = itemView.findViewById(R.id.hospitalNameTitle);
            hospitalNameLocation = itemView.findViewById(R.id.locationHospital);
            doctorNameTv = itemView.findViewById(R.id.docname);
            hospitalTypeTitle = itemView.findViewById(R.id.hospitalTYpe);
            currentDate = itemView.findViewById(R.id.currentdate);
            lastVisit = itemView.findViewById(R.id.visitdays);
          /*  table = itemView.findViewById(R.id.laytable);
            //table.setVisibility(View.GONE);
            table.setFocusable(false);*/

        }

    }

    public void updateList(ArrayList<HospitalList> list) {
        hospitalLists = list;
        notifyDataSetChanged();
    }

    private void getRoomPrevVisits(List<PrevVisitsData> parentDataPreviousSamples, Mholder mholder) {

        Date currentTime = Calendar.getInstance().getTime();

        //getting difference in days
        Date date = null;
        Date dateOne = null;
        try {
            // date = curFormater.parse(parentDataPreviousSamples.get(1).getdVisit());
            String formattedDate = "";
            if (!parentDataPreviousSamples.isEmpty()) {
                for (int i = 0; i < parentDataPreviousSamples.get(0).getdVisit().length(); i++) {
                    if (i < 10) {
                        formattedDate = formattedDate + parentDataPreviousSamples.get(0).getdVisit().charAt(i);
                    }
                }
            }


            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
            dateOne = curFormater.parse(modifyDateLayout(formattedDate));
            date = curFormater.parse(curFormater.format(currentTime));
            long diffInMillies = Math.abs(dateOne.getTime() - date.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            String ext = "";
            if (diff < 2) {
                ext = "Day";
            } else {
                ext = "Days";
            }
            // mholder.lastVisit.setText(String.valueOf(diff + " " + ext));
            Log.d("bhdyigd", "init: " + curFormater.format(currentTime));
            Log.d("bhdyigd", "init: " + formattedDate);
            Log.d("bhdyigd", "init: " + diff);


        } catch (ParseException e) {
            Log.d("bhdyigd", "init: " + "failll");
            e.printStackTrace();
        }
        /*LiveData<List<RoomPrevVisitsData>> roomPrevVisitProviderFromRoom = dataBase.customerDao().getSelectedRoomPrevVisitProviderFromRoom();
        roomPrevVisitProviderFromRoom.observe(this, roomPreviousVisits1 -> {
            List<RoomPrevVisitsData> parentDataPreviousSamples;

            parentDataPreviousSamples = roomPreviousVisits1;

           *//* //getting difference in days
            Date date = null;
            Date dateOne = null;
            try {
                // date = curFormater.parse(parentDataPreviousSamples.get(1).getdVisit());
                String formattedDate = "";
                if (!parentDataPreviousSamples.isEmpty()) {
                    for (int i = 0; i < parentDataPreviousSamples.get(0).getdVisit().length(); i++) {
                        if (i < 10) {
                            formattedDate = formattedDate + parentDataPreviousSamples.get(0).getdVisit().charAt(i);
                        }
                    }
                }


                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                dateOne = curFormater.parse(modifyDateLayout(formattedDate));
                date = curFormater.parse(curFormater.format(currentTime));
                long diffInMillies = Math.abs(dateOne.getTime() - date.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                String ext = "";
                if (diff < 2) {
                    ext = "Day";
                } else {
                    ext = "Days";
                }
                mholder.lastVisit.setText(String.valueOf(diff + " " + ext));
                Log.d("bhdyigd", "init: " + curFormater.format(currentTime));
                Log.d("bhdyigd", "init: " + formattedDate);
                Log.d("bhdyigd", "init: " + diff);


            } catch (ParseException e) {
                Log.d("bhdyigd", "init: " + "failll");
                e.printStackTrace();
            }*//*
            //setRecycler();
        });*/
    }

    private String modifyDateLayout(String inputDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void getPrevVisits(Mholder holder, String hfID) {
        if (!BaseUtils.isNetworkAvailable(context)) {
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_visit_typ&w=n_hf_id<<EQUALTO>>" + hfID;
        ApiClient.getClient().getPrevVisits(url).enqueue(new Callback<PrevVisitsResponse>() {
            @Override
            public void onResponse(Call<PrevVisitsResponse> call, Response<PrevVisitsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        //dataBase.customerDao().deletePrevVisitProviderFromRoom();
                        /*for (int i = 0; i < response.body().getUserData().size(); i++) {
                            RoomPrevVisitsData roomPreviousVisits = new RoomPrevVisitsData(
                                    response.body().getUserData().get(i).getdVisit(),
                                    response.body().getUserData().get(i).getcVal(),
                                    response.body().getUserData().get(i).getnHfId()

                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousVisits.getcVal());

                            dataBase.customerDao().getPrevVisitsProviderFromServer(roomPreviousVisits);
                        }*/
                        getRoomPrevVisits(response.body().getUserData(), holder);

                    }
                }
            }

            @Override
            public void onFailure(Call<PrevVisitsResponse> call, Throwable t) {

            }
        });
    }

    public void setData(List<HospitalList> newHospitalLlist) {
        HospitalDiffUtil diffUtil = new HospitalDiffUtil(hospitalLists, newHospitalLlist);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        hospitalLists = newHospitalLlist;
        diffResult.dispatchUpdatesTo(this);
    }
}
