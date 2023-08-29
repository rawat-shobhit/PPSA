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
import com.smit.ppsa.Adapter.FilterDropdownAdapter
import com.smit.ppsa.Adapter.LpaPatientAdapter
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Network.NetworkCalls
import com.smit.ppsa.Response.FormOneData
import com.smit.ppsa.Response.PatientFilterDataModel
import com.smit.ppsa.Response.RegisterParentData
import com.smit.ppsa.Response.RegisterParentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class TuSearchPatientList : AppCompatActivity() {
    private lateinit var tuSpinner: Spinner
    private lateinit var searchText: EditText
    private lateinit var searchBtn: TextView
    private lateinit var tvTu: EditText
    private lateinit var filtertt: TextView
    private lateinit var visiit: LinearLayout
    private lateinit var checkboxNonVisit: CheckBox
    private lateinit var patientRecyclerView: RecyclerView
    private lateinit var backBtn: ImageView
    private lateinit var filtteerr: LinearLayout
    private lateinit var dropDownForFilter: AutoCompleteTextView

    var fdcHospitalsAdapter: LpaPatientAdapter? = null

    var registerParentDataList: ArrayList<RegisterParentData>? = ArrayList<RegisterParentData>()
    var tuString = ""
    var selectedFilter = ""
    private var filterArray = ArrayList<PatientFilterDataModel>()
    private var progressDialog: GlobalProgressDialog? = null

    var tuStrings: MutableList<String> = ArrayList()
    private var tu: MutableList<FormOneData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tu_search_patient_list)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(""))
        init()

        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
