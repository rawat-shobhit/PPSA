package com.smit.ppsa.providerStatusFolder

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.R
import com.smit.ppsa.healthFacilityFolder.HealthFacilityAdapter
import com.smit.ppsa.healthFacilityFolder.HealthFacilityResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.Provider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProviderVisitActivity : AppCompatActivity() {

    lateinit var dateFrom :TextView
    lateinit var dateTo:TextView
    var dateToFinal=""
    var dateFromFinal=""
    lateinit var tvOk:ImageView
    var list=ArrayList<ProviderVisitList>()
    lateinit var recyclerView:RecyclerView
    private var adapter :ProviderVisitAdapter?=null
   private lateinit var totalColumn:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_visit)

        recyclerView=findViewById(R.id.rvHealthFacility)
        dateFrom=findViewById(R.id.dateFrom)
        dateTo= findViewById(R.id.dateTo)
        tvOk=findViewById(R.id.ok)
        totalColumn= findViewById(R.id.totalColumn)

        dateTo.setOnClickListener(){

            val myCalendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val myFormat = "yyyy-MM-dd"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    dateTo.setText(sdf.format(myCalendar.time));
                    dateToFinal="'"+sdf.format(myCalendar.time)+"'"
                    Log.d("checking_", dateToFinal);
                    Log.d("checking", dateFromFinal);
                }


            val m_date = DatePickerDialog(
                this, R.style.calender_theme, date,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            val calendar = Calendar.getInstance()
//            m_date.datePicker.minDate = calendar.timeInMillis
            m_date.show()
            m_date.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setBackgroundColor(resources.getColor(R.color.black))
            m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)

        }
        dateFrom.setOnClickListener(){

            val myCalendar = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    myCalendar[Calendar.YEAR] = year
                    myCalendar[Calendar.MONTH] = monthOfYear
                    myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val myFormat = "yyyy-MM-dd"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    dateFrom.setText(sdf.format(myCalendar.time));
                    dateFromFinal="'"+sdf.format(myCalendar.time)+"'"
                    Log.d("checking", dateToFinal);
                    Log.d("checking_", dateFromFinal);
                }


            val m_date = DatePickerDialog(
                this, R.style.calender_theme, date,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            val calendar = Calendar.getInstance()
//            m_date.datePicker.minDate = calendar.timeInMillis
            m_date.show()
            m_date.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setBackgroundColor(resources.getColor(R.color.black))
            m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)



        }
        tvOk.setOnClickListener(){
            list.clear()
            applyProviderVistApi()
        }
    }

    private fun applyProviderVistApi() {
        if (!BaseUtils.isNetworkAvailable(this)) {
            BaseUtils.showToast(
                this@ProviderVisitActivity,
                "Please Check your internet  Connectivity"
            )
            return
        }
        val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_m_prov_visit&w=prd<<GTEQ>>"+dateFromFinal+"<<AND>>prd<<LTEQ>>"+dateToFinal+"<<AND>>n_user_id<<EQUALTO>>"+BaseUtils.getUserInfo(this).getId().toString();

        Log.d("urlFinal", url)
        ApiClient.getClient().getProviderVisitResponse(url).enqueue(object :
            Callback<ProviderVistResponseModel> {


            override fun onResponse(
                call: Call<ProviderVistResponseModel>,
                response: Response<ProviderVistResponseModel>
            ) {
                if(response.isSuccessful){
                    if (response.body()!!.status == true){
                        list = response.body()!!.userData
                        setRecycler()
                        totalColumn.setText("Total number of Column :- ${list.size.toString()}")
                    }else{
                        list.clear()
                        setRecycler()
                        totalColumn.setText("Total number of Column :- 0")
                        BaseUtils.showToast(this@ProviderVisitActivity, "No data found")
                    }
                }
            }

            override fun onFailure(call: Call<ProviderVistResponseModel>, t: Throwable) {

            }

        })
    }


    private fun setRecycler() {

        Log.d("testing", "setHospitalRecycler: " + list.size)

        adapter =ProviderVisitAdapter(list, this@ProviderVisitActivity)

        val linearLayoutManager =
            LinearLayoutManager(this@ProviderVisitActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutAnimation(
            AnimationUtils.loadLayoutAnimation(
                this,
                R.anim.layout_animation
            )
        )

    }
}