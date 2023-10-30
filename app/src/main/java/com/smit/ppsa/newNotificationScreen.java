package com.smit.ppsa;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.FormOneResponse;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.HospitalResponse;
import com.smit.ppsa.Response.PatientFilterDataModel;
import com.smit.ppsa.Response.PatientResponse;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.Response.RoomDoctorsList;
import com.smit.ppsa.patientNotificationDuplicacy.PatientNotificationDuplicacyResponseModel;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newNotificationScreen extends AppCompatActivity implements View.OnClickListener {

    private CardView proceedbtn;
    private ImageView backBtn;
    private LinearLayout notificationImageForm;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<FormOneData> genders = new ArrayList<>();
    private List<FormOneData> state = new ArrayList<>();
    private List<FormOneData> district = new ArrayList<>();
    private Boolean formProviderEngagement = false;
    List<String> genderStrings = new ArrayList<>();
    List<String> stateStrings = new ArrayList<>();
    List<String> distri = new ArrayList<>();
    List<String> tuStrings = new ArrayList<>();
    List<String> hospitalName = new ArrayList<>();
    List<String> doctorName = new ArrayList<>();
    List<HospitalList> hospitalLists = new ArrayList<>();
    private List<FormOneData> tu = new ArrayList<>();
    String lat = "0", lng = "0", tuId = "0", hfID = "";
    private AppDataBase dataBase;
    private String[] st_id;
    private String st_id_res = "";
    private String[] dis_id;
    private String dis_id_res = "";
    private String[] tu_id;
    private final String tu_id_res = "";
    private String type = "normal";
    private String gender;
    private ImageView patientNotificationImg, patientBankImg;
    private String imageType = "front";
    private TextView EnrollmentDate, dataOf;
    Uri CommonImageUri = null;
    private EditText tuString, EnrollHealthFacilitySector, EnrollmentFaciltyPHI, EnrollmentFaciltyHFcode, UserIDEnrollment, EnrolmentId, PatientName, Age, Weight, Height, Address,
            Taluka, Town, Ward, Landmark, Pincode, PrimaryPhoneNumber, SecondaryPhoneNumber;
    private Spinner EnrollmentFaciltyState, EnrollmentFaciltyDistrict, EnrollmentFaciltyTBU, Gender, ResidentialState, ResidentialDistrict, ResidentialTU, hospitalSpinner, spinnerDoctor;
    Uri notificationImageUri = null;
    Uri bankImageUri = null;
    int SELECT_PICTURE = 200;
    int PIC_CROP = 500;
    private static ApiClient.APIInterface apiInterface;

    String noti = "";
    String bank = "";

    static Boolean cameraPermession = false;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //get this value from the selected patient

    /*
    https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_api_fnd_nksh&w=n_nksh_id<<EQUALTO>>'2739924203'
     */
    private List<RoomDoctorsList> DoctorsLists = new ArrayList<>();

    private static final List<FormOneData> TuList = new ArrayList<>();

    String hfIdGlobal = "", doctorIdGlobal = "";

    private GlobalProgressDialog progressDialog;


    String hivFilterId = "";
    String diabeticsId = "";

    ArrayList<PatientFilterDataModel> hivFilter = new ArrayList<>();
    ArrayList<PatientFilterDataModel> diabetiesFilter = new ArrayList<>();

    private AutoCompleteTextView hivDropDown, diabetiesDropDown;

    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification_screen);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        verifyStoragePermissions(newNotificationScreen.this);
        dataOf = findViewById(R.id.dataOf);
        initViews();

        type = getIntent().getStringExtra("type");

        progressDialog = new GlobalProgressDialog(this);
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }

        callCheckApiForDuplicacy();

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiverDoc, new IntentFilter("doc"));

        try {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        } catch (Exception e) {
            Log.d("catchException", e.toString());
        }

    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(newNotificationScreen.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(newNotificationScreen.this, new String[]{permission}, requestCode);
        } else {
            cameraPermession = true;


//            Toast.makeText(FormSix.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void callCheckApiForDuplicacy() {

        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

            return;
        }

        String url = "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_api_fnd_nksh&w=n_nksh_id<<EQUALTO>>" + "";

        ApiClient.getClient().getFormTU(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                    }
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {

            }
        });


    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {

        }
    }

    private void initViews() {
        dataBase = AppDataBase.getDatabase(newNotificationScreen.this);


        dataBase = AppDataBase.getDatabase(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        proceedbtn = findViewById(R.id.bt_proceedone);
        backBtn = findViewById(R.id.backbtn);
        EnrollmentDate = findViewById(R.id.f1_enrollment);
        patientNotificationImg = findViewById(R.id.addpatientfrontimg);
        patientBankImg = findViewById(R.id.addpatientBankimg);
        tuString = findViewById(R.id.tuString);
        EnrolmentId = findViewById(R.id.f1_enrollID);
        PatientName = findViewById(R.id.f1_patientName);
        notificationImageForm = findViewById(R.id.notificationImageForm);
        Age = findViewById(R.id.f1_age);
        Gender = findViewById(R.id.f1_gender);
        Weight = findViewById(R.id.f1_weight);
        Height = findViewById(R.id.f1_height);
        Address = findViewById(R.id.f1_address);
        Taluka = findViewById(R.id.f1_taluka);
        Town = findViewById(R.id.f1_town);
        Ward = findViewById(R.id.f1_ward);
        Landmark = findViewById(R.id.f1_landmark);
        Pincode = findViewById(R.id.f1_pincode);
        ResidentialState = findViewById(R.id.f1_residentialstate);
        ResidentialDistrict = findViewById(R.id.f1_residentialdistrict);
        ResidentialTU = findViewById(R.id.f1_residentialTu);
        PrimaryPhoneNumber = findViewById(R.id.f1_primaryphonenumber);
        SecondaryPhoneNumber = findViewById(R.id.f1_secondaryphonenumber);
        SecondaryPhoneNumber = findViewById(R.id.f1_secondaryphonenumber);
        SecondaryPhoneNumber = findViewById(R.id.f1_secondaryphonenumber);
        hivDropDown = findViewById(R.id.hivAutoComplete);
        diabetiesDropDown = findViewById(R.id.diabtesAutoComplete);
        hospitalSpinner = findViewById(R.id.spinnerHospital);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);


        type = getIntent().getStringExtra("type");
        if (Objects.equals(getIntent().getStringExtra("type"), "sample")) {
            formProviderEngagement = false;
            dataOf.setVisibility(View.GONE);
            EnrollmentDate.setVisibility(View.GONE);
            Log.d("typeCheck", type);
        } else if (Objects.equals(getIntent().getStringExtra("type"), "tree")) {
            formProviderEngagement = false;
            dataOf.setVisibility(View.GONE);
            try {
                setCurrentDate();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            EnrollmentDate.setVisibility(View.GONE);
            notificationImageForm.setVisibility(View.GONE);

            Log.d("typeCheck", type);
        } else {
            formProviderEngagement = true;
            dataOf.setVisibility(View.VISIBLE);
            EnrollmentDate.setVisibility(View.VISIBLE);
            dataOf.setText("Date of Diagnosis*");
            Log.d("typeCheckElse", type);
        }


        // handle the Choose Image button to trigger
        // the image chooser function
        patientNotificationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "front";
                Log.d("dnun", "onActivityResult:" + imageType);
                /*if (notificationImageUri != null) {
                    notificationImageUri = null;

                }*/
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                verifyStoragePermissions(newNotificationScreen.this);

                if (cameraPermession) {
                    chooseImage(newNotificationScreen.this);
                }


            }
        });

        patientBankImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "back";
                Log.d("dnun", "onActivityResult:" + imageType);
                /*if (bankImageUri != null) {
                    bankImageUri = null;
                }*/
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                verifyStoragePermissions(newNotificationScreen.this);

                if (cameraPermession) {
                    chooseImage(newNotificationScreen.this);
                }
            }
        });

        setUpCalender();
        NetworkCalls.getGender(this);
        NetworkCalls.getState(this);

