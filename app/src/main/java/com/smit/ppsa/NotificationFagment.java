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

import com.smit.ppsa.Dao.AppDataBase;


public class NotificationFagment extends DialogFragment {

    TextView yesBtn, noBtn;
    private AppDataBase dataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_fagment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBase = AppDataBase.getDatabase(getActivity());
 /*       if (BaseUtils.getFormOne(getActivity()).equals("false")) {
            LiveData<List<FormOneModel>> allCustomer = dataBase.customerDao().fetchFormOne();
            allCustomer.observe(this, hospitalModels -> {

                if (hospitalModels.size() != 0) {
                    for (int a = 0; a < hospitalModels.size(); a++) {
                        Log.d("lplpl", "onCreate: " + hospitalModels.get(a).getN_st_id());

                        NetworkCalls.sendForm(getActivity(),
                                hospitalModels.get(a).getN_st_id(),
                                hospitalModels.get(a).getN_dis_id(),
                                hospitalModels.get(a).getN_tu_id(),
                                hospitalModels.get(a).getN_hf_id(),
                                hospitalModels.get(a).getN_doc_id(),
                                hospitalModels.get(a).getD_reg_dat(),
                                hospitalModels.get(a).getN_nksh_id(),
                                hospitalModels.get(a).getC_pat_nam(),
                                hospitalModels.get(a).getN_age(),
                                hospitalModels.get(a).getN_sex(),
                                hospitalModels.get(a).getN_wght(),
                                hospitalModels.get(a).getN_hght(),
                                hospitalModels.get(a).getC_add(),
                                hospitalModels.get(a).getC_taluka(),
                                hospitalModels.get(a).getC_town(),
                                hospitalModels.get(a).getC_ward(),
                                hospitalModels.get(a).getC_lnd_mrk(),
                                hospitalModels.get(a).getN_pin(),
                                hospitalModels.get(a).getN_st_id_res(),
                                hospitalModels.get(a).getN_dis_id_res(),
                                hospitalModels.get(a).getN_tu_id_res(),
                                hospitalModels.get(a).getC_mob(),
                                hospitalModels.get(a).getC_mob_2(),
                                hospitalModels.get(a).getN_lat(),
                                hospitalModels.get(a).getN_lng(),
                                hospitalModels.get(a).getN_user_id(),
                                "",
                                false,
                                hospitalModels.get(a).getNotification_image(),
                                hospitalModels.get(a).getBank_image(),
                                hospitalModels.get(a).getN_sac_id()
                        );
                    }
                }

            });

        }*/
        yesBtn = view.findViewById(R.id.yesbtn);
        noBtn = view.findViewById(R.id.nobtn);

        String hfId;
        String docId;

        Bundle bundle = this.getArguments();

        hfId = bundle.getString("hf_id", "");
       docId = bundle.getString("doc_id", "");


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MainActivity.class).putExtra("reportdelivery", "reportdelivery"));
                getActivity().finishAffinity();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseUtils.putSection(getActivity(),"addpat");
                // startActivity(new Intent(getActivity(), FormTwo.class).putExtra("hf_id", hfId).putExtra("tu_id", tuId).putExtra("hospitalName", hospitalName).putExtra("fdc", "fdc"));
                startActivity(new Intent(getActivity(), HospitalFacility.class)
                        .putExtra("type", "provider")
                        .putExtra("doc_id", docId)
                        .putExtra("type","normal")
                        .putExtra("hf_id", hfId).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                dismiss();
            }
        });
    }
}