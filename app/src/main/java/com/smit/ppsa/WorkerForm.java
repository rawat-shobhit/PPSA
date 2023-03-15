package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.smit.ppsa.Adapter.AttendenceListAdapter;
import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Network.NetworkConnection;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.AttendeceResponse;
import com.smit.ppsa.Response.AttendenceListData;
import com.smit.ppsa.Response.AttendenceTypeData;
import com.smit.ppsa.Response.AttendenceTypeResponse;
import com.smit.ppsa.Response.FormOneModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerForm extends AppCompatActivity implements View.OnClickListener {
    private CardView submitbtn;
    private String post,date;
    private AppDataBase dataBase;
    private ImageView backbtn,menuBtn;
    private static GlobalProgressDialog progressDialog;
    private List<AttendenceTypeData> attendenceTypeData =new ArrayList<>();
    private List<String> typ = new ArrayList<>();
    private TextView attendencestatus,attendencetype ,dateone,timeone,name,at_dateftwo,at_timeftwo,home;
    private Context context;
    private String wayLatitude="",wayLongitude="";
    private RecyclerView attendanceRec;
    private List<AttendenceListData> attendenceLists = new ArrayList<>();
    private Spinner statusattendence,attendenceType,pp_spinner;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_form);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        initViews();
    }

    private void initViews() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dataBase = AppDataBase.getDatabase(this);
        submitbtn = findViewById(R.id.bt_submit);
        attendanceRec = findViewById(R.id.attendanceRec);
        backbtn = findViewById(R.id.backbtn);
        attendencestatus = findViewById(R.id.attendence_status);
        attendencetype = findViewById(R.id.attendence_type);
        menuBtn = findViewById(R.id.menuBtn);
        statusattendence = findViewById(R.id.af_spinnerstatus);
        home = findViewById(R.id.home);
        dateone = findViewById(R.id.at_datefone);
        at_dateftwo = findViewById(R.id.at_dateftwo);

        pp_spinner = findViewById(R.id.pp_spinner);
        name = findViewById(R.id.at_worker_name);
        at_timeftwo = findViewById(R.id.at_timeftwo);
        name.setText(BaseUtils.getUserInfo(this).getcName());
        context = WorkerForm.this;


        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm aaa");
        SimpleDateFormat postdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        date = curFormater.format(currentTime);
        post = postdate.format(currentTime);
        at_dateftwo.setText(curFormater.format(currentTime));
        at_timeftwo.setText(time.format(currentTime));
        dateone.setText(curFormater.format(currentTime));
