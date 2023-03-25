package com.smit.ppsa.Network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.smit.ppsa.BaseUtils;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.GlobalProgressDialog;
import com.smit.ppsa.HospitalFacility;
import com.smit.ppsa.LogIn;
import com.smit.ppsa.LoginViewModel;
import com.smit.ppsa.MainActivity;
import com.smit.ppsa.NotificationFagment;
import com.smit.ppsa.PasswordActivity;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.AllUserResponse;
import com.smit.ppsa.Response.AttendeceResponse;
import com.smit.ppsa.Response.AttendenceData;
import com.smit.ppsa.Response.AttendenceListResponse;
import com.smit.ppsa.Response.CollectedBy.CollectedByResponse;
import com.smit.ppsa.Response.CollectedFrom.CollectedFromSamplesRes;
import com.smit.ppsa.Response.DiagnosticTest.DiagnosticTestRes;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.DoctorsList;
import com.smit.ppsa.Response.DoctorsResponse;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.FormOneModel;
import com.smit.ppsa.Response.FormOneResponse;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.HospitalModel;
import com.smit.ppsa.Response.HospitalResponse;
import com.smit.ppsa.Response.QualificationList;
import com.smit.ppsa.Response.QualificationResponse;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.Response.RegisterParentResponse;
import com.smit.ppsa.Response.RoomDoctorsList;
import com.smit.ppsa.Response.UserInfoList;
import com.smit.ppsa.Response.UserInfoResponse;
import com.smit.ppsa.Response.UserList;
import com.smit.ppsa.Response.noOfCont.NoOfContResponse;
import com.smit.ppsa.Response.pythologylab.LabTypeResponse;
import com.smit.ppsa.Response.pythologylab.PythologyLabResponse;
import com.smit.ppsa.WorkerForm;
import com.smit.ppsa.commons;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private static List<FormOneData> TuList = new ArrayList<>();
    private static List<DoctorsList> doctorsData = new ArrayList<>();
    private static List<QualificationList> qualificationLists = new ArrayList<>();
    private static List<QualificationList> qualificationListspe = new ArrayList<>();
    private static List<QualificationList> hfTypeLIsts = new ArrayList<>();
    private static List<QualificationList> scopeLists = new ArrayList<>();
    private static List<QualificationList> benefeciaryList = new ArrayList<>();
    private static List<RegisterParentData> registerParentData = new ArrayList<>();
    private static AppDataBase dataBase;

    public static void getUserData(LogIn context, String phoneNumber, String password, LoginViewModel viewModel, Boolean call) {

        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        Log.d("djuyehdu", "getUserData: " + phoneNumber);
        apiInterface.getAllUsers("c_MOBILE=" + phoneNumber).enqueue(new Callback<AllUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllUserResponse> call, @NotNull Response<AllUserResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {

                        allUser = response.body().getUserData().get(0);
                        BaseUtils.saveUserInfo(context, allUser);

                        //  BaseUtils.showToast(context,allUser.getN_staff_sanc());
                        Log.d("userInfo", allUser.toString());
                        BaseUtils.putUsername(context, phoneNumber);
                        // BaseUtils.putUserLogIn(context, true, phoneNumber);
                        if (password.equals("pass@123")) {
                            context.startActivity(new Intent(context, PasswordActivity.class)
                                    .putExtra("type", "newUser"));
                        } else {
                            //submitUserData(context, allUser.getId(), password);
                            String passwordHash = new commons().md5(password);
                            if (passwordHash.equals(BaseUtils.getUserInfo(context).getcPassword())) {
                                //getUserOtherData(context, allUser.getnUserLevel());
                                BaseUtils.putUserLogIn(context, true);
                                getAttendenceLogin(context);
                            } else {
                                BaseUtils.showToast(context, "password is incorrect");
                            }


                            // viewModel.submitPassword(Integer.valueOf(allUser.getId()), passwordHash, "", context, null, "normal");
                     /*   context.startActivity(new Intent(context, WorkerForm.class));
                        context.finish();*/


                        }

                    } else {
                        BaseUtils.showToast(context, "user not found");
                    }
                } else {
                    BaseUtils.showToast(context, "user not found");
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<AllUserResponse> call, @NotNull Throwable t) {
                BaseUtils.showToast(context, "user not found");

                progressDialog.hideProgressBar();
            }
        });
    }

    private static void getAttendenceLogin(LogIn context) {
        Date currentTime = Calendar.getInstance().getTime();
        progressDialog = new GlobalProgressDialog(context);
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        String date = curFormater.format(currentTime);
        SimpleDateFormat currFormater = new SimpleDateFormat("yyyy-MM-dd");
        String simpdate = currFormater.format(currentTime);
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_t_attend&w=Year(d_rpt)%3C%3CEQUALTO%3E%3E" + date.split("/")[2] + "%3C%3CAND%3E%3EMonth(d_rpt)%3C%3CEQUALTO%3E%3E" + date.split("/")[1] + "%3C%3CAND%3E%3EDay(d_rpt)%3C%3CEQUALTO%3E%3E" + date.split("/")[0] + "%3C%3CAND%3E%3En_user_id%3C%3CEQUALTO%3E%3E" + BaseUtils.getUserInfo(context).getId();
        ApiClient.getClient().getAttendence(url).enqueue(new Callback<AttendeceResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<AttendeceResponse> call, Response<AttendeceResponse> response) {
                if (response.isSuccessful()) {
                    BaseUtils.showToast(context, "logged in success");

                    if (response.body().getStatus()) {

                        BaseUtils.putUserAttendance(context, true);
                        Log.d("juguffyg", "onResponse: " + response.body().getUserData().get(0).getdCdat().toString());
                        //added for loop
                        for (int a = 0; a < response.body().getUserData().size(); a++) {
                            AttendenceData item = response.body().getUserData().get(a);
                            Log.d("kopk", "setHospitalRecycler: " + item.getdRpt());
                            String datee = "";
                            for (int b = 0; b < item.getdRpt().length(); b++) {
                                if (b < 10) {
                                    datee = datee + item.getdRpt().charAt(b);
                                }
                            }


                            Log.d("sr", "setHospitalRecycler   date:    " + simpdate.toString());
                            Log.d("sr", "setHospitalRecycler   date:    " + datee.toString());
                            if (simpdate.equals(datee)) {
                                progressDialog.hideProgressBar();
                                context.startActivity(new Intent(context, MainActivity.class));
                                context.finishAffinity();
                            }

                        }
                        //    context.startActivity(new Intent(context, MainActivity.class));
//                        attendencestatus.setText(response.body().getUserData().get(0).getnAttendTyp());
                        //  dateone.setText(response.body().getUserData().get(0).getdCdat());
//                        attendencestatus.setText(response.body().getUserData().get(0).getnAttendTyp());
                        //     attendencetype .setText(response.body().getMessage());

                    } else {
                        progressDialog.hideProgressBar();
                        context.startActivity(new Intent(context, WorkerForm.class));
                        context.finishAffinity();
                    }
                    progressDialog.hideProgressBar();
                } else {
                    BaseUtils.showToast(context, "logged in success");
                    progressDialog.hideProgressBar();
                    context.startActivity(new Intent(context, WorkerForm.class));
                    context.finishAffinity();


                }
                //  context.finish();
            }

            @Override
            public void onFailure(Call<AttendeceResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    private static void submitUserData(LogIn context, String userId, String password) {
        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            return;
        }
        String passwordHash = new commons().md5(password);

        Log.d("djuyehdu", "getUserData: " + userId);
        Log.d("djuyehdu", "getUserData: " + passwordHash);
        apiInterface.submitPassword(userId, passwordHash).enqueue(new Callback<AllUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllUserResponse> call, @NotNull Response<AllUserResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("ffrty", "onResponse: htfuyui");

                   /*     allUser = response.body().getUserData().get(0);
                        BaseUtils.saveUserInfo(context, allUser);
                        BaseUtils.putUserLogIn(context, true);
*/

                    ;

                    BaseUtils.showToast(context, "password change success");
                    progressDialog.hideProgressBar();

                    context.startActivity(new Intent(context, WorkerForm.class));
                    context.finish();


                    // getUserOtherData(context, allUser.getnUserLevel());

                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<AllUserResponse> call, @NotNull Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    public static void getUserOtherData(Context context, String id, String tu) {

        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            return;
        }
        apiInterface.getUsersInfo("oth_staff_id=" + id + "<<AND>>n_tu_id<<EQUALTO>>" + tu).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        userInfoList = response.body().getUserData().get(0);
                        BaseUtils.saveUserOtherInfo(context, userInfoList);
                        BaseUtils.putUserLogIn(context, true);

                        hideProgress(progressDialog);

                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, @NotNull Throwable t) {
                hideProgress(progressDialog);

            }
        });
    }

    public static void postTest(
            Context context,
            String n_st_idd,
            String n_dis_idd,
            String n_tu_idd,
            String n_hf_idd,
            String n_enroll_idd,
            String n_smpl_col_idd,
            String d_smpl_recdd,
            String d_rpt_docc,
            String n_diag_testt,
            String d_tst_diagg,
            String d_tst_rpt_diagg,
            String n_dstt,
            String d_tst_dstt,
            String d_tst_rpt_dstt,
            String n_oth_dstt,
            String d_tst_rpt_oth_dstt,
            String d_tst_oth_dstt,
            String n_fnl_intpp,
            String n_tst_rsultt,
            String n_case_typp,
            String n_pat_statuss,
            String n_user_idd,
            String n_latt,
            String n_lngg,
            Boolean navigate
    ) {
        BaseUtils.putSubmitTestForm(context, "false");
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            BaseUtils.putTestData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, n_enroll_idd, n_smpl_col_idd, d_smpl_recdd, d_rpt_docc, n_diag_testt, d_tst_diagg, d_tst_rpt_diagg, n_dstt, d_tst_dstt, d_tst_rpt_dstt, n_oth_dstt, d_tst_rpt_oth_dstt, d_tst_oth_dstt, n_fnl_intpp, n_tst_rsultt, n_case_typp, n_pat_statuss, n_user_idd, n_latt, n_lngg);
            if (navigate) {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                context.startActivity(new Intent(context, MainActivity.class));
                //  ((Activity) context).finish();
            }
            return;
        }

        RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));
        RequestBody n_enroll_id = RequestBody.create(n_enroll_idd, MediaType.parse("text/plain"));
        RequestBody n_smpl_col_id = RequestBody.create(n_smpl_col_idd, MediaType.parse("text/plain"));
        RequestBody d_smpl_recd = RequestBody.create(d_smpl_recdd, MediaType.parse("text/plain"));
        RequestBody d_rpt_doc = RequestBody.create(d_rpt_docc, MediaType.parse("text/plain"));
        RequestBody n_diag_test = RequestBody.create(n_diag_testt, MediaType.parse("text/plain"));
        RequestBody d_tst_diag = RequestBody.create(d_tst_diagg, MediaType.parse("text/plain"));
        RequestBody d_tst_rpt_diag = RequestBody.create(d_tst_rpt_diagg, MediaType.parse("text/plain"));
        RequestBody n_dst = RequestBody.create(n_dstt, MediaType.parse("text/plain"));
        RequestBody d_tst_dst = RequestBody.create(d_tst_dstt, MediaType.parse("text/plain"));
        RequestBody d_tst_rpt_dst = RequestBody.create(d_tst_rpt_dstt, MediaType.parse("text/plain"));
        RequestBody n_oth_dst = RequestBody.create(n_oth_dstt, MediaType.parse("text/plain"));
        RequestBody d_tst_rpt_oth_dst = RequestBody.create(d_tst_rpt_oth_dstt, MediaType.parse("text/plain"));
        RequestBody d_tst_oth_dst = RequestBody.create(d_tst_oth_dstt, MediaType.parse("text/plain"));
        RequestBody n_fnl_intp = RequestBody.create(n_fnl_intpp, MediaType.parse("text/plain"));
        RequestBody n_tst_rsult = RequestBody.create(n_tst_rsultt, MediaType.parse("text/plain"));
        RequestBody n_case_typ = RequestBody.create(n_case_typp, MediaType.parse("text/plain"));
        RequestBody n_pat_status = RequestBody.create(n_pat_statuss, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
        RequestBody n_lat = RequestBody.create(n_latt, MediaType.parse("text/plain"));
        RequestBody n_lng = RequestBody.create(n_lngg, MediaType.parse("text/plain"));

        ApiClient.getClient().postTest(n_st_id, n_dis_id, n_tu_id, n_hf_id,
                n_enroll_id, n_smpl_col_id, d_smpl_recd, d_rpt_doc, n_diag_test,
                d_tst_diag, d_tst_rpt_diag, n_dst, d_tst_dst, d_tst_rpt_dst,
                n_oth_dst, d_tst_rpt_oth_dst, d_tst_oth_dst, n_fnl_intp, n_tst_rsult,
                n_case_typ, n_pat_status, n_user_id, n_lat, n_lng).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        BaseUtils.showToast(context, "Submitted");
                        BaseUtils.putSubmitTestForm(context, "true");
                        if (navigate) {
                            ((Activity) context).finish();
                        }
                    } else {
                        BaseUtils.putSubmitTestForm(context, "false");
                    }
                } else {
                    BaseUtils.putSubmitTestForm(context, "false");
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                BaseUtils.putSubmitTestForm(context, "false");
            }
        });

    }

    public static void addSample(
            Context context,
            String n_st_idd,
            String n_dis_idd,
            String n_tu_idd,
            String n_hf_idd,
            String n_doc_idd,
            String n_enroll_idd,
            String d_specm_coll,
            String n_smpl_ext_idd,
            String n_test_reas_idd,
            String n_purp_vstt,
            String n_typ_specm_idd,
            String n_cont_smpll,
            String c_plc_samp_coll,
            String n_sputm_typ_idd,
            String n_diag_tstt,
            String n_lab_idd,
            String n_staff_infoo,
            String n_user_idd,
            Boolean navigate
    ) {
        BaseUtils.putSubmitAddSampleForm(context, "false");
        BaseUtils.putAddSampleData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, n_doc_idd, n_enroll_idd, d_specm_coll, n_smpl_ext_idd, n_test_reas_idd, n_purp_vstt, n_typ_specm_idd, n_cont_smpll, c_plc_samp_coll, n_sputm_typ_idd, n_diag_tstt, n_lab_idd, n_staff_infoo, n_user_idd);

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            //  dataBase.customerDao().insertAddSample(roomAddSample);
            if (navigate) {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                context.startActivity(new Intent(context, MainActivity.class));
                //  ((Activity) context).finish();
            }
            return;
        }
        RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));
        RequestBody n_doc_id = RequestBody.create(n_doc_idd, MediaType.parse("text/plain"));
        RequestBody n_enroll_id = RequestBody.create(n_enroll_idd, MediaType.parse("text/plain"));
        RequestBody d_specm_col = RequestBody.create(d_specm_coll, MediaType.parse("text/plain"));
        RequestBody n_smpl_ext_id = RequestBody.create(n_smpl_ext_idd, MediaType.parse("text/plain"));
        RequestBody n_test_reas_id = RequestBody.create(n_test_reas_idd, MediaType.parse("text/plain"));
        RequestBody n_purp_vst = RequestBody.create(n_purp_vstt, MediaType.parse("text/plain"));
        RequestBody n_typ_specm_id = RequestBody.create(n_typ_specm_idd, MediaType.parse("text/plain"));
        RequestBody n_cont_smpl = RequestBody.create(n_cont_smpll, MediaType.parse("text/plain"));
        RequestBody c_plc_samp_col = RequestBody.create(/*f2_placeofsamplecollection.getText().toString()*/"0", MediaType.parse("text/plain"));
        RequestBody n_sputm_typ_id = RequestBody.create(n_sputm_typ_idd, MediaType.parse("text/plain"));
        RequestBody n_diag_tst = RequestBody.create(n_diag_tstt, MediaType.parse("text/plain"));
        RequestBody n_lab_id = RequestBody.create(n_lab_idd, MediaType.parse("text/plain"));
        RequestBody n_staff_info = RequestBody.create(n_staff_infoo, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));

        ApiClient.getClient().postFormPartOne(n_st_id, n_dis_id, n_tu_id, n_hf_id, n_doc_id, n_enroll_id, d_specm_col, n_smpl_ext_id, n_test_reas_id, n_purp_vst, n_typ_specm_id, n_cont_smpl, c_plc_samp_col, n_sputm_typ_id, n_diag_tst, n_lab_id, n_staff_info, n_user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    BaseUtils.putSubmitAddSampleForm(context, "true");
                    // dataBase.customerDao().deleteAddSample(roomAddSample);
                    if (response.body().isStatus()) {
                        BaseUtils.showToast(context, "Sample submitted");
                        if (navigate) {
                            ((Activity) context).finish();
                        }
                    }
                } else {
                    BaseUtils.putSubmitAddSampleForm(context, "false");
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                BaseUtils.putSubmitAddSampleForm(context, "false");
            }
        });
    }

    public static void reasonForTesting(
            Context context,
            String n_enroll_idd,
            String n_user_idd,
            Boolean navigate,
            String date,
            String confirm
    ) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            //  dataBase.customerDao().insertAddSample(roomAddSample);
            return;
        }
        RequestBody d_diag_dt = RequestBody.create(date, MediaType.parse("text/plain"));
        RequestBody n_cfrm = RequestBody.create(confirm, MediaType.parse("text/plain"));

        ApiClient.getClient().reason(d_diag_dt, n_cfrm, n_enroll_idd, "id<<EQUALTO>>" + n_user_idd).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                } else {
                    BaseUtils.putSubmitAddSampleForm(context, "false");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                BaseUtils.putSubmitAddSampleForm(context, "false");
            }
        });
    }

    public static void submitCounselling(
            Context context,
            String user_idd,
            String staff_infoo,
            String n_stidd,
            String n_disIdd,
            String n_docIdd,
            String latt,
            String lngg,
            String n_typeCounn,
            String d_Counn,
            String neenrollIDD,
            String nTuIdd,
            String nHfidd,
            GlobalProgressDialog progressDialog,
            Boolean navigate
    ) {
        // dataBase = AppDataBase.getDatabase(context);

        /*   progressDialog = new GlobalProgressDialog(context);*/
        progressDialog.showProgressBar();
        BaseUtils.putSubmitCounsellingForm(context, "false");
        BaseUtils.putCounselingFormData(
                context,
                user_idd,
                staff_infoo,
                n_stidd,
                n_disIdd,
                n_docIdd,
                latt,
                lngg,
                n_typeCounn,
                d_Counn,
                neenrollIDD,
                nTuIdd,
                nHfidd);

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();


            //dataBase.customerDao().insertCounsellingForm(roomCounsellingData);
            if (navigate) {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                context.startActivity(new Intent(context, MainActivity.class));

            }


            return;
        }


        //   Log.d(TAG, "submitCounseling: ");


        RequestBody user_id = RequestBody.create(user_idd, MediaType.parse("text/plain"));
        RequestBody staff_info = RequestBody.create(staff_infoo, MediaType.parse("text/plain"));
        RequestBody n_stid = RequestBody.create(n_stidd, MediaType.parse("text/plain"));
        RequestBody n_disId = RequestBody.create(n_disIdd, MediaType.parse("text/plain"));
        RequestBody n_docId = RequestBody.create(n_docIdd, MediaType.parse("text/plain"));
        RequestBody lat = RequestBody.create(latt, MediaType.parse("text/plain"));
        RequestBody lng = RequestBody.create(lngg, MediaType.parse("text/plain"));
        RequestBody n_typeCoun = RequestBody.create(n_typeCounn, MediaType.parse("text/plain"));
        RequestBody d_Coun = RequestBody.create(d_Counn, MediaType.parse("text/plain"));
        RequestBody neenrollID = RequestBody.create(neenrollIDD, MediaType.parse("text/plain"));
        RequestBody nTuId = RequestBody.create(nTuIdd, MediaType.parse("text/plain"));
        RequestBody nHfid = RequestBody.create(nHfidd, MediaType.parse("text/plain"));


    /*    Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(CounsellingForm.this).getnUserLevel());

*/
        String url = "_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_couns";
        ApiClient.getClient().addCounselling(n_stid, n_disId, nTuId, nHfid, n_docId, neenrollID, d_Coun, n_typeCoun, lat, lng, staff_info, user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        //    parentData = response.body().getUser_data();

                        Log.d("gug", "onResponse: " + response.body().getMessage());
                        BaseUtils.putSubmitCounsellingForm(context, "true");
                        //    dataBase.customerDao().deleteCounsellingForm(roomCounsellingData);

                        BaseUtils.showToast(context, response.body().getMessage());
                        if (navigate) {
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((Activity) context).finishAffinity();
                        }
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                } else {
                    BaseUtils.putSubmitCounsellingForm(context, "false");
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
    }

    public static void getHospitalData(Context context, String TuId, Boolean navigate) {

        apiInterface = ApiClient.getClient();
//        progressDialog = new GlobalProgressDialog(context);
//        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            // progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));

            return;
        }
        Log.d("rerw", "onResponseIdd TU: " + TuId);
        Log.d("rerw", "onResponseIdd: Base" + BaseUtils.getUserInfo(context).getnAccessRights());

        // String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=n_tu_id<<EQUALTO>>" + TuId;
        // String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserOtherInfo(context).getN_staff_sanc() + "&tu_id=" + TuId;
        String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserInfo(context).getN_staff_sanc() + "&tu_id=" + TuId;
        //String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=5&sanc=34&tu_id=235";

        apiInterface.getHospitalList(url).enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, @NotNull Response<HospitalResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    BaseUtils.putAddHospitalForm(context, "true");
                    if (response.body().getStatus().equals("true")) {
                        hospitalLists = response.body().getUserData();
                        Log.d("lpossapo", "onResponse: " + hospitalLists.size());
                        Log.d("Hospitals Data", hospitalLists.toString());
                        BaseUtils.putSelectedTu(context, TuId);
                        BaseUtils.saveHospitalList(context, hospitalLists);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));
