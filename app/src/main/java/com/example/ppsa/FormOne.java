package com.example.ppsa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppsa.Adapter.CustomSpinnerAdapter;
import com.example.ppsa.Dao.AppDataBase;
import com.example.ppsa.Network.ApiClient;
import com.example.ppsa.Network.NetworkCalls;
import com.example.ppsa.Response.AddDocResponse;
import com.example.ppsa.Response.FormOneData;
import com.example.ppsa.Response.FormOneModel;
import com.example.ppsa.Response.QualificationList;
import com.google.android.gms.common.api.Api;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormOne extends AppCompatActivity implements View.OnClickListener {
    private CardView proceedbtn;
    private ImageView backBtn;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<FormOneData> genders = new ArrayList<>();
    private List<FormOneData> state = new ArrayList<>();
    private List<FormOneData> district = new ArrayList<>();
    private List<FormOneData> tu = new ArrayList<>();
    List<String> genderStrings = new ArrayList<>();
    List<String> stateStrings = new ArrayList<>();
    List<String> distri = new ArrayList<>();
    List<String> tuStrings = new ArrayList<>();
    String lat, lng;
    private AppDataBase dataBase;
    private String[] st_id;
    private String st_id_res;
    private String[] dis_id;
    private String dis_id_res;
    private String[] tu_id;
    private String tu_id_res;
    private String gender;
    private TextView EnrollmentDate;
    private EditText EnrollHealthFacilitySector, EnrollmentFaciltyPHI, EnrollmentFaciltyHFcode, UserIDEnrollment, EnrolmentId, PatientName, Age, Weight, Height, Address,
            Taluka, Town, Ward, Landmark, Pincode, PrimaryPhoneNumber, SecondaryPhoneNumber;
    private Spinner EnrollmentFaciltyState, EnrollmentFaciltyDistrict, EnrollmentFaciltyTBU, Gender, ResidentialState, ResidentialDistrict, ResidentialTU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_one);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));

        initViews();

    }

    private void initViews() {
        dataBase = AppDataBase.getDatabase(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        proceedbtn = findViewById(R.id.bt_proceedone);
        backBtn = findViewById(R.id.backbtn);
        EnrollmentDate = findViewById(R.id.f1_enrollment);

        EnrolmentId = findViewById(R.id.f1_enrollID);
        PatientName = findViewById(R.id.f1_patientName);
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

        setUpCalender();
        NetworkCalls.getGender(this);
        NetworkCalls.getState(this);
        NetworkCalls.getTU(this);
        NetworkCalls.getDistrict(this);



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
                    tu_id_res = "";
                } else {
                    tu_id_res = tu.get(i - 1).getN_tu_id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ResidentialDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    dis_id_res = "";
                } else {
                    dis_id_res = district.get(i - 1).getnDisId();
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
    }

    private void spinnerSelect(final String type[], List<FormOneData> formOneData, Spinner spinner) {

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
                if (isValidate()) {
                    sendForm();
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
        } else if (emptyText(Age)) {
            BaseUtils.showToast(this, "Enter age");
            return false;
        } else if(Integer.parseInt(Age.getText().toString())<1||Integer.parseInt(Age.getText().toString())>105){
            BaseUtils.showToast(this,"Enter age between 0 to 105");
            return false;
        }else if (TextUtils.isEmpty(gender)) {
            BaseUtils.showToast(this, "Select gender");
            return false;
        } else if (emptyText(Weight)) {
            BaseUtils.showToast(this, "Enter weight");
            return false;
        } else if (emptyText(Height)) {
            BaseUtils.showToast(this, "Enter height");
            return false;
        } else if (emptyText(Address)) {
            BaseUtils.showToast(this, "Enter address");
            return false;
        } else if (emptyText(Taluka)) {
            BaseUtils.showToast(this, "Enter taluka");
            return false;
        } else if (emptyText(Town)) {
            BaseUtils.showToast(this, "Enter town name");
            return false;
        } else if (emptyText(Ward)) {
            BaseUtils.showToast(this, "Enter ward");
            return false;
        } else if (emptyText(Landmark)) {
            BaseUtils.showToast(this, "Enter Landmark");
            return false;
        } else if (emptyText(Pincode)) {
            BaseUtils.showToast(this, "Enter pincode");
            return false;
        } else if (TextUtils.isEmpty(st_id_res)) {
            BaseUtils.showToast(this, "Select residential state");
            return false;
        } else if (TextUtils.isEmpty(dis_id_res)) {
            BaseUtils.showToast(this, "Select residential district");
            return false;
        } else if (TextUtils.isEmpty(tu_id_res)) {
            BaseUtils.showToast(this, "Select residential TU");
            return false;
        } else if (emptyText(PrimaryPhoneNumber)) {
            BaseUtils.showToast(this, "Enter primary phone number");
            return false;
        } else if (emptyText(SecondaryPhoneNumber)) {
            BaseUtils.showToast(this, "Enter secondary phone number");
            return false;
        }
        return true;
    }

    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localGender")){
                genders = BaseUtils.getGender(FormOne.this);
                //for (int a = 0;a<genders.size(),)
            }
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra("localGender")) {
                genders = BaseUtils.getGender(context);
                for (FormOneData gender : genders) {
                    genderStrings.add(gender.getcTyp());
                }

                        setSpinnerAdapter(Gender, genderStrings);



            }else if(intent.hasExtra("notifyGender")){
                genders = BaseUtils.getGender(FormOne.this);
                for (FormOneData gender : genders) {
                    genderStrings.add(gender.getcTyp());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setSpinnerAdapter(Gender, genderStrings);
                    }
                },500);
            }

            if (intent.hasExtra("localDistrict")) {
                district = BaseUtils.getDistrict(FormOne.this);
                for (FormOneData district : district) {
                    distri.add(district.getcDisNam());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(ResidentialDistrict, distri);
            }
            if (intent.hasExtra("localTU")) {
                tu = BaseUtils.getTU(FormOne.this);
                for (FormOneData tu : tu) {
                    tuStrings.add(tu.getcTuName());
                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(ResidentialTU, tuStrings);
            }
            if (intent.hasExtra("localState")){
                state = BaseUtils.getState(FormOne.this);
                for (FormOneData stat : state){
                    stateStrings.add(stat.getcStNam());
                }
                setSpinnerAdapter(ResidentialState,stateStrings);
            }
        }
    };


    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FormOne.this, values);
        spinner.setAdapter(spinnerAdapter);


    }


    private void sendForm() {
        if (!BaseUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet, saved in local", Toast.LENGTH_SHORT).show();
            FormOneModel formOneModel = new FormOneModel(BaseUtils.getUserOtherInfo(this).getnStId(),
                    BaseUtils.getUserOtherInfo(this).getnDisId(),
                    BaseUtils.getUserOtherInfo(this).getnTuId(),
                    getIntent().getStringExtra("hf_id"),
                    getIntent().getStringExtra("doc_id"),
                    EnrollmentDate.getText().toString(),
                    EnrolmentId.getText().toString(),
                    PatientName.getText().toString(),
                    Age.getText().toString(),
                    genders.get(Gender.getSelectedItemPosition() - 1).getcTyp(),
                    Weight.getText().toString(),
                    Height.getText().toString(),
                    Address.getText().toString(),
                    Taluka.getText().toString(),
                    Town.getText().toString(),
                    Ward.getText().toString(), Landmark.getText().toString(),
                    Pincode.getText().toString(),
                    st_id_res,
                    dis_id_res, tu_id_res, PrimaryPhoneNumber.getText().toString(),
                    SecondaryPhoneNumber.getText().toString(),
                    lat, lng, BaseUtils.getUserInfo(this).getnUserLevel());
            dataBase.customerDao().insertFormOne(formOneModel);
            startActivity(new Intent(FormOne.this, FormTwo.class));
            finish();

            return;
        }
        RequestBody n_st_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnStId(), MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnDisId(), MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnTuId(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(getIntent().getStringExtra("hf_id"), MediaType.parse("text/plain"));
        RequestBody n_doc_id = RequestBody.create(getIntent().getStringExtra("doc_id"), MediaType.parse("text/plain"));
        RequestBody d_reg_dat = RequestBody.create(EnrollmentDate.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_nksh_id = RequestBody.create(EnrolmentId.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_pat_nam = RequestBody.create(PatientName.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_age = RequestBody.create(Age.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_sex = RequestBody.create(genders.get(Gender.getSelectedItemPosition() - 1).getId(), MediaType.parse("text/plain"));
        RequestBody n_wght = RequestBody.create(Weight.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_hght = RequestBody.create(Height.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_add = RequestBody.create(Address.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_taluka = RequestBody.create(Taluka.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_town = RequestBody.create(Town.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_ward = RequestBody.create(Ward.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_lnd_mrk = RequestBody.create(Landmark.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_pin = RequestBody.create(Pincode.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_st_id_res = RequestBody.create(st_id_res, MediaType.parse("text/plain"));
        RequestBody n_dis_id_res = RequestBody.create(dis_id_res, MediaType.parse("text/plain"));
        RequestBody n_tu_id_res = RequestBody.create(tu_id_res, MediaType.parse("text/plain"));
        RequestBody c_mob = RequestBody.create(PrimaryPhoneNumber.getText().toString(), MediaType.parse("text/plain"));
        RequestBody c_mob_2 = RequestBody.create(SecondaryPhoneNumber.getText().toString(), MediaType.parse("text/plain"));
        RequestBody n_lat = RequestBody.create(lat, MediaType.parse("text/plain"));
        RequestBody n_lng = RequestBody.create(lng, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(this).getnUserLevel(), MediaType.parse("text/plain"));


        ApiClient.getClient().postFormOne(n_st_id, n_dis_id
                , n_tu_id, n_hf_id
                , n_doc_id, d_reg_dat
                , n_nksh_id, c_pat_nam
                , n_age, n_sex
                , n_wght, n_hght
                , c_add, c_taluka
                , c_town, c_ward
                , c_lnd_mrk, n_pin
                , n_st_id_res, n_dis_id_res
                , n_tu_id_res, c_mob
                , c_mob_2, n_lat
                , n_lng, n_user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(FormOne.this, "Form one submitted", Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(FormOne.this, FormTwo.class));

                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

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
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(FormOne.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(FormOne.this)
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
                DatePickerDialog m_date = new DatePickerDialog(FormOne.this, R.style.calender_theme, date, myCalendar
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