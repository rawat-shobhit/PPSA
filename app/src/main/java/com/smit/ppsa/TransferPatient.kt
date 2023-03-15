package com.smit.ppsa

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smit.ppsa.Adapter.CustomSpinnerAdapter
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Network.NetworkCalls
import com.smit.ppsa.Response.AddDocResponse
import com.smit.ppsa.Response.FormOneData
import com.smit.ppsa.Response.FormOneResponse
import com.smit.ppsa.Response.RegisterParentData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TransferPatient : AppCompatActivity() {
    private lateinit var date: TextView
    private lateinit var submit: CardView
    private lateinit var stateSpinner: Spinner
    private lateinit var districtSpinner: Spinner
    private lateinit var tuSpinner: Spinner
    private lateinit var backbtn: ImageView
    private var st_id_res: String= ""
    private var patient: RegisterParentData = RegisterParentData()
    private var lat = ""
    private var lng = ""
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var dis_id_res: String= ""
    private var tu_id_res: String= ""
    private var state: MutableList<FormOneData> = java.util.ArrayList()
    private var district: MutableList<FormOneData> = java.util.ArrayList()
    private var tu: MutableList<FormOneData> = java.util.ArrayList()
    var stateStrings: MutableList<String> = java.util.ArrayList()
    var distri: MutableList<String> = java.util.ArrayList()
    var tuStrings: MutableList<String> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_patient)
        init()
    }
    private fun init(){
        patient = intent.getSerializableExtra("pat_ob") as RegisterParentData
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        LocalBroadcastManager.getInstance(
            Objects.requireNonNull(
                applicationContext
            )
        ).registerReceiver(broadcastReceiver, IntentFilter(""))

        date = findViewById(R.id.date)
        submit = findViewById(R.id.bt_proceedone)
        stateSpinner = findViewById(R.id.f1_residentialstate)
        backbtn = findViewById(R.id.backbtn)
        districtSpinner = findViewById(R.id.f1_residentialdistrict)
        tuSpinner = findViewById(R.id.f1_residentialTu)
        NetworkCalls.getState(this)

        stateSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i == 0) {
                    st_id_res = ""
                } else {
                    st_id_res = state.get(i - 1).getId()
                    getDistrict(this@TransferPatient, st_id_res)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
        tuSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i == 0) {
                    tu_id_res = ""
                } else {
                    tu_id_res = tu.get( /*i - 1*/i)
                        .getN_tu_id() //selecting the second value in list first value is null
                    //tu_id_res = tu.get(i - 2).getN_tu_id();
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
        districtSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i == 0) {
                    dis_id_res = ""
                } else {
                    dis_id_res = district.get(i - 1).getId()
                    getTU(this@TransferPatient, st_id_res, dis_id_res)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })
        submit.setOnClickListener {
            if (date.text.toString().isEmpty()){
                BaseUtils.showToast(this,"Please select date of transfer")
            }else if (stateSpinner.selectedItemPosition==0){
                BaseUtils.showToast(this,"Please select state")
            }else if (districtSpinner.selectedItemPosition==0){
                BaseUtils.showToast(this,"Please select district")
            }else if (tuSpinner.selectedItemPosition==0){
                BaseUtils.showToast(this,"Please select TU")
            }else{
                transferPat()
            }
        }
        backbtn.setOnClickListener { super.onBackPressed() }
        setUpCalender()
        getLocation()


    }
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 123
            )
            return
        }
        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lat = location.latitude.toString()
                lng = location.longitude.toString()
                //                  lat=30.977006;
