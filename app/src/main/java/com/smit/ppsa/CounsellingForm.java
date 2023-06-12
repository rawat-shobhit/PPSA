package com.smit.ppsa;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.PreviousVisitsCounsellingAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.CounselingResponse;
import com.smit.ppsa.Response.FilterResponse;
import com.smit.ppsa.Response.PrevVisitsCounselling.PreviousVisitsResponse;
import com.smit.ppsa.Response.RoomCounsellingFilters;
import com.smit.ppsa.Response.RoomCounsellingTypes;
import com.smit.ppsa.Response.RoomPreviousVisits;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CounsellingForm extends AppCompatActivity {

    TextView counsellingDate, submitBtn, showPatientBtn, hospitalName, doctorName, patientName;
    Spinner counsellingType, filterspinner;
    private ImageView backBtn;
    private GlobalProgressDialog progressDialog;
    //private AppDataBase dataBase;
    private List<RoomCounsellingTypes> parentData = new ArrayList<>();
    private List<RoomCounsellingFilters> parentDataFilters = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    private String wayLatitude = "", wayLongitude = "", typeOfCounselling = "", dateOfCounseling = "", filter = "";
    List<RoomPreviousVisits> parentDataPreviousSamples;
    PreviousVisitsCounsellingAdapter previousAdapter;
    RecyclerView previousSamplesRecycler;
    private AppDataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselling_form);
        dataBase = AppDataBase.getDatabase(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();


        counsellingDate = findViewById(R.id.f1_counsellingtv);
        hospitalName = findViewById(R.id.hospitalName);
        doctorName = findViewById(R.id.docname);
        patientName = findViewById(R.id.patientname);
        backBtn = findViewById(R.id.backbtn);
        submitBtn = findViewById(R.id.submitbtnc);
        showPatientBtn = findViewById(R.id.showpatbtnc);
        previousSamplesRecycler = findViewById(R.id.previoussamplecollectionsrecyclerC);
        progressDialog = new GlobalProgressDialog(this);
        counsellingType = findViewById(R.id.f1_counsellingtypetv);
        filterspinner = findViewById(R.id.filterCounsell);
        //  dataBase = AppDataBase.getDatabase(this);
        if (BaseUtils.getCounsellingPatientName(CounsellingForm.this).equals(getIntent().getStringExtra("patient_name"))) {
            getRoomCounsellingTypes();
            getRoomPreviousVisits();
        }

        setUpCounsellingCalender();
        getTypeOfCounseling();
        getPreviousSamples();
        getFilters();


        doctorName.setText(getIntent().getStringExtra("doc_name"));
        patientName.setText(getIntent().getStringExtra("patient_name") + "  " + "(" + getIntent().getStringExtra("patient_phone") + ")");
        hospitalName.setText(getIntent().getStringExtra("hospitalName"));

        hospitalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getIntent().getStringExtra("patient_phone")));
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CounsellingForm.super.onBackPressed();
            }
        });


        showPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PatientsFragment dialog = new PatientsFragment();
                dialog.show(getSupportFragmentManager(), "patients");

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dateOfCounseling.equals("")) {
                    Date date = null;
                    Date dateOne = null;
                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentTime = Calendar.getInstance().getTime();
                    try {
                        //registration dsate
                        dateOne = curFormater.parse(modifyDateLayout(getIntent().getStringExtra("reg_date")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        //counselling date
                        date = curFormater.parse(modifyDateLayout(dateOfCounseling));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long diffInMillies = Math.abs(date.getTime() - dateOne.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    Log.d("jduh", "onClick: "+ diff);

                    if (!typeOfCounselling.equals("")) {
//                        if (diff < 0) {
                            submitCounseling();
//                        } else {
//                            BaseUtils.showToast(CounsellingForm.this, "Counselling date should be greater than registration date");
//                        }
                    } else {
                        BaseUtils.showToast(CounsellingForm.this, "Choose type of counselling");
                    }


                } else {
                    BaseUtils.showToast(CounsellingForm.this, "Choose date of counselling");
                }
            }
        });
        counsellingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    typeOfCounselling = "";
                    Log.d("dded", "onItemSelected: " + typeOfCounselling);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    typeOfCounselling = parentData.get(i - 1).getC_val();
                    Log.d("dded", "onItemSelected: " + typeOfCounselling);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    filter = "";
                    Log.d("dded", "onItemSelected: " + typeOfCounselling);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    filter = parentData.get(i - 1).getC_val();
                    Log.d("dded", "onItemSelected: " + typeOfCounselling);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private String modifyDateLayout(String inputDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(CounsellingForm.this, values);
        spinner.setAdapter(spinnerAdapter);

    }


    private void getFilters() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(CounsellingForm.this)) {
            BaseUtils.showToast(CounsellingForm.this, "Please Check your internet  Connectivity");
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(CounsellingForm.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_coun_filters&w=id<<GT>>0";
        ApiClient.getClient().getCounsellingFilters(url).enqueue(new Callback<FilterResponse>() {
            @Override
            public void onResponse(Call<FilterResponse> call, Response<FilterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.putCounsellingFormPatientName(CounsellingForm.this, getIntent().getStringExtra("patient_name"));

                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomCounsellingFilters roomCounsellingFilters = new RoomCounsellingFilters(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomCounsellingFilters.getId());
                            dataBase.customerDao().getCounsellingFilterFromServer(roomCounsellingFilters);
                        }

                        Log.d("gug", "onResponse: " + response.body().getStatus());

                        getRoomCounsellingFilters();

                        LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<FilterResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    private void getTypeOfCounseling() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(CounsellingForm.this)) {
            BaseUtils.showToast(CounsellingForm.this, "Please Check your internet  Connectivity");
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(CounsellingForm.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_couns&w=id<<GT>>0";
        ApiClient.getClient().getCounsellingTypes(url).enqueue(new Callback<CounselingResponse>() {
            @Override
            public void onResponse(Call<CounselingResponse> call, Response<CounselingResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        BaseUtils.putCounsellingFormPatientName(CounsellingForm.this, getIntent().getStringExtra("patient_name"));

                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomCounsellingTypes roomCounsellingTypes = new RoomCounsellingTypes(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomCounsellingTypes.getId());
                            dataBase.customerDao().getCounsellingTypeFromServer(roomCounsellingTypes);
                        }

                        Log.d("gug", "onResponse: " + response.body().isStatus());

                        getRoomCounsellingTypes();

                        LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<CounselingResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    private void getRoomCounsellingFilters() {
        LiveData<List<RoomCounsellingFilters>> roomCounsellingFilter = dataBase.customerDao().getSelectedCounsellingFilterFromRoom();
        roomCounsellingFilter.observe(CounsellingForm.this, roomCounsellingFilters -> {
            parentDataFilters = roomCounsellingFilters;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataFilters.size(); i++) {
                if (!stringnames.contains(parentDataFilters.get(i).getC_val())) {
                    stringnames.add(parentDataFilters.get(i).getC_val());
                }
                setSpinnerAdapter(filterspinner, stringnames);
            }

        });
    }

    private void getRoomCounsellingTypes() {
        LiveData<List<RoomCounsellingTypes>> roomCounsellingType = dataBase.customerDao().getSelectedCounsellingTypeFromRoom();
        roomCounsellingType.observe(CounsellingForm.this, roomCounsellingTypes -> {
            parentData = roomCounsellingTypes;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentData.size(); i++) {
                if (!stringnames.contains(parentData.get(i).getC_val())) {
                    stringnames.add(parentData.get(i).getC_val());
                }
                setSpinnerAdapter(counsellingType, stringnames);
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

    private void getPreviousSamples() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(CounsellingForm.this)) {
            BaseUtils.showToast(CounsellingForm.this, "Please Check your internet  Connectivity");
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPreviousSamples: " + BaseUtils.getUserInfo(CounsellingForm.this).getnUserLevel());
        Log.d("kopopddwi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("hf_id"));

        Log.d("dfert", "getPreviousSamples: " + getIntent().getStringExtra("doc_id"));

        String tuId = "51"/*getIntent().getStringExtra("tu_id")*/;

        //  String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        Log.d("jiejd", "getPreviousSamples: " + url);
        //String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>164<<AND>>n_hf_id<<EQUALTO>>7<<AND>>n_doc_id<<EQUALTO>>7<<AND>>n_enroll_id<<EQUALTO>>67";
        //   String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_couns&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("doc_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("enroll_id"));
        ApiClient.getClient().getPreviousSamplesCounselling(url).enqueue(new Callback<PreviousVisitsResponse>() {
            @Override
            public void onResponse(Call<PreviousVisitsResponse> call, Response<PreviousVisitsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        Log.d("koko", "onResponse: " + response.body().getUser_data().toString());
                        dataBase.customerDao().deletePreviousVisitFromRoom();
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomPreviousVisits roomPreviousVisits = new RoomPreviousVisits(
                                    response.body().getUser_data().get(i).getC_val(),
                                    response.body().getUser_data().get(i).getD_coun(),
                                    response.body().getUser_data().get(i).getN_dis_id(),
                                    response.body().getUser_data().get(i).getN_doc_id(),
                                    response.body().getUser_data().get(i).getN_enroll_id(),
                                    response.body().getUser_data().get(i).getN_hf_id(),
                                    response.body().getUser_data().get(i).getN_st_id(),
                                    response.body().getUser_data().get(i).getN_tu_id()
                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousVisits.getC_val());

                            dataBase.customerDao().getPreviousVisitsFromServer(roomPreviousVisits);
                        }
                        getRoomPreviousVisits();

                        LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<PreviousVisitsResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomPreviousVisits() {
        LiveData<List<RoomPreviousVisits>> roomPreviousVisits = dataBase.customerDao().getSelectedRoomPreviousVisitFromRoom();
        roomPreviousVisits.observe(CounsellingForm.this, roomPreviousVisits1 -> {
            parentDataPreviousSamples = roomPreviousVisits1;
            setRecycler();
        });

    }

    private void setRecycler() {
        previousAdapter = new PreviousVisitsCounsellingAdapter(parentDataPreviousSamples, CounsellingForm.this);
        previousSamplesRecycler.setLayoutManager(new LinearLayoutManager(this));
        previousSamplesRecycler.setAdapter(previousAdapter);
    }

    private void submitCounseling(
    ) {
        //   Log.d(TAG, "submitCounseling: ");

        Log.d("hugytf", "submitCounseling: " + typeOfCounselling);

        String id = "";
        for (int a = 0; a < parentData.size(); a++) {
            if (parentData.get(a).getC_val().equals(typeOfCounselling)) {
                id = parentData.get(a).getId().toString();
            }
        }


        NetworkCalls.submitCounselling(
                CounsellingForm.this,
                BaseUtils.getUserInfo(this).getId(),
                BaseUtils.getUserInfo(this).getnUserLevel(),
                getIntent().getStringExtra("st_id"),
                getIntent().getStringExtra("dis_id"),
                getIntent().getStringExtra("doc_id"),
                String.valueOf(wayLatitude),
                String.valueOf(wayLongitude),
                id,
                dateOfCounseling,
                getIntent().getStringExtra("enroll_id"),
                getIntent().getStringExtra("tu_id"),
                getIntent().getStringExtra("hf_id"),
                progressDialog,
                true
        );
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    private void setUpCounsellingCalender() {
        final Calendar trCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener trdate = (view, year, monthOfYear, dayOfMonth) -> {
            trCalendar.set(Calendar.YEAR, year);
            trCalendar.set(Calendar.MONTH, monthOfYear);
            trCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
           // trCalendar.before(new Date())


            dateOfCounseling = sdf.format(trCalendar.getTime());
            counsellingDate.setText(sdf.format(trCalendar.getTime()));
        };
        counsellingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(CounsellingForm.this, R.style.calender_theme, trdate, trCalendar
                        .get(Calendar.YEAR), trCalendar.get(Calendar.MONTH),
                        trCalendar.get(Calendar.DAY_OF_MONTH));

                m_date.show();
                Date date = null;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(getIntent().getStringExtra("reg_date").split("-")[0]),
                        Integer.parseInt(getIntent().getStringExtra("reg_date").split("-")[1])-1,
                                Integer.parseInt(getIntent().getStringExtra("reg_date").split("-")[2]));
                m_date.getDatePicker().setMinDate(calendar.getTimeInMillis());
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }

}