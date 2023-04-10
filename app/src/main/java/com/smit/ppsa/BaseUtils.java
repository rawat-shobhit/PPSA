package com.smit.ppsa;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Response.AttendenceListData;
import com.smit.ppsa.Response.CollectedBy.UserData;
import com.smit.ppsa.Response.DoctorsList;
import com.smit.ppsa.Response.FormOneData;
import com.smit.ppsa.Response.HospitalList;
import com.smit.ppsa.Response.QualificationList;
import com.smit.ppsa.Response.RegisterParentData;
import com.smit.ppsa.Response.RoomDoctorsList;
import com.smit.ppsa.Response.UserInfoList;
import com.smit.ppsa.Response.UserList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smit.ppsa.Response.pythologylab.LabResponseInternal;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BaseUtils {

    private AppDataBase dataBase;
    private static final String PREF_NAME = "sfa_pref";
    static AlertDialog number_dialog;

    public static void showToast(Context context, String msg) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup)((Activity)context).findViewById(R.id.toast_layout_root));


//        ImageView image = (ImageView) layout.findViewById(R.id.image);
//        image.setImageResource(R.drawable.logo);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    // check network status (Available or not)
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Network Not Available !!!");
        }
        return false;
    }

    public static boolean getUserLogIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("is_login", false);
    }

    public static String getattendanceType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("attendanceType", "");
    }


    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("username", "");


    }



    public static String getSelectedTu(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("selectedTu", "");
    }

    public static String getSubmitLabReportStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("labreportstatus", "");
    }

    public static String getSubmitCounsellingFormStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("counsellingformstatus", "");
    }

    public static String getAddDocFormFormStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("adddocformstatus", "");
    }

    public static String getSubmitFdcDispensationHfForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("fdcDispensationHfState", "");
    }

    public static String getSubmitFdcReceiveForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("fdcReceiveState", "");
    }

    public static String getAddSampleForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("addSampleState", "");
    }

    public static String getAddTestForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("addTestState", "");
    }

    public static String getGlobalHfIdProvider(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globalhfidprovider", "");
    }

    public static String getGlobalDocIdProvider(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globaldocidprovider", "");
    }

    public static String getSelectedGlobalHfIdProvider(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globalselectedhfidprovider", "");
    }

    public static String getProviderForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("addProviderState", "");
    }

    public static String getFormOne(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("addformoneState", "");
    }

    public static String getSection(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("sect", "");
    }

    public static String getPrevSection(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("Prevsect", "");
    }

    public static String getAddHosptitalForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("addhospitalformState", "");
    }

    public static String getSubmitFdcDispensationPaForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("fdcDispensationPaState", "");
    }

    public static String getSubmitFdcDispensationOpForm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("fdcDispensationOpState", "");
    }

    public static String getGlobalTuid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globaltuid", "");
    }

    public static String getGlobalnHfTypeid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globalhftypeid", "");
    }

    public static String getGlobalPatientName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globalpatientname", "");
    }

    public static String getGlobalSelectedPatientName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globalselectedpatientname", "");
    }


    public static String getGlobaldocId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globaldocid", "");
    }

    public static String getGlobalhfId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("globalHfId", "");
    }

    public static Boolean getofflineDataAlreadyFetched(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("offlinereportupdate", false);
    }

    public static String getn_st_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_idformsix", "");
    }

    public static String getn_dis_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_idformsix", "");
    }

    public static String getn_tu_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_idformsix", "0");
    }

    public static String getn_hf_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_idformsix", "0");
    }

    public static String getn_doc_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_doc_idformsix", "0");
    }

    public static String getn_enroll_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_enroll_idformsix", "0");
    }

    public static String getn_smpl_col_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_smpl_col_idformsix", "0");
    }

    public static String getd_tst_rsltFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_rsltformsix", "0");
    }

    public static String getn_tst_rptFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tst_rptformsix", "");
    }

    public static String getd_rpt_colFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_rpt_colformsix", "");
    }

    public static String getc_tr_fp_imgFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("c_tr_fp_imgformsix", "");
    }

    public static String getc_tr_bp_imgFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("c_tr_bp_imgformsix", "");
    }

    public static String getd_lpa_smplFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_lpa_smplformsix", "");
    }

    public static String getn_lpa_rsltFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lpa_rsltformsix", "0");
    }

    public static String getn_latFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_latformsix", "");
    }

    public static String getn_lngFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lngformsix", "");
    }

    public static String getn_staff_infoFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_staff_infoformsix", "");
    }

    public static String getn_user_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_idformsix", "");
    }

    public static String getn_lab_idFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lab_idformsix", "0");
    }

    public static String getn_rpt_delFormSix(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_rpt_delformsix", "0");
    }

    public static String getFormSixTuId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("selectedFormSixTuId", "");
    }

    public static String getFormSixHfId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("selectedFormSixHfId", "");
    }

    public static String getCounsellingFormuser_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("user_iddcounselling", "");
    }

    public static String getCounsellingFormstaff_infoo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("staff_infoocounselling", "");
    }

    public static String getCounsellingFormn_stidd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_stiddcounselling", "");
    }

    public static String getCounsellingFormn_disIdd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_disIddcounselling", "");
    }

    public static String getCounsellingFormn_docIdd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_docIddcounselling", "");
    }

    public static String getCounsellingFormlatt(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("lattcounselling", "");
    }

    public static String getCounsellingFormlngg(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("lnggcounselling", "");
    }

    public static String getCounsellingFormn_typeCounn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_typeCounncounselling", "");
    }

    public static String getCounsellingFormd_Counn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_Counncounselling", "");
    }

    public static String getCounsellingFormneenrollIDD(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("neenrollIDDcounselling", "");
    }

    public static String getCounsellingFormnTuIdd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("nTuIddcounselling", "");
    }

    public static String getCounsellingFormnHfidd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("nHfiddcounselling", "");
    }

    public static String getFdcHfn_st_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_idFdcHf", "");
    }

    public static String getFdcHfn_dis_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_idFdcHf", "");
    }

    public static String getFdcHfn_tu_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_idFdcHf", "");
    }

    public static String getFdcHfn_hf_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_idFdcHf", "");
    }

    public static String getFdcHfn_doc_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_doc_idFdcHf", "");
    }


    public static String getFdcHfd_issue(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_issueFdcHf", "");
    }

    public static String getFdcHfn_med_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc2FdcHf", "");
    }

    public static String getFdcHfn_uom_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc3FdcHf", "");
    }

    public static String getFdcHfn_qty(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc4FdcHf", "");
    }

    public static String getFdcHfn_etham(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_ethamFdcHf", "");
    }

    public static String getFdcHfn_lat(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_latFdcHf", "");
    }

    public static String getFdcHfn_lng(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lngFdcHf", "");
    }

    public static String getFdcHfn_staff_info(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_staff_infoFdcHf", "");
    }

    public static String getFdcHfn_user_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_idFdcHf", "");
    }

    public static String getFdcOPn_st_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_idOp", "");
    }

    public static String getFdcOPn_dis_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_idOp", "");
    }

    public static String getFdcOPn_tu_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_idOp", "");
    }

    public static String getFdcOPn_hf_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_idOp", "");
    }

    public static String getFdcPan_st_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_idFdcPa", "");
    }

    public static String getFdcPan_dis_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_idFdcPa", "");
    }

    public static String getFdcPan_tu_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_idFdcPa", "");
    }

    public static String getFdcPan_hf_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_idFdcPa", "");
    }

    public static String getFdcPan_doc_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_doc_idFdcPa", "");
    }

    public static String getFdcPan_enroll_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_enroll_idFdcPa", "");
    }

    public static String getFdcPad_issue(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_issueFdcPa", "");
    }

    public static String getFdcPan_wght_bnd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc2FdcPa", "");
    }

    public static String getFdcPan_med_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc3FdcPa", "");
    }

    public static String getFdcPan_uom_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc4FdcPa", "");
    }

    public static String getFdcPan_qty(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_ethamFdcPa", "");
    }

    public static String getFdcPan_days(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_daysFdcPa", "");
    }

    public static String getFdcPan_lat(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_latFdcPa", "");
    }

    public static String getFdcPan_lng(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lngFdcPa", "");
    }

    public static String getFdcPan_staff_info(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_staff_infoFdcPa", "");
    }

    public static String getFdcPan_user_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_idFdcPa", "");
    }

    public static String getFdcOPn_med_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc2Op", "");
    }

    public static String getFdcOPn_uom_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc3Op", "");
    }

    public static String getFdcOPn_qty(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fdc4Op", "");
    }

    public static String getFdcOPn_entham(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_enthamOp", "");
    }

    public static String getFdcOPn_lat(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_latOp", "");
    }

    public static String getFdcOPn_lng(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lngOp", "");
    }

    public static String getFdcOPn_staff_info(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_staff_infoOp", "");
    }

    public static String getFdcOPn_user_id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_idOp", "");
    }


    public static String getPatientName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("patientname", "");
    }

    public static String getCounsellingPatientName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("Counsellingpatientname", "");
    }

    public static String getTuIdFdc(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("TuIdFdc", "");
    }

    public static String getHfIdFdc(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("HfIdFdc", "");
    }

    public static String getHospitalnStId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("nStIdHospital", "");
    }

    public static String getHospitalnDisId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("nDisIdHospital", "");
    }

    public static String getHospitalnTuId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("nTuIdHospital", "");
    }

    public static String getAdddocnHfId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("nHfIdAdddoc", "");
    }

    public static String getAdddoctype(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("typeAdddoc", "");
    }

    public static String getAdddochospitalName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("hospitalNameAdddoc", "");
    }

    public static String getPatientNameFdcPa(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("patientNameFdcPa", "");
    }

    public static void putUserAttendance(Context context, boolean status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_attend", status);
        editor.apply();
    }

    public static boolean isAttendanceDone(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("is_attend", false);
    }

    public static List<UserList> getUsers(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<UserList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static List<RoomDoctorsList> getSavedSelectedDoctors(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<RoomDoctorsList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("saved_doctors", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RoomDoctorsList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void putUserLogIn(Context context, boolean status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_login", status);
        editor.apply();
    }

    public static void putUserAttendanceType(Context context, String attendanceType) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("attendanceType", attendanceType);
        editor.apply();
    }

    public static void putUsername(Context context, String userName) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", userName);
        editor.apply();
    }

    public static void putPatientName(Context context, String patientName) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patientname", patientName);
        editor.apply();
    }

    public static void putCounsellingFormPatientName(Context context, String patientName) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Counsellingpatientname", patientName);
        editor.apply();
    }

    public static void putTuidFdc(Context context, String tu_id) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TuIdFdc", tu_id);
        editor.apply();
    }

    public static void putHfidFdc(Context context, String hf_id) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("HfIdFdc", hf_id);
        editor.apply();
    }

    public static void putHospitalnStId(Context context, String nStId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nStIdHospital", nStId);
        editor.apply();
    }

    public static void putHospitalnDisId(Context context, String nDisId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nDisIdHospital", nDisId);
        editor.apply();
    }

    public static void putHospitalnTUId(Context context, String nDisId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nTuIdHospital", nDisId);
        editor.apply();
    }

    public static void putAdddocnHfId(Context context, String nDisId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nHfIdAdddoc", nDisId);
        editor.apply();
    }

    public static void putAdddoctype(Context context, String nDisId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("typeAdddoc", nDisId);
        editor.apply();
    }

    public static void putAdddocHospitalName(Context context, String nDisId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("hospitalNameAdddoc", nDisId);
        editor.apply();
    }

    public static void putSubmitLabReportStatus(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("labreportstatus", status);
        editor.apply();
    }

    public static void putSubmitCounsellingForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("counsellingformstatus", status);
        editor.apply();
    }

    public static void putAddDocForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("adddocformstatus", status);
        editor.apply();
    }

    public static void putSubmitFdcDispensationHfForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fdcDispensationHfState", status);
        editor.apply();
    }
    public static void putSubmitFdcReceiveForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fdcReceiveState", status);
        editor.apply();
    }

    public static void putSubmitAddSampleForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("addSampleState", status);
        editor.apply();
    }

    public static void putSubmitTestForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("addTestState", status);
        editor.apply();
    }

    public static void putGlobalHfIdProvider(Context context, String hfid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globalhfidprovider", hfid);
        editor.apply();
    }

    public static void putGlobalDocIdProvider(Context context, String hfid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globaldocidprovider", hfid);
        editor.apply();
    }

    public static void putSelectedGlobalHfIdProvider(Context context, String hfid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globalselectedhfidprovider", hfid);
        editor.apply();
    }

    public static void putSubmitProviderForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("addProviderState", status);
        editor.apply();
    }

    public static void putSubmitFormOne(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("addformoneState", status);
        editor.apply();
    }
      public static void putSection(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sect", status);
        editor.apply();
    }
    public static void putPrevSection(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Prevsect", status);
        editor.apply();
    }

    public static void putAddHospitalForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("addhospitalformState", status);
        editor.apply();
    }

    public static void putSubmitFdcDispensationPaForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fdcDispensationPaState", status);
        editor.apply();
    }

    public static void putPatientNamePaForm(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patientNameFdcPa", name);
        editor.apply();
    }

    public static void putGlobalTuid(Context context, String tuid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globaltuid", tuid);
        editor.apply();
    }

    public static void putGlobalDocid(Context context, String tuid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globaldocid", tuid);
        editor.apply();
    }

    public static void putGlobalHfid(Context context, String hfid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globalHfId", hfid);
        editor.apply();
    }

    public static void putGlobalHftypeid(Context context, String hftypeid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globalhftypeid", hftypeid);
        editor.apply();
    }

    public static void putGlobalPatientName(Context context, String patientName) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globalpatientname", patientName);
        editor.apply();
    }

    public static void putGlobalSelectedPatientName(Context context, String patientName) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("globalselectedpatientname", patientName);
        editor.apply();
    }

    public static void putSubmitFdcDispensationOpForm(Context context, String status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fdcDispensationOpState", status);
        editor.apply();
    }

    public static String getn_st_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_iddProvider", "");
    }

    public static String getn_dis_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_iddProvider", "");
    }

    public static String getn_tu_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_iddProvider", "");
    }

    public static String getn_hf_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_iddProvider", "");
    }

    public static String gettyppProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("typpProvider", "");
    }

    public static String getd_visittProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_visittProvider", "");
    }

    public static String getn_visit_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_visit_iddProvider", "");
    }

    public static String getn_sac_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_sac_iddProvider", "");
    }

    public static String getn_user_iddProviderData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_iddProvider", "");
    }


    public static void putProviderData(
            Context context,
            String n_st_idd,
            String n_dis_idd,
            String n_tu_idd,
            String n_hf_idd,
            String typp,
            String d_visitt,
            String n_visit_idd,
            String n_sac_idd,
            String n_user_idd
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_iddProvider", n_st_idd);
        editor.putString("n_dis_iddProvider", n_dis_idd);
        editor.putString("n_tu_iddProvider", n_tu_idd);
        editor.putString("n_hf_iddProvider", n_hf_idd);
        editor.putString("typpProvider", typp);
        editor.putString("d_visittProvider", d_visitt);
        editor.putString("n_visit_iddProvider", n_visit_idd);
        editor.putString("n_sac_iddProvider", n_sac_idd);
        editor.putString("n_user_iddProvider", n_user_idd);
        editor.apply();
    }


    public static String getn_st_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_idTest", "");
    }

    public static String getn_dis_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_idTest", "");
    }

    public static String getn_tu_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_idTest", "");
    }

    public static String getn_hf_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_idTest", "");
    }

    public static String getn_enroll_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_enroll_idTest", "");
    }

    public static String getn_smpl_col_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_smpl_col_idTest", "");
    }

    public static String getd_smpl_recdTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_smpl_recdTest", "");
    }

    public static String getd_rpt_docTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_rpt_docTest", "");
    }

    public static String getn_diag_testTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_diag_testTest", "");
    }

    public static String getd_tst_diagTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_diagTest", "");
    }

    public static String getd_tst_rpt_diagTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_rpt_diagTest", "");
    }

    public static String getn_dstTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dstTest", "");
    }

    public static String getd_tst_dstTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_dstTest", "");
    }

    public static String getd_tst_rpt_dstTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_rpt_dstTest", "");
    }

    public static String getn_oth_dstTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_oth_dstTest", "");
    }

    public static String getd_tst_rpt_oth_dstTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_rpt_oth_dstTest", "");
    }

    public static String getd_tst_oth_dstTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_tst_oth_dstTest", "");
    }

    public static String getn_fnl_intpTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_fnl_intpTest", "");
    }

    public static String getn_tst_rsultTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tst_rsultTest", "");
    }

    public static String getn_case_typTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_case_typTest", "");
    }

    public static String getn_pat_statusTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_pat_statusTest", "");
    }

    public static String getn_user_idTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_idTest", "");
    }

    public static String getn_latTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_latTest", "");
    }

    public static String getn_lngTest(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lngTest", "");
    }

    public static void putTestData(Context context,
                                   String n_st_id,
                                   String n_dis_id,
                                   String n_tu_id,
                                   String n_hf_id,
                                   String n_enroll_id,
                                   String n_smpl_col_id,
                                   String d_smpl_recd,
                                   String d_rpt_doc,
                                   String n_diag_test,
                                   String d_tst_diag,
                                   String d_tst_rpt_diag,
                                   String n_dst,
                                   String d_tst_dst,
                                   String d_tst_rpt_dst,
                                   String n_oth_dst,
                                   String d_tst_rpt_oth_dst,
                                   String d_tst_oth_dst,
                                   String n_fnl_intp,
                                   String n_tst_rsult,
                                   String n_case_typ,
                                   String n_pat_status,
                                   String n_user_id,
                                   String n_lat,
                                   String n_lng
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_idTest", n_st_id);
        editor.putString("n_dis_idTest", n_dis_id);
        editor.putString("n_tu_idTest", n_tu_id);
        editor.putString("n_hf_idTest", n_hf_id);
        editor.putString("n_enroll_idTest", n_enroll_id);
        editor.putString("n_smpl_col_idTest", n_smpl_col_id);
        editor.putString("d_smpl_recdTest", d_smpl_recd);
        editor.putString("d_rpt_docTest", d_rpt_doc);
        editor.putString("n_diag_testTest", n_diag_test);
        editor.putString("d_tst_diagTest", d_tst_diag);
        editor.putString("d_tst_rpt_diagTest", d_tst_rpt_diag);
        editor.putString("n_dstTest", n_dst);
        editor.putString("d_tst_dstTest", d_tst_dst);
        editor.putString("d_tst_rpt_dstTest", d_tst_rpt_dst);
        editor.putString("n_oth_dstTest", n_oth_dst);
        editor.putString("d_tst_rpt_oth_dstTest", d_tst_rpt_oth_dst);
        editor.putString("d_tst_oth_dstTest", d_tst_oth_dst);
        editor.putString("n_fnl_intpTest", n_fnl_intp);
        editor.putString("n_tst_rsultTest", n_tst_rsult);
        editor.putString("n_case_typTest", n_case_typ);
        editor.putString("n_pat_statusTest", n_pat_status);
        editor.putString("n_user_idTest", n_user_id);
        editor.putString("n_latTest", n_lat);
        editor.putString("n_lngTest", n_lng);
        editor.apply();
    }

    public static void putFdCOPForm(
            int n_st_id,
            int n_dis_id,
            int n_tu_id,
            int n_hf_id,
            int n_fdc2,
            int n_fdc3,
            int n_fdc4,
            String n_lat,
            String n_lng,
            int n_staff_info,
            int n_user_id,
            Context context
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_idOp", String.valueOf(n_st_id));
        editor.putString("n_dis_idOp", String.valueOf(n_dis_id));
        editor.putString("n_tu_idOp", String.valueOf(n_tu_id));
        editor.putString("n_hf_idOp", String.valueOf(n_hf_id));
        editor.putString("n_fdc2Op", String.valueOf(n_fdc2));
        editor.putString("n_fdc3Op", String.valueOf(n_fdc3));
        editor.putString("n_fdc4Op", String.valueOf(n_fdc4));
        // editor.putString("n_enthamOp", String.valueOf(n_entham));
        editor.putString("n_latOp", String.valueOf(n_lat));
        editor.putString("n_lngOp", String.valueOf(n_lng));
        editor.putString("n_staff_infoOp", String.valueOf(n_staff_info));
        editor.putString("n_user_idOp", String.valueOf(n_user_id));
        editor.apply();
    }

    public static void putFdcHfForm(

            int n_st_id,
            int n_dis_id,
            int n_tu_id,
            int n_hf_id,
            int n_doc_id,
            String d_issue,
            int n_fdc2,
            int n_fdc3,
            int n_fdc4,
            int n_etham,
            String n_lat,
            String n_lng,
            int n_staff_info,
            int n_user_id,
            Context context
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_idFdcHf", String.valueOf(n_st_id));
        editor.putString("n_dis_idFdcHf", String.valueOf(n_dis_id));
        editor.putString("n_tu_idFdcHf", String.valueOf(n_tu_id));
        editor.putString("n_hf_idFdcHf", String.valueOf(n_hf_id));
        editor.putString("n_doc_idFdcHf", String.valueOf(n_doc_id));
        editor.putString("d_issueFdcHf", String.valueOf(d_issue));
        editor.putString("n_fdc2FdcHf", String.valueOf(n_fdc2));
        editor.putString("n_fdc3FdcHf", String.valueOf(n_fdc3));
        editor.putString("n_fdc4FdcHf", String.valueOf(n_fdc4));
        editor.putString("n_ethamFdcHf", String.valueOf(n_etham));
        editor.putString("n_latFdcHf", String.valueOf(n_lat));
        editor.putString("n_lngFdcHf", String.valueOf(n_lng));
        editor.putString("n_staff_infoFdcHf", String.valueOf(n_staff_info));
        editor.putString("n_user_idFdcHf", String.valueOf(n_user_id));
        editor.apply();
    }

    public static void putFdcPatientForm(

            int n_st_id,
            int n_dis_id,
            int n_tu_id,
            int n_hf_id,
            int n_doc_id,
            int n_enroll_id,
            String d_issue,
            int n_wght_bnd,
            int n_med_id,
            int n_uom_id,
            int n_qty,
            int n_days,
            String n_lat,
            String n_lng,
            int n_staff_info,
            int n_user_id,
            Context context
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_idFdcPa", String.valueOf(n_st_id));
        editor.putString("n_dis_idFdcPa", String.valueOf(n_dis_id));
        editor.putString("n_tu_idFdcPa", String.valueOf(n_tu_id));
        editor.putString("n_hf_idFdcPa", String.valueOf(n_hf_id));
        editor.putString("n_doc_idFdcPa", String.valueOf(n_doc_id));
        editor.putString("n_enroll_idFdcPa", String.valueOf(n_enroll_id));
        editor.putString("d_issueFdcPa", String.valueOf(d_issue));
        editor.putString("n_fdc2FdcPa", String.valueOf(n_wght_bnd));
        editor.putString("n_fdc3FdcPa", String.valueOf(n_med_id));
        editor.putString("n_fdc4FdcPa", String.valueOf(n_uom_id));
        editor.putString("n_ethamFdcPa", String.valueOf(n_qty));
        editor.putString("n_daysFdcPa", String.valueOf(n_days));
        editor.putString("n_latFdcPa", String.valueOf(n_lat));
        editor.putString("n_lngFdcPa", String.valueOf(n_lng));
        editor.putString("n_staff_infoFdcPa", String.valueOf(n_staff_info));
        editor.putString("n_user_idFdcPa", String.valueOf(n_user_id));
        editor.apply();
    }


    public static void putCounselingFormData(
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
            String nHfidd
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_iddcounselling", user_idd);
        editor.putString("staff_infoocounselling", staff_infoo);
        editor.putString("n_stiddcounselling", n_stidd);
        editor.putString("n_disIddcounselling", n_disIdd);
        editor.putString("n_docIddcounselling", n_docIdd);
        editor.putString("lattcounselling", latt);
        editor.putString("lnggcounselling", lngg);
        editor.putString("n_typeCounncounselling", n_typeCounn);
        editor.putString("d_Counncounselling", d_Counn);
        editor.putString("neenrollIDDcounselling", neenrollIDD);
        editor.putString("nTuIddcounselling", nTuIdd);
        editor.putString("nHfiddcounselling", nHfidd);
        editor.apply();
    }

    public static String getAddSamplen_st_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_st_iddaddSample", "");
    }

    public static String getAddSamplen_dis_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_dis_iddaddSample", "");
    }

    public static String getAddSamplen_tu_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_tu_iddaddSample", "");
    }

    public static String getAddSamplen_hf_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_hf_iddaddSample", "");
    }

    public static String getAddSamplen_doc_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_doc_iddaddSample", "");
    }

    public static String getAddSamplen_enroll_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_enroll_iddaddSample", "");
    }

    public static String getAddSampled_specm_coll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("d_specm_colladdSample", "");
    }

    public static String getAddSamplen_smpl_ext_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_smpl_ext_iddaddSample", "");
    }

    public static String getAddSamplen_test_reas_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_test_reas_iddaddSample", "");
    }

    public static String getAddSamplen_purp_vstt(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_purp_vsttaddSample", "");
    }

    public static String getAddSamplen_typ_specm_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_typ_specm_iddaddSample", "");
    }

    public static String getAddSamplen_cont_smpll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_cont_smplladdSample", "");
    }

    public static String getAddSamplec_plc_samp_coll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("c_plc_samp_colladdSample", "");
    }

    public static String getAddSamplen_sputm_typ_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_sputm_typ_iddaddSample", "");
    }

    public static String getAddSamplen_diag_tstt(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_diag_tsttaddSample", "");
    }

    public static String getAddSamplen_lab_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_lab_iddSample", "");
    }

    public static String getAddSamplen_staff_infoo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_staff_infooSample", "");
    }

    public static String getAddSamplen_user_idd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("n_user_iddSample", "");
    }

    public static void putAddSampleData(
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
            String n_user_idd
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_iddaddSample", n_st_idd);
        editor.putString("n_dis_iddaddSample", n_dis_idd);
        editor.putString("n_tu_iddaddSample", n_tu_idd);
        editor.putString("n_hf_iddaddSample", n_hf_idd);
        editor.putString("n_doc_iddaddSample", n_doc_idd);
        editor.putString("n_enroll_iddaddSample", n_enroll_idd);
        editor.putString("d_specm_colladdSample", d_specm_coll);
        editor.putString("n_smpl_ext_iddaddSample", n_smpl_ext_idd);
        editor.putString("n_test_reas_iddaddSample", n_test_reas_idd);
        editor.putString("n_purp_vsttaddSample", n_purp_vstt);
        editor.putString("n_typ_specm_iddaddSample", n_typ_specm_idd);
        editor.putString("n_cont_smplladdSample", n_cont_smpll);
        editor.putString("c_plc_samp_colladdSample", c_plc_samp_coll);
        editor.putString("n_sputm_typ_iddaddSample", n_sputm_typ_idd);
        editor.putString("n_diag_tsttaddSample", n_diag_tstt);
        editor.putString("n_lab_iddSample", n_lab_idd);
        editor.putString("n_staff_infooSample", n_staff_infoo);
        editor.putString("n_user_iddSample", n_user_idd);
        editor.apply();
    }

    public static void putofflineDataAlreadyFetched(Context context, Boolean state) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("offlinereportupdate", state);
        editor.apply();
    }

    public static void putLabTestReportData(Context context,
                                            String n_st_id,
                                            String n_dis_id,
                                            String n_tu_id,
                                            String n_hf_id,
                                            String n_doc_id,
                                            String n_enroll_id,
                                            String n_smpl_col_id,
                                            String d_tst_rslt,
                                            String n_tst_rpt,
                                            String d_rpt_col,
                                            String c_tr_fp_img,
                                            String c_tr_bp_img,

                                            String n_lat,
                                            String n_lng,
                                            String n_staff_info,
                                            String n_user_id,
                                            String n_lab_id,
                                            String n_rpt_del
    ) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("n_st_idformsix", String.valueOf(n_st_id));
        editor.putString("n_dis_idformsix", String.valueOf(n_dis_id));
        editor.putString("n_tu_idformsix", String.valueOf(n_tu_id));
        editor.putString("n_hf_idformsix", String.valueOf(n_hf_id));
        editor.putString("n_doc_idformsix", String.valueOf(n_doc_id));
        editor.putString("n_enroll_idformsix", String.valueOf(n_enroll_id));
        editor.putString("n_smpl_col_idformsix", String.valueOf(n_smpl_col_id));
        editor.putString("d_tst_rsltformsix", String.valueOf(d_tst_rslt));
        editor.putString("n_tst_rptformsix", String.valueOf(n_tst_rpt));
        editor.putString("d_rpt_colformsix", String.valueOf(d_rpt_col));
        editor.putString("c_tr_fp_imgformsix", String.valueOf(c_tr_fp_img));
        editor.putString("c_tr_bp_imgformsix", String.valueOf(c_tr_bp_img));

        editor.putString("n_latformsix", String.valueOf(n_lat));
        editor.putString("n_lngformsix", String.valueOf(n_lng));
        editor.putString("n_staff_infoformsix", String.valueOf(n_staff_info));
        editor.putString("n_user_idformsix", String.valueOf(n_user_id));
        editor.putString("n_lab_idformsix", String.valueOf(n_lab_id));
        editor.putString("n_rpt_delformsix", String.valueOf(n_rpt_del));
        editor.apply();
    }

    public static void putSelectedTu(Context context, String Tu) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedTu", Tu);
        editor.apply();
    }

    public static void putFormSixTuId(Context context, String Tuid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedFormSixTuId", Tuid);
        editor.apply();
    }

    public static void putFormSixHfId(Context context, String Hfid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedFormSixHfId", Hfid);
        editor.apply();
    }

    public static void savedUserData(Context context, UserList mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_data", json);
        editor.apply();
    }

    public static void savedUsers(Context context, List<UserList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_list", json);
        editor.apply();
    }

    public static void savedSelectedDoctors(Context context, List<RoomDoctorsList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("saved_doctors", json);
        editor.apply();
    }

    public static Date getDateFromString(String date){
        Date date1 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static boolean sameMonth(Context context,String date){
        if (date.equals(new SimpleDateFormat("yyyy/MM", Locale.getDefault()).format(new Date()))){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isSameDay(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (preferences.getString("set_date", "").equals(new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date()))) {
            return true;
            // Date matches. User has already Launched the app once today. So do nothing.
        } else {
            return false;
        }
    }

    public static void setDay(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        //   String json = gson.toJson(mList);
        editor.putString("set_date", new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date()));
        editor.apply();
    }

    public static void saveHospitalList(Context context, List<HospitalList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_hospital_list", json);
        editor.apply();
    }

    public static List<HospitalList> getHospital(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<HospitalList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_hospital_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<HospitalList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveTuPatientList(Context context, List<RegisterParentData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_tu_patient_list", json);
        editor.apply();
    }

    public static List<RegisterParentData> getTuPatient(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<RegisterParentData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_tu_patient_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RegisterParentData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void savePatientList(Context context, List<RegisterParentData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("patient_list", json);
        editor.apply();
    }

    public static List<RegisterParentData> getPateint(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<RegisterParentData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("patient_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RegisterParentData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveAttendanceList(Context context, List<AttendenceListData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_attendance_list", json);
        editor.apply();
    }

    public static List<AttendenceListData> getAttendance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<AttendenceListData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_attendance_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<AttendenceListData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveUserInfo(Context context, UserList userInfo) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString("user_info", json);
        editor.apply();
    }

    public static UserList getUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserList arrayItems = new UserList();
        String serializedObject = preferences.getString("user_info", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserList>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    public static void saveUserOtherInfo(Context context, UserInfoList userInfo) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString("user_other_info", json);
        editor.apply();
    }

    public static Boolean haveAccess(Context context){
        if (BaseUtils.getUserInfo(context).getnAccessRights().equals("4")){
            return true;
        }else if (BaseUtils.getUserInfo(context).getnAccessRights().equals("5")){
            return true;
        }else if (BaseUtils.getUserInfo(context).getnAccessRights().equals("6")){
            return true;
        }else{
            return false;
        }
    }
    public static UserInfoList getUserOtherInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserInfoList arrayItems = new UserInfoList();
        String serializedObject = preferences.getString("user_other_info", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserInfoList>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }


    public static void saveDocList(Context context, List<DoctorsList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_doc_list", json);
        editor.apply();
    }

    public static List<DoctorsList> getDoc(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<DoctorsList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_doc_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<DoctorsList>>() {
            }.getType();

            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    public static void saveQualList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_qual_list", json);
        editor.apply();
    }

    public static List<QualificationList> getQual(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_qual_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveQualSpeList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_qualspe_list", json);
        editor.apply();
    }

    public static List<QualificationList> getQualSpe(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_qualspe_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savehfTypeList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_hfType_list", json);
        editor.apply();
    }

    public static List<QualificationList> gethfType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_hfType_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveScopeList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_scope_list", json);
        editor.apply();
    }

    public static List<QualificationList> getScope(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_scope_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveBenefeciaryList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_benefeciary_list", json);
        editor.apply();
    }

    public static List<QualificationList> getBenefeciary(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_benefeciary_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveGender(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("gender", json);
        editor.apply();
    }

    public static List<FormOneData> getGender(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("gender", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveSpecimen(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("specimen", json);
        editor.apply();
    }

    public static List<FormOneData> getSpecimen(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("specimen", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveTesting(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("testing", json);
        editor.apply();
    }

    public static List<FormOneData> getTesting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("testing", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveExtraction(Context context, List<UserData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("extraction", json);
        editor.apply();
    }

    public static List<UserData> getExtraction(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<UserData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("extraction", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveCollectedFrom(Context context, List<com.smit.ppsa.Response.CollectedFrom.UserData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("collectedfrom", json);
        editor.apply();
    }

    public static List<com.smit.ppsa.Response.CollectedFrom.UserData> getCollectedFrom(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<com.smit.ppsa.Response.CollectedFrom.UserData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("collectedfrom", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<com.smit.ppsa.Response.CollectedFrom.UserData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveNoOfCont(Context context, List<com.smit.ppsa.Response.noOfCont.UserData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("noOfCont", json);
        editor.apply();
    }

    public static List<com.smit.ppsa.Response.noOfCont.UserData> getNoOfCont(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<com.smit.ppsa.Response.noOfCont.UserData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("noOfCont", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<com.smit.ppsa.Response.noOfCont.UserData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savePythologyLabSamples(Context context, List<com.smit.ppsa.Response.pythologylab.UserData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("pyLabsample", json);
        editor.apply();
    }

    public static List<com.smit.ppsa.Response.pythologylab.UserData> getPythologyLabSample(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<com.smit.ppsa.Response.pythologylab.UserData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("pyLabsample", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<com.smit.ppsa.Response.pythologylab.UserData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savePythologyLabTypes(Context context, List<LabResponseInternal> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("pyLabType", json);
        editor.apply();
    }

    public static List<LabResponseInternal> getPythologyLabTypes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<LabResponseInternal> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("pyLabType", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<LabResponseInternal>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveDiagnosticTestSample(Context context, List<com.smit.ppsa.Response.DiagnosticTest.UserData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("diagnosticTestSample", json);
        editor.apply();
    }

    public static List<com.smit.ppsa.Response.DiagnosticTest.UserData> getDiagnosticTestSample(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<com.smit.ppsa.Response.DiagnosticTest.UserData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("diagnosticTestSample", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<com.smit.ppsa.Response.DiagnosticTest.UserData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savetype(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("sputumTyp", json);
        editor.apply();
    }

    public static List<FormOneData> gettype(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("sputumTyp", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveState(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("state", json);
        editor.apply();
    }

    public static List<FormOneData> getState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("state", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveDistrict(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("district", json);
        editor.apply();
    }

    public static List<FormOneData> getDistrict(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("district", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveTU(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("tu", json);
        editor.apply();
    }

    public static List<FormOneData> getTU(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("tu", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveDiagnostic(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("diagnostic", json);
        editor.apply();
    }

    public static List<FormOneData> getDiagnostic(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("diagnostic", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveDST(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("dst", json);
        editor.apply();
    }

    public static List<FormOneData> getDST(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("dst", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveOtherDST(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("otherDST", json);
        editor.apply();
    }

    public static List<FormOneData> getOtherDST(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("otherDST", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveInterpretation(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("interpretation", json);
        editor.apply();
    }

    public static List<FormOneData> getInterpretation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("interpretation", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveTestResult(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("testResult", json);
        editor.apply();
    }

    public static List<FormOneData> getTestResult(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("testResult", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveCaseType(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("casetype", json);
        editor.apply();
    }

    public static List<FormOneData> getCaseType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("casetype", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savePatientStatus(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("patientStatus", json);
        editor.apply();
    }

    public static List<FormOneData> getPatientStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("patientStatus", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savePurpose(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("purpose", json);
        editor.apply();
    }

    public static List<FormOneData> getPurpose(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("purpose", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }




    public static String getPatientsNams(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("patientName", "");


    }

    public static void setPatientName(Context context, String patientNam) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patientName", patientNam);
        editor.apply();
    }



    public static String getHospitalNams(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("HospitalName", "");


    }

    public static void setHospitalName(Context context, String HosNam) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("HospitalName", HosNam);
        editor.apply();
    }



    public static String getDocNams(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("DocName", "");


    }

    public static void setDocName(Context context, String docName) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("DocName", docName);
        editor.apply();
    }


    public static String getPhoneNo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("phoneNo", "");


    }

    public static void setPhoneNo(Context context, String phoneNo) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phoneNo", phoneNo);
        editor.apply();
    }

    public static String getEnrollNo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("enrollNo", "");


    }

    public static void seEnrollNo(Context context, String enrollNo) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("enrollNo", enrollNo);
        editor.apply();
    }




}
