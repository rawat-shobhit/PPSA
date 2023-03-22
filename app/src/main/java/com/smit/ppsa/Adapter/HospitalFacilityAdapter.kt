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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.smit.ppsa.Adapter.HospitalFacilityAdapter.NotificationHolder
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.ProviderEngagementViewModel
import com.smit.ppsa.R
import com.smit.ppsa.Response.*
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
    var prevPos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_hospitalfacility, parent, false)
        )
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

        var qual = "select"
        val qualID = arrayOfNulls<String>(1)

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
//            val intent = Intent(context, AddHospitalFacilty::class.java)
//            intent.putExtra("tu_id", hospitalLists[position].getnTuId())
//            intent.putExtra("doctorId", hospitalLists[position].getnHfId())
//            context.startActivity(intent)

//            val li = LayoutInflater.from(context)
//            val dialogView = li.inflate(R.layout.dialog_adddoctor, null)
//            val sDialog = AlertDialog.Builder(context).setView(dialogView).setCancelable(true).create()

            Log.d("male","MALE Male Female FEMALE ")


                val view= LayoutInflater.from(context).inflate(R.layout.dialog_adddoctor,null)
            val builder= AlertDialog.Builder(context,R.style.dialog_transparent_style).setView(view)
            val dialogBinding= DialogAdddoctorBinding.bind(view)

            val mAlertDialog = builder.show()
            val id=nList[position].hf_id.toString();


            dialogBinding.adCancelbtn.setOnClickListener(){
                mAlertDialog.dismiss();
            }



            dialogBinding.adQualificationOne.setOnItemSelectedListener(
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {

                        // It returns the clicked item.
                        val clickedItem = parent.getItemAtPosition(position) as QualificationList
                        qual = clickedItem.getcQualf()
//                        qualID.get(0) = clickedItem.id
//                        getQual(qualID.get(0))
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })





            //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf&w=id<<EQUALTO>>1632
            val url ="_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_doc&w=id<<EQUALTO>>"+id;
            ApiClient.getClient().getDoctorDetails(url)
                .enqueue(object : Callback<DoctorsResponse> {
                    override fun onResponse(
                        call: Call<DoctorsResponse>,
                        response: Response<DoctorsResponse>
                    ) {
                        if (response.isSuccessful) {
                                Log.d("apiResponce",response.body()!!.userData.get(0).getcQual().toString());

                            dialogBinding.adCancelbtn.setOnClickListener(){
                                Toast.makeText(context, "checking toast", Toast.LENGTH_SHORT).show()
                            }

                            dialogBinding.namepracticingdoctor.setText(response.body()!!.userData.get(0).getcDocNam())
                            dialogBinding.regnumpracticingdoctor.setText(response.body()!!.userData.get(0).regNo)

                            dialogBinding.adContact.setText(response.body()!!.userData.get(0).getcMob());

                        }
                    }

                    override fun onFailure(call: Call<DoctorsResponse>, t: Throwable) {
                        Log.d("apiResponceAtHospital",t.toString())
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
                BaseUtils.showToast(context, "1")
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
                BaseUtils.showToast(context, "2")
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
                BaseUtils.showToast(context, "3")
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

    private fun getQual(quelId: String) {
        if (!BaseUtils.isNetworkAvailable(context)) {
                  return
        }
        val url ="_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_doc_spec_qual&w=n_spec<<EQUALTO>>"+quelId
        ApiClient.getClient().getQualification(url)
            .enqueue(object : Callback<QualificationResponse> {
                override fun onResponse(
                    call: Call<QualificationResponse>,
                    response: Response<QualificationResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {

                        }
                    }
                }

                override fun onFailure(call: Call<QualificationResponse>, t: Throwable) {}
            })
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