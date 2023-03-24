package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.anurag.multiselectionspinner.MultiSpinner;
import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.PrevVisitsAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.PrevVisitsResponse;
import com.smit.ppsa.Response.RoomPrevVisitsData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderEngagement extends AppCompatActivity implements View.OnClickListener {
    private HospitalList hospitalList;
    private TextView hospitalNameTt, hospitalNameLocation, doctorName, hospitalTypeTitle,
            currentDate, hospitalName, hospitalAddress, hospitalType, lastVisit, date, purposeOfVisit;
    //private MultiSpinner purposeOfVisit;
    private ImageView backbtn;
    private CardView bt_proceedone;
    boolean[] selectedLanguage;
    private List<FormOneData> purposeVisit;
    private ArrayList<String> purposeStrings = new ArrayList<>();
    private RecyclerView prevVisits;
    private AppDataBase dataBase;
    private GlobalProgressDialog progressDialog;
    private List<String> selectedIds = new ArrayList<>();
    List<RoomPrevVisitsData> parentDataPreviousSamples;
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa");
    SimpleDateFormat curFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<String> visitids = new ArrayList<>();
    private List<String> visitnames = new ArrayList<>();
   // ProviderEngagementViewModel providerEngagementViewModel;
    private FusedLocationProviderClient mFusedLocationClient;
    // String visitid = "";
    String wayLatitude = "", wayLongitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_engagement);
        //providerEngagementViewModel = new ViewModelProvider(this).get(ProviderEngagementViewModel.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        dataBase = AppDataBase.getDatabase(this);
   /*     if (BaseUtils.getFormOne(this).equals("false")) {
            LiveData<List<FormOneModel>> allCustomer = dataBase.customerDao().fetchFormOne();
            allCustomer.observe(this, hospitalModels -> {

                if (hospitalModels.size() != 0) {
                    for (int a = 0; a < hospitalModels.size(); a++) {
                        Log.d("lplpl", "onCreate: " + hospitalModels.get(a).getN_st_id());

                        NetworkCalls.sendForm(this,
                                hospitalModels.get(a).getN_st_id(),
                                hospitalModels.get(a).getN_dis_id(),
                                hospitalModels.get(a).getN_tu_id(),
                                hospitalModels.get(a).getN_hf_id(),
                                hospitalModels.get(a).getN_doc_id(),
                                hospitalModels.get(a).getD_reg_dat(),
                                hospitalModels.get(a).getN_nksh_id(),
                                hospitalModels.get(a).getC_pat_nam(),
                                hospitalModels.get(a).getN_age(),
                                hospitalModels.get(a).getN_sex(),
                                hospitalModels.get(a).getN_wght(),
                                hospitalModels.get(a).getN_hght(),
                                hospitalModels.get(a).getC_add(),
                                hospitalModels.get(a).getC_taluka(),
                                hospitalModels.get(a).getC_town(),
                                hospitalModels.get(a).getC_ward(),
                                hospitalModels.get(a).getC_lnd_mrk(),
                                hospitalModels.get(a).getN_pin(),
                                hospitalModels.get(a).getN_st_id_res(),
                                hospitalModels.get(a).getN_dis_id_res(),
                                hospitalModels.get(a).getN_tu_id_res(),
                                hospitalModels.get(a).getC_mob(),
                                hospitalModels.get(a).getC_mob_2(),
                                hospitalModels.get(a).getN_lat(),
                                hospitalModels.get(a).getN_lng(),
                                hospitalModels.get(a).getN_user_id(),
                                "",
                                false,
                                hospitalModels.get(a).getNotification_image(),
                                hospitalModels.get(a).getBank_image(),
                                hospitalModels.get(a).getN_sac_id()
                        );
                    }
                }

            });

        }*/
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


                    wayLatitude =new DecimalFormat("##.######").format(location.getLatitude());
                    wayLongitude =new DecimalFormat("##.######").format(location.getLongitude());
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


        progressDialog = new GlobalProgressDialog(this);

        hospitalNameTt = findViewById(R.id.hospitalNameTitle);
        hospitalNameLocation = findViewById(R.id.locationHospital);
        doctorName = findViewById(R.id.docname);
        hospitalTypeTitle = findViewById(R.id.hospitalTYpe);
        currentDate = findViewById(R.id.currentdate);
        lastVisit = findViewById(R.id.lstvisit);
        hospitalName = findViewById(R.id.hospitalName);
        hospitalAddress = findViewById(R.id.hospitalAddress);
        hospitalType = findViewById(R.id.hospitalType);
        purposeOfVisit = findViewById(R.id.purpose_of_visit);
        bt_proceedone = findViewById(R.id.bt_proceedone);
        prevVisits = findViewById(R.id.prevVisits);
        backbtn = findViewById(R.id.backbtn);
        date = findViewById(R.id.date);
        List<HospitalList> hospitalLists = BaseUtils.getHospital(this);

        hospitalNameTt.setText(getIntent().getStringExtra("hospitalName"));
        hospitalNameLocation.setText(getIntent().getStringExtra("hospitallocation"));
        hospitalTypeTitle.setText(getIntent().getStringExtra("hospitaltypeName"));
        doctorName.setText(getIntent().getStringExtra("docName"));
        currentDate.setText(curFormater.format(currentTime));
      //  purposeOfVisit.initMultiSpinner(this, purposeOfVisit);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiverOne, new IntentFilter("provider"));

        purposeOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ProviderEngagement.this);

                // set title
                builder.setTitle("Select Purpose of Visit(s)");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems((CharSequence[]) purposeStrings.toArray(new String[purposeStrings.size()]), selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list

                            if(selectedIds.contains(String.valueOf(i))){

                            }else{
                                selectedIds.add(String.valueOf(i));
                                visitids.add(purposeVisit.get(/*purposeOfVisit.getSelectedItemPosition() - 1*/i).getId());
                                visitnames.add(purposeVisit.get(/*purposeOfVisit.getSelectedItemPosition() - 1*/i).getC_val());
                            }

                         //   selectedIds.add(i);
                            // Sort array list
                            Collections.sort(selectedIds);
                           // Collections.sort(visitnames);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            selectedIds.remove(String.valueOf(i));
                            visitids.remove(purposeVisit.get(/*purposeOfVisit.getSelectedItemPosition() - 1*/i).getId());
                            visitnames.remove(purposeVisit.get(/*purposeOfVisit.getSelectedItemPosition() - 1*/i).getC_val());
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < selectedIds.size(); j++) {
                            // concat array value
                            stringBuilder.append((purposeStrings.toArray(new String[purposeStrings.size()]))[Integer.valueOf(selectedIds.get(j))]);
                            // check condition
                            if (j != selectedIds.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        purposeOfVisit.setText(stringBuilder.toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                     //   for (int j = 0; j < selectedIds.length; j++) {
                            // remove all selection
                     //       selectedLanguage[j] = false;
                            // clear language list
                            selectedIds.clear();
                            visitids.clear();
                            visitnames.clear();
                            // clear text view value
                            purposeOfVisit.setText("Select");
                       // }
                    }
                });
                // show dialog
                builder.show();

            }
        });



        Log.d("dmkidjio", "onReceive: " + getIntent().getStringExtra("lastvisit"));




        /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
