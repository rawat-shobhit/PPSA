package com.smit.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.smit.ppsa.Adapter.TestAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.GetTestResponse;
import com.smit.ppsa.Response.RoomTestData;
import com.smit.ppsa.Response.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestList extends AppCompatActivity {
    private RecyclerView testRecycler;
    private TextView addSpecimenBtn;
    private ImageView backbtn;
    private List<TestData> testData = new ArrayList<>();
    private List<RoomTestData> roomTestData = new ArrayList<>();
    private AppDataBase dataBase;
    private TestAdapter testAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        init();
    }
    private void init(){
        dataBase = AppDataBase.getDatabase(this);
        testRecycler = findViewById(R.id.testRecycler);
        addSpecimenBtn = findViewById(R.id.addSpecimenBtn);
        backbtn = findViewById(R.id.backbtn);
        addSpecimenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TestList.this,TestActivity.class).putExtra("enroll_id",getIntent().getStringExtra("enroll_id")).
                        putExtra("n_smpl_col_id",getIntent().getStringExtra("n_smpl_col_id")).
                        putExtra("d_smpl_recd",getIntent().getStringExtra("d_smpl_recd")).
                        putExtra("hf_id",getIntent().getStringExtra("hf_id")));

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestList.super.onBackPressed();
            }
        });


    }

    private void getTest(){
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            LocalBroadcastManager.getInstance(TestList.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_sampl_tests&w=n_smpl_col_id<<EQUALTO>>"+getIntent().getStringExtra("n_smpl_col_id")+"<<AND>>n_user_id<<EQUALTO>>"+BaseUtils.getUserInfo(this).getnUserLevel();
        ApiClient.getClient().getTestList(url).enqueue(new Callback<GetTestResponse>() {
            @Override
            public void onResponse(Call<GetTestResponse> call, Response<GetTestResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        testData = response.body().getUserData();
                        dataBase.customerDao().deleteAllTest();
                        for (int a = 0;a<testData.size();a++){
                            RoomTestData roomTestData = new RoomTestData(testData.get(a).getdSmplRecd(),
                                    testData.get(a).getdRptDoc(),
                                    testData.get(a).getDiagTest(),testData.get(a).getdTstDiag(),testData.get(a).getdTstRptDiag(),
                                    testData.get(a).getDstTest(),testData.get(a).getdTstDst(),testData.get(a).getdTstRptDst(),testData.get(a).getOtherDst(),testData.get(a).getdTstRptOthDst(),testData.get(a).getdTstOthDst(),testData.get(a).getFnlInterp(),testData.get(a).getTestResul(),testData.get(a).getCaseTyp(),testData.get(a).getPatientStatus(),testData.get(a).getnSmplColId(),testData.get(a).getnUserId());
                            dataBase.customerDao().getTestFromServer(roomTestData);
                        }
                        LocalBroadcastManager.getInstance(TestList.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

                    }
                }
            }

            @Override
            public void onFailure(Call<GetTestResponse> call, Throwable t) {

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
                    getTestList();
                }, 1000);
            }
        }
    };
    private void getTestList(){
        LiveData<List<RoomTestData>> roomDoc = dataBase.customerDao().getSelectedTestFromRoom(getIntent().getStringExtra("n_smpl_col_id"),BaseUtils.getUserInfo(this).getnUserLevel());
        roomDoc.observe(TestList.this,roomDoctorsLists -> {
            this.roomTestData = roomDoctorsLists;
            setHospitalRecycler(roomTestData);
        });
    }
    private void setHospitalRecycler(List<RoomTestData> roomTestData){
        testAdapter = new TestAdapter(roomTestData);
        testRecycler.setLayoutManager(new LinearLayoutManager(this));
        testRecycler.setAdapter(testAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTest();
    }
}