//                        progressDialog.hideProgressBar();
//                        hideProgress(progressDialog);


                    } else {
                        BaseUtils.saveHospitalList(context, hospitalLists);
                        Log.d("lpossapo", "error: " + response.body().getStatus() + response.body().getMessage());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
                        // progressDialog.hideProgressBar();
                    }
                } else {
                    Log.d("lpossapo", "error: " + response.errorBody().toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
                    //  progressDialog.hideProgressBar();
                }

            }

            @Override
            public void onFailure(Call<HospitalResponse> call, @NotNull Throwable t) {
                //  progressDialog.hideProgressBar();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
            }
        });
    }

    public static void getTUPatient(Context context, String TuId) {
        apiInterface = ApiClient.getClient();
//        progressDialog = new GlobalProgressDialog(context);
//        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));

            return;
        }
        // w=n_tu_id<<EQUALTO>>2<<OR>>n_tu_id<<EQUALTO>>3<<OR>>n_tu_id<<EQUALTO>>4
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_smplcol_indv_lst&w=" + TuId;
        //String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=5&sanc=34&tu_id=235";

        apiInterface.getTUPatient(url).enqueue(new Callback<RegisterParentResponse>() {
            @Override
            public void onResponse(Call<RegisterParentResponse> call, @NotNull Response<RegisterParentResponse> response) {
                if (response.isSuccessful()) {
                    //     progressDialog.hideProgressBar();

                    assert response.body() != null;
                    BaseUtils.putAddHospitalForm(context, "true");
                    if (response.body().getStatus()) {
                        registerParentData = response.body().getUserData();
                        Log.d("lpossapo", "onResponse: " + registerParentData.size());
                        BaseUtils.putSelectedTu(context, TuId);
                        BaseUtils.saveTuPatientList(context, registerParentData);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("notifyAdapter", ""));

                        //    hideProgress(progressDialog);


                    } else {
                        BaseUtils.saveHospitalList(context, hospitalLists);
                        Log.d("lpossapo", "error: " + response.body().getStatus() + response.body().getMessage());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
//                        progressDialog.hideProgressBar();
//                        hideProgress(progressDialog);

                    }
                } else {
                    Log.d("lpossapo", "error: " + response.errorBody().toString());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
//                    progressDialog.hideProgressBar();
//                    hideProgress(progressDialog);
                }

            }

            @Override
            public void onFailure(Call<RegisterParentResponse> call, @NotNull Throwable t) {
                //  progressDialog.hideProgressBar();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
            }
        });
    }

    public static void getDocData(Context context, String hfID) {
        progressDialog = new GlobalProgressDialog(context);
        dataBase = AppDataBase.getDatabase(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                        for (int a = 0; a < doctorsData.size(); a++) {
                            RoomDoctorsList roomDoctorsList = new RoomDoctorsList(doctorsData.get(a).getcDocNam(),
                                    doctorsData.get(a).getcQualf(),
                                    doctorsData.get(a).getcQual(),
                                    doctorsData.get(a).getcMob(),
                                    doctorsData.get(a).getnHfId(),
                                    doctorsData.get(a).getId(), doctorsData.get(a).getLst_vst());
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
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("qual").putExtra("localQualspeData", ""));
            }
        });
    }

    public static void getHFType(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));
                }
            }

            @Override
            public void onFailure(Call<QualificationResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localScopeData", ""));
            }
        });
    }

    public static void setAddDoc(Context context,
                                 String name,
                                 String regNum,
                                 String qualId, String specId,
                                 String mobNumber,
                                 String hospitalName,
                                 String type,
                                 String hfId, AlertDialog sDialog, Boolean navigate) {

        dataBase = AppDataBase.getDatabase(context);
        DoctorModel doctorModel = new DoctorModel(hfId,
                name,
                regNum,
                qualId,
                specId,
                mobNumber);
        BaseUtils.putAddDocForm(context, "false");
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.putAddDocForm(context, "false");
            dataBase.customerDao().insertDoctor(doctorModel);
            if (navigate) {
                sDialog.dismiss();
            }
            BaseUtils.showToast(context, "Saved in local");
            return;
        }
        RequestBody hfID = RequestBody.create(hfId, MediaType.parse("text/plain"));
        RequestBody docName = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody qualID = RequestBody.create(qualId, MediaType.parse("text/plain"));
        RequestBody specID = RequestBody.create(specId, MediaType.parse("text/plain"));
        RequestBody mobNum = RequestBody.create(mobNumber, MediaType.parse("text/number"));
        RequestBody regnum = RequestBody.create(regNum, MediaType.parse("text/number"));

        ApiClient.getClient().addDoc(hfID, docName, regnum, qualID, specID, mobNum).enqueue(new Callback<AddDocResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        BaseUtils.putAddDocForm(context, "true");
                        dataBase.customerDao().deleteDoctor(doctorModel);
                        BaseUtils.showToast(context, "success");
                        if (navigate) {
                            sDialog.dismiss();
                            context.startActivity(new Intent(context, HospitalFacility.class)
                                    .putExtra("hospitalName", hospitalName)
                                    .putExtra("type", type)
                                    .putExtra("regnum", regNum)
                                    .putExtra("hf_id", hfId));
                            ((Activity) context).finish();
                        }
                        // updateAdapter(name, qualId, specId, mobNumber);

                    }
                } else {
                    BaseUtils.putAddDocForm(context, "false");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {
                BaseUtils.putAddDocForm(context, "false");
            }
        });

    }

    public static void addHospital(
            Context context,
            String stIDd,
            String disIDd,
            String n_tu_idd,
            String n_hf_cdd,
            String c_hf_namm,
            String n_hf_typ_idd,
            String c_hf_addrr,
            String c_cont_perr,
            String c_cp_mobb,
            String c_cp_emaill,
            String n_sc_idd,
            String n_pp_idenrr,
            String c_tc_namm,
            String c_tc_mobb,
            String n_bf_idd,
            String n_pay_statuss,
            String n_user_idd,
            String latt,
            String lngg,
            Boolean navigate
    ) {
        dataBase = AppDataBase.getDatabase(context);
        BaseUtils.putAddHospitalForm(context, "false");

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.putAddHospitalForm(context, "false");
            BaseUtils.showToast(context, "Please Check your internet  Connectivity,\nsaved to local");
            HospitalModel hospitalModel = new HospitalModel(stIDd,
                    disIDd,
                    n_tu_idd,
                    n_hf_cdd,
                    c_hf_namm,
                    n_hf_typ_idd,
                    c_hf_addrr,
                    c_cont_perr,
                    c_cp_mobb,
                    c_cp_emaill,
                    n_sc_idd,
                    n_pp_idenrr,
                    c_tc_namm,
                    c_tc_mobb,
                    n_bf_idd,
                    n_pay_statuss,
                    n_user_idd,
                    latt,
                    lngg);
            dataBase.customerDao().insertHospital(hospitalModel);
            if (navigate) {
                ((Activity) context).finish();
            }
            return;
        }

        Log.d("fte4t", "addHospital: " + n_tu_idd);

        RequestBody stID = RequestBody.create(stIDd, MediaType.parse("text/plain"));
        RequestBody disID = RequestBody.create(disIDd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_cd = RequestBody.create(n_hf_cdd, MediaType.parse("text/plain"));
        RequestBody c_hf_nam = RequestBody.create(c_hf_namm, MediaType.parse("text/plain"));
        RequestBody n_hf_typ_id = RequestBody.create(n_hf_typ_idd, MediaType.parse("text/plain"));
        RequestBody c_hf_addr = RequestBody.create(c_hf_addrr, MediaType.parse("text/plain"));
        RequestBody c_cont_per = RequestBody.create(c_cont_perr, MediaType.parse("text/plain"));
        RequestBody c_cp_mob = RequestBody.create(c_cp_mobb, MediaType.parse("text/plain"));
        RequestBody c_cp_email = RequestBody.create(c_cp_emaill, MediaType.parse("text/plain"));
        RequestBody n_sc_id = RequestBody.create(n_sc_idd, MediaType.parse("text/plain"));
        RequestBody n_pp_idenr = RequestBody.create(n_pp_idenrr, MediaType.parse("text/plain"));
        RequestBody c_tc_nam = RequestBody.create(c_tc_namm, MediaType.parse("text/plain"));
        RequestBody c_tc_mob = RequestBody.create(c_tc_mobb, MediaType.parse("text/plain"));
        RequestBody n_bf_id = RequestBody.create(n_bf_idd, MediaType.parse("text/plain"));
        RequestBody n_pay_status = RequestBody.create(n_pay_statuss, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
        RequestBody lat = RequestBody.create(latt, MediaType.parse("text/plain"));
        RequestBody lng = RequestBody.create(lngg, MediaType.parse("text/plain"));


        ApiClient.getClient().addHospital(stID, disID, n_tu_id, n_hf_cd, c_hf_nam, n_hf_typ_id, c_hf_addr, c_cont_per, c_cp_mob, c_cp_email,
                n_sc_id, n_pp_idenr, c_tc_nam, c_tc_mob, n_bf_id, n_pay_status, n_user_id, lat, lng).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        BaseUtils.showToast(context, "Hospital added successful");
                        Log.d("yuygfu", "onResponse: " + BaseUtils.getUserInfo(context).getnUserLevel());
                        hospitalSync(((Activity) context), response.body().getUserData(), false, n_tu_idd, navigate);

                    }
                } else {
                    BaseUtils.putAddHospitalForm(context, "false");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {
                BaseUtils.putAddHospitalForm(context, "false");
            }
        });
    }

    /*
    c_doc_nam  varchar(200)       n_qual_id	    int(11)	   n_spec_id    int(11)    c_mob	 varchar(12) c_regno	    varchar(50)
     */
    public static void editDoctor(
            Context context,
            String c_doc_namm,
            String n_qual_idd,
            String n_spec_idd,
            String c_mobb,
            String c_regnoo,
            String hospitalId
    ) {
        String url = "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_hf_doc&w=id<<EQUALTO>>" + hospitalId;
        RequestBody c_doc_nam = RequestBody.create(c_doc_namm, MediaType.parse("text/plain"));
        RequestBody n_qual_id = RequestBody.create(n_qual_idd, MediaType.parse("text/plain"));
        RequestBody n_spec_id = RequestBody.create(n_spec_idd, MediaType.parse("text/plain"));
        RequestBody c_mob = RequestBody.create(c_mobb, MediaType.parse("text/plain"));
        RequestBody c_regno = RequestBody.create(c_regnoo, MediaType.parse("text/plain"));


        ApiClient.getClient().editDoctorApi(c_doc_nam, n_qual_id, n_spec_id, c_mob, c_regno, url).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
                if (response.isSuccessful()) {


                    BaseUtils.showToast(context, "doctor update successful");
                    Log.d("doctorAdded", "onResponse: " + response);

                } else {
                    BaseUtils.putAddHospitalForm(context, "false");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {
                BaseUtils.putAddHospitalForm(context, "false");
            }
        });


    }


