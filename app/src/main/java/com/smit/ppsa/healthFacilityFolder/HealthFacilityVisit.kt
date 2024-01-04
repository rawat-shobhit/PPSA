package com.smit.ppsa.healthFacilityFolder

import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.R
import com.smit.ppsa.Response.PatientFilterDataModel
import com.smit.ppsa.newFilterDropDown
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HealthFacilityVisit : AppCompatActivity() {

    private var list=ArrayList<HealthFacilityList>()
    private var monthList=ArrayList<PatientFilterDataModel>()
    private var yearList=ArrayList<PatientFilterDataModel>()
    private lateinit var monthDropDown:AutoCompleteTextView
    var healthFacilityAdapter:HealthFacilityAdapter?= null
    lateinit var recyclerView:RecyclerView
    lateinit var totalCount:TextView

    lateinit var tvVisitTot:TextView
    lateinit var tvNcTot:TextView
    lateinit var tvScTot:TextView
    lateinit var tvFdcTot:TextView

    private var visitCount=0
    var ncCount=0;
    var scCount=0;
    var fdcCont=0;

    private lateinit var okText:ImageView

    private var month=""
    private var year=""

    private lateinit var yearDropDown:AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_facility_visit)

        okText=findViewById(R.id.ok)
        monthDropDown=findViewById(R.id.monthDropDown)
        yearDropDown=findViewById(R.id.yearDropDown)
        recyclerView=findViewById(R.id.rvHealthFacility)
        totalCount=findViewById(R.id.totalColumn)

        tvFdcTot=findViewById(R.id.tvFdcTot);
        tvVisitTot =findViewById(R.id.visitTot);
        tvNcTot = findViewById(R.id.tvNcTot);
        tvScTot = findViewById(R.id.tvScTot)


        Log.d("dataCheckPerson",BaseUtils.getUserInfo(this).getnAccessRights()+"<-rights  nStaff->  "+
                BaseUtils.getUserInfo(this).getN_staff_sanc())

        monthDropDown.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            month = (position + 1).toString() + ""

            //  Toast.makeText(FormOne.this, hivFilterId, Toast.LENGTH_SHORT).show();
        })




        yearDropDown.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->

          when(position+1)
          {

              1->year="2020"

              2->year="2021"

              3->year="2022"

              4->year="2023"

              5->year="2024"
          }
            Log.d("Checking",position.toString())
            Log.d("Checking_",year.toString())
            //    Toast.makeText(FormOne.this, diabeticsId, Toast.LENGTH_SHORT).show();
        })




        initViews()
        okText.setOnClickListener(){
            list.clear()
            callHealthFacilityApi()
        }

    }

    private fun initViews(){
        updateSpinner()
    }


    private fun updateSpinner() {
        monthList.add(PatientFilterDataModel(1, "January"))
        monthList.add(PatientFilterDataModel(2, "February"))
        monthList.add(PatientFilterDataModel(3, "March"))
        monthList.add(PatientFilterDataModel(4, "April"))
        monthList.add(PatientFilterDataModel(5, "May"))
        monthList.add(PatientFilterDataModel(6, "June"))
        monthList.add(PatientFilterDataModel(7, "July"))
        monthList.add(PatientFilterDataModel(8, "August"))
        monthList.add(PatientFilterDataModel(9, "September"))
        monthList.add(PatientFilterDataModel(10, "October"))
        monthList.add(PatientFilterDataModel(11, "November"))
        monthList.add(PatientFilterDataModel(12, "December"))


//        yearList.add(PatientFilterDataModel(2018, "2018"))
//        yearList.add(PatientFilterDataModel(2019, "2019"))
        yearList.add(PatientFilterDataModel(2020, "2020"))
        yearList.add(PatientFilterDataModel(2021, "2021"))
        yearList.add(PatientFilterDataModel(2022, "2022"))
        yearList.add(PatientFilterDataModel(2023, "2023"))
        yearList.add(PatientFilterDataModel(2024, "2024"))



        val monthAdapter = newFilterDropDown(this, monthList)
        monthDropDown.setAdapter<newFilterDropDown>(monthAdapter)

        val yearAdapter = newFilterDropDown(this, yearList)
        yearDropDown.setAdapter<newFilterDropDown>(yearAdapter)


    }

    private fun callHealthFacilityApi(){
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(
                this@HealthFacilityVisit,
                "Please Check your internet  Connectivity"
            )

            return
        }
        val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_m_hfvisit&w=mnth<<EQUALTO>>"+month.trim()+"<<AND>>yr<<EQUALTO>>"+year.trim()+"<<AND>>n_user_id<<EQUALTO>>"+    BaseUtils.getUserInfo(this).getId().toString();

        Log.d("urlFinal", url)
        ApiClient.getClient().getHealthFaciliy(url).enqueue(object : Callback<HealthFacilityResponseModel>{


            override fun onResponse(
                call: Call<HealthFacilityResponseModel>,
                response: Response<HealthFacilityResponseModel>
            ) {
                if(response.isSuccessful){
                    if (response.body()!!.status == true){
                        list = response.body()!!.userData
                        setRecycler()
                        totalCount.setText("Total number of Row :- ${list.size.toString()}")
                    }else{
                        list.clear()
                        setRecycler()
                        tvVisitTot.setText("0");
                        tvNcTot.setText("0");
                        tvScTot.setText("0");
                        tvFdcTot.setText("0");
                        totalCount.setText("Total number of Row :- 0 ")
                        BaseUtils.showToast(this@HealthFacilityVisit, "No Data found")
                    }
                }
            }

            override fun onFailure(call: Call<HealthFacilityResponseModel>, t: Throwable) {

            }

        })
    }


    private fun setRecycler() {

        visitCount=0;
        ncCount=0;
        scCount=0;
        fdcCont=0;

        Log.d("testing", "setHospitalRecycler: " + list.size)


        for (i in 0 until  list.size)
        {
            visitCount += list[i].visit!!.toInt()
            ncCount += list[i].nc!!.toInt()
            scCount += list[i].sc!!.toInt();
            fdcCont += list[i].fdc!!.toInt()
        }


        tvVisitTot.setText(visitCount.toString());
        tvNcTot.setText(ncCount.toString());
        tvScTot.setText(scCount.toString());
        tvFdcTot.setText(fdcCont.toString());


        healthFacilityAdapter =
            HealthFacilityAdapter(list, this@HealthFacilityVisit)



        val linearLayoutManager =
            LinearLayoutManager(this@HealthFacilityVisit, LinearLayoutManager.VERTICAL, false)
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setAdapter(healthFacilityAdapter)
        recyclerView.setLayoutAnimation(
            AnimationUtils.loadLayoutAnimation(
                this,
                R.anim.layout_animation
            )
        )

    }
}