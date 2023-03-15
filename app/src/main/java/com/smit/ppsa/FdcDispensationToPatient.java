package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.DispensationPatientAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.RoomMedicines;
import com.smit.ppsa.Response.RoomPreviousSamplesPatient;
import com.smit.ppsa.Response.RoomUOM;
import com.smit.ppsa.Response.RoomWeightBand;
import com.smit.ppsa.Response.WeightResponse;
import com.smit.ppsa.Response.dispensationPatient.DiispensationPatientResponse;
import com.smit.ppsa.Response.uom.UOMResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FdcDispensationToPatient extends AppCompatActivity {
    EditText productName, productQuantity, dayset, ethambutol, medicineQuantity;
    Spinner measurementUnitSpinner;
    private ImageView backBtn;
    TextView hospitalName, doctorName, patientName, submitBtn, dispensationDate;
    String dateDispensation = "";
    private FusedLocationProviderClient mFusedLocationClient;
    String wayLatitude = "", wayLongitude = "", measurementUnit = "";
    FdcDispensationToPatientViewModel mViewModel;
    private GlobalProgressDialog progressDialog;
    List<RoomPreviousSamplesPatient> parentDataPreviousSamples;
    DispensationPatientAdapter previousAdapter;
    RecyclerView previousSamplesRecycler;
    private AppDataBase dataBase;
    Spinner medicineSpinner, uomSpinner, weightBandSpinner;
    private String medicine ="", uom = "", weightBand = "";
    List<RoomMedicines> parentDataMedicines;
    List<RoomUOM> parentDataUom;
    List<RoomWeightBand> parentDataWeightBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdc_dispensation_to_patient);
        dataBase = AppDataBase.getDatabase(this);
        mViewModel = new ViewModelProvider(this).get(FdcDispensationToPatientViewModel.class);
        progressDialog = new GlobalProgressDialog(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();


        getTableData();
        getPreviousSamples();
        previousSamplesRecycler = findViewById(R.id.previoussamplecollectionsrecyclerC);

        dispensationDate = findViewById(R.id.dateofdispensationtv);
        productName = findViewById(R.id.nameet);
        productQuantity = findViewById(R.id.quantityet);
        medicineQuantity = findViewById(R.id.quantet);
        measurementUnitSpinner = findViewById(R.id.meset);
        dayset = findViewById(R.id.dayset);
        ethambutol = findViewById(R.id.ethambutolet);
        backBtn = findViewById(R.id.backbtn);
        hospitalName = findViewById(R.id.hospitalName);
        doctorName = findViewById(R.id.docname);
        patientName = findViewById(R.id.patientname);
        submitBtn = findViewById(R.id.submitbtnc);
        weightBandSpinner = findViewById(R.id.weightbands);

        medicineSpinner = findViewById(R.id.fdc2et);
        uomSpinner = findViewById(R.id.fdc3et);

        hospitalName.setText(getIntent().getStringExtra("hospitalName"));
        doctorName.setText(getIntent().getStringExtra("doc_name"));
        patientName.setText(getIntent().getStringExtra("patient_name"));

        if (BaseUtils.getPatientNameFdcPa(FdcDispensationToPatient.this).equals(getIntent().getStringExtra("patient_name"))){
            getRoomPreviousSamples();
        }

        setUpDispensationCalender();
        getRoomMedicines();
        getRoomUOM();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FdcDispensationToPatient.super.onBackPressed();
            }
        });


        measurementUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    measurementUnit = "";
                    Log.d("dded", "onItemSelected: " + measurementUnit);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    //medicine = parentDataUom.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + measurementUnit);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        weightBandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    weightBand = "";
                    Log.d("dded", "onItemSelected: " + medicine);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    weightBand = parentDataWeightBand.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + medicine);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitData();
            }
        });


        getMedicines();
        getUOM();
        getWeight();

    }

    private void getRoomMedicines() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomMedicines>> medicinesFromRoom = dataBase.customerDao().getSelectedMedicinesFromRoom();
        medicinesFromRoom.observe(FdcDispensationToPatient.this, roomMedicines -> {
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

    private void getRoomUOM() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomUOM>> uomFromRoom = dataBase.customerDao().getSelectedUOMFromRoom();
        uomFromRoom.observe(FdcDispensationToPatient.this, roomUOMS -> {
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

    private void getRoomWeightBands() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomWeightBand>> weightBandFromRoom = dataBase.customerDao().getSelectedWeightBandFromRoom();
        weightBandFromRoom.observe(FdcDispensationToPatient.this, roomWeightBands -> {
            parentDataWeightBand = roomWeightBands;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataWeightBand.size(); i++) {
                if (!stringnames.contains(parentDataWeightBand.get(i).getC_val())) {
                    stringnames.add(parentDataWeightBand.get(i).getC_val());
                }
                setSpinnerAdapter(weightBandSpinner, stringnames);
            }


        });
    }
    private void getMedicines() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcDispensationToPatient.this)) {
            Toast.makeText(FdcDispensationToPatient.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcDispensationToPatient.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcDispensationToPatient.this).getnUserLevel());


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
    private void getUOM() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcDispensationToPatient.this)) {
            Toast.makeText(FdcDispensationToPatient.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcDispensationToPatient.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcDispensationToPatient.this).getnUserLevel());


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

    private void getWeight() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcDispensationToPatient.this)) {
            Toast.makeText(FdcDispensationToPatient.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcDispensationToPatient.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcDispensationToPatient.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_wght_bnd&w=id<<GT>>0";
        ApiClient.getClient().getWeight(url).enqueue(new Callback<WeightResponse>() {
            @Override
            public void onResponse(Call<WeightResponse> call, Response<WeightResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataTestReportResults = response.body().getUser_data();

                        Log.d("gugwdwd", "onResponse: " + response.body().getStatus());
                        Log.d("gugwdwd", "onResponse: " + response.body().getUser_data());

                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomWeightBand roomWeightBand = new RoomWeightBand(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomWeightBand.getId());
                            dataBase.customerDao().getWeightBandsFromServer(roomWeightBand);
                        }


                        getRoomWeightBands();

                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<WeightResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FdcDispensationToPatient.this, values);
        spinner.setAdapter(spinnerAdapter);

    }

    public void getTableData() {
        mViewModel.getPreviousDrugDispensation(
                Integer.valueOf(getIntent().getStringExtra("tu_id")),
                Integer.valueOf(getIntent().getStringExtra("hf_id")),
                Integer.valueOf(getIntent().getStringExtra("doc_id")),
                Integer.valueOf(getIntent().getStringExtra("enroll_id"))
        );
    }

    private void setRecycler() {
        previousAdapter = new DispensationPatientAdapter(parentDataPreviousSamples, FdcDispensationToPatient.this);
        previousSamplesRecycler.setLayoutManager(new LinearLayoutManager(this));
        previousSamplesRecycler.setAdapter(previousAdapter);
    }

    private void getPreviousSamples() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcDispensationToPatient.this)) {
            Toast.makeText(FdcDispensationToPatient.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPreviousSamples: " + BaseUtils.getUserInfo(FdcDispensationToPatient.this).getnUserLevel());
        Log.d("kopopddwi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("hf_id"));

        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));

        String tuId = "51"/*getIntent().getStringExtra("tu_id")*/;
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fdc_disp&w=n_tu_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("tu_id")) + "<<AND>>n_hf_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("hf_id")) + "<<AND>>n_doc_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("doc_id")) + "<<AND>>n_enroll_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("enroll_id"));
        //  String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        // String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        //   String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        ApiClient.getClient().getPreviousSamplesfdcPatient(url).enqueue(new Callback<DiispensationPatientResponse>() {
            @Override
            public void onResponse(Call<DiispensationPatientResponse> call, Response<DiispensationPatientResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //parentDataPreviousSamples = response.body().getUser_data();
                        BaseUtils.putPatientNamePaForm(FdcDispensationToPatient.this, getIntent().getStringExtra("patient_name"));
                        dataBase.customerDao().deletePreviousSamplesPatientFromRoom();
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomPreviousSamplesPatient roomPreviousSamplesPatient = new RoomPreviousSamplesPatient(
                                    response.body().getUser_data().get(i).getD_disp(),
                                    response.body().getUser_data().get(i).getN_dis_id(),
                                    response.body().getUser_data().get(i).getN_doc_id(),
                                    response.body().getUser_data().get(i).getN_enroll_id(),
                                    response.body().getUser_data().get(i).getN_hf_id(),
                                    response.body().getUser_data().get(i).getN_st_id(),
                                    response.body().getUser_data().get(i).getN_tu_id(),
                                    response.body().getUser_data().get(i).getTot_drug()
                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousSamplesPatient.getN_dis_id());

                            dataBase.customerDao().getPreviousSamplesPatientFromServer(roomPreviousSamplesPatient);
                        }
                        Log.d("koko", "onResponse: " + response.body().getUser_data().toString());
                       getRoomPreviousSamples();

                        LocalBroadcastManager.getInstance(FdcDispensationToPatient.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<DiispensationPatientResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomPreviousSamples() {
        LiveData<List<RoomPreviousSamplesPatient>> roomPreviousSamplesPatientFromRoom = dataBase.customerDao().getSelectedRoomPreviousSamplesPatientFromRoom();
        roomPreviousSamplesPatientFromRoom.observe(FdcDispensationToPatient.this, roomPreviousSamplesPatients -> {
            parentDataPreviousSamples = roomPreviousSamplesPatients;
            setRecycler();
        });

    }

    private void submitData() {

        if (!dateDispensation.equals("")) {
            if (!weightBand.equals("")) {
                if (!medicine.equals("")) {
                    if (!uom.equals("")) {
                        if (isNotEmpty(medicineQuantity)) {
                            if (isNotEmpty(dayset)) {
                                if (Integer.valueOf(dayset.getText().toString()) < 120){
                                    progressDialog.showProgressBar();
                                    mViewModel.submitFdcDispensationToPatient(
                                            Integer.valueOf(BaseUtils.getUserInfo(this).getnStCd()),
                                            Integer.valueOf(BaseUtils.getUserInfo(this).getnDisCd()),
                                            Integer.valueOf(getIntent().getStringExtra("tu_id")),
                                            Integer.valueOf(getIntent().getStringExtra("hf_id")),
                                            Integer.valueOf(getIntent().getStringExtra("doc_id")),
                                            Integer.valueOf(getIntent().getStringExtra("enroll_id")),
                                            dateDispensation,
                                            Integer.valueOf(weightBand),
                                            Integer.valueOf(medicine),
                                            Integer.valueOf(medicineQuantity.getText().toString()),
                                            Integer.valueOf(uom),
                                            Integer.valueOf(dayset.getText().toString()),
                                            wayLatitude,
                                            wayLongitude,
                                /*Integer.valueOf(BaseUtils.getUserInfo(this).getnAccessRights()),
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnUserLevel()),*/
                                            Integer.valueOf(BaseUtils.getUserInfo(this).getnAccessRights()),
                                            Integer.valueOf(BaseUtils.getUserInfo(this).getnUserLevel()),
                                            FdcDispensationToPatient.this,
                                            progressDialog,
                                            FdcDispensationToPatient.this,
                                            true
                                    );
                                }else {
                                    Toast.makeText(this, "No. of Days should be lass than 120 days", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(this, "Enter No. of Days", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Enter Medicine Quantity", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Enter UOM", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter Medicine", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter Weight Band", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Choose date", Toast.LENGTH_SHORT).show();
        }

    }


    private void setUpDispensationCalender() {
        final Calendar trCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener trdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                trCalendar.set(Calendar.YEAR, year);
                trCalendar.set(Calendar.MONTH, monthOfYear);
                trCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateDispensation = sdf.format(trCalendar.getTime());
                dispensationDate.setText(sdf.format(trCalendar.getTime()));
            }
        };
        dispensationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(FdcDispensationToPatient.this, R.style.calender_theme, trdate, trCalendar
                        .get(Calendar.YEAR), trCalendar.get(Calendar.MONTH),
                        trCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }

    private Boolean isNotEmpty(EditText editText) {
        Boolean state = false;
        if (!editText.getText().toString().isEmpty()) {
            state = true;
        }
        return state;
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
}