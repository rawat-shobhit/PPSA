package com.smit.ppsa

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FdcOpeningStockBalanceViewModel : ViewModel() {
    val repository = ApiRepositoryImpl()
    fun submitFdcOpenStockBalance(
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
    ) {
        viewModelScope.launch {
            repository.submitFdcOpenStockBalance(
                n_st_id = n_st_id,
                n_dis_id = n_dis_id,
                n_tu_id = n_tu_id,
                n_hf_id = n_hf_id,
                n_med_id = n_med_id,
                n_uom_id = n_uom_id,
                n_qty = n_qty,
                n_lat = n_lat,
                n_lng = n_lng,
                n_staff_info = n_staff_info,
                n_user_id = n_user_id,
                context = context,
                progressDialog = progressDialog,
                navigate = navigate
            )
        }

    }

}