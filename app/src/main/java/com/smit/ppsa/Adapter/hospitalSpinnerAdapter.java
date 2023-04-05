package com.smit.ppsa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smit.ppsa.R;
import com.smit.ppsa.Response.HospitalList;

import java.util.List;

public class hospitalSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<HospitalList> hospitalLists ;

    public hospitalSpinnerAdapter(Context context,List<HospitalList> hospitalLists){
        this.context=context;
        this.hospitalLists=hospitalLists;
    }


    @Override
    public int getCount() {
        return hospitalLists!=null ? hospitalLists.size():0 ;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View  rootView= LayoutInflater.from(context).inflate(R.layout.layout_hospital_spinner,parent,false);

        TextView hospitalName=rootView.findViewById(R.id.hospitalName);

        hospitalName.setText(hospitalLists.get(position).getcHfNam());


        return  rootView;

    }
}
