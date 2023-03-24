package com.smit.ppsa;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModelProvider;
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
import android.text.Editable;
import android.text.TextUtils;
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

import com.google.android.gms.common.api.Api;
import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.HospitalFacilityAdapter;

import com.smit.ppsa.Adapter.SpinAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Network.NetworkConnection;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.DoctorsList;
import com.smit.ppsa.Response.QualificationList;
import com.smit.ppsa.Response.QualificationResponse;
import com.smit.ppsa.Response.RoomDoctorsList;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalFacility extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView doctorsRecycler;
    private TextView  nextbtn, hfName;
    List<DoctorsList> doctors = new ArrayList<>();
    private ImageView backbtn,addDoc;
    private AppDataBase dataBase;
    HospitalFacilityAdapter docAdapter;
    private String name = "";
    private String priority = "";
    private String qual = "select";
    private String quals = "select";
    private String phone = "";
    private String hospitalName = "";
    private String hospitallocation = "";
    private String hospitaltypeName = "";
    private String docName = "";
    private String hf_id = "";
    private String doc_id = "";
    Spinner qual2;
    private GlobalProgressDialog globalProgressDialog;
    private List<QualificationList> qualificationLists = new ArrayList<>();
    private List<QualificationList> qualificationListspe = new ArrayList<>();
    private List<RoomDoctorsList> roomDoctorsLists = new ArrayList<>();
    private List<RoomDoctorsList> selectedroomDoctorsLists = new ArrayList<>();
    List<DoctorModel> doctorModels = new ArrayList<>();




    private String type="";
    private EditText search;
    private boolean checked;
    int position = 0;
    FdcDispensationToPatientViewModel fdcDispensationToPatientViewModel;
    ProviderEngagementViewModel providerEngagementViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_facility);
        fdcDispensationToPatientViewModel = new ViewModelProvider(this).get(FdcDispensationToPatientViewModel.class);
        providerEngagementViewModel = new ViewModelProvider(this).get(ProviderEngagementViewModel.class);





        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver2, new IntentFilter("qual"));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter("doc"));
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiverOne, new IntentFilter("doctors"));

        type= getIntent().getStringExtra("type");
        if (getIntent().hasExtra("type")){
            type= getIntent().getStringExtra("type");
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

        if (BaseUtils.getSubmitCounsellingFormStatus(this).equals("false")) {

         /*   LiveData<List<RoomCounsellingData>> roomCounsellingData = dataBase.customerDao().fetchCounsellingFormData();
            roomCounsellingData.observe(this, roomCounsellingData1 -> {
  dsfdsdf
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

        try {
            Objects.requireNonNull(doctorsRecycler.findViewHolderForAdapterPosition(0)).itemView.performClick();

        }catch (Exception e){}

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
        init();
        hospitalName = getIntent().getStringExtra("hospitalName");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        dataBase = AppDataBase.getDatabase(HospitalFacility.this);
        NetworkCalls.getDocData(this, getIntent().getStringExtra("hf_id"));
        NetworkCalls.getQualDataspe(this);
        NetworkCalls.getQualData(this);
        doctorsRecycler = findViewById(R.id.hf_recycleview);
        hfName = findViewById(R.id.hfName);
        backbtn = findViewById(R.id.backbtn);
        addDoc = findViewById(R.id.addDoc);
        search = findViewById(R.id.search);
        nextbtn = findViewById(R.id.nextbtn);
        if (!BaseUtils.haveAccess(this)){
            addDoc.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra("hospitalName")){
            hfName.setText(getIntent().getStringExtra("hospitalName"));
        }
        getAllCustomer();
        setClickListeners();
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                if (doctorModels.size() != 0) {
                    for (int a = 0; a < doctorModels.size(); a++) {
                        setAddDoc(
                                doctorModels.get(a).getC_doc_nam(),
                                doctorModels.get(a).getC_regno(),
                                doctorModels.get(a).getN_qual_id(),
                                doctorModels.get(a).getN_spec_id(),
                                doctorModels.get(a).getC_mob()
                        );
                        dataBase.customerDao().deleteDoctor(doctorModels.get(a));
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
                if (roomDoctorsLists.size() == 0) {

                } else {
                    filter(editable.toString());
                }

            }
        });



        if (getIntent().hasExtra("issued")) {
            nextbtn.setOnClickListener(view -> {
                if (checked) {
                    BaseUtils.showToast(HospitalFacility.this,"1");
                    if (getIntent().getStringExtra("issued").equals("patient")) {
                        Intent intent = new Intent(HospitalFacility.this, FormTwo.class);
                        intent.putExtra("tu_id", getIntent().getStringExtra("tu_" +
                                "id"));
                        intent.putExtra("hf_id", getIntent().getStringExtra("hf_id"));
                        intent.putExtra("hospitalName", hospitalName);

                        intent.putExtra("docName", docName);
                        intent.putExtra("doc_id", doc_id);
                        intent.putExtra("issued", "patient");
                        startActivity(intent);
                        //   intent.putExtra("docid", hospitalLists.get(position).get());
                    } else {
                        BaseUtils.showToast(HospitalFacility.this,"2");

                        Intent intent = new Intent(HospitalFacility.this, FdcForm.class);
                        intent.putExtra("tu_id", getIntent().getStringExtra("tu_id"));
                        intent.putExtra("hf_id", getIntent().getStringExtra("hf_id"));
                        intent.putExtra("hospitalName", hospitalName);
                        intent.putExtra("docName", docName);
                        intent.putExtra("doc_id", doc_id);
                        intent.putExtra("issued", "hospital");
                        startActivity(intent);
                        //   intent.putExtra("docid", hospitalLists.get(position).get());
                    }
                }
            });
        } else {
            nextbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("jmuih", "onClick: " + BaseUtils.getSection(HospitalFacility.this));

                    if (selectedroomDoctorsLists.size() > 0) {


                        Log.d("insideIfCondition","checking outside if condition  ");
                        if (BaseUtils.getSection(HospitalFacility.this).equals("hospital") || BaseUtils.getSection(HospitalFacility.this).equals("hospitald")) {
                            Log.d("insideIfCondition","checking ");
                            BaseUtils.showToast(HospitalFacility.this,"3");

                            BaseUtils.savedSelectedDoctors(HospitalFacility.this, selectedroomDoctorsLists);
                            // Log.d("jko", "onClick: " + nList.get(position).getIdd());
                            startActivity(new Intent(HospitalFacility.this, FormTwo.class)
                                    .putExtra("doc_id", doc_id)
                                    .putExtra("counsel", "")
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                                    .putExtra("section", getIntent().getStringExtra("section"))
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hf_id", hf_id));

                            //  context.finish();
                        } else if (BaseUtils.getSection(HospitalFacility.this).equals("provider")) {
                            BaseUtils.showToast(HospitalFacility.this,"4");

                            BaseUtils.savedSelectedDoctors(HospitalFacility.this, selectedroomDoctorsLists);

                            startActivity(new Intent(HospitalFacility.this, ProviderEngagement.class)
                                    .putExtra("doc_id", doc_id)
                                    .putExtra("provider", "")
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hospitallocation", hospitallocation)
                                    .putExtra("hospitaltypeName", hospitaltypeName)
                                    .putExtra("docName", docName)
                                    .putExtra("lastvisit", getIntent().getStringExtra("lastvisit"))
                                    .putExtra("hf_id", hf_id)
                            );
                        } else if (BaseUtils.getSection(HospitalFacility.this).equals("sample")) {

                            BaseUtils.showToast(HospitalFacility.this,"5");

                            BaseUtils.savedSelectedDoctors(HospitalFacility.this, selectedroomDoctorsLists);


                            startActivity(new Intent(HospitalFacility.this, FormTwo.class)
                                    .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                                    .putExtra("section", "sample")
                                    .putExtra("type", "sample")
                                    //.putExtra("sample", "sample")

                                    .putExtra("doc_id", doc_id)
                                    .putExtra("sample", "")
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hospitallocation", hospitallocation)
                                    .putExtra("hospitaltypeName", getIntent().getStringExtra("hospitaltypeName"))
                                    .putExtra("docName", docName)
                                    .putExtra("lastvisit", getIntent().getStringExtra("lastvisit"))
                                    .putExtra("hf_id", hf_id)
                            );
                        } else if (BaseUtils.getSection(HospitalFacility.this).equals("addpat")) {

                            //BaseUtils.showToast(HospitalFacility.this,"6");

                            BaseUtils.savedSelectedDoctors(HospitalFacility.this, selectedroomDoctorsLists);

                            Log.d("type",type+"  to form one ");
                            startActivity(new Intent(HospitalFacility.this, FormOne.class)
                                    .putExtra("doc_id", doc_id)
                                    .putExtra("type",type)
                                    .putExtra("hf_id", hf_id));

                            finish();
                        }
                    } else {
                        BaseUtils.showToast(HospitalFacility.this, "select doctor");
                    }

                }
            });
        }

    }

    @Override
    public void onBackPressed() {

        // finish();
        if (BaseUtils.getSection(HospitalFacility.this).equals("addpat")) {
            finish();
        } else if (BaseUtils.getSection(HospitalFacility.this).equals("provider")) {
            startActivity(
                    new Intent(HospitalFacility.this, HospitalsList.class)
                            .putExtra("provider", "").putExtra("backpress", "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );

            finish();
        } else if (BaseUtils.getSection(HospitalFacility.this).equals("sample")) {
            startActivity(
                    new Intent(HospitalFacility.this, HospitalsList.class)
                            .putExtra("sample", "").putExtra("backpress", "").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            );
            finish();
        }
        //super.onBackPressed();
    }

    private void filter(String text) {
        ArrayList<RoomDoctorsList> temp = new ArrayList();
        for (RoomDoctorsList d : roomDoctorsLists) {
            String value = d.getDocname().toLowerCase();
            if (value.contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        if(temp.isEmpty()){
            for (RoomDoctorsList d : roomDoctorsLists) {
                String value = d.getMob().toLowerCase();
                if (value.contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
        }
        if(temp.isEmpty()){
            for (RoomDoctorsList d : roomDoctorsLists) {
                String value = d.getHf_id().toLowerCase();
                if (value.contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
        }
        docAdapter.updateList(temp);
    }

    private void setClickListeners() {
        addDoc.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    private boolean emptyText(String textValue, String textValue2) {
        return TextUtils.equals(textValue.toLowerCase(), textValue2.toLowerCase());
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
    public BroadcastReceiver broadcastReceiverOne = new BroadcastReceiver() {
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
            if (intent.hasExtra("checked")) {
                checked = intent.getBooleanExtra("checked", false);
            }

            if (intent.hasExtra("hospitalName")) {
                hospitalName = intent.getStringExtra("hospitalName");
            }

            if (intent.hasExtra("hospitallocation")) {
                hospitallocation = intent.getStringExtra("hospitallocation");
            }
            if (intent.hasExtra("hospitaltypeName")) {
                hospitaltypeName = intent.getStringExtra("hospitaltypeName");
            }
            if (intent.hasExtra("docName")) {
                docName = intent.getStringExtra("docName");
            }
            if (intent.hasExtra("position")) {
                position = Integer.valueOf(intent.getStringExtra("position"));
                if (!selectedroomDoctorsLists.contains(roomDoctorsLists.get(position))) {
                    for (int i = 0; i < roomDoctorsLists.size(); i++) {
                        if (i == position) {
                            selectedroomDoctorsLists.add(roomDoctorsLists.get(position));
                        }
                    }
                }else{
                    for (int i=0;i<selectedroomDoctorsLists.size();i++){
                        if (selectedroomDoctorsLists.get(i).equals(roomDoctorsLists.get(position))){
                            selectedroomDoctorsLists.remove(i);
                        }
                    }
                }
            }
            if (intent.hasExtra("hf_id")) {
                hf_id = intent.getStringExtra("hf_id");
            }

            if (intent.hasExtra("doc_id")) {
                doc_id = intent.getStringExtra("doc_id");
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
        if (broadcastReceiverOne != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiverOne);
        }
    }

    private void setHospitalRecycler(List<RoomDoctorsList> roomDoctorsLists) {
        Log.d("ijij", "setHospitalRecycler: " + BaseUtils.getSection(HospitalFacility.this));
        docAdapter = new HospitalFacilityAdapter(roomDoctorsLists, HospitalFacility.this, BaseUtils.getSection(HospitalFacility.this), providerEngagementViewModel);
        doctorsRecycler.setAdapter(docAdapter);


        Log.d("size of list ",roomDoctorsLists.size()+" ");

        /*

         if(roomDoctorsLists.size()>=0){

            Log.d("size of list ",roomDoctorsLists.size()+" ");

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (getIntent().hasExtra("issued")) {

                        BaseUtils.showToast(HospitalFacility.this, "1");
                        if (getIntent().getStringExtra("issued").equals("patient")) {
                            Intent intent = new Intent(HospitalFacility.this, FormTwo.class);
                            intent.putExtra("tu_id", getIntent().getStringExtra("tu_" +
                                    "id"));
                            intent.putExtra("hf_id", getIntent().getStringExtra("hf_id"));
                            intent.putExtra("hospitalName", hospitalName);

                            intent.putExtra("docName", docName);
                            intent.putExtra("doc_id", doc_id);
                            intent.putExtra("issued", "patient");
                            startActivity(intent);
                            //   intent.putExtra("docid", hospitalLists.get(position).get());
                        } else {
                            BaseUtils.showToast(HospitalFacility.this, "2");

                            Intent intent = new Intent(HospitalFacility.this, FdcForm.class);
                            intent.putExtra("tu_id", getIntent().getStringExtra("tu_id"));
                            intent.putExtra("hf_id", getIntent().getStringExtra("hf_id"));
                            intent.putExtra("hospitalName", hospitalName);
                            intent.putExtra("docName", docName);
                            intent.putExtra("doc_id", doc_id);
                            intent.putExtra("issued", "hospital");
                            startActivity(intent);
                            //   intent.putExtra("docid", hospitalLists.get(position).get());
                        }

                        finish();
                    }
                    else{
                        doctorsRecycler.findViewHolderForAdapterPosition(0).itemView.performClick();

                        BaseUtils.savedSelectedDoctors(HospitalFacility.this, selectedroomDoctorsLists);
                        // Log.d("jko", "onClick: " + nList.get(position).getIdd());
                        startActivity(new Intent(HospitalFacility.this, FormTwo.class)
                                .putExtra("doc_id", doc_id)
                                .putExtra("counsel", "")
                                .putExtra("hospitaltypeName", hospitaltypeName)
                                .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                                .putExtra("section", getIntent().getStringExtra("section"))
                                .putExtra("hospitalName", hospitalName)
                                .putExtra("hf_id", hf_id));

                        finish();

                    }

                }
            }, 0);

        }

         */

        /*
          if (BaseUtils.getSection(HospitalFacility.this).equals("sample")) {

            if(roomDoctorsLists.size()>=0){


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        doctorsRecycler.findViewHolderForAdapterPosition(0).itemView.performClick();

                        Log.d("checkingShobhit","checking");

                        if(BaseUtils.getSection(HospitalFacility.this).equals("sample")){

                            BaseUtils.savedSelectedDoctors(HospitalFacility.this, selectedroomDoctorsLists);


                            startActivity(new Intent(HospitalFacility.this, FormTwo.class)
                                    .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                                    .putExtra("section", "sample")
                                    .putExtra("type", "sample")
                                    //.putExtra("sample", "sample")

                                    .putExtra("doc_id", doc_id)
                                    .putExtra("sample", "")
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("hospitallocation", hospitallocation)
                                    .putExtra("hospitaltypeName", getIntent().getStringExtra("hospitaltypeName"))
                                    .putExtra("docName", docName)
                                    .putExtra("lastvisit", getIntent().getStringExtra("lastvisit"))
                                    .putExtra("hf_id", hf_id)
                            );
                        }
                    }
                }, 100);

            }

        }
         */



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

    @Override
    protected void onResume() {
        super.onResume();
        type =getIntent().getStringExtra("type");
        Log.d("type",type+"  onresume");

    }

    private boolean emptyText(EditText editText) {
        return editText.getText().toString().isEmpty();
    }




    private void addDocDialog() {
        LayoutInflater li = LayoutInflater.from(HospitalFacility.this);
        View dialogView = li.inflate(R.layout.dialog_adddoctor, null);

        Spinner qual1 = dialogView.findViewById(R.id.ad_qualification_one);
        Spinner visitPriorityspinner = dialogView.findViewById(R.id.ad_visit);

        qual2 = dialogView.findViewById(R.id.ad_qualification_two);

        EditText regNum = dialogView.findViewById(R.id.regnumpracticingdoctor);

        SpinAdapter adapter;
        adapter = new SpinAdapter(HospitalFacility.this,
                qualificationLists);

        qual1.setAdapter(adapter);

        List<String> strings = new ArrayList<>();
        strings.add("High loaded");
        strings.add("Low loaded");
        strings.add("Non-potential");

        setSpinnerAdapter(visitPriorityspinner, strings);

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
                        getQual(qualID[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        visitPriorityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    priority = "";
                    Log.d("dded", "onItemSelected: " + priority);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    priority = strings.get(i - 1);
                    Log.d("dded", "onItemSelected: " + priority);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        cancel.setOnClickListener(view -> sDialog.dismiss());

        add.setOnClickListener(view -> {
            if (emptyText(namepracticingdoctor)) {
                BaseUtils.showToast(this, "Enter doctor name");
            } else if (qual1.getSelectedItemPosition() == 0) {
                BaseUtils.showToast(this, "Select qualification");
            } else if (qual2.getSelectedItemPosition() == 0) {
                BaseUtils.showToast(this, "Select specialisation");
            } else {
                String number = "";

                if (!emptyText(ad_contact)) {
                    /*  BaseUtils.showToast(this, "Enter phone number");*/
                    if (ad_contact.getText().toString().length() < 9) {
                        BaseUtils.showToast(this, "Enter valid mobile number");
                    } else {
                        number = ad_contact.getText().toString();
                    }
                } else {
                    number = "";
                }


                String regNo = "0";
                if (emptyText(regNum)) {
                    regNo = regNum.getText().toString();
                }

                setAddDoc(namepracticingdoctor.getText().toString(),
                        regNo,
                        qualID[0],
                        qualSpeID[0],
                        number,
                        sDialog);
            }
        });

        //bookingMsg.setText(response.body().getMessage());

    }




    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(HospitalFacility.this, values);
        spinner.setAdapter(spinnerAdapter);

    }

    private void setAddDoc(String name, String regNUm, String qualId, String specId, String mobNumber, AlertDialog sDialog) {

        BaseUtils.putAdddocnHfId(HospitalFacility.this, getIntent().getStringExtra("hf_id"));
        BaseUtils.putAdddoctype(HospitalFacility.this, getIntent().getStringExtra("type"));
        BaseUtils.putAdddocHospitalName(HospitalFacility.this, hospitalName);

        NetworkCalls.setAddDoc(
                HospitalFacility.this,
                name, regNUm, qualId, specId, mobNumber, hospitalName, getIntent().getStringExtra("type"),
                getIntent().getStringExtra("hf_id"), sDialog, true
        );

    }

    private void setAddDoc(String name,
                           String regNum,
                           String qualId, String specId, String mobNumber) {
        if (!BaseUtils.isNetworkAvailable(this)) {
            return;
        }
        RequestBody hfID = RequestBody.create(getIntent().getStringExtra("hf_id"), MediaType.parse("text/plain"));
        RequestBody docName = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody qualID = RequestBody.create(qualId, MediaType.parse("text/plain"));
        RequestBody specID = RequestBody.create(specId, MediaType.parse("text/plain"));
        RequestBody mobNum = RequestBody.create(mobNumber, MediaType.parse("text/number"));
        RequestBody regnum = RequestBody.create(regNum, MediaType.parse("text/number"));


        BaseUtils.putAddDocForm(getApplicationContext(), "false");
        ApiClient.getClient().addDoc(hfID, docName, regnum, qualID, specID, mobNum).enqueue(new Callback<AddDocResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    BaseUtils.putAddDocForm(HospitalFacility.this, "true");
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        updateAdapter(name, qualId, specId, mobNumber);
                    }
                } else {
                    BaseUtils.putAddDocForm(HospitalFacility.this, "false");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {
                BaseUtils.putAddDocForm(HospitalFacility.this, "false");

            }
        });


    }

    private void getRoomDoctors() {
        LiveData<List<RoomDoctorsList>> roomDoc = dataBase.customerDao().getSelectedDoctorsFromRoom(getIntent().getStringExtra("hf_id"));
        roomDoc.observe(HospitalFacility.this, roomDoctorsLists -> {
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
        } catch (NullPointerException e) {
        }
    }
    private void getQual(String specId){
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(HospitalFacility.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            return;
        }
        String url="_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_doc_spec_qual&w=n_spec<<EQUALTO>>"+specId;
        ApiClient.getClient().getQualification(url).enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        qualificationListspe = response.body().getUserData();
                        BaseUtils.saveQualSpeList(HospitalFacility.this, qualificationListspe);
                        SpinAdapter adapter2;
                        adapter2 = new SpinAdapter(HospitalFacility.this,
                                qualificationListspe);
                        qual2.setAdapter(adapter2);
                    }
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {

            }
        });
    }

}