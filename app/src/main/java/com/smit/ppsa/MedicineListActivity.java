package com.smit.ppsa;

import android.os.Bundle;

import com.smit.ppsa.Adapter.MedicineAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.MedicineTestJa;
import com.smit.ppsa.Response.RoomMedicines;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.databinding.ActivityMedicineListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineListActivity extends AppCompatActivity {
    private CardView proceedbtn;
    private ImageView backBtn, hl_addbtn;
    private GlobalProgressDialog progressDialog;
    private TextView nextbtn;
    private EditText search;
    private RecyclerView medicinerecycler;
    MedicineAdapter medicineAdapter;
    List<MedicineTestJa> medicineList = new ArrayList<>();
    private AppDataBase dataBase;
    List<RoomMedicines> parentDataMedicines;
    MedicinesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        mViewModel = new ViewModelProvider(this).get(MedicinesViewModel.class);
        proceedbtn = findViewById(R.id.bt_proceedtwo);
        dataBase = AppDataBase.getDatabase(this);
        backBtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        progressDialog = new GlobalProgressDialog(this);
        medicinerecycler = findViewById(R.id.f2_patientrecycler);

        search = findViewById(R.id.search);

        getMedicines();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineListActivity.this.onBackPressed();

            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (medicineList.size() == 0) {

                } else {
                    filter(editable.toString());
                }

            }
        });

    }

    private void setHospitalRecycler(List<RoomMedicines> medicineList) {
        medicineAdapter = new MedicineAdapter(medicineList, MedicineListActivity.this, "activity",mViewModel);
        medicinerecycler.setLayoutManager(new LinearLayoutManager(this));
        medicinerecycler.setAdapter(medicineAdapter);
    }

    private void filter(String text) {
        ArrayList<RoomMedicines> temp = new ArrayList();
        for (RoomMedicines d : parentDataMedicines) {
            String value = d.getC_val().toLowerCase();
            if (value.contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        medicineAdapter.updateList(temp);
    }

    private void getMedicines() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(MedicineListActivity.this)) {
            Toast.makeText(MedicineListActivity.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(MedicineListActivity.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(MedicineListActivity.this).getnUserLevel());


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
        medicinesFromRoom.observe(MedicineListActivity.this, roomMedicines -> {
            parentDataMedicines = roomMedicines;


            setHospitalRecycler(parentDataMedicines);

        });
    }

}