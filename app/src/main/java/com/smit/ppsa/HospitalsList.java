package com.smit.ppsa;

import static java.sql.DriverManager.println;

import static kotlinx.coroutines.CoroutineScopeKt.CoroutineScope;
import static kotlinx.coroutines.DelayKt.delay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.FdcHospitalsAdapter;
import com.smit.ppsa.Adapter.HospitalsAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.FormOneResponse;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.HospitalModel;
import com.smit.ppsa.Response.HospitalResponse;
import com.smit.ppsa.Response.PrevVisitsResponse;
import com.smit.ppsa.Response.RoomPrevVisitsData;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalsList extends AppCompatActivity implements View.OnClickListener {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private ImageView backbtn, addbtn;
    private TextView nextbtn;
    private EditText search;
    private static ApiClient.APIInterface apiInterface;

    private CheckBox checkboxNonVisit;
    private boolean isFirstTymOnThisPage = true;
    private boolean isDataChanged = false;
    private static List<FormOneData> TuList = new ArrayList<>();

    private Thread thread = new Thread("name");
    //    private TextView hospitalNameTt, hospitalNameLocation, doctorNameTv, hospitalTypeTitle,
    //    currentDate, hospitalNameTv, hospitalAddress, hospitalType, lastVisit, date;
    List<String> tuStrings = new ArrayList<>();
    private List<FormOneData> tu = new ArrayList<>();
    private AppDataBase dataBase;
    private List<HospitalModel> hospitalModelList = new ArrayList<>();
    private RecyclerView hospitalRecycler;
    private String hfID, tuId = "0", hospitalName, fdctype = "", doctorName = "", hf_type_id = "",
            hospitaltypeName = "", hospitallocation = "", lastvisit = "";
//    HospitalsAdapter hospitalsAdapter;

    FdcHospitalsAdapter fdcHospitalsAdapter;
    List<HospitalList> hospitalLists = new ArrayList<>();
//    List<HospitalList> selectedHospitalLists = new ArrayList<>();
    private Spinner ResidentialTU;
    FormSixViewModel mViewModel;
    private GlobalProgressDialog progressDialog;
    FdcDispensationToHfViewModel fdcDispensationToHfViewModel;
    FdcOpeningStockBalanceViewModel fdcOpeningStockBalanceViewModel;
    FdcDispensationToPatientViewModel fdcDispensationToPatientViewModel;
    List<RoomPrevVisitsData> parentDataPreviousSamples;
    Date currentTime = Calendar.getInstance().getTime();
//    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa");
    boolean checked = false;
    FdcReceivedViewModel receiveViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals_list);
        dataBase = AppDataBase.getDatabase(this);
        receiveViewModel = new ViewModelProvider(this).get(FdcReceivedViewModel.class);
        progressDialog = new GlobalProgressDialog(this);
        mViewModel = new ViewModelProvider(this).get(FormSixViewModel.class);
        fdcDispensationToHfViewModel = new ViewModelProvider(this).get(FdcDispensationToHfViewModel.class);
        fdcOpeningStockBalanceViewModel = new ViewModelProvider(this).get(FdcOpeningStockBalanceViewModel.class);
        fdcDispensationToPatientViewModel = new ViewModelProvider(this).get(FdcDispensationToPatientViewModel.class);




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

        /*if (BaseUtils.getSubmitLabReportStatus(this).equals("false")) {
         */
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
        /*       if (BaseUtils.getFormOne(this).equals("false")) {
            LiveData<List<FormOneModel>> allCustomer = dataBase.customerDao().fetchFormOne();
            allCustomer.observe(this, hospitalModels -> {

                if (hospitalModels.size() != 0) {
                    for (int a = 0; a < hospitalModels.size(); a++) {
                        Log.d("lplpl", "onCreate: " + hospitalModels.get(a).getN_st_id());

                        NetworkCalls.sendForm(this,
                                hospitalModels.get(a).getN_st_id(),
                                hospitalModels.get(a).getN_dis_id(),
                                hospitalModels.get(a).getN_tu_id(),
                                hospitalModels.get(a).getN_hf_id(),
                                hospitalModels.get(a).getN_doc_id(),
                                hospitalModels.get(a).getD_reg_dat(),
                                hospitalModels.get(a).getN_nksh_id(),
                                hospitalModels.get(a).getC_pat_nam(),
                                hospitalModels.get(a).getN_age(),
                                hospitalModels.get(a).getN_sex(),
                                hospitalModels.get(a).getN_wght(),
                                hospitalModels.get(a).getN_hght(),
                                hospitalModels.get(a).getC_add(),
                                hospitalModels.get(a).getC_taluka(),
                                hospitalModels.get(a).getC_town(),
                                hospitalModels.get(a).getC_ward(),
                                hospitalModels.get(a).getC_lnd_mrk(),
                                hospitalModels.get(a).getN_pin(),
                                hospitalModels.get(a).getN_st_id_res(),
                                hospitalModels.get(a).getN_dis_id_res(),
                                hospitalModels.get(a).getN_tu_id_res(),
                                hospitalModels.get(a).getC_mob(),
                                hospitalModels.get(a).getC_mob_2(),
                                hospitalModels.get(a).getN_lat(),
                                hospitalModels.get(a).getN_lng(),
                                hospitalModels.get(a).getN_user_id(),
                                "",
                                false,
                                hospitalModels.get(a).getNotification_image(),
                                hospitalModels.get(a).getBank_image(),
                                hospitalModels.get(a).getN_sac_id()
                        );
                    }
                }

            });

        }*/




        if (BaseUtils.getAddHosptitalForm(this).equals("false")) {
            Log.d("koiuou", "onCreate: " + "k,ojmklmlm");
            getAllCustomer();
        }

        if (BaseUtils.getSubmitCounsellingFormStatus(this).equals("false")) {
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
        if (BaseUtils.getProviderForm(this).equals("false")) {
            NetworkCalls.postProvider(
                    this,
                    BaseUtils.getn_st_iddProviderData(this),
                    BaseUtils.getn_dis_iddProviderData(this),
                    BaseUtils.getn_tu_iddProviderData(this),
                    BaseUtils.getn_hf_iddProviderData(this),
                    BaseUtils.gettyppProviderData(this),
                    BaseUtils.getd_visittProviderData(this),
                    BaseUtils.getn_visit_iddProviderData(this),
                    "",
                    BaseUtils.getn_sac_iddProviderData(this),
                    BaseUtils.getn_user_iddProviderData(this),
                    getSupportFragmentManager(),
                    false
            );
        }

        if (BaseUtils.getSubmitFdcDispensationOpForm(this).equals("false")) {
           /* LiveData<List<RoomFdcOpenStockBalance>> roomFdcOpenStockBalance = dataBase.customerDao().fetchFdcOpenStockData();
            roomFdcOpenStockBalance.observe(this, roomFdcDispensationPatients -> {

                if (roomFdcDispensationPatients.size() != 0) {
                    for (int a = 0; a < roomFdcDispensationPatients.size(); a++) {
                        Log.d("lplpl", "onCreate: " + roomFdcDispensationPatients.get(a).getN_lat());

                        fdcOpeningStockBalanceViewModel.submitFdcOpenStockBalance(
                                roomFdcDispensationPatients.get(a).getN_st_id(),
                                roomFdcDispensationPatients.get(a).getN_dis_id(),
                                roomFdcDispensationPatients.get(a).getN_tu_id(),
                                roomFdcDispensationPatients.get(a).getN_hf_id(),
                                roomFdcDispensationPatients.get(a).getN_fdc2(),
                                roomFdcDispensationPatients.get(a).getN_fdc3(),
                                roomFdcDispensationPatients.get(a).getN_fdc4(),
                                roomFdcDispensationPatients.get(a).getN_etham(),
                                roomFdcDispensationPatients.get(a).getN_lat(),
                                roomFdcDispensationPatients.get(a).getN_lng(),
                                roomFdcDispensationPatients.get(a).getN_staff_info(),
                                roomFdcDispensationPatients.get(a).getN_user_id(), this,
                                new GlobalProgressDialog(this), false

                        );
                    }
                }

            });*/

            fdcOpeningStockBalanceViewModel.submitFdcOpenStockBalance(
                    Integer.valueOf(BaseUtils.getFdcOPn_st_id(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_dis_id(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_tu_id(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_hf_id(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_med_id(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_uom_id(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_qty(this)),
                    BaseUtils.getFdcOPn_lat(this),
                    BaseUtils.getFdcOPn_lng(this),
                    Integer.valueOf(BaseUtils.getFdcOPn_staff_info(this)),
                    Integer.valueOf(BaseUtils.getFdcOPn_user_id(this)),
                    this,
                    new GlobalProgressDialog(this), false

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

        if (BaseUtils.getSubmitFdcReceiveForm(this).equals("false")) {
            LiveData<List<FdcReceivedModel>> fdcReceived = dataBase.customerDao().fetchFdcReceived();
            fdcReceived.observe(this, hospitalModels -> {

                if (hospitalModels.size() != 0) {
                    for (int a = 0; a < hospitalModels.size(); a++) {
                        //Log.d("lplpl", "onCreate: " + hospitalModels.get(a).getN_st_id());
                        receiveViewModel.submitFdcReceived(
                                hospitalModels.get(a).getD_rec(),
                                Integer.valueOf(hospitalModels.get(a).getN_med()),
                                Integer.valueOf(hospitalModels.get(a).getN_uom()),
                                Integer.valueOf(hospitalModels.get(a).getN_qty()),
                                Integer.valueOf(hospitalModels.get(a).getN_sanc()),
                                hospitalModels.get(a).getN_lat(),
                                hospitalModels.get(a).getN_lng(),
                                Integer.valueOf(hospitalModels.get(a).getN_staff_info()),
                                Integer.valueOf(hospitalModels.get(a).getN_user_id()), this,
                                new GlobalProgressDialog(this),
                                null, null, null, false
                        );

                    }
                }

            });

        }

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiverSelected, new IntentFilter("Selected"));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiverLocal, new IntentFilter("local"));
        init();

    }

    @Override
    public void onBackPressed() {

        if (getIntent().hasExtra("backpress")) {
            startActivity(
                    new Intent(HospitalsList.this, MainActivity.class).putExtra("backpress", "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {

        search = findViewById(R.id.search);
        backbtn = findViewById(R.id.backbtn);
        addbtn = findViewById(R.id.hl_addbtn);
        hospitalRecycler = findViewById(R.id.hospitalRecycler);
        ResidentialTU = findViewById(R.id.f1_residentialTu);
        nextbtn = findViewById(R.id.nextbtn);
        checkboxNonVisit = findViewById(R.id.checkboxNonVisit);
        LinearLayout visiit = findViewById(R.id.visiit);

        if (getIntent().hasExtra("sample")) {
            visiit.setVisibility(View.GONE);
        }


        try {
            if(BaseUtils.getUserInfo(this).getN_staff_sanc().toString().equals("5"))
            {
                addbtn.setVisibility (View.VISIBLE);
            }
        }catch (Exception e){
            Log.d("crash_data",e.toString());
        }



        //nextbtn.setEnabled(false);

        /*hospitalNameTt = findViewById(R.id.hospitalNameTitle);
        hospitalNameLocation = findViewById(R.id.locationHospital);
        doctorNameTv = findViewById(R.id.docname);
        hospitalTypeTitle = findViewById(R.id.hospitalTYpe);
        currentDate = findViewById(R.id.currentdate);
        lastVisit = findViewById(R.id.visitdays);*/
        //table = findViewById(R.id.laytable);


     /*   Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa");
        currentDate.setText(curFormater.format(currentTime));*/
        //hospitalType = findViewById(R.id.hospitalType);

        checkboxNonVisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // BaseUtils.showToast(HospitalsList.this, String.valueOf(b));
                try {
                    filter(search.getText().toString().trim());
                }catch (Exception e ){
                    Log.d("crashSearch",e.toString());
                }

                //   filter(search.getText().toString().trim(),b);

            }
        });

        setClickListners();
        nextbtn.setOnClickListener(view -> {

            BaseUtils.setHospitalName(this,hospitalName);
            BaseUtils.setDocName(this,doctorName);

            Log.d("shobhit",hospitalName+"-> "+doctorName);
            Log.d("hswtyes", "onClick: clickeddd");
            if (getIntent().hasExtra("fdc")) {
                LayoutInflater li = LayoutInflater.from(HospitalsList.this);
                View dialogView = li.inflate(R.layout.demo_layout, null);
                TextView yesbtn = dialogView.findViewById(R.id.yesbtn);
                TextView nobtn = dialogView.findViewById(R.id.nobtn);
                AlertDialog sDialog = new AlertDialog.Builder(HospitalsList.this).setView(dialogView).setCancelable(true).create();

                yesbtn.setOnClickListener(view1 -> {
                    Intent intent = new Intent(HospitalsList.this, HospitalFacility.class);
                    intent.putExtra("tu_id", tuId);
                    intent.putExtra("hf_id", hfID);
                    intent.putExtra("issued", "patient");
                    intent.putExtra("hospitalName", hospitalName);
                    intent.putExtra("docName", doctorName);
                    //   intent.putExtra("docid", hospitalLists.get(position).get());
                    startActivity(intent);
                    sDialog.dismiss();
                });
                nobtn.setOnClickListener(view1 -> {
                    Intent intent = new Intent(HospitalsList.this, HospitalFacility.class);
                    intent.putExtra("tu_id", tuId);
                    intent.putExtra("hf_id", hfID);
                    intent.putExtra("hospitalName", hospitalName);
                    intent.putExtra("issued", "hospital");
                    intent.putExtra("docName", doctorName);
                    //   intent.putExtra("docid", hospitalLists.get(position).get());
                    startActivity(intent);
                    sDialog.dismiss();
                });
                sDialog.show();
            } else {
                Intent intent = new Intent();
                intent.setAction("local");
                intent.putExtra("checked", true);
                Log.d("muyu", "Hospital: tu_id " + tuId);
                Log.d("muyu", "Hospital: hf_id " + hfID);
                intent.putExtra("tu_id", tuId);
                intent.putExtra("hf_id", hfID);
                intent.putExtra("submitbtnClicked", "submitbtnClicked");
                intent.putExtra("hf_type_id", hf_type_id);
                intent.putExtra("hospitalName", hospitalName);
                intent.putExtra("hospitaltypeName", hospitaltypeName);
                intent.putExtra("hospitallocation", hospitallocation);
                intent.putExtra("docName", doctorName);
                LocalBroadcastManager.getInstance(HospitalsList.this).sendBroadcast(intent);
            }
        });
        // tuId =BaseUtils.getUserInfo(HospitalsList.this).getnUserLevel();
   /*     NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                new Handler().postDelayed(this::getAllCustomer, 100);
            }
        });*/
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (hospitalLists.size() == 0) {
                    Log.d("crashCheck","size of list is 0 ");
                } else {
                    try {
                        filter(editable.toString());
                    }catch (Exception e)
                    {
                       Log.d("crashCheck",e.toString());
                    }

                }

            }
        });

        if (getIntent().hasExtra("counsel")) {
            addbtn.setVisibility(View.GONE);
        }

        if (tuId.equals("0")) {
            addbtn.setVisibility(View.GONE);
        }

        Log.d("yuygfu", "onResponse: " + BaseUtils.getUserInfo(HospitalsList.this).getnAccessRights() + BaseUtils.getUserInfo(HospitalsList.this).getnUserLevel());
        ResidentialTU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    tuId = "0";
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    Log.d("mosojdo", "onItemSelected: " + tu.size());

                    try {
                        tuId = tu.get(/*i - 1*/i - 1).getN_tu_id();//selecting the second value in list first value is null

                      //  isDataChanged = true;

                       // BaseUtils.showToast(HospitalsList.this,"called");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Call your function here
                                getHospitalData(HospitalsList.this, tuId, false);
                            }
                        }, 2000);



                       // setHospitalRecycler();

                    } catch (Exception e) {

                    }
                    Log.d("mosojdo", "onItemSelected: " + tuId);
                    if (BaseUtils.haveAccess(HospitalsList.this)) {
                        if (getIntent().hasExtra("counsel")) {
                            addbtn.setVisibility(View.GONE);
                        } else {
                            addbtn.setVisibility(View.VISIBLE);
                        }
                    }

//                    if (tuId.equals(BaseUtils.getSelectedTu(HospitalsList.this))) {
//                        setHospitalRecycler();
//                    }



                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Call your function here
                            NetworkCalls.getUserOtherData(HospitalsList.this, BaseUtils.getUserInfo(HospitalsList.this).getN_staff_sanc(), tuId);
                        }
                    }, 1000);



                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getTU(this);


    }

    public  void getHospitalData(Context context, String TuId, Boolean navigate) {
        //Log.d("mosojdo","API CalleEd");
    //    BaseUtils.showToast(HospitalsList.this, "Please wait while we fetch data.");
        try{
          hospitalLists.clear();
        }catch (Exception e){}

        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(HospitalsList.this);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            // progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
          //  LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));

            return;
        }
        Log.d("rerw", "onResponseIdd TU: " + TuId);
        Log.d("rerw", "onResponseIdd: Base" + BaseUtils.getUserInfo(context).getnAccessRights());
        //https://nikshayppsa.hlfppt.org/_api-v1_/_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=4&sanc=819&tu_id=20
        // String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=n_tu_id<<EQUALTO>>" + TuId;
        // String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserOtherInfo(context).getN_staff_sanc() + "&tu_id=" + TuId;
        String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserInfo(context).getN_staff_sanc() + "&tu_id=" + TuId;
        //String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=5&sanc=34&tu_id=235";
        Log.d("HospitalUrl",url.toString());
        apiInterface.getHospitalList(url).enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, @NotNull Response<HospitalResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    BaseUtils.putAddHospitalForm(context, "true");

                    if (response.body().getStatus().equals("true")) {
                        hospitalLists = response.body().getUserData();


                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Call your function here
                                setHospitalRecycler();
                            }
                        }, 1500);


                   //     progressDialog.hideProgressBar();

