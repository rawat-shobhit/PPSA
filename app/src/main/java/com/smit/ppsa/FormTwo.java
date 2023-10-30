package com.smit.ppsa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.PatientAdapter;
import com.smit.ppsa.Adapter.PatientCounsellingAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.CollectedBy.UserData;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.FilterResponse;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.Response.RegisterParentResponse;
import com.smit.ppsa.Response.RoomCounsellingFilters;
import com.smit.ppsa.Response.RoomPatientList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormTwo extends AppCompatActivity implements View.OnClickListener {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private String hospitaltypename = "";
    private CardView proceedbtn;
    private ImageView backBtn, hl_addbtn;
    private TextView filterTitle, counselTuTitle;

    private  boolean isFirstTym;
    List<String> tuStrings = new ArrayList<>();
    private List<FormOneData> tu = new ArrayList<>();
    private EditText Enrollmentid, Placeofsamplecollection, TestId, DiagnosingFaciltyDistrict,
            DiagnosingFaciltyTBU, DiagnosingFaciltyLab, DiagnosingFaciltyHFcode, DiagnosticTestsOffered, DSTOffered, OtherDSTOffered, FinalInterpretation, TestResult,
            CaseType, PatientStatus, search;
    private Spinner ReasonforTesting, tuCounsell, Typeofspecimen, Sampleextractiondoneby, SputumsampletypeandNumber;
    private PatientAdapter patientAdapter;
    PatientAdapter docAdapter;
    PatientCounsellingAdapter docCounsellingAdapter;
    Spinner filterspinner;
    private GlobalProgressDialog progressDialog;
    private List<RoomCounsellingFilters> parentDataFilters = new ArrayList<>();
    private TextView nextbtn,searchbtn;
    private String enroll_id = "";
    private String doc_id = "";
    private String doctorname = "";
    private String st_id = "";
    private String dis_id = "";
    private String reg_date = "";
    private String patientname = "";
    private String patientphone = "";
    private String patientage = "";
    private String hospitalname = "";
    private String niksh_id = "";
    private String patienttype = "";
    String tus="";
    private String hfNam = "";
    private String patientdate = "";
    private String hfId = "";
    private String resn = "";
    private String diag_test = "";
    private String tuId = "";
    private String d_sample = "";
    private String smpl_col_id = "";
    private int n_diag_cd = -1;
    private List<FormOneData> specimens = new ArrayList<>();
    private List<FormOneData> testings = new ArrayList<>();
    private List<FormOneData> types = new ArrayList<>();
    private List<UserData> extractions = new ArrayList<>();
    private List<com.smit.ppsa.Response.CollectedFrom.UserData> collectedFrom = new ArrayList<>();
    private List<String> specimenStrings = new ArrayList<>();
    private List<com.smit.ppsa.Response.noOfCont.UserData> noOfContainerss = new ArrayList<>();
    private List<String> testingStrings = new ArrayList<>();
    private List<String> extractString = new ArrayList<>();
    private List<String> collectedFromString = new ArrayList<>();
    private List<String> noOfContString = new ArrayList<>();
    private List<String> typString = new ArrayList<>();
    private List<RegisterParentData> parentData = new ArrayList<>();
    private List<RoomPatientList> roomPatientLists = new ArrayList<>();
    private AppDataBase dataBase;
    String filter = "";
    private RecyclerView patientrecycler;
    FormSixViewModel mViewModel;
    FdcDispensationToHfViewModel fdcDispensationToHfViewModel;
    FdcDispensationToPatientViewModel fdcDispensationToPatientViewModel;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_two);
        progressDialog = new GlobalProgressDialog(this);
        mViewModel = new ViewModelProvider(this).get(FormSixViewModel.class);
        fdcDispensationToHfViewModel = new ViewModelProvider(this).get(FdcDispensationToHfViewModel.class);
        fdcDispensationToPatientViewModel = new ViewModelProvider(this).get(FdcDispensationToPatientViewModel.class);
        title = findViewById(R.id.title);

        String from = getIntent().getStringExtra("from");


//        Log.d("shobhit",from.toString());
//
//        if(from=="main")
//        {
//            counselTuTitle.setVisibility(View.GONE);
//            tuCounsell.setVisibility(View.GONE);
//        }

        /*
Hritik  All points are covered in the latest aPP

1:- In Hiv and DM " Test not done " need to add..........................
2:- TU needs to be removed in counselling..........................
3:- options need to be given for upload consent form if the patient does not want NPY ..........................
4:- Status of Notification document also needs to be uploaded documents. ..........................
5:- sample collection date needs to be opened for back dated entry ..........................
7:- option for uploading patients proscription if treatment is extended. ..
         */
