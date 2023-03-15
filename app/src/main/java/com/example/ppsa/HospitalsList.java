package com.example.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ppsa.Adapter.HospitalsAdapter;
import com.example.ppsa.Dao.AppDataBase;
import com.example.ppsa.Network.ApiClient;
import com.example.ppsa.Network.NetworkCalls;
import com.example.ppsa.Network.NetworkConnection;
import com.example.ppsa.Response.AddDocResponse;
import com.example.ppsa.Response.DoctorModel;
import com.example.ppsa.Response.DoctorsList;
import com.example.ppsa.Response.HospitalList;
import com.example.ppsa.Response.HospitalModel;
import com.example.ppsa.Response.QualificationList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalsList extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbtn,addbtn;
    private TextView nextbtn;
    private AppDataBase dataBase;
    private List<HospitalModel> hospitalModelList = new ArrayList<>();
    private RecyclerView hospitalRecycler;
    private String hfID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals_list);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        init();

    }

    private void init() {
        dataBase = AppDataBase.getDatabase(this);
        NetworkCalls.getHospitalData(this);
        backbtn = findViewById(R.id.backbtn);
        addbtn = findViewById(R.id.hl_addbtn);
        hospitalRecycler = findViewById(R.id.hospitalRecycler);
        nextbtn = findViewById(R.id.nextbtn);
        nextbtn.setEnabled(false);
        setClickListners();
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                new Handler().postDelayed(this::getAllCustomer,100);
            }
        });
    }

    private void setClickListners() {
        backbtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        addbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                super.onBackPressed();
                break;
            case R.id.nextbtn:
                startActivity(new Intent(HospitalsList.this,FormTwo.class).putExtra("hf_id",hfID));
                break;
            case R.id.hl_addbtn:
                startActivity(new Intent(HospitalsList.this,AddHospitalFacilty.class));
                break;
        }
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            if (intent.hasExtra("checked")) {
                boolean checked = intent.getBooleanExtra("checked", false);
                nextbtn.setEnabled(checked);
                hfID = intent.getStringExtra("hf_id");
            } else if (intent.hasExtra("notifyAdapter")) {
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
                setHospitalRecycler();
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

    private void setHospitalRecycler() {
        List<HospitalList> hospitalLists = BaseUtils.getHospital(HospitalsList.this);
        hfID = hospitalLists.get(0).getnHfId();
        HospitalsAdapter hospitalsAdapter = new HospitalsAdapter(hospitalLists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
        hospitalRecycler.setLayoutManager(linearLayoutManager);
        hospitalRecycler.setAdapter(hospitalsAdapter);
        hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
    }

    private void getAllCustomer() {
        LiveData<List<HospitalModel>> allCustomer = dataBase.customerDao().fetchHospital();
        allCustomer.observe(HospitalsList.this, hospitalModels -> {
            hospitalModelList = hospitalModels;
            if (hospitalModelList.size()!=0){
                for (int a = 0; a<hospitalModelList.size();a++){
                    addHospital(hospitalModelList.get(a),a);
                }
            }

        });
    }

    private void addHospital(HospitalModel hospitalModel,int pos){
        if (!BaseUtils.isNetworkAvailable(HospitalsList.this)){
            return;
        }
        RequestBody stID = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnStId(), MediaType.parse("text/plain"));
        RequestBody disID = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnDisId(),MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnTuId(),MediaType.parse("text/plain"));
        RequestBody n_hf_cd = RequestBody.create(hospitalModel.getN_hf_cd().toString(),MediaType.parse("text/plain"));
        RequestBody c_hf_nam = RequestBody.create(hospitalModel.getC_hf_nam(),MediaType.parse("text/plain"));
        RequestBody n_hf_typ_id = RequestBody.create(hospitalModel.getN_hf_typ_id(),MediaType.parse("text/plain"));
        RequestBody c_hf_addr = RequestBody.create(hospitalModel.getC_hf_addr(),MediaType.parse("text/plain"));
        RequestBody c_cont_per = RequestBody.create(hospitalModel.getC_cont_per(),MediaType.parse("text/plain"));
        RequestBody c_cp_mob = RequestBody.create(hospitalModel.getC_cp_mob(),MediaType.parse("text/plain"));
        RequestBody c_cp_email = RequestBody.create(hospitalModel.getC_cp_email(),MediaType.parse("text/plain"));
        RequestBody n_sc_id = RequestBody.create(hospitalModel.getN_sc_id(),MediaType.parse("text/plain"));
        RequestBody n_pp_idenr = RequestBody.create(hospitalModel.getN_pp_idenr(),MediaType.parse("text/plain"));
        RequestBody c_tc_nam = RequestBody.create(hospitalModel.getC_tc_nam(),MediaType.parse("text/plain"));
        RequestBody c_tc_mob = RequestBody.create(hospitalModel.getC_tc_mob(),MediaType.parse("text/plain"));
        RequestBody n_bf_id = RequestBody.create(hospitalModel.getN_bf_id(),MediaType.parse("text/plain"));
        RequestBody n_pay_status = RequestBody.create("0",MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(this).getnUserLevel(),MediaType.parse("text/plain"));
        RequestBody lat = RequestBody.create(hospitalModel.getLat(),MediaType.parse("text/plain"));
        RequestBody lng = RequestBody.create(hospitalModel.getLng(),MediaType.parse("text/plain"));
        ApiClient.getClient().addHospital(stID,disID,n_tu_id,n_hf_cd,c_hf_nam,n_hf_typ_id,c_hf_addr,c_cont_per,c_cp_mob,c_cp_email,
                n_sc_id,n_pp_idenr,c_tc_nam,c_tc_mob,n_bf_id,n_pay_status,n_user_id,lat,lng).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().isStatus()){
                        dataBase.customerDao().deleteHospital(hospitalModel);
                        NetworkCalls.hospitalSync(HospitalsList.this,response.body().getUserData(),true);

                        if (pos == hospitalModelList.size()-1){
                            Toast.makeText(HospitalsList.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }

}