//                  lng=76.537880;
                //saveAddress();
            } else {
                // Toast.makeText(HomeActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }.addOnFailureListener {
            //  Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    fun getDistrict(context: Context?, state_id: String) {
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            LocalBroadcastManager.getInstance(context!!)
                .sendBroadcast(Intent().setAction("").putExtra("localDistrict", ""))
            return
        }
        val url =
            "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_dist&w=n_st_id<<EQUALTO>>$state_id"
        ApiClient.getClient().getDistrictFromState(url)
            .enqueue(object : Callback<FormOneResponse?> {
                override fun onResponse(
                    call: Call<FormOneResponse?>,
                    response: Response<FormOneResponse?>
                ) {
                    if (response.isSuccessful) {
                        assert(response.body() != null)
                        if (response.body()!!.status) {
                            BaseUtils.saveDistrict(context, response.body()!!.userData)
                            LocalBroadcastManager.getInstance(context!!)
                                .sendBroadcast(Intent().setAction("").putExtra("localDistrict", ""))
                        } else {
                            LocalBroadcastManager.getInstance(context!!)
                                .sendBroadcast(Intent().setAction("").putExtra("localDistrict", ""))
                        }
                    } else {
                        LocalBroadcastManager.getInstance(context!!)
                            .sendBroadcast(Intent().setAction("").putExtra("localDistrict", ""))
                    }
                }

                override fun onFailure(call: Call<FormOneResponse?>, t: Throwable) {
                    LocalBroadcastManager.getInstance(context!!)
                        .sendBroadcast(Intent().setAction("").putExtra("localDistrict", ""))
                }
            })
    }

    fun getTU(context: Context?, st_id: String, dis_id: String) {
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            LocalBroadcastManager.getInstance(context!!)
                .sendBroadcast(Intent().setAction("").putExtra("localTU", ""))
            return
        }
        Log.d("ihsi", "getTU: W  " + BaseUtils.getUserInfo(context).getnAccessRights())
        Log.d("ihsi", "getTU: sanc  " + BaseUtils.getUserInfo(context).n_staff_sanc)
        val url =
            "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_tu&w=n_st_id<<EQUALTO>>$st_id<<AND>>n_dis_id<<EQUALTO>>$dis_id"
        //String url = "_sptu_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_tu&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserOtherInfo(context).getN_staff_sanc();
        Log.d("ihsi", "getTU: sanc  $url")
        val TuList: List<FormOneData> = ArrayList()
        ApiClient.getClient().getFormTU(url).enqueue(object : Callback<FormOneResponse?> {
            override fun onResponse(
                call: Call<FormOneResponse?>,
                response: Response<FormOneResponse?>
            ) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    if (response.body()!!.status) {
                        Log.d("uhuhoy", "onResponse: " + response.body()!!.userData.size)
                        BaseUtils.saveTU(context, response.body()!!.userData)
                        LocalBroadcastManager.getInstance(context!!)
                            .sendBroadcast(Intent().setAction("").putExtra("localTU", ""))
                    } else {
                        BaseUtils.saveTU(context, TuList)
                        LocalBroadcastManager.getInstance(context!!)
                            .sendBroadcast(Intent().setAction("").putExtra("localTU", ""))
                    }
                } else {
                    LocalBroadcastManager.getInstance(context!!)
                        .sendBroadcast(Intent().setAction("").putExtra("localTU", ""))
                }
            }

            override fun onFailure(call: Call<FormOneResponse?>, t: Throwable) {
                LocalBroadcastManager.getInstance(context!!)
                    .sendBroadcast(Intent().setAction("").putExtra("localTU", ""))
            }
        })
    }
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.hasExtra("localDistrict")) {
                district = BaseUtils.getDistrict(this@TransferPatient)
                if (distri.size > 0) {
                    distri.clear()
                }
                for (district in district) {
                    distri.add(district.getcDisNam())
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(districtSpinner, distri)
            }
            if (intent.hasExtra("localTU")) {
                tu = BaseUtils.getTU(this@TransferPatient)
                if (tuStrings.size > 0) {
                    tuStrings.clear()
                }
                for (a in tu.indices) {
                    if (a > 0) {
                        if (!tuStrings.contains(tu[a].getcTuName())) {
                            tuStrings.add(tu[a].getcTuName())
                        }
                    }
                }
                /*for (FormOneData tu : tu) {
                    tuStrings.add(tu.getcTuName());
                }*/
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(tuSpinner, tuStrings)
            }
            if (intent.hasExtra("localState")) {
                state = BaseUtils.getState(this@TransferPatient)
                for (stat in state) {
                    stateStrings.add(stat.getcStNam())
                }
                setSpinnerAdapter(stateSpinner, stateStrings)
            }
        }
    }
    private fun setSpinnerAdapter(spinner: Spinner, values: List<String>) {
        val spinnerAdapter = CustomSpinnerAdapter(this@TransferPatient, values)
        spinner.adapter = spinnerAdapter
    }

    private fun transferPat(){
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            LocalBroadcastManager.getInstance(this)
                .sendBroadcast(Intent().setAction("").putExtra("localDistrict", ""))
            return
        }
        val n_enroll_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getId())
        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(),st_id_res)
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(),dis_id_res)
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(),tu_id_res)
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(),lat)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(),lng)
        val n_sanc_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).n_staff_sanc)
        val n_user_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getId())
        val n_date = RequestBody.create("text/plain".toMediaTypeOrNull(),date.text.toString())
        ApiClient.getClient().transferPatient(n_st_id,n_dis_id,n_tu_id,n_date,n_enroll_id, n_sanc_id, n_user_id, n_lat, n_lng).enqueue(object: Callback<AddDocResponse>{
            override fun onResponse(
                call: Call<AddDocResponse>,
                response: Response<AddDocResponse>
            ) {
                if(response.isSuccessful){
                    if (response.body()!!.isStatus){
                        BaseUtils.showToast(this@TransferPatient,
                            "Patient transferred successfully")
                        startActivity(Intent(this@TransferPatient,MainActivity::class.java).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                }
            }

            override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {
            }
        })

    }

    private fun setUpCalender() {
        val myCalendar = Calendar.getInstance()
        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                date.setText(sdf.format(myCalendar.time))
            }
        this.date.setOnClickListener(View.OnClickListener {
            val m_date = DatePickerDialog(
                this@TransferPatient, R.style.calender_theme, date,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            m_date.show()

            m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK)
            m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY)
        })
    }

}
