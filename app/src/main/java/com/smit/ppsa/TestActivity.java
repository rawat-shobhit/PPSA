package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.HospitalList;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner f2_diagnotictestsoffered, f2_dstoffered, f2_otherdstoffered, f2_finalinterpretation, f2_testresults, f2_casetype, f2_patientstatus;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<FormOneData> diagnosticList = new ArrayList<>();
    private List<FormOneData> dstList = new ArrayList<>();
    private List<FormOneData> otherDSTList = new ArrayList<>();
    private List<FormOneData> interList = new ArrayList<>();
    private List<FormOneData> testResultList = new ArrayList<>();
    private List<FormOneData> caseType = new ArrayList<>();
    private List<FormOneData> patientStatus = new ArrayList<>();
    private List<String> diagnosticStrings = new ArrayList<>();
    private List<String> dstStrings = new ArrayList<>();
    private List<String> otherDstStrings = new ArrayList<>();
    private List<String> interStrings = new ArrayList<>();
    private List<String> testResultStrings = new ArrayList<>();
    private List<String> caseTypeStrings = new ArrayList<>();
    private List<String> patientStatusStrings = new ArrayList<>();
    private ImageView backbtn;
    private EditText testName;
    private String wayLatitude = "0.0", wayLongitude = "0.0";
    private HospitalList hospitalLis;
    private TextView f2_dateofreportdeliverytodoctor, proceed, f2_datetesteddiagnostictests, f2_dateoftestreporteddiagnostictests, f2_datetesteddst, f2_dateoftestreporteddst, f2_datetestedotherdst, f2_dateoftestreportedotherdst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        init();
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

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        testName = findViewById(R.id.tnameet);
        f2_casetype = findViewById(R.id.f2_casetype);
        f2_diagnotictestsoffered = findViewById(R.id.f2_diagnotictestsoffered);
        f2_dstoffered = findViewById(R.id.f2_dstoffered);
        f2_otherdstoffered = findViewById(R.id.f2_otherdstoffered);
        f2_testresults = findViewById(R.id.f2_testresults);
        f2_finalinterpretation = findViewById(R.id.f2_finalinterpretation);
        f2_patientstatus = findViewById(R.id.f2_patientstatus);
        proceed = findViewById(R.id.proceed);
        f2_dateofreportdeliverytodoctor = findViewById(R.id.f2_dateofreportdeliverytodoctor);
        f2_datetesteddiagnostictests = findViewById(R.id.f2_datetesteddiagnostictests);
        f2_dateoftestreporteddiagnostictests = findViewById(R.id.f2_dateoftestreporteddiagnostictests);
        f2_dateoftestreporteddst = findViewById(R.id.f2_dateoftestreporteddst);
        f2_datetesteddst = findViewById(R.id.f2_datetesteddst);
        f2_datetestedotherdst = findViewById(R.id.f2_datetestedotherdst);
        f2_dateoftestreportedotherdst = findViewById(R.id.f2_dateoftestreportedotherdst);
        setUpCalender(f2_dateofreportdeliverytodoctor);
        setUpCalender(f2_datetesteddiagnostictests);
        setUpCalender(f2_dateoftestreporteddiagnostictests);
        setUpCalender(f2_dateoftestreporteddst);
        setUpCalender(f2_datetesteddst);
        setUpCalender(f2_datetestedotherdst);
        setUpCalender(f2_dateoftestreportedotherdst);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.super.onBackPressed();
            }
        });

        NetworkCalls.getDiagnostic(this);
        NetworkCalls.getDST(this);
        NetworkCalls.getOtherDST(this);
        NetworkCalls.getInterpretation(this);
        NetworkCalls.getCaseType(this);
        NetworkCalls.getPatientStatus(this);
        NetworkCalls.getTestResult(this);

        List<HospitalList> hospitalLists = BaseUtils.getHospital(this);
        for (int a = 0; a < hospitalLists.size(); a++) {
            if (hospitalLists.get(a).getnHfId().equals(getIntent().getStringExtra("hf_id"))) {
                hospitalLis = hospitalLists.get(a);
            }
        }
        proceed.setOnClickListener(this);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localDiagnostic")) {
                diagnosticList = BaseUtils.getDiagnostic(TestActivity.this);
                for (FormOneData specimen : diagnosticList) {
                    diagnosticStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_diagnotictestsoffered, diagnosticStrings);
            }
            if (intent.hasExtra("localDST")) {
                dstList = BaseUtils.getDST(TestActivity.this);
                for (FormOneData specimen : dstList) {
                    dstStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_dstoffered, dstStrings);
            }
            if (intent.hasExtra("localOtherDST")) {
                otherDSTList = BaseUtils.getOtherDST(TestActivity.this);
                for (FormOneData specimen : otherDSTList) {
                    otherDstStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_otherdstoffered, otherDstStrings);
            }
            if (intent.hasExtra("localInter")) {
                interList = BaseUtils.getInterpretation(TestActivity.this);
                for (FormOneData specimen : interList) {
                    interStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_finalinterpretation, interStrings);
            }
            if (intent.hasExtra("localTestResult")) {
                testResultList = BaseUtils.getTestResult(TestActivity.this);
                for (FormOneData specimen : testResultList) {
                    testResultStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_testresults, testResultStrings);
            }
            if (intent.hasExtra("localCaseType")) {
                caseType = BaseUtils.getCaseType(TestActivity.this);
                for (FormOneData specimen : caseType) {
                    caseTypeStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_casetype, caseTypeStrings);
            }
            if (intent.hasExtra("localPatientStatus")) {
                patientStatus = BaseUtils.getPatientStatus(TestActivity.this);
                for (FormOneData specimen : patientStatus) {
                    patientStatusStrings.add(specimen.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(f2_patientstatus, patientStatusStrings);

            }
        }
    };

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(TestActivity.this, values);
        spinner.setAdapter(spinnerAdapter);
    }

    private void postTest() {


        NetworkCalls.postTest(TestActivity.this,
                hospitalLis.getnStId(),
                hospitalLis.getnDisId(),
                hospitalLis.getnTuId(),
                hospitalLis.getnHfId(),
                getIntent().getStringExtra("enroll_id"),
                getIntent().getStringExtra("n_smpl_col_id"),
                getIntent().getStringExtra("d_smpl_recd"),
                f2_dateofreportdeliverytodoctor.getText().toString(),
                diagnosticList.get(f2_diagnotictestsoffered.getSelectedItemPosition() - 1).getId(),
                f2_datetesteddiagnostictests.getText().toString(),
                f2_dateoftestreporteddiagnostictests.getText().toString(),
                dstList.get(f2_dstoffered.getSelectedItemPosition() - 1).getId(),
                f2_datetesteddst.getText().toString(),
                f2_dateoftestreporteddst.getText().toString(),
                otherDSTList.get(f2_otherdstoffered.getSelectedItemPosition() - 1).getId(),
                f2_dateoftestreportedotherdst.getText().toString(),
                f2_datetestedotherdst.getText().toString(),
                interList.get(f2_finalinterpretation.getSelectedItemPosition() - 1).getId(),
                testResultList.get(f2_testresults.getSelectedItemPosition() - 1).getId(),
                caseType.get(f2_casetype.getSelectedItemPosition() - 1).getId(),
                patientStatus.get(f2_patientstatus.getSelectedItemPosition() - 1).getId(),
                BaseUtils.getUserInfo(this).getnUserLevel(),
                wayLatitude,
                wayLongitude,
                true
        );

    }

    private void setUpCalender(TextView textView) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textView.setText(sdf.format(myCalendar.getTime()));
            }
        };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(TestActivity.this, R.style.calender_theme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceed:
                if (isEmpty(f2_dateofreportdeliverytodoctor)) {
                    BaseUtils.showToast(this, "Please enter date of report delivery to doctor");
                } else if (f2_diagnotictestsoffered.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please diagnostic test offered");
                } else if (isEmpty(f2_datetesteddiagnostictests)) {
                    BaseUtils.showToast(this, "Please enter date tested diagnostic tests");
                } else if (isEmpty(f2_dateoftestreporteddiagnostictests)) {
                    BaseUtils.showToast(this, "Please enter date of test reported tests");
                } else if (f2_dstoffered.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please select DST offered");
                } else if (isEmpty(f2_datetesteddst)) {
                    BaseUtils.showToast(this, "Please enter date tested DST");
                } else if (isEmpty(f2_dateoftestreporteddst)) {
                    BaseUtils.showToast(this, "Please enter date of test reported DST");
                } else if (f2_otherdstoffered.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please select other DST offered");
                } else if (isEmpty(f2_datetestedotherdst)) {
                    BaseUtils.showToast(this, "Please enter date tested other DST");
                } else if (isEmpty(f2_dateoftestreportedotherdst)) {
                    BaseUtils.showToast(this, "Please enter date of test reported other DST");
                } else if (f2_finalinterpretation.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please select final interpretation");
                } else if (f2_testresults.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please select test results");
                } else if (f2_casetype.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please select case type");
                } else if (f2_patientstatus.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(this, "Please select patient status");
                } else {
                    postTest();
                }
        }
    }

    private boolean isEmpty(TextView textView) {
        if (textView.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }

    }
}