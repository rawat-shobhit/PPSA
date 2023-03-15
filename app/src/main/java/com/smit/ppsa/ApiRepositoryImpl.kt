package com.smit.ppsa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import com.smit.ppsa.Dao.AppDataBase
import com.smit.ppsa.Response.AddDocResponse
import com.smit.ppsa.Response.ErrorResponse
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ApiRepositoryImpl() : ApiRepository {
    val BASE_URL = "https://nikshayppsa.hlfppt.org/_api-v1_/"
    lateinit var dataBase: AppDataBase
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


    override suspend fun submitLabTestReport(
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
        c_tr_fp_img: Uri?,
        c_tr_bp_img: Uri?,

        n_lat: String,
        n_lng: String,
        n_staff_info: Int,
        n_user_id: Int,
        context: Context,
        n_lab_id: Int,
        progressDialog: GlobalProgressDialog,
        activity: FormSix?,
        offline: Boolean,
        photofront: String?,
        photoBack: String?,
        navigate: Boolean,
        n_rpt_del: String
    ): AddDocResponse? {
        BaseUtils.putSubmitLabReportStatus(context, "true")
        var res: AddDocResponse? = null
        val dataBase = AppDataBase.getDatabase(context)
        val inputStreamFront = c_tr_fp_img?.let { context.contentResolver.openInputStream(it) }
        val imageByteArrayFront = inputStreamFront?.readBytes()
        var encodedStringFront: String? = null
        if (imageByteArrayFront?.equals(null) == false) {
            encodedStringFront = getBase64String().convertByteArray(imageByteArrayFront)
        }
        val inputStreamBack = c_tr_bp_img?.let { context.contentResolver.openInputStream(it) }
        val imageByteArrayBack = inputStreamBack?.readBytes()
        var encodedStringBack: String? = null
        if (imageByteArrayBack?.equals(null) == false) {
            encodedStringBack = getBase64String().convertByteArray(imageByteArrayBack)
        }

        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(
                context,
                "Please Check your internet  Connectivity",
                Toast.LENGTH_SHORT
            ).show()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            if (c_tr_fp_img != null && c_tr_bp_img != null) {
                BaseUtils.putLabTestReportData(
                    context,
                    n_st_id.toString(),
                    n_dis_id.toString(),
                    n_tu_id.toString(),
                    n_hf_id.toString(),
                    n_doc_id.toString(),
                    n_enroll_id.toString(),
                    n_smpl_col_id.toString(),
                    d_tst_rslt,
                    n_tst_rpt.toString(),
                    d_rpt_col,
                    Imagee().getEncodedImage(c_tr_fp_img!!, context),
                    Imagee().getEncodedImage(c_tr_bp_img!!, context),

                    n_lat,
                    n_lng,
                    n_staff_info.toString(),
                    n_user_id.toString(),
                    "0",
                    n_rpt_del
                )
            }
            Toast.makeText(
                context,
                "Data will be submitted when you are back online",
                Toast.LENGTH_SHORT
            ).show()
            if (navigate) {
                startActivity(
                    context,
                    Intent(
                        context,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    /*  .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)*/, null
                )
                if (activity != null) {
                    finishAffinity(activity)
                }
            }
            progressDialog.hideProgressBar()
            return null
        }

        if (imageByteArrayFront != null && imageByteArrayBack != null) {

            Log.d("koiu", "submitLabTestReport: b" + n_st_id)
            Log.d("koiu", "submitLabTestReport: b" + n_dis_id)
            Log.d("koiu", "submitLabTestReport: b" + n_tu_id)
            Log.d("koiu", "submitLabTestReport: b" + n_hf_id)
            Log.d("koiu", "submitLabTestReport: b" + n_doc_id)
            Log.d("koiu", "submitLabTestReport: b" + n_enroll_id)
            Log.d("koiu", "submitLabTestReport: b" + n_smpl_col_id)
            Log.d("koiu", "submitLabTestReport: b" + d_tst_rslt)
            Log.d("koiu", "submitLabTestReport: b" + n_tst_rpt)
            Log.d("koiu", "submitLabTestReport: b" + d_rpt_col)
//            Log.d("koiu", "submitLabTestReport: b" + d_lpa_smpl)
//            Log.d("koiu", "submitLabTestReport: b" + n_lpa_rslt)
            Log.d("koiu", "submitLabTestReport: b" + n_lat)
            Log.d("koiu", "submitLabTestReport: b" + n_lng)
            Log.d("koiu", "submitLabTestReport: b" + n_staff_info)
            Log.d("koiu", "submitLabTestReport: b" + n_user_id)
            Log.d("koiu", "submitLabTestReport: b" + n_lab_id)
            Log.d("koiu", "submitLabTestReport:f " + encodedStringFront)
            Log.d("koiu", "submitLabTestReport: b" + encodedStringBack)

/*            try {*/
            val response: HttpResponse = try {
                client.post("${BASE_URL}_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_rpt_delv") {

                    body = MultiPartFormDataContent(
                        formData {
                            append("n_st_id", n_st_id)
                            append("n_dis_id", n_dis_id)
                            append("n_tu_id", n_tu_id)
                            append("n_hf_id", n_hf_id)
                            append("n_doc_id", n_doc_id)
                            append("n_enroll_id", n_enroll_id)
                            append("n_smpl_col_id", n_smpl_col_id)
                            append("d_tst_rslt", d_tst_rslt)
                            append("n_tst_rpt", n_tst_rpt)
                            append("d_rpt_col", d_rpt_col)
                            if (encodedStringFront != null) {
                                append("c_tr_fp_img", encodedStringFront)
                            }
                            if (encodedStringBack != null) {
                                append("c_tr_bp_img", encodedStringBack)
                            }
                            /*            append("c_tr_fp_img", imageByteArrayFront, Headers.build {
                                            append(HttpHeaders.ContentType, "image/png")
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"ktor_logo.png\""
                                            )
                                        })
                                        append("c_tr_bp_img", imageByteArrayBack, Headers.build {
                                            append(HttpHeaders.ContentType, "image/png")
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"ktor_logo.png\""
                                            )
                                        })*/
//                            append("d_lpa_smpl", d_lpa_smpl)
//                            append("n_lpa_rslt", n_lpa_rslt)
                            append("n_lat", n_lat)
                            append("n_lng", n_lng)
                            append("n_staff_info", n_staff_info)
                            append("n_user_id", n_user_id)
                            append("n_rpt_del", n_rpt_del)
                        },
                        //  boundary = "WebAppBoundary"
                    )


                }
            } catch (e: Exception) {
                Log.d("TAGGG", "getCartItems: Error fetching items")
                /*BaseUtils.putSubmitLabReportStatus(context,false)
                BaseUtils.putLabTestReportData(
                    context,
                    n_st_id,
                    n_dis_id,
                    n_tu_id,
                    n_hf_id,
                    n_doc_id,
                    n_enroll_id,
                    n_smpl_col_id,
                    d_tst_rslt,
                    n_tst_rpt,
                    d_rpt_col,
                    encodedStringFront,
                    encodedStringBack,
                    d_lpa_smpl,
                    n_lpa_rslt,
                    n_lat,
                    n_lng,
                    n_staff_info,
                    n_user_id,
                    n_lab_id
                )*/
                return null
                // return null
            }
            if (response.status == HttpStatusCode.OK) {
                val raw = response.readText()
                val result = Gson().fromJson(raw, AddDocResponse::class.java)
                Log.d("eeerre", "onStart: $result")
                res = result
                //dataBase.customerDao().insertFormSix(formOneModel)
                BaseUtils.putSubmitLabReportStatus(context, "true")
                //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                progressDialog.hideProgressBar()


                //    parentData = response.body().getUser_data();
                Log.d("gug", "onResponse: " + result.getMessage())
                Toast.makeText(
                    context,
                    result.getMessage(),
                    Toast.LENGTH_SHORT
                ).show()
                if (navigate) {
                    Toast.makeText(
                        context,
                        result.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        context,
                        Intent(
                            context,
                            MainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        /*  .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)*/, null
                    )
                    if (activity != null) {
                        finishAffinity(activity)
                    }
                }

                /*  startActivity()*/
            } else {
                BaseUtils.putSubmitLabReportStatus(context, "false")

                val raw = response.readText()
                val result = Gson().fromJson(raw, ErrorResponse::class.java)
                Log.d("eeerre", "onStart: $result")
                Toast.makeText(context, "${result.message}", Toast.LENGTH_SHORT).show()
                //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                progressDialog.hideProgressBar()
            }
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()
            /*   } catch (e: Exception) {

                   Log.d("TAG", "addShop: error")
               }*/

        } else {
            val response: HttpResponse = try {
                client.post("${BASE_URL}_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_rpt_delv") {

                    body = MultiPartFormDataContent(
                        formData {
                            append("n_st_id", n_st_id)
                            append("n_dis_id", n_dis_id)
                            append("n_tu_id", n_tu_id)
                            append("n_hf_id", n_hf_id)
                            append("n_doc_id", n_doc_id)
                            append("n_enroll_id", n_enroll_id)
                            append("n_smpl_col_id", n_smpl_col_id)
                            append("d_tst_rslt", d_tst_rslt)
                            append("n_tst_rpt", n_tst_rpt)
                            append("d_rpt_col", d_rpt_col)
                            append("c_tr_fp_img", photofront.toString())
                            append("c_tr_bp_img", photoBack.toString())
//                            append("d_lpa_smpl", d_lpa_smpl)
//                            append("n_lpa_rslt", n_lpa_rslt)
                            append("n_lat", n_lat)
                            append("n_lng", n_lng)
                            append("n_staff_info", n_staff_info)
                            append("n_user_id", n_user_id)
                            append("n_rpt_del", n_rpt_del)
                        },
                        //  boundary = "WebAppBoundary"
                    )


                }
            } catch (e: Exception) {
                Log.d("TAGGG", "getCartItems: Error fetching items")
                /*    BaseUtils.putSubmitLabReportStatus(context,false)
                    BaseUtils.putLabTestReportData(
                        context,
                        n_st_id,
                        n_dis_id,
                        n_tu_id,
                        n_hf_id,
                        n_doc_id,
                        n_enroll_id,
                        n_smpl_col_id,
                        d_tst_rslt,
                        n_tst_rpt,
                        d_rpt_col,
                        encodedStringFront,
                        encodedStringBack,
                        d_lpa_smpl,
                        n_lpa_rslt,
                        n_lat,
                        n_lng,
                        n_staff_info,
                        n_user_id,
                        n_lab_id
                    )*/
                return null
                // return null
            }
            if (response.status == HttpStatusCode.OK) {
                val raw = response.readText()
                val result = Gson().fromJson(raw, AddDocResponse::class.java)
                Log.d("eeerre", "onStart: $result")
                res = result

                BaseUtils.putSubmitLabReportStatus(context, "true")
                //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                progressDialog.hideProgressBar()


                //    parentData = response.body().getUser_data();
                Log.d("gug", "onResponse: " + result.getMessage())
                if (navigate) {
                    Toast.makeText(
                        context,
                        result.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        context,
                        Intent(
                            context,
                            MainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        /*  .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)*/, null
                    )
                    if (activity != null) {
                        finishAffinity(activity)
                    }
                }

                /*  startActivity()*/
            } else {
                BaseUtils.putSubmitLabReportStatus(context, "false")
                val raw = response.readText()
                val result = Gson().fromJson(raw, ErrorResponse::class.java)
                Log.d("eeerre", "onStart: $result")
                Toast.makeText(context, "${result.message}", Toast.LENGTH_SHORT).show()
                //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                progressDialog.hideProgressBar()
            }
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()
            /*   } catch (e: Exception) {

                   Log.d("TAG", "addShop: error")
               }*/

        }
        progressDialog.hideProgressBar()
        return res
    }


    override suspend fun submitFdcOpenStockBalance(
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
    ): AddDocResponse? {
        var res: AddDocResponse? = null
        BaseUtils.putSubmitFdcDispensationOpForm(context, "false")
        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(
                context,
                "Please Check your internet  Connectivity",
                Toast.LENGTH_SHORT
            ).show()

            BaseUtils.putFdCOPForm(
                n_st_id,
                n_dis_id,
                n_tu_id,
                n_hf_id,
                n_med_id,
                n_uom_id,
                n_qty,
                n_lat,
                n_lng,
                n_staff_info,
                n_user_id,
                context
            )
            Toast.makeText(
                context,
                "Data will be submitted when you are back online",
                Toast.LENGTH_SHORT
            ).show()
            if (navigate) {
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as Activity).finishAffinity()
            }
            progressDialog.hideProgressBar()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
        }

        Log.d("uioi", "submitFdcOpenStockBalance: n_st_id" + ": " + n_st_id)
        Log.d("uioi", "submitFdcOpenStockBalance: n_dis_id" + ": " + n_dis_id)
        Log.d("uioi", "submitFdcOpenStockBalance: n_tu_id" + ": " + n_tu_id)
        Log.d("uioi", "submitFdcOpenStockBalance: n_hf_id" + ": " + n_hf_id)
        Log.d("uioi", "submitFdcOpenStockBalance: n_fdc2" + ": " + n_med_id)
        Log.d("uioi", "submitFdcOpenStockBalance: n_fdc3" + ": " + n_uom_id)
        Log.d("uioi", "submitFdcOpenStockBalance: n_fdc4" + ": " + n_qty)
        // Log.d("uioi", "submitFdcOpenStockBalance: n_entham" + ": " + n_entham)
        Log.d("uioi", "submitFdcOpenStockBalance: n_lat" + ": " + n_lat)
        Log.d("uioi", "submitFdcOpenStockBalance: n_lng" + ": " + n_lng)
        Log.d("uioi", "submitFdcOpenStockBalance: n_staff_info" + ": " + n_staff_info)
        Log.d("uioi", "submitFdcOpenStockBalance: n_user_id" + ": " + n_user_id)

        val response: HttpResponse = try {

            client.post("${BASE_URL}_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_obal") {

                body = MultiPartFormDataContent(
                    formData {
                        append("n_st_id", n_st_id)
                        append("n_dis_id", n_dis_id)
                        append("n_tu_id", n_tu_id)
                        append("n_hf_id", n_hf_id)
                        append("n_med_id", n_med_id)
                        append("n_uom_id", n_uom_id)
                        append("n_qty", n_qty)
                        //   append("n_etham", n_entham)
                        append("n_lat", n_lat)
                        append("n_lng", n_lng)
                        append("n_staff_info", n_staff_info)
                        append("n_user_id", n_user_id)
                    }
                )
            }
        } catch (e: Exception) {
            return null
        }

        if (response.status == HttpStatusCode.OK) {
            BaseUtils.putSubmitFdcDispensationOpForm(context, "true")
            val raw = response.readText()
            val result = Gson().fromJson(raw, AddDocResponse::class.java)
            Log.d("eeerre", "onStart: $result")
            res = result
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()


            //    parentData = response.body().getUser_data();
            Log.d("gug", "onResponse: " + result.getMessage())
            Toast.makeText(
                context,
                result.getMessage(),
                Toast.LENGTH_SHORT
            ).show()
            if (navigate) {
                startActivity(
                    context,
                    Intent(
                        context,
                        HospitalsList::class.java
                    ).putExtra("fdc", "fdc").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                    null
                )
                (context as Activity).finishAffinity()
            }

        } else {
            BaseUtils.putSubmitFdcDispensationOpForm(context, "false")
        }
        progressDialog.hideProgressBar()
        return res
    }


    override suspend fun submitPassword(
        userId: Int,
        password: String,
        context: LogIn?,
        passwordcontext: PasswordActivity?, text: String
    ): AddDocResponse? {
        val progressDialog =
            if (context != null) GlobalProgressDialog(context) else GlobalProgressDialog(
                passwordcontext
            )
        progressDialog.show()
        val res: AddDocResponse? = null

        val response: HttpResponse = try {

            val con = if (context != null) {
                context
            } else passwordcontext
            client.post(
                "${BASE_URL}_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_user_mst&w=id<<EQUALTO>>${
                   userId
                }"
            ) {

                body = MultiPartFormDataContent(
                    formData {
                        //append("w", userId)
                        append("c_password", password)
                    })
            }
        } catch (e: Exception) {
            return null
        }
        if (response.status == HttpStatusCode.OK) {
            Log.d("h7uh", "submitPassword: okoiuio")
            //  Log.d("h7uh", "submitPassword: $response.")
            if (context != null) {
                Toast.makeText(context, "logged in success", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, WorkerForm::class.java))
                context.finish()
            }
            if (passwordcontext != null) {
                Toast.makeText(passwordcontext, "password change success", Toast.LENGTH_SHORT)
                    .show()
                if (text.equals("existingUser")) {
                    passwordcontext.finish()
                } else {
                    passwordcontext.startActivity(
                        Intent(
                            passwordcontext,
                            LogIn::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    passwordcontext.finish()
                }

            }
            progressDialog.hideProgressBar()
        }
        progressDialog.hideProgressBar()

        return res
    }

    override suspend fun submitFdcDispensationToHf(
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
        dispensationFragment: DispensationFragment?,
        fragmentManager: FragmentManager?,
        hospitalName: String?,
        navigate: Boolean
    ): AddDocResponse? {

        var res: AddDocResponse? = null
        Log.d("lopopk", "submitFdcDispensationToHf:One   ${n_st_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_dis_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_tu_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_hf_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_doc_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${d_issue}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_med_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_uom_id}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_qty}")
        //  Log.d("lopopk", "submitFdcDispensationToHf: ${n_etham}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_lat}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_lng}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_staff_info}")
        Log.d("lopopk", "submitFdcDispensationToHf:last   ${n_user_id}")

        BaseUtils.putSubmitFdcDispensationHfForm(context, "false")
        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT)
                .show()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()
            BaseUtils.putFdcHfForm(
                n_st_id,
                n_dis_id,
                n_tu_id,
                n_hf_id,
                n_doc_id,
                d_issue,
                n_med_id,
                n_uom_id,
                n_qty,
                0,
                n_lat,
                n_lng,
                n_staff_info,
                n_user_id,
                context
            )
            if (navigate) {
                /* Toast.makeText(
                     context,
                     "Data will be submitted when back online",
                     Toast.LENGTH_SHORT
                 ).show()
                 context.startActivity(Intent(context, MainActivity::class.java))*/
                dispensationFragment?.isCancelable = false
                val bundle = Bundle()
                bundle.putString("hf_id", n_hf_id.toString())
                bundle.putString("tu_id", n_tu_id.toString())
                bundle.putString("hospitalName", hospitalName)
                dispensationFragment?.arguments = bundle
                fragmentManager?.let { dispensationFragment?.show(it, "dispensationfrag") }

            }
            return null
        }


        val response: HttpResponse = try {

            client.post("${BASE_URL}_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_issue") {

                body = MultiPartFormDataContent(
                    formData {
                        append("n_st_id", n_st_id)
                        append("n_dis_id", n_dis_id)
                        append("n_tu_id", n_tu_id)
                        append("n_hf_id", n_hf_id)
                        append("n_doc_id", n_doc_id)
                        append("d_issue", d_issue)
                        append("n_med_id", n_med_id)
                        append("n_uom_id", n_uom_id)
                        append("n_qty", n_qty)
                        // append("n_etham", n_etham)
                        append("n_lat", n_lat)
                        append("n_lng", n_lng)
                        append("n_staff_info", n_staff_info)
                        append("n_user_id", n_user_id)
                    }
                )
            }
        } catch (e: Exception) {
            return null
        }

        if (response.status == HttpStatusCode.OK) {
            BaseUtils.putSubmitFdcDispensationHfForm(context, "true")
            val raw = response.readText()
            val result = Gson().fromJson(raw, AddDocResponse::class.java)
            Log.d("eeerre", "onStart: $result")
            res = result
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()
            dispensationFragment?.isCancelable = false
            val bundle = Bundle()
            bundle.putString("hf_id", n_hf_id.toString())
            bundle.putString("tu_id", n_tu_id.toString())
            bundle.putString("hospitalName", hospitalName)
            dispensationFragment?.arguments = bundle
            fragmentManager?.let { dispensationFragment?.show(it, "dispensationfrag") }

            //    parentData = response.body().getUser_data();
            Log.d("gug", "onResponse: " + result.getMessage())
            Toast.makeText(
                context,
                result.getMessage(),
                Toast.LENGTH_SHORT
            ).show()
            /*    startActivity(
                    context,
                    Intent(
                        context,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    null
                )*/
        } else {
            BaseUtils.putSubmitFdcDispensationHfForm(context, "false")
        }
        progressDialog.hideProgressBar()
        return res

    }
    override suspend fun submitFdcReceived(
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
        dispensationFragment: DispensationFragment? ,
        fragmentManager: FragmentManager? ,
        hospitalName: String?,
        navigate: Boolean
    ): AddDocResponse? {

        dataBase = AppDataBase.getDatabase(context)

        var res: AddDocResponse? = null
        Log.d("lopopk", "submitFdcDispensationToHf:One   ${d_rec}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_med}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_uom}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_qty}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_sanc}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_lat}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_lng}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_staff_info}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_qty}")
        //  Log.d("lopopk", "submitFdcDispensationToHf: ${n_etham}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_lat}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_lng}")
        Log.d("lopopk", "submitFdcDispensationToHf: ${n_staff_info}")
        Log.d("lopopk", "submitFdcDispensationToHf:last   ${n_user_id}")

        BaseUtils.putSubmitFdcReceiveForm(context, "false")
        val model = FdcReceivedModel(
            d_rec,n_med.toString(),n_uom.toString(),n_qty.toString(),n_sanc.toString(),n_lat,n_lng,n_staff_info.toString(), n_user_id.toString()
        )
        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT)
                .show()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()

            dataBase.customerDao().insertFdcReceived(model)


            if (navigate) {
                 Toast.makeText(
                     context,
                     "Data will be submitted when back online",
                     Toast.LENGTH_SHORT
                 ).show()
                startActivity(
                    context,
                    Intent(
                        context,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    null
                )
              /*  dispensationFragment?.isCancelable = false
                val bundle = Bundle()
                bundle.putString("hf_id", n_hf_id.toString())
                bundle.putString("tu_id", n_tu_id.toString())
                bundle.putString("hospitalName", hospitalName)
                dispensationFragment?.arguments = bundle
                fragmentManager?.let { dispensationFragment?.show(it, "dispensationfrag") }*/

            }
            return null
        }


        val response: HttpResponse = try {

            client.post("${BASE_URL}_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_rec") {

                body = MultiPartFormDataContent(
                    formData {
                        append("d_rec", d_rec)
                        append("n_med", n_med)
                        append("n_uom", n_uom)
                        append("n_qty", n_qty)
                        append("n_sanc", n_sanc)
            /*            append("d_issue", d_issue)
                        append("n_med_id", n_med_id)
                        append("n_uom_id", n_uom_id)
                        append("n_qty", n_qty)*/
                        // append("n_etham", n_etham)
                        append("n_lat", n_lat)
                        append("n_lng", n_lng)
                        append("n_staff_info", n_staff_info)
                        append("n_user_id", n_user_id)
                    }
                )
            }
        } catch (e: Exception) {
            return null
        }

        if (response.status == HttpStatusCode.OK) {
            BaseUtils.putSubmitFdcReceiveForm(context, "true")
            val raw = response.readText()
            val result = Gson().fromJson(raw, AddDocResponse::class.java)
            Log.d("eeerre", "onStart: $result")
            res = result
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()
           /* dispensationFragment?.isCancelable = false
            val bundle = Bundle()
            bundle.putString("hf_id", n_hf_id.toString())
            bundle.putString("tu_id", n_tu_id.toString())
            bundle.putString("hospitalName", hospitalName)
            dispensationFragment?.arguments = bundle
            fragmentManager?.let { dispensationFragment?.show(it, "dispensationfrag") }*/

            //    parentData = response.body().getUser_data();
            Log.d("gug", "onResponse: " + result.getMessage())
            Toast.makeText(
                context,
                result.getMessage(),
                Toast.LENGTH_SHORT
            ).show()
                startActivity(
                    context,
                    Intent(
                        context,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    null
                )
        } else {
            BaseUtils.putSubmitFdcReceiveForm(context, "false")
        }
        progressDialog.hideProgressBar()
        return res

    }

    override suspend fun submitFdcDispensationToPatient(
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
        activity: FdcDispensationToPatient?,
        navigate: Boolean
    ): AddDocResponse? {

        var res: AddDocResponse? = null
        BaseUtils.putSubmitFdcDispensationPaForm(context, "false")
        if (!BaseUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT)
                .show()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()
            BaseUtils.putFdcPatientForm(
                n_st_id,
                n_dis_id,
                n_tu_id,
                n_hf_id,
                n_doc_id,
                n_enroll_id,
                d_issue,
                n_wght_bnd,
                n_med_id,
                n_uom_id,
                n_qty,
                n_days,
                n_lat,
                n_lng,
                n_staff_info,
                n_user_id,
                context
            )
            if (navigate) {
                Toast.makeText(
                    context,
                    "Data will be submitted when back online",
                    Toast.LENGTH_SHORT
                ).show()
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as Activity).finishAffinity()
            }
            return null
        }
        val response: HttpResponse = try {

            Log.d("uioi", "submitFdcOpenStockBalance: n_st_id" + ": " + n_st_id)
            Log.d("uioi", "submitFdcOpenStockBalance: n_dis_id" + ": " + n_dis_id)
            Log.d("uioi", "submitFdcOpenStockBalance: n_tu_id" + ": " + n_tu_id)
            Log.d("uioi", "submitFdcOpenStockBalance: n_hf_id" + ": " + n_hf_id)
            Log.d("uioi", "submitFdcOpenStockBalance: n_doc_id" + ": " + n_doc_id)
            Log.d("uioi", "submitFdcOpenStockBalance: n_enroll_id" + ": " + n_enroll_id)
            Log.d("uioi", "submitFdcOpenStockBalance: d_issue" + ": " + d_issue)
            Log.d("uioi", "submitFdcOpenStockBalance: n_fdc2" + ": " + n_med_id)
            Log.d("uioi", "submitFdcOpenStockBalance: n_fdc3" + ": " + n_qty)
            Log.d("uioi", "submitFdcOpenStockBalance: n_fdc4" + ": " + n_days)
            //   Log.d("uioi", "submitFdcOpenStockBalance: n_etham" + ": " + n_etham)
            Log.d("uioi", "submitFdcOpenStockBalance: n_lat" + ": " + n_lat)
            Log.d("uioi", "submitFdcOpenStockBalance: n_lng" + ": " + n_lng)
            Log.d("uioi", "submitFdcOpenStockBalance: n_staff_info" + ": " + n_staff_info)
            Log.d("uioi", "submitFdcOpenStockBalance: n_user_id" + ": " + n_user_id)

            client.post("${BASE_URL}_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_disp") {

                body = MultiPartFormDataContent(
                    formData {
                        append("n_st_id", n_st_id)
                        append("n_dis_id", n_dis_id)
                        append("n_tu_id", n_tu_id)
                        append("n_hf_id", n_hf_id)
                        append("n_doc_id", n_doc_id)
                        append("n_enroll_id", n_enroll_id)
                        append("d_disp", d_issue)
                        append("n_wght_bnd", n_wght_bnd)
                        append("n_med_id", n_med_id)
                        append("n_uom_id", n_uom_id)
                        append("n_qty", n_qty)
                        append("n_days", n_days)
                        append("n_lat", n_lat)
                        append("n_lng", n_lng)
                        append("n_staff_info", n_staff_info)
                        append("n_user_id", n_user_id)
                    }
                )
            }
        } catch (e: Exception) {
            return null
        }

        if (response.status == HttpStatusCode.OK) {
            BaseUtils.putSubmitFdcDispensationPaForm(context, "true")
            val raw = response.readText()
            val result = Gson().fromJson(raw, AddDocResponse::class.java)
            Log.d("eeerre", "onStart: $result")
            res = result
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar()

            //    parentData = response.body().getUser_data();
            Log.d("gug", "onResponse: " + result.getMessage())
            Toast.makeText(
                context,
                result.getMessage(),
                Toast.LENGTH_SHORT
            ).show()
            if (navigate) {
                startActivity(
                    context,
                    Intent(
                        context,
                        MainActivity::class.java
                    )/*.putExtra("fdc", "fdc").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)*/,
                    null
                )

                (context as Activity).finishAffinity()
            }


        } else {
            BaseUtils.putSubmitFdcDispensationPaForm(context, "false")
        }
        progressDialog.hideProgressBar()
        return res

    }

}