//
        getAttendenceType(context,curFormater.format(currentTime));
        setOnclick();
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                new Handler().postDelayed(this::getFormOneFromRoom,100);
            }
        });

        getLocation();
        NetworkCalls.getAttendanceList(WorkerForm.this,BaseUtils.getUserInfo(this).getnUserLevel());
    }


    private void setOnclick() {
        submitbtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        home.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.backbtn:
                  super.onBackPressed();
                  break;

            case R.id.bt_submit:
                if (pp_spinner.getSelectedItemPosition() == 0 ){
                    BaseUtils.showToast(this,"Please select presence type");
                    return;
                }
                postAttendance();
                break;

            case R.id.menuBtn:
                PopupMenu popupMenu = new PopupMenu(WorkerForm.this, menuBtn);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        switch (menuItem.getItemId()){
                            case R.id.add_hf:
                                startActivity(new Intent(WorkerForm.this,AddHospitalFacilty.class));
                                break;
                            case R.id.view_hf:
                                startActivity(new Intent(WorkerForm.this,HospitalsList.class));
                                break;
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
                break;
            case R.id.home:
                startActivity(new Intent(WorkerForm.this,MainActivity.class));
                finish();
        }
    }


    private void getAttendence(Context context,String date){
        if (!BaseUtils.isNetworkAvailable(context)) {

            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_t_attend&w=Year(d_rpt)%3C%3CEQUALTO%3E%3E"+date.split("/")[2]+"%3C%3CAND%3E%3EMonth(d_rpt)%3C%3CEQUALTO%3E%3E"+date.split("/")[1]+"%3C%3CAND%3E%3EDay(d_rpt)%3C%3CEQUALTO%3E%3E"+date.split("/")[0]+"%3C%3CAND%3E%3En_user_id%3C%3CEQUALTO%3E%3E"+BaseUtils.getUserInfo(WorkerForm.this).getnUserLevel();
        ApiClient.getClient().getAttendence(url).enqueue(new Callback<AttendeceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<AttendeceResponse> call, Response<AttendeceResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        AttendenceTypeData typeData = attendenceTypeData.stream()
                                .filter(attendenceType -> response.body().getUserData().get(0).getnAttendTyp().equals(attendenceType.getId()))
                                .findAny()
                                .orElse(null);
                        BaseUtils.putUserAttendance(context,true);

                        attendencetype.setText(typeData.getcAttendTyp());
//                        attendencestatus.setText(response.body().getUserData().get(0).getnAttendTyp());
                      //  dateone.setText(response.body().getUserData().get(0).getdCdat());
//                        attendencestatus.setText(response.body().getUserData().get(0).getnAttendTyp());
                   //     attendencetype .setText(response.body().getMessage());

                    }else{
                        findViewById(R.id.attendence_formone).setVisibility(View.GONE);
                        findViewById(R.id.attendence_formtwo).setVisibility(View.VISIBLE);
                    }
                }else{
                    BaseUtils.showToast(context, response.message());
                }
            }

            @Override
            public void onFailure(Call<AttendeceResponse> call, Throwable t) {
                BaseUtils.showToast(context, ""+t.getMessage());
                findViewById(R.id.attendence_formone).setVisibility(View.GONE);
                findViewById(R.id.attendence_formtwo).setVisibility(View.VISIBLE);
            }
        });
    }

    private void getFormOneFromRoom(){
        LiveData<List<FormOneModel>> allCustomer = dataBase.customerDao().fetchFormOne();
        allCustomer.observe(WorkerForm.this, hospitalModels -> {

            if (hospitalModels.size()!=0){
                for (int a = 0; a<hospitalModels.size();a++){
                    sendForm(hospitalModels.get(a));
                }
            }
        });

    }

    private void getAttendenceType(Context context,String date){


        if (!BaseUtils.isNetworkAvailable(context)) {

            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        ApiClient.getClient().getAttendenceType().enqueue(new Callback<AttendenceTypeResponse>() {
            @Override
            public void onResponse(Call<AttendenceTypeResponse> call, Response<AttendenceTypeResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){

                        attendenceTypeData = response.body().getUserData();
                        for (int a = 0;a<attendenceTypeData.size();a++){
                            typ.add(attendenceTypeData.get(a).getcAttendTyp());
                        }
                        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(WorkerForm.this,typ);
                        pp_spinner.setAdapter(customSpinnerAdapter);
                        getAttendence(context,date);

                    }
                }
            }

            @Override
            public void onFailure(Call<AttendenceTypeResponse> call, Throwable t) {

            }
        });
    }
    private void sendForm(FormOneModel formOneModel){
        if (!BaseUtils.isNetworkAvailable(this)){
            return;
        }
        RequestBody n_st_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnStId(), MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnDisId(), MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(BaseUtils.getUserOtherInfo(this).getnTuId(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(formOneModel.getN_hf_id(), MediaType.parse("text/plain"));
        RequestBody n_doc_id = RequestBody.create(formOneModel.getN_doc_id(), MediaType.parse("text/plain"));
        RequestBody d_reg_dat = RequestBody.create(formOneModel.getD_reg_dat(), MediaType.parse("text/plain"));
        RequestBody n_nksh_id = RequestBody.create(formOneModel.getN_nksh_id(), MediaType.parse("text/plain"));
        RequestBody c_pat_nam = RequestBody.create(formOneModel.getC_pat_nam(), MediaType.parse("text/plain"));
        RequestBody n_age = RequestBody.create(formOneModel.getN_age(), MediaType.parse("text/plain"));
        RequestBody n_sex = RequestBody.create(formOneModel.getN_sex(), MediaType.parse("text/plain"));
        RequestBody n_wght = RequestBody.create(formOneModel.getN_wght(), MediaType.parse("text/plain"));
        RequestBody n_hght = RequestBody.create(formOneModel.getN_hght(), MediaType.parse("text/plain"));
        RequestBody c_add = RequestBody.create(formOneModel.getC_add(), MediaType.parse("text/plain"));
        RequestBody c_taluka = RequestBody.create(formOneModel.getC_taluka(), MediaType.parse("text/plain"));
        RequestBody c_town = RequestBody.create(formOneModel.getC_town(), MediaType.parse("text/plain"));
        RequestBody c_ward = RequestBody.create(formOneModel.getC_ward(), MediaType.parse("text/plain"));
        RequestBody c_lnd_mrk = RequestBody.create(formOneModel.getC_lnd_mrk(), MediaType.parse("text/plain"));
        RequestBody n_pin = RequestBody.create(formOneModel.getN_pin(), MediaType.parse("text/plain"));
        RequestBody n_st_id_res = RequestBody.create(formOneModel.getN_st_id_res(), MediaType.parse("text/plain"));
        RequestBody n_dis_id_res = RequestBody.create(formOneModel.getN_dis_id_res(), MediaType.parse("text/plain"));
        RequestBody n_tu_id_res = RequestBody.create(formOneModel.getN_tu_id_res(), MediaType.parse("text/plain"));
        RequestBody c_mob = RequestBody.create(formOneModel.getC_mob(), MediaType.parse("text/plain"));
        RequestBody c_mob_2 = RequestBody.create(formOneModel.getC_mob_2(), MediaType.parse("text/plain"));
        RequestBody n_lat = RequestBody.create(formOneModel.getN_lat(), MediaType.parse("text/plain"));
        RequestBody n_lng = RequestBody.create(formOneModel.getN_lng(), MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(this).getnUserLevel(), MediaType.parse("text/plain"));


        ApiClient.getClient().postFormOne(n_st_id,n_dis_id
                ,n_tu_id,n_hf_id
                ,n_doc_id,d_reg_dat
                ,n_nksh_id,c_pat_nam
                ,n_age,n_sex
                ,n_wght,n_hght
                ,c_add,c_taluka
                ,c_town,c_ward
                ,c_lnd_mrk,n_pin
                ,n_st_id_res,n_dis_id_res
                ,n_tu_id_res,c_mob
                ,c_mob_2,n_lat
                ,n_lng,n_user_id,n_user_id,n_user_id,n_user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()){

                        dataBase.customerDao().deleteFormOne(formOneModel);
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

            }
        });
    }
    private void postAttendance(){
        if (!BaseUtils.isNetworkAvailable(this)){
            BaseUtils.showToast(this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        RequestBody date1 = RequestBody.create(post,MediaType.parse("text/plain"));
        RequestBody type = RequestBody.create(attendenceTypeData.get(pp_spinner.getSelectedItemPosition()-1).getId(),MediaType.parse("text/plain"));
        RequestBody userId = RequestBody.create(BaseUtils.getUserInfo(this).getId(),MediaType.parse("text/plain"));
        RequestBody sanc_id= RequestBody.create(BaseUtils.getUserInfo(this).getN_staff_sanc(),MediaType.parse("text/plain"));
        RequestBody lat= RequestBody.create(wayLatitude,MediaType.parse("text/plain"));
        RequestBody lng= RequestBody.create(wayLongitude,MediaType.parse("text/plain"));

        ApiClient.getClient().postAttendance(type,date1,userId,sanc_id,lat,lng).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                BaseUtils.showToast(context, "Attendance updated");
                BaseUtils.putUserAttendance(context,true);
                getAttendence(WorkerForm.this,date);
                startActivity(new Intent(WorkerForm.this,MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

            }
        });
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            if (intent.hasExtra("notifyAdapter")) {
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
    private void setHospitalRecycler() {
        List<AttendenceListData> hospitalLists = BaseUtils.getAttendance(WorkerForm.this);
        //hfID = hospitalLists.get(0).getnHfId();

        AttendenceListAdapter hospitalsAdapter = new AttendenceListAdapter(hospitalLists,WorkerForm.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkerForm.this, LinearLayoutManager.VERTICAL, false);
        attendanceRec.setLayoutManager(linearLayoutManager);
        attendanceRec.setAdapter(hospitalsAdapter);
        attendanceRec.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));

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
}