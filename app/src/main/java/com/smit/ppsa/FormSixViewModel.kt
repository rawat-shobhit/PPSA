package com.smit.ppsa

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FormSixViewModel : ViewModel() {

    val repository = ApiRepositoryImpl()
    fun submitLabReport(
        n_st_id: String,
        n_dis_id: String,
        n_tu_id: String,
        n_hf_id: String,
        n_doc_id: String,
        n_enroll_id: String,
        n_smpl_col_id: String,
        d_tst_rslt: String,
        n_tst_rpt: String,
        d_rpt_col: String,
        c_tr_fp_img: Uri? = null,
        c_tr_bp_img: Uri? = null,

        n_lat: String,
        n_lng: String,
        n_staff_info: String,
        n_user_id: String,
        context: Context,
        n_lab_id: Int,
        progressDialog: GlobalProgressDialog,
        activity: FormSix? =null,
        offline: Boolean,
        photofront: String? = null,
        photoBack: String? = null,
        navigate: Boolean,
        n_rpt_del: String
    ){
        viewModelScope.launch {


            Log.d("juioweq", "submitLabTestReport: "+n_st_id)

                    repository.submitLabTestReport(
                        n_st_id = n_st_id.toInt(),
                        n_dis_id = n_dis_id.toInt(),
                        n_tu_id = n_tu_id.toInt(),
                        n_hf_id = n_hf_id.toInt(),
                        n_doc_id = n_doc_id.toInt(),
                        n_enroll_id = n_enroll_id.toInt(),
                        n_smpl_col_id = n_smpl_col_id.toInt(),
                        d_tst_rslt = d_tst_rslt,
                        n_tst_rpt = n_tst_rpt.toInt(),
                        d_rpt_col = d_rpt_col,
                        c_tr_fp_img = c_tr_fp_img,
                        c_tr_bp_img = c_tr_bp_img,

                        n_lat = n_lat,
                        n_lng = n_lng,
                        n_staff_info = n_staff_info.toInt(),
                        n_user_id = n_user_id.toInt(),
                        context = context,
                        n_lab_id = 0,
                        progressDialog = progressDialog,
                        activity = activity,
                        offline,
                        photofront,
                        photoBack,
                        navigate,n_rpt_del
                    )


        }

    }
}