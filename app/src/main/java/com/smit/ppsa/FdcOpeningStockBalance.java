package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.RoomMedicines;
import com.smit.ppsa.Response.RoomUOM;
import com.smit.ppsa.Response.testreport.TestreportResponse;
import com.smit.ppsa.Response.uom.UOMResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FdcOpeningStockBalance extends AppCompatActivity {

    Spinner medicineSpinner, uomSpinner;
    EditText quantity, ethambutol;
    TextView hospitalName, doctorName, submitBtn;
    private ImageView backBtn;
    FdcOpeningStockBalanceViewModel mViewModel;
    String wayLatitude = "", wayLongitude = "";
    private FusedLocationProviderClient mFusedLocationClient;
    List<RoomMedicines> parentDataMedicines;
    private GlobalProgressDialog progressDialog;
    private AppDataBase dataBase;
    List<RoomUOM> parentDataUom;
    private String medicine ="", uom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdc_opening_stock_balance);
        dataBase = AppDataBase.getDatabase(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        mViewModel = new ViewModelProvider(this).get(FdcOpeningStockBalanceViewModel.class);
        progressDialog = new GlobalProgressDialog(this);


        medicineSpinner = findViewById(R.id.fdc2et);
        uomSpinner = findViewById(R.id.fdc3et);
        quantity = findViewById(R.id.fdc4et);
        ethambutol = findViewById(R.id.ethambutolet);
        hospitalName = findViewById(R.id.hospitalName);
        doctorName = findViewById(R.id.docname);
        backBtn = findViewById(R.id.backbtn);
        submitBtn = findViewById(R.id.submitbtnc);

        hospitalName.setText(getIntent().getStringExtra("hospitalName"));
        doctorName.setText(getIntent().getStringExtra("docName"));

        getRoomMedicines();
        getRoomUOM();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FdcOpeningStockBalance.super.onBackPressed();
            }
        });

        getFdcData();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        medicineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    medicine = "";
                    Log.d("dded", "onItemSelected: " + medicine);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                      medicine = parentDataMedicines.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + medicine);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        uomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    uom = "";
                    Log.d("dded", "onItemSelected: " + uom);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    uom = parentDataUom.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + uom);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getUOM();
        getMedicines();

    }

    private void getUOM() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcOpeningStockBalance.this)) {
            Toast.makeText(FdcOpeningStockBalance.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcOpeningStockBalance.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcOpeningStockBalance.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_uom&w=id<<GT>>0";
        ApiClient.getClient().getUOM(url).enqueue(new Callback<UOMResponse>() {
            @Override
            public void onResponse(Call<UOMResponse> call, Response<UOMResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataTestReportResults = response.body().getUser_data();

                        Log.d("gugwdwd", "onResponse: " + response.body().getStatus());
                        Log.d("gugwdwd", "onResponse: " + response.body().getUser_data());

                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomUOM roomUOM = new RoomUOM(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomUOM.getId());
                            dataBase.customerDao().getUOMFromServer(roomUOM);
                        }


                         getRoomUOM();

                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<UOMResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomUOM() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomUOM>> uomFromRoom = dataBase.customerDao().getSelectedUOMFromRoom();
        uomFromRoom.observe(FdcOpeningStockBalance.this, roomUOMS -> {
            parentDataUom = roomUOMS;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataUom.size(); i++) {
                if (!stringnames.contains(parentDataUom.get(i).getC_val())) {
                    stringnames.add(parentDataUom.get(i).getC_val());
                }
                setSpinnerAdapter(uomSpinner, stringnames);
            }


        });
    }
    private void getMedicines() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcOpeningStockBalance.this)) {
            Toast.makeText(FdcOpeningStockBalance.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcOpeningStockBalance.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcOpeningStockBalance.this).getnUserLevel());


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
        medicinesFromRoom.observe(FdcOpeningStockBalance.this, roomMedicines -> {
            parentDataMedicines = roomMedicines;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataMedicines.size(); i++) {
                if (!stringnames.contains(parentDataMedicines.get(i).getC_val())) {
                    stringnames.add(parentDataMedicines.get(i).getC_val());
                }
                setSpinnerAdapter(medicineSpinner, stringnames);
            }


        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    wayLatitude = String.valueOf(location.getLatitude());
                    wayLongitude = String.valueOf(location.getLongitude());
//                  lat=30.977006;
//                  lng=76.537880;
                    //saveAddress();
                } else {
                    // Toast.makeText(HomeActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FdcOpeningStockBalance.this, values);
        spinner.setAdapter(spinnerAdapter);

    }
    private Boolean isNotEmpty(EditText editText) {
        Boolean state = false;
        if (!editText.getText().toString().isEmpty()) {
            state = true;
        }
        return state;
    }

    private void getFdcData() {
        //progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcOpeningStockBalance.this)) {
            Toast.makeText(FdcOpeningStockBalance.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();

            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            //  progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcOpeningStockBalance.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fds_obal&w=n_hf_id_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("hf_id")) + "<<AND>>n_tu_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("tu_id"));
        ApiClient.getClient().getFdcOpeningStockData(url).enqueue(new Callback<TestreportResponse>() {
            @Override
            public void onResponse(Call<TestreportResponse> call, Response<TestreportResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        response.body().getUser_data();

                        Log.d("gugwdwd", "onResponse: " + response.body().getStatus());
                        Log.d("gugwdwd", "onResponse: " + response.body().getUser_data());

                        //for (int i = 0; i < parentData.size(); i++) {
                          /*  RoomCounsellingTypes roomCounsellingTypes = new RoomCounsellingTypes(
                               response.body().isStatus(),
                               response.body().getMessage(),
                               response.body().getUser_data()
                            );*/
                        //       Log.d("kiuij", "onResponse: " + roomCounsellingTypes.getUser_data());
                        //   dataBase.customerDao().getCounselingTypes(roomCounsellingTypes);

                        //  }
                    /*    for (int i = 0; i < parentData.size(); i++) {
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getTestReas());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSpecmTyp());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getdSpecmCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getcPlcSampCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSmplExt());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSputmSampl());


                            RoomPatientList roomPatientList = new RoomPatientList(parentData.get(i).getdRegDat(), parentData.get(i).getnNkshId(),
                                    parentData.get(i).getcPatNam(), parentData.get(i).getnAge(), parentData.get(i).getcTyp(),
                                    parentData.get(i).getnWght(), parentData.get(i).getnHght(), parentData.get(i).getcAdd(), parentData.get(i).getcTaluka(), parentData.get(i).getcTown(), parentData.get(i).getcWard(), parentData.get(i).getcLndMrk(), parentData.get(i).getnPin(), parentData.get(i).getcDocNam(), parentData.get(i).getId(),
                                    parentData.get(i).getnStId(), parentData.get(i).getnDisId(), parentData.get(i).getnTuId(), parentData.get(i).getnHfId(), parentData.get(i).getnDocId(), parentData.get(i).getnUserId());
                            dataBase.customerDao().getPatientsFromServer(roomPatientList);
                        }*/

                        LocalBroadcastManager.getInstance(FdcOpeningStockBalance.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<TestreportResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void submitData() {
        if (!medicine.equals("")) {
            if (!uom.equals("")) {
                if (isNotEmpty(quantity)) {
                    //if (isNotEmpty(ethambutol)) {
                        progressDialog.showProgressBar();
                        mViewModel.submitFdcOpenStockBalance(
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnStCd()),
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnDisCd()),
                                Integer.valueOf(getIntent().getStringExtra("tu_id")),
                                Integer.valueOf(getIntent().getStringExtra("hf_id")),
                                Integer.valueOf(medicine),
                                Integer.valueOf(uom),
                                Integer.valueOf(quantity.getText().toString()),
                                wayLatitude,
                                wayLongitude,
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnAccessRights()),
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnUserLevel()),
                              //  Integer.valueOf(BaseUtils.getUserOtherInfo(this).getN_staff_sanc()),
                                FdcOpeningStockBalance.this,
                                progressDialog,
                                true
                        );
                    /*} else {
                        Toast.makeText(this, "Ethambutol is empty", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    Toast.makeText(this, "Quantity is empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "UOM is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Medicine is empty", Toast.LENGTH_SHORT).show();
        }
    }
}