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
import com.smit.ppsa.Adapter.DispensationHfAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.RoomMedicines;
import com.smit.ppsa.Response.RoomPreviousSamplesHf;
import com.smit.ppsa.Response.RoomUOM;
import com.smit.ppsa.Response.fdcdispensationHf.DispensationHfResponse;
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

public class FdcReceived extends AppCompatActivity {

    Spinner medicineSpinner, uomSpinner, weightBandSpinner;
    EditText quantity, ethambutol;
    private ImageView backBtn;
    TextView hospitalName, doctorName, submitBtn, dispensationDate;
    String dateDispensation = "";
    String wayLatitude = "", wayLongitude = "";
    private FusedLocationProviderClient mFusedLocationClient;
    FdcReceivedViewModel mViewModel;
    private GlobalProgressDialog progressDialog;
    List<RoomPreviousSamplesHf> parentDataPreviousSamples;
    private AppDataBase dataBase;
    private String medicine ="", uom = "";
    List<RoomUOM> parentDataUom;
    List<RoomMedicines> parentDataMedicines;
    DispensationHfAdapter previousAdapter;
    RecyclerView previousSamplesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdc_received);
        dataBase = AppDataBase.getDatabase(this);
        mViewModel = new ViewModelProvider(this).get(FdcReceivedViewModel.class);
        progressDialog = new GlobalProgressDialog(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (BaseUtils.getTuIdFdc(FdcReceived.this).equals(getIntent().getStringExtra("tu_id")) && BaseUtils.getHfIdFdc(FdcReceived.this).equals(getIntent().getStringExtra("hf_id"))) {
            getRoomPreviousSamples();
        }

        getLocation();
        getPreviousSamples();
        previousSamplesRecycler = findViewById(R.id.previoussamplecollectionsrecyclerC);

        medicineSpinner = findViewById(R.id.fdc2et);
        uomSpinner = findViewById(R.id.fdc3et);
        weightBandSpinner = findViewById(R.id.weightbands);
        quantity = findViewById(R.id.fdc4et);
        ethambutol = findViewById(R.id.ethambutolet);
        backBtn = findViewById(R.id.backbtn);
        hospitalName = findViewById(R.id.hospitalName);
        doctorName = findViewById(R.id.docname);
        submitBtn = findViewById(R.id.submitbtnc);
        //submitBtn = findViewById(R.id.submitbtnc);
        dispensationDate = findViewById(R.id.dateofdispensationtv);
        getRoomMedicines();
        getRoomUOM();

        hospitalName.setText(getIntent().getStringExtra("hospitalName"));
        doctorName.setText(getIntent().getStringExtra("docName"));

        setUpDispensationCalender();
        getUOM();
        getMedicines();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FdcReceived.super.onBackPressed();
            }
        });

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
                    medicine = parentDataMedicines.get(i - 1).getId();
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    //   medicine = parentDataTestReportResults.get(i - 1).getId();
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

    }
    private void getMedicines() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcReceived.this)) {
            Toast.makeText(FdcReceived.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcReceived.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcReceived.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_med&w=id<<GT>>0";
        ApiClient.getClient().getMedicine(url).enqueue(new Callback<MedicineResponse>() {
            @Override
            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataTestReportResults = response.body().getUser_data();

                        Log.d("gugwdwd", "onResponse: " + response.body().getStatus());
                        Log.d("gugwdwd", "onResponse: " + response.body().getUser_data());
                        dataBase.customerDao().deleteRoomAllMedicines();
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
        if (!BaseUtils.isNetworkAvailable(FdcReceived.this)) {
            Toast.makeText(FdcReceived.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FdcReceived.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FdcReceived.this).getnUserLevel());


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
        uomFromRoom.observe(FdcReceived.this, roomUOMS -> {
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
    private void getRoomMedicines() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomMedicines>> medicinesFromRoom = dataBase.customerDao().getSelectedMedicinesFromRoom();
        medicinesFromRoom.observe(FdcReceived.this, roomMedicines -> {
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

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FdcReceived.this, values);
        spinner.setAdapter(spinnerAdapter);

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
                DatePickerDialog m_date = new DatePickerDialog(FdcReceived.this, R.style.calender_theme, trdate, trCalendar
                        .get(Calendar.YEAR), trCalendar.get(Calendar.MONTH),
                        trCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }

    private void getPreviousDrugDispensation() {
        mViewModel.getPreviousDrugDispensation(Integer.valueOf(getIntent().getStringExtra("tu_id")),
                Integer.valueOf(getIntent().getStringExtra("hf_id")),
                Integer.valueOf(getIntent().getStringExtra("hf_id")));

    }

    private void setRecycler() {
        previousAdapter = new DispensationHfAdapter(parentDataPreviousSamples, FdcReceived.this);
        previousSamplesRecycler.setLayoutManager(new LinearLayoutManager(this));
        previousSamplesRecycler.setAdapter(previousAdapter);
    }

    private void getPreviousSamples() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FdcReceived.this)) {
            Toast.makeText(FdcReceived.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPreviousSamples: " + BaseUtils.getUserInfo(FdcReceived.this).getnUserLevel());
        Log.d("kopopddwi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("hf_id"));

        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));

        String tuId = "51"/*getIntent().getStringExtra("tu_id")*/;
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fdc_issue&w=n_tu_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("tu_id")) + "<<AND>>n_hf_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("hf_id")) + "<<AND>>n_doc_id<<EQUALTO>>" + Integer.valueOf(getIntent().getStringExtra("hf_id"));
        //  String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        // String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        //   String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        Log.d("juish", "getPythologyLab: " + url);
        ApiClient.getClient().getPreviousSamplesfdcHf(url).enqueue(new Callback<DispensationHfResponse>() {
            @Override
            public void onResponse(Call<DispensationHfResponse> call, Response<DispensationHfResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        // parentDataPreviousSamples = response.body().getUser_data();
                        BaseUtils.putTuidFdc(FdcReceived.this, getIntent().getStringExtra("tu_id"));
                        BaseUtils.putHfidFdc(FdcReceived.this, getIntent().getStringExtra("hf_id"));
                        dataBase.customerDao().deletePreviousSamplesHfFromRoom();
                        Log.d("koko", "onResponse: " + response.body().getUser_data().toString());
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomPreviousSamplesHf roomPreviousSamplesHf = new RoomPreviousSamplesHf(
                                    response.body().getUser_data().get(i).getD_issue(),
                                    response.body().getUser_data().get(i).getN_dis_id(),
                                    response.body().getUser_data().get(i).getN_doc_id(),
                                    response.body().getUser_data().get(i).getN_hf_id(),
                                    response.body().getUser_data().get(i).getN_st_id(),
                                    response.body().getUser_data().get(i).getN_tu_id(),
                                    response.body().getUser_data().get(i).getTot_drug()
                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousSamplesHf.getN_dis_id());

                            dataBase.customerDao().getPreviousSamplesHfFromServer(roomPreviousSamplesHf);
                        }
                        getRoomPreviousSamples();


                        LocalBroadcastManager.getInstance(FdcReceived.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<DispensationHfResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomPreviousSamples() {
        LiveData<List<RoomPreviousSamplesHf>> roomPreviousSamplesHf = dataBase.customerDao().getSelectedRoomPreviousSamplesHfFromRoom();
        roomPreviousSamplesHf.observe(FdcReceived.this, roomPreviousSamplesHfs -> {
            parentDataPreviousSamples = roomPreviousSamplesHfs;
            setRecycler();
        });
    }

    private void submitData() {
        if (!dateDispensation.equals("")) {
            if (!medicine.equals("")) {
                if (!uom.equals("")) {
                    if (isNotEmpty(quantity)) {
                        //if (isNotEmpty(ethambutol)) {
                        progressDialog.showProgressBar();
                        mViewModel.submitFdcReceived(
                                dateDispensation,
                                Integer.valueOf(medicine),
                                Integer.valueOf(uom),
                                Integer.valueOf(quantity.getText().toString()),
                                Integer.valueOf(BaseUtils.getUserInfo(this).getN_staff_sanc()),
                                /*       Integer.valueOf(ethambutol.getText().toString()),*/
                                wayLatitude,
                                wayLongitude,
                               /* Integer.valueOf(BaseUtils.getUserInfo(this).getnAccessRights()),
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnUserLevel()),*/
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnAccessRights()),
                                Integer.valueOf(BaseUtils.getUserInfo(this).getnUserLevel()),
                                FdcReceived.this,
                                progressDialog,
                                new DispensationFragment(),
                                getSupportFragmentManager(),
                                getIntent().getStringExtra("hospitalName"),
                                true
                        );

                       /* } else {
                            Toast.makeText(this, "Enter ethambutol", Toast.LENGTH_SHORT).show();
                        }*/
                    } else {
                        Toast.makeText(this, "Enter Quantity", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter UOM", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter Medicine", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter date dispensation", Toast.LENGTH_SHORT).show();
        }

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