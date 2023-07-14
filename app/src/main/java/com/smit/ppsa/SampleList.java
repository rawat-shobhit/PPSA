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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Adapter.SmapleAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Response.RoomSampleList;
import com.smit.ppsa.Response.SampleData;
import com.smit.ppsa.Response.SampleResponse;
import com.smit.ppsa.sampleCollectionSampleFolder.sampleCollectionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleList extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView sampleRecycler;
    private ImageView backbtn,homeBtn;
    private TextView addSpecimenBtn,patName;
    private List<SampleData> sampleData = new ArrayList<>();
    private List<RoomSampleList> roomSampleLists = new ArrayList<>();
    private SmapleAdapter smapleAdapter;
    private AppDataBase dataBase;
    String globalEnrollmentId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_list);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));

        init();
    }


    private void init() {

        dataBase = AppDataBase.getDatabase(this);
        sampleRecycler = findViewById(R.id.sample_recycler);
        backbtn = findViewById(R.id.backbtn);
        patName = findViewById(R.id.pat_name);
        addSpecimenBtn = findViewById(R.id.addSpecimenBtn);
        if (!BaseUtils.haveAccess(this)){
            addSpecimenBtn.setVisibility(View.GONE);
        }
        sampleRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeBtn = findViewById(R.id.homeBtn);
        setOnClick();
        if (getIntent().hasExtra("patient_name")){
            patName.setText(getIntent().getStringExtra("patient_name"));
        }
        globalEnrollmentId=getIntent().getStringExtra("enroll_id");
        Log.d("enrollmentId",getIntent().getStringExtra("enroll_id"));
        //   getSample();
    }

    private void setOnClick() {
        addSpecimenBtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                super.onBackPressed();
                break;
            case R.id.addSpecimenBtn:

                //                                startActivity(new Intent(FormTwo.this, SampleList.class)
                //                                .putExtra("hf_id", hfId)
                //                                .putExtra("hospitalName", getIntent().getStringExtra("hospitalName"))
                //                                .putExtra("doc_name", doctorname)
                //                                .putExtra("patient_name", patientname)
                //                                .putExtra("patient_phone", patientphone)
                //                                .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                //                                .putExtra("enrolldate", reg_date)
                //                                .putExtra("tu_id", tuId).putExtra("enroll_id", enroll_id));

                startActivity(new Intent(this, PatientSampleList.class)
                        .putExtra("enroll_id", globalEnrollmentId)
                        .putExtra("hf_id", getIntent().getStringExtra("hf_id"))
                        .putExtra("hf_type_id", getIntent().getStringExtra("hf_type_id"))
                        .putExtra("hospitalName", getIntent().getStringExtra("hospitalName"))
                        .putExtra("doc_name", getIntent().getStringExtra("doc_name"))
                        .putExtra("patient_name", getIntent().getStringExtra("patient_name"))
                        .putExtra("patient_phone", getIntent().getStringExtra("patient_phone"))
                        .putExtra("reg_date", getIntent().getStringExtra("enrolldate"))
                        .putExtra("tu_id", getIntent().getStringExtra("tu_id")));
                break;
            case R.id.homeBtn:
                startActivity(new Intent(this,MainActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }
    }

    private void getSample() {
        sampleData.clear();
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            LocalBroadcastManager.getInstance(SampleList.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            return;
        }
        //https://nikshayppsa.hlfppt.org/_api-v1_/
/*
_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smpl_col&w=n_enroll_id<<EQUALTO>>46321<<AND>>n_user_id<<EQUALTO>>802
 */
        //http://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smpl_col&w=n_enroll_id<<EQUALTO>>46321
        // _get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smpl_col&w=n_enroll_id<<EQUALTO>>46321
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smpl_col&w=n_enroll_id<<EQUALTO>>"+globalEnrollmentId;

        Log.d("finalUrl__",url);
        ApiClient.getClient().getSample(url).enqueue(new Callback<SampleResponse>() {
            @Override
            public void onResponse(Call<SampleResponse> call, Response<SampleResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        sampleData = response.body().getUserData();
                        dataBase.customerDao().deleteAllSample();
                        Log.d("response data", response.body().getUserData().toString() );
                        sampleData = response.body().getUserData();

//                        setHospitalRecycler(sampleData);
                        setAdapterRecycler(sampleData);
                        
                        /*
                                    for (int i = 0; i < sampleData.size(); i++) {

                                RoomSampleList roomSampleList = new RoomSampleList(sampleData.get(i).getTestReas(),
                                sampleData.get(i).getDiag_test(),
                                sampleData.get(i).getSpecmTyp(),
                                sampleData.get(i).getdSpecmCol(),
                                sampleData.get(i).getcPlcSampCol(),
                                sampleData.get(i).getSmplExt(),
                                sampleData.get(i).getSputmSampl(),
                                sampleData.get(i).getnEnrollId(),
                                sampleData.get(i).getnUserId(),
                                sampleData.get(i).getnStId(),
                                sampleData.get(i).getnDisId(),
                                sampleData.get(i).getnTuId(),
                                sampleData.get(i).getnHfId(),
                                sampleData.get(i).getId());
                        dataBase.customerDao().getSampleFromServer(roomSampleList);
                    }
                    LocalBroadcastManager.getInstance(SampleList.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                         */

                    }

                }

            }

            @Override
            public void onFailure(Call<SampleResponse> call, Throwable t) {
                BaseUtils.showToast(SampleList.this, "OnFaliure  " + t.getMessage());
            }
        });
    }

    private void setAdapterRecycler(List<SampleData> sampleData) {
        sampleCollectionAdapter docAdapter = new sampleCollectionAdapter(sampleData, this);
        sampleRecycler.setLayoutManager(new LinearLayoutManager(this));
        sampleRecycler.setAdapter(docAdapter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("setRecycler")) {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
//                    getRoomDoctors();
                }, 1000);
            }
        }
    };

    private void getRoomDoctors() {
        LiveData<List<RoomSampleList>> roomDoc = dataBase.customerDao().getSelectedSampleFromRoom(globalEnrollmentId, BaseUtils.getUserInfo(this).getnUserLevel());
        roomDoc.observe(SampleList.this, roomDoctorsLists -> {
            this.roomSampleLists = roomDoctorsLists;
            setHospitalRecycler(this.roomSampleLists);
        });
    }

    private void setHospitalRecycler(List<RoomSampleList> roomDoctorsLists) {

        SmapleAdapter docAdapter = new SmapleAdapter(roomDoctorsLists, this);
        sampleRecycler.setLayoutManager(new LinearLayoutManager(this));
        sampleRecycler.setAdapter(docAdapter);

    }



    @Override
    protected void onResume() {
        super.onResume();
        getSample();
    }
}