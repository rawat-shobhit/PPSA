package com.example.ppsa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ppsa.R;
import com.example.ppsa.Response.QualificationList;

import java.util.List;

public class SpinAdapter extends ArrayAdapter<QualificationList> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<QualificationList> values;

    public SpinAdapter(Context context, List<QualificationList> values) {
        super(context, 0, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size()+1;
    }

    @Override
    public QualificationList getItem(int position){
        if (position == 0){
            return new QualificationList();
        }else{
            return values.get(position-1);
        }

    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinnerlayout, parent, false);
        }
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) convertView.findViewById(R.id.label);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        if (position == 0){
            label.setText("Select");
        }else{
            if (!values.get(position-1).getcQual().equals("")){
                label.setText(values.get(position-1).getcQual());
            }else if (!values.get(position-1).getcQualf().equals("")){
                label.setText(values.get(position-1).getcQualf());
            }else if (!values.get(position-1).getC_hf_typ().equals("")){
                label.setText(values.get(position-1).getC_hf_typ());
            }else if (!values.get(position-1).getC_soe().equals("")){
                label.setText(values.get(position-1).getC_soe());
            }else if(!values.get(position-1).getC_typ().equals("")){
                label.setText(values.get(position-1).getC_typ());
            }
        }


        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinnerlayout, parent, false);
        }
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) convertView.findViewById(R.id.label);
        label.setTextColor(Color.BLACK);
        if (position == 0){
            label.setText("Select");
        }else{
            if (!values.get(position-1).getcQual().equals("")){
                label.setText(values.get(position-1).getcQual());
            }else if (!values.get(position-1).getcQualf().equals("")){
                label.setText(values.get(position-1).getcQualf());
            }else if (!values.get(position-1).getC_hf_typ().equals("")){
                label.setText(values.get(position-1).getC_hf_typ());
            }else if (!values.get(position-1).getC_soe().equals("")){
                label.setText(values.get(position-1).getC_soe());
            }else if(!values.get(position-1).getC_typ().equals("")){
                label.setText(values.get(position-1).getC_typ());
            }
        }

        return convertView;
    }
}