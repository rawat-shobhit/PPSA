package com.smit.ppsa

import android.content.Context
import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.smit.ppsa.Response.AddDocResponse

interface ApiRepository {
    suspend fun submitLabTestReport(
        n_st_id: Int,
        n_dis_id: Int,
        n_tu_id: Int,
        n_hf_id: Int,
        n_doc_id: Int,
        n_enroll_id: Int,
        n_smpl_col_id: Int,
        d_tst_rslt: String,
        n_tst_rpt: Int,
        d_rpt_col: String,
        c_tr_fp_img: Uri? = null,
        c_tr_bp_img: Uri? = null,

        n_lat: String,
        n_lng: String,
        n_staff_info: Int,
        n_user_id: Int,
        context: Context,
        n_lab_id: Int,
        progressDialog: GlobalProgressDialog,
        activity: FormSix? = null,
        offline: Boolean,
        photofront: String? = null,
        photoBack: String? = null,
        navigate: Boolean,
        n_rpt_del: String
    ): AddDocResponse?

    suspend fun submitPassword(
        userId: Int,
        password: String,
        context: LogIn? = null, passwordcontext: PasswordActivity? = null,text: String
    ): AddDocResponse?


    suspend fun submitFdcOpenStockBalance(
        n_st_id: Int,
        n_dis_id: Int,
        n_tu_id: Int,
        n_hf_id: Int,
        n_med_id: Int,
        n_uom_id: Int,
        n_qty: Int,
        n_lat: String,
        n_lng: String,
        n_staff_info: Int,
        n_user_id: Int,
        context: Context,
        progressDialog: GlobalProgressDialog,
        navigate: Boolean
    ): AddDocResponse?

    suspend fun submitFdcDispensationToHf(
        n_st_id: Int,
        n_dis_id: Int,
        n_tu_id: Int,
        n_hf_id: Int,
        n_doc_id: Int,
        d_issue: String,
        n_med_id: Int,
        n_uom_id: Int,
        n_qty: Int,
        n_lat: String,
        n_lng: String,
        n_staff_info: Int,
        n_user_id: Int,
        context: Context,
        progressDialog: GlobalProgressDialog,
        dispensationFragment: DispensationFragment? = null,
        fragmentManager: FragmentManager? = null,
        hospitalName: String? = null,
        navigate: Boolean
    ): AddDocResponse?

    suspend fun submitFdcReceived(
        d_rec: String,
        n_med: Int,
        n_uom: Int,
        n_qty: Int,
        n_sanc: Int,
        n_lat: String,
        n_lng: String,
        n_staff_info: Int,
        n_user_id: Int,
        context: Context,
        progressDialog: GlobalProgressDialog,
        dispensationFragment: DispensationFragment? = null,
        fragmentManager: FragmentManager? = null,
        hospitalName: String? = null,
        navigate: Boolean
    ): AddDocResponse?

    suspend fun submitFdcDispensationToPatient(
        n_st_id: Int,
        n_dis_id: Int,
        n_tu_id: Int,
        n_hf_id: Int,
        n_doc_id: Int,
        n_enroll_id: Int,
        d_issue: String,
        n_wght_bnd: Int,
        n_med_id: Int,
        n_uom_id: Int,
        n_qty: Int,
        n_days: Int,
        n_lat: String,
        n_lng: String,
        n_staff_info: Int,
        n_user_id: Int,
        context: Context,
        progressDialog: GlobalProgressDialog,
        activity: FdcDispensationToPatient? = null,
        navigate: Boolean
    ): AddDocResponse?

}