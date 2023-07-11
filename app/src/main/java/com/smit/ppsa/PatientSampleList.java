package com.smit.ppsa;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.PreviousSamplesCollectionAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.CollectedBy.UserData;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.PreviousSamplesCollection.PreviousSamplesCollection;
import com.smit.ppsa.Response.RoomPreviousSamplesCollection;
import com.smit.ppsa.Response.pythologylab.LabResponseInternal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class PatientSampleList extends AppCompatActivity implements View.OnClickListener {
    private EditText f2_placeofsamplecollection;
    private TextView f2_datespecimencollected, nextbtn, hospitalName, docname, patientname, patientphone, date_of_diagnosis;
    private ImageView backbtn;
    private LinearLayout llDOD;
    private List<FormOneData> specimens = new ArrayList<>();
    private List<FormOneData> testings = new ArrayList<>();
    private RecyclerView previousSamplesRecycler;
    private List<FormOneData> types = new ArrayList<>();
    private List<UserData> extractions = new ArrayList<>();
    private List<com.smit.ppsa.Response.CollectedFrom.UserData> collectedFrom = new ArrayList<>();
    private List<com.smit.ppsa.Response.noOfCont.UserData> noOfContainerss = new ArrayList<>();
    private List<com.smit.ppsa.Response.DiagnosticTest.UserData> diagnostictests = new ArrayList<>();
    private List<com.smit.ppsa.Response.pythologylab.UserData> pythologyLabsLi = new ArrayList<>();
    private List<LabResponseInternal> pythologyLabsTypeLi = new ArrayList<>();
    private List<String> specimenStrings = new ArrayList<>();
    private List<String> testingStrings = new ArrayList<>();
    private List<String> extractString = new ArrayList<>();
    private GlobalProgressDialog progressDialog;
    private List<String> collectedFromString = new ArrayList<>();
    private List<String> noOfContString = new ArrayList<>();
    private List<String> diagnosticTestString = new ArrayList<>();
    private List<String> pythologyLabsString = new ArrayList<>();
    private List<String> pythologyLabsTypeString = new ArrayList<>();
    private List<String> typString = new ArrayList<>();
    private String reg_date = "";
    private String enroll_id = "";
    private Spinner ReasonforTesting, Typeofspecimen, noOfContainers, Sampleextractiondoneby,
            Sampleextractionfrom, SputumsampletypeandNumber, diagnosticTestSpi, pythologyLabs, pythologyLabsType;
    private AppDataBase dataBase;
    PreviousSamplesCollectionAdapter previousAdapter;
    List<RoomPreviousSamplesCollection> parentDataPreviousSamples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sample_list2);
        dataBase = AppDataBase.getDatabase(this);
        progressDialog = new GlobalProgressDialog(this);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        init();

    }

    private void init() {

        ReasonforTesting = findViewById(R.id.f2_reasonfortesting);
        noOfContainers = findViewById(R.id.f2_noofcont);
        Typeofspecimen = findViewById(R.id.f2_typeofspecimen);
        pythologyLabs = findViewById(R.id.f2_pythologylabs);
        pythologyLabsType = findViewById(R.id.f2_pythologylabsType);
        date_of_diagnosis = findViewById(R.id.date_of_diagnosis);
        llDOD = findViewById(R.id.llDOD);
        nextbtn = findViewById(R.id.nextbtn);
        backbtn = findViewById(R.id.backbtn);
        hospitalName = findViewById(R.id.hospitalName);
        docname = findViewById(R.id.docname);
        patientname = findViewById(R.id.patientname);
        patientphone = findViewById(R.id.patientphone);

        try {
            if (getIntent().hasExtra("reg_date")) {
                reg_date = getIntent().getStringExtra("reg_date");

            } else {
                reg_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


            }
        } catch (Exception e) {


        }

        if (BaseUtils.getPatientsNams(this).equals("")) {
            patientname.setText(getIntent().getStringExtra("patient_name"));
        } else {
            patientname.setText(BaseUtils.getPatientsNams(this));
        }

        if (BaseUtils.getHospitalNams(this).equals("")) {
            hospitalName.setText(getIntent().getStringExtra("hospitalName"));
        } else {
            hospitalName.setText(BaseUtils.getHospitalNams(this));
        }


        if (BaseUtils.getDocNams(this).equals("")) {
            docname.setText(getIntent().getStringExtra("doc_name"));
//            Toast.makeText(this, "from get string ", Toast.LENGTH_SHORT).show();
        } else {
            docname.setText(BaseUtils.getDocNams(this));
            //   Toast.makeText(this, "pref ", Toast.LENGTH_SHORT).show();
        }


        if (BaseUtils.getPhoneNo(this).equals("")) {
            patientphone.setText(getIntent().getStringExtra("patient_phone"));
        } else {
            patientphone.setText(BaseUtils.getPhoneNo(this));
        }

        if (BaseUtils.getEnrollNo(this).equals("")) {
            enroll_id = getIntent().getStringExtra("enroll_id");
        } else {
            enroll_id = BaseUtils.getEnrollNo(this);
        }

        try {
            enroll_id=BaseUtils.getEnrollNo(this);
        }catch (Exception e)
        {
            enroll_id= "";
            Log.d("crash_enrollId",e.toString());
        }


       // Toast.makeText(this, enroll_id, Toast.LENGTH_SHORT).show();


        previousSamplesRecycler = findViewById(R.id.previoussamplecollectionsrecycler);

        f2_datespecimencollected = findViewById(R.id.f2_datespecimencollected);
        f2_placeofsamplecollection = findViewById(R.id.f2_placeofsamplecollection);
        Sampleextractiondoneby = findViewById(R.id.f2_sampleexractiondoneby);
        Sampleextractionfrom = findViewById(R.id.f2_sampleexractionfrom);
        diagnosticTestSpi = findViewById(R.id.f2_diagnostictests);
        SputumsampletypeandNumber = findViewById(R.id.f2_sputumsampletypeandnumber);

        Log.d("jijsoj", "init: " + BaseUtils.getGlobalPatientName(this));
        Log.d("jijsoj", "init: " + BaseUtils.getGlobalSelectedPatientName(this));

        //if (BaseUtils.getGlobalPatientName(this).equals(BaseUtils.getGlobalSelectedPatientName(this))) {
        getRoomPreviousSamples();
        //}

        NetworkCalls.getTesting(this);
        NetworkCalls.getSpecimen(this);
        NetworkCalls.getExtraction(this);
        NetworkCalls.getSamplesFrom(this);
        NetworkCalls.getNoOfCont(this);
        getPreviousSamples();
        NetworkCalls.getDiagnosticTestSample(this);

        NetworkCalls.getType(this);
        NetworkCalls.getPythologyLabType(this);
        setUpCalender();
        setUpCalender2();
        setOnClick();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        BaseUtils.setPhoneNo(this, "");
        BaseUtils.setPatientName(this, "");
        BaseUtils.setDocName(this, "");
        BaseUtils.setHospitalName(this, "");


    }

    private void setOnClick() {

        nextbtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);