//    public static void updateHospital(
//            Context context,
//            String stIDd,
//            String disIDd,
//            String n_tu_idd,
//            String n_hf_cdd,
//            String c_hf_namm,
//            String n_hf_typ_idd,
//            String c_hf_addrr,
//            String c_cont_perr,
//            String c_cp_mobb,
//            String c_cp_emaill,
//            String n_sc_idd,
//            String n_pp_idenrr,
//            String c_tc_namm,
//            String c_tc_mobb,
//            String n_bf_idd,
//            String n_pay_statuss,
//            String n_user_idd,
//            String latt,
//            String lngg,
//            Boolean navigate,
//            String hospitalId
//    ) {
//        dataBase = AppDataBase.getDatabase(context);
//        BaseUtils.putAddHospitalForm(context, "false");
//
//
//        RequestBody stID = RequestBody.create(stIDd, MediaType.parse("text/plain"));
//        RequestBody disID = RequestBody.create(disIDd, MediaType.parse("text/plain"));
//        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
//        RequestBody n_hf_cd = RequestBody.create(n_hf_cdd, MediaType.parse("text/plain"));
//        RequestBody c_hf_nam = RequestBody.create(c_hf_namm, MediaType.parse("text/plain"));
//        RequestBody n_hf_typ_id = RequestBody.create(n_hf_typ_idd, MediaType.parse("text/plain"));
//        RequestBody c_hf_addr = RequestBody.create(c_hf_addrr, MediaType.parse("text/plain"));
//        RequestBody c_cont_per = RequestBody.create(c_cont_perr, MediaType.parse("text/plain"));
//        RequestBody c_cp_mob = RequestBody.create(c_cp_mobb, MediaType.parse("text/plain"));
//        RequestBody c_cp_email = RequestBody.create(c_cp_emaill, MediaType.parse("text/plain"));
//        RequestBody n_sc_id = RequestBody.create(n_sc_idd, MediaType.parse("text/plain"));
//        RequestBody n_pp_idenr = RequestBody.create(n_pp_idenrr, MediaType.parse("text/plain"));
//        RequestBody c_tc_nam = RequestBody.create(c_tc_namm, MediaType.parse("text/plain"));
//        RequestBody c_tc_mob = RequestBody.create(c_tc_mobb, MediaType.parse("text/plain"));
//        RequestBody n_bf_id = RequestBody.create(n_bf_idd, MediaType.parse("text/plain"));
//        RequestBody n_pay_status = RequestBody.create(n_pay_statuss, MediaType.parse("text/plain"));
//        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
//        RequestBody lat = RequestBody.create(latt, MediaType.parse("text/plain"));
//        RequestBody lng = RequestBody.create(lngg, MediaType.parse("text/plain"));
//
//
//        ApiClient.getClient().updateHospital(stID, disID, n_tu_id, n_hf_cd, c_hf_nam, n_hf_typ_id, c_hf_addr, c_cont_per, c_cp_mob, c_cp_email,
//                n_sc_id, n_pp_idenr, c_tc_nam, c_tc_mob, n_bf_id, n_pay_status, n_user_id, lat, lng,).enqueue(new Callback<AddDocResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<AddDocResponse> call, @NonNull Response<AddDocResponse> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    if (response.body().isStatus()) {
//                        BaseUtils.showToast(context, "Hospital update successful");
//                        Log.d("yuygfu", "onResponse: " + BaseUtils.getUserInfo(context).getnUserLevel());
//                        hospitalSync(((Activity) context), response.body().getUserData(), false, n_tu_idd, navigate);
//                    }
//                } else {
//                    BaseUtils.putAddHospitalForm(context, "false");
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AddDocResponse> call, @NonNull Throwable t) {
//                BaseUtils.putAddHospitalForm(context, "false");
//            }
//        });
//    }

    public static void getBenefeciary(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localBenData", ""));
            }
        });
    }

    public static void hospitalSync(Activity context, String hfID, boolean reloadList, String TuId, Boolean navigate) {
        RequestBody n_user_id = RequestBody.create(BaseUtils.getUserInfo(context).getId(), MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(hfID, MediaType.parse("text/plain"));
        RequestBody n_pm_id = RequestBody.create("0", MediaType.parse("text/plain"));
        RequestBody n_pc_id = RequestBody.create("0", MediaType.parse("text/plain"));
        RequestBody n_sfta_id = RequestBody.create("0", MediaType.parse("text/plain"));
        RequestBody n_staff_id = RequestBody.create(BaseUtils.getUserInfo(context).getN_staff_sanc(), MediaType.parse("text/plain"));
        RequestBody n_st_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getnStId(), MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getnDisId(), MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(BaseUtils.getUserOtherInfo(context).getnTuId(), MediaType.parse("text/plain"));


        ApiClient.getClient().hospitalSync(n_hf_id, n_pm_id, n_pc_id, n_sfta_id, n_staff_id, n_user_id, n_st_id, n_dis_id, n_tu_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        if (reloadList) {
                            NetworkCalls.getHospitalData(context, TuId, navigate);
                        } else {
                            if (navigate) {
                                context.finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {

            }
        });


    }

    private static void hideProgress(GlobalProgressDialog progressDialog) {
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
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localGender", ""));
            }
        });
    }

    public static void getState(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localState", ""));
            }
        });
    }

    public static void getDistrict(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
            }
        });
    }

    public static void getTU(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

            return;
        }

        Log.d("ihsi", "getTU: W  " + BaseUtils.getUserInfo(context).getnAccessRights());
        Log.d("ihsi", "getTU: sanc  " + BaseUtils.getUserInfo(context).getN_staff_sanc());

        String url = "_sptu_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_tu&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserInfo(context).getN_staff_sanc();
        Log.d("ihsi", "getTU: sanc  " + url);

        ApiClient.getClient().getFormTU(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        Log.d("uhuhoy", "onResponse: " + response.body().getUserData().size());
                        BaseUtils.saveTU(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

                    } else {
                        BaseUtils.saveTU(context, TuList);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
            }
        });
    }

    public static void getSpecimen(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localspecimen", ""));
            }
        });
    }

    public static void getTesting(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtesting", ""));
            }
        });
    }

    public static void getExtraction(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));

            return;
        }
        ApiClient.getClient().getExtraction().enqueue(new Callback<CollectedByResponse>() {
            @Override
            public void onResponse(Call<CollectedByResponse> call, Response<CollectedByResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    BaseUtils.putGlobalSelectedPatientName(context, BaseUtils.getGlobalPatientName(context));
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.saveExtraction(context, response.body().getUser_data());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));
                }
            }

            @Override
            public void onFailure(Call<CollectedByResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localextract", ""));
            }
        });
    }

    public static void getSamplesFrom(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localcollectedfrom", ""));

            return;
        }
        ApiClient.getClient().getCollectedFrom().enqueue(new Callback<CollectedFromSamplesRes>() {
            @Override
            public void onResponse(Call<CollectedFromSamplesRes> call, Response<CollectedFromSamplesRes> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.saveCollectedFrom(context, response.body().getUser_data());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localcollectedfrom", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localcollectedfrom", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localcollectedfrom", ""));
                }
            }

            @Override
            public void onFailure(Call<CollectedFromSamplesRes> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localcollectedfrom", ""));
            }
        });
    }

    public static void getNoOfCont(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localnoOfCont", ""));

            return;
        }
        ApiClient.getClient().getNoOfContainers().enqueue(new Callback<NoOfContResponse>() {
            @Override
            public void onResponse(Call<NoOfContResponse> call, Response<NoOfContResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.saveNoOfCont(context, response.body().getUser_data());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localnoOfCont", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localnoOfCont", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localnoOfCont", ""));
                }
            }

            @Override
            public void onFailure(Call<NoOfContResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localnoOfCont", ""));
            }
        });
    }

    ////n_nksh_id	varchar(20)
