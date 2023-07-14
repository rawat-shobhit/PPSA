package com.smit.ppsa;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientsFragment extends DialogFragment {
    private CardView proceedbtn;
    private ImageView backBtn, hl_addbtn;
    private TextView filterTitle;
    private EditText Enrollmentid, Placeofsamplecollection, TestId, DiagnosingFaciltyDistrict,
            DiagnosingFaciltyTBU, DiagnosingFaciltyLab, DiagnosingFaciltyHFcode, DiagnosticTestsOffered, DSTOffered, OtherDSTOffered, FinalInterpretation, TestResult,
            CaseType, PatientStatus, search;
    private Spinner ReasonforTesting, Typeofspecimen, Sampleextractiondoneby, SputumsampletypeandNumber;
    private PatientAdapter patientAdapter;
    PatientAdapter docAdapter;
    PatientCounsellingAdapter docCounsellingAdapter;
    Spinner filterspinner;
    private GlobalProgressDialog progressDialog;
    private List<RoomCounsellingFilters> parentDataFilters = new ArrayList<>();
    private TextView nextbtn;
    private String enroll_id = "";
    private String doc_id = "";
    private String doctorname = "";
    private Activity context;


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        context = activity;

    }

    private String st_id = "";
    private String dis_id = "";
    private String patientname = "";
    private String patientphone = "";
    private String patientage = "";
    private String patienttype = "";
    private String patientdate = "";
    private String hfId = "";
    private String tuId = "";
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new GlobalProgressDialog(getActivity());
        mViewModel = new ViewModelProvider(this).get(FormSixViewModel.class);
        fdcDispensationToHfViewModel = new ViewModelProvider(this).get(FdcDispensationToHfViewModel.class);
        fdcDispensationToPatientViewModel = new ViewModelProvider(this).get(FdcDispensationToPatientViewModel.class);

        dataBase = AppDataBase.getDatabase(getActivity());

      //  Toast.makeText(context, "thissss", Toast.LENGTH_SHORT).show();
        if (BaseUtils.getSubmitLabReportStatus(getActivity()).equals("false")) {
            /*LiveData<List<RoomFormSixData>> formSix = dataBase.customerDao().fetchFormSix();
            formSix.observe(this, formSixData -> {

                if (formSixData.size() != 0) {
                    for (int a = 0; a < formSixData.size(); a++) {
                        Log.d("lplpl", "onCreate: "+ formSixData.get(a).getN_st_id());

                        mViewModel.submitLabReport(
                                formSixData.get(a).getN_st_id(),
                                formSixData.get(a).getN_dis_id(),
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
            mViewModel.submitLabReport(
                    BaseUtils.getn_st_idFormSix(getActivity()),
                    BaseUtils.getn_dis_idFormSix(getActivity()),
                    BaseUtils.getn_tu_idFormSix(getActivity()),
                    BaseUtils.getn_hf_idFormSix(getActivity()),
                    BaseUtils.getn_doc_idFormSix(getActivity()),
                    BaseUtils.getn_enroll_idFormSix(getActivity()),
                    BaseUtils.getn_enroll_idFormSix(getActivity()),
                    BaseUtils.getd_tst_rsltFormSix(getActivity()),
                    BaseUtils.getn_tst_rptFormSix(getActivity()),
                    BaseUtils.getd_rpt_colFormSix(getActivity()),
                    null,
                    null,

                    BaseUtils.getn_latFormSix(getActivity()),
                    BaseUtils.getn_lngFormSix(getActivity()),
                    BaseUtils.getn_staff_infoFormSix(getActivity()),
                    BaseUtils.getn_user_idFormSix(getActivity()),
                    getActivity(),
                    Integer.valueOf(BaseUtils.getn_lab_idFormSix(getActivity())),
                    progressDialog,
                    null,
                    true,
                    BaseUtils.getc_tr_fp_imgFormSix(getActivity()),
                    BaseUtils.getc_tr_bp_imgFormSix(getActivity()),
                    false,
                    BaseUtils.getn_rpt_delFormSix(getActivity())
            );


        }


        if (BaseUtils.getSubmitCounsellingFormStatus(getActivity()).equals("false")) {
         /*   LiveData<List<RoomCounsellingData>> roomCounsellingData = dataBase.customerDao().fetchCounsellingFormData();
            roomCounsellingData.observe(this, roomCounsellingData1 -> {

                if (roomCounsellingData1.size() != 0) {
                    for (int a = 0; a < roomCounsellingData1.size(); a++) {
                        Log.d("lplpl", "onCreate: "+ roomCounsellingData1.get(a).getD_Counn());


                    }
                }

            });
*/
            NetworkCalls.submitCounselling(
                    getActivity(),
                    BaseUtils.getCounsellingFormuser_idd(getActivity()),
                    BaseUtils.getCounsellingFormstaff_infoo(getActivity()),
                    BaseUtils.getCounsellingFormn_stidd(getActivity()),
                    BaseUtils.getCounsellingFormn_disIdd(getActivity()),
                    BaseUtils.getCounsellingFormn_docIdd(getActivity()),
                    BaseUtils.getCounsellingFormlatt(getActivity()),
                    BaseUtils.getCounsellingFormlngg(getActivity()),
                    BaseUtils.getCounsellingFormn_typeCounn(getActivity()),
                    BaseUtils.getCounsellingFormd_Counn(getActivity()),
                    BaseUtils.getCounsellingFormneenrollIDD(getActivity()),
                    BaseUtils.getCounsellingFormnTuIdd(getActivity()),
                    BaseUtils.getCounsellingFormnHfidd(getActivity()),
                    new GlobalProgressDialog(getActivity()),
                    false
            );

        }

        if (BaseUtils.getSubmitFdcDispensationHfForm(getActivity()).equals("false")) {
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
                    Integer.valueOf(BaseUtils.getFdcHfn_st_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_dis_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_tu_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_hf_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_doc_id(getActivity())),
                    BaseUtils.getFdcHfd_issue(getActivity()),
                    Integer.valueOf(BaseUtils.getFdcHfn_med_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_uom_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_qty(getActivity())),
                    // Integer.valueOf(BaseUtils.getFdcHfn_etham(this)),
                    BaseUtils.getFdcHfn_lat(getActivity()),
                    BaseUtils.getFdcHfn_lng(getActivity()),
                    Integer.valueOf(BaseUtils.getFdcHfn_staff_info(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcHfn_user_id(getActivity())),
                    getActivity(),
                    new GlobalProgressDialog(getActivity()),
                    null, null, null, false

            );


        }

        if (BaseUtils.getAddSampleForm(getActivity()).equals("false")) {

          /*  LiveData<List<RoomAddSample>> samples = dataBase.customerDao().fetchAddSample();
            samples.observe(this, addSamples -> {

                if (addSamples.size() != 0) {
                    for (int a = 0; a < addSamples.size(); a++) {

                    }
                }

            });*/
            NetworkCalls.addSample(
                    getActivity(),
                    BaseUtils.getAddSamplen_st_idd(getActivity()),
                    BaseUtils.getAddSamplen_dis_idd(getActivity()),
                    BaseUtils.getAddSamplen_tu_idd(getActivity()),
                    BaseUtils.getAddSamplen_hf_idd(getActivity()),
                    BaseUtils.getAddSamplen_doc_idd(getActivity()),
                    BaseUtils.getAddSamplen_enroll_idd(getActivity()),
                    BaseUtils.getAddSampled_specm_coll(getActivity()),
                    BaseUtils.getAddSamplen_smpl_ext_idd(getActivity()),
                    BaseUtils.getAddSamplen_test_reas_idd(getActivity()),
                    BaseUtils.getAddSamplen_purp_vstt(getActivity()),
                    BaseUtils.getAddSamplen_typ_specm_idd(getActivity()),
                    BaseUtils.getAddSamplen_cont_smpll(getActivity()),
                    BaseUtils.getAddSamplec_plc_samp_coll(getActivity()),
                    BaseUtils.getAddSamplen_sputm_typ_idd(getActivity()),
                    BaseUtils.getAddSamplen_diag_tstt(getActivity()),
                    BaseUtils.getAddSamplen_lab_idd(getActivity()),
                    BaseUtils.getAddSamplen_staff_infoo(getActivity()),
                    BaseUtils.getAddSamplen_user_idd(getActivity()),
                    false
            );

        }

        if (BaseUtils.getAddTestForm(getActivity()).equals("false")) {
            NetworkCalls.postTest(
                    getActivity(),
                    BaseUtils.getn_st_idTest(getActivity()),
                    BaseUtils.getn_dis_idTest(getActivity()),
                    BaseUtils.getn_tu_idTest(getActivity()),
                    BaseUtils.getn_hf_idTest(getActivity()),
                    BaseUtils.getn_enroll_idTest(getActivity()),
                    BaseUtils.getn_smpl_col_idTest(getActivity()),
                    BaseUtils.getd_smpl_recdTest(getActivity()),
                    BaseUtils.getd_rpt_docTest(getActivity()),
                    BaseUtils.getn_diag_testTest(getActivity()),
                    BaseUtils.getd_tst_diagTest(getActivity()),
                    BaseUtils.getd_tst_rpt_diagTest(getActivity()),
                    BaseUtils.getn_dstTest(getActivity()),
                    BaseUtils.getd_tst_dstTest(getActivity()),
                    BaseUtils.getd_tst_rpt_dstTest(getActivity()),
                    BaseUtils.getn_oth_dstTest(getActivity()),
                    BaseUtils.getd_tst_rpt_oth_dstTest(getActivity()),
                    BaseUtils.getd_tst_oth_dstTest(getActivity()),
                    BaseUtils.getn_fnl_intpTest(getActivity()),
                    BaseUtils.getn_tst_rsultTest(getActivity()),
                    BaseUtils.getn_case_typTest(getActivity()),
                    BaseUtils.getn_pat_statusTest(getActivity()),
                    BaseUtils.getn_user_idTest(getActivity()),
                    BaseUtils.getn_latTest(getActivity()),
                    BaseUtils.getn_lngTest(getActivity()),
                    false

            );
        }
        if (BaseUtils.getAddDocFormFormStatus(getActivity()).equals("false")) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View dialogView = li.inflate(R.layout.dialog_adddoctor, null);
            androidx.appcompat.app.AlertDialog sDialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(true).create();
            LiveData<List<DoctorModel>> doctorModels = dataBase.customerDao().fetchAllDoctor();
            doctorModels.observe(this, doctorModels1 -> {

                if (doctorModels1.size() != 0) {
                    for (int a = 0; a < doctorModels1.size(); a++) {
                        Log.d("lplpl", "onCreate: "+ doctorModels1.get(a).getId());

                        NetworkCalls.setAddDoc(
                                getActivity(),
                                doctorModels1.get(a).getC_doc_nam(),
                                doctorModels1.get(a).getC_regno(),
                                doctorModels1.get(a).getN_qual_id(),
                                doctorModels1.get(a).getN_spec_id(),
                                doctorModels1.get(a).getC_mob(),
                                BaseUtils.getAdddochospitalName(getActivity()),
                                BaseUtils.getAdddoctype(getActivity()),
                                BaseUtils.getAdddocnHfId(getActivity()),
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
        if (BaseUtils.getAddTestForm(getActivity()).equals("false")) {
            NetworkCalls.postTest(
                    getActivity(),
                    BaseUtils.getn_st_idTest(getActivity()),
                    BaseUtils.getn_dis_idTest(getActivity()),
                    BaseUtils.getn_tu_idTest(getActivity()),
                    BaseUtils.getn_hf_idTest(getActivity()),
                    BaseUtils.getn_enroll_idTest(getActivity()),
                    BaseUtils.getn_smpl_col_idTest(getActivity()),
                    BaseUtils.getd_smpl_recdTest(getActivity()),
                    BaseUtils.getd_rpt_docTest(getActivity()),
                    BaseUtils.getn_diag_testTest(getActivity()),
                    BaseUtils.getd_tst_diagTest(getActivity()),
                    BaseUtils.getd_tst_rpt_diagTest(getActivity()),
                    BaseUtils.getn_dstTest(getActivity()),
                    BaseUtils.getd_tst_dstTest(getActivity()),
                    BaseUtils.getd_tst_rpt_dstTest(getActivity()),
                    BaseUtils.getn_oth_dstTest(getActivity()),
                    BaseUtils.getd_tst_rpt_oth_dstTest(getActivity()),
                    BaseUtils.getd_tst_oth_dstTest(getActivity()),
                    BaseUtils.getn_fnl_intpTest(getActivity()),
                    BaseUtils.getn_tst_rsultTest(getActivity()),
                    BaseUtils.getn_case_typTest(getActivity()),
                    BaseUtils.getn_pat_statusTest(getActivity()),
                    BaseUtils.getn_user_idTest(getActivity()),
                    BaseUtils.getn_latTest(getActivity()),
                    BaseUtils.getn_lngTest(getActivity()),
                    false

            );
        }

        if (BaseUtils.getSubmitFdcDispensationPaForm(getActivity()).equals("false")) {
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
                    Integer.valueOf(BaseUtils.getFdcPan_st_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_dis_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_tu_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_hf_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_doc_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_enroll_id(getActivity())),
                    BaseUtils.getFdcPad_issue(getActivity()),
                    Integer.valueOf(BaseUtils.getFdcPan_wght_bnd(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_med_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_uom_id(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_qty(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_days(getActivity())),
                    BaseUtils.getFdcPan_lat(getActivity()),
                    BaseUtils.getFdcPan_lng(getActivity()),
                    Integer.valueOf(BaseUtils.getFdcPan_staff_info(getActivity())),
                    Integer.valueOf(BaseUtils.getFdcPan_user_id(getActivity())),
                    getActivity(),
                    new GlobalProgressDialog(getActivity()), null, false


            );

        }

        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broadcastReceiver, new IntentFilter(""));
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(broadcastReceiver2, new IntentFilter("patient"));
        initViews(view);
    }

    private void initViews(View view) {

        progressDialog = new GlobalProgressDialog(getActivity());

        patientrecycler = view.findViewById(R.id.f2_patientrecycler);
        proceedbtn = view.findViewById(R.id.bt_proceedtwo);
        backBtn = view.findViewById(R.id.backbtn);
        hl_addbtn = view.findViewById(
                R.id.hl_addbtn
        );
        nextbtn = view.findViewById(R.id.nextbtn);
        Enrollmentid = view.findViewById(R.id.f2_enrollmentid);
        ReasonforTesting = view.findViewById(R.id.f2_reasonfortesting);
        Typeofspecimen = view.findViewById(R.id.f2_typeofspecimen);
        Placeofsamplecollection = view.findViewById(R.id.f2_placeofsamplecollection);
        Sampleextractiondoneby = view.findViewById(R.id.f2_sampleexractiondoneby);
        SputumsampletypeandNumber = view.findViewById(R.id.f2_sputumsampletypeandnumber);
        TestId = view.findViewById(R.id.f2_testid);
        DiagnosingFaciltyDistrict = view.findViewById(R.id.f2_diagnosingfaciltydistrict);
        DiagnosingFaciltyTBU = view.findViewById(R.id.f2_diagnosingfaciltytbu);
        DiagnosingFaciltyLab = view.findViewById(R.id.f2_diagnosingfaciltylab);
        DiagnosingFaciltyHFcode = view.findViewById(R.id.f2_diagnosingfaciltyhfcode);
        DiagnosticTestsOffered = view.findViewById(R.id.f2_diagnotictestsoffered);
        DSTOffered = view.findViewById(R.id.f2_dstoffered);
        OtherDSTOffered = view.findViewById(R.id.f2_otherdstoffered);
        FinalInterpretation = view.findViewById(R.id.f2_finalinterpretation);
        TestResult = view.findViewById(R.id.f2_testresults);
        CaseType = view.findViewById(R.id.f2_casetype);
        PatientStatus = view.findViewById(R.id.f2_patientstatus);
        backBtn = view.findViewById(R.id.backbtn);
        search = view.findViewById(R.id.search);
        filterspinner = view.findViewById(R.id.filterCounsell);
        filterTitle = view.findViewById(R.id.filtertt);

        if (getActivity().getIntent().hasExtra("counsel")){

            filterspinner.setVisibility(View.VISIBLE);
            filterTitle.setVisibility(View.VISIBLE);

        }else {
            filterspinner.setVisibility(View.GONE);
            filterTitle.setVisibility(View.GONE);
        }

        backBtn = view.findViewById(R.id.backbtn);
//        NetworkCalls.getTesting(this);
//        NetworkCalls.getSpecimen(this);
//        NetworkCalls.getExtraction(this);
//        NetworkCalls.getType(this);
        setOnclick();
        getFilters();
        //getPatient();
        filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    filter = "";
                    Log.d("dded", "onItemSelected: " + filter);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    filter = parentDataFilters.get(i - 1).getC_val();
                    filter(filter, "pure");
                    Log.d("dded", "onItemSelected: " + filter);
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
        doc_id = getActivity().getIntent().getStringExtra("doc_id");

        Log.d("jku", "initViews: " + getActivity().getIntent().getStringExtra("doc_id"));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("gloplh", "onStart: " + getActivity().getIntent().getStringExtra("hf_id"));
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
            if (type.equals("normal")){
                String value = d.getcPatNam().toLowerCase();
                if (value.contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }else {
                String value = d.getNo_days().toLowerCase();
                if (text.equals("1st Counselling Pending less than 48 hrs")) {
                    if (Integer.valueOf(value) < 3) {
                        temp.add(d);
                    }
                } else  if (text.equals("1st Counselling Pending More than 48 Hrs")) {
                    if (Integer.valueOf(value) > 3) {
                        temp.add(d);
                    }
                }
                else  if (text.equals(">= 15 Days from Last Follow up")) {
                    if (Integer.valueOf(value) >= 15) {
                        temp.add(d);
                    }
                }
                else  if (text.equals(">= 30 Days from Last Follow up")) {
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
        if (!BaseUtils.isNetworkAvailable(getActivity())) {
            BaseUtils.showToast(getActivity(), "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        Log.d("dkl9", "getPatientdd: " + getActivity().getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(getActivity()).getnUserLevel());


        //http://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smpl_col&w=n_enroll_id<<EQUALTO>>46321

        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_coun_filters&w=id<<GT>>0";
        ApiClient.getClient().getCounsellingFilters(url).enqueue(new Callback<FilterResponse>() {
            @Override
            public void onResponse(Call<FilterResponse> call, Response<FilterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.putCounsellingFormPatientName(getActivity(), getActivity().getIntent().getStringExtra("patient_name"));

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

                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


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
        roomCounsellingFilter.observe(getActivity(), roomCounsellingFilters -> {
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
        proceedbtn.setOnClickListener(this::onClick);
        backBtn.setOnClickListener(this::onClick);
        hl_addbtn.setOnClickListener(this::onClick);
        nextbtn.setOnClickListener(this::onClick);
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_proceedtwo:
                startActivity(new Intent(getActivity(), FormThree.class));
                break;
            case R.id.backbtn:
                dismiss();
                break;
            case R.id.nextbtn:
                if (enroll_id.equals("")) {
                    BaseUtils.showToast(getActivity(), "Please select patient");
                } else {
                    BaseUtils.putGlobalTuid(getActivity(), String.valueOf(tuId));
                    BaseUtils.putGlobalDocid(getActivity(), String.valueOf(doc_id));
                    BaseUtils.putGlobalHfid(getActivity(), String.valueOf(hfId));
                    BaseUtils.putGlobalPatientName(getActivity(), String.valueOf(patientname));
                    BaseUtils.putGlobalHftypeid(getActivity(), getActivity().getIntent().getStringExtra("hf_type_id"));
                    if (getActivity().getIntent().hasExtra("counsel")) {
                        Log.d("lpoi", "onClick: " + tuId);
                        dismiss();
                        startActivity(new Intent(getActivity(), CounsellingForm.class).putExtra("doc_name", doctorname)
                                .putExtra("patient_name", patientname)
                                .putExtra("doc_id", doc_id)
                                .putExtra("dis_id", dis_id)
                                .putExtra("st_id", st_id)
                                .putExtra("patient_phone", patientphone)
                                .putExtra("hospitalName", getActivity().getIntent().getStringExtra("hospitalName"))
                                .putExtra("tu_id", tuId)
                                .putExtra("hf_id", hfId).putExtra("enroll_id", enroll_id)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else if (getActivity().getIntent().hasExtra("reportdelivery")) {
                        startActivity(new Intent(getActivity(), FormSix.class)
                                .putExtra("hf_id", hfId).putExtra("enroll_id", enroll_id)
                                .putExtra("doc_id", doc_id).putExtra("doc_name", doctorname)
                                .putExtra("patient_name", patientname)
                                .putExtra("patient_age", patientage)
                                .putExtra("patient_phone", patientphone)
                                .putExtra("hospitalName", getActivity().getIntent().getStringExtra("hospitalName"))
                                .putExtra("reg_date", patientdate)
                                .putExtra("patient_type", patienttype)
                                .putExtra("hf_type_id", getActivity().getIntent().getStringExtra("hf_type_id"))
                                .putExtra("tu_id", tuId));
                    } else if (getActivity().getIntent().hasExtra("sample")) {
                        startActivity(new Intent(getActivity(), SampleList.class)
                                .putExtra("hf_id", hfId)
                                .putExtra("hospitalName", getActivity().getIntent().getStringExtra("hospitalName"))
                                .putExtra("doc_name", doctorname)
                                .putExtra("patient_name", patientname)
                                .putExtra("patient_phone", patientphone)
                                .putExtra("hf_type_id", getActivity().getIntent().getStringExtra("hf_type_id"))
                                .putExtra("tu_id", tuId).putExtra("enroll_id", enroll_id));
                    } else if (getActivity().getIntent().hasExtra("fdc")) {
                        startActivity(new Intent(getActivity(), FdcDispensationToPatient.class)
                                .putExtra("hf_id", hfId)
                                .putExtra("doc_name", doctorname)
                                .putExtra("doc_id", doc_id)
                                .putExtra("enroll_id", enroll_id)
                                .putExtra("patient_name", patientname)
                                .putExtra("tu_id", tuId)
                                .putExtra("hospitalName", getActivity().getIntent().getStringExtra("hospitalName")));
                    }
                }
                break;
            case R.id.hl_addbtn:
                startActivity(new Intent(getActivity(), HospitalFacility.class).putExtra("hf_id", getActivity().getIntent().getStringExtra("hf_id")).putExtra("tu_id", tuId).putExtra("type", "tree"));
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
                specimens = BaseUtils.getSpecimen(getActivity());
                for (FormOneData specimen : specimens) {
                    specimenStrings.add(specimen.getC_typ_specm());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(Typeofspecimen, specimenStrings);
            }
            if (intent.hasExtra("localextract")) {
                extractions = BaseUtils.getExtraction(getActivity());
                for (UserData tu : extractions) {
                    extractString.add(tu.getC_smpl_ext());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(Sampleextractiondoneby, extractString);
            }
            if (intent.hasExtra("localcollectedfrom")) {
                collectedFrom = BaseUtils.getCollectedFrom(getActivity());
                Log.d("kioij", "onReceive: " + extractions.size());
                for (com.smit.ppsa.Response.CollectedFrom.UserData tu : collectedFrom) {
                    collectedFromString.add(tu.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                // setSpinnerAdapter(Sampleextractionfrom, collectedFromString);
            }
            if (intent.hasExtra("localnoOfCont")) {
                noOfContainerss = BaseUtils.getNoOfCont(getActivity());
                Log.d("kioij", "onReceive: " + extractions.size());
                for (com.smit.ppsa.Response.noOfCont.UserData tu : noOfContainerss) {
                    noOfContString.add(tu.getC_val());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                // setSpinnerAdapter(noOfContainers, noOfContString);
            }
            if (intent.hasExtra("localtype")) {
                types = BaseUtils.gettype(getActivity());
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
        }
    };
    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // if (intent.hasExtra())
            if (intent.hasExtra("enroll_id")) {
                enroll_id = intent.getStringExtra("enroll_id");
            }
            if (getActivity().getIntent().hasExtra("doc_id")) {

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
            if (intent.hasExtra("patient_phone")) {
                patientphone = intent.getStringExtra("patient_phone");
            }

            if (intent.hasExtra("patient_age")) {
                patientage = intent.getStringExtra("patient_age");
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

        }
    };

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(context, values);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getPatient() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(getActivity())) {
            BaseUtils.showToast(getActivity(), "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        Log.d("dkl9", "getPatientdd: " + getActivity().getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(getActivity()).getnUserLevel());


        // String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_user_id<<EQUALTO>>" + BaseUtils.getUserInfo(FormTwo.this).getnUserLevel();
        String url = "_spat_coun.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_coun_visits&w=1" /*+ getIntent().getStringExtra("hf_id")*/;
        ApiClient.getClient().getRegisterParent(url).enqueue(new Callback<RegisterParentResponse>() {
            @Override
            public void onResponse(Call<RegisterParentResponse> call, Response<RegisterParentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        parentData = response.body().getUserData();
/*

                        Log.d("jsuju", "onResponse: " + parentData.size());
                        for (int i = 0; i < parentData.size(); i++) {
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getTestReas());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSpecmTyp());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getdSpecmCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getcPlcSampCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSmplExt());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSputmSampl());


                            RoomPatientList roomPatientList = new RoomPatientList(
                                    parentData.get(i).getdRegDat(),
                                    parentData.get(i).getnNkshId(),
                                    parentData.get(i).getcPatNam(),
                                    parentData.get(i).getnAge(),
                                    parentData.get(i).getcTyp(),
                                    parentData.get(i).getnWght(),
                                    parentData.get(i).getnHght(),
                                    parentData.get(i).getcAdd(),
                                    parentData.get(i).getcTaluka(),
                                    parentData.get(i).getcTown(),
                                    parentData.get(i).getcWard(),
                                    parentData.get(i).getcLndMrk(),
                                    parentData.get(i).getnPin(),
                                    parentData.get(i).getcDocNam(),
                                    parentData.get(i).getId(),
                                    parentData.get(i).getnStId(),
                                    parentData.get(i).getnDisId(),
                                    parentData.get(i).getnTuId(),
                                    parentData.get(i).getnHfId(),
                                    parentData.get(i).getnDocId(),
                                    parentData.get(i).getnUserId(),
                                    parentData.get(i).getNo_days(),
                                    parentData.get(i).getLST_DT()
                            );
                            dataBase.customerDao().getPatientsFromServer(roomPatientList);
                        }
                        LocalBroadcastManager.getInstance(FormTwo.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
*/
                        setHospitalRecycler(parentData);

                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<RegisterParentResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    private void getRoomDoctors() {
        LiveData<List<RoomPatientList>> roomDoc = dataBase.customerDao().getSelectedPatientFromRoom(getActivity().getIntent().getStringExtra("hf_id"));
        roomDoc.observe(getActivity(), roomDoctorsLists -> {
            this.roomPatientLists = roomDoctorsLists;
            //    setHospitalRecycler(this.roomPatientLists);
        });
    }

    private void setHospitalRecycler(List<RegisterParentData> roomDoctorsLists) {
       /* if (getActivity().getIntent().hasExtra("counsel")){
            docCounsellingAdapter = new PatientCounsellingAdapter(roomDoctorsLists, getActivity());
            patientrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            patientrecycler.setAdapter(docCounsellingAdapter);
        }else {
            docAdapter = new PatientAdapter(roomDoctorsLists, getActivity(), "");
            patientrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            patientrecycler.setAdapter(docAdapter);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        enroll_id = "";
        getPatient();
    }
}