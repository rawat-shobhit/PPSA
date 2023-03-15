package com.example.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppsa.Adapter.CustomSpinnerAdapter;
import com.example.ppsa.Network.ApiClient;
import com.example.ppsa.Network.NetworkCalls;
import com.example.ppsa.Response.AddDocResponse;
import com.example.ppsa.Response.FormOneData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientSampleList extends AppCompatActivity implements View.OnClickListener {
    private EditText f2_enrollmentid,f2_placeofsamplecollection;
    private TextView f2_datespecimencollected,addSpecimenBtn,nextbtn;
    private ImageView backbtn;
    private List<FormOneData> specimens = new ArrayList<>();
    private List<FormOneData> testings = new ArrayList<>();
    private List<FormOneData> types = new ArrayList<>();
    private List<FormOneData> extractions = new ArrayList<>();
    private List<String> specimenStrings = new ArrayList<>();
    private List<String> testingStrings = new ArrayList<>();
    private List<String> extractString = new ArrayList<>();
    private List<String> typString = new ArrayList<>();
    private Spinner ReasonforTesting, Typeofspecimen, Sampleextractiondoneby, SputumsampletypeandNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sample_list2);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        init();
    }
    private void init(){
        addSpecimenBtn = findViewById(R.id.addSpecimenBtn);
        ReasonforTesting = findViewById(R.id.f2_reasonfortesting);
        Typeofspecimen = findViewById(R.id.f2_typeofspecimen);
        nextbtn = findViewById(R.id.nextbtn);
        backbtn = findViewById(R.id.backbtn);
        f2_enrollmentid = findViewById(R.id.f2_enrollmentid);
        f2_datespecimencollected = findViewById(R.id.f2_datespecimencollected);
        f2_placeofsamplecollection = findViewById(R.id.f2_placeofsamplecollection);
        Sampleextractiondoneby = findViewById(R.id.f2_sampleexractiondoneby);
        SputumsampletypeandNumber = findViewById(R.id.f2_sputumsampletypeandnumber);
        NetworkCalls.getTesting(this);
        NetworkCalls.getSpecimen(this);
        NetworkCalls.getExtraction(this);
        NetworkCalls.getType(this);
        setUpCalender();
        setOnClick();
    }
    private void setOnClick(){
        addSpecimenBtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addSpecimenBtn:
                findViewById(R.id.addSpecimenLayout).setVisibility(View.VISIBLE);
                break;
            case R.id.nextbtn:
                if (isValidate()){
                    addSample();
                }
                break;
            case R.id.backbtn:
                super.onBackPressed();
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localtesting")) {
                testings = BaseUtils.getTesting(context);
                for (FormOneData testing : testings) {
                    testingStrings.add(testing.getC_test_reas());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setSpinnerAdapter(ReasonforTesting, testingStrings);
                    }
                }, 500);

            }
            if (intent.hasExtra("localspecimen")) {
                specimens = BaseUtils.getSpecimen(PatientSampleList.this);
                for (FormOneData specimen : specimens) {
                    specimenStrings.add(specimen.getC_typ_specm());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(Typeofspecimen, specimenStrings);
            }
            if (intent.hasExtra("localextract")) {
                extractions = BaseUtils.getExtraction(PatientSampleList.this);
                for (FormOneData tu : extractions) {
                    extractString.add(tu.getC_smpl_ext());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(Sampleextractiondoneby, extractString);
            }
            if (intent.hasExtra("localtype")) {
                types = BaseUtils.gettype(PatientSampleList.this);
                for (FormOneData stat : types) {
                    typString.add(stat.getC_sputm_typ());
                }
                setSpinnerAdapter(SputumsampletypeandNumber, typString);
            }
        }
    };

    private boolean isValidate(){
        if (f2_enrollmentid.getText().toString().equals("")){
            BaseUtils.showToast(PatientSampleList.this,"Enter enrollment ID");
            return false;
        }else if(ReasonforTesting.getSelectedItemPosition() == 0){
            BaseUtils.showToast(PatientSampleList.this,"Select reason for testing");
            return false;
        }else if(Typeofspecimen.getSelectedItemPosition() == 0){
            BaseUtils.showToast(PatientSampleList.this,"Select type of specimen");
            return false;
        }else if (f2_datespecimencollected.getText().toString().equals("")){
            BaseUtils.showToast(PatientSampleList.this,"Enter date of spcimen collected");
            return false;
        }else if (f2_placeofsamplecollection.getText().toString().equals("")){
            BaseUtils.showToast(PatientSampleList.this,"Enter place of sample collection");
            return false;
        }else if (Sampleextractiondoneby.getSelectedItemPosition() == 0){
            BaseUtils.showToast(PatientSampleList.this,"Select sample extraction done by");
            return  false;
        }else if (SputumsampletypeandNumber.getSelectedItemPosition() == 0){
            BaseUtils.showToast(PatientSampleList.this,"Select sputum sample type and number");
            return false;
        }
        return true;
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(PatientSampleList.this, values);
        spinner.setAdapter(spinnerAdapter);
    }

    private void addSample(){
        if (!BaseUtils.isNetworkAvailable(PatientSampleList.this)) {
            Toast.makeText(PatientSampleList.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody n_st_id = RequestBody.create(BaseUtils.getUserOtherInfo(PatientSampleList.this).getnStId(), MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(BaseUtils.getUserOtherInfo(PatientSampleList.this).getnDisId(), MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(BaseUtils.getUserOtherInfo(PatientSampleList.this).getnTuId(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(getIntent().getStringExtra("hf_id"), MediaType.parse("text/plain"));
        RequestBody n_enroll_id = RequestBody.create(f2_enrollmentid.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_test_reas_id = RequestBody.create(testings.get(ReasonforTesting.getSelectedItemPosition()-1).getC_test_reas(), MediaType.parse("text/plain"));
        RequestBody n_typ_specm_id = RequestBody.create(specimens.get(Typeofspecimen.getSelectedItemPosition()-1).getC_typ_specm(), MediaType.parse("text/plain"));
        RequestBody d_specm_col = RequestBody.create(f2_datespecimencollected.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_plc_samp_col = RequestBody.create(f2_placeofsamplecollection.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_smpl_ext_id = RequestBody.create(extractions.get(Sampleextractiondoneby.getSelectedItemPosition()-1).getC_smpl_ext(), MediaType.parse("text/plain"));
        RequestBody n_sputm_typ_id = RequestBody.create(types.get(SputumsampletypeandNumber.getSelectedItemPosition()-1).getC_sputm_typ(), MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(PatientSampleList.this).getnUserLevel(), MediaType.parse("text/plain"));

        ApiClient.getClient().postFormPartOne(n_st_id,n_dis_id,n_tu_id,n_hf_id,n_enroll_id,n_test_reas_id,n_typ_specm_id,d_specm_col,c_plc_samp_col,n_smpl_ext_id,n_sputm_typ_id,n_user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().isStatus()){
                        Toast.makeText(PatientSampleList.this,"Sample submitted" , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

            }
        });
    }
    private void setUpCalender() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                f2_datespecimencollected.setText(sdf.format(myCalendar.getTime()));
            }
        };
        f2_datespecimencollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(PatientSampleList.this, R.style.calender_theme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                m_date.getDatePicker().setMinDate(System.currentTimeMillis());


                m_date.show();
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }
}