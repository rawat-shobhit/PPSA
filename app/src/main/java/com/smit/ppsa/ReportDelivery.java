package com.smit.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.smit.ppsa.Adapter.TestedPatientAdapter;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.Response.RegisterParentResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDelivery extends AppCompatActivity {
    private RecyclerView f2_patientrecycler;
    private List<RegisterParentData> registerParentData = new ArrayList<>();
    private TestedPatientAdapter testedPatientAdapter;
    private GlobalProgressDialog progressDialog;
    private EditText search;
    private ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_delivery);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        init();

    }
    private void init(){
        progressDialog = new GlobalProgressDialog(this);
        f2_patientrecycler = findViewById(R.id.f2_patientrecycler);
        search = findViewById(R.id.search);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportDelivery.super.onBackPressed();
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
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<RegisterParentData> temp = new ArrayList();
        for (RegisterParentData d : registerParentData) {
            String value = d.getcPatNam().toLowerCase();
            if (value.contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        testedPatientAdapter.updateList(temp);
    }
    private void getPatientList(){
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(ReportDelivery.this)) {
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            LocalBroadcastManager.getInstance(ReportDelivery.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        String url = "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_del&w=n_tu_id%3C%3CEQUALTO%3E%3E"+BaseUtils.getUserOtherInfo(this).getnTuId()+"%3C%3CAND%3E%3En_user_id%3C%3CEQUALTO%3E%3E"+BaseUtils.getUserInfo(this).getnUserLevel();
        ApiClient.getClient().getPatientTestDone(url).enqueue(new Callback<RegisterParentResponse>() {
            @Override
            public void onResponse(Call<RegisterParentResponse> call, Response<RegisterParentResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        LocalBroadcastManager.getInstance(ReportDelivery.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                        BaseUtils.savePatientList(ReportDelivery.this,response.body().getUserData());

                    }
                }
                new Handler(Looper.getMainLooper()).postDelayed(()->{
                    progressDialog.hideProgressBar();
                },900);
            }

            @Override
            public void onFailure(Call<RegisterParentResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(ReportDelivery.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                new Handler(Looper.getMainLooper()).postDelayed(()->{
                    progressDialog.hideProgressBar();
                },900);

            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("setRecycler")){
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
                    registerParentData = BaseUtils.getPateint(ReportDelivery.this);
                    testedPatientAdapter = new TestedPatientAdapter(registerParentData,ReportDelivery.this);
                    f2_patientrecycler.setLayoutManager(new LinearLayoutManager(ReportDelivery.this));
                    f2_patientrecycler.setAdapter(testedPatientAdapter);
                }, 1000);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getPatientList();
    }
}