*/

        for (int a = 0; a < hospitalLists.size(); a++) {
            if (hospitalLists.get(a).getnHfId().equals(getIntent().getStringExtra("hf_id"))) {
                hospitalList = hospitalLists.get(a);
            }
        }

        date.setText(curFormater.format(currentTime));
        hospitalName.setText(hospitalList.getcHfNam());
        hospitalAddress.setText(hospitalList.getcHfAddr());
        hospitalType.setText(hospitalList.getcHfTyp());
        String ext = "";
        if (getIntent().getStringExtra("lastvisit") != null) {
            if (Integer.valueOf(getIntent().getStringExtra("lastvisit")) < 2) {
                ext = "Day";
            } else {
                ext = "Days";
            }
            date.setText(date.getText().toString()+String.format("(%s)",getIntent().getStringExtra("lastvisit") + " " + ext));
        }
        BaseUtils.putGlobalHfIdProvider(ProviderEngagement.this, getIntent().getStringExtra("hf_id"));
        BaseUtils.putGlobalDocIdProvider(ProviderEngagement.this, getIntent().getStringExtra("doc_id"));

        if (BaseUtils.getGlobalHfIdProvider(this).equals(BaseUtils.getSelectedGlobalHfIdProvider(this))) {
            getRoomPrevVisits();

        }

        getLocation();
        NetworkCalls.getPurpose(this);
        getPrevVisits();
        bt_proceedone.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localPurpose")) {
                purposeVisit = BaseUtils.getPurpose(ProviderEngagement.this);
                for (FormOneData specimen : purposeVisit) {
                    Log.d("koop", "onReceive: " + specimen.getC_val());
                    purposeStrings.add(specimen.getC_val());
                }

                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
              //  setSpinnerAdapter(purposeOfVisit, purposeStrings);
            }
        }
    };
    private BroadcastReceiver broadcastReceiverOne = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("checked")) {
                if (intent.getBooleanExtra("checked", false)){
                    selectedIds.clear();
                    visitids.clear();
                    visitnames.clear();
                    // clear text view value
                    purposeOfVisit.setText("Select");
                }

                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
              //  setSpinnerAdapter(purposeOfVisit, purposeStrings);
            }
        }
    };

    private void setSpinnerAdapter(MultiSpinner spinner, List<String> values) {
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(this, values);
        spinner.setAdapterWithOutImage(this, values, new MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener() {
            @Override
            public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
                //This is where you get all your items selected from the Multi Selection Spinner :)
                for (int i = 0; i < chosenItems.size(); i++) {
                    Log.e("chosenItems", chosenItems.get(i));
                    visitids.add(purposeVisit.get(/*purposeOfVisit.getSelectedItemPosition() - 1*/i).getId());
                    visitnames.add(purposeVisit.get(/*purposeOfVisit.getSelectedItemPosition() - 1*/i).getC_val());
                  //  selectedIds.add(i);
                }


            }
        });
    }

    private Boolean postProvider() {
       /* Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
   /*     if (!BaseUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();

            return;
        }

        RequestBody n_st_id = RequestBody.create(hospitalList.getnStId(), MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(hospitalList.getnDisId(), MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(hospitalList.getnTuId(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(hospitalList.getnHfId(), MediaType.parse("text/plain"));
        RequestBody typ = RequestBody.create(hospitalList.getnHfTypId(), MediaType.parse("text/plain"));
        RequestBody d_visit = RequestBody.create(curFormater.format(currentTime), MediaType.parse("text/plain"));
        RequestBody n_visit_id = RequestBody.create(purposeVisit.get(purposeOfVisit.getSelectedItemPosition() - 1).getId(), MediaType.parse("text/plain"));
        RequestBody n_sac_id = RequestBody.create(BaseUtils.getUserInfo(this).getnUserLevel(), MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(this).getId(), MediaType.parse("text/plain"));
*/

        Boolean notificationCollectionPresent = false;
        int instance = 0;
        postDateProvider(
                ProviderEngagement.this,
                hospitalList.getnStId(),
                hospitalList.getnDisId(),
                hospitalList.getnTuId(),
                hospitalList.getnHfId(),
                hospitalList.getnHfTypId(),
                curFormater1.format(currentTime),
                wayLatitude,
                wayLongitude,
                //visitids.get(j),
                //  BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).get(i).getIdd(),
                BaseUtils.getUserInfo(this).getnUserLevel(),
                BaseUtils.getUserInfo(this).getId(),
                getSupportFragmentManager()

        );
        for (int i = 0; i < BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).size(); i++) {

           // if (visitids.size() > 1) {
                for (int j = 0; j < visitids.size(); j++) {
                    Boolean last = false;

                    if(visitnames.get(j).equals("Notification Collection")){
                        notificationCollectionPresent = true;
                        instance = instance + 1;

                        if (instance == 1){
                            last = true;
                        }else {
                            last = false;
                        }


                    }

                    if (j == (visitids.size() - 1)){
                        if (notificationCollectionPresent == false){
                            last = true;
                        }

                    }

                    Date currentTime = Calendar.getInstance().getTime();
                    //BaseUtils.putSubmitProviderForm(context, "false");
                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





                   /* NetworkCalls.postMultipleDoctorsProvider(
                            ProviderEngagement.this,
                            wayLatitude,
                            wayLongitude,
                            visitids.get(j),
                            BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).get(i).getIdd(),
                            BaseUtils.getUserInfo(this).getnUserLevel(),
                            BaseUtils.getUserInfo(this).getId(),
                            getSupportFragmentManager(),
                            last
                    );
                    NetworkCalls.postMultipleVisitsProvider(
                            ProviderEngagement.this,
                            wayLatitude,
                            wayLongitude,
                            visitids.get(j),
                            visitnames.get(j),
                            hospitalList.getnHfTypId(),
                            *//*providerEngagementViewModel.currentSelectedDoctorsList().get(i).getIdd(),*//*
                            BaseUtils.getUserInfo(this).getnUserLevel(),
                            BaseUtils.getUserInfo(this).getId(),
                            getSupportFragmentManager(),
                            last
                    );
*/

                }
          /*  } else {
                NetworkCalls.postMultipleDoctorsProvider(
                        ProviderEngagement.this,
                        wayLatitude,
                        wayLongitude,
                        visitids.get(0),
                        providerEngagementViewModel.currentSelectedDoctorsList().get(i).getIdd(),
                        BaseUtils.getUserInfo(this).getnUserLevel(),
                        BaseUtils.getUserInfo(this).getId(),
                        getSupportFragmentManager(),
                        true
                );
            }*/

            if (i == BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).size()) {
                return true;
            }
        }

        /*if (visitids.size() < 2) {
            NetworkCalls.postProvider(
                    ProviderEngagement.this,
                    hospitalList.getnStId(),
                    hospitalList.getnDisId(),
                    hospitalList.getnTuId(),
                    hospitalList.getnHfId(),
                    hospitalList.getnHfTypId(),
                    curFormater.format(currentTime),
                    visitids.get(0),
                    visitids.get(0),
                    BaseUtils.getUserInfo(this).getnUserLevel(),
                    BaseUtils.getUserInfo(this).getId(),
                    getSupportFragmentManager(),
                    true
            );
        }*/
        return false;
    }

    private void getPrevVisits() {
        if (!BaseUtils.isNetworkAvailable(this)) {
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_visit_typ&w=n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id");
        ApiClient.getClient().getPrevVisits(url).enqueue(new Callback<PrevVisitsResponse>() {
            @Override
            public void onResponse(Call<PrevVisitsResponse> call, Response<PrevVisitsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        dataBase.customerDao().deletePrevVisitProviderFromRoom();
                        for (int i = 0; i < response.body().getUserData().size(); i++) {
                            RoomPrevVisitsData roomPreviousVisits = new RoomPrevVisitsData(
                                    response.body().getUserData().get(i).getdVisit(),
                                    response.body().getUserData().get(i).getcVal(),
                                    response.body().getUserData().get(i).getnHfId()

                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousVisits.getcVal());

                            dataBase.customerDao().getPrevVisitsProviderFromServer(roomPreviousVisits);
                        }
                        getRoomPrevVisits();

                    }
                }
            }

            @Override
            public void onFailure(Call<PrevVisitsResponse> call, Throwable t) {

            }
        });
    }

    private void getRoomPrevVisits() {
        LiveData<List<RoomPrevVisitsData>> roomPrevVisitProviderFromRoom = dataBase.customerDao().getSelectedRoomPrevVisitProviderFromRoom();
        roomPrevVisitProviderFromRoom.observe(ProviderEngagement.this, roomPreviousVisits1 -> {
            parentDataPreviousSamples = roomPreviousVisits1;

            //getting difference in days
            Date date = null;
            Date dateOne = null;
            try {
                // date = curFormater.parse(parentDataPreviousSamples.get(1).getdVisit());
                String formattedDate = "";
                if (!parentDataPreviousSamples.isEmpty()) {
                    for (int i = 0; i < parentDataPreviousSamples.get(0).getdVisit().length(); i++) {
                        if (i < 10) {
                            formattedDate = formattedDate + parentDataPreviousSamples.get(0).getdVisit().charAt(i);
                        }
                    }
                }


                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                dateOne = curFormater.parse(modifyDateLayout(formattedDate));
                date = curFormater.parse(curFormater.format(currentTime));
                long diffInMillies = Math.abs(dateOne.getTime() - date.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                String ext = "";
                if (diff < 2) {
                    ext = "Day";
                } else {
                    ext = "Days";
                }
                //lastVisit.setText(String.valueOf(diff+" "+ext));
                Log.d("bhdyigd", "init: " + curFormater.format(currentTime));
                Log.d("bhdyigd", "init: " + formattedDate);
                Log.d("bhdyigd", "init: " + diff);


            } catch (ParseException e) {
                Log.d("bhdyigd", "init: " + "failll");
                e.printStackTrace();
            }
            setRecycler();
        });
    }

    private String modifyDateLayout(String inputDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void setRecycler() {
        prevVisits.setLayoutManager(new LinearLayoutManager(ProviderEngagement.this));
        prevVisits.setAdapter(new PrevVisitsAdapter(parentDataPreviousSamples));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_proceedone:
                if (visitids.size() == 0) {
                    BaseUtils.showToast(this, "Please select purpose");
                } else {

                    postProvider();

                }
                break;
            case R.id.backbtn:
                super.onBackPressed();
        }
    }
    public void postDateProvider(
            Context context,
            String n_st_idd,
            String n_dis_idd,
            String n_tu_idd,
            String n_hf_idd,
            String typp,
            String d_visitt,
            String n_lat,
            String n_lng,
            String n_sac_idd,
            String n_user_idd,
            FragmentManager fragmentManager
    ) {
        //BaseUtils.putProviderData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, typp, d_visitt, n_visit_idd, n_sac_idd, n_user_idd);


        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            //Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();

            //dataBase.customerDao().insertPostProvider(roomPostProvider);
            // BaseUtils.putSubmitProviderForm(context, "false");
           /* if (navigate) {
                // Toast.makeText(context, "Data will be submitted when back online", Toast.LENGTH_SHORT).show();
                //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                *//*if (n_visit_name.equals("Notification Collection")) {
                    NotificationFagment fragment = new NotificationFagment();
                    fragment.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                    bundle.putString("doc_id",  BaseUtils.getGlobalDocIdProvider(context));
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "providerfrag");
                } else {
                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }*//*
                //  ((Activity) context).finish();
            } else {
                // Toast.makeText(context, "Data will be submitted when back online", Toast.LENGTH_SHORT).show();
                //return;
            }*/
            progressDialog.hideProgressBar();
            return;
        }

        RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));
        RequestBody typ = RequestBody.create(typp, MediaType.parse("text/plain"));
        //  RequestBody d_visit = RequestBody.create(d_visitt, MediaType.parse("text/plain"));
        RequestBody d_visit = RequestBody.create(d_visitt, MediaType.parse("text/plain"));
        //  RequestBody n_doc_idd = RequestBody.create(n_doc_id, MediaType.parse("text/plain"));
        RequestBody n_sac_id = RequestBody.create(n_sac_idd, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
        RequestBody n_latt = RequestBody.create(n_lat, MediaType.parse("text/plain"));
        RequestBody n_lngg = RequestBody.create(n_lng, MediaType.parse("text/plain"));

        ApiClient.getClient().postMultipleDateProvider(n_st_id,
                n_dis_id, n_tu_id, n_hf_id, typ, d_visit, n_user_id, n_latt, n_lngg, n_sac_id
        ).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Log.d("provider_engagement0987","date");
                        //  BaseUtils.showToast(context, "Submitted");
                        //    dataBase.customerDao().deletePostProvider(roomPostProvider);
                        for (int i = 0; i < BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).size(); i++) {
                            boolean last = false;
                            if (i==BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).size()-1){
                                last = true;
                            }
                            postMultipleDoctorsProvider(
                                    ProviderEngagement.this,
                                    wayLatitude,
                                    wayLongitude,
                                    response.body().getUserData(),
                                    BaseUtils.getSavedSelectedDoctors(ProviderEngagement.this).get(i).getIdd(),
                                    BaseUtils.getUserInfo(ProviderEngagement.this).getnUserLevel(),
                                    BaseUtils.getUserInfo(ProviderEngagement.this).getId(),
                                    getSupportFragmentManager(),
                                    last
                            );
                        }
                        BaseUtils.putSubmitProviderForm(context, "true");
                        BaseUtils.putSelectedGlobalHfIdProvider(context, BaseUtils.getGlobalHfIdProvider(context));
                        /*if (navigate) {
                            //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                           *//* if (n_visit_name.equals("Notification Collection")) {
                                NotificationFagment fragment = new NotificationFagment();
                                fragment.setCancelable(false);
                                Bundle bundle = new Bundle();
                                bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                                //bundle.putString("doc_id", getIntent().getStringExtra("doc_id"));
                                fragment.setArguments(bundle);
                                fragment.show(fragmentManager, "providerfrag");
                            } else {
                                ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }*//*
                        }*/
                    } else {
                        progressDialog.hideProgressBar();
                        //    BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }else{
                    progressDialog.hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                // BaseUtils.putSubmitProviderForm(context, "false");
                progressDialog.hideProgressBar();
            }
        });
    }

    public void postMultipleDoctorsProvider(
            Context context,
            String n_lat,
            String n_lng,
            String n_visit_idd,
            String n_doc_id,
            String n_sac_idd,
            String n_user_idd,
            FragmentManager fragmentManager,
            Boolean navigate
    ) {
        //BaseUtils.putProviderData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, typp, d_visitt, n_visit_idd, n_sac_idd, n_user_idd);

        Date currentTime = Calendar.getInstance().getTime();
        //BaseUtils.putSubmitProviderForm(context, "false");
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            //Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();

            //dataBase.customerDao().insertPostProvider(roomPostProvider);
            // BaseUtils.putSubmitProviderForm(context, "false");
          /*  if (navigate) {
                Toast.makeText(context, "Data will be submitted when back online", Toast.LENGTH_SHORT).show();
                //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                *//*if (n_visit_name.equals("Notification Collection")) {
                    NotificationFagment fragment = new NotificationFagment();
                    fragment.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                    bundle.putString("doc_id",  BaseUtils.getGlobalDocIdProvider(context));
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "providerfrag");
                } else {
                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }*//*
                //  ((Activity) context).finish();
            } else {
                Toast.makeText(context, "Data will be submitted when back online", Toast.LENGTH_SHORT).show();
                //return;
            }*/
            return;
        }

  /*      RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));*/
     /*   RequestBody typ = RequestBody.create(typp, MediaType.parse("text/plain"));
        RequestBody d_visit = RequestBody.create(d_visitt, MediaType.parse("text/plain"));*/
        RequestBody n_visit_id = RequestBody.create(n_visit_idd, MediaType.parse("text/plain"));
        RequestBody n_doc_idd = RequestBody.create(n_doc_id, MediaType.parse("text/plain"));
        RequestBody n_sac_id = RequestBody.create(n_sac_idd, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
        RequestBody n_latt = RequestBody.create(n_lat, MediaType.parse("text/plain"));
        RequestBody n_lngg = RequestBody.create(n_lng, MediaType.parse("text/plain"));

        ApiClient.getClient().postMultipleDoctorsProvider(n_visit_id, n_sac_id, n_user_id, n_latt, n_lngg, n_doc_idd).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Log.d("provider_engagement0987","doc");
                        //BaseUtils.showToast(context, "Submitted");
                        //    dataBase.customerDao().deletePostProvider(roomPostProvider);
                        BaseUtils.putSubmitProviderForm(context, "true");
                        BaseUtils.putSelectedGlobalHfIdProvider(context, BaseUtils.getGlobalHfIdProvider(context));
                        if (navigate){
                            for (int j = 0; j < visitids.size(); j++) {
                                boolean b = false;
                                if (j==visitids.size()-1){
                                    b = true;
                                }
                                postMultipleVisitsProvider(
                                        ProviderEngagement.this,
                                        wayLatitude,
                                        wayLongitude,
                                        n_visit_idd,
                                        visitnames.get(j),
                                        visitids.get(j),
                                        /*providerEngagementViewModel.currentSelectedDoctorsList().get(i).getIdd(),*/
                                        BaseUtils.getUserInfo(ProviderEngagement.this).getnUserLevel(),
                                        BaseUtils.getUserInfo(ProviderEngagement.this).getId(),
                                        getSupportFragmentManager(),
                                         b
                                );
                            }
                        }
                        /*if (navigate) {
                            //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                            if (n_visit_name.equals("Notification Collection")) {
                                NotificationFagment fragment = new NotificationFagment();
                                fragment.setCancelable(false);
                                Bundle bundle = new Bundle();
                                bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                                //bundle.putString("doc_id", getIntent().getStringExtra("doc_id"));
                                fragment.setArguments(bundle);
                                fragment.show(fragmentManager, "providerfrag");
                            } else {
                                ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }*/
                    } else {
                        progressDialog.hideProgressBar();
                        // BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }else{
                    progressDialog.hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                //   BaseUtils.putSubmitProviderForm(context, "false");
            }
        });
    }

    public  void postMultipleVisitsProvider(
            Context context,
            String n_lat,
            String n_lng,
            String n_visit_idd,
            String n_visit_name,
            String n_typ,
            String n_sac_idd,
            String n_user_idd,
            FragmentManager fragmentManager,
            Boolean navigate
    ) {
        //BaseUtils.putProviderData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, typp, d_visitt, n_visit_idd, n_sac_idd, n_user_idd);

        Date currentTime = Calendar.getInstance().getTime();
        //BaseUtils.putSubmitProviderForm(context, "false");
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            BaseUtils.showToast(ProviderEngagement.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            //dataBase.customerDao().insertPostProvider(roomPostProvider);
            // BaseUtils.putSubmitProviderForm(context, "false");
            if (navigate) {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                if (visitnames.contains("Notification Collection")) {
                    NotificationFagment fragment = new NotificationFagment();
                    fragment.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                    bundle.putString("doc_id", BaseUtils.getGlobalDocIdProvider(context));

                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "providerfrag");
                    Log.d("notificationCollection","shobhit");
                } else {
                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                //  ((Activity) context).finish();
            } else {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                //return;
            }
            return;
        }

  /*      RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));*/
     /*   RequestBody typ = RequestBody.create(typp, MediaType.parse("text/plain"));
        RequestBody d_visit = RequestBody.create(d_visitt, MediaType.parse("text/plain"));*/
        RequestBody n_visit_id = RequestBody.create(n_visit_idd, MediaType.parse("text/plain"));
        RequestBody n_typp = RequestBody.create(n_typ, MediaType.parse("text/plain"));
        RequestBody n_sac_id = RequestBody.create(n_sac_idd, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
        RequestBody n_latt = RequestBody.create(n_lat, MediaType.parse("text/plain"));
        RequestBody n_lngg = RequestBody.create(n_lng, MediaType.parse("text/plain"));

        ApiClient.getClient().postMultipleVisitsProvider(n_visit_id, n_typp, n_sac_id, n_user_id, n_latt, n_lngg).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Log.d("provider_engagement0987","visits");
                        //    dataBase.customerDao().deletePostProvider(roomPostProvider);
                        BaseUtils.putSubmitProviderForm(context, "true");
                        BaseUtils.putSelectedGlobalHfIdProvider(context, BaseUtils.getGlobalHfIdProvider(context));
                        if (navigate) {
                            BaseUtils.showToast(context, response.body().getMessage());
                            Intent intent = new Intent();
                            intent.setAction("provider");
                            intent.putExtra("checked", true);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                            if (visitnames.contains("Notification Collection")) {
                                NotificationFagment fragment = new NotificationFagment();
                                fragment.setCancelable(false);
                                Bundle bundle = new Bundle();
                                bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                                bundle.putString("shobhit","shobhit");
                                //bundle.putString("doc_id", getIntent().getStringExtra("doc_id"));
                                fragment.setArguments(bundle);
                                Log.d("notificationCollection","shobhit fragment");
                                fragment.show(fragmentManager, "providerfrag");
                            } else {
                                ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }
                    } else {
                        // BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                BaseUtils.putSubmitProviderForm(context, "false");
            }
        });
    }

}