package com.example.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppsa.Adapter.CustomSpinnerAdapter;
import com.example.ppsa.Adapter.PatientAdapter;
import com.example.ppsa.Network.ApiClient;
import com.example.ppsa.Network.NetworkCalls;
import com.example.ppsa.Response.AttendeceResponse;
import com.example.ppsa.Response.FormOneData;
import com.example.ppsa.Response.RegisterParentResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormTwo extends AppCompatActivity implements View.OnClickListener {
    private CardView proceedbtn;
    private ImageView backBtn;
    private EditText Enrollmentid, Placeofsamplecollection, TestId, DiagnosingFaciltyDistrict,
            DiagnosingFaciltyTBU, DiagnosingFaciltyLab, DiagnosingFaciltyHFcode, DiagnosticTestsOffered, DSTOffered, OtherDSTOffered, FinalInterpretation, TestResult,
            CaseType, PatientStatus;
    private Spinner ReasonforTesting, Typeofspecimen, Sampleextractiondoneby, SputumsampletypeandNumber;
    private PatientAdapter patientAdapter;
    private TextView nextbtn;
    private List<FormOneData> specimens = new ArrayList<>();
    private List<FormOneData> testings = new ArrayList<>();
    private List<FormOneData> types = new ArrayList<>();
    private List<FormOneData> extractions = new ArrayList<>();
    private List<String> specimenStrings = new ArrayList<>();
    private List<String> testingStrings = new ArrayList<>();
    private List<String> extractString = new ArrayList<>();
    private List<String> typString = new ArrayList<>();
    private RecyclerView patientrecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_two);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver2, new IntentFilter("patient"));
        initViews();
    }

    private void initViews() {

        patientrecycler = findViewById(R.id.f2_patientrecycler);
        proceedbtn = findViewById(R.id.bt_proceedtwo);
        backBtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        Enrollmentid = findViewById(R.id.f2_enrollmentid);
        ReasonforTesting = findViewById(R.id.f2_reasonfortesting);
        Typeofspecimen = findViewById(R.id.f2_typeofspecimen);
        Placeofsamplecollection = findViewById(R.id.f2_placeofsamplecollection);
        Sampleextractiondoneby = findViewById(R.id.f2_sampleexractiondoneby);
        SputumsampletypeandNumber = findViewById(R.id.f2_sputumsampletypeandnumber);
        TestId = findViewById(R.id.f2_testid);
        DiagnosingFaciltyDistrict = findViewById(R.id.f2_diagnosingfaciltydistrict);
        DiagnosingFaciltyTBU = findViewById(R.id.f2_diagnosingfaciltytbu);
        DiagnosingFaciltyLab = findViewById(R.id.f2_diagnosingfaciltylab);
        DiagnosingFaciltyHFcode = findViewById(R.id.f2_diagnosingfaciltyhfcode);
        DiagnosticTestsOffered = findViewById(R.id.f2_diagnotictestsoffered);
        DSTOffered = findViewById(R.id.f2_dstoffered);
        OtherDSTOffered = findViewById(R.id.f2_otherdstoffered);
        FinalInterpretation = findViewById(R.id.f2_finalinterpretation);
        TestResult = findViewById(R.id.f2_testresults);
        CaseType = findViewById(R.id.f2_casetype);
        PatientStatus = findViewById(R.id.f2_patientstatus);
        backBtn = findViewById(R.id.backbtn);
        backBtn = findViewById(R.id.backbtn);
//        NetworkCalls.getTesting(this);
//        NetworkCalls.getSpecimen(this);
//        NetworkCalls.getExtraction(this);
//        NetworkCalls.getType(this);
        setOnclick();
        getPatient();
    }


    private void setOnclick() {
        proceedbtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_proceedtwo:
                startActivity(new Intent(FormTwo.this, FormThree.class));
                break;
            case R.id.backbtn:
                super.onBackPressed();
                break;
            case R.id.nextbtn:
                startActivity(new Intent(FormTwo.this,PatientSampleList.class).putExtra("hf_id",getIntent().getStringExtra("hf_id")));
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
                specimens = BaseUtils.getSpecimen(FormTwo.this);
                for (FormOneData specimen : specimens) {
                    specimenStrings.add(specimen.getC_typ_specm());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(Typeofspecimen, specimenStrings);
            }
            if (intent.hasExtra("localextract")) {
                extractions = BaseUtils.getExtraction(FormTwo.this);
                for (FormOneData tu : extractions) {
                    extractString.add(tu.getC_smpl_ext());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(Sampleextractiondoneby, extractString);
            }
            if (intent.hasExtra("localtype")) {
                types = BaseUtils.gettype(FormTwo.this);
                for (FormOneData stat : types) {
                    typString.add(stat.getC_sputm_typ());
                }
                setSpinnerAdapter(SputumsampletypeandNumber, typString);
            }
        }
    };
    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // if (intent.hasExtra())
        }
    };

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FormTwo.this, values);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getPatient() {
        if (!BaseUtils.isNetworkAvailable(FormTwo.this)) {
            Toast.makeText(FormTwo.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(FormTwo.this).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_hf_id<<EQUALTO>>"+getIntent().getStringExtra("hf_id")+"<<AND>>n_user_id<<EQUALTO>>"+BaseUtils.getUserInfo(FormTwo.this).getnUserLevel();
        ApiClient.getClient().getRegisterParent(url).enqueue(new Callback<RegisterParentResponse>() {
            @Override
            public void onResponse(Call<RegisterParentResponse> call, Response<RegisterParentResponse> response) {
                patientAdapter = new PatientAdapter(response.body().getUserData(),FormTwo.this);
                patientrecycler.setLayoutManager(new LinearLayoutManager(FormTwo.this));
                patientrecycler.setAdapter(patientAdapter);
            }

            @Override
            public void onFailure(Call<RegisterParentResponse> call, Throwable t) {

            }
        });
    }

}