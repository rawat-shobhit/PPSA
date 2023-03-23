package com.smit.ppsa

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
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

class TuSearchPatientList : AppCompatActivity() {
    private lateinit var tuSpinner: Spinner
    private lateinit var searchText: EditText
    private lateinit var searchBtn: TextView
    private lateinit var tvTu: EditText
    private lateinit var filtertt: TextView
    private lateinit var visiit:LinearLayout
    private lateinit var checkboxNonVisit: CheckBox
    private lateinit var patientRecyclerView: RecyclerView
    private lateinit var backBtn: ImageView
    var fdcHospitalsAdapter: LpaPatientAdapter? = null

    var registerParentDataList: List<RegisterParentData>? = null
    var tuString = ""

    var tuStrings: MutableList<String> = ArrayList()
    private var tu: MutableList<FormOneData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tu_search_patient_list)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(""))
        init()
    }

    private fun init() {

        tuSpinner = findViewById(R.id.filterCounsell)
        checkboxNonVisit = findViewById(R.id.checkboxNonVisit)
        backBtn = findViewById(R.id.backbtn)
        tvTu = findViewById(R.id.tvTU)
        visiit = findViewById(R.id.visiit)
        searchText = findViewById(R.id.search)
        filtertt = findViewById(R.id.filtertt)
        searchBtn = findViewById(R.id.searchbtn)
        patientRecyclerView = findViewById(R.id.f2_patientrecycler)
        if (getIntent().hasExtra("upload")) {
            tvTu.visibility = View.GONE
            filtertt.visibility = View.GONE
            visiit.visibility = View.VISIBLE
        }
        NetworkCalls.getTU(this)

        //nextbtn.setEnabled(false);

        /*hospitalNameTt = findViewById(R.id.hospitalNameTitle);
        hospitalNameLocation = findViewById(R.id.locationHospital);
        doctorNameTv = findViewById(R.id.docname);
        hospitalTypeTitle = findViewById(R.id.hospitalTYpe);
        currentDate = findViewById(R.id.currentdate);
        lastVisit = findViewById(R.id.visitdays);*/
        //table = findViewById(R.id.laytable);


        /*   Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm aaa");
        currentDate.setText(curFormater.format(currentTime));*/
        //hospitalType = findViewById(R.id.hospitalType);

        checkboxNonVisit.setOnCheckedChangeListener { compoundButton, b ->
            // BaseUtils.showToast(HospitalsList.this, String.valueOf(b));

            try {
                filter()

            } catch (e: Exception) {

            }
            //   filter(search.getText().toString().trim(),b);
        }
        searchBtn.setOnClickListener {
            if (searchText.text.length < 4) {
                BaseUtils.showToast(this, "Minimun for 4 character required")
            } else {
                getPatient()
            }
        }
        backBtn.setOnClickListener { super.onBackPressed() }
    }

    private fun filter() {
        val temp: ArrayList<RegisterParentData?> =
            registerParentDataList as ArrayList<RegisterParentData?>
        if (registerParentDataList!!.isNotEmpty()) {
            for (d in registerParentDataList!!) {
                temp.add(d)
                if (checkboxNonVisit.isChecked) {
                    if (d.aadhar_img != "0" || d.notf_img != "0" || d.bnk_img != "") {
                        temp.remove(d)
                    }
                }
            }
            if (registerParentDataList != null) {
                fdcHospitalsAdapter!!.updateList(temp)
            }
        }
    }

    private fun getPatient() {
        if (!BaseUtils.isNetworkAvailable(this@TuSearchPatientList)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }

        val name = searchText.text.toString().trim()
        // BaseUtils.showToast(this,tuString)
        val url = if (getIntent().hasExtra("upload")) {
            //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll_docs&w=n
            // _tu_id<<EQUALTO>>235<<AND>><<SBRK>>c_pat_nam<<SLIKE>>Anil<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>Anil<<ELIKE>><<OR>>c_mob<<SLIKE>>Anil<<ELIKE>><<EBRK>>
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll_docs&w=<<SBRK>>" + tuString + "<<EBRK>><<AND>><<SBRK>>c_pat_nam<<SLIKE>>" + name + "<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>" + name + "<<ELIKE>><<OR>>c_mob<<SLIKE>>" + name + "<<ELIKE>><<EBRK>>"
        } else {
            var tuString = tvTu.text.toString().trim()


            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_tu_id<<EQUALTO>>" + tuString + "<<AND>>trans_out<<ISNULL>>" + "<<AND>><<SBRK>>c_pat_nam<<SLIKE>>" + name + "<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>" + name + "<<ELIKE>><<OR>>c_mob<<SLIKE>>" + name + "<<ELIKE>><<EBRK>>"
        }

        ApiClient.getClient().getTUPatient(url).enqueue(object : Callback<RegisterParentResponse> {
            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        registerParentDataList = response.body()!!.userData
                        patientRecyclerView.layoutManager =
                            LinearLayoutManager(this@TuSearchPatientList)
                        fdcHospitalsAdapter =
                            LpaPatientAdapter(registerParentDataList, this@TuSearchPatientList)
                        patientRecyclerView.setAdapter(fdcHospitalsAdapter);
                    } else {
                        BaseUtils.showToast(this@TuSearchPatientList, "No patient found")
                    }
                }
            }

            //  fdcHospitalsAdapter = new FdcHospitalsAdapter(hospitalLists, HospitalsList.this, "koko", hfID, dataBase);
            //
            //        }
            //        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HospitalsList.this, LinearLayoutManager.VERTICAL, false);
            //        hospitalRecycler.setLayoutManager(linearLayoutManager);
            //        hospitalRecycler.setAdapter(fdcHospitalsAdapter);
            //        hospitalRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
            //

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
                        tuString = if (a < tu.size - 1) {
                            tuString + "n_tu_id<<EQUALTO>>" + tu[a].n_tu_id + "<<OR>>"
                        } else {
                            tuString + "n_tu_id<<EQUALTO>>" + tu[a].n_tu_id
                        }
                    }
                }

                setSpinnerAdapter(tuSpinner, tuStrings)

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