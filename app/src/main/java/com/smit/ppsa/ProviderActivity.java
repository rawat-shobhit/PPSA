package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.PostProviderFromRoom;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView providerangagementbtn,reportdeliverybtn,samplecollection;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView type,nextbtn,username,address;
    String wayLatitude,wayLongitude;
    private int providerType=0;
    private AppDataBase dataBase;

    private HospitalList hospitalLis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        init();
    }
    private void getLocation() {
        dataBase = AppDataBase.getDatabase(this);
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
    private void init(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        providerangagementbtn = findViewById(R.id.providerangagementbtn);
        reportdeliverybtn = findViewById(R.id.reportdeliverybtn);
        samplecollection = findViewById(R.id.samplecollection);
        type = findViewById(R.id.type);
        nextbtn = findViewById(R.id.nextbtn);
        username = findViewById(R.id.username);
        address = findViewById(R.id.dateandtime);

        List<HospitalList> hospitalLists = BaseUtils.getHospital(this);
        for (int a = 0;a<hospitalLists.size();a++){
            if (hospitalLists.get(a).getnHfId().equals(getIntent().getStringExtra("hf_id"))){
                hospitalLis = hospitalLists.get(a);
            }
        }
        username.setText(hospitalLis.getcHfNam());
        address.setText(hospitalLis.getcHfAddr());
        setOnClick();
    }

    private void setOnClick(){
        providerangagementbtn.setOnClickListener(this);
        reportdeliverybtn.setOnClickListener(this);
        samplecollection.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.providerangagementbtn:
                type.setText("Seeking Support Visit");
                providerType = 1;
                break;
            case R.id.reportdeliverybtn:
                type.setText("Referrals of presumptive");
                providerType = 2;
                break;
            case R.id.samplecollection:
                type.setText("Sample Collections");
                providerType = 3;
                break;
            case R.id.nextbtn:
                if (providerType==0){
                    BaseUtils.showToast(this,"Please select provider type");
                    return;
                }else{
                    postProvider();
                }
                break;


        }
    }
    private void postProvider(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(ProviderActivity.this, "Please Check your internet  Connectivity, saved to local");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            PostProviderFromRoom providerFromRoom = new PostProviderFromRoom(hospitalLis.getnStId(),
                    hospitalLis.getnDisId(),hospitalLis.getnTuId(),hospitalLis.getnHfId(),curFormater.format(currentTime),
                    String.valueOf(providerType),BaseUtils.getUserInfo(this).getnUserLevel(),wayLatitude,wayLongitude);
            dataBase.customerDao().saveNewProviderDetail(providerFromRoom);
            startActivity(new Intent(ProviderActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            return;
        }

        RequestBody n_st_id = RequestBody.create(hospitalLis.getnStId(),MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(hospitalLis.getnDisId(),MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(hospitalLis.getnTuId(),MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(hospitalLis.getnHfId(),MediaType.parse("text/plain"));
        RequestBody d_reg_dat = RequestBody.create(curFormater.format(currentTime),MediaType.parse("text/plain"));
        RequestBody n_typ = RequestBody.create(String.valueOf(providerType),MediaType.parse("text/plain"));
        RequestBody user_id = RequestBody.create(BaseUtils.getUserInfo(this).getId(),MediaType.parse("text/plain"));
        RequestBody lat = RequestBody.create(wayLatitude,MediaType.parse("text/plain"));
        RequestBody lng = RequestBody.create(wayLongitude,MediaType.parse("text/plain"));

        ApiClient.getClient().postProviderSubmit(n_st_id,n_dis_id,n_tu_id,n_hf_id,d_reg_dat,n_typ,user_id,lat,lng).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        BaseUtils.showToast(ProviderActivity.this, "Submit");
                        startActivity(new Intent(ProviderActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

            }
        });

    }
}