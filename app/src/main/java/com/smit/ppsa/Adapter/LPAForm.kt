package com.smit.ppsa.Adapter

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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
import com.smit.ppsa.*
import com.smit.ppsa.Dao.AppDataBase
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Response.AddDocResponse
import com.smit.ppsa.Response.RegisterParentData
import com.smit.ppsa.Response.RoomLpaTestResult
import com.smit.ppsa.Response.RoomPreviousSamples
import com.smit.ppsa.Response.lpaTestResult.LpaTestResultResponse
import com.smit.ppsa.Response.previoussamplesformsix.PreviousSampleResponse
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LPAForm : AppCompatActivity() {
    private lateinit var hospitalName: TextView
    private lateinit var docname: TextView
    private lateinit var patientName: TextView
    private lateinit var lpaDate: TextView
    private lateinit var lpaResult: Spinner
    private lateinit var backbtn: ImageView
    private var lat = ""
    private var lng = ""

    private var dataBase: AppDataBase? = null
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var frontselectedImageUri: Uri? = null
    //   File Frontfile = new File(""), BackFile = new File("");
    var SELECT_PICTURE = 200
    private val REQUEST_EXTERNAL_STORAGE = 1
    var parentDataLpaSampleResult: List<RoomLpaTestResult>? = null
    private lateinit var lpaRsltImg: ImageView
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var bt_proceedone: CardView
    private lateinit var previoussamplerecycler: RecyclerView

    private var patient: RegisterParentData = RegisterParentData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lpaform)
        init()
    }
    private fun init(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        previoussamplerecycler = findViewById(R.id.previoussamplecollectionsrecycler)
        backbtn = findViewById(R.id.backbtn)
        dataBase = AppDataBase.getDatabase(this)
        bt_proceedone = findViewById(R.id.bt_proceedone)
        patientName= findViewById(R.id.patientName)
        hospitalName = findViewById(R.id.hospitalName)
        docname= findViewById(R.id.docname)
        lpaRsltImg = findViewById(R.id.addpatientfrontimg)
        lpaDate = findViewById(R.id.lpaDate)
        lpaResult = findViewById(R.id.lpaResult)
        patient = intent.getSerializableExtra("pat_ob") as RegisterParentData
        patientName.text = patient.getcPatNam()
        hospitalName.text = patient.c_hf_nam
        docname.text = patient.getcDocNam()

        backbtn.setOnClickListener {
            super.onBackPressed()
        }
        lpaRsltImg.setOnClickListener {
            chooseImage(this)
        }
        bt_proceedone.setOnClickListener {
            if (lpaDate.text.isEmpty()){
                BaseUtils.showToast(this,"Please select date of sample for LPA")
//            }
//            else if(lpaResult.selectedItemPosition==0){
//                BaseUtils.showToast(this,"Please select LPA test result")
//            }else if(frontselectedImageUri==null){
//                BaseUtils.showToast(this,"Please select test report page image for upload")
            }else{
                postLpaResult()
            }
        }
        setUpTestReportCalender()
        getLpaTestResult()
        getLocation()
        prevSample()
    }
    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
        val builder = AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                // Open the camera and get the photo
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                // choose from  external storage
                imageChooser()
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }
    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
    fun imageChooser() {
        verifyStoragePermissions(this@LPAForm)
        // create an instance of the
        // intent of the type image
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), SELECT_PICTURE)
        //   CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FormSix.this);
    }
    private fun setUpTestReportCalender() {
        val trCalendar = Calendar.getInstance()
        val trdate =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                trCalendar[Calendar.YEAR] = year
                trCalendar[Calendar.MONTH] = monthOfYear
                trCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val myFormat = "yyyy-MM-dd"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                //dTestReport = sdf.format(trCalendar.time)
                lpaDate.setText(sdf.format(trCalendar.time))
            }
        lpaDate.setOnClickListener(View.OnClickListener {
            val m_date = DatePickerDialog(
                this@LPAForm, R.style.calender_theme, trdate,
                trCalendar[Calendar.YEAR], trCalendar[Calendar.MONTH],
                trCalendar[Calendar.DAY_OF_MONTH]
            )
            m_date.show()
//            val calendar = Calendar.getInstance()
//
//            m_date.datePicker.maxDate = calendar.timeInMillis
            m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK)
            m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY)
        })
    }
    private fun getLpaTestResult() {

        if (!BaseUtils.isNetworkAvailable(this@LPAForm)) {
            Toast.makeText(
                this@LPAForm,
                "Please Check your internet  Connectivity",
                Toast.LENGTH_SHORT
            ).show()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            return
        }
        if (BaseUtils.getFormSixTuId(this@LPAForm) == intent.getStringExtra("tu_id") && BaseUtils.getFormSixHfId(
                this@LPAForm
            ) == intent.getStringExtra("hf_id")
        ) {
            getRoomLpaTestResult()
        }

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(this@LPAForm).getnUserLevel())

        // if (isTrueNatOrCbNaat.equals(false)) {
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_lpa_rsult&w=id<<GT>>0"
        ApiClient.getClient().getLpaTestResults(url)
            .enqueue(object : Callback<LpaTestResultResponse> {
                override fun onResponse(
                    call: Call<LpaTestResultResponse>,
                    response: Response<LpaTestResultResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "true") {
                            //   parentDataLpaSampleResult = response.body().getUser_data();
                            for (i in response.body()!!.user_data!!.indices) {
                                val roomLpaTestResult = RoomLpaTestResult(
                                    response.body()!!.user_data!![i].id,
                                    response.body()!!.user_data!![i].c_val
                                )
                                Log.d("kiuij", "onResponse: " + roomLpaTestResult.id)
                                dataBase!!.customerDao().getLpaTestResultFromServer(roomLpaTestResult)
                            }
                            Log.d("gug", "onResponse: " + response.body()!!.status)
                            getRoomLpaTestResult()
                            LocalBroadcastManager.getInstance(this@LPAForm)
                                .sendBroadcast(Intent().setAction("").putExtra("setRecycler", ""))
                        }
                    }
                }

                override fun onFailure(call: Call<LpaTestResultResponse>, t: Throwable) {
                }
            })
        //   }
    }
    private fun getRoomLpaTestResult() {
        Log.d("j8ijo", "getRoomTestResult: mkljui")
        val roomLpaResult: LiveData<List<RoomLpaTestResult>?> =
            dataBase!!.customerDao().getSelectedLpaResultFromRoom()
        roomLpaResult.observe(
            this@LPAForm
        ) { roomLpaTestResults: List<RoomLpaTestResult>? ->
            parentDataLpaSampleResult = roomLpaTestResults
            val stringnames: MutableList<String> =
                ArrayList()
            stringnames.clear()
            for (i in parentDataLpaSampleResult!!.indices) {
                if (!stringnames.contains(parentDataLpaSampleResult!!.get(i).getC_val())) {
                    stringnames.add(parentDataLpaSampleResult!!.get(i).getC_val())
                }
                setSpinnerAdapter(lpaResult, stringnames);
            }
        }
    }
    private fun setSpinnerAdapter(spinner: Spinner, values: List<String>) {
        val spinnerAdapter = CustomSpinnerAdapter(this@LPAForm, values)
        spinner.adapter = spinnerAdapter
    }

    // Method to get the absolute path of the selected image from its URI
    /*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    part_image = cursor.getString(indexImage);
                    imgPath.setText(part_image);                                                        // Get the image file absolute path
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
                }
            }
        }
    }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE /*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                // Get the url of the image from data
                // Uri selectedImageUri = data.getData();
                /*if (null != selectedImageUri) {*/
                // update the preview image in the layout


                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                    /*   if (frontselectedImageUri != null) {*/
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                    /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }*/
                    frontselectedImageUri = data!!.data
                    lpaRsltImg.setImageURI(frontselectedImageUri)
                    startCrop(frontselectedImageUri!!)
                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);


                // }
            } else if (requestCode == 0 /*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                // Get the url of the image from data
                // Uri selectedImageUri = data.getData();
                /*if (null != selectedImageUri) {*/
                // update the preview image in the layout

                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                    /*   if (frontselectedImageUri != null) {*/
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                    /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }*/
                    val selectedImage = data!!.extras!!["data"] as Bitmap?
                    frontselectedImageUri = selectedImage?.let {
                        getImageUri(applicationContext,
                            it
                        )
                    }
                    lpaRsltImg.setImageURI(frontselectedImageUri)
                    startCrop(frontselectedImageUri!!)
                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);


                // }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (data != null) {

                        //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                        //   frontselectedImageUri = data.getData();                                                         // Get the image file URI
                        //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                        /*   if (frontselectedImageUri != null) {*/
                        //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                        // testReportFrontImg.setImageURI(frontselectedImageUri);
                        /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }*/
                        // get the returned data
                        val uri = UCrop.getOutput(data)
                        frontselectedImageUri = uri
                        lpaRsltImg.setImageURI(frontselectedImageUri)

                }
            }
        }
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
    private fun startCrop(uri: Uri) {
        var destinationFileName = ""
        destinationFileName += ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        //uCrop.withAspectRatio(1, 1);
//        uCrop.withAspectRatio(3, 4);
//        uCrop.withAspectRatio();
        uCrop.withAspectRatio(1f, 2f)
        //        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(450, 450)
        uCrop.withOptions(getUcropOptions()!!)
        uCrop.start(this@LPAForm)
    }
    private fun getUcropOptions(): UCrop.Options? {
        val options = UCrop.Options()
        options.setCompressionQuality(100)

        //compress type
//        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(true)
        /*
        //colors
        options.setStatusBarColor(getResources().getColor(R.color.black));
        options.setToolbarColor(getResources().getColor(R.color.black));

        options.setToolbarTitle("Crop image");*/return options
    }

    private fun postLpaResult(){
        if (!BaseUtils.isNetworkAvailable(this@LPAForm)) {
            Toast.makeText(
                this@LPAForm,
                "Please Check your internet  Connectivity",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnStId())
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnDisId())
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnTuId())
        val n_hf_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnHfId())
        val n_doc_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnDocId())
        val n_enroll_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getN_enroll_id())
        val n_smpl_col_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getN_smpl_col_id())
        val n_rpt_col_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getN_rpt_col_id())
        val d_lpa = RequestBody.create("text/plain".toMediaTypeOrNull(),lpaDate.text.toString())
    //    val c_pla_img = RequestBody.create("text/plain".toMediaTypeOrNull(),Imagee().getEncodedImage(frontselectedImageUri!!,this))
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(),lat)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(),lng)
        val n_sanc_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).n_staff_sanc)
        val n_user_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getId())