//                        Log.d("lpossapo", "onResponse: " + hospitalLists.size());
//                        Log.d("Hospitals Data", hospitalLists.toString());


                        BaseUtils.putSelectedTu(context, TuId);
                        BaseUtils.saveHospitalList(context, hospitalLists);
                        //    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));
                       progressDialog.hideProgressBar();
                      //  hideProgress(progressDialog);


                    } else {
//                        BaseUtils.saveHospitalList(context, hospitalLists);
                        Log.d("lpossapo", "error: " + response.body().getStatus() + response.body().getMessage());
                     //   LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
                         progressDialog.hideProgressBar();
                    }
                } else {
                    Log.d("lpossapo", "error: " + response.errorBody().toString());
                //    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
                      progressDialog.hideProgressBar();
                }

            }

            @Override
            public void onFailure(Call<HospitalResponse> call, @NotNull Throwable t) {
                 progressDialog.hideProgressBar();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
            }
        });
    }


    public void getTU(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

            return;
        }

        Log.d("ihsi", "getTU: W  " + BaseUtils.getUserInfo(context).getnAccessRights());
        Log.d("ihsi", "getTU: sanc  " + BaseUtils.getUserInfo(context).getN_staff_sanc());

        String url = "_sptu_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_tu&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserInfo(context).getN_staff_sanc();
        Log.d("ihsi", "getTU: sanc  " + url);

        ApiClient.getClient().getFormTU(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        Log.d("uhuhoy", "onResponse: " + response.body().getUserData().size());
                        BaseUtils.saveTU(context, response.body().getUserData());
                        tu = response.body().getUserData();
                        Log.d("mijop", "onReceive: " + tu.size());
                        Log.d("mijop", "onReceive: " + tu.toString());
                        for (int a = 0; a < tu.size(); a++) {

                            if (!tuStrings.contains(tu.get(a).getcTuName())) {
                                tuStrings.add(tu.get(a).getcTuName());
                            }

                        }
                        //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                        setSpinnerAdapter(ResidentialTU, tuStrings);
                        ResidentialTU.setSelection(1);

                        //   LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

                    } else {
                        BaseUtils.saveTU(context, TuList);
                        //  LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
            }
        });
    }


    private void getPrevVisits() {
        if (!BaseUtils.isNetworkAvailable(this)) {
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_visit_typ&w=n_hf_id<<EQUALTO>>" + hfID;
        ApiClient.getClient().getPrevVisits(url).enqueue(new Callback<PrevVisitsResponse>() {
            @Override
            public void onResponse(Call<PrevVisitsResponse> call, Response<PrevVisitsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        dataBase.customerDao().deletePrevVisitProviderFromRoom();
                        for (int i = 0; i < response.body().getUserData().size(); i++) {
                            RoomPrevVisitsData roomPreviousVisits = new RoomPrevVisitsData(
                                    response.body().getUserData().get(i).getdVisit(),
                                    response.body().getUserData().get(i).getcVal(),
                                    response.body().getUserData().get(i).getnHfId()

                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousVisits.getcVal());

                            dataBase.customerDao().getPrevVisitsProviderFromServer(roomPreviousVisits);
                        }
                        getRoomPrevVisits();

                    }
                }
            }

            @Override
            public void onFailure(Call<PrevVisitsResponse> call, Throwable t) {

            }
        });
    }

    private void getRoomPrevVisits() {
        LiveData<List<RoomPrevVisitsData>> roomPrevVisitProviderFromRoom = dataBase.customerDao().getSelectedRoomPrevVisitProviderFromRoom();
        roomPrevVisitProviderFromRoom.observe(HospitalsList.this, roomPreviousVisits1 -> {
            parentDataPreviousSamples = roomPreviousVisits1;

            //getting difference in days
            Date date = null;
            Date dateOne = null;
            try {
                // date = curFormater.parse(parentDataPreviousSamples.get(1).getdVisit());
                String formattedDate = "";
                if (!parentDataPreviousSamples.isEmpty()) {
                    for (int i = 0; i < parentDataPreviousSamples.get(0).getdVisit().length(); i++) {
                        if (i < 10) {
                            formattedDate = formattedDate + parentDataPreviousSamples.get(0).getdVisit().charAt(i);
                        }
                    }
                }


                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                dateOne = curFormater.parse(modifyDateLayout(formattedDate));
                date = curFormater.parse(curFormater.format(currentTime));
                long diffInMillies = Math.abs(dateOne.getTime() - date.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                String ext = "";
                if (diff < 2) {
                    ext = "Day";
                } else {
                    ext = "Days";
                }
                //lastVisit.setText(String.valueOf(diff + " " + ext));
                Log.d("bhdyigd", "init: " + curFormater.format(currentTime));
                Log.d("bhdyigd", "init: " + formattedDate);
                Log.d("bhdyigd", "init: " + diff);


            } catch (ParseException e) {
                Log.d("bhdyigd", "init: " + "failll");
                e.printStackTrace();
            }
            //setRecycler();
        });
    }

    private String modifyDateLayout(String inputDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void filter(String text) {
        ArrayList<HospitalList> temp = new ArrayList();
        if (!hospitalLists.isEmpty()) {
            for (HospitalList d : hospitalLists) {
                String value = d.getcHfNam().toLowerCase();
                if (value.contains(text.toLowerCase())) {


                    Log.d("TAG", "filter: " + d);

                    try {
                        temp.add(d);
                        if (checkboxNonVisit.isChecked()) {
                            if (d.getLst_visit() != "" && d.getLst_visit() != null) {
                                temp.remove(d);
                            }
                        }
                    }catch (Exception e){
                        Log.d("crashChecking","line number 1063"+e.toString());
                    }

                }

            }

            try {
                if (fdcHospitalsAdapter != null) {
                    fdcHospitalsAdapter.updateList(temp);
                }
            }catch (Exception e){
                Log.d("crashSearch",e.toString());
            }

        }else{
            Log.d("crashSearch","line number 1060");
        }
    }

    private void setClickListners() {
        backbtn.setOnClickListener(this);
        //   nextbtn.setOnClickListener(this);
        addbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                super.onBackPressed();
                break;
            /*    case R.id.nextbtn:



             */
            /*   if (getIntent().hasExtra("provider")) {
                    startActivity(new Intent(HospitalsList.this, ProviderEngagement.class).putExtra("hf_id", hfID));
                } else if (getIntent().hasExtra("counsel")) {
                    startActivity(new Intent(HospitalsList.this, FormTwo.class).putExtra("hospitalName", hospitalName).putExtra("hf_id", hfID).putExtra("tu_id", tuId).
                            putExtra("counsel", ""));
                } else if (getIntent().hasExtra("sample")) {
                    startActivity(new Intent(HospitalsList.this, FormTwo.class).putExtra("hf_id", hfID).putExtra("tu_id", tuId).putExtra("sample", getIntent().getStringExtra("sample")));
                } else if ( getIntent().hasExtra("reportdelivery")){
                    startActivity(new Intent(HospitalsList.this, FormTwo.class).putExtra("hf_id", hfID).putExtra("tu_id", tuId).putExtra("reportdelivery", getIntent().getStringExtra("reportdelivery")));
                }*//*
                break;*/
            case R.id.hl_addbtn:
                startActivity(new Intent(HospitalsList.this, AddHospitalFacilty.class).putExtra("tu_id", tuId));
                break;
        }
    }

    public BroadcastReceiver broadcastReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("submitbtnClicked")) {


                boolean checked = intent.getBooleanExtra("checked", false);
                if (checked) {

                    if (getIntent().hasExtra("fdc")) {
                        if (fdctype.equals("bag")) {
                            Intent intentt = new Intent(context, FdcOpeningStockBalance.class);
                            intent.putExtra("tu_id", tuId);
                            intent.putExtra("hf_id", hfID);
                            intent.putExtra("hospitalName", hospitalName);
                            intent.putExtra("docName", doctorName);
                            startActivity(intentt);
                        } else {
                            Intent intentt = new Intent(context, FdcDispensationToHf.class);
                            intentt.setAction("");
                            intentt.putExtra("tu_id", tuId);
                            intentt.putExtra("hf_id", hfID);
                            intentt.putExtra("hospitalName", hospitalName);
                            intentt.putExtra("docName", doctorName);

                            //   intent.putExtra("docid", hospitalLists.get(position).get());
                            startActivity(intentt);
                        }
                    } else {
                        if (getIntent().hasExtra("provider")) {

                            Log.d("dmkidjio", "onReceive: " + lastvisit);
                            BaseUtils.putSection(HospitalsList.this, "provider");
                            startActivity(new Intent(HospitalsList.this, HospitalFacility.class)
                                            .putExtra("hospitalName", hospitalName)
                                            .putExtra("hospitaltypeName", hospitaltypeName)
                                            .putExtra("docName", doctorName)
                                            .putExtra("hospitallocation", hospitallocation)
                                            .putExtra("hf_id", hfID)
                                            .putExtra("tu_id", tuId)
                                            .putExtra("lastvisit", lastvisit)

                                    /*    .putExtra("type", "provider")*/);
                        } else if (getIntent().hasExtra("counsel")) {
                            startActivity(new Intent(HospitalsList.this, HospitalFacility.class)
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("section", "counsel")
                                    .putExtra("hf_id", hfID).putExtra("type", "hospitald").
                                    putExtra("counsel", ""));
                        } else if (getIntent().hasExtra("stockreceiving")) {
                            startActivity(new Intent(HospitalsList.this, MedicineListActivity.class)
                                    .putExtra("tu_id", tuId)
                                    //  .putExtra("hf_id", hfID)
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("docName", doctorName)
                                    // .putExtra("hospitalName", hospitalName)
                                    .putExtra("hf_id", hfID)
                                    .putExtra("type", "hospital")
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("section", "stockreceiving")
                                    .putExtra("stockreceiving", "stockreceiving"));
                        } else if (getIntent().hasExtra("sample")) {
                            BaseUtils.putSection(HospitalsList.this, "sample");

                            startActivity(new Intent(HospitalsList.this, FormTwo.class)
                                    .putExtra("hf_type_id", hf_type_id)
                                    .putExtra("section", "sample")
                                    .putExtra("type", "sample")
                                    //.putExtra("sample", "sample")

                                    .putExtra("sample", "")
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hospitallocation", hospitallocation)
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("hf_id", hfID)
                            );
//                            startActivity(new Intent(HospitalsList.this, HospitalFacility.class)
//                                            .putExtra("hospitalName", hospitalName)
//                                            .putExtra("hf_id", hfID)
//                                            .putExtra("hospitaltypeName", hospitaltypeName)
//                                            .putExtra("hf_type_id", hf_type_id)
//                                            .putExtra("section", "sample")
//                                    /*.putExtra("type", "sample")
//                                    .putExtra("sample", "sample")*/);
                        } else if (getIntent().hasExtra("reportdelivery")) {
                            startActivity(new Intent(HospitalsList.this, FormTwo.class)
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("hf_id", hfID)
                                    .putExtra("section", "reportdelivery")
                                    .putExtra("tu_id", tuId)
                                    .putExtra("hf_type_id", hf_type_id)
                                    .putExtra("reportdelivery", "reportdelivery"));
                        }
                    }

                }
            }
        }
    };

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            if (intent.hasExtra("docName")) {
                doctorName = intent.getStringExtra("docName");
            }
            if (intent.hasExtra("checked")) {
                checked = intent.getBooleanExtra("checked", false);
                // nextbtn.setEnabled(checked);
                //table.setVisibility(View.VISIBLE);
                hfID = intent.getStringExtra("hf_id");
                hospitalName = intent.getStringExtra("hospitalName");
                hf_type_id = intent.getStringExtra("hf_type_id");
                hospitaltypeName = intent.getStringExtra("hospitaltypeName");
                hospitallocation = intent.getStringExtra("hospitallocation");
                lastvisit = intent.getStringExtra("lastvisit");
                if (intent.hasExtra("fdctype")) {
                    fdctype = intent.getStringExtra("fdctype");
                }

                /*hospitalNameTt.setText(hospitalName);
                hospitalNameLocation.setText(hospitallocation);
                doctorNameTv.setText(doctorName);
                hospitalTypeTitle.setText(hospitaltypeName);*/
                //getPrevVisits();
                // hospitalName = intent.getStringExtra("hospitalName");

            } else if (intent.hasExtra("notifyAdapter")) {

//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void run() {
//                        //Do something after 1000ms
//                        setHospitalRecycler();
//
//
//                    }
//                }, 1000);
            } else if (intent.hasExtra("localData")) {
                // setHospitalRecycler();
            }
            if (intent.hasExtra("localTU")) {


            }
        }
    };
    public BroadcastReceiver broadcastReceiverSelected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            if (intent.hasExtra("docName")) {
                doctorName = intent.getStringExtra("docName");
            }
            if (intent.hasExtra("checked")) {
                checked = intent.getBooleanExtra("checked", false);
                // nextbtn.setEnabled(checked);
                //table.setVisibility(View.VISIBLE);
                hfID = intent.getStringExtra("hf_id");
                hospitalName = intent.getStringExtra("hospitalName");
                hf_type_id = intent.getStringExtra("hf_type_id");
                hospitaltypeName = intent.getStringExtra("hospitaltypeName");
                hospitallocation = intent.getStringExtra("hospitallocation");
                lastvisit = intent.getStringExtra("lastvisit");
                if (intent.hasExtra("fdctype")) {
                    fdctype = intent.getStringExtra("fdctype");
                }

                /*hospitalNameTt.setText(hospitalName);
                hospitalNameLocation.setText(hospitallocation);
                doctorNameTv.setText(doctorName);
                hospitalTypeTitle.setText(hospitaltypeName);*/
                //getPrevVisits();
                // hospitalName = intent.getStringExtra("hospitalName");

            } /*else if (intent.hasExtra("notifyAdapter")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        //Do something after 1000ms
                        setHospitalRecycler();
                    }
                }, 1000);
            } else if (intent.hasExtra("localData")) {
                // setHospitalRecycler();
            }
            if (intent.hasExtra("localTU")) {
                Log.d("gjuy", "onReceive: njkguyg");
                tu = BaseUtils.getTU(HospitalsList.this);
                Log.d("mijop", "onReceive: " + tu.size());
                Log.d("mijop", "onReceive: " + tu.toString());
                for (int a = 0; a < tu.size(); a++) {

                    if (a > 0) {
                        if (!tuStrings.contains(tu.get(a).getcTuName())) {
                            tuStrings.add(tu.get(a).getcTuName());
                        }
                    }
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(ResidentialTU, tuStrings);
            }*/
        }
    };

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(HospitalsList.this, values);
        spinner.setAdapter(spinnerAdapter);

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
        if (broadcastReceiverLocal != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverLocal);
        }
        if (broadcastReceiverSelected != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverSelected);
        }
    }

    /*    private void setHospitalRecycler(String tuID) {
            selectedHospitalLists = new ArrayList<>();
            for (HospitalList hospitalList:hospitalLists){
                if (hospitalList.getnTuId().equals(tuID)){
                    selectedHospitalLists.add(hospitalList);
                }
            }

            if (selectedHospitalLists.size()>0){
                hfID = selectedHospitalLists.get(0).getnHfId();
                hospitalsAdapter = new HospitalsAdapter(selectedHospitalLists);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
                hospitalRecycler.setLayoutManager(linearLayoutManager);
                hospitalRecycler.setAdapter(hospitalsAdapter);
                hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
            }


        }*/


    private void setHospitalRecycler() {

//        if (isFirstTymOnThisPage) {
//            isFirstTymOnThisPage = false;
//        } else {
//            getHospitalData(this, tuId, false);
//        }

      //  BaseUtils.showToast(HospitalsList.this,"Called");
        Log.d("shobhit_hospitalList", "setHospital 1206");
       // hospitalLists = BaseUtils.getHospital(HospitalsList.this);
        if (!hospitalLists.isEmpty()) {
            hfID = hospitalLists.get(0).getnHfId();
        }
        Log.d("jiouyo", "setHospitalRecycler: " + hospitalLists.size());

        if (getIntent().hasExtra("fdc")) {
            fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, HospitalsList.this, "fdc", hfID, dataBase);
        } else if (getIntent().hasExtra("provider")) {

            try {
                fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, HospitalsList.this, "provider", hfID, dataBase);
            } catch (Exception e) {
            }
            ;


        } else {
            fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, HospitalsList.this, "koko", hfID, dataBase);

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
        hospitalRecycler.setLayoutManager(linearLayoutManager);
        hospitalRecycler.setAdapter(fdcHospitalsAdapter);
        hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
      /*  }else {
            hospitalsAdapter = new HospitalsAdapter(hospitalLists);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
            hospitalRecycler.setLayoutManager(linearLayoutManager);
            hospitalRecycler.setAdapter(hospitalsAdapter);
            hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
        }*/
    }

    private void getAllCustomer() {
        LiveData<List<HospitalModel>> allCustomer = dataBase.customerDao().fetchHospital();
        allCustomer.observe(HospitalsList.this, hospitalModels -> {
            hospitalModelList = hospitalModels;
            if (hospitalModelList.size() != 0) {
                for (int a = 0; a < hospitalModelList.size(); a++) {
                    addHospital(hospitalModelList.get(a), a);
                }
            }

        });
    }

    private void addHospital(HospitalModel hospitalModel, int pos) {

        NetworkCalls.addHospital(HospitalsList.this,
                BaseUtils.getHospitalnStId(this),
                BaseUtils.getHospitalnDisId(this),
                BaseUtils.getHospitalnTuId(this),
                hospitalModel.getN_hf_cd().toString(),
                hospitalModel.getC_hf_nam(),
                hospitalModel.getN_hf_typ_id(),
                hospitalModel.getC_hf_addr(),
                hospitalModel.getC_cont_per(),
                hospitalModel.getC_cp_mob(),
                hospitalModel.getC_cp_email(),
                hospitalModel.getN_sc_id(),
                hospitalModel.getN_pp_idenr(),
                hospitalModel.getC_tc_nam(),
                hospitalModel.getC_tc_mob(),
                hospitalModel.getN_bf_id(),
                hospitalModel.getN_pay_status(),
                BaseUtils.getUserInfo(this).getnUserLevel(),
                hospitalModel.getLat(),
                hospitalModel.getLng(), false
        );
    }

    private void showProgressBarForDuration() {
        progressDialog.showProgressBar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hideProgressBar();
            }
        }, 4000);
    }


    @Override
    public void onResume() {
        super.onResume();


        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,
                "MyApp:WakeLockTag"
        );

        // Acquire the WakeLock to keep the screen on
        wakeLock.acquire();

        // Make the screen inactive for 4 seconds
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // Handler to remove the inactive state after the specified duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (wakeLock.isHeld()) {
                    wakeLock.release(); // Release the WakeLock after 4 seconds
                }
            }
        }, 4000);

        if (isFirstTymOnThisPage) {
            isFirstTymOnThisPage = false;
        } else {
            getHospitalData(this, tuId, false);

        }

    }

}