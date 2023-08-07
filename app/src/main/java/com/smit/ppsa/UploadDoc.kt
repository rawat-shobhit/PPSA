package com.smit.ppsa

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*

class UploadDoc : AppCompatActivity() {
    private var lat = ""
    private var lng = ""
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var isUploaded = true
    private lateinit var adhaar: ImageView
    private lateinit var prescription: ImageView
    private lateinit var bank_detail: ImageView
    private lateinit var test_report: ImageView
    private lateinit var hiv_report: ImageView
    private lateinit var udst_report: ImageView
    private lateinit var diabetes_report: ImageView
    private lateinit var additionalprescription: ImageView
    private lateinit var consent: ImageView
    private lateinit var notification: ImageView
    private lateinit var notificationDownload: TextView
    private lateinit var backbtn: ImageView
    private lateinit var proceed: CardView

    var manager: DownloadManager? = null

    lateinit var adharDownload: TextView
    lateinit var presDownload: TextView
    lateinit var prescriptionDownload:TextView
    lateinit var consentDownload:TextView
    lateinit var testDonload: TextView
    lateinit var hivDownload: TextView
    lateinit var udstDownload: TextView
    lateinit var diabDownload: TextView
    lateinit var bankDownload: TextView
    var CommonImageUri: Uri? = null
    private var adhaarUri: Uri? = null
    private var prescriptionUri: Uri? = null
    private var bank_detailUri: Uri? = null
    private var test_reportUri: Uri? = null
    private var udst_reportUri: Uri? = null
    private var hiv_reportUri: Uri? = null
    private var additionalPrescriptionUri: Uri? = null
    private var consentUri: Uri? = null
    private var notificationUri: Uri? = null
    private var diabetes_reportUri: Uri? = null
    private var imageType = ""
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )


    private val CAMERA_PERMISSION_CODE = 100
    private val STORAGE_PERMISSION_CODE = 101


    var cameraPermession = false
    private var adhaar_img = ""
    private var presc_img = ""
    private var bank_img = ""
    private var test_img = ""
    private var udst_img = ""
    private var hiv_img = ""
    private var diabetes_img = ""
    private var additionalPres_img = ""
    private var consent_img = ""
    private var notification_img = ""
    var SELECT_PICTURE = 200
    private val REQUEST_EXTERNAL_STORAGE = 1
    private var patient: RegisterParentData = RegisterParentData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_doc)
         manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        init()
    }

    private fun init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        backbtn = findViewById(R.id.backbtn)
        patient = intent.getSerializableExtra("pat_ob") as RegisterParentData
        adhaar = findViewById(R.id.adhaar)
        prescription = findViewById(R.id.prescription)
        bank_detail = findViewById(R.id.bank_detail)
        test_report = findViewById(R.id.test_report)
        proceed = findViewById(R.id.bt_proceedone)
        hiv_report = findViewById(R.id.hiv_report)
        udst_report = findViewById(R.id.udst_report)
        diabetes_report = findViewById(R.id.diabetes_report)
        prescriptionDownload = findViewById(R.id.prescriptionDownload)
        consentDownload = findViewById(R.id.consentDownload)
        additionalprescription = findViewById(R.id.additionalprescription)
        consent = findViewById(R.id.consent)
        notification = findViewById(R.id.notification)

        adharDownload = findViewById(R.id.adharDownload)
        bankDownload = findViewById(R.id.bankDownload)
        testDonload = findViewById(R.id.testDownload)
        hivDownload = findViewById(R.id.hivDownload)
        udstDownload = findViewById(R.id.udstDowload)
        diabDownload = findViewById(R.id.diabDownload)
        presDownload = findViewById(R.id.presDownload)
        notificationDownload = findViewById(R.id.notificationDownload)

        adhaar.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "adhaar")
        }
        prescription.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "presc")
        }
        bank_detail.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "bank")
        }
        test_report.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "test")
        }
        udst_report.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "udst")
        }
        hiv_report.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "hiv")
        }
        diabetes_report.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "diabetes")
        }
        additionalprescription.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "additionalprescription")
        }
        consent.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)
            if (cameraPermession)
            chooseImage(this, "consent")
        }
        notification.setOnClickListener {

            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            verifyStoragePermissions(this)

            if (cameraPermession)
            chooseImage(this, "notification")
        }

        backbtn.setOnClickListener { super.onBackPressed() }
        proceed.setOnClickListener {
            if (adhaar_img == "" && presc_img == "" && bank_img == ""
                && test_img == "" && hiv_img == "" && udst_img == ""
                && diabetes_img == "" && additionalPres_img == "" && consent_img ==
                "" && notification_img == ""
            ) {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } else {

//                Log.d("checkingClick",To)
//                Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show()
                if (adhaar_img == "" || presc_img == "" ||  test_img == "" || hiv_img == "" || udst_img == ""
                    || diabetes_img == "" || additionalPres_img == "" || consent_img ==
                    ""
                        ){
                    uploadDocuments()
                }


                if (notification_img != "" || bank_img != "") {
                    uploadBankNotificationDocuments()
                }
            }

        }
        getPrevUpload()
        getLocation()
    }

    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
             cameraPermession = true

            //            Toast.makeText(FormSix.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
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

    private fun chooseImage(context: Context, type: String) {
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
//                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(takePicture, 0)
                //                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);
                takeImageFromCameraUri()
            } else if (optionsMenu[i] == "Choose from Gallery") {
                // choose from  external storage
                imageChooser()
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    private fun takeImageFromCameraUri() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "MyPicture")
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )
        CommonImageUri = this.getApplication().getContentResolver().insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonImageUri)
        startActivityForResult(intent, 0)
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
                when (imageType) {
                    "adhaar" -> {
                        adhaarUri = data!!.data
                        adhaar.setImageURI(adhaarUri)
                        adhaar_img = Imagee().getEncodedImage(adhaarUri!!, this)
                        startCrop(adhaarUri!!)
                    }

                    "presc" -> {
                        prescriptionUri = data!!.data
                        presc_img = Imagee().getEncodedImage(prescriptionUri!!, this)
                        prescription.setImageURI(prescriptionUri)
                        startCrop(prescriptionUri!!)
                    }

                    "bank" -> {
                        bank_detailUri = data!!.data
                        bank_img = Imagee().getEncodedImage(bank_detailUri!!, this)
                        bank_detail.setImageURI(bank_detailUri)
                        startCrop(bank_detailUri!!)
                    }

                    "test" -> {
                        test_reportUri = data!!.data
                        test_img = Imagee().getEncodedImage(test_reportUri!!, this)
                        test_report.setImageURI(test_reportUri)
                        startCrop(test_reportUri!!)
                    }

                    "diabetes" -> {
                        diabetes_reportUri = data!!.data
                        diabetes_img = Imagee().getEncodedImage(diabetes_reportUri!!, this)
                        diabetes_report.setImageURI(diabetes_reportUri)
                        startCrop(diabetes_reportUri!!)
                    }

                    "hiv" -> {
                        hiv_reportUri = data!!.data
                        hiv_img = Imagee().getEncodedImage(hiv_reportUri!!, this)
                        hiv_report.setImageURI(hiv_reportUri)
                        startCrop(hiv_reportUri!!)
                    }

                    "udst" -> {
                        udst_reportUri = data!!.data
                        udst_img = Imagee().getEncodedImage(udst_reportUri!!, this)
                        udst_report.setImageURI(udst_reportUri)
                        startCrop(udst_reportUri!!)
                    }

                    "additionalprescription" -> {
                        additionalPrescriptionUri = data!!.data
                        additionalPres_img =
                            Imagee().getEncodedImage(additionalPrescriptionUri!!, this)
                        additionalprescription.setImageURI(additionalPrescriptionUri)
                        startCrop(additionalPrescriptionUri!!)
                    }

                    "consent" -> {
                        consentUri = data!!.data
                        consent_img = Imagee().getEncodedImage(consentUri!!, this)
                        consent.setImageURI(consentUri)
                        startCrop(consentUri!!)
                    }

                    "notification" -> {
                        notificationUri = data!!.data
                        notification_img = Imagee().getEncodedImage(notificationUri!!, this)
                        notification.setImageURI(notificationUri)
                        startCrop(notificationUri!!)


                    }
                }

            }
            else if (requestCode == 0 /*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                when (imageType) {
                    "adhaar" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        adhaarUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }
                        //                        adhaar.setImageURI(adhaarUri)
                    adhaar_img = Imagee().getEncodedImage(CommonImageUri!!, this)

                        adhaarUri=CommonImageUri;
                        adhaar.setImageURI(adhaarUri)
                        startCrop(adhaarUri!!)
                    }

                    "presc" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        prescriptionUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }
                        presc_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        prescriptionUri=CommonImageUri
                        prescription.setImageURI(prescriptionUri)
                        startCrop(prescriptionUri!!)
                    }

                    "bank" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        bank_detailUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }

                        bank_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        bank_detailUri=CommonImageUri
                        bank_detail.setImageURI(bank_detailUri)
                        startCrop(bank_detailUri!!)
                    }

                    "test" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        test_reportUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }
                        test_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        test_reportUri=CommonImageUri
                        test_report.setImageURI(test_reportUri)
                        startCrop(test_reportUri!!)
                    }

                    "diabetes" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        diabetes_reportUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }
                        diabetes_reportUri=CommonImageUri
                        diabetes_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        diabetes_report.setImageURI(diabetes_reportUri)
                        startCrop(diabetes_reportUri!!)
                    }

                    "hiv" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        hiv_reportUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }
                        hiv_reportUri=CommonImageUri
                        hiv_img = Imagee().getEncodedImage(hiv_reportUri!!, this)
                        hiv_report.setImageURI(hiv_reportUri)
                        startCrop(hiv_reportUri!!)
                    }

                    "udst" -> {

//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        udst_reportUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }

                        udst_reportUri=CommonImageUri
                        udst_img = Imagee().getEncodedImage(udst_reportUri!!, this)
                        udst_report.setImageURI(udst_reportUri)
                        startCrop(udst_reportUri!!)
                    }
                    "additionalprescription" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        additionalPrescriptionUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }

                        additionalPrescriptionUri=CommonImageUri
                        additionalPres_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        additionalprescription.setImageURI(additionalPrescriptionUri)
                        startCrop(additionalPrescriptionUri!!)
                    }
                    "consent" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        consentUri = selectedImage?.let {
//                            getImageUri(
//                                applicationContext,
//                                it
//                            )
//                        }
                        consentUri=CommonImageUri
                        consent_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        consent.setImageURI(consentUri)
                        startCrop(consentUri!!)
                    }
                    "notification" -> {
//                        val selectedImage = data!!.extras!!["data"] as Bitmap?
//                        notificationUri = selectedImage?.let {
//                            getImageUri(
//                                this@UploadDoc,
//                                it
//                            )
//                        }

                        notificationUri=CommonImageUri
                        notification_img = Imagee().getEncodedImage(CommonImageUri!!, this)
                        notification.setImageURI(notificationUri)
                        startCrop(notificationUri!!)
                    }
                }


            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (data != null) {
                    // get the returned data
                    when (imageType) {
                        "adhaar" -> {
                            val uri = UCrop.getOutput(data)

                            adhaarUri = uri
                            adhaar_img = Imagee().getEncodedImage(adhaarUri!!, this)
                            adhaar.setImageURI(adhaarUri)
                        }

                        "presc" -> {
                            val uri = UCrop.getOutput(data)

                            prescriptionUri = uri
                            presc_img = Imagee().getEncodedImage(prescriptionUri!!, this)
                            prescription.setImageURI(prescriptionUri)
                        }

                        "bank" -> {
                            val uri = UCrop.getOutput(data)

                            bank_detailUri = uri
                            bank_img = Imagee().getEncodedImage(bank_detailUri!!, this)
                            bank_detail.setImageURI(bank_detailUri)
                        }

                        "test" -> {
                            val uri = UCrop.getOutput(data)

                            test_reportUri = uri
                            test_img = Imagee().getEncodedImage(test_reportUri!!, this)
                            test_report.setImageURI(test_reportUri)
                        }

                        "hiv" -> {
                            val uri = UCrop.getOutput(data)

                            hiv_reportUri = uri
                            hiv_img = Imagee().getEncodedImage(hiv_reportUri!!, this)
                            hiv_report.setImageURI(hiv_reportUri)
                        }

                        "udst" -> {
                            val uri = UCrop.getOutput(data)

                            udst_reportUri = uri
                            udst_img = Imagee().getEncodedImage(udst_reportUri!!, this)
                            udst_report.setImageURI(udst_reportUri)
                        }

                        "diabetes" -> {
                            val uri = UCrop.getOutput(data)

                            diabetes_reportUri = uri
                            diabetes_img = Imagee().getEncodedImage(diabetes_reportUri!!, this)
                            diabetes_report.setImageURI(diabetes_reportUri)
                        }
                        "additionalprescription" -> {
                            val uri = UCrop.getOutput(data)

                            additionalPrescriptionUri = uri
                            additionalPres_img = Imagee().getEncodedImage(additionalPrescriptionUri!!, this)
                            additionalprescription.setImageURI(additionalPrescriptionUri)
                        }
                        "consent" -> {
                            val uri = UCrop.getOutput(data)

                            consentUri = uri
                            consent_img = Imagee().getEncodedImage(consentUri!!, this)
                            consent.setImageURI(consentUri)
                        }
                        "notification" -> {
                            val uri = UCrop.getOutput(data)

                            notificationUri = uri
                            notification_img = Imagee().getEncodedImage(notificationUri!!, this)
                            notification.setImageURI(notificationUri)
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
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, Calendar.getInstance().getTime().toString(), null)
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

    private fun uploadBankNotificationDocuments() {
        if (!BaseUtils.isNetworkAvailable(this@UploadDoc)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }

        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnStId())
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnDisId())
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnTuId())
        val n_hf_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnHfId())
        val n_doc_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnDocId())
        val n_enroll_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getId())
        val c_bankImage = RequestBody.create("text/plain".toMediaTypeOrNull(), bank_img)
        val c_notification = RequestBody.create("text/plain".toMediaTypeOrNull(), notification_img)

        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(), lat)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(), lng)
        val n_sanc_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            BaseUtils.getUserInfo(this).n_staff_sanc
        )
        val n_user_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            BaseUtils.getUserInfo(this).getId()
        )
        val map: HashMap<String, RequestBody> = HashMap()

        if (!bank_img.equals("")) {
            map["c_bnk_img"] = c_bankImage
        }
        if (!notification_img.equals("")) {
            map["c_not_img"] = c_notification
        }

        val url =
            "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll&w=id<<EQUALTO>>" + patient.id
//        val url =
//            "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=n_enroll_id<<EQUALTO>>" + patient.id

        Log.d("finalUrl784",url.toString());
        val apiClient = when (isUploaded) {
            true -> ApiClient.getClient().uploadDocumentIfResult(url, map)

            else -> ApiClient.getClient().uploadDocumentIfNoResult(
                n_st_id,
                n_dis_id,
                n_tu_id,
                n_hf_id,
                n_doc_id,
                n_enroll_id,
                map,
                n_lat,
                n_lng,
                n_sanc_id,
                n_user_id
            )
        }
        apiClient.enqueue(object : Callback<AddDocResponse> {
            override fun onResponse(
                call: Call<AddDocResponse>,
                response: Response<AddDocResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.isStatus) {
                        BaseUtils.showToast(this@UploadDoc, "Document uploaded successfully")
                        startActivity(
                            Intent(this@UploadDoc, MainActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {

            }
        })

    }

    private fun uploadDocuments() {
        if (!BaseUtils.isNetworkAvailable(this@UploadDoc)) {
            BaseUtils.showToast(
                this,
                "Please Check your internet  Connectivity"
            ) //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


            return
        }

        val n_st_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnStId())
        val n_dis_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnDisId())
        val n_tu_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnTuId())
        val n_hf_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnHfId())
        val n_doc_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getnDocId())
        val n_enroll_id = RequestBody.create("text/plain".toMediaTypeOrNull(), patient.getId())
        val c_adhaar = RequestBody.create("text/plain".toMediaTypeOrNull(), adhaar_img)
        val c_presc = RequestBody.create("text/plain".toMediaTypeOrNull(), presc_img)
        val c_additionalPrescription =
            RequestBody.create("text/plain".toMediaTypeOrNull(), additionalPres_img)
        val c_consent =
            RequestBody.create("text/plain".toMediaTypeOrNull(), consent_img)
        val c_test = RequestBody.create("text/plain".toMediaTypeOrNull(), test_img)
        val c_udst = RequestBody.create("text/plain".toMediaTypeOrNull(), udst_img)
        val c_hiv = RequestBody.create("text/plain".toMediaTypeOrNull(), hiv_img)
        val c_diabetes = RequestBody.create("text/plain".toMediaTypeOrNull(), diabetes_img)
        val n_lat = RequestBody.create("text/plain".toMediaTypeOrNull(), lat)
        val n_lng = RequestBody.create("text/plain".toMediaTypeOrNull(), lng)
        val n_sanc_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            BaseUtils.getUserInfo(this).n_staff_sanc
        )
        val n_user_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            BaseUtils.getUserInfo(this).getId()
        )
        val map: HashMap<String, RequestBody> = HashMap()
        if (!adhaar_img.equals("")) {
            map["c_aadh_img"] = c_adhaar
        }
        if (!presc_img.equals("")) {
            map["c_presc_img"] = c_presc
        }
        if (!additionalPres_img.equals("")) {
            map["c_add_presc_img"] = c_additionalPrescription
        }
        if (!consent_img.equals("")) {
            map["c_con_frm_img"] = c_consent
        }
        if (!test_img.equals("")) {
            map["c_tst_rpt_img"] = c_test
        }
        if (!udst_img.equals("")) {
            map["c_udst_img"] = c_udst
        }
        if (!hiv_img.equals("")) {
            map["c_hiv_img"] = c_hiv
        }
        if (!diabetes_img.equals("")) {
            map["c_diab_img"] = c_diabetes
        }

        val url =
            "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=n_enroll_id<<EQUALTO>>" + patient.id

//        val url ="_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=id<<EQUALTO>>" + patient.getId()

        Log.d("finalUrl_894",url.toString())
        Log.d("finalUrl__",isUploaded.toString())
        val apiClient = when (isUploaded) {
            true -> ApiClient.getClient().uploadDocumentIfResult(url, map)

            else -> ApiClient.getClient().uploadDocumentIfNoResult(
                n_st_id,
                n_dis_id,
                n_tu_id,
                n_hf_id,
                n_doc_id,
                n_enroll_id,
                map,
                n_lat,
                n_lng,
                n_sanc_id,
                n_user_id
            )
        }
        apiClient.enqueue(object : Callback<AddDocResponse> {
            override fun onResponse(
                call: Call<AddDocResponse>,
                response: Response<AddDocResponse>
            ) {

                Log.d("dataCheck_response",response.message().toString());
                if (response.isSuccessful) {
                    Log.d("dataCheck_response",response.body()!!.message);
                    Log.d("dataCheck_response",response.body()!!.isStatus.toString());

                    if (response.body()!!.isStatus) {


                        BaseUtils.showToast(this@UploadDoc, "Document uploaded successfully")
                        startActivity(
                            Intent(this@UploadDoc, MainActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<AddDocResponse>, t: Throwable) {
                Log.d("dataOnFalior",t.toString());
            }
        })

    }

    private fun uploadPartmap() {
        val c_adhaar = RequestBody.create("text/plain".toMediaTypeOrNull(), adhaar_img)
        val c_presc = RequestBody.create("text/plain".toMediaTypeOrNull(), presc_img)
        val c_bank = RequestBody.create("text/plain".toMediaTypeOrNull(), bank_img)
        val c_test = RequestBody.create("text/plain".toMediaTypeOrNull(), test_img)
        val map: HashMap<String, RequestBody> = HashMap()
        if (!adhaar_img.equals("")) {
            map["c_aadh_img"] = c_adhaar
        }
        if (!presc_img.equals("")) {
            map["c_presc_img"] = c_presc
        }
        if (!bank_img.equals("")) {
            map["c_bnk_img"] = c_bank
        }
        if (!test_img.equals("")) {
            map["c_tst_rpt_img"] = c_test
        }
//https://nikshayppsa.hlfppt.org/_api-v1_/
//           _data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=n_enroll_id<<EQUALTO>>35
        val url =
            "_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs&w=n_enroll_id<<EQUALTO>>" + patient.n_enroll_id
        ApiClient.getClient().uploadDocumentIfResult(url, map)
            .enqueue(object : Callback<AddDocResponse> {
                override fun onResponse(
                    call: Call<AddDocResponse>,
                    response: Response<AddDocResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isStatus) {
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
        //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_pat_docs&w=n_tu_id<<EQUALTO>>26<<AND>>n_enroll_id<<EQUALTO>>46735
        //https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_pat_docs&w=n_tu_id<<EQUALTO>>26<<AND>>n_enroll_id<<EQUALTO>>46624
        val url =
            "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_pat_docs&w=n_tu_id<<EQUALTO>>" + patient.getnTuId() + "<<AND>>n_enroll_id<<EQUALTO>>" + patient.getId()
        Log.d("Pateint LIst URL ", url)

        //http://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_pat_docs&w=n_tu_id<<EQUALTO>>28<<AND>>n_enroll_id<<EQUALTO>>43787
        ApiClient.getClient().getTUPatient(url).enqueue(object : Callback<RegisterParentResponse> {
            override fun onResponse(
                call: Call<RegisterParentResponse>,
                response: Response<RegisterParentResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status) {

                        try {

                            if (response.body()!!.userData[0].getC_aadh_img().contains(".png")) {
                                adharDownload.visibility = View.VISIBLE
                                setImage(response.body()!!.userData[0].getC_aadh_img(), adhaar)

                                adharDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_aadh_img()))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                            isUploaded = false
                        }

                        try {

                            if (response.body()!!.userData[0].c_presc_img.contains(".png")) {
                                presDownload.visibility = View.VISIBLE

                                setImage(
                                    response.body()!!.userData[0].getC_presc_img(),
                                    prescription
                                )
                                presDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_presc_img()))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                            isUploaded = false
                        }



                        try {

                            if (response.body()!!.userData[0].c_add_presc_img.contains(".png")) {
                                prescriptionDownload.visibility = View.VISIBLE
                                setImage(
                                    response.body()!!.userData[0].c_add_presc_img,
                                    additionalprescription
                                )
                                prescriptionDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].c_add_presc_img))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                            isUploaded = false
                        }



                        try {

                            if (response.body()!!.userData[0].c_con_frm_img.contains(".png")) {
                                consentDownload.visibility = View.VISIBLE
                                setImage(
                                    response.body()!!.userData[0].c_con_frm_img,
                                    consent
                                )
                                consentDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].c_con_frm_img))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                            isUploaded = false
                        }



                        try {
                            if (response.body()!!.userData[0].c_not_img.contains(".png")) {
                                notificationDownload.visibility = View.VISIBLE
                                setImage(
                                    response.body()!!.userData[0].c_not_img,
                                    notification
                                )
                                notificationDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].c_not_img))
                                }

//                                isUploaded = true
                            }
                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }



                            try {
                                Log.d("ImageNoti",response.body()!!.userData[0].notf_img);
                            }catch (e:Exception){
                                Log.d("ImageNoti",e.toString());
                            }


                            try {
                                Log.d("ImageBank",response.body()!!.userData[0].bnk_img);
                            }catch (e:Exception){
                                Log.d("ImageBank",e.toString());
                            }


                        try {
                            if (response.body()!!.userData[0].getC_bnk_img().contains(".png")) {
                                bankDownload.visibility = View.VISIBLE
                                setImage(response.body()!!.userData[0].getC_bnk_img(), bank_detail)
                                bankDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_bnk_img()))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }



                        try {

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }

                        try {
                            if (response.body()!!.userData[0].getC_tst_rpt_img().contains(".png")) {
                                testDonload.visibility = View.VISIBLE
                                setImage(
                                    response.body()!!.userData[0].getC_tst_rpt_img(),
                                    test_report
                                )


                                testDonload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_tst_rpt_img()))
                                }

//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }



                        try {
                            if (response.body()!!.userData[0].getC_hiv_img().contains(".png")) {
                                hivDownload.visibility = View.VISIBLE
                                setImage(response.body()!!.userData[0].getC_hiv_img(), hiv_report)
                                hivDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_hiv_img()))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }



                        try {
                            if (response.body()!!.userData[0].getC_udst_img().contains(".png")) {
                                udstDownload.visibility = View.VISIBLE
                                setImage(response.body()!!.userData[0].getC_udst_img(), udst_report)
                                udstDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_udst_img()))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }


                        try {

                            if (response.body()!!.userData[0].getC_diab_img().contains(".png")) {
                                diabDownload.visibility = View.VISIBLE
                                setImage(
                                    response.body()!!.userData[0].getC_diab_img(),
                                    diabetes_report
                                )
                                diabDownload.setOnClickListener() {
                                    download((response.body()!!.userData[0].getC_diab_img()))
                                }
//                                isUploaded = true
                            }

                        }catch (e:Exception){
                            Log.d("crash_image",e.toString())
                        }


                        try {
//                            isUploaded = true
                        }catch (e:Exception){
                            Log.d("isUpload",e.toString())
                        }

                        Log.d("checkData",isUploaded.toString())

                        //  adhaar_img = encodeImage(adhaar.drawable as BitmapDrawable)


                    }
                }
            }

            override fun onFailure(call: Call<RegisterParentResponse>, t: Throwable) {

            }
        })
    }


    fun downloadImage(urlString: String) {
        val destinationPath=Calendar.getInstance().toString()
        val url = URL(urlString)
        val connection = url.openConnection()
        connection.connect()
        val inputStream = BufferedInputStream(connection.getInputStream())
        val outputStream = FileOutputStream(destinationPath)

        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.close()
        inputStream.close()
    }


    private fun download(url: String) {
        manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//        val uri =
//            Uri.parse(url)
//        val request = DownloadManager.Request(uri)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        val reference = manager!!.enqueue(request)

        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val reference: Long = manager!!.enqueue(request)

    }


    private fun setImage(url: String, imageView: ImageView) {
        Glide.with(this).load(url).into(imageView)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
                FormSix.cameraPermession = true
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
//                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}