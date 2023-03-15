package com.smit.ppsa

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.Adapter.CustomSpinnerAdapter
import com.smit.ppsa.Adapter.LpaPatientAdapter
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Network.NetworkCalls
import com.smit.ppsa.Response.FormOneData
import com.smit.ppsa.Response.RegisterParentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TuSearchPatientList : AppCompatActivity() {
    private lateinit var tuSpinner: Spinner
    private lateinit var searchText: EditText
    private lateinit var searchBtn: TextView
    private lateinit var patientRecyclerView: RecyclerView
    private lateinit var backBtn: ImageView
    var tuStrings: MutableList<String> = ArrayList()
    private var tu: MutableList<FormOneData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tu_search_patient_list)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(""))
        init()
    }
    private fun init(){

        NetworkCalls.getTU(this)

        tuSpinner = findViewById(R.id.filterCounsell)
        backBtn = findViewById(R.id.backbtn)
        searchText = findViewById(R.id.search)
        searchBtn = findViewById(R.id.searchbtn)
        patientRecyclerView = findViewById(R.id.f2_patientrecycler)
        searchBtn.setOnClickListener {
            if (tuSpinner.selectedItemPosition==0){
                BaseUtils.showToast(this,"Please select TU")
            }else if (searchText.text.length<4){
                BaseUtils.showToast(this,"Minimun for 4 character required")
            }else{
                getPatient()
            }
        }
        backBtn.setOnClickListener { super.onBackPressed() }
    }
    private fun getPatient(){
        if (!BaseUtils.isNetworkAvailable(this@TuSearchPatientList)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }
        val name = searchText.text.toString().trim()
        val url = if (getIntent().hasExtra("upload")){
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_tu_id<<EQUALTO>>"+tu[tuSpinner.selectedItemPosition-1].n_tu_id+"<<AND>><<SBRK>>c_pat_nam<<SLIKE>>"+name+"<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>"+name+"<<ELIKE>><<OR>>c_mob<<SLIKE>>"+name+"<<ELIKE>><<EBRK>>"
        }else{
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_tu_id<<EQUALTO>>"+tu[tuSpinner.selectedItemPosition-1].n_tu_id+"<<AND>>trans_out<<ISNULL>>"+"<<AND>><<SBRK>>c_pat_nam<<SLIKE>>"+name+"<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>"+name+"<<ELIKE>><<OR>>c_mob<<SLIKE>>"+name+"<<ELIKE>><<EBRK>>"
        }

        ApiClient.getClient().getTUPatient(url).enqueue(object: Callback<RegisterParentResponse>{
            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.status){
                        patientRecyclerView.layoutManager = LinearLayoutManager(this@TuSearchPatientList)
                        patientRecyclerView.adapter = LpaPatientAdapter(response.body()!!.userData,this@TuSearchPatientList)
                    }else{
                        BaseUtils.showToast(this@TuSearchPatientList,"No patient found")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterParentResponse>, t: Throwable) {

            }
        })
    }
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.hasExtra("localTU")) {
                Log.d("gjuy", "onReceive: njkguyg")
                tu = BaseUtils.getTU(this@TuSearchPatientList)
                Log.d("mijop", "onReceive: " + tu.size)


                for (a in tu.indices) {
                    if (!tuStrings.contains(tu[a].getcTuName())) {
                        tuStrings.add(tu[a].getcTuName())
                        Log.d("tuitems ${a}", "onReceive: " + tu[a].getcTuName())

                    }
                }

                setSpinnerAdapter(tuSpinner, tuStrings)
                tuSpinner.setSelection(1)

//                for (a in tu.indices) {
//                    if (a > 0) {
//                        if (!tuStrings.contains(tu.get(a).getcTuName())) {
//                            tuStrings.add(tu.get(a).getcTuName())
//                        }
//                    }
//                }
//                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
//                setSpinnerAdapter(tuSpinner, tuStrings)
            }
        }
    }
    private fun setSpinnerAdapter(spinner: Spinner, values: List<String>) {

        val spinnerAdapter = CustomSpinnerAdapter(this@TuSearchPatientList, values)
        spinner.adapter = spinnerAdapter
    }

}