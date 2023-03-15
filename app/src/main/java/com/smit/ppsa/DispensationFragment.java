package com.smit.ppsa;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DispensationFragment extends DialogFragment {

    TextView yesBtn, noBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dispensation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yesBtn = view.findViewById(R.id.yesbtn);
        noBtn = view.findViewById(R.id.nobtn);

        String hfId;
        String tuId;
        String hospitalName;

        Bundle bundle = this.getArguments();

        hfId = bundle.getString("hf_id", "");
        tuId = bundle.getString("tu_id", "");
        hospitalName = bundle.getString("hospitalName", "");


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MainActivity.class).putExtra("fdc", "fdc"));
                getActivity().finishAffinity();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FormTwo.class).putExtra("hf_id", hfId).putExtra("tu_id", tuId).putExtra("hospitalName", hospitalName).putExtra("fdc", "fdc"));
                getActivity().finish();

            }
        });
    }
}