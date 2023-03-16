package com.smit.ppsa

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smit.ppsa.Adapter.PrevFdcVisitAdapter
import com.smit.ppsa.BaseUtils.*
import com.smit.ppsa.BaseUtils.showToast
import com.smit.ppsa.Dao.AppDataBase
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Response.*
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse
import com.smit.ppsa.Response.uom.UOMResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FdcForm : AppCompatActivity() {

    companion object {
        var selectProductList: MutableList<RoomMedicines> = ArrayList()
        var parentDataUom: List<RoomUOM> = ArrayList()

        private var select_adapter: SelectedAdapter? = null
        private fun openEnterQuantityPopUp(context: Context, position: Int) {
            val li = LayoutInflater.from(context)
            val dialogView: View = li.inflate(R.layout.add_quantity_dialog, null)
            val sDialog =
                android.app.AlertDialog.Builder(context).setView(dialogView).setCancelable(false)
                    .create()
            val qty = dialogView.findViewById<EditText>(R.id.aqd_qty)
            val submit = dialogView.findViewById<TextView>(R.id.aqd_submit)
            val close = dialogView.findViewById<TextView>(R.id.aqd_close)
            sDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Objects.requireNonNull(sDialog.window)!!.attributes.windowAnimations =
                R.style.ScaleFromCenter
            submit.setOnClickListener(View.OnClickListener {
                if (qty.text.toString().trim { it <= ' ' }.isEmpty()) {
                    showToast(context, "Please enter quantity")
                    return@OnClickListener
                }
                if (qty.text.toString().trim { it <= ' ' } == "0") {
                    showToast(context, "Quantity must be 1 or greater")
                    return@OnClickListener
                }
                val mQty = qty.text.toString().trim { it <= ' ' }.toInt()
                if (mQty < 1) {
                    showToast(context, "Quantity must be 1 or greater")
                    return@OnClickListener
                }
                if (mQty > selectProductList[position].qty) {
                    sDialog.dismiss()
                    selectProductList[position].qty = qty.text.toString().trim { it <= ' ' }
                        .toInt()
                    select_adapter!!.notifyDataSetChanged()
                } else {
                    showToast(context, "Stock not available")
                }
            })
            close.setOnClickListener { sDialog.dismiss() }
            sDialog.show()
        }

        private fun showConfirmPopup(context: Context, position: Int) {
            val li = LayoutInflater.from(context)
            val dialogView: View = li.inflate(R.layout.confirmation_dialog, null)
            val sDialog =
                android.app.AlertDialog.Builder(context).setView(dialogView).setCancelable(false)
                    .create()
            val cancel = dialogView.findViewById<TextView>(R.id.cd_no)
            val yes = dialogView.findViewById<TextView>(R.id.cd_yes)
            sDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Objects.requireNonNull(sDialog.window)!!.attributes.windowAnimations =
                R.style.ScaleFromCenter
            yes.setOnClickListener {
                selectProductList.removeAt(position)
                select_adapter!!.notifyDataSetChanged()
                sDialog.dismiss()
            }
            cancel.setOnClickListener { sDialog.dismiss() }
            sDialog.show()
        }
    }

    lateinit var prevRecyclerView: RecyclerView
    lateinit var dayLabel: TextView
    lateinit var dayET: EditText
    lateinit var dayLayout: LinearLayout
    lateinit var weightBandSpinner: Spinner
    var parentDataWeightBand: List<RoomWeightBand>? = ArrayList()
    private lateinit var backbtn:ImageView
    private lateinit var heading:TextView
    private var wayLatitude: String = ""
    private var wayLongitude = ""
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var hospitalName: TextView
    private lateinit var doctorName: TextView
    private lateinit var no_select_order: Button
    private lateinit var selectedMedRecyclerView: RecyclerView
    private var sDialog: AlertDialog? = null
    private var progressDialog: GlobalProgressDialog? = null
    private var dataBase: AppDataBase? = null
    var parentDataMedicines: List<RoomMedicines> = ArrayList()
    var tem_list: MutableList<RoomMedicines> = ArrayList()
    lateinit var adapter: ProductListAdapter
    lateinit var no_create_order: Button
    private var weightBandString: String = ""
    private lateinit var dateLabel: TextView
    private lateinit var patientName: TextView
    private lateinit var weightBand: LinearLayout
    private lateinit var weightBandLabel: TextView
    private lateinit var dateTextView: TextView
    private lateinit var hospitalLayout: LinearLayout
    private lateinit var patientLayout: LinearLayout
    private lateinit var docLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fdc_form)
        init()

    }


    private fun init() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(""))
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        dataBase = AppDataBase.getDatabase(this)
        progressDialog = GlobalProgressDialog(this)
        dateLabel = findViewById(R.id.date_label)
        weightBandSpinner = findViewById(R.id.anc_dis_name)
        dayET = findViewById(R.id.day_et)
        dayLabel = findViewById(R.id.daysLabel)
        dayLayout = findViewById(R.id.daysLayout)
        weightBand = findViewById(R.id.weightbands)
        weightBandLabel = findViewById(R.id.weightbands_label)
        prevRecyclerView = findViewById(R.id.prev_drug)
        heading = findViewById(R.id.heading)
        backbtn = findViewById(R.id.backbtn)
        dateTextView = findViewById(R.id.f1_testreport)
        no_select_order = findViewById(R.id.no_select_order)
        no_create_order = findViewById(R.id.no_create_order)
        selectedMedRecyclerView = findViewById(R.id.no_select_rv)
        hospitalName = findViewById(R.id.hospitalName)
        doctorName = findViewById(R.id.docname)
        hospitalLayout = findViewById(R.id.hospitalLayout)
        docLayout = findViewById(R.id.doc_layout)
        patientLayout = findViewById(R.id.patientLayout)
        patientName = findViewById(R.id.patientName)
        no_select_order.setOnClickListener {
            showProductDialog()
        }
        if (intent.hasExtra("issued")){
            getPrevDrugList()
            heading.text = "Dispensation to HF"
            dateTextView.visibility = View.VISIBLE
            dateLabel.apply {
                visibility = View.VISIBLE
                text = "Date Dispensation*"
            }
            if (intent.getStringExtra("issued").equals("patient")){
                weightBand.visibility = View.VISIBLE
                weightBandLabel.visibility = View.VISIBLE
                dayLayout.visibility = View.VISIBLE
                dayET.visibility = View.VISIBLE
                dayLabel.visibility = View.VISIBLE
                patientLayout.visibility = View.VISIBLE
                patientName.text = intent.getStringExtra("patient_name")
                heading.text = "Dispensation to Patient"
            }

        }else if (intent.hasExtra("rec")){
            heading.text = "FDC Received"
            dateLabel.visibility = View.VISIBLE
            dateTextView.visibility = View.VISIBLE
            hospitalLayout.visibility = View.GONE
            docLayout.visibility = View.GONE
            no_create_order.text = "Receive FDC"
            dateLabel.text = "Accept date"
        }else{
            no_create_order.text = "Update Stock"
        }
        hospitalName.setText(intent.getStringExtra("hospitalName"))
        doctorName.setText(intent.getStringExtra("docName"))
        select_adapter = SelectedAdapter()
        val manager = LinearLayoutManager(this)
        selectedMedRecyclerView.setLayoutManager(manager)
        selectedMedRecyclerView.setAdapter(select_adapter)
        getMedicines()
        getUOM()
        backbtn.setOnClickListener {
            super.onBackPressed()
        }
        no_create_order.setOnClickListener {
            if(intent.hasExtra("rec")){
                if(dateTextView.text.equals("")){
                    showToast(this,"Select date")
                }else if (selectProductList.size==0){
                    showToast(this,"Select minimum one medicine")
                }else if (!selectProductList.all { it.uom !="" }){
                    showToast(this,"Select unit of measurement for the medicine")
                }else{
                postFdcRec(selectProductList)
                }
            }else if(intent.hasExtra("op")){
                if (selectProductList.size==0){
                    showToast(this,"Select minimum one medicine")
                }else if (!selectProductList.all { it.uom !="" }){
                    showToast(this,"Select unit of measurement for the medicine")
                }else {
                    postFdcOpBal(selectProductList)
                }
            }else if (intent.hasExtra("issued")){
                if(dateTextView.text.equals("")){
                    showToast(this,"Select date")
                }else if (selectProductList.size==0){
                    showToast(this,"Select minimum one medicine")
                }else if (!selectProductList.all { it.uom !="" }){
                    showToast(this,"Select unit of measurement for the medicine")
                }else{
                    if(intent.getStringExtra("issued").equals("hospital")){
                        postDataIssued(selectProductList)
                    }else{
                        if (weightBandString.equals("")){
                            showToast(this,"Select weight band")
                        }else if(dayET.text.toString().equals("")){
                            showToast(this,"Enter no. of days")
                        }else if(dayET.text.toString().toInt()>120){
                            showToast(this,"no. of days should not be more than 120")

                        }
                        else{
                            postDataIssued(selectProductList)
                        }
                    }
                }
            }
        }
        weightBandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i == 0) {
                    weightBandString = ""
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    weightBandString = parentDataWeightBand!![i - 1].id
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        setUpTestReportCalender()
        getWeight()
    }

    override fun onDestroy() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        }
        super.onDestroy()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 123
            )
            return
        }
        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                wayLatitude = location.latitude.toString()
                wayLongitude = location.longitude.toString()
                //                  lat=30.977006;