//  1

        // 1 2 3 5
    //    Toast.makeText(this, "checking 1231", Toast.LENGTH_SHORT).show();

        dataBase = AppDataBase.getDatabase(this);


        /*    if (BaseUtils.getSubmitLabReportStatus(this).equals("false")) {
         */
         /*LiveData<List<RoomFormSixData>> formSix = dataBase.customerDao().fetchFormSix();
            formSix.observe(this, formSixData ->
            {

                if (formSixData.size() != 0) {
                    for (int a = 0; a < formSixData.size(); a++) {
                        Log.d("lplpl", "onCreate: "+ formSixData.get(a).getN_st_id());

                        mViewModel.submitLabReport(
                                formSixData.get(a).getN_st_id(),
                                formSixData.get(a).rgetN_dis_id(),
                                formSixData.get(a).getN_tu_id(),
                                formSixData.get(a).getN_hf_id(),
                                formSixData.get(a).getN_doc_id(),
                                formSixData.get(a).getN_enroll_id(),
                                formSixData.get(a).getN_enroll_id(),
                                formSixData.get(a).getD_tst_rslt(),
                                formSixData.get(a).getN_tst_rpt(),
                                formSixData.get(a).getD_rpt_col(),
                                null,
                                null,
                                formSixData.get(a).getD_lpa_smpl(),
                                formSixData.get(a).getN_lpa_rslt(),
                                formSixData.get(a).getN_lat(),
                                formSixData.get(a).getN_lng(),
                                formSixData.get(a).getN_staff_info(),
                                formSixData.get(a).getN_user_id(),
                                getApplicationContext(),
                                Integer.valueOf(formSixData.get(a).getN_lab_id()),
                                progressDialog,
                                null,
                                true,
                                formSixData.get(a).getC_tr_fp_img(),
                                formSixData.get(a).getC_tr_bp_img(),
                                false
                        );
                    }
                }

            });*/
        /*
            mViewModel.submitLabReport(
                    BaseUtils.getn_st_idFormSix(this),
                    BaseUtils.getn_dis_idFormSix(this),
                    BaseUtils.getn_tu_idFormSix(this),
                    BaseUtils.getn_hf_idFormSix(this),
                    BaseUtils.getn_doc_idFormSix(this),
                    BaseUtils.getn_enroll_idFormSix(this),
                    BaseUtils.getn_enroll_idFormSix(this),
                    BaseUtils.getd_tst_rsltFormSix(this),
                    BaseUtils.getn_tst_rptFormSix(this),
                    BaseUtils.getd_rpt_colFormSix(this),
                    null,
                    null,
                    BaseUtils.getd_lpa_smplFormSix(this),
                    BaseUtils.getn_lpa_rsltFormSix(this),
                    BaseUtils.getn_latFormSix(this),
                    BaseUtils.getn_lngFormSix(this),
                    BaseUtils.getn_staff_infoFormSix(this),
                    BaseUtils.getn_user_idFormSix(this),
                    getApplicationContext(),
                    Integer.valueOf(BaseUtils.getn_lab_idFormSix(this)),
                    progressDialog,
                    null,
                    true,
                    BaseUtils.getc_tr_fp_imgFormSix(this),
                    BaseUtils.getc_tr_bp_imgFormSix(this),
                    false,
                    BaseUtils.getn_rpt_delFormSix(this)
            );


        }*/




        if (getIntent().hasExtra("report_col")) {

            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(""));
            LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver2, new IntentFilter("patient"));
            title.setText("Report collection");

            initViews();
            hl_addbtn.setVisibility(View.INVISIBLE);
            backBtn.setOnClickListener(this);
            nextbtn.setOnClickListener(this);
            filterTitle.setText("TU");
            filterspinner.setVisibility(View.GONE);
            filterTitle.setVisibility(View.GONE);
            filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        tuId = "0";

                    } else {
                        //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                        Log.d("mosojdo", "onItemSelected: " + tu.size());

                        tuId = tu.get(/*i - 1*/i - 1).getN_tu_id();//selecting the second value in list first value is null
                        Log.d("mosojdo", "onItemSelected: " + tuId);

                        /*if (tuId.equals(BaseUtils.getSelectedTu(HospitalsList.this))) {
                            setHospitalRecycler();
                        }*/

                        //  NetworkCalls.getTUPatient(FormTwo.this, tuId);


                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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
                    if (parentData.size() == 0) {

                    } else {
                        filter(editable.toString(), "normal");
                    }

                }
            });
            NetworkCalls.getTU(this);

        } else {

            if (BaseUtils.getSubmitCounsellingFormStatus(this).equals("false")) {


         /*   LiveData<List<RoomCounsellingData>> roomCounsellingData = dataBase.customerDao().fetchCounsellingFormData();
            roomCounsellingData.observe(this, roomCounsellingData1 -> {
            // the issue that is in upload document will be resolved by backend side

                if (roomCounsellingData1.size() != 0) {
                    for (int a = 0; a < roomCounsellingData1.size(); a++) {
                        Log.d("lplpl", "onCreate: "+ roomCounsellingData1.get(a).getD_Counn());
                    }
                }
            });
*/
                NetworkCalls.submitCounselling(
                        this,
                        BaseUtils.getCounsellingFormuser_idd(this),
                        BaseUtils.getCounsellingFormstaff_infoo(this),
                        BaseUtils.getCounsellingFormn_stidd(this),
                        BaseUtils.getCounsellingFormn_disIdd(this),
                        BaseUtils.getCounsellingFormn_docIdd(this),
                        BaseUtils.getCounsellingFormlatt(this),
                        BaseUtils.getCounsellingFormlngg(this),
                        BaseUtils.getCounsellingFormn_typeCounn(this),
                        BaseUtils.getCounsellingFormd_Counn(this),
                        BaseUtils.getCounsellingFormneenrollIDD(this),
                        BaseUtils.getCounsellingFormnTuIdd(this),
                        BaseUtils.getCounsellingFormnHfidd(this),
                        new GlobalProgressDialog(this),
                        false
                );

            }

            if (BaseUtils.getSubmitFdcDispensationHfForm(this).equals("false")) {
      /*      LiveData<List<RoomFdcDispensationHf>> roomFdcDispensationHf = dataBase.customerDao().fetchFdcDispensationHfData();
            roomFdcDispensationHf.observe(this, roomFdcDispensationHfs -> {

                if (roomFdcDispensationHfs.size() != 0) {
                    for (int a = 0; a < roomFdcDispensationHfs.size(); a++) {
                        Log.d("lplpl", "onCreate: " + roomFdcDispensationHfs.get(a).getD_issue());

                        fdcDispensationToHfViewModel.submitFdcDispensationToHf(
                                roomFdcDispensationHfs.get(a).getN_st_id(),
                                roomFdcDispensationHfs.get(a).getN_dis_id(),
                                roomFdcDispensationHfs.get(a).getN_tu_id(),
                                roomFdcDispensationHfs.get(a).getN_hf_id(),
                                roomFdcDispensationHfs.get(a).getN_doc_id(),
                                roomFdcDispensationHfs.get(a).getD_issue(),
                                roomFdcDispensationHfs.get(a).getN_fdc2(),
                                roomFdcDispensationHfs.get(a).getN_fdc3(),
                                roomFdcDispensationHfs.get(a).getN_fdc4(),
                                roomFdcDispensationHfs.get(a).getN_etham(),
                                roomFdcDispensationHfs.get(a).getN_lat(),
                                roomFdcDispensationHfs.get(a).getN_lng(),
                                roomFdcDispensationHfs.get(a).getN_staff_info(),
                                roomFdcDispensationHfs.get(a).getN_user_id(),
                                this,
                                new GlobalProgressDialog(this),
                                null, null, null, false

                        );
                    }
                }

            });*/

                fdcDispensationToHfViewModel.submitFdcDispensationToHf(
                        Integer.valueOf(BaseUtils.getFdcHfn_st_id(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_dis_id(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_tu_id(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_hf_id(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_doc_id(this)),
                        BaseUtils.getFdcHfd_issue(this),
                        Integer.valueOf(BaseUtils.getFdcHfn_med_id(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_uom_id(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_qty(this)),
                        // Integer.valueOf(BaseUtils.getFdcHfn_etham(this)),
                        BaseUtils.getFdcHfn_lat(this),
                        BaseUtils.getFdcHfn_lng(this),
                        Integer.valueOf(BaseUtils.getFdcHfn_staff_info(this)),
                        Integer.valueOf(BaseUtils.getFdcHfn_user_id(this)),
                        this,
                        new GlobalProgressDialog(this),
                        null, null, null, false

                );


            }

            if (BaseUtils.getAddSampleForm(this).equals("false")) {

          /*  LiveData<List<RoomAddSample>> samples = dataBase.customerDao().fetchAddSample();
            samples.observe(this, addSamples -> {

                if (addSamples.size() != 0) {
                    for (int a = 0; a < addSamples.size(); a++) {

                    }
                }

            });*/
                NetworkCalls.addSample(
                        this,
                        BaseUtils.getAddSamplen_st_idd(this),
                        BaseUtils.getAddSamplen_dis_idd(this),
                        BaseUtils.getAddSamplen_tu_idd(this),
                        BaseUtils.getAddSamplen_hf_idd(this),
                        BaseUtils.getAddSamplen_doc_idd(this),
                        BaseUtils.getAddSamplen_enroll_idd(this),
                        BaseUtils.getAddSampled_specm_coll(this),
                        BaseUtils.getAddSamplen_smpl_ext_idd(this),
                        BaseUtils.getAddSamplen_test_reas_idd(this),
                        BaseUtils.getAddSamplen_purp_vstt(this),
                        BaseUtils.getAddSamplen_typ_specm_idd(this),
                        BaseUtils.getAddSamplen_cont_smpll(this),
                        BaseUtils.getAddSamplec_plc_samp_coll(this),
                        BaseUtils.getAddSamplen_sputm_typ_idd(this),
                        BaseUtils.getAddSamplen_diag_tstt(this),
                        BaseUtils.getAddSamplen_lab_idd(this),
                        BaseUtils.getAddSamplen_staff_infoo(this),
                        BaseUtils.getAddSamplen_user_idd(this),
                        false
                );

            }

            if (BaseUtils.getAddTestForm(this).equals("false")) {
                NetworkCalls.postTest(
                        this,
                        BaseUtils.getn_st_idTest(this),
                        BaseUtils.getn_dis_idTest(this),
                        BaseUtils.getn_tu_idTest(this),
                        BaseUtils.getn_hf_idTest(this),
                        BaseUtils.getn_enroll_idTest(this),
                        BaseUtils.getn_smpl_col_idTest(this),
                        BaseUtils.getd_smpl_recdTest(this),
                        BaseUtils.getd_rpt_docTest(this),
                        BaseUtils.getn_diag_testTest(this),
                        BaseUtils.getd_tst_diagTest(this),
                        BaseUtils.getd_tst_rpt_diagTest(this),
                        BaseUtils.getn_dstTest(this),
                        BaseUtils.getd_tst_dstTest(this),
                        BaseUtils.getd_tst_rpt_dstTest(this),
                        BaseUtils.getn_oth_dstTest(this),
                        BaseUtils.getd_tst_rpt_oth_dstTest(this),
                        BaseUtils.getd_tst_oth_dstTest(this),
                        BaseUtils.getn_fnl_intpTest(this),
                        BaseUtils.getn_tst_rsultTest(this),
                        BaseUtils.getn_case_typTest(this),
                        BaseUtils.getn_pat_statusTest(this),
                        BaseUtils.getn_user_idTest(this),
                        BaseUtils.getn_latTest(this),
                        BaseUtils.getn_lngTest(this),
                        false

                );
            }
            if (BaseUtils.getAddDocFormFormStatus(this).equals("false")) {
                LayoutInflater li = LayoutInflater.from(this);
                View dialogView = li.inflate(R.layout.dialog_adddoctor, null);
                androidx.appcompat.app.AlertDialog sDialog = new AlertDialog.Builder(this).setView(dialogView).setCancelable(true).create();
                LiveData<List<DoctorModel>> doctorModels = dataBase.customerDao().fetchAllDoctor();
                doctorModels.observe(this, doctorModels1 -> {

                    if (doctorModels1.size() != 0) {
                        for (int a = 0; a < doctorModels1.size(); a++) {
                            Log.d("lplpl", "onCreate: " + doctorModels1.get(a).getId());

                            NetworkCalls.setAddDoc(
                                    this,
                                    doctorModels1.get(a).getC_doc_nam(),
                                    doctorModels1.get(a).getC_regno(),
                                    doctorModels1.get(a).getN_qual_id(),
                                    doctorModels1.get(a).getN_spec_id(),
                                    doctorModels1.get(a).getC_mob(),
                                    BaseUtils.getAdddochospitalName(this),
                                    BaseUtils.getAdddoctype(this),
                                    BaseUtils.getAdddocnHfId(this),
                                    sDialog,
                                    false
                            );
                        }
                    }

                });

/*
            for (int a = 0; a < doctorModels.size(); a++) {
                setAddDoc(doctorModels.get(a).getC_doc_nam(),
                        doctorModels.get(a).getN_qual_id(),
                        doctorModels.get(a).getN_spec_id(),
                        doctorModels.get(a).getC_mob()
                );

            }*/

            }
            if (BaseUtils.getAddTestForm(this).equals("false")) {
                NetworkCalls.postTest(
                        this,
                        BaseUtils.getn_st_idTest(this),
                        BaseUtils.getn_dis_idTest(this),
                        BaseUtils.getn_tu_idTest(this),
                        BaseUtils.getn_hf_idTest(this),
                        BaseUtils.getn_enroll_idTest(this),
                        BaseUtils.getn_smpl_col_idTest(this),
                        BaseUtils.getd_smpl_recdTest(this),
                        BaseUtils.getd_rpt_docTest(this),
                        BaseUtils.getn_diag_testTest(this),
                        BaseUtils.getd_tst_diagTest(this),
                        BaseUtils.getd_tst_rpt_diagTest(this),
                        BaseUtils.getn_dstTest(this),
                        BaseUtils.getd_tst_dstTest(this),
                        BaseUtils.getd_tst_rpt_dstTest(this),
                        BaseUtils.getn_oth_dstTest(this),
                        BaseUtils.getd_tst_rpt_oth_dstTest(this),
                        BaseUtils.getd_tst_oth_dstTest(this),
                        BaseUtils.getn_fnl_intpTest(this),
                        BaseUtils.getn_tst_rsultTest(this),
                        BaseUtils.getn_case_typTest(this),
                        BaseUtils.getn_pat_statusTest(this),
                        BaseUtils.getn_user_idTest(this),
                        BaseUtils.getn_latTest(this),
                        BaseUtils.getn_lngTest(this),
                        false

                );
            }

            if (BaseUtils.getSubmitFdcDispensationPaForm(this).equals("false")) {
       /*     LiveData<List<RoomFdcDispensationPatient>> roomFdcDispensationPatient = dataBase.customerDao().fetchFdcDispensationPatientData();
            roomFdcDispensationPatient.observe(this, roomFdcDispensationPatients -> {

                if (roomFdcDispensationPatients.size() != 0) {
                    for (int a = 0; a < roomFdcDispensationPatients.size(); a++) {
                        Log.d("lplpl", "onCreate: " + roomFdcDispensationPatients.get(a).getD_issue());

                        fdcDispensationToPatientViewModel.submitFdcDispensationToPatient(
                                roomFdcDispensationPatients.get(a).getN_st_id(),
                                roomFdcDispensationPatients.get(a).getN_dis_id(),
                                roomFdcDispensationPatients.get(a).getN_tu_id(),
                                roomFdcDispensationPatients.get(a).getN_hf_id(),
                                roomFdcDispensationPatients.get(a).getN_doc_id(),
                                roomFdcDispensationPatients.get(a).getN_enroll_id(),
                                roomFdcDispensationPatients.get(a).getD_issue(),
                                roomFdcDispensationPatients.get(a).getN_fdc2(),
                                roomFdcDispensationPatients.get(a).getN_fdc3(),
                                roomFdcDispensationPatients.get(a).getN_fdc4(),
                                roomFdcDispensationPatients.get(a).getN_etham(),
                                roomFdcDispensationPatients.get(a).getN_lat(),
                                roomFdcDispensationPatients.get(a).getN_lng(),
                                roomFdcDispensationPatients.get(a).getN_staff_info(),
                                roomFdcDispensationPatients.get(a).getN_user_id(), this,
                                new GlobalProgressDialog(this), null, false


                        );
                    }
                }

            });
*/

                fdcDispensationToPatientViewModel.submitFdcDispensationToPatient(
                        Integer.valueOf(BaseUtils.getFdcPan_st_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_dis_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_tu_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_hf_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_doc_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_enroll_id(this)),
                        BaseUtils.getFdcPad_issue(this),
                        Integer.valueOf(BaseUtils.getFdcPan_wght_bnd(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_med_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_uom_id(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_qty(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_days(this)),
                        BaseUtils.getFdcPan_lat(this),
                        BaseUtils.getFdcPan_lng(this),
                        Integer.valueOf(BaseUtils.getFdcPan_staff_info(this)),
                        Integer.valueOf(BaseUtils.getFdcPan_user_id(this)),
                        this,
                        new GlobalProgressDialog(this), null, false


                );

            }

            LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
            LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver2, new IntentFilter("patient"));
            initViews();
            function();
        }

    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
        if (broadcastReceiver2 != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver2);
        }
        super.onDestroy();
    }

    private void initViews() {

        if (BaseUtils.getSection(FormTwo.this).equals("addpat")) {
            BaseUtils.putSection(FormTwo.this, BaseUtils.getPrevSection(FormTwo.this));
        }

        tuCounsell = findViewById(R.id.tuCounsell);
        counselTuTitle = findViewById(R.id.counselTuTitle);
        patientrecycler = findViewById(R.id.f2_patientrecycler);
        proceedbtn = findViewById(R.id.bt_proceedtwo);
        backBtn = findViewById(R.id.backbtn);
        hl_addbtn = findViewById(
                R.id.hl_addbtn
        );
        if (!BaseUtils.haveAccess(this)) {
            hl_addbtn.setVisibility(View.GONE);
        }
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
        search = findViewById(R.id.search);
        searchbtn = findViewById(R.id.searchbtn);
        filterspinner = findViewById(R.id.filterCounsell);
        filterTitle = findViewById(R.id.filtertt);
        backBtn = findViewById(R.id.backbtn);
//        NetworkCalls.getTesting(this);
//        NetworkCalls.getSpecimen(this);
//        NetworkCalls.getExtraction(this);
//        NetworkCalls.getType(this);

    }

    private void function() {
        if (getIntent().hasExtra("counsel")) {
//            counselTuTitle.setVisibility(View.GONE);
//            tuCounsell.setVisibility(View.VISIBLE);

            filterspinner.setVisibility(View.VISIBLE);
            hl_addbtn.setVisibility(View.INVISIBLE);
            filterTitle.setVisibility(View.VISIBLE);
            NetworkCalls.getTU(this);
        } else {
            counselTuTitle.setVisibility(View.GONE);
            tuCounsell.setVisibility(View.GONE);
            filterspinner.setVisibility(View.GONE);
            filterTitle.setVisibility(View.GONE);
        }

        setOnclick();
        getFilters();

        Log.d("shobhitChecking","on Create:- getPatient");
    //    getPatient();

        tuCounsell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else if (filterspinner.getSelectedItemPosition() == 0) {
                    BaseUtils.showToast(FormTwo.this, "Please select filter to view list");
                  patientrecycler.setVisibility(View.GONE);
                } else {

                    patientrecycler.setVisibility(View.VISIBLE);

                    try {
                        getCounselPatList();
                      //  BaseUtils.showToast(FormTwo.this, tuCounsell.getSelectedItemPosition() + "");
                    } catch (Exception e) {

                    }


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

                }
//                else if (tuCounsell.getSelectedItemPosition() == 0) {
//                    BaseUtils.showToast(FormTwo.this, "Select TU first to view patient list");
//                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
//                    /*filter = parentDataFilters.get(i - 1).getC_val();
//                    filter(filter, "pure");
//                    Log.d("dded", "onItemSelected: " + filter);*/
//                    patientrecycler.setVisibility(View.GONE);
//
//                }
                else {
                    patientrecycler.setVisibility(View.VISIBLE);
                    Log.d("shobhitChecking"," 704 else filter spinner");
                    getCounselPatList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parentData.size() == 0) {

                } else {
                    if(search.getText().toString().length()>=4){
                        filter(search.getText().toString(), "normal");
                    }else{
                        BaseUtils.showToast(FormTwo.this,"Please enter atleast 4 characters.");
                    }
                }
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

//                if (parentData.size() == 0) {
//
//                } else {
//                    filter(editable.toString(), "normal");
//                }


            }
        });
        doc_id = getIntent().getStringExtra("doc_id");

        Log.d("jku", "initViews: " + getIntent().getStringExtra("doc_id"));
    }

    private void getCounselPatList() {

        Log.d("shobhitChecking","getCounselPatList");

        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(FormTwo.this, "Please Check your internet  Connectivity");
            progressDialog.hideProgressBar();
//   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }

//        String url = "_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=" + parentDataFilters.get(filterspinner.getSelectedItemPosition() - 1).getId() + "&n_tu=" + tu.get(tuCounsell.getSelectedItemPosition() - 1).getN_tu_id();

//_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=4&n_tu=n_tu_id<<EQUALTO>>26<<OR>>n_tu_id<<EQUALTO>>28&sanc=820
        String url = "_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=" + parentDataFilters.get(filterspinner.getSelectedItemPosition() - 1).getId() + "&n_tu=" + tus+"&sanc="+ BaseUtils.getUserInfo(this).getN_staff_sanc();
/*
full path yeh hai
https://nikshayppsa.hlfppt.org/_api-v1_/_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=1&n_tu=n_tu_id<<EQUALTO>>26<<OR>>n_tu_id<<EQUALTO>>28&sanc=820
//
 */
        Log.d("used_url_1", url);
        ApiClient.getClient().getTUPatient(url).enqueue(new Callback<RegisterParentResponse>() {
            @Override
            public void onResponse(Call<RegisterParentResponse> call, Response<RegisterParentResponse> response) {
                progressDialog.hideProgressBar();
                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {
                        BaseUtils.saveTuPatientList(FormTwo.this, response.body().getUserData());
                        LocalBroadcastManager.getInstance(FormTwo.this).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));


                    } else {

                        parentData.clear();
                        BaseUtils.saveTuPatientList(FormTwo.this, parentData);

                        LocalBroadcastManager.getInstance(FormTwo.this).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));


                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterParentResponse> call, Throwable t) {
                progressDialog.hideProgressBar();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("gloplh", "onStart: " + getIntent().getStringExtra("hf_id"));
    }

    private void filter(String text, String type) {
       /* ArrayList<RoomPatientList> temp = new ArrayList();
        for (RoomPatientList d : roomPatientLists) {
            String value = d.getcPatNam().toLowerCase();
            if (value.contains(text.toLowerCase())) {
                temp.add(d);
            }
        }*/


        ArrayList<RegisterParentData> temp = new ArrayList();
        for (RegisterParentData d : parentData) {
            if (type.equals("normal")) {
                String value = d.getcPatNam().toLowerCase();
                if (value.contains(text.toLowerCase())) {
                    temp.add(d);
                }
            } else {
                String value = d.getNo_days().toLowerCase();
                if (text.equals("1st Counselling Pending less than 48 hrs")) {
                    if (Integer.valueOf(value) < 3) {
                        temp.add(d);
                    }
                } else if (text.equals("1st Counselling Pending More than 48 Hrs")) {
                    if (Integer.valueOf(value) > 3) {
                        temp.add(d);
                    }
                } else if (text.equals(">= 15 Days from Last Follow up")) {
                    if (Integer.valueOf(value) >= 15) {
                        temp.add(d);
                    }
                } else if (text.equals(">= 30 Days from Last Follow up")) {
                    if (Integer.valueOf(value) >= 30) {
                        temp.add(d);
                    }
                }
            }
        }
        // docAdapter = new PatientAdapter(parentData, FormTwo.this);
        if (docCounsellingAdapter != null) {
            docCounsellingAdapter.updateList(temp);
        }
        if (docAdapter != null) {
            docAdapter.updateList(temp);
        }
    }

    private void getFilters() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormTwo.this)) {
            BaseUtils.showToast(FormTwo.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormTwo.this).getnUserLevel());


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_coun_filters&w=id<<GT>>0";
        Log.d("used_url_2",url.toString());
        ApiClient.getClient().getCounsellingFilters(url).enqueue(new Callback<FilterResponse>() {
            @Override
            public void onResponse(Call<FilterResponse> call, Response<FilterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.putCounsellingFormPatientName(FormTwo.this, getIntent().getStringExtra("patient_name"));

                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomCounsellingFilters roomCounsellingFilters = new RoomCounsellingFilters(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse:"+ roomCounsellingFilters.getId());
                            dataBase.customerDao().getCounsellingFilterFromServer(roomCounsellingFilters);
                        }

                        Log.d("gug", "onResponse: " + response.body().getStatus());

                        getRoomCounsellingFilters();

                        LocalBroadcastManager.getInstance(FormTwo.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


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

    private void getRoomCounsellingFilters() {
        LiveData<List<RoomCounsellingFilters>> roomCounsellingFilter = dataBase.customerDao().getSelectedCounsellingFilterFromRoom();
        roomCounsellingFilter.observe(FormTwo.this, roomCounsellingFilters -> {
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

    private void setOnclick() {
        proceedbtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        hl_addbtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        if (BaseUtils.getSection(FormTwo.this).equals("addpat")) {
            BaseUtils.putSection(FormTwo.this, BaseUtils.getPrevSection(FormTwo.this));
        } else if (BaseUtils.getSection(FormTwo.this).equals("reportdelivery") || BaseUtils.getSection(FormTwo.this).equals("counsel")) {
            startActivity(
                    new Intent(FormTwo.this, HospitalsList.class)
                            .putExtra("reportdelivery", "").putExtra("backpress", "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
            finish();
        } else if (BaseUtils.getSection(FormTwo.this).equals("counsel")) {
            startActivity(
                    new Intent(FormTwo.this, HospitalsList.class)
                            .putExtra("counsel", "").putExtra("backpress", "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
            finish();
        } else {
            finish();
            super.onBackPressed();
        }

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
                if (enroll_id.equals("")) {
                    BaseUtils.showToast(FormTwo.this, "Please select patient");
                } else {
                    BaseUtils.putGlobalTuid(FormTwo.this, String.valueOf(tuId));
                    BaseUtils.putGlobalDocid(FormTwo.this, String.valueOf(doc_id));
                    BaseUtils.putGlobalHfid(FormTwo.this, String.valueOf(hfId));
                    BaseUtils.putGlobalPatientName(FormTwo.this, String.valueOf(patientname));
                    BaseUtils.putGlobalHftypeid(FormTwo.this, getIntent().getStringExtra("hf_type_id"));
                    if (getIntent().hasExtra("counsel")) {
                        Log.d("lpoi", "onClick: " + tuId);
                        startActivity(new Intent(FormTwo.this, CounsellingForm.class).putExtra("doc_name", doctorname)
                                .putExtra("patient_name", patientname)
                                .putExtra("doc_id", doc_id)
                                .putExtra("dis_id", dis_id)
                                .putExtra("hf_type_id", /*hf_type_id*/"1")
                                //  .putExtra("hf_id", hfId)
                                .putExtra("counsel", "po")
                                .putExtra("reg_date", reg_date)
                                .putExtra("st_id", st_id)
                                .putExtra("patient_phone", patientphone)
                                .putExtra("hospitalName", hfNam)
                                .putExtra("tu_id", tuId).putExtra("hf_id", hfId).putExtra("enroll_id", enroll_id));
                    } else if (getIntent().hasExtra("report_col")) {

                       // BaseUtils.showToast(FormTwo.this, BaseUtils.getUserInfo(this).getnDisCd().toString());
                        startActivity(new Intent(FormTwo.this, FormSix.class)
                                .putExtra("hf_id", hfId).putExtra("enroll_id", enroll_id)
                                .putExtra("doc_id", doc_id).putExtra("doc_name", doctorname)
                                .putExtra("patient_name", patientname)
                                .putExtra("patient_age", patientage)
                                .putExtra("d_sample", d_sample)
                                .putExtra("smpl_col_id", smpl_col_id)
                                .putExtra("niksh_id", niksh_id)
                                .putExtra("patient_phone", patientphone)
                                .putExtra("hospitalName", hfNam)
                                .putExtra("hospitaltypeName", hospitaltypename)
                                .putExtra("reg_date", patientdate)
                                .putExtra("patient_type", patienttype)
                                .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                                .putExtra("tu_id", tuId)
                                .putExtra("diag_test", diag_test)
                                .putExtra("n_diag_cd", n_diag_cd)
                                .putExtra("resn", resn));
                    } else if (getIntent().hasExtra("sample")) {

//                        Toast.makeText(this, "from form 2", Toast.LENGTH_SHORT).show();



                        startActivity(new Intent(FormTwo.this, SampleList.class)
                                .putExtra("hf_id", hfId)
                                .putExtra("hospitalName", getIntent().getStringExtra("hospitalName"))
                                .putExtra("doc_name", doctorname)
                                .putExtra("patient_name", patientname)
                                .putExtra("patient_phone", patientphone)
                                .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                                .putExtra("enrolldate", reg_date)
                                .putExtra("tu_id", tuId).putExtra("enroll_id", enroll_id));
                    } else if (getIntent().hasExtra("fdc")) {
                        startActivity(new Intent(FormTwo.this, FdcDispensationToPatient.class)
                                .putExtra("hf_id", hfId)
                                .putExtra("doc_name", doctorname)
                                .putExtra("doc_id", doc_id)
                                .putExtra("enroll_id", enroll_id)
                                .putExtra("patient_name", patientname)
                                .putExtra("tu_id", tuId)
                                .putExtra("hospitalName", getIntent().getStringExtra("hospitalName")));
                    } else if (getIntent().hasExtra("issued")) {
                        Intent intent = new Intent(FormTwo.this, FdcForm.class);
                        intent.putExtra("tu_id", getIntent().getStringExtra("tu_id"));
                        intent.putExtra("hf_id", getIntent().getStringExtra("hf_id"));
                        intent.putExtra("hospitalName", getIntent().getStringExtra("hospitalName"));
                        intent.putExtra("docName", getIntent().getStringExtra("docName"));
                        intent.putExtra("doc_id", getIntent().getStringExtra("doc_id"));
                        intent.putExtra("issued", "patient");
                        intent.putExtra("patient_name", patientname);
                        intent.putExtra("enroll_id", enroll_id);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.hl_addbtn:
                BaseUtils.putPrevSection(FormTwo.this, BaseUtils.getSection(FormTwo.this));
                BaseUtils.putSection(FormTwo.this, "addpat");
                startActivity(new Intent(FormTwo.this, HospitalFacility.class).putExtra("hf_id", getIntent().getStringExtra("hf_id")).putExtra("tu_id", tuId).putExtra("type", "tree"));
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
                for (UserData tu : extractions) {
                    extractString.add(tu.getC_smpl_ext());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(Sampleextractiondoneby, extractString);
            }
            if (intent.hasExtra("localcollectedfrom")) {
                collectedFrom = BaseUtils.getCollectedFrom(FormTwo.this);
                Log.d("kioij", "onReceive: " + extractions.size());
                for (com.smit.ppsa.Response.CollectedFrom.UserData tu : collectedFrom) {
                    collectedFromString.add(tu.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                // setSpinnerAdapter(Sampleextractionfrom, collectedFromString);
            }
            if (intent.hasExtra("localnoOfCont")) {
                noOfContainerss = BaseUtils.getNoOfCont(FormTwo.this);
                Log.d("kioij", "onReceive: " + extractions.size());
                for (com.smit.ppsa.Response.noOfCont.UserData tu : noOfContainerss) {
                    noOfContString.add(tu.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                // setSpinnerAdapter(noOfContainers, noOfContString);
            }
            if (intent.hasExtra("localtype")) {
                types = BaseUtils.gettype(FormTwo.this);
                for (FormOneData stat : types) {
                    typString.add(stat.getC_sputm_typ());
                }
                setSpinnerAdapter(SputumsampletypeandNumber, typString);
            }
            if (intent.hasExtra("setRecycler")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
                    //getRoomDoctors();
                    // setHospitalRecycler();
                }, 1000);
            }
            if (intent.hasExtra("localTU")) {
                Log.d("gjuy", "onReceive: njkguyg");
                tu = BaseUtils.getTU(FormTwo.this);
                Log.d("mijop", "onReceive: " + tu.size());
                Log.d("mijop", "onReceive: " + tu.toString());

                 tus = "";

                //w=n_tu_id<<EQUALTO>>2<<OR>>n_tu_id<<EQUALTO>>3<<OR>>n_tu_id<<EQUALTO>>4
                for (int a = 0; a < tu.size(); a++) {
                    if (!tuStrings.contains(tu.get(a).getcTuName())) {
                        tuStrings.add(tu.get(a).getcTuName());
                        if (a < tu.size() - 1) {
                            tus = tus + "n_tu_id<<EQUALTO>>" + tu.get(a).getN_tu_id() + "<<OR>>";
                        } else {
                            tus = tus + "n_tu_id<<EQUALTO>>" + tu.get(a).getN_tu_id();
                        }
                    }


//https://nikshayppsa.hlfppt.org/_api-v1_/__spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=4&n_tu=n_tu_id<<EQUALTO>>26<<OR>>n_tu_id<<EQUALTO>>28&sanc=820
                }
                // BaseUtils.showToast(FormTwo.this,tus);
                NetworkCalls.getTUPatient(FormTwo.this, tus);
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                if (!getIntent().hasExtra("counsel")) {
                    setSpinnerAdapter(filterspinner, tuStrings);
                    if (getIntent().hasExtra("report_col")) {

                    } else {
//                        filterspinner.setSelection(1);
                    }

                }
                setSpinnerAdapter(tuCounsell, tuStrings);
//                tuCounsell.setSelection(1);


            } else if (intent.hasExtra("notifyAdapter")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        //Do something after 1000ms
                        setPatientAdapter();
                    }
                }, 1000);
            }

        }
    };

    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // if (intent.hasExtra())
            if (intent.hasExtra("enroll_id")) {
                enroll_id = intent.getStringExtra("enroll_id");
            }
            if (getIntent().hasExtra("doc_id")) {

            } else {
                if (intent.hasExtra("doc_id")) {
                    doc_id = intent.getStringExtra("doc_id");
                }
            }

            if (intent.hasExtra("doc_name")) {
                doctorname = intent.getStringExtra("doc_name");
            }
            if (intent.hasExtra("doc_name")) {
                doctorname = intent.getStringExtra("doc_name");
            }

            if (intent.hasExtra("patient_name")) {
                patientname = intent.getStringExtra("patient_name");
            }

            if (intent.hasExtra("reg_date")) {
                reg_date = intent.getStringExtra("reg_date");
            }
            if (intent.hasExtra("patient_phone")) {
                try {
                    patientphone = intent.getStringExtra("patient_phone");
                }catch (Exception e){
                    patientphone="";
                }
//                Log.d("phoneNumber",patientphone);
            }else{
                patientphone="";
            }

            if (intent.hasExtra("patient_age")) {
                patientage = intent.getStringExtra("patient_age");
            }

            if (intent.hasExtra("hospital_name")) {
                hospitalname = intent.getStringExtra("hospital_name");
            }

            if (intent.hasExtra("niksh_id")) {
                niksh_id = intent.getStringExtra("niksh_id");
            }

            if (intent.hasExtra("reg_date")) {
                patientdate = intent.getStringExtra("reg_date");
            }
            if (intent.hasExtra("patient_type")) {
                patienttype = intent.getStringExtra("patient_type");
            }
            if (intent.hasExtra("hf_id")) {
                hfId = intent.getStringExtra("hf_id");
            }
            if (intent.hasExtra("tu_id")) {
                tuId = intent.getStringExtra("tu_id");
            }
            if (intent.hasExtra("st_id")) {
                st_id = intent.getStringExtra("st_id");
            }
            if (intent.hasExtra("dis_id")) {
                dis_id = intent.getStringExtra("dis_id");
            }
            if (intent.hasExtra("diag_test")) {
                diag_test = intent.getStringExtra("diag_test");
            }
            if (intent.hasExtra("resn")) {
                resn = intent.getStringExtra("resn");
            }
            if (intent.hasExtra("hf_nam")) {
                hfNam = intent.getStringExtra("hf_nam");
            }
            if (intent.hasExtra("n_diag_cd")) {
                n_diag_cd = intent.getIntExtra("n_diag_cd", -1);
            }
            if (intent.hasExtra("d_sample")) {
                d_sample = intent.getStringExtra("d_sample");
            }
            if (intent.hasExtra("smpl_col_id")) {
                smpl_col_id = intent.getStringExtra("smpl_col_id");
            }
            if (intent.hasExtra("hospitaltypename")) {
                hospitaltypename = intent.getStringExtra("hospitaltypename");
            }
        }
    };

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FormTwo.this, values);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getPatient() {

        Log.d("shobhitChecking","function :- getPatient");

        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormTwo.this)) {
            BaseUtils.showToast(FormTwo.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            LocalBroadcastManager.getInstance(FormTwo.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            progressDialog.hideProgressBar();
            return;
        }


        Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormTwo.this).getnUserLevel());


        String urlOne = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id");
        String url = "_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=1" /*+ getIntent().getStringExtra("hf_id")*/;
        String urlTwo = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smplcol_indv_lst&w=n_tu_id<<EQUALTO>>" + tuId;

        String usedUrl = "";

        Log.d("huhopu", "getPatient: " + urlOne);



        if (getIntent().hasExtra("counsel")) {
            usedUrl = url;
        } else if (getIntent().hasExtra("reportdelivery")) {
            usedUrl = urlTwo;
        } else {
            usedUrl = urlOne;
        }
        //https://nikshayppsa.hlfppt.org/_api-v1_/_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=2&sanc=838

        Log.d("used_url_3",usedUrl);
        BaseUtils.showToast(FormTwo.this,"Please wait while we fetch data for you.");

        ApiClient.getClient().getRegisterParent(usedUrl).enqueue(new Callback<RegisterParentResponse>() {
            @Override
            public void onResponse(Call<RegisterParentResponse> call, Response<RegisterParentResponse> response) {
                progressDialog.hideProgressBar();
                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {
                        // parentData = response.body().getUserData();

                        Log.d("popipok", "onResponse: " + response.body().getUserData().size());
                        parentData.clear();
                        List<RegisterParentData> nett = new ArrayList<>();
                        for (int i = 0; i < response.body().getUserData().size(); i++) {
                            //   if (i < 20){
                            RegisterParentData da = response.body().getUserData().get(i);
                            /*if (!parentData.contains(da)) {*/
                            if (da.getcPatNam().equals("yhb") || da.getcPatNam().equals("cg") || da.getcPatNam().equals("yuunat")) {

                            } else {
                                parentData.add(da);
                            }

                            if (i == response.body().getUserData().size() - 1) {
                                setHospitalRecycler(parentData);
                            }

                            //}
                            //   }
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<RegisterParentResponse> call, Throwable t) {
                progressDialog.hideProgressBar();

             //   BaseUtils.showToast(FormTwo.this,t.getMessage());
                //    progressDialog.hideProgressBar();
            }
        });
    }

    private void getRoomDoctors() {
        LiveData<List<RoomPatientList>> roomDoc = dataBase.customerDao().getSelectedPatientFromRoom(getIntent().getStringExtra("hf_id"));
        roomDoc.observe(FormTwo.this, roomDoctorsLists -> {
            this.roomPatientLists = roomDoctorsLists;
            //    setHospitalRecycler(this.roomPatientLists);
        });
    }

    private void setPatientAdapter() {
        Log.d("shobhitChecking"," -- setPatientAdapter");
        //checkingShobhit127
        parentData = BaseUtils.getTuPatient(this);
        if (title.getText() == "Report collection") {
            docAdapter = new PatientAdapter(
                    parentData,
                    FormTwo.this,
                    "reportdelivery");

            Log.d("checkingList",parentData.get(0).toString());
            patientrecycler.setLayoutManager(new LinearLayoutManager(this));
            patientrecycler.setAdapter(docAdapter);
        } else {
            Log.d("shobhitChecking"," --else  setPatientAdapter");
            docAdapter = new PatientAdapter(
                    parentData,
                    FormTwo.this,
                    "Patient");
//            Log.d("checkingList",parentData.stream().toArray());
            patientrecycler.setLayoutManager(new LinearLayoutManager(this));
            patientrecycler.setAdapter(docAdapter);
        }

    }

    private void setHospitalRecycler(List<RegisterParentData> roomDoctorsLists) {
        if (getIntent().hasExtra("counsel")) {
            Log.d("kuuiy", "setHospitalRecycler: " + roomDoctorsLists.toString());

            docCounsellingAdapter = new PatientCounsellingAdapter(roomDoctorsLists, FormTwo.this, "counsel");
            patientrecycler.setLayoutManager(new LinearLayoutManager(this));
            patientrecycler.setAdapter(docCounsellingAdapter);

            progressDialog.hideProgressBar();
        } else {
            docAdapter = new PatientAdapter(
                    roomDoctorsLists,
                    FormTwo.this,
                    getIntent().getStringExtra("section"));
            Log.d("kuuiy", "setHospitalRecycler: " + roomDoctorsLists.toString());
            patientrecycler.setLayoutManager(new LinearLayoutManager(this));
            patientrecycler.setAdapter(docAdapter);

            progressDialog.hideProgressBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enroll_id = "";


        try {

                progressDialog.hideProgressBar();

        }catch (Exception e){
                Log.d("checking",e.toString());
        }
        if (!getIntent().hasExtra("report_col")) {

            Log.d("shobhitChecking","onResume:- getPatient");
             getPatient();
        }
    }

}