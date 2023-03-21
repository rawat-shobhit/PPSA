package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smit.ppsa.Adapter.SpinAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.HospitalModel;
import com.smit.ppsa.Response.HospitalResponse;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.QualificationList;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHospitalFacilty extends AppCompatActivity implements View.OnClickListener {
    private FusedLocationProviderClient mFusedLocationClient;
    private AppDataBase dataBase;
    private ImageView backBtn;
    private EditText hf_code, hf_name, other, address, contact_name, contact_number, email, tc_name, tc_number;
    private Spinner hf_type_spinner, se_spinner, pp_spinner, beneficiary_spinner;
    private static List<QualificationList> hfTypeLIsts = new ArrayList<>();
    private static List<QualificationList> scopeLists = new ArrayList<>();
    private static List<QualificationList> benefeciaryList = new ArrayList<>();
    private String hfTypeId = "", scopeID = "", benID = "";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String wayLatitude = "0", wayLongitude = "0";
    String[] arraySpinner = new String[]{
            "Select", "Yes", "No"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital_facilty);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));

        init();
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

                    wayLatitude = String.valueOf(location.getLatitude());
                    wayLongitude = String.valueOf(location.getLongitude());
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

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        dataBase = AppDataBase.getDatabase(this);
        hf_code = findViewById(R.id.hf_code);
        hf_name = findViewById(R.id.hf_name);
        other = findViewById(R.id.other);
        backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(this);
        address = findViewById(R.id.address);
        contact_name = findViewById(R.id.contact_name);
        contact_number = findViewById(R.id.contact_number);
        email = findViewById(R.id.email);
        tc_name = findViewById(R.id.tc_name);
        tc_number = findViewById(R.id.tc_number);
        hf_type_spinner = findViewById(R.id.hf_type_spinner);
        se_spinner = findViewById(R.id.se_spinner);
        pp_spinner = findViewById(R.id.pp_spinner);
        beneficiary_spinner = findViewById(R.id.beneficiary_spinner);
        findViewById(R.id.ad_nextbtn).setOnClickListener(view -> {
            if (allFill()) addHospital();

        });
        NetworkCalls.getHFType(this);
        NetworkCalls.getScope(this);
        NetworkCalls.getBenefeciary(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        pp_spinner.setAdapter(adapter);
        hf_type_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        // It returns the clicked item.

                        if (position != 5) {
                            other.setText("");
                            other.setEnabled(false);
                        } else {
                            other.setEnabled(true);
                        }
                        QualificationList clickedItem = (QualificationList)
                                parent.getItemAtPosition(position);
                        hfTypeId = clickedItem.getId();
                        Log.d("mkoi", "onItemSelected: " + hfTypeId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        se_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        // It returns the clicked item.
                        QualificationList clickedItem = (QualificationList)
                                parent.getItemAtPosition(position);
                        scopeID = clickedItem.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        beneficiary_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        // It returns the clicked item.
                        QualificationList clickedItem = (QualificationList)
                                parent.getItemAtPosition(position);
                        benID = clickedItem.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                super.onBackPressed();
                break;
        }
    }

    private boolean emptyText(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private boolean allFill() {
        if (emptyText(hf_code)) {
            BaseUtils.showToast(this, "Enter HF code");
            return false;
        } else if (emptyText(hf_name)) {
            BaseUtils.showToast(this, "Enter HF name");
            return false;
        } else if (hf_type_spinner.getSelectedItemPosition() == 0) {
            BaseUtils.showToast(this, "Select HF type");
            return false;
        } else if (hf_type_spinner.getSelectedItemPosition() == 5 && emptyText(other)) {
            BaseUtils.showToast(this, "Enter HF type in others");
            return false;
        } else if (emptyText(address)) {
            BaseUtils.showToast(this, "Enter address");
            return false;
        } else if (emptyText(contact_name)) {
            BaseUtils.showToast(this, "Enter contact person name");
            return false;
        } else if (emptyText(contact_number)) {
            BaseUtils.showToast(this, "Enter mobile number");
            return false;
        }

        return true;
    }

    private void setspinnerAdapter(Spinner spinner, List<QualificationList> qualificationLists) {

        SpinAdapter adapter = new SpinAdapter(AddHospitalFacilty.this,

                qualificationLists);
        spinner.setAdapter(adapter);
        fillDetailForEdit();

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localhfTypeData")) {
                hfTypeLIsts = BaseUtils.gethfType(context);
                setspinnerAdapter(hf_type_spinner, hfTypeLIsts);

            } else if (intent.hasExtra("notifyhfTypeAdapter")) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    hfTypeLIsts = BaseUtils.gethfType(context);
                    setspinnerAdapter(hf_type_spinner, hfTypeLIsts);
                }, 1000);

            }
            if (intent.hasExtra("localScopeData")) {

                scopeLists = BaseUtils.getScope(context);
                setspinnerAdapter(se_spinner, scopeLists);
            } else if (intent.hasExtra("notifyScopeAdapter")) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    scopeLists = BaseUtils.getScope(context);
                    setspinnerAdapter(se_spinner, scopeLists);
                }, 1000);


            }
            if (intent.hasExtra("localBenData")) {
                benefeciaryList = BaseUtils.getBenefeciary(context);
                setspinnerAdapter(beneficiary_spinner, benefeciaryList);
            } else if (intent.hasExtra("notifyBenAdapter")) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    benefeciaryList = BaseUtils.getBenefeciary(context);
                    setspinnerAdapter(beneficiary_spinner, benefeciaryList);
                }, 1000);

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
        }
    }

    private void addHospital() {
        String pp = "";
        if (pp_spinner.getSelectedItemPosition() > 0) {
            pp = pp_spinner.getSelectedItem().toString();
        }
        BaseUtils.putHospitalnStId(this, BaseUtils.getUserOtherInfo(this).getnStId());
        BaseUtils.putHospitalnDisId(this, BaseUtils.getUserOtherInfo(this).getnDisId());
        BaseUtils.putHospitalnTUId(this, getIntent().getStringExtra("tu_id"));

        NetworkCalls.addHospital(
                AddHospitalFacilty.this,
                BaseUtils.getUserOtherInfo(this).getnStId(),
                BaseUtils.getUserOtherInfo(this).getnDisId(),
                getIntent().getStringExtra("tu_id"),
                hf_code.getText().toString(),
                hf_name.getText().toString(),
                hfTypeId,
                address.getText().toString(),
                contact_name.getText().toString(),
                contact_number.getText().toString(),
                email.getText().toString(),
                scopeID,
                pp,
                tc_name.getText().toString(),
                tc_number.getText().toString(),
                benID, "0",
                BaseUtils.getUserInfo(this).getId(),
                wayLatitude,
                wayLongitude, true
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddHospitalFacilty.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(AddHospitalFacilty.this)
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

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
    }

    private void fillDetailForEdit() {
        //progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(AddHospitalFacilty.this)) {
            Toast.makeText(AddHospitalFacilty.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        BaseUtils.putPatientName(AddHospitalFacilty.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(AddHospitalFacilty.this).getnUserLevel());
//https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf&w=id<<EQUALTO>>1632
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf&w=id<<EQUALTO>>1632";
        ApiClient.getClient().getHospitalDetail(url).enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, Response<HospitalResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataTestReportResults = response.body().getUser_data();

                        HospitalList model = response.body().getUserData().get(0);

                        hf_code.setText(model.getnHfCd());
                        hf_name.setText(model.getcHfNam());
                        other.setText(model.getOthNam());
                        address.setText(model.getcHfAddr());
                        email.setText(model.getcCpEmail());
                        tc_name.setText(model.getcTuName());
                        contact_number.setText(model.getcCpMob());
                        contact_name.setText(model.getcContPer());


                        for (int i = 0; i < arraySpinner.length; i++) {

                            if (arraySpinner[i].toLowerCase().equals(model.getN_pp_idenr().toLowerCase())) {

                                pp_spinner.setSelection(i);
                                break;
                            }
                        }


                        for (int i = 0; i < hfTypeLIsts.size(); i++) {

                            Log.d("checking loop ", hfTypeLIsts.get(i).getId().toString() + "  " + model.getnHfTypId().toLowerCase());
                            if (hfTypeLIsts.get(i).getId().toString().toLowerCase().equals(model.getnHfTypId().toLowerCase())) {

                                hf_type_spinner.setSelection(i + 1);
                                break;
                            }
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<HospitalResponse> call, Throwable t) {
            }
        });


    }


}