//c_pat_nam	varchar(200)
//n_age	int(11)
//n_sex	int(11)	1-Male | 2-Female | 3-TG
//n_wght	int(11)
//n_hght	int(11)
//c_add	varchar(250)
//c_taluka	varchar(100)
//c_town	varchar(100)
//c_ward	varchar(100)
//c_lnd_mrk	varchar(150)
//n_pin	double
//n_st_id_res	int(11)
//n_dis_id_res	int(11)
//n_tu_id_res	varchar(200)
//c_mob	varchar(10)
//c_mob_2	varchar(10)
//c_not_img	varchar(50)
//c_bnk_img	varchar(50)
//d_diag_dt	date
    private static void updateNotification(Context context, String d_reg_datt, String n_nksh_idd,
                                           String c_pat_namm, String n_agee,
                                           String n_sexx, String n_wghtt,
                                           String n_hghtt,
                                           String c_addd,
                                           String c_talukaa,
                                           String c_townn,
                                           String c_wardd, String c_lnd_mrkk,
                                           String n_pinn,
                                           String n_st_id_ress, String n_dis_id_ress,
                                           String n_tu_id_ress, String c_mobbb,
                                           String c_mob_22, String c_not_imgg,
                                           String c_bnk_imgg, String d_diag_dtt, String pateintId) {
        RequestBody d_reg_dat = RequestBody.create(d_reg_datt, MediaType.parse("text/plain"));
        RequestBody n_nksh_id = RequestBody.create(n_nksh_idd, MediaType.parse("text/plain"));
        RequestBody c_pat_nam = RequestBody.create(c_pat_namm, MediaType.parse("text/plain"));
        RequestBody n_age = RequestBody.create(n_agee, MediaType.parse("text/plain"));
        RequestBody n_sex = RequestBody.create(n_sexx, MediaType.parse("text/plain"));
        RequestBody n_wght = RequestBody.create(n_wghtt, MediaType.parse("text/plain"));
        RequestBody n_hght = RequestBody.create(n_hghtt, MediaType.parse("text/plain"));
        RequestBody c_add = RequestBody.create(c_addd, MediaType.parse("text/plain"));
        RequestBody c_taluka = RequestBody.create(c_talukaa, MediaType.parse("text/plain"));
        RequestBody c_town = RequestBody.create(c_townn, MediaType.parse("text/plain"));
        RequestBody c_ward = RequestBody.create(c_wardd, MediaType.parse("text/plain"));
        RequestBody c_lnd_mrk = RequestBody.create(c_lnd_mrkk, MediaType.parse("text/plain"));
        RequestBody n_pin = RequestBody.create(n_pinn, MediaType.parse("text/plain"));
        RequestBody n_st_id_res = RequestBody.create(n_st_id_ress, MediaType.parse("text/plain"));
        RequestBody n_dis_id_res = RequestBody.create(n_dis_id_ress, MediaType.parse("text/plain"));
        RequestBody n_tu_id_res = RequestBody.create(n_tu_id_ress, MediaType.parse("text/plain"));
        RequestBody c_mob = RequestBody.create(c_mobbb, MediaType.parse("text/plain"));
        RequestBody c_mob_2 = RequestBody.create(c_mob_22, MediaType.parse("text/plain"));
        RequestBody c_not_img = RequestBody.create(c_not_imgg, MediaType.parse("text/plain"));
        RequestBody c_bnk_img = RequestBody.create(c_bnk_imgg, MediaType.parse("text/plain"));
        RequestBody d_diag_dt = RequestBody.create(d_diag_dtt, MediaType.parse("text/plain"));

        if (c_bnk_img.equals("")) {
            ApiClient.getClient().updateFormOne(d_reg_dat, n_nksh_id,
                    c_pat_nam, n_age,
                    n_sex, n_wght,
                    n_hght,
                    c_add,
                    c_taluka,
                    c_town,
                    c_ward, c_lnd_mrk,
                    n_pin,
                    n_st_id_res, n_dis_id_res,
                    n_tu_id_res, c_mob,
                    c_mob_2, c_not_img,
                    c_bnk_img, d_diag_dt, pateintId).enqueue(new Callback<AddDocResponse>() {
                @Override
                public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                    if (response.isSuccessful()) {


                        ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                    } else {
                        // BaseUtils.putSubmitFormOne(context, "false");
                        BaseUtils.putSubmitFormOne(context, "true");
                    }
                }

                @Override
                public void onFailure(Call<AddDocResponse> call, Throwable t) {

                }
            });


        }
    }

    public static void sendForm(
            Context context,
            String n_st_idd,
            String n_dis_idd,
            String n_tu_idd,
            String n_hf_idd,
            String n_doc_idd,
            String d_reg_datt,
            String n_nksh_idd,
            String c_pat_namm,
            String n_agee,
            String n_sexx,
            String n_wghtt,
            String n_hghtt,
            String c_addd,
            String c_talukaa,
            String c_townn,
            String c_wardd,
            String c_lnd_mrkk,
            String n_pinn,
            String n_st_id_ress,
            String n_dis_id_ress,
            String n_tu_id_ress,
            String c_mobb,
            String c_mob_22,
            String n_latt,
            String n_lngg,
            String n_user_idd,
            String type,
            Boolean navigate,
            String notification_image,
            String bank_image,
            String n_sac_idd

    ) {
        dataBase = AppDataBase.getDatabase(context);
        FormOneModel formOneModel = new FormOneModel(n_st_idd,
                n_dis_idd,
                n_tu_idd,
                n_hf_idd,
                n_doc_idd,
                d_reg_datt,
                n_nksh_idd,
                c_pat_namm,
                n_agee,
                n_sexx,
                n_wghtt,
                n_hghtt,
                c_addd,
                c_talukaa,
                c_townn,
                c_wardd,
                c_lnd_mrkk,
                n_pinn,
                n_st_id_ress,
                n_dis_id_ress,
                n_tu_id_ress,
                c_mobb,
                c_mob_22,
                n_latt,
                n_lngg,
                n_user_idd, notification_image, bank_image, n_sac_idd);
        if (!BaseUtils.isNetworkAvailable(context)) {
            // BaseUtils.putSubmitFormOne(context, "false");
            BaseUtils.putSubmitFormOne(context, "true");
            BaseUtils.showToast(context, "No internet, saved in local");

            dataBase.customerDao().insertFormOne(formOneModel);
            if (navigate) {
                if (type.equals("provider")) {
                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else {
                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }

            return;
        }
        RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));
        RequestBody n_doc_id = RequestBody.create(n_doc_idd, MediaType.parse("text/plain"));
        RequestBody d_reg_dat = RequestBody.create(d_reg_datt, MediaType.parse("text/plain"));
        RequestBody n_nksh_id = RequestBody.create(n_nksh_idd, MediaType.parse("text/plain"));
        RequestBody c_pat_nam = RequestBody.create(c_pat_namm, MediaType.parse("text/plain"));
        RequestBody n_age = RequestBody.create(n_agee, MediaType.parse("text/plain"));
        RequestBody n_sex = RequestBody.create(n_sexx, MediaType.parse("text/plain"));
        RequestBody n_wght = RequestBody.create(n_wghtt, MediaType.parse("text/plain"));
        RequestBody n_hght = RequestBody.create(n_hghtt, MediaType.parse("text/plain"));
        RequestBody c_add = RequestBody.create(c_addd, MediaType.parse("text/plain"));
        RequestBody c_taluka = RequestBody.create(c_talukaa, MediaType.parse("text/plain"));
        RequestBody c_town = RequestBody.create(c_townn, MediaType.parse("text/plain"));
        RequestBody c_ward = RequestBody.create(c_wardd, MediaType.parse("text/plain"));
        RequestBody c_lnd_mrk = RequestBody.create(c_lnd_mrkk, MediaType.parse("text/plain"));
        RequestBody n_pin = RequestBody.create(n_pinn, MediaType.parse("text/plain"));
        RequestBody n_st_id_res = RequestBody.create(n_st_id_ress, MediaType.parse("text/plain"));
        RequestBody n_dis_id_res = RequestBody.create(n_dis_id_ress, MediaType.parse("text/plain"));
        RequestBody n_tu_id_res = RequestBody.create(n_tu_id_ress, MediaType.parse("text/plain"));
        RequestBody c_mob = RequestBody.create(c_mobb, MediaType.parse("text/plain"));
        RequestBody c_mob_2 = RequestBody.create(c_mob_22, MediaType.parse("text/plain"));
        RequestBody n_lat = RequestBody.create(n_latt, MediaType.parse("text/plain"));
        RequestBody n_lng = RequestBody.create(n_lngg, MediaType.parse("text/plain"));
        RequestBody c_not_img = RequestBody.create(notification_image, MediaType.parse("text/plain"));
        RequestBody c_bnk_img = RequestBody.create(bank_image, MediaType.parse("text/plain"));
        RequestBody n_sac_id = RequestBody.create(n_sac_idd, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));
        ApiClient apiClient = null;
        if (bank_image.equals("")) {
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
                    , n_lng, c_not_img, n_sac_id, n_user_id).enqueue(new Callback<AddDocResponse>() {
                @Override
                public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                    if (response.isSuccessful()) {

                        if (response.body().isStatus()) {
                            dataBase.customerDao().deletePatient(formOneModel);
                            BaseUtils.showToast(context, "Form one submitted");
                            BaseUtils.putSubmitFormOne(context, "true");
                            // startActivity(new Intent(FormOne.this, FormTwo.class));
                            if (navigate) {
                                if (type.equals("provider")) {
                                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                } else {
                                    if (BaseUtils.getSection(context).equals("addpat")) {
                                        BaseUtils.putSection(context, BaseUtils.getPrevSection(context));
                                    }
                                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                            }

                        }
                    } else {
                        // BaseUtils.putSubmitFormOne(context, "false");
                        BaseUtils.putSubmitFormOne(context, "true");
                    }
                }

                @Override
                public void onFailure(Call<AddDocResponse> call, Throwable t) {
                    //   BaseUtils.putSubmitFormOne(context, "false");
                    BaseUtils.putSubmitFormOne(context, "true");


                }
            });
        } else {
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
                    , n_lng, c_bnk_img, c_not_img, n_sac_id, n_user_id).enqueue(new Callback<AddDocResponse>() {
                @Override
                public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                    if (response.isSuccessful()) {

                        if (response.body().isStatus()) {
                            dataBase.customerDao().deletePatient(formOneModel);
                            BaseUtils.showToast(context, "Form one submitted");
                            BaseUtils.putSubmitFormOne(context, "true");
                            // startActivity(new Intent(FormOne.this, FormTwo.class));
                            if (navigate) {
                                if (type.equals("provider")) {
                                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                } else {
                                    if (BaseUtils.getSection(context).equals("addpat")) {
                                        BaseUtils.putSection(context, BaseUtils.getPrevSection(context));
                                    }
                                    ((Activity) context).finish();
                                }
                            }

                        }
                    } else {
                        // BaseUtils.putSubmitFormOne(context, "false");
                        BaseUtils.putSubmitFormOne(context, "true");
                    }
                }

                @Override
                public void onFailure(Call<AddDocResponse> call, Throwable t) {
                    //   BaseUtils.putSubmitFormOne(context, "false");
                    BaseUtils.putSubmitFormOne(context, "true");


                }
            });
        }


    }

    public static void postProvider(
            Context context,
            String n_st_idd,
            String n_dis_idd,
            String n_tu_idd,
            String n_hf_idd,
            String typp,
            String d_visitt,
            String n_visit_idd,
            String n_visit_name,
            String n_sac_idd,
            String n_user_idd,
            FragmentManager fragmentManager,
            Boolean navigate
    ) {
        BaseUtils.putProviderData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, typp, d_visitt, n_visit_idd, n_sac_idd, n_user_idd);

        Date currentTime = Calendar.getInstance().getTime();
        BaseUtils.putSubmitProviderForm(context, "false");
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            //dataBase.customerDao().insertPostProvider(roomPostProvider);
            BaseUtils.putSubmitProviderForm(context, "false");
            if (navigate) {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                Log.d("mkijklj", "onResponse: " + n_visit_name);
                if (n_visit_name.equals("Notification Collection")) {
                    NotificationFagment fragment = new NotificationFagment();
                    fragment.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                    bundle.putString("doc_id", BaseUtils.getGlobalDocIdProvider(context));
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "providerfrag");
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

        RequestBody n_st_id = RequestBody.create(n_st_idd, MediaType.parse("text/plain"));
        RequestBody n_dis_id = RequestBody.create(n_dis_idd, MediaType.parse("text/plain"));
        RequestBody n_tu_id = RequestBody.create(n_tu_idd, MediaType.parse("text/plain"));
        RequestBody n_hf_id = RequestBody.create(n_hf_idd, MediaType.parse("text/plain"));
        RequestBody typ = RequestBody.create(typp, MediaType.parse("text/plain"));
        RequestBody d_visit = RequestBody.create(d_visitt, MediaType.parse("text/plain"));
        RequestBody n_visit_id = RequestBody.create(n_visit_idd, MediaType.parse("text/plain"));
        RequestBody n_sac_id = RequestBody.create(n_sac_idd, MediaType.parse("text/plain"));
        RequestBody n_user_id = RequestBody.create(n_user_idd, MediaType.parse("text/plain"));

        ApiClient.getClient().postProvider(n_st_id, n_dis_id, n_tu_id, n_hf_id, typ, d_visit, n_visit_id, n_sac_id, n_user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        BaseUtils.showToast(context, "Submitted");
                        //    dataBase.customerDao().deletePostProvider(roomPostProvider);
                        BaseUtils.putSubmitProviderForm(context, "true");
                        BaseUtils.putSelectedGlobalHfIdProvider(context, BaseUtils.getGlobalHfIdProvider(context));
                        if (navigate) {
                            Log.d("mkijklj", "onResponse: " + n_visit_name);
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
                        }
                    } else {
                        BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                BaseUtils.putSubmitProviderForm(context, "false");
            }
        });
    }

    public static void postDateProvider(
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
            FragmentManager fragmentManager,
            Boolean navigate
    ) {
        //BaseUtils.putProviderData(context, n_st_idd, n_dis_idd, n_tu_idd, n_hf_idd, typp, d_visitt, n_visit_idd, n_sac_idd, n_user_idd);


        if (!BaseUtils.isNetworkAvailable(context)) {
            //Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();

            //dataBase.customerDao().insertPostProvider(roomPostProvider);
            // BaseUtils.putSubmitProviderForm(context, "false");
            if (navigate) {
                // Toast.makeText(context, "Data will be submitted when back online", Toast.LENGTH_SHORT).show();
                //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                /*if (n_visit_name.equals("Notification Collection")) {
                    NotificationFagment fragment = new NotificationFagment();
                    fragment.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                    bundle.putString("doc_id",  BaseUtils.getGlobalDocIdProvider(context));
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "providerfrag");
                } else {
                    ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }*/
                //  ((Activity) context).finish();
            } else {
                // Toast.makeText(context, "Data will be submitted when back online", Toast.LENGTH_SHORT).show();
                //return;
            }
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
                        //  BaseUtils.showToast(context, "Submitted");
                        //    dataBase.customerDao().deletePostProvider(roomPostProvider);
                        BaseUtils.putSubmitProviderForm(context, "true");
                        BaseUtils.putSelectedGlobalHfIdProvider(context, BaseUtils.getGlobalHfIdProvider(context));
                        if (navigate) {
                            //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                           /* if (n_visit_name.equals("Notification Collection")) {
                                NotificationFagment fragment = new NotificationFagment();
                                fragment.setCancelable(false);
                                Bundle bundle = new Bundle();
                                bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                                //bundle.putString("doc_id", getIntent().getStringExtra("doc_id"));
                                fragment.setArguments(bundle);
                                fragment.show(fragmentManager, "providerfrag");
                            } else {
                                ((Activity) context).startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }*/
                        }
                    } else {
                        //    BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                // BaseUtils.putSubmitProviderForm(context, "false");
            }
        });
    }

    public static void postMultipleDoctorsProvider(
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
                        //BaseUtils.showToast(context, "Submitted");
                        //    dataBase.customerDao().deletePostProvider(roomPostProvider);
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
                        // BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                //   BaseUtils.putSubmitProviderForm(context, "false");
            }
        });
    }

    public static void postMultipleVisitsProvider(
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
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");

            //dataBase.customerDao().insertPostProvider(roomPostProvider);
            // BaseUtils.putSubmitProviderForm(context, "false");
            if (navigate) {
                BaseUtils.showToast(context, "Data will be submitted when back online");
                //  Log.d("mkijklj", "onResponse: " + n_visit_name);
                if (n_visit_name.equals("Notification Collection")) {
                    NotificationFagment fragment = new NotificationFagment();
                    fragment.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("hf_id", BaseUtils.getGlobalHfIdProvider(context));
                    bundle.putString("doc_id", BaseUtils.getGlobalDocIdProvider(context));
                    fragment.setArguments(bundle);
                    fragment.show(fragmentManager, "providerfrag");
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
                        }
                    } else {
                        // BaseUtils.putSubmitProviderForm(context, "false");
                    }
                }
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                BaseUtils.putSubmitProviderForm(context, "false");
            }
        });
    }

    public static void getPythologyLabSample(Context context, String n_tu_id, String type) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabsample", ""));
            return;
        }
        String url = "_sphf_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=5&sanc=" + BaseUtils.getUserInfo(context).getN_staff_sanc() + "&tu_id=" + BaseUtils.getGlobalTuid(context) + "&hf=" + BaseUtils.getGlobalnHfTypeid(context) + "&labt=" + type;

        ApiClient.getClient().getPythologyLabs(url).enqueue(new Callback<PythologyLabResponse>() {
            @Override
            public void onResponse(Call<PythologyLabResponse> call, Response<PythologyLabResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("true")) {

                        BaseUtils.savePythologyLabSamples(context, response.body().getUser_data());
                        Log.d("jioussqw", "onResponse: " + response.body().getUser_data().size());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabsample", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabsample", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabsample", ""));
                }
            }

            @Override
            public void onFailure(Call<PythologyLabResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabsample", ""));
            }
        });
    }

    public static void getPythologyLabType(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabtype", ""));
            return;
        }
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_lab_typ&w=id<<GT>>0";

        ApiClient.getClient().getPythologyLabTypes(url).enqueue(new Callback<LabTypeResponse>() {
            @Override
            public void onResponse(Call<LabTypeResponse> call, Response<LabTypeResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("true")) {

                        BaseUtils.savePythologyLabTypes(context, response.body().getUser_data());
                        Log.d("jioussqw", "onResponse: " + response.body().getUser_data().size());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabtype", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabtype", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabtype", ""));
                }
            }

            @Override
            public void onFailure(Call<LabTypeResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPythologylabtype", ""));
            }
        });
    }

    public static void getDiagnosticTestSample(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnosticTestSample", ""));

            return;
        }
        ApiClient.getClient().getDiagnosticTestsSample().enqueue(new Callback<DiagnosticTestRes>() {
            @Override
            public void onResponse(Call<DiagnosticTestRes> call, Response<DiagnosticTestRes> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("true")) {
                        BaseUtils.saveDiagnosticTestSample(context, response.body().getUser_data());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnosticTestSample", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnosticTestSample", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnosticTestSample", ""));
                }
            }

            @Override
            public void onFailure(Call<DiagnosticTestRes> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnosticTestSample", ""));
            }
        });
    }

    public static void getType(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localtype", ""));
            }
        });
    }

    public static void getAttendanceList(Context context, String userID) {

        apiInterface = ApiClient.getClient();
        progressDialog = new GlobalProgressDialog(context);
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(context)) {
            progressDialog.hideProgressBar();
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));

            return;
        }

        apiInterface.getAttendenceList("n_user_id=" + BaseUtils.getUserInfo(context).getnUserLevel()).enqueue(new Callback<AttendenceListResponse>() {
            @Override
            public void onResponse(Call<AttendenceListResponse> call, @NotNull Response<AttendenceListResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {

                        BaseUtils.saveAttendanceList(context, response.body().getUserData());
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
            public void onFailure(Call<AttendenceListResponse> call, @NotNull Throwable t) {
                progressDialog.hideProgressBar();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localData", ""));
            }
        });
    }

    public static void getDiagnostic(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnostic", ""));
            return;
        }
        ApiClient.getClient().getDiagnosticTests().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveDiagnostic(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnostic", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnostic", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnostic", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDiagnostic", ""));
            }
        });
    }

    public static void getDST(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDST", ""));
            return;
        }
        ApiClient.getClient().getDSTOffered().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveDST(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDST", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDST", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDST", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDST", ""));
            }
        });
    }

    public static void getOtherDST(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localOtherDST", ""));
            return;
        }
        ApiClient.getClient().getOtherDST().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveOtherDST(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localOtherDST", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localOtherDST", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localOtherDST", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localOtherDST", ""));
            }
        });
    }

    public static void getInterpretation(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localInter", ""));
            return;
        }
        ApiClient.getClient().getInter().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveInterpretation(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localInter", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localInter", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localInter", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localInter", ""));
            }
        });
    }

    public static void getTestResult(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTestResult", ""));
            return;
        }
        ApiClient.getClient().getTestResult().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveTestResult(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTestResult", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTestResult", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTestResult", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTestResult", ""));
            }
        });
    }

    public static void getCaseType(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localCaseType", ""));
            return;
        }
        ApiClient.getClient().getCaseType().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveCaseType(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localCaseType", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localCaseType", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localCaseType", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localCaseType", ""));
            }
        });
    }

    public static void getPatientStatus(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPatientStatus", ""));
            return;
        }
        ApiClient.getClient().getPatientStatus().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.savePatientStatus(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPatientStatus", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPatientStatus", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPatientStatus", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPatientStatus", ""));
            }
        });
    }

    public static void getPurpose(Context context) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPurpose", ""));
            return;
        }
        ApiClient.getClient().getPurpose().enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.savePurpose(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPurpose", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPurpose", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPurpose", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localPurpose", ""));
            }
        });
    }
}