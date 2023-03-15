package com.smit.ppsa.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.RoomSampleList;
import com.smit.ppsa.TestList;

import java.util.List;

public class SmapleAdapter extends RecyclerView.Adapter<SmapleAdapter.Mholder> {
    private List<RoomSampleList> nList;
    private Activity context;

    public SmapleAdapter(List<RoomSampleList> registerParentData, Activity context) {
        this.nList = registerParentData;
        this.context = context;
    }

    @NonNull
    @Override
    public Mholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Mholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sample,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Mholder holder, @SuppressLint("RecyclerView") int position) {
        if (nList.get(position).getdSpecmCol()==null){
            holder.doctorname.setText("--");
        }else{
            holder.doctorname.setText(nList.get(position).getdSpecmCol());
        }
        if (nList.get(position).getTestReas() ==null){
            holder.qualificationone.setText("--");
        }else{
            holder.qualificationone.setText(nList.get(position).getTestReas());
        }
        if (nList.get(position).getSpecmTyp() == null){
            holder.qualificationtwo.setText("--");
        }else{
            holder.qualificationtwo.setText(nList.get(position).getSpecmTyp());
        }
        if (nList.get(position).getcPlcSampCol() == null){
            holder.conatactno.setText("--");
        }else {
            holder.conatactno.setText(nList.get(position).getDiag_test());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TestList.class).putExtra("enroll_id",context.getIntent().getStringExtra("enroll_id")).
                        putExtra("n_smpl_col_id",nList.get(position).getId()).
                        putExtra("d_smpl_recd",nList.get(position).getdSpecmCol()).
                        putExtra("hf_id",nList.get(position).getnHfId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class Mholder extends RecyclerView.ViewHolder {
        private TextView doctorname, qualificationone, qualificationtwo, conatactno;


        public Mholder(@NonNull View itemView) {
            super(itemView);
            doctorname = itemView.findViewById(R.id.doctor_name);
            qualificationone = itemView.findViewById(R.id.qualification_one);
            qualificationtwo = itemView.findViewById(R.id.qualification_two);
            conatactno = itemView.findViewById(R.id.conatact_number);
        }
    }
}
