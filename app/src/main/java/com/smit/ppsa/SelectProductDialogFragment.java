package com.smit.ppsa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Adapter.MedicineAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.MedicineTestJa;
import com.smit.ppsa.Response.RoomMedicines;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectProductDialogFragment extends DialogFragment {

    RecyclerView medicineRecyclerView;
    MedicineAdapter medicineAdapter;
    List<MedicineTestJa> medicineList = new ArrayList<>();
    List<RoomMedicines> parentDataMedicines;
    private GlobalProgressDialog progressDialog;
    private AppDataBase dataBase;
    TextView selectBtn;
    MedicinesViewModel mViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_select_product_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MedicinesViewModel.class);
        medicineRecyclerView =  view.findViewById(R.id.pd_rv_style);
        medicineRecyclerView =  view.findViewById(R.id.pd_rv_style);
        progressDialog = new GlobalProgressDialog(getActivity());
        dataBase = AppDataBase.getDatabase(getActivity());

        getMedicines();

    }
    private void setHospitalRecycler(List<RoomMedicines> medicineList) {
        medicineAdapter = new MedicineAdapter(medicineList, getActivity(), "fragment", mViewModel);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        medicineRecyclerView.setAdapter(medicineAdapter);
    }

    private void getMedicines() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(getActivity())) {
            BaseUtils.showToast(getActivity(), "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(getActivity(), getActivity().getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(getActivity()).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_med&w=id<<GT>>0";
        ApiClient.getClient().getMedicine(url).enqueue(new Callback<MedicineResponse>() {
            @Override
            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataTestReportResults = response.body().getUser_data();

                        Log.d("gugwdwd", "onResponse: " + response.body().getStatus());
                        Log.d("gugwdwd", "onResponse: " + response.body().getUser_data());

                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomMedicines roomMedicines = new RoomMedicines(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomMedicines.getId());
                            dataBase.customerDao().getMedicinesFromServer(roomMedicines);
                        }


                        getRoomMedicines();

                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<MedicineResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomMedicines() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomMedicines>> medicinesFromRoom = dataBase.customerDao().getSelectedMedicinesFromRoom();
        medicinesFromRoom.observe(getActivity(), roomMedicines -> {
            parentDataMedicines = roomMedicines;


            setHospitalRecycler(parentDataMedicines);

        });
    }
}