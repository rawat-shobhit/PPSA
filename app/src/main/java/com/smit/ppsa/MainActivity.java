package com.smit.ppsa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Network.NetworkConnection;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.GetTestResponse;
import com.smit.ppsa.Response.HospitalModel;
import com.smit.ppsa.Response.PostProviderFromRoom;
import com.smit.ppsa.Response.RoomTestData;
import com.smit.ppsa.service.SendLiveLocationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView Providerengagement, Reportdelivery, Dcbtn, UploadDocument, SampleCollections,
            Conselling, usermainbtn, logOutBtn, refill, changePassBtn, stockReceive, fdcrec, fdcIssued, transfer, lpaResult;
    private TextView username, dateandtime;
    private ImageView img, img2;
    private AppDataBase dataBase;
    private List<PostProviderFromRoom> providerFromRooms = new ArrayList<>();
    private GlobalProgressDialog progressDialog;
    private List<HospitalModel> hospitalModelList = new ArrayList<>();
    FormSixViewModel mViewModel;
    FdcReceivedViewModel receiveViewModel;
    FdcDispensationToHfViewModel fdcDispensationToHfViewModel;
    FdcOpeningStockBalanceViewModel fdcOpeningStockBalanceViewModel;
    FdcDispensationToPatientViewModel fdcDispensationToPatientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = AppDataBase.getDatabase(this);
        progressDialog = new GlobalProgressDialog(this);
        mViewModel = new ViewModelProvider(this).get(FormSixViewModel.class);
        fdcDispensationToHfViewModel = new ViewModelProvider(this).get(FdcDispensationToHfViewModel.class);
        fdcOpeningStockBalanceViewModel = new ViewModelProvider(this).get(FdcOpeningStockBalanceViewModel.class);
        fdcDispensationToPatientViewModel = new ViewModelProvider(this).get(FdcDispensationToPatientViewModel.class);
        receiveViewModel = new ViewModelProvider(this).get(FdcReceivedViewModel.class);

        /*     if (BaseUtils.getAddSampleForm(this).equals("false")) {

         *//*  LiveData<List<RoomAddSample>> samples = dataBase.customerDao().fetchAddSample();
            samples.observe(this, addSamples -> {

                if (addSamples.size() != 0) {
                    for (int a = 0; a < addSamples.size(); a++) {

                    }
                }

            });*//*
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

        }*/

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

       /* if (BaseUtils.getFormOne(this).equals("false")) {
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
  /*      if (BaseUtils.getSubmitFdcReceiveForm(this).equals("false")) {
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
                               Integer.valueOf( hospitalModels.get(a).getN_sanc()),
                                hospitalModels.get(a).getN_lat(),
                                hospitalModels.get(a).getN_lng(),
                              Integer.valueOf(  hospitalModels.get(a).getN_staff_info()),
                               Integer.valueOf( hospitalModels.get(a).getN_user_id()), this,
                                new GlobalProgressDialog(this),
                                null, null, null, false
                        );

                    }
                }

            });

        }*/


        /*     if (BaseUtils.getSubmitLabReportStatus(this).equals("false")) {
         *//*LiveData<List<RoomFormSixData>> formSix = dataBase.customerDao().fetchFormSix();
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

            });*//*
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

        if (BaseUtils.getAddHosptitalForm(this).equals("false")) {
            Log.d("koiuou", "onCreate: " + "k,ojmklmlm");
            getAllHospitals();
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

        initViews();
    }

    private void getAllHospitals() {
        LiveData<List<HospitalModel>> allCustomer = dataBase.customerDao().fetchHospital();
        allCustomer.observe(MainActivity.this, hospitalModels -> {
            hospitalModelList = hospitalModels;
            if (hospitalModelList.size() != 0) {
                for (int a = 0; a < hospitalModelList.size(); a++) {
                    addHospital(hospitalModelList.get(a), a);
                }
            }

        });
    }

    private void addHospital(HospitalModel hospitalModel, int pos) {

        NetworkCalls.addHospital(MainActivity.this,
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

    private void initViews() {

        ContextCompat.startForegroundService(this, new Intent(this, SendLiveLocationService.class));
        fdcrec = findViewById(R.id.fdcrec);
        Providerengagement = findViewById(R.id.providerangagementbtn);
        Reportdelivery = findViewById(R.id.reportdeliverybtn);
        Dcbtn = findViewById(R.id.dcbtn);
        lpaResult = findViewById(R.id.lpaResult);
        transfer = findViewById(R.id.transfer);
        UploadDocument = findViewById(R.id.uploaddocument);
        refill = findViewById(R.id.refill);
        stockReceive = findViewById(R.id.stockreceive);
        SampleCollections = findViewById(R.id.samplecollection);
        usermainbtn = findViewById(R.id.usermainbtn);
        Conselling = findViewById(R.id.conselling);
        username = findViewById(R.id.username);
        fdcIssued = findViewById(R.id.fdc_issued);
        dateandtime = findViewById(R.id.dateandtime);
        img = findViewById(R.id.img);
        img2 = findViewById(R.id.img2);
        Bitmap icon = ((BitmapDrawable) img2.getDrawable()).getBitmap();
        Bitmap icon2 = ((BitmapDrawable) img.getDrawable()).getBitmap();
        img2.setImageBitmap(getRoundedCornerBitmap(icon, 30));
        img.setImageBitmap(getRoundedCornerBitmap(icon2, 30));
        logOutBtn = findViewById(R.id.logoutbtn);
        changePassBtn = findViewById(R.id.changepassbtn);
        if (BaseUtils.getUserInfo(this).getnAccessRights().equals("6")) {
            Providerengagement.setVisibility(View.GONE);
            fdcrec.setVisibility(View.GONE);
            Reportdelivery.setVisibility(View.GONE);
            lpaResult.setVisibility(View.GONE);
            transfer.setVisibility(View.GONE);
            stockReceive.setVisibility(View.GONE);
            fdcIssued.setVisibility(View.GONE);
            SampleCollections.setVisibility(View.GONE);
            findViewById(R.id.non_tele).setVisibility(View.GONE);
        }
        getAttendanceType();

        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
        dateandtime.setText(curFormater.format(currentTime));
        setOnclick();


        img.setClipToOutline(true);
        img2.setClipToOutline(true);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                new Handler().postDelayed(this::getAllCustomer, 100);
            }
        });
    }


    private void getAttendanceType() {
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");
            if (BaseUtils.getUserInfo(MainActivity.this).getC_lng_desc().equals("")) {
                username.setText(BaseUtils.getUserInfo(MainActivity.this).getcName() + "\n" + BaseUtils.getattendanceType(MainActivity.this));
            } else {
                username.setText(BaseUtils.getUserInfo(MainActivity.this).getcName() + String.format("(%s)", BaseUtils.getUserInfo(MainActivity.this).getC_lng_desc()) + "\n" + BaseUtils.getattendanceType(MainActivity.this));
            }
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_attndce&w=id<<EQUALTO>>" +
                BaseUtils.getUserInfo(this).getId();
        ApiClient.getClient().getAttendanceType(url).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    String atype = response.body().getAsJsonArray("user_data").get(0).getAsJsonObject().get("c_attend_typ").getAsString();

                    BaseUtils.putUserAttendanceType(MainActivity.this,atype);
                    if (BaseUtils.getUserInfo(MainActivity.this).getC_lng_desc().equals("")) {
                        username.setText(BaseUtils.getUserInfo(MainActivity.this).getcName() + "\n" + BaseUtils.getattendanceType(MainActivity.this));
                    } else {
                        username.setText(BaseUtils.getUserInfo(MainActivity.this).getcName() + String.format("(%s)", BaseUtils.getUserInfo(MainActivity.this).getC_lng_desc()) + "\n" + BaseUtils.getattendanceType(MainActivity.this));
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("backpress")) {
           /* startActivity(
                    new Intent(HospitalsList.this, MainActivity.class).putExtra("backpress", "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );*/
            finishAffinity();
        } else {
            finishAffinity();
        }

    }

    private void setOnclick() {
        Providerengagement.setOnClickListener(this);
        Reportdelivery.setOnClickListener(this);
        SampleCollections.setOnClickListener(this);
        Conselling.setOnClickListener(this);
        usermainbtn.setOnClickListener(this);
        Dcbtn.setOnClickListener(this);
        logOutBtn.setOnClickListener(this);
        stockReceive.setOnClickListener(this);
        changePassBtn.setOnClickListener(this);
        fdcrec.setOnClickListener(this);
        fdcIssued.setOnClickListener(this);
        lpaResult.setOnClickListener(this);
        UploadDocument.setOnClickListener(this);
        refill.setOnClickListener(this);
        transfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.providerangagementbtn:
                startActivity(new Intent(MainActivity.this, HospitalsList.class).putExtra("provider", ""));
                break;
            case R.id.samplecollection:
                startActivity(new Intent(MainActivity.this, HospitalsList.class).putExtra("sample", ""));
                break;
            case R.id.uploaddocument:
                startActivity(new Intent(MainActivity.this, TuSearchPatientList.class).putExtra("upload", ""));
                break;
            case R.id.refill:
                startActivity(new Intent(MainActivity.this, RefillPatientList.class));
                break;
            case R.id.transfer:
                startActivity(new Intent(MainActivity.this, TuSearchPatientList.class).putExtra("transfer", ""));
                break;
            case R.id.conselling:
                startActivity(new Intent(MainActivity.this, FormTwo.class)
                        .putExtra("section", "counsel")
                        .putExtra("counsel", ""));
                break;
            case R.id.stockreceive:
                startActivity(new Intent(MainActivity.this, HospitalsList.class).putExtra("stockreceiving", ""));
                break;
            case R.id.reportdeliverybtn:
                startActivity(new Intent(MainActivity.this, FormTwo.class).putExtra("report_col", "report_col"));
                // startActivity(new Intent(MainActivity.this,ReportDelivery.class));
                break;

            case R.id.dcbtn:
                startActivity(new Intent(MainActivity.this, HospitalsList.class)
                        .putExtra("fdc", "fdc"));
                // startActivity(new Intent(MainActivity.this,ReportDelivery.class));
                break;
            case R.id.logoutbtn:
               /* BaseUtils.putUserLogIn(MainActivity.this, false);
                startActivity(new Intent(MainActivity.this, LogIn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finishAffinity();*/
                LogOutFragment fragment = new LogOutFragment();
                fragment.show(getSupportFragmentManager(), "logout");
                // startActivity(new Intent(MainActivity.this,ReportDelivery.class));
                break;
            case R.id.changepassbtn:

                startActivity(new Intent(MainActivity.this, PasswordActivity.class)
                        .putExtra("type", "existingUser"));

                // startActivity(new Intent(MainActivity.this,ReportDelivery.class));
                break;
            case R.id.fdcrec:
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View dialogView = li.inflate(R.layout.demo_layout, null);
                TextView yesbtn = dialogView.findViewById(R.id.yesbtn);
                TextView nobtn = dialogView.findViewById(R.id.nobtn);
                TextView message = dialogView.findViewById(R.id.message);
                TextView heading = dialogView.findViewById(R.id.heading);
                heading.setVisibility(View.GONE);
                message.setText("Do you want to issue or receive fdc ?");
                yesbtn.setText("Issue");
                nobtn.setText("Received");
                AlertDialog sDialog = new AlertDialog.Builder(MainActivity.this).setView(dialogView).setCancelable(true).create();
                nobtn.setOnClickListener(v -> {
                    sDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, FdcForm.class).putExtra("rec", ""));
                });
                yesbtn.setOnClickListener(v -> {
                    sDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, HospitalsList.class).putExtra("fdc", "fdc"));
                });
                sDialog.show();
                break;
            case R.id.fdc_issued:
                break;
            case R.id.lpaResult:
                startActivity(new Intent(MainActivity.this, LpaPatient.class));
        }
    }

    private void getAllCustomer() {
        LiveData<List<PostProviderFromRoom>> allCustomer = dataBase.customerDao().fetchproviders();
        allCustomer.observe(MainActivity.this, hospitalModels -> {
            providerFromRooms = hospitalModels;
            if (providerFromRooms.size() != 0) {
                for (int a = 0; a < providerFromRooms.size(); a++) {
                    postProvider(providerFromRooms.get(a), a);
                }
            }

        });
    }

    private void postProvider(PostProviderFromRoom providerFromRoom, int pos) {
        if (!BaseUtils.isNetworkAvailable(MainActivity.this)) {
            return;
        }
        RequestBody n_st_id = RequestBody.create(providerFromRoom.getN_st_id(), MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(providerFromRoom.getN_dis_id(), MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(providerFromRoom.getN_tu_id(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(providerFromRoom.getN_hf_id(), MediaType.parse("text/plain"));
        RequestBody d_reg_dat = RequestBody.create(providerFromRoom.getD_reg_dat(), MediaType.parse("text/plain"));
        RequestBody n_typ = RequestBody.create(providerFromRoom.getN_typ(), MediaType.parse("text/plain"));
        RequestBody user_id = RequestBody.create(BaseUtils.getUserInfo(this).getId(), MediaType.parse("text/plain"));
        RequestBody lat = RequestBody.create(providerFromRoom.getLat(), MediaType.parse("text/plain"));
        RequestBody lng = RequestBody.create(providerFromRoom.getLng(), MediaType.parse("text/plain"));

        ApiClient.getClient().postProviderSubmit(n_st_id, n_dis_id, n_tu_id, n_hf_id, d_reg_dat, n_typ, user_id, lat, lng).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        dataBase.customerDao().deleteProvider(providerFromRoom);
                        if (pos == providerFromRooms.size() - 1) {
                            BaseUtils.showToast(MainActivity.this, "uploaded successfully");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
            }
        });
    }
}