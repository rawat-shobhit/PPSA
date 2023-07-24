package com.smit.ppsa.notificationLedgerFolder

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.PatientsFollowFolder.PatientFollowUpAdapter
import com.smit.ppsa.PatientsFollowFolder.PatientFollowUpList
import com.smit.ppsa.PatientsFollowFolder.PatientsFollowUpResponseModel
import com.smit.ppsa.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationLedgerActivity : AppCompatActivity() {

    lateinit var dateFrom : TextView
    lateinit var dateTo: TextView
    var dateToFinal=""
    var dateFromFinal=""
    lateinit var tvOk: ImageView
    var list=ArrayList<NotificationLedgerList>()
    lateinit var recyclerView: RecyclerView
    private var adapter : NotificationLeadgerAdapter?=null
    private lateinit var totalColumn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_ledger)

        recyclerView=findViewById(R.id.rvHealthFacility)
        dateFrom=findViewById(R.id.dateFrom)
        dateTo= findViewById(R.id.dateTo)
        tvOk=findViewById(R.id.ok)
        totalColumn=findViewById(R.id.totalColumn)

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
                    dateToFinal=sdf.format(myCalendar.time)
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
                    dateFromFinal=sdf.format(myCalendar.time)
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
                this@NotificationLedgerActivity,
                "Please Check your internet  Connectivity"
            )

            return
        }
        //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_m_followup&w=prd<<GTEQ>>'2023-07-01'<<AND>>prd<<LTEQ>>'2023-07-31'<<AND>>n_user_id<<EQUALTO>>1380
        //https://nikshayppsa.hlfppt.org/_api-v1_/_get_notinf.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_notfy&w=5&sanc=805&sdt=2023-01-01&edt=2023-07-31
        Log.d("dataCheckPerson",BaseUtils.getUserInfo(this).getnAccessRights()+"<-rights  nStaff->  "+
                BaseUtils.getUserInfo(this).getN_staff_sanc())
        val url = "_get_notinf.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_notfy&w="+BaseUtils.getUserInfo(this).getnAccessRights().toString()+"&sanc="+BaseUtils.getUserInfo(this).getN_staff_sanc()+"&sdt="+dateFromFinal+"&edt="+dateToFinal ;

        Log.d("urlFinal", url)
        ApiClient.getClient().getNotificationLedger(url).enqueue(object :
            Callback<NotificationLedgerResponse> {


            override fun onResponse(
                call: Call<NotificationLedgerResponse>,
                response: Response<NotificationLedgerResponse>
            ) {
                if(response.isSuccessful){
                    if (response.body()!!.status == true){
                        list = response.body()!!.userData
                        totalColumn.setText("Total number of Row :- ${list.size.toString()}")
                        setRecycler()
                    }else{
                        list.clear()
                        setRecycler()
                        totalColumn.setText("Total number of Row :- 0")
                        BaseUtils.showToast(this@NotificationLedgerActivity, "No Data found")
                    }
                }
            }

            override fun onFailure(call: Call<NotificationLedgerResponse>, t: Throwable) {

            }

        })
    }


    private fun setRecycler() {

        Log.d("testing", "setHospitalRecycler: " + list.size)

        adapter =NotificationLeadgerAdapter(list, this@NotificationLedgerActivity)

        val linearLayoutManager =
            LinearLayoutManager(this@NotificationLedgerActivity, LinearLayoutManager.VERTICAL, false)
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