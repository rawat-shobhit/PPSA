package com.smit.ppsa.dailyVisitOutputFolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.R
import com.smit.ppsa.Response.PatientFilterDataModel
import com.smit.ppsa.healthFacilityFolder.HealthFacilityAdapter
import com.smit.ppsa.healthFacilityFolder.HealthFacilityList
import com.smit.ppsa.healthFacilityFolder.HealthFacilityResponseModel
import com.smit.ppsa.newFilterDropDown
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyVisitActivity : AppCompatActivity() {


    private var list=ArrayList<DailyVisitList>()
    private var monthList=ArrayList<PatientFilterDataModel>()
    private var yearList=ArrayList<PatientFilterDataModel>()
    private lateinit var monthDropDown: AutoCompleteTextView
    var dailyVisitAdapter: DailyVisitAdaper?= null
    lateinit var recyclerView: RecyclerView

    private lateinit var okText: ImageView
    lateinit var totalColumn:TextView
    private var month=""
    private var year=""

    lateinit var tvDcTot:TextView
    lateinit var tvPfTot:TextView
    lateinit var tvScTot:TextView
    lateinit var tvNcTot:TextView
    lateinit var tvNeTot:TextView
    lateinit var tvBeTot:TextView

    var dvCount=0;
    var pfCount=0;
    var scCount=0;
    var ncCount=0;
    var neCount=0;
    var beCount=0;









    private lateinit var yearDropDown:AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_visit)


        okText=findViewById(R.id.ok)
        monthDropDown=findViewById(R.id.monthDropDown)
        yearDropDown=findViewById(R.id.yearDropDown)
        recyclerView=findViewById(R.id.rvHealthFacility)
        totalColumn=findViewById(R.id.totalColumn)

        tvDcTot=findViewById(R.id.tvDvTot)
        tvPfTot=findViewById(R.id.tvPfTot)
        tvScTot=findViewById(R.id.tvScTot)
        tvNcTot=findViewById(R.id.tvNcTot)
        tvNeTot = findViewById(R.id.tvNeTot)
        tvBeTot=findViewById(R.id.tvBeTot)


        monthDropDown.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            month = (position + 1).toString() + ""

            //  Toast.makeText(FormOne.this, hivFilterId, Toast.LENGTH_SHORT).show();
        })


        yearDropDown.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            when (position + 1) {

                1 -> year = "2020"

                2 -> year = "2021"

                3 -> year = "2022"

                4 -> year = "2023"
            }
            Log.d("Checking", position.toString())
            Log.d("Checking_", year.toString())
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
//        yearList.add(PatientFilterDataModel(2024, "2024"))



        val monthAdapter = newFilterDropDown(this, monthList)
        monthDropDown.setAdapter<newFilterDropDown>(monthAdapter)

        val yearAdapter = newFilterDropDown(this, yearList)
        yearDropDown.setAdapter<newFilterDropDown>(yearAdapter)


    }

    private fun callHealthFacilityApi(){
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(
                this@DailyVisitActivity,
                "Please Check your internet  Connectivity"
            )

            return
        }
        //http://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_m_dailyvisit&w=mnth<<EQUALTO>>6<<AND>>yr<<EQUALTO>>2023<<AND>>n_user_id<<EQUALTO>>1369
        val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_m_dailyvisit&w=mnth<<EQUALTO>>"+month.trim()+"<<AND>>yr<<EQUALTO>>"+year.trim()+"<<AND>>n_user_id<<EQUALTO>>"+    BaseUtils.getUserInfo(this).getId().toString()+"<<OBY>>prd";

        Log.d("urlFinal", url)
        ApiClient.getClient().getDailyVisit(url).enqueue(object :
            Callback<DailyVisitResponseModel> {


            override fun onResponse(
                call: Call<DailyVisitResponseModel>,
                response: Response<DailyVisitResponseModel>
            ) {
                if(response.isSuccessful){
                    if (response.body()!!.status == true){
                        list = response.body()!!.userData
                        setRecycler()
                        totalColumn.setText("Total number of Row :- ${list.size.toString()}")
                        Log.d("totalData",list.size.toString());
                    }else{
                        list.clear()
                        setRecycler()

                        tvBeTot.setText("0");
                        tvPfTot.setText("0");
                        tvScTot.setText("0");
                        tvNcTot.setText("0");
                        tvNeTot.setText("0");
                        tvBeTot.setText("0");


                        totalColumn.setText("Total number of Row :- 0")
                        BaseUtils.showToast(this@DailyVisitActivity, "No Data found")
                    }
                }
            }

            override fun onFailure(call: Call<DailyVisitResponseModel>, t: Throwable) {

            }

        })
    }


    private fun setRecycler() {

        dvCount=0;
        pfCount=0;
        scCount=0;
        ncCount=0;
        neCount=0;
        beCount=0;
        Log.d("testing", "setHospitalRecycler: " + list.size)

        dailyVisitAdapter =
            DailyVisitAdaper(list, this@DailyVisitActivity)

        for(i in 0 until list.size)
        {
            dvCount +=list[i].dv!!.toInt()
            pfCount += list[i].pf!!.toInt()
            scCount += list[i].sc!!.toInt()
            ncCount += list[i].nc!!.toInt()
            neCount += list[i].ne!!.toInt()
            beCount += list[i].be!!.toInt()
        }

        tvDcTot.setText(dvCount.toString());
        tvPfTot.setText(pfCount.toString())
        tvScTot.setText(scCount.toString());
        tvNcTot.setText(ncCount.toString());
        tvNeTot.setText(neCount.toString());
        tvBeTot.setText(beCount.toString());



        val linearLayoutManager =
            LinearLayoutManager(this@DailyVisitActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setAdapter(dailyVisitAdapter)
        recyclerView.setLayoutAnimation(
            AnimationUtils.loadLayoutAnimation(
                this,
                R.anim.layout_animation
            )
        )

    }
}