//        val n_lpa_rslt = RequestBody.create("text/plain".toMediaTypeOrNull(),
//            parentDataLpaSampleResult!![lpaResult.getSelectedItemPosition() -1].id)

        ApiClient.getClient().postLpaResult(n_st_id, n_dis_id, n_tu_id, n_hf_id, n_doc_id, n_enroll_id, n_smpl_col_id, n_rpt_col_id,
            d_lpa,
           // n_lpa_rslt, c_pla_img,
            n_lat, n_lng, n_sanc_id, n_user_id).enqueue(object: Callback<AddDocResponse>{
            override fun onResponse(
                call: Call<AddDocResponse>,
                response: Response<AddDocResponse>
            ) {
                if (response.isSuccessful){
                    if(response.body()!!.isStatus){
                        BaseUtils.showToast(this@LPAForm,"Data sent successfully")
                        startActivity(Intent(this@LPAForm,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                }
            }

            override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {

            }
        })

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
                lat = location.latitude.toString()
                lng = location.longitude.toString()
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
    private fun prevSample(){
        if (!BaseUtils.isNetworkAvailable(this@LPAForm)) {
            Toast.makeText(
                this@LPAForm,
                "Please Check your internet  Connectivity",
                Toast.LENGTH_SHORT
            ).show()
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));

            return
        }
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>" +patient.getnTuId() + "<<AND>>n_hf_id<<EQUALTO>>" + patient.getnHfId() + "<<AND>>n_doc_id<<EQUALTO>>" + patient.getnDocId() + "<<AND>>n_enroll_id<<EQUALTO>>" + intent.getStringExtra("enroll_id")+"<<AND>>n_enroll_id<<EQUALTO>>"+patient.getN_enroll_id()
        //String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        //String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        Log.d("kopopi", "getPythologyLab: " + intent.getStringExtra("tu_id"))
        Log.d("kopopi", "getPythologyLab: " + intent.getStringExtra("hf_id"))
        ApiClient.getClient().getPreviousSamples(url)
            .enqueue(object : Callback<PreviousSampleResponse> {
                override fun onResponse(
                    call: Call<PreviousSampleResponse>,
                    response: Response<PreviousSampleResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "true") {
                            //  parentDataPreviousSamples = response.body().getUser_data();
                            previoussamplerecycler.layoutManager= LinearLayoutManager(this@LPAForm)
                            previoussamplerecycler.adapter = PreviousSampleAdapter(response.body()!!.user_data, this@LPAForm,true)
                            //LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
                        }
                    }

                }

                override fun onFailure(call: Call<PreviousSampleResponse>, t: Throwable) {

                }
            })
    }
}