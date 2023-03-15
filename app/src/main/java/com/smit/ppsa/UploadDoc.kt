package com.smit.ppsa

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smit.ppsa.Network.ApiClient
import com.smit.ppsa.Response.AddDocResponse
import com.smit.ppsa.Response.RegisterParentData
import com.smit.ppsa.Response.RegisterParentResponse
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class UploadDoc : AppCompatActivity() {
    private var lat = ""
    private var lng = ""
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var isUploaded = false
    private lateinit var adhaar:ImageView
    private lateinit var prescription:ImageView
    private lateinit var bank_detail:ImageView
    private lateinit var test_report:ImageView
    private lateinit var backbtn: ImageView
    private lateinit var proceed: CardView
    private var adhaarUri: Uri? = null
    private var prescriptionUri: Uri? = null
    private var bank_detailUri: Uri? = null
    private var test_reportUri: Uri? = null
    private var imageType = ""
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private var adhaar_img=""
    private var presc_img=""
    private var bank_img=""
    private var test_img=""
    var SELECT_PICTURE = 200
    private val REQUEST_EXTERNAL_STORAGE = 1
    private var patient: RegisterParentData = RegisterParentData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_doc)
        init()
    }
    private fun init(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        backbtn = findViewById(R.id.backbtn)
        patient = intent.getSerializableExtra("pat_ob") as RegisterParentData
        adhaar = findViewById(R.id.adhaar)
        prescription = findViewById(R.id.prescription)
        bank_detail = findViewById(R.id.bank_detail)
        test_report = findViewById(R.id.test_report)
        proceed = findViewById(R.id.bt_proceedone)
        adhaar.setOnClickListener {
            chooseImage(this,"adhaar")
        }
        prescription.setOnClickListener {
            chooseImage(this,"presc")
        }
        bank_detail.setOnClickListener {
            chooseImage(this,"bank")
        }
        test_report.setOnClickListener {
            chooseImage(this,"test")
        }

        backbtn.setOnClickListener { super.onBackPressed() }
        proceed.setOnClickListener {
            if(adhaar_img.equals("")&&presc_img.equals("")&&bank_img.equals("")&&test_img.equals("")){
                startActivity(Intent(this,MainActivity::class.java).
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }else{
                uploadDocuments()
            }


        }
        getPrevUpload()
        getLocation()
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    fun getEncodedImage(uri: Uri, context: Context): Bitmap {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        return bitmap
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

    private fun chooseImage(context: Context,type: String) {
        imageType = type
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
        verifyStoragePermissions(this@UploadDoc)
        // create an instance of the
        // intent of the type image
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), SELECT_PICTURE)
        //   CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FormSix.this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE /*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {
                when(imageType){
                    "adhaar"->{
                        adhaarUri = data!!.data
                        adhaar.setImageURI(adhaarUri)
                        adhaar_img = Imagee().getEncodedImage(adhaarUri!!,this)
                        startCrop(adhaarUri!!)
                    }
                    "presc"->{
                        prescriptionUri = data!!.data
                        presc_img = Imagee().getEncodedImage(prescriptionUri!!,this)
                        prescription.setImageURI(prescriptionUri)
                        startCrop(prescriptionUri!!)
                    }
                    "bank"->{
                        bank_detailUri = data!!.data
                        bank_img = Imagee().getEncodedImage(bank_detailUri!!,this)
                        bank_detail.setImageURI(bank_detailUri)
                        startCrop(bank_detailUri!!)
                    }
                    "test"->{
                        test_reportUri = data!!.data
                        test_img = Imagee().getEncodedImage(test_reportUri!!,this)
                        test_report.setImageURI(test_reportUri)
                        startCrop(test_reportUri!!)
                    }
                }

            } else if (requestCode == 0 /*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                when(imageType){
                    "adhaar"->{
                        val selectedImage = data!!.extras!!["data"] as Bitmap?
                        adhaarUri = selectedImage?.let {
                            getImageUri(applicationContext,
                                it
                            )
                        }
                        adhaar_img = Imagee().getEncodedImage(adhaarUri!!,this)
                        adhaar.setImageURI(adhaarUri)
                        startCrop(adhaarUri!!)
                    }
                    "presc"->{
                        val selectedImage = data!!.extras!!["data"] as Bitmap?
                        prescriptionUri = selectedImage?.let {
                            getImageUri(applicationContext,
                                it
                            )
                        }
                        presc_img = Imagee().getEncodedImage(prescriptionUri!!,this)
                        prescription.setImageURI(prescriptionUri)
                        startCrop(prescriptionUri!!)
                    }
                    "bank"->{
                        val selectedImage = data!!.extras!!["data"] as Bitmap?
                        bank_detailUri = selectedImage?.let {
                            getImageUri(applicationContext,
                                it
                            )
                        }
                        bank_img = Imagee().getEncodedImage(bank_detailUri!!,this)
                        bank_detail.setImageURI(bank_detailUri)
                        startCrop(bank_detailUri!!)
                    }
                    "test"->{
                        val selectedImage = data!!.extras!!["data"] as Bitmap?
                        test_reportUri = selectedImage?.let {
                            getImageUri(applicationContext,
                                it
                            )
                        }
                        test_img = Imagee().getEncodedImage(test_reportUri!!,this)
                        test_report.setImageURI(test_reportUri)
                        startCrop(test_reportUri!!)
                    }
                }


            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (data != null) {
                    // get the returned data
                    when(imageType){
                        "adhaar"->{
                            val uri = UCrop.getOutput(data)

                            adhaarUri = uri
                            adhaar_img = Imagee().getEncodedImage(adhaarUri!!,this)
                            adhaar.setImageURI(adhaarUri)
                        }
                        "presc"->{
                            val uri = UCrop.getOutput(data)

                            prescriptionUri = uri
                            presc_img = Imagee().getEncodedImage(prescriptionUri!!,this)
                            prescription.setImageURI(prescriptionUri)
                        }
                        "bank"->{
                            val uri = UCrop.getOutput(data)

                            bank_detailUri = uri
                            bank_img = Imagee().getEncodedImage(bank_detailUri!!,this)
                            bank_detail.setImageURI(bank_detailUri)
                        }
                        "test"->{
                            val uri = UCrop.getOutput(data)

                            test_reportUri = uri
                            test_img = Imagee().getEncodedImage(test_reportUri!!,this)
                            test_report.setImageURI(test_reportUri)
                        }
                    }


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
        destinationFileName += ".png"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        //uCrop.withAspectRatio(1, 1);
//        uCrop.withAspectRatio(3, 4);
//        uCrop.withAspectRatio();
        uCrop.withAspectRatio(1f, 2f)
        //        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(450, 450)
        uCrop.withOptions(getUcropOptions()!!)
        uCrop.start(this@UploadDoc)
    }

    private fun getUcropOptions(): UCrop.Options? {
        val options = UCrop.Options()
        options.setCompressionQuality(70)

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

    private fun uploadDocuments(){
        if (!BaseUtils.isNetworkAvailable(this@UploadDoc)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }

        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnStId())
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnDisId())
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnTuId())
        val n_hf_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnHfId())
        val n_doc_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getnDocId())
        val n_enroll_id = RequestBody.create("text/plain".toMediaTypeOrNull(),patient.getId())
        val c_adhaar = RequestBody.create("text/plain".toMediaTypeOrNull(),adhaar_img)
        val c_presc = RequestBody.create("text/plain".toMediaTypeOrNull(),presc_img)
        val c_bank = RequestBody.create("text/plain".toMediaTypeOrNull(),bank_img)
        val c_test = RequestBody.create("text/plain".toMediaTypeOrNull(),test_img)
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(),lat)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(),lng)
        val n_sanc_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).n_staff_sanc)
        val n_user_id = RequestBody.create("text/plain".toMediaTypeOrNull(),BaseUtils.getUserInfo(this).getId())
        val map: HashMap<String, RequestBody> = HashMap()
        if (!adhaar_img.equals("")){
            map["c_aadh_img"] = c_adhaar
        }
        if (!presc_img.equals("")){
            map["c_presc_img"] = c_presc
        }
        if (!bank_img.equals("")){
            map["c_bnk_img"] = c_bank
        }
        if (!test_img.equals("")){
            map["c_tst_rpt_img"] = c_test
        }
        val url = "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=id<<EQUALTO>>"+patient.getId()
        val apiClient = when(isUploaded){
            true-> ApiClient.getClient().uploadDocumentIfResult(url,map)

            else->ApiClient.getClient().uploadDocumentIfNoResult(n_st_id,n_dis_id,n_tu_id,n_hf_id,n_doc_id,n_enroll_id
                ,map,n_lat,n_lng,n_sanc_id,n_user_id)
        }
        apiClient.enqueue(object: Callback<AddDocResponse>{
            override fun onResponse(
                call: Call<AddDocResponse>,
                response: Response<AddDocResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.isStatus){
                        BaseUtils.showToast(this@UploadDoc,"Document uploaded successfully")
                        startActivity(Intent(this@UploadDoc,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                }
            }

            override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {

            }
        })

    }

    private fun uploadPartmap(){
        val c_adhaar = RequestBody.create("text/plain".toMediaTypeOrNull(),adhaar_img)
        val c_presc = RequestBody.create("text/plain".toMediaTypeOrNull(),presc_img)
        val c_bank = RequestBody.create("text/plain".toMediaTypeOrNull(),bank_img)
        val c_test = RequestBody.create("text/plain".toMediaTypeOrNull(),test_img)
        val map: HashMap<String, RequestBody> = HashMap()
        if (!adhaar_img.equals("")){
            map["c_aadh_img"] = c_adhaar
        }
        if (!presc_img.equals("")){
            map["c_presc_img"] = c_presc
        }
        if (!bank_img.equals("")){
            map["c_bnk_img"] = c_bank
        }
        if (!test_img.equals("")){
            map["c_tst_rpt_img"] = c_test
        }

        val url = "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=id<<EQUALTO>>"+patient.getId()
        ApiClient.getClient().uploadDocumentIfResult(url,map).enqueue(object: Callback<AddDocResponse>{
            override fun onResponse(
                call: Call<AddDocResponse>,
                response: Response<AddDocResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.isStatus){
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {

            }
        })

    }
    private fun getPrevUpload() {
        if (!BaseUtils.isNetworkAvailable(this@UploadDoc)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }
        val url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_pat_docs&w=n_tu_id<<EQUALTO>>"+patient.getnTuId()+"<<AND>>n_enroll_id<<EQUALTO>>"+patient.getId()
        ApiClient.getClient().getTUPatient(url).enqueue(object: Callback<RegisterParentResponse>{
            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) { if (response.isSuccessful){
                    if (response.body()!!.status){
                        setImage(response.body()!!.userData[0].getC_aadh_img(),adhaar)
                        setImage(response.body()!!.userData[0].getC_presc_img(),prescription)
                        setImage(response.body()!!.userData[0].getC_bnk_img(),bank_detail)
                        setImage(response.body()!!.userData[0].getC_tst_rpt_img(),test_report)
                      //  adhaar_img = encodeImage(adhaar.drawable as BitmapDrawable)

                        isUploaded = true
                    }
                }
            }

            override fun onFailure(call: Call<RegisterParentResponse>, t: Throwable) {

            }
        })
    }
    private fun setImage(url: String,imageView: ImageView){
        Glide.with(this).load(url).into(imageView)
    }
}