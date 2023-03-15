package com.smit.ppsa

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch

class FdcDispensationToHfViewModel : ViewModel() {
    val BASE_URL = "https://nikshayppsa.hlfppt.org/_api-v1_/"
    val client = HttpClient(CIO) {
        /*     install(ContentNegotiation) {
                 json(Json {
                     prettyPrint = true
                     isLenient = true
                     ignoreUnknownKeys = true
                 })
             }*/
        install(HttpTimeout) {
            requestTimeoutMillis = 25000L
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i("Logging", message)
                }
            }
            level = LogLevel.ALL
        }
        install(ResponseObserver) {
            onResponse {}
        }
    }
    val repository = ApiRepositoryImpl()
    fun submitFdcDispensationToHf(
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
    ) {
        viewModelScope.launch {
            repository.submitFdcDispensationToHf(
                n_st_id = n_st_id,
                n_dis_id = n_dis_id,
                n_tu_id = n_tu_id,
                n_hf_id = n_hf_id,
                n_doc_id = n_doc_id,
                d_issue = d_issue,
                n_med_id = n_med_id,
                n_uom_id = n_uom_id,
                n_qty = n_qty,
              //  n_etham = n_etham,
                n_lat = n_lat,
                n_lng = n_lng,
                n_staff_info = n_staff_info,
                n_user_id = n_user_id,
                context = context,
                progressDialog = progressDialog,
                dispensationFragment = dispensationFragment,
                fragmentManager = fragmentManager,
                hospitalName = hospitalName,
                navigate = navigate
            )
        }
    }

    fun getPreviousDrugDispensation(
        n_tu_id: Int,
        n_hf_id: Int,
        n_doc_id: Int,
    ): String? {
        viewModelScope.launch {
            try {
                val response: HttpResponse =
                    client.get {
                        url("https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fdc_issue&w=n_tu_id<<EQUALTO>>" + n_tu_id + "<<AND>>n_hf_id<<EQUALTO>>" + n_hf_id + "<<AND>>n_doc_id<<EQUALTO>>" + n_doc_id)
                    }
                val raw = response.readText()
                Log.d("kopok", "getPreviousDrugDispensation: $raw")
            } catch (e: Exception) {
            }
        }
        return null
    }
}