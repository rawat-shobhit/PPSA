package com.smit.ppsa.Adapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.Adapter.HospitalFacilityAdapter.NotificationHolder
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Network.NetworkCalls
import com.smit.ppsa.ProviderEngagementViewModel
import com.smit.ppsa.R
import com.smit.ppsa.Response.DoctorsResponse
import com.smit.ppsa.Response.QualificationList
import com.smit.ppsa.Response.QualificationResponse
import com.smit.ppsa.Response.RoomDoctorsList
import com.smit.ppsa.databinding.DialogAdddoctorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HospitalFacilityAdapter(
    private var nList: List<RoomDoctorsList>,
    private val context: Activity,
    private val type: String,
    private val viewModel: ProviderEngagementViewModel
) : RecyclerView.Adapter<NotificationHolder>() {
    var qualfId="";
    var specId="";
    var qualificationLists: List<QualificationList> = ArrayList()
    var prevPos = -1
    var model:ArrayList<QualificationList>?=null;
    var doctorId="";
    var specIdGlobal ="";

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_hospitalfacility, parent, false)
        )
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

        var specialization="select"
        var qualification="select"
        holder.radioButton.isClickable = false
        holder.radioButton.isFocusable = false
        holder.radioButtonOne.isClickable = false
        holder.radioButtonOne.isFocusable = false

        //  nList.get(0).setChecked(true);
        if (type == "provider") {
            holder.radioButton.visibility = View.VISIBLE
        } else holder.radioButtonOne.visibility = View.VISIBLE
        if (nList[position].isChecked) {
            holder.radioButton.isChecked = true
            holder.radioButtonOne.isChecked = true
        } else {
            holder.radioButton.isChecked = false
            holder.radioButtonOne.isChecked = false
        }




        holder.editDoctor.setOnClickListener {

            doctorId = nList[position].idd

            val view= LayoutInflater.from(context).inflate(R.layout.dialog_adddoctor,null)
            val builder= AlertDialog.Builder(context,R.style.dialog_transparent_style).setView(view)
            val dialogBinding= DialogAdddoctorBinding.bind(view)

            val mAlertDialog = builder.show()

            dialogBinding.adCancelbtn.setOnClickListener(){
                mAlertDialog.dismiss();
            }
//            dialogBinding.adQualificationOne.setOnItemSelectedListener(
//                object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(
//                        parent: AdapterView<*>,
//                        view: View, position: Int, id: Long
//                    ) {
//
//                        // It returns the clicked item.
//                        val clickedItem = parent.getItemAtPosition(position) as QualificationList
//                        specialization = clickedItem.id.toString()
//
//                        Toast.makeText(context, clickedItem.id.toString(), Toast.LENGTH_SHORT).show();
//
//                        if(specialization.trim()!="")
//                        {
//                            val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_doc_spec_qual&w=n_spec<<EQUALTO>>"+specialization
//                            ApiClient.getClient().getQualification(url)
//                                .enqueue(object : Callback<QualificationResponse> {
//                                    override fun onResponse(
//                                        call: Call<QualificationResponse>,
//                                        response: Response<QualificationResponse>
//                                    ) {
//                                        if (response.isSuccessful) {
//                                            if (response.body()!!.status) {
//
//                                                qualificationLists = response.body()!!.userData
//                                                BaseUtils.saveQualSpeList(context, qualificationLists)
//                                                val adapter2: SpinAdapter
//                                                adapter2 = SpinAdapter(
//                                                    context,
//                                                    qualificationLists
//                                                )
//                                                dialogBinding.adQualificationTwo.setAdapter(adapter2)
//
//                                            }
//                                        }
//                                    }
//
//                                    override fun onFailure(call: Call<QualificationResponse>, t: Throwable) {
////                                    Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
//                                    }
//                                })
//                        }
//
//
//
////                        qualID.get(0) = clickedItem.id
////                        getQual(qualID.get(0))
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {}
//                })
//
//            dialogBinding.adQualificationTwo.setOnItemSelectedListener(
//                object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(
//                        parent: AdapterView<*>,
//                        view: View, position: Int, id: Long
//                    ) {
//
//                        try {
//                            val clickedItem = parent.getItemAtPosition(position) as QualificationList
//                            specIdGlobal=clickedItem.id.toString()
//
//                            Toast.makeText(context,specIdGlobal.toString(),Toast.LENGTH_SHORT).show()
//                        }catch (e:Exception){
//
//                        }
//
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {}
//                })




            ApiClient.getClient().qualificationList.enqueue(object :
                Callback<QualificationResponse?> {
                override fun onResponse(
                    call: Call<QualificationResponse?>,
                    responseMain: Response<QualificationResponse?>
                ) {
                    if (responseMain.isSuccessful) {
                        assert(responseMain.body() != null)
                        if (responseMain.body()!!.status) {

                            model  =responseMain.body()!!.userData


                            setspinnerAdapter(dialogBinding.adQualificationOne,model as List<QualificationList>)



                            //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_doc&w=id<<EQUALTO>>1632
                            val url ="_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_doc&w=id<<EQUALTO>>"+doctorId;
                            ApiClient.getClient().getDoctorDetails(url)
                                .enqueue(object : Callback<DoctorsResponse> {
                                    override fun onResponse(
                                        call: Call<DoctorsResponse>,
                                        response: Response<DoctorsResponse>
                                    ) {
                                        if (response.isSuccessful) {


                                            Log.d("idHospital",doctorId.toString());

                                            dialogBinding.adCancelbtn.setOnClickListener(){
                                                mAlertDialog.dismiss()
                                            }

                                            dialogBinding.namepracticingdoctor.setText(response.body()!!.userData[0].getcDocNam())
                                            dialogBinding.regnumpracticingdoctor.setText(response.body()!!.userData[0].regNo)

                                            dialogBinding.adContact.setText(response.body()!!.userData[0].getcMob());




                                            for(i in 0 until (model as java.util.ArrayList<QualificationList>?)!!.size){

                                                try{

                                                    if(response.body()!!.userData.get(0).getcQualf().equals(responseMain.body()!!.userData[i].getcQualf())) {
                                                        Log.d("QualfMain",response.body()!!.userData[0].getcQualf()+" -> "+ responseMain.body()!!.userData[i].getcQualf())

                                                        dialogBinding.adQualificationOne.setSelection(i+1)
                                                        qualfId=responseMain.body()!!.userData[i].id
                                                        //responseMain.body()!!.userData[i].id.toString()
                                                        val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_doc_spec_qual&w=n_spec<<EQUALTO>>"+qualfId
                                                        ApiClient.getClient().getQualification(url)
                                                            .enqueue(object : Callback<QualificationResponse> {
                                                                override fun onResponse(
                                                                    call: Call<QualificationResponse>,
                                                                    responsesmall: Response<QualificationResponse>
                                                                ) {
                                                                    if (response.isSuccessful) {
                                                                        if (response.body()!!.status) {

                                                                            qualificationLists = responsesmall.body()!!.userData
                                                                            BaseUtils.saveQualSpeList(context, qualificationLists)
                                                                            val adapter2: SpinAdapter
                                                                            adapter2 = SpinAdapter(
                                                                                context,
                                                                                qualificationLists
                                                                            )
                                                                            dialogBinding.adQualificationTwo.setAdapter(adapter2)



                                                                            for(i in 0 until qualificationLists.size){
                                                                                if(qualificationLists.get(i).c_val.toString().equals(response.body()!!.userData.get(0).getcQual()))
                                                                                {
                                                                                    specId=qualificationLists.get(i).id.toString()
                                                                                    dialogBinding.adQualificationTwo.setSelection(i+1)
                                                                                    Log.d("lastLog",qualificationLists.get(i).getcQual().toString() +"->"+response.body()!!.userData.get(0).getcQual())
                                                                                }else{
                                                                                    Log.d("lastLog",qualificationLists.get(i).getcQual().toString() +"->"+response.body()!!.userData.get(0).getcQual())
                                                                                }
                                                                            }
                                                                            dialogBinding.adNextbtn.setOnClickListener(){

                                                                                /*
                                                                               Context context,            String c_doc_namm,
                                                                    String n_qual_idd,            String n_spec_idd,            String c_mobb,
                                                                    String c_regnoo,
                                                                    String hospitalId

                                                    dialogBinding.namepracticingdoctor.setText(response.body()!!.userData[0].getcDocNam())
                                        dialogBinding.regnumpracticingdoctor.setText(response.body()!!.userData[0].regNo)

                                        dialogBinding.adContact.setText(response.body()!!.userData[0].getcMob());
                                                                                 */

                                                                                // another api calling here down



                                                                                Log.d("editDoctor",dialogBinding.namepracticingdoctor.text.toString()+" "+qualfId+"  "+specId+ "  "+dialogBinding.adContact.text.toString() +dialogBinding.regnumpracticingdoctor.text.toString() )

                                                                                NetworkCalls.editDoctor(context,dialogBinding.namepracticingdoctor.text.toString(),qualfId,specId,dialogBinding.adContact.text.toString(),dialogBinding.regnumpracticingdoctor.text.toString(),doctorId)

                                                                            }

                                                                        }
                                                                    }
                                                                }

                                                                override fun onFailure(call: Call<QualificationResponse>, t: Throwable) {
//                                                                Toast.makeText(context,t.toString(),Toast.LENGTH_SHORT).show()
                                                                }
                                                            })

                                                    } //responseMain.body()!!.userData[i].getcQualf()

                                                }catch (e:Exception){
                                                    Log.d("creash_",e.toString());
                                                }

                                            }

                                        }
                                    }

                                    override fun onFailure(call: Call<DoctorsResponse>, t: Throwable) {
                                        Log.d("apiResponceAtHospital",t.toString())
                                    }
                                })




                        } else {
                            Log.d("insideResponse1",responseMain.toString())
                        }
                    } else {
                        Log.d("insideResponse2",responseMain.toString())
                    }
                }

                override fun onFailure(call: Call<QualificationResponse?>, t: Throwable) {
                    Log.d("insideResponse",t.toString())
                }
            })











        }
        holder.doctorname.text = nList[position].docname
        holder.qualificationone.text = nList[position].qualf
        holder.qualificationtwo.text = nList[position].qual
        holder.conatactno.text = nList[position].mob
        try {
            val difference = getDateDiff(
                SimpleDateFormat("yyyy/MM/dd"),
                nList[position].getlst_vst().toString(),
                dateTime
            ).toInt()
            Log.d("dateDifference", "$difference done")
            if (difference <= 30) {
                holder.borderLayout.backgroundTintList =
                    context.resources.getColorStateList(R.color.teal_700)
            }
        } catch (e: Exception) {
        }
        holder.lastvisit.text = nList[position].getlst_vst()
        //
