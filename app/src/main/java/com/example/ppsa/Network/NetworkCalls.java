package com.example.ppsa.Network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ppsa.AddHospitalFacilty;
import com.example.ppsa.BaseUtils;
import com.example.ppsa.Dao.AppDataBase;
import com.example.ppsa.GlobalProgressDialog;
import com.example.ppsa.HospitalFacility;
import com.example.ppsa.HospitalsList;
import com.example.ppsa.LogIn;
import com.example.ppsa.Manager;
import com.example.ppsa.Response.AddDocResponse;
import com.example.ppsa.Response.AllUserResponse;
import com.example.ppsa.Response.DoctorsList;
import com.example.ppsa.Response.DoctorsResponse;
import com.example.ppsa.Response.FormOneResponse;
import com.example.ppsa.Response.HospitalList;
import com.example.ppsa.Response.HospitalResponse;
import com.example.ppsa.Response.QualificationList;
import com.example.ppsa.Response.QualificationResponse;
import com.example.ppsa.Response.RoomDoctorsList;
import com.example.ppsa.Response.UserInfoList;
import com.example.ppsa.Response.UserInfoResponse;
import com.example.ppsa.Response.UserList;
import com.example.ppsa.WorkerForm;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCalls {

    //    private static AppDataBase dataBase;
    private static ApiClient.APIInterface apiInterface;
    private static UserList allUser;
    @SuppressLint("StaticFieldLeak")
    private static GlobalProgressDialog progressDialog;
    private static UserInfoList userInfoList;
    private static List<HospitalList> hospitalLists = new ArrayList<>();
    private static List<DoctorsList> doctorsData = new ArrayList<>();
    private static List<QualificationList> qualificationLists = new ArrayList<>();
    private static List<QualificationList> qualificationListspe = new ArrayList<>();
    private static List<QualificationList> hfTypeLIsts = new ArrayList<>();
    private static List<QualificationList> scopeLists = new ArrayList<>();
    private static List<QualificationList> benefeciaryList = new ArrayList<>();
    private static AppDataBase dataBase;

    public static void getUserData(LogIn context, String phoneNumber) {

        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            return;
        }
        apiInterface.getAllUsers("c_MOBILE=" + phoneNumber).enqueue(new Callback<AllUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllUserResponse> call, @NotNull Response<AllUserResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        allUser = response.body().getUserData().get(0);
                        BaseUtils.saveUserInfo(context, allUser);
                        BaseUtils.putUserLogIn(context, true);

                            context.startActivity(new Intent(context, WorkerForm.class));
                            context.finish();


                        getUserOtherData(context,allUser.getnUserLevel());
                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<AllUserResponse> call, @NotNull Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    public static void getUserOtherData(Context context, String id) {

        apiInterface = ApiClient.getClient();

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            return;
        }
        apiInterface.getUsersInfo("oth_staff_id=" + id).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        userInfoList = response.body().getUserData().get(0);
                        BaseUtils.saveUserOtherInfo(context, userInfoList);
                        BaseUtils.putUserLogIn(context, true);


                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, @NotNull Throwable t) {
            }
        });
    }

    public static void getHospitalData(Context context) {

        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));

            return;
        }

        apiInterface.getHospitalList("oth_staff_id="+BaseUtils.getUserInfo(context).getnUserLevel()).enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, @NotNull Response<HospitalResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        hospitalLists = response.body().getUserData();
                        BaseUtils.saveHospitalList(context, hospitalLists);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));

                        hideProgress(progressDialog);


                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
                        progressDialog.hideProgressBar();
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
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

    public static void getDocData(Context context, String hfID) {
        progressDialog = new GlobalProgressDialog(context);
        dataBase = AppDataBase.getDatabase(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("doc").putExtra("localDocData", ""));

            progressDialog.hideProgressBar();
            return;
        }
        ApiClient.getClient().getDoctorsList("n_hf_id=" + hfID).enqueue(new Callback<DoctorsResponse>() {
            @Override
            public void onResponse(Call<DoctorsResponse> call, Response<DoctorsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        doctorsData = response.body().getUserData();
                        for(int a = 0;a < doctorsData.size();a++){
                            RoomDoctorsList roomDoctorsList = new RoomDoctorsList(doctorsData.get(a).getcDocNam(),
                                    doctorsData.get(a).getcQualf(),
                                    doctorsData.get(a).getcQual(),
                                    doctorsData.get(a).getcMob(),
                                    doctorsData.get(a).getnHfId(),
                                    doctorsData.get(a).getId());
                            dataBase.customerDao().getDoctorsFromServer(roomDoctorsList);
                        }
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("doc").putExtra("notifyDocAdapter", ""));
                        hideProgress(progressDialog);

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("doc").putExtra("localDocData", ""));
                        progressDialog.hideProgressBar();
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("doc").putExtra("localDocData", ""));
                    progressDialog.hideProgressBar();
                }

            }

            @Override
            public void onFailure(Call<DoctorsResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("doc").putExtra("localDocData", ""));
            }
        });
    }

    public static void getQualData(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualData", ""));

            return;
        }
        ApiClient.getClient().getQualificationList().enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        qualificationLists = response.body().getUserData();
                        BaseUtils.saveQualList(context, qualificationLists);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("notifyqualAdapter", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualData", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualData", ""));
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualData", ""));
            }
        });
    }

    public static void getQualDataspe(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualspeData", ""));

            return;
        }
        ApiClient.getClient().getQualificationSpeList().enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        qualificationListspe = response.body().getUserData();
                        BaseUtils.saveQualSpeList(context, qualificationListspe);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("notifyqualspeAdapter", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualspeData", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualspeData", ""));
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualspeData", ""));            }
        });
    }

    public static void getHFType(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localhfTypeData", ""));

            return;
        }
        ApiClient.getClient().getHFType().enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        hfTypeLIsts = response.body().getUserData();
                        BaseUtils.savehfTypeList(context, hfTypeLIsts);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyhfTypeAdapter", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localhfTypeData", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localhfTypeData", ""));
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localhfTypeData", ""));
            }
        });
    }
    public static void getScope(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));

            return;
        }
        ApiClient.getClient().getScope().enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        scopeLists = response.body().getUserData();
                        BaseUtils.saveScopeList(context, scopeLists);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyScopeAdapter", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));            }
        });
    }
    public static void getBenefeciary(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));

            return;
        }
        ApiClient.getClient().getBenefeciary().enqueue(new Callback<QualificationResponse>() {
            @Override
            public void onResponse(Call<QualificationResponse> call, Response<QualificationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        benefeciaryList = response.body().getUserData();
                        BaseUtils.saveBenefeciaryList(context, benefeciaryList);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));            }
        });
    }
    public static void hospitalSync(Activity context, String hfID,boolean reloadList){
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(context).getnUserLevel(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(hfID,MediaType.parse("text/plain"));
        RequestBody n_pm_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getnPmSancId(),MediaType.parse("text/plain"));
        RequestBody n_pc_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getnPcSancId(),MediaType.parse("text/plain"));
        RequestBody n_sfta_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getnStfSancId(),MediaType.parse("text/plain"));
        RequestBody n_staff_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getN_staff_sanc(),MediaType.parse("text/plain"));

        ApiClient.getClient().hospitalSync(n_hf_id,n_pm_id,n_pc_id,n_sfta_id,n_staff_id,n_user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()) {
                        if (reloadList){
                            NetworkCalls.getHospitalData(context);
                        }else{
                            context.startActivity(new Intent(context, HospitalsList.class));
                            context.finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

            }
        });


    }
    private static void hideProgress(GlobalProgressDialog progressDialog){
        final android.os.Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                //Do something after 1000ms
                progressDialog.hideProgressBar();
            }
        }, 500);
    }
    public static void getGender(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localGender", ""));

            return;
        }
        ApiClient.getClient().getFormGender().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveGender(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyGender", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localGender", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localGender", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localGender", ""));            }
        });
    }
    public static void getState(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localState", ""));

            return;
        }
        ApiClient.getClient().getFormState().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveState(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localState", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localState", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localState", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localState", ""));            }
        });
    }
    public static void getDistrict(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));

            return;
        }
        ApiClient.getClient().getFormDistrict().enqueue(new Callback<FormOneResponse>() {
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));            }
        });
    }

    public static void getTU(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

            return;
        }
        ApiClient.getClient().getFormTU().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveTU(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));            }
        });
    }

    public static void getSpecimen(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localspecimen", ""));

            return;
        }
        ApiClient.getClient().getSpecimen().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveSpecimen(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localspecimen", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localspecimen", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localspecimen", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localspecimen", ""));            }
        });
    }
    public static void getTesting(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtesting", ""));

            return;
        }
        ApiClient.getClient().getTesting().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveTesting(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtesting", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtesting", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtesting", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtesting", ""));            }
        });
    }
    public static void getExtraction(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));

            return;
        }
        ApiClient.getClient().getExtraction().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveExtraction(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));            }
        });
    }
    public static void getType(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtype", ""));

            return;
        }
        ApiClient.getClient().getType().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.savetype(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtype", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtype", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtype", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtype", ""));            }
        });
    }
}