//                  lng=76.537880;
                //saveAddress();
            } else {
                // Toast.makeText(HomeActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }.addOnFailureListener {
            //  Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private fun getMedicines() {
        progressDialog!!.showProgressBar()
        if (!BaseUtils.isNetworkAvailable(this)) {
            showToast(
                this,
                "Please Check your internet  Connectivity",

            )
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog!!.hideProgressBar()
            return
        }
        BaseUtils.putPatientName(this, intent.getStringExtra("patient_name"))
        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d(
            "dkl9",
            "getPatiena: " + BaseUtils.getUserInfo(this).getnUserLevel()
        )
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_med&w=id<<GT>>0"
        ApiClient.getClient().getMedicine(url).enqueue(object : Callback<MedicineResponse> {
            override fun onResponse(
                call: Call<MedicineResponse>,
                response: Response<MedicineResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "true") {
                        //  parentDataTestReportResults = response.body().getUser_data();
                        Log.d("gugwdwd", "onResponse: " + response.body()!!.status)
                        Log.d("gugwdwd", "onResponse: " + response.body()!!.user_data)
                        dataBase!!.customerDao()!!.deleteRoomAllMedicines()
                        for (i in response.body()!!.user_data!!.indices) {
                            val roomMedicines = RoomMedicines(
                                response.body()!!.user_data!![i].id,
                                response.body()!!.user_data!![i].c_val
                            )

                            Log.d("kiuij", "onResponse: " + roomMedicines.id)
                            dataBase!!.customerDao().getMedicinesFromServer(roomMedicines)
                        }
                        getRoomMedicines()
                    }
                }
                progressDialog!!.hideProgressBar()
            }

            override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                progressDialog!!.hideProgressBar()
            }
        })
    }

    private fun getRoomMedicines() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui")
        val medicinesFromRoom: LiveData<List<RoomMedicines>> =
            dataBase!!.customerDao().getSelectedMedicinesFromRoom()
        medicinesFromRoom.observe(
            this
        ) { roomMedicines: List<RoomMedicines> ->
            parentDataMedicines = roomMedicines
        }
    }

    private fun getUOM() {
        progressDialog!!.showProgressBar()
        if (!isNetworkAvailable(this)) {
            showToast(
                this,
                "Please Check your internet  Connectivity",
            )
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog!!.hideProgressBar()
            return
        }
        BaseUtils.putPatientName(this, intent.getStringExtra("patient_name"))

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d(
            "dkl9",
            "getPatiena: " + BaseUtils.getUserInfo(this).getnUserLevel()
        )
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_uom&w=id<<GT>>0"
        ApiClient.getClient().getUOM(url).enqueue(object : Callback<UOMResponse> {
            override fun onResponse(call: Call<UOMResponse>, response: Response<UOMResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "true") {
                        //  parentDataTestReportResults = response.body().getUser_data();
                        Log.d("gugwdwd", "onResponse: " + response.body()!!.status)
                        Log.d("gugwdwd", "onResponse: " + response.body()!!.user_data)
                        for (i in response.body()!!.user_data!!.indices) {
                            val roomUOM = RoomUOM(
                                response.body()!!.user_data!![i].id,
                                response.body()!!.user_data!![i].c_val
                            )
                            Log.d("kiuij", "onResponse: " + roomUOM.id)
                            dataBase!!.customerDao().getUOMFromServer(roomUOM)
                        }

                        getRoomUOM()
                    }
                }
                progressDialog!!.hideProgressBar()
            }

            override fun onFailure(call: Call<UOMResponse>, t: Throwable) {
                progressDialog!!.hideProgressBar()
            }
        })
    }

    private fun getRoomUOM() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui")
        val uomFromRoom = dataBase!!.customerDao().selectedUOMFromRoom
        uomFromRoom.observe(
            this
        ) { roomUOMS: List<RoomUOM> ->
            parentDataUom = roomUOMS

        }
    }

    private fun showProductDialog() {
        val li = LayoutInflater.from(this)
        val dialogView: View = li.inflate(R.layout.select_med_dialog, null)
        sDialog = AlertDialog.Builder(this).setView(dialogView).setCancelable(false).create()
        val cancel = dialogView.findViewById<TextView>(R.id.pd_cancel)
        val msg = dialogView.findViewById<TextView>(R.id.pd_msg)
        val search = dialogView.findViewById<EditText>(R.id.pd_search)
        val list = dialogView.findViewById<RecyclerView>(R.id.pd_rv_style)
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty()) {
                    val temp: MutableList<RoomMedicines> = ArrayList<RoomMedicines>()
                    for (st in parentDataMedicines) {
                        if (st.c_val.lowercase(Locale.ROOT)
                                .contains(s.toString().lowercase(Locale.getDefault()))
                        ) {
                            temp.add(st)
                        }
                    }
                    tem_list = temp
                    list.adapter = null
                    if (tem_list.size > 0) {
                        msg.visibility = View.GONE
                        list.visibility = View.VISIBLE
                        adapter = ProductListAdapter(
                            tem_list,
                            sDialog!!
                        )
                        val manager = LinearLayoutManager(applicationContext)
                        list.layoutManager = manager
                        list.setHasFixedSize(true)
                        list.adapter = adapter
                    } else {
                        list.visibility = View.GONE
                        msg.visibility = View.VISIBLE
                    }
                } else {
                    msg.visibility = View.GONE
                    list.visibility = View.VISIBLE
                    if (tem_list.size > 0) {
                        tem_list.clear()
                    }
                    tem_list.addAll(parentDataMedicines)
                    list.adapter = null
                    adapter = ProductListAdapter(
                        parentDataMedicines.toMutableList(),
                        sDialog!!
                    )
                    val manager = LinearLayoutManager(applicationContext)
                    list.layoutManager = manager
                    list.setHasFixedSize(true)
                    list.adapter = adapter
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        adapter = ProductListAdapter(parentDataMedicines.toMutableList(), sDialog!!)
        Log.d("Medicines List",parentDataMedicines.toString())
        val manager = LinearLayoutManager(applicationContext)
        list.layoutManager = manager
        list.setHasFixedSize(true)
        list.adapter = adapter
        /*Objects.requireNonNull<Window>(sDialog!!.getWindow()).attributes.windowAnimations =
            R.style.ScaleFromCenter*/
        sDialog!!.show()
        cancel.setOnClickListener { sDialog!!.dismiss() }
    }

    class ProductListAdapter(allProduct: MutableList<RoomMedicines>, sDialog: AlertDialog) :
        RecyclerView.Adapter<ProductListAdapter.DataHolder>() {
        private val allProduct: MutableList<RoomMedicines>
        private val sDialog: AlertDialog
        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun setHasStableIds(hasStableIds: Boolean) {
            super.setHasStableIds(hasStableIds)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
            return DataHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
            )
        }

        @SuppressLint("RecyclerView")
        override fun onBindViewHolder(holder: DataHolder, position: Int) {
            holder.run {
                name.setText(allProduct[position].c_val)
                main.setOnClickListener {
                    val intent = Intent()
                    intent.apply {
                        action = ""
                        putExtra("medicine", allProduct.get(position))
                    }
                    LocalBroadcastManager.getInstance(holder.itemView.context)
                        .sendBroadcast(intent)
                    sDialog.dismiss()
                }
            }
        }

        override fun getItemCount(): Int {
            return allProduct.size
        }

        inner class DataHolder(view: View) : RecyclerView.ViewHolder(view) {
            var name: TextView
            var main: LinearLayout

            init {
                name = view.findViewById(R.id.rp_name)
                main = view.findViewById(R.id.rp_main)
            }
        }

        init {
            this.allProduct = allProduct
            this.sDialog = sDialog
        }
    }

    class SelectedAdapter : RecyclerView.Adapter<SelectedAdapter.DataHolder>() {

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun setHasStableIds(hasStableIds: Boolean) {
            super.setHasStableIds(hasStableIds)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
            return DataHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_selected_product, parent, false)
            )
        }

        @SuppressLint("RecyclerView")
        override fun onBindViewHolder(holder: DataHolder, position: Int) {
            holder.name.setText(selectProductList.get(position).c_val)

            /*String[] orderStatusList = {"Pack", "Box", "Carton"};
            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, orderStatusList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spOrderType.setAdapter(aa);*/
            val stringnames: MutableList<String> =
                ArrayList()
            stringnames.clear()
            for (i in parentDataUom.indices) {
                if (!stringnames.contains(parentDataUom.get(i).getC_val())) {
                    stringnames.add(parentDataUom.get(i).getC_val())
                }
            }
            setSpinnerAdapter(holder.uomSpinner, stringnames, holder.itemView.context)
            holder.uomSpinner.setSelection(selectProductList.get(position).uomPos)
            //holder.available.setText(String.valueOf(finalPack));
            // holder.spOrderType.setEnabled(false);
            /*switch (selectProductList.get(position).uom) {
                case "Pack":



                    holder.spOrderType.setSelection(0);

                    break;
                case "Box":
                    holder.price.setText(selectProductList.get(position).boxPrice);
                    mPrice = Float.parseFloat(selectProductList.get(position).boxPrice);
                    mPrice = mPrice * selectProductList.get(position).pro_quantity;
                    holder.totalPrice.setText(String.valueOf(mPrice));
                    holder.spOrderType.setSelection(1);
                    holder.available.setText(String.valueOf(finalBox));
                    break;
                case "Carton":
                    holder.price.setText(selectProductList.get(position).cartonPrice);
                    mPrice = Float.parseFloat(selectProductList.get(position).cartonPrice);
                    mPrice = mPrice * selectProductList.get(position).pro_quantity;
                    holder.totalPrice.setText(String.valueOf(mPrice));
                    holder.spOrderType.setSelection(2);
                    holder.available.setText(String.valueOf(finalCarton));
                    break;
            }*/
            holder.quantity.setText((selectProductList.get(position).qty.toString()))
            holder.quantity.setOnClickListener {
                openEnterQuantityPopUp(
                    holder.itemView.context,
                    position
                )
            }
            holder.plus.setOnClickListener {
                selectProductList.get(position)
                    .qty = selectProductList.get(position).qty + 1
                select_adapter!!.notifyDataSetChanged()
            }
            holder.minus.setOnClickListener {
                if (selectProductList.get(position).qty > 1) {
                    selectProductList.get(position)
                        .qty = selectProductList.get(position).qty - 1
                    select_adapter!!.notifyDataSetChanged()
                }
            }
            holder.delete.setOnClickListener { showConfirmPopup(holder.itemView.context, position) }

            holder.uomSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {
                        selectProductList.get(position).uom = ""
                        selectProductList.get(position).uomPos = p2
                        //Log.d("dded", "onItemSelected: $uom")
                    } else {
                        //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                        selectProductList.get(position).uom = parentDataUom[p2 - 1].id
                        selectProductList.get(position).uomPos = p2
                        //Log.d("dded", "onItemSelected: $uom")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            })
            /*holder.spOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int mpos, long id) {
                String mSel = orderStatusList[mpos];
                mPrice = 0.0f;
                selectProductList.get(position).setOrder_type(mSel);
                switch (mSel) {
                    case "Pack":
                        holder.price.setText(selectProductList.get(position).packPrice);
                        mPrice = Float.parseFloat(selectProductList.get(position).packPrice);
                        mPrice = mPrice * selectProductList.get(position).pro_quantity;
                        holder.totalPrice.setText(String.valueOf(mPrice));
                        holder.available.setText(String.valueOf(finalPack));
                        break;
                    case "Box":
                        holder.price.setText(selectProductList.get(position).boxPrice);
                        mPrice = Float.parseFloat(selectProductList.get(position).boxPrice);
                        mPrice = mPrice * selectProductList.get(position).pro_quantity;
                        holder.totalPrice.setText(String.valueOf(mPrice));
                        holder.available.setText(String.valueOf(finalBox));
                        break;
                    case "Carton":
                        holder.price.setText(selectProductList.get(position).cartonPrice);
                        mPrice = Float.parseFloat(selectProductList.get(position).cartonPrice);
                        mPrice = mPrice * selectProductList.get(position).pro_quantity;
                        holder.totalPrice.setText(String.valueOf(mPrice));
                        holder.available.setText(String.valueOf(finalCarton));
                        break;
                }
                setPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        }

        override fun getItemCount(): Int {
            return selectProductList.size
        }

        inner class DataHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView
            val plus: TextView
            val minus: TextView
            val quantity: TextView
            val delete: ImageView
            val uomSpinner: Spinner

            init {
                name = view.findViewById(R.id.rsp_name)
                plus = view.findViewById(R.id.rsp_add)
                minus = view.findViewById(R.id.rsp_minus)
                quantity = view.findViewById(R.id.rsp_quantity)

                uomSpinner = view.findViewById(R.id.anc_dis_name)
                delete = view.findViewById(R.id.rsp_remove)

            }
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let {
                when {
                    it.hasExtra("medicine") -> {
                        Log.d("med_select:  ", "item_selected")
                        if (selectProductList.contains(it.getSerializableExtra("medicine"))) {
                        } else {
                            selectProductList.add(it.getSerializableExtra("medicine") as RoomMedicines)
                            select_adapter!!.notifyDataSetChanged()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onPause() {
        selectProductList.clear()
        super.onPause()
    }

    private fun postFdcRec(roomMed: List<RoomMedicines>){
        if (!isNetworkAvailable(this)) {
            showToast(this, "Please Check your internet  Connectivity")
            return
        }
        var int= 0
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(),wayLatitude)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(),wayLongitude)
        val date = RequestBody.create("text/plain".toMediaTypeOrNull(),dateTextView.text.toString())
        val n_staff_info = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnUserLevel())
        val n_user_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getId())
        val n_sanc = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).n_staff_sanc)
        for ((index,room) in roomMed.withIndex()){
            val n_med_id = RequestBody.create("text/plain".toMediaTypeOrNull(),room.id)
            val n_uom_id = RequestBody.create("text/plain".toMediaTypeOrNull(),room.uom)
            val n_qty = RequestBody.create("text/plain".toMediaTypeOrNull(),room.qty.toString())
            ApiClient.getClient().postFdcRec(n_med_id,n_uom_id,n_qty,n_lat,n_lng,n_staff_info,n_user_id,date,n_sanc).enqueue(object: Callback<AddDocResponse>{
                override fun onResponse(
                    call: Call<AddDocResponse>,
                    response: Response<AddDocResponse>
                ) {
                    if(index==roomMed.size-1){
                        showToast(this@FdcForm,"Submitted")
                        finish()
                    }
                }

                override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {
                }
            })
            int++
        }


    }

    private fun postFdcOpBal(roomMed: List<RoomMedicines>){
        progressDialog!!.showProgressBar()
        if (!isNetworkAvailable(this)) {
            showToast(this, "Please Check your internet  Connectivity")

            return
        }

        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnStCd())
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnDisCd())
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(),
            getIntent().getStringExtra("tu_id").toString()
        )
        val n_hf_id = RequestBody.create("text/plain".toMediaTypeOrNull(),
            getIntent().getStringExtra("hf_id").toString()
        )
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(),wayLatitude)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(),wayLongitude)
        val n_staff_info = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnUserLevel())
        val n_user_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getId())

        for ((index,room) in roomMed.withIndex()){
            val n_med_id = RequestBody.create("text/plain".toMediaTypeOrNull(),room.id)
            val n_uom_id = RequestBody.create("text/plain".toMediaTypeOrNull(),room.uom)
            val n_qty = RequestBody.create("text/plain".toMediaTypeOrNull(),room.qty.toString())
            ApiClient.getClient().postFdcOpBal(n_st_id,n_dis_id,n_tu_id,n_hf_id,n_med_id,n_uom_id,n_qty,n_lat,n_lng,n_staff_info,n_user_id).enqueue(object: Callback<AddDocResponse>{
                override fun onResponse(
                    call: Call<AddDocResponse>,
                    response: Response<AddDocResponse>
                ) {
                    progressDialog!!.hideProgressBar()
                    if(index==roomMed.size-1){
                        showToast(this@FdcForm,"Submitted")
                        finish()
                    }
                }

                override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {
                }
            })

        }
    }

    private fun setUpTestReportCalender() {
        val trCalendar = Calendar.getInstance()
        val trdate =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                trCalendar[Calendar.YEAR] = year
                trCalendar[Calendar.MONTH] = monthOfYear
                trCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                //dTestReport = sdf.format(trCalendar.time)
                dateTextView.setText(sdf.format(trCalendar.time))
            }
        dateTextView.setOnClickListener(View.OnClickListener {
            val m_date = DatePickerDialog(
                this@FdcForm, R.style.calender_theme, trdate,
                trCalendar[Calendar.YEAR], trCalendar[Calendar.MONTH],
                trCalendar[Calendar.DAY_OF_MONTH]
            )
            m_date.show()
            val calendar = Calendar.getInstance()

            m_date.datePicker.maxDate = calendar.timeInMillis
            m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK)
            m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY)
        })
    }

    private fun addNoOfDays(date: String,noOfDays: Int):String{
        var dateInString = date // Start date

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        c.time = sdf.parse(dateInString)
        c.add(Calendar.DATE, noOfDays)
        sdf = SimpleDateFormat("yyyy-MM-dd")
        val resultdate = Date(c.timeInMillis)
        dateInString = sdf.format(resultdate)
        println("String date:$dateInString")
        return dateInString
    }
    private fun postDataIssued(roomMed: List<RoomMedicines>){
        progressDialog!!.showProgressBar()
        if (!BaseUtils.isNetworkAvailable(this)) {
            showToast(this, "Please Check your internet  Connectivity")
            return
        }
        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnStCd())
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnDisCd())
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(),
            getIntent().getStringExtra("tu_id").toString()
        )
        val n_hf_id = RequestBody.create("text/plain".toMediaTypeOrNull(),
            getIntent().getStringExtra("hf_id").toString()
        )
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(),wayLatitude)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(),wayLongitude)
        val n_staff_info = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getnUserLevel())
        val n_user_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getId())
        val n_doc_id = RequestBody.create("text/plain".toMediaTypeOrNull(),intent.getStringExtra("doc_id").toString())
       // val n_sanc = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserOtherInfo(this).n_staff_sanc)
        val date = RequestBody.create("text/plain".toMediaTypeOrNull(),dateTextView.text.toString())
        val d_refill = if (!intent.getStringExtra("issued").equals("hospital")){
            RequestBody.create("text/plain".toMediaTypeOrNull(),addNoOfDays(dateTextView.text.toString(),dayET.text.toString().toInt()))
        } else {
            RequestBody.create("text/plain".toMediaTypeOrNull(),"1")
        }
        for ((index,room) in roomMed.withIndex()){
            val n_med_id = RequestBody.create("text/plain".toMediaTypeOrNull(),room.id)
            val n_uom_id = RequestBody.create("text/plain".toMediaTypeOrNull(),room.uom)
            val n_qty = RequestBody.create("text/plain".toMediaTypeOrNull(),room.qty.toString())
            val apiClient = if(intent.getStringExtra("issued").equals("hospital")){
                ApiClient.getClient().postFDCIssued(n_st_id,n_dis_id,n_tu_id,n_hf_id,n_med_id,n_uom_id,n_qty,n_lat,n_lng,n_staff_info,n_user_id,n_doc_id,date)
            }else{
                ApiClient.getClient().postFDCIssuedWeight(n_st_id,n_dis_id,n_tu_id,n_hf_id,n_med_id,n_uom_id,n_qty,n_lat,n_lng,n_staff_info,n_user_id,n_doc_id,date,RequestBody.create("text/plain".toMediaTypeOrNull(),weightBandString),
                    RequestBody.create("text/plain".toMediaTypeOrNull(),intent.getStringExtra("enroll_id").toString()),RequestBody.create("text/plain".toMediaTypeOrNull(),dayET.text.toString()),d_refill)
            }
            apiClient.enqueue(object: Callback<AddDocResponse>{
                override fun onResponse(
                    call: Call<AddDocResponse>,
                    response: Response<AddDocResponse>
                ) {
                    progressDialog!!.hideProgressBar()
                    if(index==roomMed.size-1){
                        showToast(this@FdcForm,"Submitted")
                        startActivity(Intent(this@FdcForm,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        finish()
                    }
                }

                override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {
                }
            })

        }
    }
    private fun getWeight() {
        progressDialog!!.showProgressBar()
        if (!BaseUtils.isNetworkAvailable(this@FdcForm)) {
            showToast(
                this@FdcForm,
                "Please Check your internet  Connectivity",

            )
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog!!.hideProgressBar()
            return
        }
        BaseUtils.putPatientName(
            this@FdcForm,
            intent.getStringExtra("patient_name")
        )

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d(
            "dkl9",
            "getPatiena: " + BaseUtils.getUserInfo(this@FdcForm).getnUserLevel()
        )
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_wght_bnd&w=id<<GT>>0"
        ApiClient.getClient().getWeight(url).enqueue(object : Callback<WeightResponse> {
            override fun onResponse(
                call: Call<WeightResponse>,
                response: Response<WeightResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "true") {
                        //  parentDataTestReportResults = response.body().getUser_data();
                        Log.d("gugwdwd", "onResponse: " + response.body()!!.status)
                        Log.d("gugwdwd", "onResponse: " + response.body()!!.user_data)
                        for (i in response.body()!!.user_data!!.indices) {
                            val roomWeightBand = RoomWeightBand(
                                response.body()!!.user_data!![i].id,
                                response.body()!!.user_data!![i].c_val
                            )
                            Log.d("kiuij", "onResponse: " + roomWeightBand.id)
                            dataBase!!.customerDao().getWeightBandsFromServer(roomWeightBand)
                        }
                        getRoomWeightBands()
                    }
                }
                progressDialog!!.hideProgressBar()
            }

            override fun onFailure(call: Call<WeightResponse>, t: Throwable) {
                progressDialog!!.hideProgressBar()
            }
        })
    }
    private fun getRoomWeightBands() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui")
        val weightBandFromRoom = dataBase!!.customerDao().selectedWeightBandFromRoom
        weightBandFromRoom.observe(
            this@FdcForm
        ) { roomWeightBands: List<RoomWeightBand> ->
            parentDataWeightBand = roomWeightBands
            val stringnames: MutableList<String> =
                ArrayList()
            stringnames.clear()
            for (i in parentDataWeightBand!!.indices) {
                if (!stringnames.contains(parentDataWeightBand!!.get(i).getC_val())) {
                    stringnames.add(parentDataWeightBand!!.get(i).getC_val())
                }
                setSpinnerAdapter(weightBandSpinner, stringnames, this)
            }
        }
    }
    private fun getPrevDrugList(){
        if (!BaseUtils.isNetworkAvailable(this@FdcForm)) {
            showToast(
                this@FdcForm,
                "Please Check your internet  Connectivity",

            )
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return
        }
        var url= ""
        if (intent.hasExtra("issued")){
            if (intent.getStringExtra("issued").equals("hospital")){
                url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fdc_issue&w=n_tu_id<<EQUALTO>>"+getIntent().getStringExtra("tu_id").toString()+"<<AND>>n_hf_id<<EQUALTO>>"+getIntent().getStringExtra("hf_id").toString()+"<<AND>>n_doc_id<<EQUALTO>>"+intent.getStringExtra("doc_id").toString()
            }else{
                url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fdc_disp&w=n_tu_id<<EQUALTO>>"+getIntent().getStringExtra("tu_id").toString()+"<<AND>>n_hf_id<<EQUALTO>>"+getIntent().getStringExtra("hf_id").toString()+
                        "<<AND>>n_doc_id<<EQUALTO>>"+intent.getStringExtra("doc_id").toString()+"<<AND>>n_enroll_id<<EQUALTO>>"+intent.getStringExtra("enroll_id").toString()
            }
        }
        ApiClient.getClient().getFdcForm(url).enqueue(object: Callback<PrevVisitsResponse>{
            override fun onResponse(
                call: Call<PrevVisitsResponse>,
                response: Response<PrevVisitsResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.status){
                        findViewById<LinearLayout>(R.id.layout).visibility = View.VISIBLE
                        prevRecyclerView.layoutManager = LinearLayoutManager(this@FdcForm)
                        prevRecyclerView.adapter = PrevFdcVisitAdapter(response.body()!!.userData)
                    }
                }
            }

            override fun onFailure(call: Call<PrevVisitsResponse>, t: Throwable) {
//                TODO("Not yet implemented")
            }
        })
    }

}