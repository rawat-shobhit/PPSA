package com.smit.ppsa

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.Adapter.CustomSpinnerAdapter
import com.smit.ppsa.Adapter.LpaPatientAdapter
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Network.NetworkCalls
import com.smit.ppsa.Response.FormOneData
import com.smit.ppsa.Response.RegisterParentData
import com.smit.ppsa.Response.RegisterParentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RefillPatientList : AppCompatActivity() {
    private lateinit var tuSpinner: Spinner
    private lateinit var searchText: EditText
    private lateinit var patientRecyclerView: RecyclerView
    private lateinit var lpaPatientAdapter: LpaPatientAdapter
    private lateinit var backBtn: ImageView
    var tuStrings: MutableList<String> = ArrayList()
    private var tu: MutableList<FormOneData> = ArrayList()
    private var parentData: MutableList<RegisterParentData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refill_patient_list)
        init()
    }

    private fun init() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(""))
        tuSpinner = findViewById(R.id.filterCounsell)
        backBtn = findViewById(R.id.backbtn)
        searchText = findViewById(R.id.search)
        patientRecyclerView = findViewById(R.id.f2_patientrecycler)
        tuSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                } else {
                    getPatient()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        backBtn.setOnClickListener { super.onBackPressed() }
        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (parentData.size == 0) {
                } else {
                    filter(editable.toString(), "normal")
                }
            }
        })
        NetworkCalls.getTU(this)
    }

    private fun getPatient() {
        if (!BaseUtils.isNetworkAvailable(this@RefillPatientList)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_refil&w=n_tu_id<<EQUALTO>>" + tu[tuSpinner.selectedItemPosition-1].n_tu_id
        ApiClient.getClient().getTUPatient(url).enqueue(object : Callback<RegisterParentResponse> {
            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        parentData = response.body()!!.userData
                        patientRecyclerView.layoutManager =
                            LinearLayoutManager(this@RefillPatientList)
                        lpaPatientAdapter = LpaPatientAdapter(parentData, this@RefillPatientList)
                        patientRecyclerView.adapter = lpaPatientAdapter
                    } else {
                        BaseUtils.showToast(this@RefillPatientList, "No patient found")
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
                tu = BaseUtils.getTU(this@RefillPatientList)
                Log.d("mijop", "onReceive: " + tu.size)
                Log.d("mijop", "onReceive: " + tu.toString())
                for (a in tu.indices) {
                    if (!tuStrings.contains(tu.get(a).getcTuName())) {
                        tuStrings.add(tu.get(a).getcTuName())
                    }

                }
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(tuSpinner, tuStrings)
                tuSpinner.setSelection(1)
            }
        }
    }

    private fun setSpinnerAdapter(spinner: Spinner, values: List<String>) {
        val spinnerAdapter = CustomSpinnerAdapter(this@RefillPatientList, values)
        spinner.adapter = spinnerAdapter
    }

    private fun filter(text: String, type: String) {
        /* ArrayList<RoomPatientList> temp = new ArrayList();
        for (RoomPatientList d : roomPatientLists) {
            String value = d.getcPatNam().toLowerCase();
            if (value.contains(text.toLowerCase())) {
                temp.add(d);
            }
        }*/
        val temp: java.util.ArrayList<RegisterParentData?> = java.util.ArrayList()
        for (d in parentData) {

            val value = d.getcPatNam().lowercase(Locale.getDefault())
            if (value.contains(text.lowercase(Locale.getDefault()))) {
                temp.add(d)
            }

        }
        // docAdapter = new PatientAdapter(parentData, FormTwo.this);
        if (lpaPatientAdapter != null) {
            lpaPatientAdapter.updateList(temp)
        }

    }
}