//        if(type.equals("hospital") || type.equals("hospitald") || type.equals("provider")){
//
//        }else{
//            prevPos = position;
//            Intent intent = new Intent();
//            intent.setAction("doctors");
//            intent.putExtra("checked", true);
//            intent.putExtra("doc_id", nList.get(position).getIdd());
//            intent.putExtra("position", String.valueOf(position));
//            intent.putExtra("hf_id", nList.get(position).getHf_id());
//            intent.putExtra("docName",nList.get(position).getDocname());
//            LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
//            notifyDataSetChanged();
//        }
        holder.itemView.setOnClickListener {
            Log.d("type", type)
            if (nList[position].isChecked) {
                nList[position].isChecked = false
                viewModel.removeList(nList[position])
            } else {
                nList[position].isChecked = true
                viewModel.addList(nList[position])
            }
            if (type == "hospital" || type == "hospitald") {
                if (prevPos == -1) {
                } else {
                    nList[prevPos].isChecked = false
                }
                prevPos = position
                Log.d("jko", "onClick: " + nList[position].idd)
                val intent = Intent()
                intent.action = "doctors"
                intent.putExtra("checked", true)
                intent.putExtra("doc_id", nList[position].idd)
                intent.putExtra("position", position.toString())
                intent.putExtra("counsel", "")
                intent.putExtra("hospitalName", context.intent.getStringExtra("hospitalName"))
                intent.putExtra("hf_id", nList[position].hf_id)
                /* context.startActivity(new Intent(context, FormTwo.class)
                                .putExtra("doc_id", nList.get(position).getIdd())
                                .putExtra("counsel", "")
                                .putExtra("hospitalName", context.getIntent().getStringExtra("hospitalName"))
                                .putExtra("hf_id", nList.get(position).getHf_id())
                        );*/LocalBroadcastManager.getInstance(holder.itemView.context)
                    .sendBroadcast(intent)
                notifyDataSetChanged()
                //  context.finish();
            } else if (type == "provider") {
                Log.d("jko", "onClick: " + nList[position].idd)

                /*  context.startActivity(new Intent(context, */ /*FormOne*/ /*ProviderEngagement.class)
                            .putExtra("doc_id", nList.get(position).getIdd()).putExtra("provider", "")
                            .putExtra("hospitalName", context.getIntent().getStringExtra("hospitalName"))
                            .putExtra("hospitallocation", context.getIntent().getStringExtra("hospitallocation"))
                            .putExtra("hospitaltypeName", context.getIntent().getStringExtra("hospitaltypeName"))
                            .putExtra("docName", nList.get(position).getDocname())


                            .putExtra("hf_id", nList.get(position).getHf_id())
                    );*/Log.d(
                    "dmkidjio",
                    "onReceive: " + context.intent.getStringExtra("lastvisit")
                )
                val intent = Intent()
                intent.action = "doctors"
                intent.putExtra("checked", true)
                intent.putExtra("doc_id", nList[position].idd).putExtra("provider", "")
                intent.putExtra("hospitalName", context.intent.getStringExtra("hospitalName"))
                intent.putExtra(
                    "hospitallocation",
                    context.intent.getStringExtra("hospitallocation")
                )
                intent.putExtra(
                    "hospitaltypeName",
                    context.intent.getStringExtra("hospitaltypeName")
                )
                intent.putExtra("lastvisit", context.intent.getStringExtra("lastvisit"))
                intent.putExtra("docName", nList[position].docname)
                intent.putExtra("position", position.toString())
                intent.putExtra("hf_id", nList[position].hf_id)
                LocalBroadcastManager.getInstance(holder.itemView.context).sendBroadcast(intent)
                notifyDataSetChanged()

                //  context.finish();
            } else {
                if (prevPos == -1) {
                } else {
                    nList[prevPos].isChecked = false
                }
                prevPos = position
                val intent = Intent()
                intent.action = "doctors"
                intent.putExtra("checked", true)
                intent.putExtra("doc_id", nList[position].idd)
                intent.putExtra("position", position.toString())
                intent.putExtra("hf_id", nList[position].hf_id)
                intent.putExtra("docName", nList[position].docname)
                LocalBroadcastManager.getInstance(holder.itemView.context).sendBroadcast(intent)
                notifyDataSetChanged()
                /* context.startActivity(new Intent(context, FormOne.class)
                        );*/

                //  context.finish();
            }
        }
    }

    override fun getItemCount(): Int {
        return nList.size
    }

    inner class NotificationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editDoctor:ImageView
        val doctorname: TextView
        val qualificationone: TextView
        val qualificationtwo: TextView
        val conatactno: TextView
        val lastvisit: TextView
        var radioButton: CheckBox
        var radioButtonOne: RadioButton
        val borderLayout: LinearLayout

        init {
            editDoctor=itemView.findViewById(R.id.ivEditIcon1)
            doctorname = itemView.findViewById(R.id.doctor_name)
            qualificationone = itemView.findViewById(R.id.qualification_one)
            qualificationtwo = itemView.findViewById(R.id.qualification_two)
            conatactno = itemView.findViewById(R.id.conatact_number)
            radioButton = itemView.findViewById(R.id.radioButton)
            radioButtonOne = itemView.findViewById(R.id.radioButtonone)
            lastvisit = itemView.findViewById(R.id.lastvisit)
            borderLayout = itemView.findViewById(R.id.border)
        }
    }

    private val dateTime: String
        private get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
            val date = Date()
            return dateFormat.format(date)
        }

    fun updateList(list: ArrayList<RoomDoctorsList>) {
        nList = list
        notifyDataSetChanged()
    }


    private fun setspinnerAdapter(spinner: Spinner, qualificationLists: List<QualificationList>) {
        val adapter = SpinAdapter(
            context,
            qualificationLists
        )
        spinner.adapter = adapter
    }


    private fun getQual(specId: String) {
        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(
                context,
                "Please Check your internet  Connectivity"
            )
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return
        }

    }

    companion object {
        fun getDateDiff(format: SimpleDateFormat, oldDate: String?, newDate: String?): Long {
            return try {
                TimeUnit.DAYS.convert(
                    format.parse(newDate).time - format.parse(oldDate).time,
                    TimeUnit.MILLISECONDS
                )
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }
}