//        NetworkCalls.getTU(this);
//        NetworkCalls.getDistrict(this);
/*

5 2 

 */

        //spinnerSelect(st_id,state,EnrollmentFaciltyState);
        //  spinnerSelect(dis_id,district,EnrollmentFaciltyDistrict);
        //spinnerSelect(tu_id,tu,EnrollmentFaciltyTBU);
        //spinnerSelect(st_id_res, state, ResidentialState);

        ResidentialState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    st_id_res = "";
                } else {
                    st_id_res = state.get(i - 1).getId();
                    getDistrict(newNotificationScreen.this, st_id_res);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //        NetworkCalls.getDocData(this, getIntent().getStringExtra("hf_id"));
//
//        spinnerDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//
//                } else{
//
//                    doctorIdGlobal=hospitalLists.get(position-1).toString();
//
//
//                    Log.d("shobhit id",hfIdGlobal+" -> "+doctorIdGlobal);
//                    try {
//                        NetworkCalls.getDocData(newNotificationScreen.this,(position-1)+"");
//
//                    }catch (Exception e){}
//
//                }
//            }
//        });

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    doctorIdGlobal = DoctorsLists.get(i - 1).getIdd();

                    Log.d("shobhit id ", doctorIdGlobal + "->" + hfIdGlobal);


                    try {

                    } catch (Exception e) {

                    }

//                    if (tuId.equals(BaseUtils.getSelectedTu(HospitalsList.this))) {
//                        setHospitalRecycler();
//                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        hospitalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    Log.d("mosojdo", "onItemSelected: " + tu.size());

                    //  Toast.makeText(newNotificationScreen.this, hospitalLists.get(i-1).getnHfId().toString(), Toast.LENGTH_SHORT).show();

                    hfIdGlobal = hospitalLists.get(i - 1).getnHfId();

                    try {
                        NetworkCalls.getDocData(newNotificationScreen.this, hospitalLists.get(i - 1).getnHfId());
                    } catch (Exception e) {

                    }

//                    if (tuId.equals(BaseUtils.getSelectedTu(HospitalsList.this))) {
//                        setHospitalRecycler();
//                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ResidentialTU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    tuId = "0";
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    Log.d("mosojdo", "onItemSelected: " + tu.size());


                    hospitalName.clear();
                    doctorName.clear();


                    hospitalSpinner.setSelection(0);
                    spinnerDoctor.setSelection(0);

                    try {
                        tuId = tu.get(/*i - 1*/i - 1).getN_tu_id();//selecting the second value in list first value is null

                        //  isDataChanged = true;

                        // BaseUtils.showToast(HospitalsList.this,"called");
                        getHospitalData(newNotificationScreen.this, tuId, false);

                        // setHospitalRecycler();

                    } catch (Exception e) {

                    }
                    Log.d("mosojdo", "onItemSelected: " + tuId);
                    if (BaseUtils.haveAccess(newNotificationScreen.this)) {
                        if (getIntent().hasExtra("counsel")) {
                            //     addbtn.setVisibility(View.GONE);
                        } else {
                            //    addbtn.setVisibility(View.VISIBLE);
                        }
                    }

//                    if (tuId.equals(BaseUtils.getSelectedTu(HospitalsList.this))) {
//                        setHospitalRecycler();
//                    }

                    NetworkCalls.getUserOtherData(newNotificationScreen.this, BaseUtils.getUserInfo(newNotificationScreen.this).getN_staff_sanc(), tuId);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getTU(newNotificationScreen.this);


        ResidentialDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    dis_id_res = "";
                } else {
                    dis_id_res = district.get(i - 1).getId();
                    getTU(newNotificationScreen.this, st_id_res, dis_id_res);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    gender = "";
                } else {
                    gender = genders.get(i - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setOnclick();

        updateSpinner();

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


    private void updateSpinner() {

        hivFilter.add(new PatientFilterDataModel(1, "Reactive"));
        hivFilter.add(new PatientFilterDataModel(2, "Non-Reactive"));
        hivFilter.add(new PatientFilterDataModel(3, "Positive"));
        hivFilter.add(new PatientFilterDataModel(4, "Negative"));
        hivFilter.add(new PatientFilterDataModel(5, "Test Not Done"));


        diabetiesFilter.add(new PatientFilterDataModel(1, "Non-Diabetics"));
        diabetiesFilter.add(new PatientFilterDataModel(2, "Diabetics"));
        diabetiesFilter.add(new PatientFilterDataModel(3, "Test Not Done"));

        newFilterDropDown hivAdaptee = new newFilterDropDown(this, hivFilter);
        hivDropDown.setAdapter(hivAdaptee);

        hivDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hivFilterId = (position + 1) + "";


            }
        });

        newFilterDropDown DiabeticAdaptee = new newFilterDropDown(this, diabetiesFilter);
        diabetiesDropDown.setAdapter(DiabeticAdaptee);

        diabetiesDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                diabeticsId = (position + 1) + "";

            }
        });


    }


    private void fillDetailForEdit() {
        //progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(newNotificationScreen.this)) {
            Toast.makeText(newNotificationScreen.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        // https://nikshayppsa.hlfppt.org/_api-v1_/
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=id<<EQUALTO>>" + getIntent().getStringExtra("pateintId");
        ApiClient.getClient().getPateintDetail(url).enqueue(new Callback<PatientResponse>() {
            @Override
            public void onResponse(Call<PatientResponse> call, Response<PatientResponse> response) {
                if (response.isSuccessful()) {

                    try {
                        RegisterParentData model = response.body().getUserData().get(0);

                        //HospitalList model = response.body().getUserData().get(0);

                        //  BaseUtils.showToast(FormOne.this, model.getcTyp());

                        EnrollmentDate.setText(model.getdRegDat());
                        EnrolmentId.setText(model.getnNkshId());
                        PatientName.setText(model.getcPatNam());
                        Age.setText(model.getnAge());
                        PrimaryPhoneNumber.setText(model.getC_mob());
                        Weight.setText(model.getnWght());
                        Address.setText(model.getcAdd());
                        Height.setText(model.getnHght());
                        Taluka.setText(model.getcTaluka());
                        Town.setText(model.getcTown());
                        Ward.setText(model.getcWard());
                        Landmark.setText(model.getcLndMrk());
                        Pincode.setText(model.getnPin());
                        SecondaryPhoneNumber.setText(model.getC_mob_2());

                        Glide.with(getBaseContext()).load(model.getNotf_img()).into(patientNotificationImg);
                        Glide.with(getBaseContext()).load(model.getBnk_img()).into(patientBankImg);


                        for (int i = 0; i < genderStrings.size(); i++) {

                            Log.d("genderSelection", genderStrings.get(i) + " " + model.getcTyp());


                            if (genders.get(i).getC_val().equals(model.getcTyp())) {
                                Gender.setSelection(i + 1);
                                break;
                            } else {
                                Log.d("genderSelection", genderStrings.get(i) + " " + model.getcTyp().toLowerCase());
                            }
                        }

                        for (int i = 0; i < stateStrings.size(); i++) {
                            if (state.get(i).getId().equals(model.getnStId())) {
                                ResidentialState.setSelection(i + 1);
                                break;
                            } else {
                            }
                        }

                        for (int i = 0; i < distri.size(); i++) {
                            if (district.get(i).getId().equals(model.getnDisId())) {
                                ResidentialDistrict.setSelection(i + 1);
                                break;
                            } else {
                            }
                        }

                        for (int i = 0; i < tuStrings.size(); i++) {
                            if (tu.get(i).getId().equals(model.getnTuId())) {
                                ResidentialTU.setSelection(i + 1);
                                break;
                            } else {
                            }
                        }

                    } catch (Exception e) {

                    }

                    //  parentDataTestReportResults = response.body().getUser_data();

//
//                        for (int i = 0; i < hfTypeLIsts.size(); i++) {
//
//                            Log.d("checking loop ", hfTypeLIsts.get(i).getId().toString() + "  " + model.getnHfTypId().toLowerCase());
//                            if (hfTypeLIsts.get(i).getId().toString().toLowerCase().equals(model.getnHfTypId().toLowerCase())) {
//
//                                hf_type_spinner.setSelection(i + 1);
//                                break;
//                            }
//                        }


                }
            }


            @Override
            public void onFailure(Call<PatientResponse> call, Throwable t) {
            }
        });


    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);

                    verifyStoragePermissions(newNotificationScreen.this);

                    takeImageFromCameraUri();

                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    imageChooser();
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        verifyStoragePermissions(newNotificationScreen.this);

        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), SELECT_PICTURE);
        //   CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FormSix.this);
    }

    private void spinnerSelect(final String[] type, List<FormOneData> formOneData, Spinner spinner) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    type[0] = formOneData.get(i - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setOnclick() {
        proceedbtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_proceedone:
                Log.d("shobhit__", "button click");
                Toast.makeText(this, "Please wait from is submitting", Toast.LENGTH_SHORT).show();
                if (isValidate()) {
                    callCheckTheDuplicay();
//                    sendForm();
                }

                break;

            case R.id.backbtn:
                super.onBackPressed();
                break;

        }
    }

    private boolean emptyText(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    //Objects.equals(getIntent().getStringExtra("type"), "tree")
    private boolean isValidate() {
        if (EnrollmentDate.getText().toString().isEmpty()) {
            BaseUtils.showToast(this, "Enter Enrollment date");
            return false;
        } else if (emptyText(EnrolmentId)) {
            BaseUtils.showToast(this, "Enter Enrollment ID");
            return false;
        } else if (emptyText(PatientName)) {
            BaseUtils.showToast(this, "Enter patient name");
            return false;
        }

        /*
        //        else if (notificationImageUri == null && !Objects.equals(getIntent().getStringExtra("type"), "tree")) {
//            BaseUtils.showToast(this, "Select notification form image");
//            return false;
//        }
         */


        else if (emptyText(Age)) {
            BaseUtils.showToast(this, "Enter age");
            return false;
        } else if (Integer.parseInt(Age.getText().toString()) < 1 || Integer.parseInt(Age.getText().toString()) > 100) {
            BaseUtils.showToast(this, "Enter age between 0 to 100");
            return false;
        } else if (TextUtils.isEmpty(gender)) {
            BaseUtils.showToast(this, "Select gender");
            return false;
        } else if (emptyText(PrimaryPhoneNumber)) {
            BaseUtils.showToast(this, "Enter primary phone number");
            return false;
        }
        return true;
    }

    /*private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localGender")) {
                genders = BaseUtils.getGender(FormOne.this);
                //for (int a = 0;a<genders.size(),)
            }
        }
    };*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE/*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                // Get the url of the image from data
                // Uri selectedImageUri = data.getData();
                /*if (null != selectedImageUri) {*/
                // update the preview image in the layout
                Log.d("dnun", "onActivityResult:" + imageType);

                if (imageType.equals("front")) {
                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                    /*   if (frontselectedImageUri != null) {*/
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                 /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }*/
                    notificationImageUri = data.getData();
                    patientNotificationImg.setImageURI(notificationImageUri);


                    try {
                        startCrop(notificationImageUri);
                    } catch (Exception e) {
                        Log.d("crash", e.toString());
                    }


                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);
                } else {
                    // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //backselectedImageUri = data.getData();                                                         // Get the image file URI
                    //   performCrop(backselectedImageUri);
                    //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                    //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                    //  testReportBackImg.setImageURI(backselectedImageUri);
                    bankImageUri = data.getData();
                    patientBankImg.setImageURI(bankImageUri);

                    try {
                        startCrop(bankImageUri);
                    } catch (Exception e) {

                    }

                    //testReportBackImg.setImageURI(backselectedImageUri);
                }

                // }
            } else if (requestCode == 0/*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                // Get the url of the image from data
                // Uri selectedImageUri = data.getData();
                /*if (null != selectedImageUri) {*/
                // update the preview image in the layout
                Log.d("dnun", "onActivityResult:" + imageType);

                if (imageType.equals("front")) {
                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                    /*   if (frontselectedImageUri != null) {*/
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                 /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }
                    */
//                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                    notificationImageUri = getImageUri(getApplicationContext(), selectedImage);
//                    patientNotificationImg.setImageURI(notificationImageUri);
//                    startCrop(notificationImageUri);
//
//                    notificationImageUri= CommonImageUri;
//                    patientNotificationImg.setImageURI(notificationImageUri);
//                    try {
//                        startCrop(notificationImageUri);
//                    }catch (Exception e){
//                        Log.e("CROP_IMAGE", e.getMessage());
//
//                    }


                    notificationImageUri = CommonImageUri;
                    patientNotificationImg.setImageURI(notificationImageUri);
                    try {
                        startCrop(notificationImageUri);
                    } catch (Exception e) {
                        Log.e("CROP_IMAGE", e.getMessage());

                    }

                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);
                } else {
                    // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //backselectedImageUri = data.getData();                                                         // Get the image file URI
                    //   performCrop(backselectedImageUri);
                    //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                    //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                    //  testReportBackImg.setImageURI(backselectedImageUri);
//                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                    bankImageUri = getImageUri(getApplicationContext(), selectedImage);
//                    patientBankImg.setImageURI(bankImageUri);
//                    startCrop(bankImageUri);

                    bankImageUri = CommonImageUri;
                    patientBankImg.setImageURI(bankImageUri);


                    try {
                        startCrop(bankImageUri);
                    } catch (Exception e) {
                        Log.e("CROP_IMAGE", e.getMessage());

                    }

                    //testReportBackImg.setImageURI(backselectedImageUri);
                }

                // }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (data != null) {
                    Uri uri = UCrop.getOutput(data);

                    if (imageType.equals("front")) {


                        Log.d("chekingData", "1stClick");
                        //CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        //   frontselectedImageUri = data.getData();                  // Get the image file URI
                        //   frontselectedImageUri = result.getUri();                 // Get the image file URI
                        /*   if (frontselectedImageUri != null) {*/
                        //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                        // testReportFrontImg.setImageURI(frontselectedImageUri);
                         /*   } else {
                                testReportFrontImg.setImageBitmap(result.getBitmap());
                            }*/
                        // get the returned data
                        notificationImageUri = uri;
                        patientNotificationImg.setImageURI(notificationImageUri);
                        noti = new Imagee().getEncodedImage(notificationImageUri, newNotificationScreen.this);
                    } else {
                /*        // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        backselectedImageUri = data.getData();                                                         // Get the image file URI

                        //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                        //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                        testReportBackImg.setImageURI(backselectedImageUri);*/

                        //testReportBackImg.setImageURI(selectedImageUri);
                        // get the returned data
                        Log.d("chekingData", "2stClick");
                        bankImageUri = uri;
                        patientBankImg.setImageURI(bankImageUri);
                        patientNotificationImg.setImageURI(notificationImageUri);
                        bank = new Imagee().getEncodedImage(bankImageUri, newNotificationScreen.this);
                    }

                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCrop(Uri uri) {
        String destinationFileName = "";
        destinationFileName +=System.currentTimeMillis()+".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        /*
            //uCrop.withAspectRatio(1, 1);
            //uCrop.withAspectRatio(3, 4);
            //uCrop.withAspectRatio();
            //uCrop.withAspectRatio(16, 9);
         */


        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getUcropOptions());
        uCrop.start(newNotificationScreen.this);
    }

    /*

     */
    private UCrop.Options getUcropOptions() {
        UCrop.Options options = new UCrop.Options();

         options.setCompressionQuality(100);

        //        compress type
        //        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        /*
        //colors
        options.setStatusBarColor(getResources().getColor(R.color.black));
        options.setToolbarColor(getResources().getColor(R.color.black));

        options.setToolbarTitle("Crop image");*/

        return options;

    }



    private void setHospitalRecycler() {


        setSpinnerAdapter(hospitalSpinner, hospitalName);
        //    ResidentialTU.setSelection(1);


//        if (isFirstTymOnThisPage) {
//            isFirstTymOnThisPage = false;
//        } else {
//            getHospitalData(this, tuId, false);
//        }

        //  BaseUtils.showToast(HospitalsList.this,"Called");

//        Log.d("shobhit_hospitalList", "setHospital 1206");
//        // hospitalLists = BaseUtils.getHospital(HospitalsList.this);
//        if (!hospitalLists.isEmpty()) {
//            hfID = hospitalLists.get(0).getnHfId();
//        }
//        Log.d("jiouyo", "setHospitalRecycler: " + hospitalLists.size());
//
//        if (getIntent().hasExtra("fdc")) {
//            fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, newNotificationScreen.this, "fdc", hfID, dataBase);
//        } else if (getIntent().hasExtra("provider")) {
//
//            try {
//                fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, newNotificationScreen.this, "provider", hfID, dataBase);
//            } catch (Exception e) {
//            }
//            ;
//
//
//        } else {
//            fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, newNotificationScreen.this, "koko", hfID, dataBase);
//
//        }


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(newNotificationScreen.this, LinearLayoutManager.VERTICAL, false);
//        hospitalRecycler.setLayoutManager(linearLayoutManager);
//        hospitalRecycler.setAdapter(fdcHospitalsAdapter);
//        hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
      /*  }else {
            hospitalsAdapter = new HospitalsAdapter(hospitalLists);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
            hospitalRecycler.setLayoutManager(linearLayoutManager);
            hospitalRecycler.setAdapter(hospitalsAdapter);
            hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
        }*/
    }


    public void getHospitalData(Context context, String TuId, Boolean navigate) {
        //Log.d("mosojdo","API CalleEd");
        //    BaseUtils.showToast(HospitalsList.this, "Please wait while we fetch data.");
        try {
            hospitalLists.clear();
            setHospitalRecycler();
        } catch (Exception e) {
        }
        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(newNotificationScreen.this);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            // progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            //  LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));

            return;
        }
        Log.d("rerw", "onResponseIdd TU: " + TuId);
        Log.d("rerw", "onResponseIdd: Base" + BaseUtils.getUserInfo(context).getnAccessRights());

        // String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=n_tu_id<<EQUALTO>>" + TuId;
        // String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserOtherInfo(context).getN_staff_sanc() + "&tu_id=" + TuId;
        String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserInfo(context).getN_staff_sanc() + "&tu_id=" + TuId;
        //String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=5&sanc=34&tu_id=235";

        apiInterface.getHospitalList(url).enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, @NotNull Response<HospitalResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    BaseUtils.putAddHospitalForm(context, "true");

                    if (response.body().getStatus().equals("true")) {
                        hospitalLists = response.body().getUserData();

                        hospitalName.clear();
                        for (int i = 0; i < hospitalLists.size(); i++) {

                            hospitalName.add(hospitalLists.get(i).getcHfNam());

                        }

                        setHospitalRecycler();
                        //     progressDialog.hideProgressBar();

                        Log.d("lpossapo", "onResponse: " + hospitalLists.size());
                        Log.d("Hospitals Data", hospitalLists.toString());
                        BaseUtils.putSelectedTu(context, TuId);
                        BaseUtils.saveHospitalList(context, hospitalLists);
                        //    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));
                        progressDialog.hideProgressBar();
                        //  hideProgress(progressDialog);


                    } else {
                        BaseUtils.saveHospitalList(context, hospitalLists);
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


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra("localGender")) {
                genders = BaseUtils.getGender(context);
                for (FormOneData gender : genders) {
                    genderStrings.add(gender.getcTyp());
                }

                setSpinnerAdapter(Gender, genderStrings);


            } else if (intent.hasExtra("notifyGender")) {
                genders = BaseUtils.getGender(newNotificationScreen.this);
                for (FormOneData gender : genders) {
                    genderStrings.add(gender.getcTyp());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setSpinnerAdapter(Gender, genderStrings);
                    }
                }, 500);
            }

            if (intent.hasExtra("localDistrict")) {
                district = BaseUtils.getDistrict(newNotificationScreen.this);
                if (distri.size() > 0) {
                    distri.clear();
                }

                for (FormOneData district : district) {
                    distri.add(district.getcDisNam());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(ResidentialDistrict, distri);
            }
            if (intent.hasExtra("localTU")) {
                tu = BaseUtils.getTU(newNotificationScreen.this);
                if (tuStrings.size() > 0) {
                    tuStrings.clear();
                }
                for (int a = 0; a < tu.size(); a++) {
                    if (a > 0) {
                        if (!tuStrings.contains(tu.get(a).getcTuName())) {
                            tuStrings.add(tu.get(a).getcTuName());
                        }
                    }
                }
                /*for (FormOneData tu : tu) {
                    tuStrings.add(tu.getcTuName());
                }*/
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(ResidentialTU, tuStrings);
            }
            if (intent.hasExtra("localState")) {
                state = BaseUtils.getState(newNotificationScreen.this);
                for (FormOneData stat : state) {
                    stateStrings.add(stat.getcStNam());
                }
                setSpinnerAdapter(ResidentialState, stateStrings);
            }


        }
    };


    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(newNotificationScreen.this, values);
        spinner.setAdapter(spinnerAdapter);

        if (genderStrings.isEmpty()) {

        } else {
            if (getIntent().hasExtra("pateintId")) {
                ResidentialTU.setVisibility(View.VISIBLE);
                fillDetailForEdit();

            }
        }

    }


    private void sendForm() {


        if (getIntent().hasExtra("pateintId")) {
            String url = "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll&w=id<<EQUALTO>>" + getIntent().getStringExtra("pateintId");
            String noti = "";
            String bank = "";

            Log.d("checking", getIntent().getStringExtra("pateintId"));
            Log.d("finalUrl", url);


            if (notificationImageUri != null) {
                noti = new Imagee().getEncodedImage(notificationImageUri, newNotificationScreen.this);
            }
            if (bankImageUri != null) {
                bank = new Imagee().getEncodedImage(bankImageUri, newNotificationScreen.this);
            }
            RequestBody d_reg_dat = RequestBody.create("d_reg_dat", MediaType.parse("text/plain"));
            RequestBody n_nksh_id = RequestBody.create("n_nksh_id", MediaType.parse("text/plain"));
            RequestBody c_pat_nam = RequestBody.create("c_pat_nam", MediaType.parse("text/plain"));
            RequestBody n_age = RequestBody.create("n_age", MediaType.parse("text/plain"));
            RequestBody n_sex = RequestBody.create("n_sex", MediaType.parse("text/plain"));
            RequestBody n_wght = RequestBody.create("n_wght", MediaType.parse("text/plain"));
            RequestBody n_hght = RequestBody.create("n_hght", MediaType.parse("text/plain"));
            RequestBody c_add = RequestBody.create("c_add", MediaType.parse("text/plain"));
            RequestBody c_taluka = RequestBody.create("c_taluka", MediaType.parse("text/plain"));
            RequestBody c_town = RequestBody.create("c_town", MediaType.parse("text/plain"));
            RequestBody c_ward = RequestBody.create("c_ward", MediaType.parse("text/plain"));
            RequestBody c_lnd_mrk = RequestBody.create("c_lnd_mrk", MediaType.parse("text/plain"));
            RequestBody n_pin = RequestBody.create("n_pin", MediaType.parse("text/plain"));
            RequestBody n_st_id_res = RequestBody.create("n_st_id_res", MediaType.parse("text/plain"));
            RequestBody n_dis_id_res = RequestBody.create("n_dis_id_res", MediaType.parse("text/plain"));
            RequestBody n_tu_id_res = RequestBody.create("n_tu_id_res", MediaType.parse("text/plain"));
            RequestBody c_mob = RequestBody.create("c_mob", MediaType.parse("text/plain"));
            RequestBody c_mob_2 = RequestBody.create("c_mob_2", MediaType.parse("text/plain"));
            RequestBody c_not_img = RequestBody.create("c_not_img", MediaType.parse("text/plain"));
            RequestBody c_bnk_img = RequestBody.create("c_bnk_img", MediaType.parse("text/plain"));
            RequestBody d_diag_dt = RequestBody.create("d_diag_dt", MediaType.parse("text/plain"));
            RequestBody n_cfrm = RequestBody.create("n_cfrm", MediaType.parse("text/plain"));
            ApiClient.getClient().updateHospital(url, d_reg_dat, n_nksh_id,
                    c_pat_nam, n_age,
                    n_sex, n_wght,
                    n_hght,
                    c_add,
                    c_taluka,
                    c_town,
                    c_ward, c_lnd_mrk,
                    n_pin,
                    n_st_id_res, n_dis_id_res,
                    n_tu_id_res, c_mob,
                    c_mob_2, c_not_img,
                    c_bnk_img, d_diag_dt, n_cfrm).enqueue(new Callback<AddDocResponse>() {
                @Override
                public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                    if (response.isSuccessful()) {
                        //  parentDataTestReportResults = response.body().getUser_data();
                        Log.d("responseSuccessfull", "done here is code ");
//                        startActivity(new Intent(newNotificationScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }

                }

                @Override
                public void onFailure(Call<AddDocResponse> call, Throwable t) {
                    // startActivity(new Intent(FormOne.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    Log.d("infalure", t.toString());

                }
            });


        } else {
            validMobile(PrimaryPhoneNumber.getText().toString());

        }


    }

    private void validMobile(String mob) {
        //progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(newNotificationScreen.this)) {
            Toast.makeText(newNotificationScreen.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        BaseUtils.putPatientName(newNotificationScreen.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        //   Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormOne.this).getnUserLevel());


//
//        Log.d("imageUriNotification",notificationImageUri.toString());
//        Log.d("imageUriBank",bankImageUri.toString());


//        Log.d("checkingDataImage__sho", notificationImageUri.toString() + "--" + bankImageUri.toString());


        if (notificationImageUri != null) {
            noti = new Imagee().getEncodedImage(notificationImageUri, newNotificationScreen.this);
        }
        if (bankImageUri != null) {
            bank = new Imagee().getEncodedImage(bankImageUri, newNotificationScreen.this);
        }

        if (formProviderEngagement) {
            Log.d("checkSendFomr", "from provider engagement");


            Log.d("checkingDataForLayout", "uid " + BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnStId() +
                    "-2->" + BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnDisId() + "-3-> " + BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnTuId() +
                    "-4->" + hfIdGlobal + "-5->" + doctorIdGlobal + "-6->" + EnrollmentDate.getText().toString() + "-7->" + EnrolmentId.getText().toString() +
                    "-8->" + PatientName.getText().toString() + "-9->" + Age.getText().toString() + "-10->" + genders.get(Gender.getSelectedItemPosition() - 1).getId() +
                    "-11->" + Weight.getText().toString());


            Log.d("checkingDataImage", noti + "   -----    " + bank);


            NetworkCalls.sendFormNewNotification(
                    newNotificationScreen.this,
                    BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnStId(),
                    BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnDisId(),
                    BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnTuId(),
                    hfIdGlobal,
                    doctorIdGlobal,
                    EnrollmentDate.getText().toString(),
                    EnrolmentId.getText().toString(),
                    PatientName.getText().toString(),
                    Age.getText().toString(),
                    genders.get(Gender.getSelectedItemPosition() - 1).getId(),
                    Weight.getText().toString(),
                    Height.getText().toString(),
                    Address.getText().toString(),
                    Taluka.getText().toString(),
                    Town.getText().toString(),
                    Ward.getText().toString(),
                    Landmark.getText().toString(),
                    Pincode.getText().toString(),
                    st_id_res,
                    dis_id_res,
                    tuId,
                    PrimaryPhoneNumber.getText().toString(),
                    SecondaryPhoneNumber.getText().toString(),
                    lat, lng,
                    BaseUtils.getUserInfo(newNotificationScreen.this).getId(),
                    type,
                    true,
                    noti,
                    bank,
                    BaseUtils.getUserInfo(newNotificationScreen.this).getN_staff_sanc(),
                    EnrollmentDate.getText().toString(),
                    "1", true, hivFilterId, diabeticsId

            );
        } else {

            Log.d("checkSendFomr", "aagaya");
            NetworkCalls.sendFormNewNotification(
                    newNotificationScreen.this,
                    BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnStId(),
                    BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnDisId(),
                    BaseUtils.getUserOtherInfo(newNotificationScreen.this).getnTuId(),
                    getIntent().getStringExtra("hf_id"),
                    getIntent().getStringExtra("doc_id"),
                    EnrollmentDate.getText().toString(),
                    EnrolmentId.getText().toString(),
                    PatientName.getText().toString(),
                    Age.getText().toString(),
                    genders.get(Gender.getSelectedItemPosition() - 1).getId(),
                    Weight.getText().toString(),
                    Height.getText().toString(),
                    Address.getText().toString(),
                    Taluka.getText().toString(),
                    Town.getText().toString(),
                    Ward.getText().toString(),
                    Landmark.getText().toString(),
                    Pincode.getText().toString(),
                    st_id_res,
                    dis_id_res,
                    tuString.getText().toString(),
                    PrimaryPhoneNumber.getText().toString(),
                    SecondaryPhoneNumber.getText().toString(),
                    lat, lng,
                    BaseUtils.getUserInfo(newNotificationScreen.this).getId(),
                    type,
                    true,
                    noti,
                    bank,
                    BaseUtils.getUserInfo(newNotificationScreen.this).getN_staff_sanc(),
                    EnrollmentDate.getText().toString(),
                    "0", false, hivFilterId, diabeticsId

            );
        }

//        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enrol&w=n_tu_id<<GT>>0<<AND>><<SBRK>>c_mob<<SLIKE>>" + mob + "<<ELIKE>><<OR>>c_mob_2<<SLIKE>>" + mob + "<<ELIKE>><<EBRK>>";
//        ApiClient.getClient().getMedicine(url).enqueue(new Callback<MedicineResponse>() {
//            @Override
//            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getStatus().equals("false")) {
//                        //  parentDataTestReportResults = response.body().getUser_data();
//
//                    } else {
//                        BaseUtils.showToast(FormOne.this, "Patient Already Registered with Programme");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MedicineResponse> call, Throwable t) {
//            }
//        });


    }


    private void callCheckTheDuplicay() {


        if (!BaseUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            return;
        }


        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_api_fnd_nksh&w=n_nksh_id<<EQUALTO>>" + "'" + EnrolmentId.getText().toString() + "'";
//_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_api_fnd_nksh&w=n_nksh_id<<EQUALTO>>'25865'
        Log.d("checking_sho", url);
        ApiClient.getClient().getNotificationDuplicacy(url).enqueue(new Callback<PatientNotificationDuplicacyResponseModel>() {
            @Override
            public void onResponse(Call<PatientNotificationDuplicacyResponseModel> call, Response<PatientNotificationDuplicacyResponseModel> response) {


                if (response.isSuccessful()) {

                    Log.d("checking_ohs", response.body().getStatus().toString());
                    Log.d("checking_ohs", "kasjdhfaslkdjfh");

                    if (!response.body().getStatus()) {
                        sendForm();
//                        Toast.makeText(FormOne.this, "false", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(newNotificationScreen.this, "Paltient Already Registered with the Programme", Toast.LENGTH_SHORT).show();
                    }


                }


            }

            @Override
            public void onFailure(Call<PatientNotificationDuplicacyResponseModel> call, Throwable t) {

                Log.d("checking__", t.toString());

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

                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(newNotificationScreen.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                cameraPermession = true;
            } else {
                Toast.makeText(newNotificationScreen.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(newNotificationScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(newNotificationScreen.this)
                            .setMessage("Please Grant Permission first")
                            .setCancelable(false)
                            .setPositiveButton("GO TO SETTING", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData(uri);
                                    getApplicationContext().startActivity(intent);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
            }
        }
    }

    private void setCurrentDate() throws ParseException {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        EnrollmentDate.setText(formattedDate);

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
                EnrollmentDate.setText(sdf.format(myCalendar.getTime()));

            }
        };
        EnrollmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog m_date = new DatePickerDialog(newNotificationScreen.this, R.style.calender_theme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }

    public static void getDistrict(Context context, String state_id) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));

            return;
        }
        String url = "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_dist&w=n_st_id<<EQUALTO>>" + state_id;
        ApiClient.getClient().getDistrictFromState(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveDistrict(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
            }
        });
    }

    public static void getTU(Context context, String st_id, String dis_id) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

            return;
        }

        Log.d("ihsi", "getTU: W  " + BaseUtils.getUserInfo(context).getnAccessRights());
        Log.d("ihsi", "getTU: sanc  " + BaseUtils.getUserInfo(context).getN_staff_sanc());

        String url = "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_tu&w=n_st_id<<EQUALTO>>" + st_id + "<<AND>>n_dis_id<<EQUALTO>>" + dis_id;
        //String url = "_sptu_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_tu&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserOtherInfo(context).getN_staff_sanc();
        Log.d("ihsi", "getTU: sanc  " + url);
        List<FormOneData> TuList = new ArrayList<>();
        ApiClient.getClient().getFormTU(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        Log.d("uhuhoy", "onResponse: " + response.body().getUserData().size());
                        BaseUtils.saveTU(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

                    } else {
                        BaseUtils.saveTU(context, TuList);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
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

    private void takeImageFromCameraUri() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        CommonImageUri = newNotificationScreen.this.getApplication().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonImageUri);
        startActivityForResult(intent, 0);
    }


    public BroadcastReceiver broadcastReceiverDoc = new BroadcastReceiver() {
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

    private void getRoomDoctors() {
        LiveData<List<RoomDoctorsList>> roomDoc = dataBase.customerDao().getSelectedDoctorsFromRoom(hfIdGlobal);
        roomDoc.observe(newNotificationScreen.this, roomDoctorsLists -> {

            Log.d("shobhit_docName", "inside room ");

            Log.d("shobhit_docName", roomDoctorsLists.size() + "");
            doctorName.clear();

            DoctorsLists = roomDoctorsLists;


            for (int i = 0; i < roomDoctorsLists.size(); i++) {


                doctorName.add(roomDoctorsLists.get(i).getDocname());

                Log.d("shobhit_docName", roomDoctorsLists.get(i).getDocname());
            }


            setSpinnerAdapter(spinnerDoctor, doctorName);

//                setHospitalRecycler(this.roomDoctorsLists);
        });
    }
}