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
import android.view.animation.AnimationUtils
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

class LpaPatient : AppCompatActivity() {
    var tuStrings: MutableList<String> = ArrayList()
    private var tu: MutableList<FormOneData> = ArrayList()
    private lateinit var tuLpaSpinner: Spinner
    private lateinit var patientRecycler: RecyclerView
    private lateinit var backBtn: ImageView
    private lateinit var nextbtn: TextView
    private lateinit var search: EditText
    var patientsList: List<RegisterParentData> = ArrayList()
    var fdcHospitalsAdapter: LpaPatientAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lpa_patient)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(""))
        init()
    }
    private fun init(){
        tuLpaSpinner = findViewById(R.id.tuCounsell)
        patientRecycler = findViewById(R.id.f2_patientrecycler)
        backBtn = findViewById(R.id.backbtn)
        search = findViewById(R.id.search)
        nextbtn = findViewById(R.id.nextbtn)

        // tuId =BaseUtils.getUserInfo(HospitalsList.this).getnUserLevel();
        /*     NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observeForever(aBoolean -> {
            if (aBoolean) {
                new Handler().postDelayed(this::getAllCustomer, 100);
            }
        });*/search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (patientsList.isEmpty()) {
                } else {
                    filter(editable.toString())
                }
            }
        })
        tuLpaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                } else {
                getTuPatient(tu[tuLpaSpinner.selectedItemPosition-1].n_tu_id.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        backBtn.setOnClickListener { super.onBackPressed() }
        nextbtn.setOnClickListener {

        }
        NetworkCalls.getTU(this)
    }


    private fun filter(text: String) {
        val temp: ArrayList<RegisterParentData?> = ArrayList<RegisterParentData?>()
        if (!patientsList.isEmpty()) {
            for (d in patientsList) {
                val value =d.getcPatNam().lowercase(Locale.getDefault())
                if (value.contains(text.lowercase(Locale.getDefault()))) {
                    Log.d("TAG", "filter: $d")
                    temp.add(d)
                }
                if(temp.isEmpty()){
                    val value =d.getcPatMob().lowercase(Locale.getDefault())
                    if (value.contains(text.lowercase(Locale.getDefault()))) {
                        Log.d("TAG", "filter: $d")
                        temp.add(d)
                    }
                }

                if(temp.isEmpty()){
                    val value =d.getnNkshId().lowercase(Locale.getDefault())
                    if (value.contains(text.lowercase(Locale.getDefault()))) {
                        Log.d("TAG", "filter: $d")
                        temp.add(d)
                    }
                }
            }
            if (fdcHospitalsAdapter != null) {
                fdcHospitalsAdapter!!.updateList(temp)
            }
        }
    }
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.hasExtra("localTU")) {
                Log.d("gjuy", "onReceive: njkguyg")
                tu = BaseUtils.getTU(this@LpaPatient)

                var tus = ""

                Log.d("mijop", "onReceive: " + tu.size)
                Log.d("mijop", "onReceive: " + tu.toString())
                for (a in tu.indices) {
                        if (!tuStrings.contains(tu.get(a).getcTuName())) {
                            tuStrings.add(tu.get(a).getcTuName())
                            tus = if (a < tu.size - 1) {
                                tus + "n_tu_id<<EQUALTO>>" + tu[a].n_tu_id + "<<OR>>"
                            } else {
                                tus + "n_tu_id<<EQUALTO>>" + tu[a].n_tu_id
                            }
                    }
                }
                tuLpaSpinner.visibility =View.GONE
                var counselTuTitle : TextView = findViewById(R.id.counselTuTitle)
                counselTuTitle.visibility =View.GONE
                getTuPatient(tus)
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(tuLpaSpinner, tuStrings)
                tuLpaSpinner.setSelection(1)

            }
        }
    }
    private fun setSpinnerAdapter(spinner: Spinner, values: List<String>) {
        val spinnerAdapter = CustomSpinnerAdapter(this@LpaPatient, values)
        spinner.adapter = spinnerAdapter
    }

    private fun getTuPatient(tu_id: String){
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(
                this@LpaPatient,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }
        val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_lpa_pat_lst&w="+tu_id
        ApiClient.getClient().getTUPatient(url).enqueue(object : Callback<RegisterParentResponse>{


            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) {
                if(response.isSuccessful){
                    if (response.body()!!.status){
                        patientsList = response.body()!!.userData
                        setHospitalRecycler()
                    }else{
                        BaseUtils.showToast(this@LpaPatient,"No patient found")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterParentResponse>, t: Throwable) {

            }

        })
    }

    private fun setHospitalRecycler() {

        Log.d("jiouyo", "setHospitalRecycler: " + patientsList.size)

            fdcHospitalsAdapter =
                LpaPatientAdapter(patientsList, this@LpaPatient,"LpaPatient")

        val linearLayoutManager =
            LinearLayoutManager(this@LpaPatient, LinearLayoutManager.VERTICAL, false)
        patientRecycler.setLayoutManager(linearLayoutManager)
        patientRecycler.setAdapter(fdcHospitalsAdapter)
        patientRecycler.setLayoutAnimation(
            AnimationUtils.loadLayoutAnimation(
                this,
                R.anim.layout_animation
            )
        )
        /*  }else {
            hospitalsAdapter = new HospitalsAdapter(hospitalLists);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
            hospitalRecycler.setLayoutManager(linearLayoutManager);
            hospitalRecycler.setAdapter(hospitalsAdapter);
            hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
        }*/
    }

}