//                if (getIntent().hasExtra("upload")) {
//                    if(searchText.text.toString().length>4){
//                        filter(searchText.text.toString())
//                    }else{
//                        BaseUtils.showToast(this@TuSearchPatientList,"Please enter atleast 4 characters.")
//                    }
//                }
            }
        })
        updateSpinner()
    }

    private fun updateSpinner() {
        filterArray.add(PatientFilterDataModel(1, "Aadhar Card Is Missing"))
        filterArray.add(PatientFilterDataModel(2, "Prescription Copy is missing"))
        filterArray.add(PatientFilterDataModel(3, "Bank Document is missing"))
        filterArray.add(PatientFilterDataModel(4, "Test Report is missing"))
        filterArray.add(PatientFilterDataModel(5, "UDST Report is missing"))
        filterArray.add(PatientFilterDataModel(6, "Diabetes Report is missing"))
        filterArray.add(PatientFilterDataModel(7, "HIV Report is missing"))
        filterArray.add(PatientFilterDataModel(8, "Consent Form is missing"))
        filterArray.add(PatientFilterDataModel(9, "Additional Prescription is missing"))
        filterArray.add(PatientFilterDataModel(10, "Notification Image is missing"))

        val statusAdapter = FilterDropdownAdapter(this, filterArray)
        // set adapter to the autocomplete tv to the arrayAdapter
        dropDownForFilter.setAdapter(statusAdapter)
        dropDownForFilter.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, pos, _ ->
                //this is the way to find selected object/item
                //unitId = filterData!!.Unitmaster[pos].id
                selectedFilter = (pos + 1).toString()

                registerParentDataList!!.clear()

                getPatient()

                //   Toast.makeText(this,selectedFilter, Toast.LENGTH_SHORT).show()
            }
    }

    private fun init() {
        dropDownForFilter = findViewById(R.id.autoCompleteTextViewForFilters)
        progressDialog = GlobalProgressDialog(this)

        tuSpinner = findViewById(R.id.filterCounsell)
        checkboxNonVisit = findViewById(R.id.checkboxNonVisit)
        backBtn = findViewById(R.id.backbtn)
        filtteerr = findViewById(R.id.filtteerr)
        tvTu = findViewById(R.id.tvTU)
        visiit = findViewById(R.id.visiit)
        searchText = findViewById(R.id.search)
        filtertt = findViewById(R.id.filtertt)
        searchBtn = findViewById(R.id.searchbtn)
        patientRecyclerView = findViewById(R.id.f2_patientrecycler)
        if (getIntent().hasExtra("upload")) {
            tvTu.visibility = View.GONE
            filtertt.visibility = View.GONE
            filtteerr.visibility = View.VISIBLE
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


        searchBtn.setOnClickListener {
            if (!getIntent().hasExtra("upload")) {
                if (searchText.text.length < 4) {
                    BaseUtils.showToast(this, "Minimun for 4 character required")
                } else {
                    filter(searchText.text.toString())

                     getPatient()
                }
            } else {
                try {
                    filter(searchText.text.toString())

                } catch (e: java.lang.Exception) {

                }

            }
        }
        backBtn.setOnClickListener { super.onBackPressed() }
    }

    private fun filter(text: String) {
        val temp: ArrayList<RegisterParentData?> = ArrayList<RegisterParentData?>()
        Log.d("filterList180",registerParentDataList!!.size.toString())
        if (registerParentDataList!!.isNotEmpty()) {
            for (d in registerParentDataList!!) {
                val value = d.getcPatNam().lowercase(Locale.getDefault())
                if (value.contains(text.lowercase(Locale.getDefault()))) {
                    Log.d("TAG", "filter: $d")
                    temp.add(d)
                }
                if (temp.isEmpty()) {
                    for (d in registerParentDataList!!) {
                        val value = d.getnNkshId().lowercase(Locale.getDefault())
                        if (value.contains(text.lowercase(Locale.getDefault()))) {
                            Log.d("TAG", "filter: $d")
                            temp.add(d)
                        }
                    }
                }
            }

            if (temp != null) {
                Log.d("filterList200",registerParentDataList!!.size.toString())
                fdcHospitalsAdapter!!.updateList(temp)
            }
            Log.d("filterList230",registerParentDataList!!.size.toString())
        }
    }

    private fun getPatient() {

        progressDialog!!.showProgressBar()

        if (!BaseUtils.isNetworkAvailable(this@TuSearchPatientList)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            progressDialog!!.hideProgressBar()

            return
        }

        val name = searchText.text.toString().trim()
        // BaseUtils.showToast(this,tuString)
        val url = if (!getIntent().hasExtra("upload")) {
            //   Toast.makeText(this, "this", Toast.LENGTH_SHORT).show()
            //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll_docs&w=<<SBRK>>n_tu_id<<EQUALTO>>210<<OR>>n_tu_id<<EQUALTO>>213<<EBRK>><<AND>><<SBRK>>c_pat_nam<<SLIKE>>bhai<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>bhai<<ELIKE>><<OR>>c_mob<<SLIKE>>bhai<<ELIKE>><<EBRK>>
            // &w=<<SBRK>>n_tu_id<<EQUALTO>>162<<OR>>n_tu_id<<EQUALTO>>163<<OR>>n_tu_id<<EQUALTO>>164<<EBRK>><<AND>><<SBRK>>c_pat_nam<<SLIKE>>8532<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>8532<<ELIKE>><<OR>>c_mob<<SLIKE>>8532<<ELIKE>><<EBRK>><<AND>>trans_out<<ISNULL>>
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll_docs&w=<<SBRK>>" + tuString + "<<EBRK>><<AND>><<SBRK>>c_pat_nam<<SLIKE>>" + name + "<<ELIKE>><<OR>>n_nksh_id<<SLIKE>>" + name + "<<ELIKE>><<OR>>c_mob<<SLIKE>>" + name + "<<ELIKE>><<EBRK>>"
        } else {
             //  Toast.makeText(this, "this", Toast.LENGTH_SHORT).show()

            //  var tuString = tvTu.text.toString().trim()
//https://nikshayppsa.hlfppt.org/_api-v1_/_srch_docs.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll_docs&w=<<SBRK>>n_tu_id<<EQUALTO>>26<<OR>>n_tu_id<<EQUALTO>>28<<EBRK>>&typ=3
            "_srch_docs.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll_docs&w=<<SBRK>>$tuString<<EBRK>>&typ=$selectedFilter"
        }

        Log.d("url  shobhit", url)
        ApiClient.getClient().getTUPatient(url).enqueue(object : Callback<RegisterParentResponse> {
            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        Log.d("chekcing_area","240")

//                        try {
//                            Toast.makeText(this@TuSearchPatientList,registerParentDataList!!.size.toString(),Toast.LENGTH_SHORT).show()
//
//                        }catch (e:Exception){
//                            Log.d("crash__",e.toString())
//                        }

                        registerParentDataList!!.clear()
//                        Toast.makeText(this@TuSearchPatientList,registerParentDataList.toString(),Toast.LENGTH_SHORT).show()
                        registerParentDataList =
                            response.body()!!.userData as ArrayList<RegisterParentData>

//                        Toast.makeText(
//                            this@TuSearchPatientList,
//                            registerParentDataList!!.size.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()


                        patientRecyclerView.layoutManager =
                            LinearLayoutManager(this@TuSearchPatientList)
                        if (getIntent().hasExtra("transfer")) {
                            Log.d("chekcing_area","254")
                            fdcHospitalsAdapter =
                                LpaPatientAdapter(
                                    registerParentDataList,
                                    this@TuSearchPatientList,
                                    "transfer"
                                )
                            patientRecyclerView.adapter = fdcHospitalsAdapter;
                        } else {
                            Log.d("chekcing_area","261")
                            fdcHospitalsAdapter =
                                LpaPatientAdapter(
                                    registerParentDataList,
                                    this@TuSearchPatientList,
                                    "photo"
                                )
                            patientRecyclerView.adapter = fdcHospitalsAdapter;
                        }

                    } else {
                        BaseUtils.showToast(this@TuSearchPatientList, "No patient found")
                    }
                }
                progressDialog!!.hideProgressBar()

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
                progressDialog!!.hideProgressBar()

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

                Log.d("TUSTRING", tuString)

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