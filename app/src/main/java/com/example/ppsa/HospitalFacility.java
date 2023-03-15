package com.example.ppsa;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppsa.Adapter.HospitalFacilityAdapter;

import com.example.ppsa.Adapter.SpinAdapter;
import com.example.ppsa.Dao.AppDataBase;
import com.example.ppsa.Network.ApiClient;
import com.example.ppsa.Network.NetworkCalls;
import com.example.ppsa.Network.NetworkConnection;
import com.example.ppsa.Response.AddDocResponse;
import com.example.ppsa.Response.DoctorModel;
import com.example.ppsa.Response.DoctorsList;
import com.example.ppsa.Response.QualificationList;
import com.example.ppsa.Response.RoomDoctorsList;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalFacility extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView doctorsRecycler;
    private TextView addDoc;
    List<DoctorsList> doctors = new ArrayList<>();
    private ImageView backbtn;
    private AppDataBase dataBase;
    private String name = "";
    private String qual = "select";
    private String quals = "select";
    private String phone = "";
    private GlobalProgressDialog globalProgressDialog;
    private List<QualificationList> qualificationLists = new ArrayList<>();
    private List<QualificationList> qualificationListspe = new ArrayList<>();
    private List<RoomDoctorsList> roomDoctorsLists = new ArrayList<>();
    List<DoctorModel> doctorModels = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_facility);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver2, new IntentFilter("qual"));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter("doc"));
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        dataBase = AppDataBase.getDatabase(HospitalFacility.this);
        NetworkCalls.getDocData(this, getIntent().getStringExtra("hf_id"));
        NetworkCalls.getQualDataspe(this);
        NetworkCalls.getQualData(this);
        doctorsRecycler = findViewById(R.id.hf_recycleview);
        backbtn = findViewById(R.id.backbtn);
        addDoc = findViewById(R.id.addDoc);
        getAllCustomer();
        setClickListeners();
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                if (doctorModels.size() != 0) {
                    for (int a = 0; a < doctorModels.size(); a++) {
                        setAddDoc(doctorModels.get(a).getC_doc_nam(),
                                doctorModels.get(a).getN_qual_id(),
                                doctorModels.get(a).getN_spec_id(),
                                doctorModels.get(a).getC_mob()
                        );
                        dataBase.customerDao().deleteDoctor(doctorModels.get(a));
                    }
                }
            }
        });
    }

    private void setClickListeners() {
        addDoc.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    private boolean emptyText(String textValue,String textValue2) {
        return TextUtils.equals(textValue.toLowerCase(),textValue2.toLowerCase());
    }


    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {

            if (intent.hasExtra("notifyDocAdapter")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
                    getRoomDoctors();
                }, 1000);
            } else if (intent.hasExtra("localDocData")) {
                getRoomDoctors();
            }
        }
    };
    public BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            if (intent.hasExtra("notifyqualAdapter")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
                    qualificationLists = BaseUtils.getQual(HospitalFacility.this);
                }, 1000);
            } else if (intent.hasExtra("localQualData")) {
                qualificationLists = BaseUtils.getQual(HospitalFacility.this);
            } else if (intent.hasExtra("notifyqualspeAdapter")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
                    qualificationListspe = BaseUtils.getQualSpe(HospitalFacility.this);
                }, 1000);
            } else if (intent.hasExtra("localQualspeData")) {
                qualificationListspe = BaseUtils.getQualSpe(HospitalFacility.this);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        }
        if (broadcastReceiver2 != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver2);
        }
    }

    private void setHospitalRecycler(List<RoomDoctorsList> roomDoctorsLists) {

        HospitalFacilityAdapter docAdapter = new HospitalFacilityAdapter(roomDoctorsLists, HospitalFacility.this);
        doctorsRecycler.setAdapter(docAdapter);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addDoc:
                addDocDialog();
                break;
            case R.id.backbtn:
                super.onBackPressed();
                break;
        }
    }

    private boolean emptyText(EditText editText){
        return editText.getText().toString().isEmpty();
    }

    private void addDocDialog() {
        LayoutInflater li = LayoutInflater.from(HospitalFacility.this);
        View dialogView = li.inflate(R.layout.dialog_adddoctor, null);

        Spinner qual1 = dialogView.findViewById(R.id.ad_qualification_one);

        Spinner qual2 = dialogView.findViewById(R.id.ad_qualification_two);

        SpinAdapter adapter;
        adapter = new SpinAdapter(HospitalFacility.this,
                qualificationLists);
        SpinAdapter adapter2;
        adapter2 = new SpinAdapter(HospitalFacility.this,
                qualificationListspe);
        qual2.setAdapter(adapter2);
        qual1.setAdapter(adapter);
        final String[] qualID = new String[1];
        final String[] qualSpeID = new String[1];
        qual1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        // It returns the clicked item.

                        QualificationList clickedItem = (QualificationList)
                                parent.getItemAtPosition(position);
                        qual = clickedItem.getcQualf();
                        qualID[0] = clickedItem.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        qual2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        // It returns the clicked item.
                        QualificationList clickedItem = (QualificationList)
                                parent.getItemAtPosition(position);
                        quals = clickedItem.getcQual();
                        qualSpeID[0] = clickedItem.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        EditText namepracticingdoctor = dialogView.findViewById(R.id.namepracticingdoctor);

        EditText ad_contact = dialogView.findViewById(R.id.ad_contact);
        TextView cancel = dialogView.findViewById(R.id.ad_cancelbtn);
        TextView add = dialogView.findViewById(R.id.ad_nextbtn);
        androidx.appcompat.app.AlertDialog sDialog = new AlertDialog.Builder(HospitalFacility.this).setView(dialogView).setCancelable(true).create();

        sDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sDialog.dismiss();
            }
        });

        add.setOnClickListener(view -> {
            if (emptyText(namepracticingdoctor)){
                BaseUtils.showToast(this,"Enter doctor name");
            }else if(qual1.getSelectedItemPosition() == 0){
                BaseUtils.showToast(this,"Select qualification");
            }else if (qual2.getSelectedItemPosition() == 0){
                BaseUtils.showToast(this,"Select specialisation");
            }else if (emptyText(ad_contact)){
                BaseUtils.showToast(this,"Enter phone number");
            }else if(ad_contact.getText().toString().length()<9){
               BaseUtils.showToast(this,"Enter valid mobile number");
            }else{
                setAddDoc(namepracticingdoctor.getText().toString(),
                        qualID[0],
                        qualSpeID[0],
                        ad_contact.getText().toString(),
                        sDialog);
            }
        });

        //bookingMsg.setText(response.body().getMessage());

    }

    private void setAddDoc(String name, String qualId, String specId, String mobNumber, AlertDialog sDialog) {
        if (!BaseUtils.isNetworkAvailable(this)) {
            DoctorModel doctorModel = new DoctorModel(getIntent().getStringExtra("hf_id"),
                    name,
                    qualId,
                    specId,
                    mobNumber);
            dataBase.customerDao().insertDoctor(doctorModel);
            sDialog.dismiss();
            BaseUtils.showToast(this, "Saved in local");
            return;
        }
        RequestBody hfID = RequestBody.create(getIntent().getStringExtra("hf_id"), MediaType.parse("text/plain"));
        RequestBody docName = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody qualID = RequestBody.create(qualId, MediaType.parse("text/plain"));
        RequestBody specID = RequestBody.create(specId, MediaType.parse("text/plain"));
        RequestBody mobNum = RequestBody.create(mobNumber, MediaType.parse("text/number"));

        ApiClient.getClient().addDoc(hfID, docName, qualID, specID, mobNum).enqueue(new Callback<AddDocResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Toast.makeText(HospitalFacility.this, "success", Toast.LENGTH_SHORT).show();
                        sDialog.dismiss();
                        startActivity(new Intent(HospitalFacility.this,HospitalFacility.class)
                                .putExtra("hf_id",getIntent().getStringExtra("hf_id")));
                        finish();
                       // updateAdapter(name, qualId, specId, mobNumber);

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {

            }
        });

    }

    private void setAddDoc(String name, String qualId, String specId, String mobNumber) {
        if (!BaseUtils.isNetworkAvailable(this)) {
            return;
        }
        RequestBody hfID = RequestBody.create(getIntent().getStringExtra("hf_id"), MediaType.parse("text/plain"));
        RequestBody docName = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody qualID = RequestBody.create(qualId, MediaType.parse("text/plain"));
        RequestBody specID = RequestBody.create(specId, MediaType.parse("text/plain"));
        RequestBody mobNum = RequestBody.create(mobNumber, MediaType.parse("text/number"));

        ApiClient.getClient().addDoc(hfID, docName, qualID, specID, mobNum).enqueue(new Callback<AddDocResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        updateAdapter(name, qualId, specId, mobNumber);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {

            }
        });


    }
    private void getRoomDoctors(){
        LiveData<List<RoomDoctorsList>> roomDoc = dataBase.customerDao().getSelectedDoctorsFromRoom(getIntent().getStringExtra("hf_id"));
        roomDoc.observe(HospitalFacility.this,roomDoctorsLists -> {
            this.roomDoctorsLists = roomDoctorsLists;
            setHospitalRecycler(this.roomDoctorsLists);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAllCustomer() {

        LiveData<List<DoctorModel>> allCustomer = dataBase.customerDao().fetchSelectedDoctor(getIntent().getStringExtra("hf_id"));
        allCustomer.observe(HospitalFacility.this, doctorModels -> {
            HospitalFacility.this.doctorModels = doctorModels;
                      /*  for (int a = 0; a < doctorModels.size(); a++) {
                int finalA = a;
                QualificationList qualificationList = BaseUtils.getQual(HospitalFacility.this).stream()
                        .filter(qual -> doctorModels.get(finalA).getN_qual_id().equals(qual.getId()))
                        .findAny()
                        .orElse(null);
                assert qualificationList != null;
                QualificationList qualificationListSpec = BaseUtils.getQualSpe(HospitalFacility.this).stream()
                        .filter(qual -> doctorModels.get(finalA).getN_spec_id().equals(qual.getId()))
                        .findAny()
                        .orElse(null);
                assert qualificationListSpec != null;

            }*/
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAdapter(String name, String qualId, String specId, String mobNumber) {
        QualificationList qualificationList = BaseUtils.getQual(HospitalFacility.this).stream()
                .filter(qual -> qualId.equals(qual.getId()))
                .findAny()
                .orElse(null);
        QualificationList qualificationListSpec = BaseUtils.getQualSpe(HospitalFacility.this).stream()
                .filter(qual -> specId.equals(qual.getId()))
                .findAny()
                .orElse(null);
        assert qualificationList != null;
        assert qualificationListSpec != null;

        try {
            Objects.requireNonNull(doctorsRecycler.getAdapter()).notifyDataSetChanged();
        }catch (NullPointerException e){}

    }

}