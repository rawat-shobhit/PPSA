package com.smit.ppsa.Adapter

import com.smit.ppsa.Response.HospitalList
import com.smit.ppsa.Dao.AppDataBase
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.smit.ppsa.R
import android.annotation.SuppressLint
import android.content.Context
import com.smit.ppsa.BaseUtils
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.smit.ppsa.FdcForm
import com.smit.ppsa.FdcDispensationToHf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.smit.ppsa.Response.PrevVisitsData
import kotlin.Throws
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Response.PrevVisitsResponse
import com.smit.ppsa.Adapter.HospitalDiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.DiffUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FdcHospitalsAdapter(
    private var hospitalLists: List<HospitalList>,
    context: Context,
    type: String,
    hfID: String,
    dataBase: AppDataBase
) : RecyclerView.Adapter<FdcHospitalsAdapter.Mholder>() {
    private val newhospitalLists: List<HospitalList>
    private val context: Context
    private val type: String
    private val hfID: String
    var prevPos = -1
    var dataBase: AppDataBase
    var pos = -1
    var first = false
    var containerPrev: LinearLayout? = null
    var contonePrev: LinearLayout? = null
    var conttwoPrev: LinearLayout? = null
    var contthreePrev: LinearLayout? = null
    var contfrPrev: LinearLayout? = null
    var contfvPrev: LinearLayout? = null
    var contsixPrev: LinearLayout? = null

    init {
        newhospitalLists = hospitalLists
        this.context = context
        this.type = type
        this.dataBase = dataBase
        this.hfID = hfID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Mholder {
        return Mholder(
            LayoutInflater.from(parent.context).inflate(R.layout.fdc_hospital_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Mholder, @SuppressLint("RecyclerView") position: Int) {
        holder.radioButton.isClickable = false
        holder.radioButton.isFocusable = false
        /*if ( hospitalLists.get(position).isChecked()){


            //  hospitalLists.get(position).setChecked(false);
        }else {
            // hospitalLists.get(position).setChecked(true);


            // hospitalLists.get(position).setChecked(true);
        }*/




        if (hospitalLists[position].isChecked) {
            Log.d("selectionCheck", position.toString() + "")
            holder.radioButton.isChecked = true
            holder.container.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contone.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contthree.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contfr.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contfv.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"))
        }
        else if(newhospitalLists[position].isChecked){
            Log.d("selectionCheckNew", position.toString() + "")
            holder.radioButton.isChecked = true
            holder.container.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contone.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contthree.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contfr.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contfv.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"))
            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"))
        }
        else {
            Log.d("elseStatement", position.toString() + "")
            holder.container.setBackgroundColor(Color.WHITE)
            holder.contone.setBackgroundColor(Color.WHITE)
            holder.conttwo.setBackgroundColor(Color.parseColor("#DFDFDE"))
            holder.contthree.setBackgroundColor(Color.WHITE)
            holder.contfr.setBackgroundColor(Color.parseColor("#DFDFDE"))
            holder.contfv.setBackgroundColor(Color.WHITE)
            holder.contsix.setBackgroundColor(Color.WHITE)
            holder.radioButton.isChecked = false
        }

        holder.hospitalName.text = hospitalLists[position].getcHfNam()
        holder.location.text =
            hospitalLists[position].getcDisNam() + "," + hospitalLists[position].getcTuName()
        holder.doctorName.text = hospitalLists[position].getcContPer()
        holder.typeClinic.text = hospitalLists[position].getcHfTyp()
        var currentTime = Calendar.getInstance().time
        val curFormater1 = SimpleDateFormat("yyyy/MM")
        val curFormater = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        //holder.table.setVisibility(View.GONE);
        if (type == "fdc") {
            holder.bagImg.visibility = View.VISIBLE
            //holder.table.setVisibility(View.GONE);
        } else if (type == "provider") {
            holder.contfr.visibility = View.GONE
            holder.contthree.visibility = View.GONE
            if (!hospitalLists[position].isChecked) {
                if (hospitalLists[position].lst_visit == null || hospitalLists[position].lst_visit.isEmpty()) {
                    Log.d("cecking", position.toString() + "")
                } else {
                    try {
                        currentTime = curFormater.parse(hospitalLists[position].lst_visit)
                        if (BaseUtils.sameMonth(
                                holder.itemView.context,
                                curFormater1.format(currentTime)
                            )
                        ) {
                            holder.container.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.contone.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.contthree.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.contfr.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.contfv.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFE3"))
                            holder.contsix.setBackgroundColor(Color.parseColor("#E0FFE3"))
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        holder.hospitalNameTt.text = hospitalLists[position].getcHfNam()
        holder.hospitalNameLocation.text =
            hospitalLists[position].getcDisNam() + ", " + hospitalLists[position].getcStNam()
        holder.doctorNameTv.text = hospitalLists[position].getcContPer()
        holder.hospitalTypeTitle.text = hospitalLists[position].getcHfTyp()
        var formattedDate = ""
        if (!hospitalLists.isEmpty()) {
            // for (int i = 0; i < hospitalLists.size(); i++) {
            if (hospitalLists[position].lst_visit != null) {
                for (j in 0 until hospitalLists[position].lst_visit.length) {
                    if (j < 10) {
                        formattedDate = formattedDate + hospitalLists[position].lst_visit[j]
                    }
                    var day = "Day"
                    if (hospitalLists[position].no_of_days.toInt() > 2) {
                        day = "Days"
                    }
                    holder.currentDate.text =
                        formattedDate + " (" + hospitalLists[position].no_of_days + " " + day + ")"
                    holder.currentDate.visibility = View.VISIBLE
                }
            } else {
                holder.currentDate.visibility = View.INVISIBLE
            }

            //}
        }
        var ext = ""
        if (hospitalLists[position].no_of_days != null) {
            ext = if (Integer.valueOf(hospitalLists[position].no_of_days) < 2) {
                "Day"
            } else {
                "Days"
            }
        }
        if (hospitalLists[position].no_of_days != null) {
            holder.lastVisit.visibility = View.GONE
            holder.lastVisit.text = hospitalLists[position].no_of_days + " " + ext
        }
        /*else  {
            holder.lastVisit.setVisibility(View.GONE);
        }*/

        holder.bagImg.setOnClickListener { view ->
            if (prevPos == -1) {
            } else {
                hospitalLists[prevPos].isChecked = false
                //   holder.table.setVisibility(View.GONE);
            }
            hospitalLists[position].isChecked = true
            // holder.table.setVisibility(View.GONE);
            prevPos = position
            if (hospitalLists[position].getnHfId() == "") {
                Toast.makeText(
                    view.context,
                    "Can not add or view doctors because it is added in local due to no internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(context, FdcForm::class.java)
                // Intent intent = new Intent(context, MedicineListActivity.class);
                intent.action = "Selected"
                intent.putExtra("op", "")
                intent.putExtra("tu_id", hospitalLists[position].getnTuId())
                intent.putExtra("hf_id", hospitalLists[position].getnHfId())
                intent.putExtra("hospitalName", hospitalLists[position].getcHfNam())
                intent.putExtra("docName", hospitalLists[position].getcContPer())
                //   intent.putExtra("docid", hospitalLists.get(position).get());
                context.startActivity(intent)

                /*  Intent intent = new Intent();
                            intent.setAction("");
                            intent.putExtra("checked", true);
                           */
                /* intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                            intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());*/
                /*
                        intent.putExtra("tu_id", hospitalLists.get(position).getnTuId());
                        intent.putExtra("hf_id", hospitalLists.get(position).getnHfId());
                        intent.putExtra("hospitalName", hospitalLists.get(position).getcHfNam());
                        intent.putExtra("fdctype", "bag");
                        intent.putExtra("docName", hospitalLists.get(position).getcContPer());
                            LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                            notifyDataSetChanged();*/
                /*  LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                        notifyDataSetChanged();*/
            }
        }
        holder.itemView.setOnClickListener { view ->

            // previous code
            /*
                    if (prevPos == -1) {
                    } else {
                        newhospitalLists[prevPos].isChecked = false
                        //  holder.table.setVisibility(View.GONE);
                    }
                    notifyItemChanged(prevPos)
                    prevPos = position
             */



            /*    if (hospitalLists.get(position).isChecked()) {
                        holder.radioButton.setChecked(true);
    
                        holder.container.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.contone.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.contthree.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.contfr.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.contfv.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"));
                        holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"));
    
                    } else {
                        holder.container.setBackgroundColor(Color.WHITE);
                        holder.contone.setBackgroundColor(Color.WHITE);
                        holder.conttwo.setBackgroundColor(Color.parseColor("#DFDFDE"));
                        holder.contthree.setBackgroundColor(Color.WHITE);
                        holder.contfr.setBackgroundColor(Color.parseColor("#DFDFDE"));
                        holder.contfv.setBackgroundColor(Color.WHITE);
                        holder.contsix.setBackgroundColor(Color.WHITE);
                        holder.radioButton.setChecked(false);
                    }*/
            /* if (hospitalLists.get(position).isChecked()) {*/
            //    holder.radioButton.setChecked(true);
            /*        holder.container.setBackgroundColor(Color.MAGENTA);
                        holder.contone.setBackgroundColor(Color.MAGENTA);
                        holder.conttwo.setBackgroundColor(Color.MAGENTA);
                        holder.contthree.setBackgroundColor(Color.MAGENTA);
                        holder.contfr.setBackgroundColor(Color.MAGENTA);
                        holder.contfv.setBackgroundColor(Color.MAGENTA);
                        holder.contsix.setBackgroundColor(Color.MAGENTA);
                        holder.contsix.setBackgroundColor(Color.MAGENTA);*/
            /*  } else {
                        holder.container.setBackgroundColor(Color.WHITE);
                        holder.contone.setBackgroundColor(Color.WHITE);
                        holder.conttwo.setBackgroundColor(Color.parseColor("#DFDFDE"));
                        holder.contthree.setBackgroundColor(Color.WHITE);
                        holder.contfr.setBackgroundColor(Color.parseColor("#DFDFDE"));
                        holder.contfv.setBackgroundColor(Color.WHITE);
                        holder.contsix.setBackgroundColor(Color.WHITE);
                        holder.radioButton.setChecked(false);
                    }*/


            //  holder.table.setVisibility(View.VISIBLE);


            //  getPrevVisits(holder, hospitalLists.get(position).getnHfId());
            if (hospitalLists[position].getnHfId() == "") {
                Toast.makeText(
                    view.context,
                    "Can not add or view doctors because it is added in local due to no internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("checkingchecking", position.toString() + "")
                Log.d("mkoi", "onClick: " + hospitalLists[position].getcHfTyp())
                Log.d("mkoi", "onClick: " + hospitalLists[position].getnTuId())

                if (pos==-1) {
                    Log.d("if_condition",pos.toString()+"  "+position.toString());
                    newhospitalLists[position].isChecked = true
                    pos = position
                    notifyItemChanged(pos)
                }else {
                    Log.d("else_condition",pos.toString()+"  "+position.toString());
                   updateAdapter(position)

                }

/*
//                    first = true
//                    holder.radioButton.isChecked = true
//                    holder.container.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.contone.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.conttwo.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.contthree.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.contfr.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.contfv.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    holder.contsix.setBackgroundColor(Color.parseColor("#E0FFFF"))
//                    hospitalLists[position].isChecked = true
//                    newhospitalLists[position].isChecked = true
//                    Log.d("double_tick", "$pos   $position")
//                    pos = position
 */
                if (type == "fdc") {
                    val intent = Intent(context, FdcDispensationToHf::class.java)
                    intent.action = "Selected"
                    intent.putExtra("checked", true)
                    intent.putExtra("tu_id", hospitalLists[position].getnTuId())
                    intent.putExtra("hf_id", hospitalLists[position].getnHfId())
                    intent.putExtra("hf_type_id", hospitalLists[position].getnHfTypId())
                    intent.putExtra("hospitaltypeName", hospitalLists[position].getcHfTyp())
                    intent.putExtra(
                        "hospitallocation",
                        hospitalLists[position].getcDisNam() + ", " + hospitalLists[position].getcStNam()
                    )
                    intent.putExtra("fdctype", "dede")
                    intent.putExtra("hospitalName", hospitalLists[position].getcHfNam())
                    intent.putExtra("docName", hospitalLists[position].getcContPer())
                    intent.putExtra("lastvisit", hospitalLists[position].no_of_days)
                    //   intent.putExtra("docid", hospitalLists.get(position).get());
                    //context.startActivity(intent);
                    LocalBroadcastManager.getInstance(holder.itemView.context).sendBroadcast(intent)
                    notifyItemChanged(position)
                } else {
                    // hospitalLists.get(position).setChecked(true);
                    val intent = Intent()
                    Handler().post {
                        intent.action = "Selected"
                        intent.putExtra("checked", true)


                        /*     Log.d("muyu", "Hospital: tu_id " + hospitalLists.get(position).getnTuId());
                            Log.d("muyu", "Hospital: hf_id " + hospitalLists.get(position).getnHfId());*/intent.putExtra(
                        "tu_id",
                        hospitalLists[position].getnTuId()
                    )
                        intent.putExtra("hf_id", hospitalLists[position].getnHfId())
                        intent.putExtra("hf_type_id", hospitalLists[position].getnHfTypId())
                        intent.putExtra("hospitalName", hospitalLists[position].getcHfNam())
                        intent.putExtra("hospitaltypeName", hospitalLists[position].getcHfTyp())
                        intent.putExtra(
                            "hospitallocation",
                            hospitalLists[position].getcDisNam() + ", " + hospitalLists[position].getcStNam()
                        )
                        intent.putExtra("docName", hospitalLists[position].getcContPer())
                        intent.putExtra("lastvisit", hospitalLists[position].no_of_days)
                        LocalBroadcastManager.getInstance(holder.itemView.context)
                            .sendBroadcast(intent)
                    }
                    notifyItemChanged(position)
                }
                /*        LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent);
                        notifyDataSetChanged();*/
            }
        }
    }

    override fun getItemCount(): Int {
        return hospitalLists.size
    }

    inner class Mholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hospitalName: TextView
        var location: TextView
        var doctorName: TextView
        var typeClinic: TextView
        val hospitalNameTt: TextView
        val hospitalNameLocation: TextView
        val doctorNameTv: TextView
        val hospitalTypeTitle: TextView
        val currentDate: TextView
        private val hospitalNameTv: TextView? = null
        private val hospitalAddress: TextView? = null
        private val hospitalType: TextView? = null
        val lastVisit: TextView
        private val date: TextView? = null
        var container: LinearLayout
        var contone: LinearLayout
        var conttwo: LinearLayout
        var contthree: LinearLayout
        var contfv: LinearLayout
        var contsix: LinearLayout
        var contfr: RelativeLayout
        var bagImg: ImageView
        var radioButton: RadioButton

        init {
            hospitalName = itemView.findViewById(R.id.hospitalName)
            location = itemView.findViewById(R.id.locationn)
            doctorName = itemView.findViewById(R.id.doctorname)
            typeClinic = itemView.findViewById(R.id.hospitaltype)
            bagImg = itemView.findViewById(R.id.bagimg)
            radioButton = itemView.findViewById(R.id.radioButton)
            container = itemView.findViewById(R.id.cont)
            contone = itemView.findViewById(R.id.contone)
            conttwo = itemView.findViewById(R.id.conttwo)
            contthree = itemView.findViewById(R.id.contthree)
            contfr = itemView.findViewById(R.id.contfr)
            contfv = itemView.findViewById(R.id.contfv)
            contsix = itemView.findViewById(R.id.contsixx)
            hospitalNameTt = itemView.findViewById(R.id.hospitalNameTitle)
            hospitalNameLocation = itemView.findViewById(R.id.locationHospital)
            doctorNameTv = itemView.findViewById(R.id.docname)
            hospitalTypeTitle = itemView.findViewById(R.id.hospitalTYpe)
            currentDate = itemView.findViewById(R.id.currentdate)
            lastVisit = itemView.findViewById(R.id.visitdays)
            /*  table = itemView.findViewById(R.id.laytable);
            //table.setVisibility(View.GONE);
            table.setFocusable(false);*/
        }
    }

    fun updateList(list: ArrayList<HospitalList>) {
        hospitalLists = list
        notifyDataSetChanged()
    }

    private fun getRoomPrevVisits(
        parentDataPreviousSamples: List<PrevVisitsData>,
        mholder: Mholder
    ) {
        val currentTime = Calendar.getInstance().time

        //getting difference in days
        var date: Date? = null
        var dateOne: Date? = null
        try {
            // date = curFormater.parse(parentDataPreviousSamples.get(1).getdVisit());
            var formattedDate = ""
            if (!parentDataPreviousSamples.isEmpty()) {
                for (i in 0 until parentDataPreviousSamples[0].getdVisit().length) {
                    if (i < 10) {
                        formattedDate = formattedDate + parentDataPreviousSamples[0].getdVisit()[i]
                    }
                }
            }
            val curFormater = SimpleDateFormat("yyyy-MM-dd")
            dateOne = curFormater.parse(modifyDateLayout(formattedDate))
            date = curFormater.parse(curFormater.format(currentTime))
            val diffInMillies = Math.abs(dateOne.time - date.time)
            val diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
            var ext = ""
            ext = if (diff < 2) {
                "Day"
            } else {
                "Days"
            }
            // mholder.lastVisit.setText(String.valueOf(diff + " " + ext));
            Log.d("bhdyigd", "init: " + curFormater.format(currentTime))
            Log.d("bhdyigd", "init: $formattedDate")
            Log.d("bhdyigd", "init: $diff")
        } catch (e: ParseException) {
            Log.d("bhdyigd", "init: " + "failll")
            e.printStackTrace()
        }
        /*LiveData<List<RoomPrevVisitsData>> roomPrevVisitProviderFromRoom = dataBase.customerDao().getSelectedRoomPrevVisitProviderFromRoom();
        roomPrevVisitProviderFromRoom.observe(this, roomPreviousVisits1 -> {
            List<RoomPrevVisitsData> parentDataPreviousSamples;

            parentDataPreviousSamples = roomPreviousVisits1;

           */
        /* //getting difference in days
            Date date = null;
            Date dateOne = null;
            try {
                // date = curFormater.parse(parentDataPreviousSamples.get(1).getdVisit());
                String formattedDate = "";
                if (!parentDataPreviousSamples.isEmpty()) {
                    for (int i = 0; i < parentDataPreviousSamples.get(0).getdVisit().length(); i++) {
                        if (i < 10) {
                            formattedDate = formattedDate + parentDataPreviousSamples.get(0).getdVisit().charAt(i);
                        }
                    }
                }


                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                dateOne = curFormater.parse(modifyDateLayout(formattedDate));
                date = curFormater.parse(curFormater.format(currentTime));
                long diffInMillies = Math.abs(dateOne.getTime() - date.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                String ext = "";
                if (diff < 2) {
                    ext = "Day";
                } else {
                    ext = "Days";
                }
                mholder.lastVisit.setText(String.valueOf(diff + " " + ext));
                Log.d("bhdyigd", "init: " + curFormater.format(currentTime));
                Log.d("bhdyigd", "init: " + formattedDate);
                Log.d("bhdyigd", "init: " + diff);


            } catch (ParseException e) {
                Log.d("bhdyigd", "init: " + "failll");
                e.printStackTrace();
            }*/
        /*
            //setRecycler();
        });*/
    }

    @Throws(ParseException::class)
    private fun modifyDateLayout(inputDate: String): String {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(inputDate)
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    private fun getPrevVisits(holder: Mholder, hfID: String) {
        if (!BaseUtils.isNetworkAvailable(context)) {
            return
        }
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_visit_typ&w=n_hf_id<<EQUALTO>>$hfID"
        ApiClient.getClient().getPrevVisits(url).enqueue(object : Callback<PrevVisitsResponse> {
            override fun onResponse(
                call: Call<PrevVisitsResponse>,
                response: Response<PrevVisitsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status) {

                        //dataBase.customerDao().deletePrevVisitProviderFromRoom();
                        /*for (int i = 0; i < response.body().getUserData().size(); i++) {
                            RoomPrevVisitsData roomPreviousVisits = new RoomPrevVisitsData(
                                    response.body().getUserData().get(i).getdVisit(),
                                    response.body().getUserData().get(i).getcVal(),
                                    response.body().getUserData().get(i).getnHfId()

                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousVisits.getcVal());

                            dataBase.customerDao().getPrevVisitsProviderFromServer(roomPreviousVisits);
                        }*/
                        getRoomPrevVisits(response.body()!!.userData, holder)
                    }
                }
            }

            override fun onFailure(call: Call<PrevVisitsResponse>, t: Throwable) {}
        })
    }

    fun updateAdapter(posPrev:Int){
        newhospitalLists[pos].isChecked =false
        notifyItemChanged(pos)
        newhospitalLists[posPrev].isChecked = true
        pos = posPrev
        notifyItemChanged(prevPos)
    }

    fun setData(newHospitalLlist: List<HospitalList>) {
        val diffUtil = HospitalDiffUtil(hospitalLists, newHospitalLlist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        hospitalLists = newHospitalLlist
        diffResult.dispatchUpdatesTo(this)
    }
}