//testings.get(ReasonforTesting.getSelectedItemPosition() - 1).getC_sputm_typ()=="UDST MC")
        pythologyLabsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {

                } else if (position == 1) {
                    NetworkCalls.getPythologyLabSample(PatientSampleList.this,
                            getIntent().getStringExtra("tu_id"),
                            "1");

                } else {
                    NetworkCalls.getPythologyLabSample(PatientSampleList.this,
                            getIntent().getStringExtra("tu_id"),
                            "2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ReasonforTesting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // Toast.makeText(PatientSampleList.this, position+"", Toast.LENGTH_SHORT).show();
                try {
                    if (testings.get(ReasonforTesting.getSelectedItemPosition() - 1).getC_test_reas().contains("UDST MC")) {
                        llDOD.setVisibility(View.VISIBLE);
                    } else {
                        llDOD.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nextbtn:
                if (isValidate()) {
                    Log.d("buttonClick", "clicked");
                    addSample(PatientSampleList.this);
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
                    if (!specimenStrings.contains(specimen.getC_typ_specm())) {
                        specimenStrings.add(specimen.getC_typ_specm());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(Typeofspecimen, specimenStrings);
            }
            if (intent.hasExtra("localextract")) {
                extractions = BaseUtils.getExtraction(PatientSampleList.this);
                Log.d("kioij", "onReceive: " + extractions.size());
                for (UserData tu : extractions) {
                    if (!extractString.contains(tu.getC_smpl_ext())) {
                        extractString.add(tu.getC_smpl_ext());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(Sampleextractiondoneby, extractString);
            }
            if (intent.hasExtra("localcollectedfrom")) {
                collectedFrom = BaseUtils.getCollectedFrom(PatientSampleList.this);
                Log.d("kioij", "onReceive: " + extractions.size());
                for (com.smit.ppsa.Response.CollectedFrom.UserData tu : collectedFrom) {
                    if (!collectedFromString.contains(tu.getC_val())) {
                        collectedFromString.add(tu.getC_val());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(Sampleextractionfrom, collectedFromString);
            }
            if (intent.hasExtra("localnoOfCont")) {
                noOfContainerss = BaseUtils.getNoOfCont(PatientSampleList.this);
                Log.d("kioij", "onReceive: " + noOfContainerss.size());
                for (com.smit.ppsa.Response.noOfCont.UserData tu : noOfContainerss) {
                    if (!noOfContString.contains(tu.getC_val())) {
                        noOfContString.add(tu.getC_val());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(noOfContainers, noOfContString);
            }
            if (intent.hasExtra("localDiagnosticTestSample")) {
                diagnostictests = BaseUtils.getDiagnosticTestSample(PatientSampleList.this);
                Log.d("kioij", "onReceive: " + diagnostictests.size());
                for (com.smit.ppsa.Response.DiagnosticTest.UserData tu : diagnostictests) {
                    if (!diagnosticTestString.contains(tu.getC_val())) {
                        diagnosticTestString.add(tu.getC_val());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(diagnosticTestSpi, diagnosticTestString);
            }
            if (intent.hasExtra("localPythologylabsample")) {
                pythologyLabsLi = BaseUtils.getPythologyLabSample(PatientSampleList.this);
                Log.d("kioij", "onReceive: " + diagnostictests.size());
                for (com.smit.ppsa.Response.pythologylab.UserData tu : pythologyLabsLi) {
                    if (!pythologyLabsString.contains(tu.getC_hf_nam())) {
                        pythologyLabsString.add(tu.getC_hf_nam());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(pythologyLabs, pythologyLabsString);
            }
            if (intent.hasExtra("localPythologylabtype")) {
                pythologyLabsTypeLi = BaseUtils.getPythologyLabTypes(PatientSampleList.this);
                //   Log.d("kioij", "onReceive: " + diagnostictests.size());
                for (LabResponseInternal list : pythologyLabsTypeLi) {
                    if (!pythologyLabsTypeString.contains(list.getC_val())) {
                        pythologyLabsTypeString.add(list.getC_val());
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(pythologyLabsType, pythologyLabsTypeString);
            }
            if (intent.hasExtra("localtype")) {
                types = BaseUtils.gettype(PatientSampleList.this);
                for (FormOneData stat : types) {
                    if (!typString.contains(stat.getC_sputm_typ())) {
                        typString.add(stat.getC_sputm_typ());
                    }
                }
                setSpinnerAdapter(SputumsampletypeandNumber, typString);
            }
        }
    };

    private boolean isValidate() {
        if (f2_datespecimencollected.getText().toString().equals("")) {
            BaseUtils.showToast(PatientSampleList.this, "Enter date of specimen collected");
            return false;
        } else if (Sampleextractiondoneby.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select sample collected by");
            return false;
        } else if (Sampleextractionfrom.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select sample collected from");
            return false;
        } else if (ReasonforTesting.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select reason for testing");
            return false;
        } else if (noOfContainers.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select no of containers");
            return false;
        } else if (Typeofspecimen.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select type of specimen");
            return false;
        } /* else if (f2_placeofsamplecollection.getText().toString().equals("")) {
            BaseUtils.showToast(PatientSampleList.this, "Enter place of sample collection");
            return false;
        }*/ /* else if (SputumsampletypeandNumber.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select sputum sample type and number");
            return false;
        } */ else if (diagnosticTestSpi.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select diagnostic test");
            return false;
        } else if (pythologyLabs.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(PatientSampleList.this, "Select pythology labs");
            return false;
        }
        return true;
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(PatientSampleList.this, values);
        spinner.setAdapter(spinnerAdapter);
    }

    private void addSample(Context context) {
        if ((testings.get(ReasonforTesting.getSelectedItemPosition() - 1).getC_sputm_typ() == "UDST MC")) {
            if (date_of_diagnosis.getText() == "") {
                BaseUtils.showToast(PatientSampleList.this, "Please select date of diagnosis");
            } else {

                NetworkCalls.addSample2(

                        context,
                        BaseUtils.getUserOtherInfo(PatientSampleList.this).getnStId(),
                        BaseUtils.getUserOtherInfo(PatientSampleList.this).getnDisId(),
                        BaseUtils.getUserOtherInfo(PatientSampleList.this).getnTuId(),
                        getIntent().getStringExtra("hf_id"),
                        BaseUtils.getGlobaldocId(context),
                        enroll_id,
                        f2_datespecimencollected.getText().toString(),
                        extractions.get(Sampleextractiondoneby.getSelectedItemPosition() - 1).getId(),
                        testings.get(ReasonforTesting.getSelectedItemPosition() - 1).getId(),
                        collectedFrom.get(Sampleextractionfrom.getSelectedItemPosition() - 1).getId(),
                        specimens.get(Typeofspecimen.getSelectedItemPosition() - 1).getId(),
                        noOfContainerss.get(noOfContainers.getSelectedItemPosition() - 1).getId(),
                        "0",
                        "0"/*types.get(SputumsampletypeandNumber.getSelectedItemPosition() - 1).getId()*/,
                        diagnostictests.get(diagnosticTestSpi.getSelectedItemPosition() - 1).getId(),
                        pythologyLabsLi.get(pythologyLabs.getSelectedItemPosition() - 1).getId(),
                        BaseUtils.getUserInfo(this).getN_staff_sanc(),
                        BaseUtils.getUserInfo(PatientSampleList.this).getId(), true, date_of_diagnosis.getText().toString(), "1"
                );
                Log.d("button", "add sample2");
                if (testings.get(ReasonforTesting.getSelectedItemPosition() - 1).getC_sputm_typ() == "UDST MC") {
                    NetworkCalls.reasonForTesting(PatientSampleList.this, enroll_id, BaseUtils.getUserInfo(PatientSampleList.this).getnUserLevel(), false, f2_datespecimencollected.getText().toString(), "1");
                }
            }
        } else {

            String docID = "";
            String hosID = "";
            String enrollmentID = "";

            if (getIntent().hasExtra("hf_id")) {
                hosID = getIntent().getStringExtra("hf_id");
            } else {
                hosID = getIntent().getStringExtra("hf_id");
            }

            if (getIntent().hasExtra("doc_id")) {
                docID = getIntent().getStringExtra("doc_id");
            } else {
                docID = BaseUtils.getGlobaldocId(context);
            }

            if (getIntent().hasExtra("enroll_Id")) {
                enrollmentID = getIntent().getStringExtra("enroll_Id");
            } else {
                enrollmentID = enroll_id;
            }

            NetworkCalls.addSampleData(
                    context,
                    BaseUtils.getUserOtherInfo(PatientSampleList.this).getnStId(),
                    BaseUtils.getUserOtherInfo(PatientSampleList.this).getnDisId(),
                    BaseUtils.getUserOtherInfo(PatientSampleList.this).getnTuId(),
                    hosID,
                    docID,
                    enrollmentID,
                    f2_datespecimencollected.getText().toString(),
                    extractions.get(Sampleextractiondoneby.getSelectedItemPosition() - 1).getId(),
                    testings.get(ReasonforTesting.getSelectedItemPosition() - 1).getId(),
                    collectedFrom.get(Sampleextractionfrom.getSelectedItemPosition() - 1).getId(),
                    specimens.get(Typeofspecimen.getSelectedItemPosition() - 1).getId(),
                    noOfContainerss.get(noOfContainers.getSelectedItemPosition() - 1).getId(),
                    "0",
                    "0"/*types.get(SputumsampletypeandNumber.getSelectedItemPosition() - 1).getId()*/,
                    diagnostictests.get(diagnosticTestSpi.getSelectedItemPosition() - 1).getId(),
                     pythologyLabsLi.get(pythologyLabs.getSelectedItemPosition() - 1).getId(),
                    BaseUtils.getUserInfo(this).getN_staff_sanc(),
                    BaseUtils.getUserInfo(PatientSampleList.this).getId(),
                    true
            );


        }

        Log.d("button", "add sample ends");

    }

    private void getPreviousSamples() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(PatientSampleList.this)) {
            BaseUtils.showToast(PatientSampleList.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));


        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));

        String tuId = "51"/*getIntent().getStringExtra("tu_id")*/;

        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smpl_col&w=n_tu_id<<EQUALTO>>" + BaseUtils.getGlobalTuid(PatientSampleList.this) + "<<AND>>n_hf_id<<EQUALTO>>" + BaseUtils.getGlobalhfId(PatientSampleList.this) + "<<AND>>n_doc_id<<EQUALTO>>" + BaseUtils.getGlobaldocId(PatientSampleList.this);
        //String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        ApiClient.getClient().getPreviousSamplesCollection(url).enqueue(new Callback<PreviousSamplesCollection>() {
            @Override
            public void onResponse(Call<PreviousSamplesCollection> call, Response<PreviousSamplesCollection> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataPreviousSamples = response.body().getUser_data();
                        dataBase.customerDao().deletePreviousSampleCollectionFromRoom();
                        Log.d("kokopii", "onResponse: " + response.body().getUser_data().toString());
                        Log.d("kokopii", "onResponse: " + url);
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomPreviousSamplesCollection roomPreviousSamplesCollection = new RoomPreviousSamplesCollection(
                                    response.body().getUser_data().get(i).getC_plc_samp_col(),
                                    response.body().getUser_data().get(i).getD_specm_col(),
                                    response.body().getUser_data().get(i).getDiag_test(),
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getN_dis_id(),
                                    response.body().getUser_data().get(i).getN_doc_id(),
                                    response.body().getUser_data().get(i).getN_enroll_id(),
                                    response.body().getUser_data().get(i).getN_hf_id(),
                                    response.body().getUser_data().get(i).getN_st_id(),
                                    response.body().getUser_data().get(i).getN_tu_id(),
                                    response.body().getUser_data().get(i).getN_user_id(),
                                    response.body().getUser_data().get(i).getSmpl_ext(),
                                    response.body().getUser_data().get(i).getSpecm_typ(),
                                    response.body().getUser_data().get(i).getSputm_sampl(),
                                    response.body().getUser_data().get(i).getTest_reas()
                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousSamplesCollection.getId());

                            dataBase.customerDao().getPreviousSamplesCollectionFromServer(roomPreviousSamplesCollection);
                        }

                        getRoomPreviousSamples();
                        //LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<PreviousSamplesCollection> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomPreviousSamples() {
        LiveData<List<RoomPreviousSamplesCollection>> roomPreviousCollectionFromRoom = dataBase.customerDao().getSelectedRoomPreviousCollectionFromRoom();
        roomPreviousCollectionFromRoom.observe(PatientSampleList.this, roomPreviousVisits1 -> {
            parentDataPreviousSamples = roomPreviousVisits1;
            setRecycler();
        });

    }

    private void setRecycler() {
        previousAdapter = new PreviousSamplesCollectionAdapter(parentDataPreviousSamples, PatientSampleList.this);
        previousSamplesRecycler.setLayoutManager(new LinearLayoutManager(this));
        previousSamplesRecycler.setAdapter(previousAdapter);
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


                m_date.show();
                Calendar calendar1 = Calendar.getInstance();
                try {
                    calendar1.set(Integer.parseInt(reg_date.split("-")[0]),
                            Integer.parseInt(reg_date.split("-")[1]) - 1,
                            Integer.parseInt(reg_date.split("-")[2]));
                } catch (Exception e) {

                }

                Calendar calendar = Calendar.getInstance();
                m_date.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//                m_date.getDatePicker().setMinDate(calendar1.getTimeInMillis());
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });


    }

    private void setUpCalender2() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                date_of_diagnosis.setText(sdf.format(myCalendar.getTime()));
            }
        };
        date_of_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(PatientSampleList.this, R.style.calender_theme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Integer.parseInt(reg_date.split("-")[0]),
                        Integer.parseInt(reg_date.split("-")[1]) - 1,
                        Integer.parseInt(reg_date.split("-")[2]));
                Calendar calendar = Calendar.getInstance();
                m_date.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                //    m_date.getDatePicker().setMinDate(calendar1.getTimeInMillis());
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });


    }
}