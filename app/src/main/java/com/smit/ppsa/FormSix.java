package com.smit.ppsa;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Adapter.PreviousSampleAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.RoomLpaTestResult;
import com.smit.ppsa.Response.RoomPreviousSamples;
import com.smit.ppsa.Response.RoomPythologyLabResult;
import com.smit.ppsa.Response.RoomReportDelivered;
import com.smit.ppsa.Response.RoomTestResult;
import com.smit.ppsa.Response.lpaTestResult.LpaTestResultResponse;
import com.smit.ppsa.Response.previoussamplesformsix.PreviousSampleResponse;
import com.smit.ppsa.Response.pythologylab.PythologyLabResponse;
import com.smit.ppsa.Response.testreport.TestreportResponse;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSix extends AppCompatActivity {
    private TextView testReportDate, reportCollectionDate, docId, hospitalNameTitle, regIdTT,
            submitBtn, patientName, patientAge, patentType, patentdate, doctorName;
    private Spinner testReportResult, pythologyLab, reportdeliverySpinner;
    private ImageView testReportFrontImg, testReportBackImg;
    private RecyclerView previousSamplesRecycler;
    private GlobalProgressDialog progressDialog;
    List<RoomTestResult> parentDataTestReportResults;
    private ImageView backBtn;
    FormSixViewModel mViewModel;
    private ArrayList permissionsToRequest;
    private final ArrayList permissionsRejected = new ArrayList();
    private final ArrayList permissions = new ArrayList();
    PreviousSampleAdapter previousAdapter;
    List<RoomLpaTestResult> parentDataLpaSampleResult;
    List<RoomReportDelivered> parentDataReportDelivered;
    List<RoomPythologyLabResult> parentDataPythology;
    List<RoomPreviousSamples> parentDataPreviousSamples = new ArrayList();
    private FusedLocationProviderClient mFusedLocationClient;
    static Boolean cameraPermession=false;

    static Boolean storagePermession=false;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    //   File Frontfile = new File(""), BackFile = new File("");

    // constant to compare
    // the activity result code




    int SELECT_PICTURE = 200;
    int PIC_CROP = 500;
    Uri frontselectedImageUri = null;
    Uri backselectedImageUri = null;
    Uri CommonImageUri = null;

    String frontpart_image = null;
    String frontimgPath = "ss";
    String doc_id = "";
    String backpart_image = null;
    String backimgPath = null;
    private AppDataBase dataBase;

    private String imageType = "front";
    private String dTestReport = "";
    private String ntst_rpt = "";
    private String drpt_col = "";
    private final String dlpa_smpl = "";
    private final String nlpa_rslt = "";
    private String wayLatitude = "";
    private String wayLongitude = "";
    private String nrpt_del = "";
    private int n_labid = 20998778;
    private final Boolean isTrueNatOrCbNaat = false;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //get this value from the selected patient

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_six);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        verifyStoragePermissions(FormSix.this);
        dataBase = AppDataBase.getDatabase(this);
        mViewModel = new ViewModelProvider(this).get(FormSixViewModel.class);
        testReportDate = findViewById(R.id.f1_testreport);
        testReportResult = findViewById(R.id.f1_residentialTu);
        docId = findViewById(R.id.docIdt);
        reportCollectionDate = findViewById(R.id.f1_dateofreportc);
        hospitalNameTitle = findViewById(R.id.hospitalnamett);
        regIdTT = findViewById(R.id.regid);
        pythologyLab = findViewById(R.id.f1_pythologylab);
        backBtn = findViewById(R.id.backbtn);
        testReportFrontImg = findViewById(R.id.f1_testreportfrontimg);
        patientName = findViewById(R.id.customerName);
        patientAge = findViewById(R.id.agetxt);
        patentType = findViewById(R.id.typeCl);
        patentdate = findViewById(R.id.datetxt);
        doctorName = findViewById(R.id.doctorname);
        testReportBackImg = findViewById(R.id.f1_testreportbackimg);
        //sampleForLpaDate = findViewById(R.id.f1_dateofsamplelpa);
        //  lpaTestResult = findViewById(R.id.lpatestresult);
        reportdeliverySpinner = findViewById(R.id.rportdeliv);
        submitBtn = findViewById(R.id.submitbtn);
        previousSamplesRecycler = findViewById(R.id.previoussamplecollectionsrecycler);
        progressDialog = new GlobalProgressDialog(this);
        setUpReportCollectionCalender();
        //setUpSampleForLpaCalender();
        setUpTestReportCalender();
        hospitalNameTitle.setText(getIntent().getStringExtra("hospitalName") + " / " + getIntent().getStringExtra("hospitaltypeName"));
        regIdTT.setText(getIntent().getStringExtra("niksh_id"));

        verifyStoragePermissions(FormSix.this);

        Log.d("dkl9", "getPreviousSamples: " + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());
        Log.d("dededw", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));
        Log.d("dededw", "getPreviousSamples: " + BaseUtils.getFormSixTuId(FormSix.this));
        Log.d("dededw", "getPreviousSamples: " + getIntent().getStringExtra("hf_id"));
        Log.d("dededw", "getPreviousSamples: " + BaseUtils.getFormSixHfId(FormSix.this));


        if (BaseUtils.getPatientName(FormSix.this).equals(getIntent().getStringExtra("patient_name"))) {
            getRoomTestResult();
            getRoomLpaTestResult();
            getRoomPythologyLabs();
            getRoomReportDelivered();
            getRoomPreviousSamples();
            Log.d("jioiu", "getPythologyLab: jush");
        }

        Log.d("jiouoj", "onCreate: " + BaseUtils.getSubmitLabReportStatus(FormSix.this));


        getTestReportResult(getIntent().getIntExtra("n_diag_cd", -1));
        getLpaTestResult();
        getReportDelivered();
        getPythologyLab();
        getPreviousSamples();

        BaseUtils.putFormSixTuId(FormSix.this, getIntent().getStringExtra("tu_id"));
        BaseUtils.putFormSixHfId(FormSix.this, getIntent().getStringExtra("hf_id"));

        doctorName.setText(getIntent().getStringExtra("diag_test") + String.format("(%s)", getIntent().getStringExtra("resn")));
        patientName.setText(getIntent().getStringExtra("patient_name") + " (" + getIntent().getStringExtra("patient_phone") + " )");
        patientAge.setText(getIntent().getStringExtra("patient_age"));
        patentdate.setText(getIntent().getStringExtra("d_sample"));
        patentType.setText(getIntent().getStringExtra("patient_type"));
        docId.setText(getIntent().getStringExtra("doc_id"));


        try {
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        }catch (Exception e){
            Log.d("catchException",e.toString()) ;
        }



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormSix.super.onBackPressed();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {

                try {
                    Log.d("imageUriFront",frontselectedImageUri.toString());

                }catch (Exception e)
                {

                }
                try {
                    Log.d("imageUriBack",backselectedImageUri.toString());

                }catch (Exception e)
                {

                }

                try {
                    Log.d("imageUriCommon",CommonImageUri.toString());

                }catch (Exception e)
                {

                }

                try {
                    if (!dTestReport.equals("")) {
                        if (!ntst_rpt.equals("")) {
                            if (!drpt_col.equals("")) {
                                if (frontselectedImageUri != null) {
                                    if (backselectedImageUri != null) {

                                        if (!nrpt_del.equals("")) {
                                            addLabTestReport();

                                        /*
                                                Context context,     String n_enroll_idd,       String n_user_idd,       Boolean navigate,      String date,            String confirm
                                        */





                                            if (parentDataTestReportResults.get(testReportResult.getSelectedItemPosition() - 1).getC_val().equals("MTB Detected Rif Not Detected")) {
                                                NetworkCalls.reasonForTesting(FormSix.this, getIntent().getStringExtra("enroll_id"), BaseUtils.getUserInfo(FormSix.this).getnUserLevel(), false, dTestReport, "1");

                                                Log.d("shobhit 1", getIntent().getStringExtra("enroll_id") + "->" + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());

                                            } else if (parentDataTestReportResults.get(testReportResult.getSelectedItemPosition() - 1).getC_val().equals("MTB Detected Rif Detected")) {
                                                NetworkCalls.reasonForTesting(FormSix.this, getIntent().getStringExtra("enroll_id"), BaseUtils.getUserInfo(FormSix.this).getnUserLevel(), false, dTestReport, "1");

                                                Log.d("shobhit 2", getIntent().getStringExtra("enroll_id") + "->" + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());
                                            } else if (parentDataTestReportResults.get(testReportResult.getSelectedItemPosition() - 1).getC_val().equals("Positive ( + )")) {
                                                NetworkCalls.reasonForTesting(FormSix.this, getIntent().getStringExtra("enroll_id"), BaseUtils.getUserInfo(FormSix.this).getnUserLevel(), false, dTestReport, "1");

                                                Log.d("shobhit 3", getIntent().getStringExtra("enroll_id") + "->" + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());
                                            }


                                        } else {
                                            BaseUtils.showToast(FormSix.this, "Choose report delivered");
                                        }

                                    } else {
                                        BaseUtils.showToast(FormSix.this, "Upload test report back page");
                                    }
                                } else {
                                    BaseUtils.showToast(FormSix.this, "Upload test report front page");
                                }
                            } else {
                                BaseUtils.showToast(FormSix.this, "Choose date of report collection");
                            }
                        } else {
                            BaseUtils.showToast(FormSix.this, "Choose test report result");
                        }
                    } else {
                        BaseUtils.showToast(FormSix.this, "Choose test report date");
                    }
                } catch (Exception e) {

                }

            }
        });
        testReportResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ntst_rpt = "";
                    Log.d("dded", "onItemSelected: " + ntst_rpt);
                } else {//setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    ntst_rpt = parentDataTestReportResults.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + ntst_rpt);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*lpaTestResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    nlpa_rslt = "";
                    Log.d("dded", "onItemSelected: " + ntst_rpt);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    nlpa_rslt = parentDataLpaSampleResult.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + ntst_rpt);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        pythologyLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    // i should recheck this, this passed id might not be the one needed but interchange the get Methods to one which is requires

                    n_labid = 20998778;

                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    n_labid = Integer.parseInt(parentDataPythology.get(i - 1).getPm_staff_id());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reportdeliverySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    nrpt_del = "";
                    Log.d("dded", "onItemSelected: " + ntst_rpt);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    try {
                        nrpt_del = parentDataLpaSampleResult.get(i - 1).getId().toString();
                        Log.d("dded else ", "onItemSelected: " + ntst_rpt);
                    }catch (Exception e)
                    {
                        Log.d("crash__",e.toString());
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // handle the Choose Image button to trigger
        // the image chooser function

        // shobhit front image
        testReportFrontImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "front";
                Log.d("dnun", "onActivityResult:" + imageType);

                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                if (frontselectedImageUri != null) {
                    frontselectedImageUri = null;
                }

                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                if(cameraPermession)
                { chooseImage(FormSix.this);}



            }
        });


        // front back image
        testReportBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
//                checkStoragePermission()
                verifyStoragePermissions(FormSix.this);
                imageType = "back";
                if (backselectedImageUri != null) {
                    backselectedImageUri = null;
                }
                Log.d("dnun", "onActivityResult:" + imageType);
                Log.d("camerapermession", "onActivityResult:" + cameraPermession);
                if(cameraPermession)
                {
                    chooseImage(FormSix.this);
                }





            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    wayLatitude = String.valueOf(location.getLatitude());
                    wayLongitude = String.valueOf(location.getLongitude());
//                  lat=30.977006;
//                  lng=76.537880;
                    //saveAddress();
                } else {
                    // Toast.makeText(HomeActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        verifyStoragePermissions(FormSix.this);
        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), SELECT_PICTURE);
        //   CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FormSix.this);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    private void setUpTestReportCalender() {
        final Calendar trCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener trdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                trCalendar.set(Calendar.YEAR, year);
                trCalendar.set(Calendar.MONTH, monthOfYear);
                trCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dTestReport = sdf.format(trCalendar.getTime());
                if (!testReportDate.getText().toString().isEmpty() && !reportCollectionDate.getText().toString().isEmpty()) {
                    if (trCalendar.getTime().after(BaseUtils.getDateFromString(reportCollectionDate.getText().toString()))) {
                        reportCollectionDate.setText("");
                    }
                }
                testReportDate.setText(sdf.format(trCalendar.getTime()));
            }
        };
        testReportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(FormSix.this, R.style.calender_theme, trdate, trCalendar
                        .get(Calendar.YEAR), trCalendar.get(Calendar.MONTH),
                        trCalendar.get(Calendar.DAY_OF_MONTH));

                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(patentdate.getText().toString().split("-")[0]),
                        Integer.parseInt(patentdate.getText().toString().split("-")[1]) - 1,
                        Integer.parseInt(patentdate.getText().toString().split("-")[2]));
                m_date.show();
                m_date.getDatePicker().setMinDate(calendar.getTimeInMillis());

                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }


    private void startCrop(Uri uri) {
        String destinationFileName = "";
        destinationFileName += Calendar.getInstance().getTimeInMillis()+".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
//        uCrop.withAspectRatio(1, 1);
//        uCrop.withAspectRatio(3, 4);
//        uCrop.withAspectRatio();
        uCrop.withAspectRatio(1, 2);
//        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getUcropOptions());
        uCrop.start(FormSix.this);
    }

    private UCrop.Options getUcropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);

        //compress type
//      options.setCompressionFormat(Bitmap.CompressFormat.PNG);
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
/*
        //colors
        options.setStatusBarColor(getResources().getColor(R.color.black));
        options.setToolbarColor(getResources().getColor(R.color.black));

        options.setToolbarTitle("Crop image");*/
        return options;

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            Log.d("checking_shobhit",requestCode+""+"  - "+data.toString());
        }catch ( Exception e){
            Log.d("checking_shobhit", e.toString());
        }



        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE/*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                // Get the url of the image from data
                // Uri selectedImageUri = data.getData();
                /*if (null != selectedImageUri) {*/
                // update the preview image in the layout
                Log.d("dnun", "onActivityResult:" + imageType);

                if (imageType.equals("front")) {
                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                    /*   if (frontselectedImageUri != null) {*/
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                 /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }*/

                    frontselectedImageUri = data.getData();
                    testReportFrontImg.setImageURI(frontselectedImageUri);
                    startCrop(frontselectedImageUri);

                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);
                } else {
                    // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //backselectedImageUri = data.getData();                                                         // Get the image file URI
                    //   performCrop(backselectedImageUri);
                    //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                    //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                    //  testReportBackImg.setImageURI(backselectedImageUri);


                    backselectedImageUri = data.getData();
                    testReportBackImg.setImageURI(backselectedImageUri);
                    startCrop(backselectedImageUri);

                    Log.d("shobhit front ",frontselectedImageUri.toString());

                    //testReportBackImg.setImageURI(backselectedImageUri);
                }

                // }
            } else if (requestCode == 0/*requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE*/) {

                // Get the url of the image from data
                // Uri selectedImageUri = data.getData();
                /*if (null != selectedImageUri) {*/
                // update the preview image in the layout
                Log.d("dnun", "onActivityResult:" + imageType);

                if (imageType.equals("front")) {
                    /*
                                        //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                      if (frontselectedImageUri != null) {
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                    } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());

                     */

//                    frontselectedImageUri = saveImageToStorage(selectedImage);
//                    startCrop(frontselectedImageUri);


                    /*
                    // commented now
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    frontselectedImageUri = getImageUri(getApplicationContext(), selectedImage);
                    testReportFrontImg.setImageURI(frontselectedImageUri);
                    Log.d("uploadPhoto front if  ",frontselectedImageUri.toString());
                    */

//                    File outputDir = new File(FormSix.this.getFilesDir(), ".jpg");
//                    Uri outputUri
//
//                    try {
//                        startCrop(outputUri);
//                    } catch (e: Exception) {
//                        Log.e("CROP_IMAGE", e.message!!)
//                    }


//                    File outputDir = new File(FormSix.this.getFilesDir(), ".jpg");
//                    Uri outputUri = Uri.fromFile(outputDir);

                    /*
                    @Shobhit pls check before image uploading are we reducing the quality of the image .... all uploaded images from APP are not clear
                    yes sir the image quality is reducing before we upload image . I resolved this issue but test it and make changes everywhere when we upload image so this will take take I will let you know when this work gets complete
                    I have also check the firebase
                     */
//                    Uri outputUri = FileProvider.getUriForFile(FormSix.this, "your.file.provider.authority", outputDir);

                    frontselectedImageUri= CommonImageUri;

                    try {
                   startCrop(frontselectedImageUri);
                        testReportFrontImg.setImageURI(frontselectedImageUri);
                        startCrop(frontselectedImageUri);
//                        startCrop(outputUri);
//                        cropImage.launch(listUri);
                    } catch (Exception e) {
                        Log.e("CROP_IMAGE", e.getMessage());
                    }




                } else {
                    /*
                    // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //backselectedImageUri = data.getData();                                                         // Get the image file URI
                    //   performCrop(backselectedImageUri);
                    //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                    //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                    //  testReportBackImg.setImageURI(backselectedImageUri);
                     */

//                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                    backselectedImageUri = getImageUri(getApplicationContext(), selectedImage);
//                    testReportBackImg.setImageURI(backselectedImageUri);

                    backselectedImageUri=CommonImageUri;
                    testReportBackImg.setImageURI(backselectedImageUri);
                    try {
                    startCrop(backselectedImageUri);
//                        startCrop(outputUri);
//                        cropImage.launch(listUri);
                    } catch (Exception e) {
                        Log.e("CROP_IMAGE", e.getMessage());
                    }
                    testReportFrontImg.setImageURI(frontselectedImageUri);
//                    testReportBackImg.setImageURI(backselectedImageUri);



                    Log.d("uploadPhoto back else",backselectedImageUri.toString());

                    //testReportBackImg.setImageURI(backselectedImageUri);
                }

                // }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (data != null) {
                    if (imageType.equals("front")) {


                        /*



                        //CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        //   frontselectedImageUri = data.getData();                                                         // Get the image file URI
                        //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                            if (frontselectedImageUri != null) {
                        //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                        // testReportFrontImg.setImageURI(frontselectedImageUri);
                 } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }
                    */
                        // get the returned data

                        Uri uri = UCrop.getOutput(data);
                        frontselectedImageUri = uri;
                        Log.d("Ucrop",uri.toString());
                        testReportFrontImg.setImageURI(frontselectedImageUri);

                        Log.d("upload__Front",frontselectedImageUri.toString());

                    } else {

                        /*        // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        backselectedImageUri = data.getData();                                                         // Get the image file URI

                        //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                        //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                        testReportBackImg.setImageURI(backselectedImageUri);*/

                        //testReportBackImg.setImageURI(selectedImageUri);
                        // get the returned data

                        Uri uri = UCrop.getOutput(data);
                        backselectedImageUri = uri;
                        testReportBackImg.setImageURI(backselectedImageUri);
                        testReportFrontImg.setImageURI(frontselectedImageUri);

                        Log.d("upload__Back",backselectedImageUri.toString());

                    }

                }
            }
        }
    }

    public Uri getImageUri(@NonNull Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, Calendar.getInstance().getTime().toString(), null);
        return Uri.parse(path);
    }

    private void setUpReportCollectionCalender() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                drpt_col = sdf.format(calendar.getTime());

                reportCollectionDate.setText(sdf.format(calendar.getTime()));
            }
        };
        reportCollectionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testReportDate.getText().toString().isEmpty()) {
                    BaseUtils.showToast(FormSix.this, "Please select test report date first");
                } else {
                    DatePickerDialog m_date = new DatePickerDialog(FormSix.this, R.style.calender_theme, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Integer.parseInt(testReportDate.getText().toString().split("-")[0]),
                            Integer.parseInt(testReportDate.getText().toString().split("-")[1]) - 1,
                            Integer.parseInt(testReportDate.getText().toString().split("-")[2]));

                    m_date.show();
                    m_date.getDatePicker().setMinDate(calendar.getTimeInMillis());
                    m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                    m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
                }
            }
        });
    }

    private void performCrop(Uri picUri) {
        /*  try {*/
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri
        cropIntent.setDataAndType(picUri, "image/*");
        // set crop properties here
        cropIntent.putExtra("crop", true);
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 128);
        cropIntent.putExtra("outputY", 128);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, PIC_CROP);
        // }
        // respond to users whose devices do not support the crop action
 /*       catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }

    /*private void setUpSampleForLpaCalender() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dlpa_smpl = sdf.format(calendar.getTime());
                sampleForLpaDate.setText(sdf.format(calendar.getTime()));
            }
        };
        sampleForLpaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(FormSix.this, R.style.calender_theme, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }*/
    /*    private boolean isValidate() {
            if (testReportDate.getText().toString().isEmpty()) {
                BaseUtils.showToast(this, "Enter Enrollment date");
                return false;
            }
            else  if (reportCollectionDate.getText().toString().isEmpty()) {
                BaseUtils.showToast(this, "Enter Enrollment date");
                return false;
            } else  if (sampleForLpaDate.getText().toString().isEmpty()) {
                BaseUtils.showToast(this, "Enter Enrollment date");
                return false;
            }
            else  if (testReportDate.getText().toString().isEmpty()) {
                BaseUtils.showToast(this, "Enter Enrollment date");
                return false;
            }
        }*/


    private void getTestReportResult(int n_diag_cd) {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormSix.this)) {
            BaseUtils.showToast(FormSix.this, "Please Check your internet  Connectivity");
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        BaseUtils.putPatientName(FormSix.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());

        if (isTrueNatOrCbNaat.equals(false)) {
            String url = "";
            if (n_diag_cd <= 3) {
                url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_test_result&w=id<<LT>>6";
            } else {
                url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_test_result&w=id<<GT>>5";
            }
            ApiClient.getClient().getTestReportResults(url).enqueue(new Callback<TestreportResponse>() {
                @Override
                public void onResponse(Call<TestreportResponse> call, Response<TestreportResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("true")) {
                            //  parentDataTestReportResults = response.body().getUser_data();

                            Log.d("gugwdwd", "onResponse: " + response.body().getStatus());
                            Log.d("gugwdwd", "onResponse: " + response.body().getUser_data());

                            for (int i = 0; i < response.body().getUser_data().size(); i++) {
                                RoomTestResult roomTestResult = new RoomTestResult(
                                        response.body().getUser_data().get(i).getId(),
                                        response.body().getUser_data().get(i).getC_val()
                                );
                                Log.d("kiuij", "onResponse: " + roomTestResult.getId());
                                dataBase.customerDao().getTestReportResultFromServer(roomTestResult);
                            }


                            //  }
                    /*    for (int i = 0; i < parentData.size(); i++) {
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getTestReas());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSpecmTyp());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getdSpecmCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getcPlcSampCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSmplExt());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSputmSampl());


                            RoomPatientList roomPatientList = new RoomPatientList(parentData.get(i).getdRegDat(), parentData.get(i).getnNkshId(),
                                    parentData.get(i).getcPatNam(), parentData.get(i).getnAge(), parentData.get(i).getcTyp(),
                                    parentData.get(i).getnWght(), parentData.get(i).getnHght(), parentData.get(i).getcAdd(), parentData.get(i).getcTaluka(), parentData.get(i).getcTown(), parentData.get(i).getcWard(), parentData.get(i).getcLndMrk(), parentData.get(i).getnPin(), parentData.get(i).getcDocNam(), parentData.get(i).getId(),
                                    parentData.get(i).getnStId(), parentData.get(i).getnDisId(), parentData.get(i).getnTuId(), parentData.get(i).getnHfId(), parentData.get(i).getnDocId(), parentData.get(i).getnUserId());
                            dataBase.customerDao().getPatientsFromServer(roomPatientList);
                        }*/

                            getRoomTestResult();

                        }
                    }
                    progressDialog.hideProgressBar();
                }

                @Override
                public void onFailure(Call<TestreportResponse> call, Throwable t) {
                    progressDialog.hideProgressBar();
                }
            });
        }

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        // We don't have permission so prompt the user
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else
        {
            Log.d("camera permission","else statement 1026");

        }

    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo



                        takeImageFromCameraUri();



//                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);

                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    imageChooser();
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void getLpaTestResult() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormSix.this)) {
            BaseUtils.showToast(FormSix.this, "Please Check your internet  Connectivity");
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
        if (BaseUtils.getFormSixTuId(FormSix.this).equals(getIntent().getStringExtra("tu_id")) &&
                BaseUtils.getFormSixHfId(FormSix.this).equals(getIntent().getStringExtra("hf_id"))) {
            getRoomLpaTestResult();
        }

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
//        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormSix.this).getnUserLevel().toString());

        // if (isTrueNatOrCbNaat.equals(false)) {
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_lpa_rsult&w=id<<GT>>0";
        ApiClient.getClient().getLpaTestResults(url).enqueue(new Callback<LpaTestResultResponse>() {
            @Override
            public void onResponse(Call<LpaTestResultResponse> call, Response<LpaTestResultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //   parentDataLpaSampleResult = response.body().getUser_data();
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomLpaTestResult roomLpaTestResult = new RoomLpaTestResult(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomLpaTestResult.getId());
                            dataBase.customerDao().getLpaTestResultFromServer(roomLpaTestResult);
                        }
                        Log.d("gug", "onResponse: " + response.body().getStatus());

                        getRoomLpaTestResult();

                        LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<LpaTestResultResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
        //   }

    }

    private void getReportDelivered() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormSix.this)) {
            BaseUtils.showToast(FormSix.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }
       /* if (BaseUtils.getFormSixTuId(FormSix.this).equals(getIntent().getStringExtra("tu_id")) &&
                BaseUtils.getFormSixHfId(FormSix.this).equals(getIntent().getStringExtra("hf_id"))) {
            getRoomLpaTestResult();
        }*/

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());

        // if (isTrueNatOrCbNaat.equals(false)) {
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_sc_frm&w=id<<GT>>0";
        ApiClient.getClient().getLpaTestResults(url).enqueue(new Callback<LpaTestResultResponse>() {
            @Override
            public void onResponse(Call<LpaTestResultResponse> call, Response<LpaTestResultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //   parentDataLpaSampleResult = response.body().getUser_data();
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomReportDelivered roomReportDelivered = new RoomReportDelivered(
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getC_val()
                            );
                            Log.d("kiuij", "onResponse: " + roomReportDelivered.getId());
                            dataBase.customerDao().getRoomReportDeliveredFromServer(roomReportDelivered);
                        }
                        Log.d("gug", "onResponse: " + response.body().getStatus());

                        getRoomReportDelivered();

                        LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<LpaTestResultResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });
        //   }

    }

    private void getRoomLpaTestResult() {
        Log.d("j8ijo", "getRoomTestResult: mkljui");
        LiveData<List<RoomLpaTestResult>> roomLpaResult = dataBase.customerDao().getSelectedLpaResultFromRoom();
        roomLpaResult.observe(FormSix.this, roomLpaTestResults -> {
            parentDataLpaSampleResult = roomLpaTestResults;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataLpaSampleResult.size(); i++) {
                if (!stringnames.contains(parentDataLpaSampleResult.get(i).getC_val())) {
                    stringnames.add(parentDataLpaSampleResult.get(i).getC_val());
                }
                //setSpinnerAdapter(lpaTestResult, stringnames);
            }


        });

    }

    private void takeImageFromCameraUri() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        CommonImageUri = FormSix.this.getApplication().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, CommonImageUri);
        startActivityForResult(intent, 0);
    }

    private void getRoomReportDelivered() {
        Log.d("j8ijo", "getRoomReportDelivered: mkljui");
        LiveData<List<RoomReportDelivered>> roomReportDeliver = dataBase.customerDao().getSelectedReportDeliveredFromRoom();
        roomReportDeliver.observe(FormSix.this, roomLpaTestResults -> {
            parentDataReportDelivered = roomLpaTestResults;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataReportDelivered.size(); i++) {
                if (!stringnames.contains(parentDataReportDelivered.get(i).getC_val())) {
                    stringnames.add(parentDataReportDelivered.get(i).getC_val());
                }
                setSpinnerAdapter(reportdeliverySpinner, stringnames);
            }
            reportdeliverySpinner.setSelection(2);

        });

    }

    private void getPythologyLab() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormSix.this)) {
            BaseUtils.showToast(FormSix.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormSix.this).getnUserLevel());
        Log.d("kopopddwi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        Log.d("kopopi", "getPythologyLabhftypeid:   " + getIntent().getStringExtra("hf_type_id"));

        Log.d("kopopi", "getPythologyLabTUid :   " + getIntent().getStringExtra("tu_id"));

        String tuId = "51"/*getIntent().getStringExtra("tu_id")*/;
        // _get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=n_tu_id<<EQUALTO>>0<<AND>>n_hf_typ_id<<EQUALTO>>3
        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_typ_id<<EQUALTO>>" + getIntent().getStringExtra("hf_type_id");
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        Log.d("kopopi", "getPythologyLab: " + url);
        ApiClient.getClient().getPythologyLabs(url).enqueue(new Callback<PythologyLabResponse>() {
            @Override
            public void onResponse(Call<PythologyLabResponse> call, Response<PythologyLabResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //     /*   parentDataPythology =*/ response.body().getUser_data();
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomPythologyLabResult roomPythologyLabResult = new RoomPythologyLabResult(
                                    response.body().getUser_data().get(i).getAll_code(),
                                    response.body().getUser_data().get(i).getAll_short(),
                                    response.body().getUser_data().get(i).getC_cont_per(),
                                    response.body().getUser_data().get(i).getC_cp_email(),
                                    response.body().getUser_data().get(i).getC_cp_mob(),
                                    response.body().getUser_data().get(i).getC_dis_nam(),
                                    response.body().getUser_data().get(i).getC_dis_short(),
                                    response.body().getUser_data().get(i).getC_hf_addr(),
                                    response.body().getUser_data().get(i).getC_hf_nam(),
                                    response.body().getUser_data().get(i).getC_hf_typ(),
                                    response.body().getUser_data().get(i).getC_st_nam(),
                                    response.body().getUser_data().get(i).getC_st_short(),
                                    response.body().getUser_data().get(i).getC_tu_name(),
                                    response.body().getUser_data().get(i).getN_dis_id(),
                                    response.body().getUser_data().get(i).getN_hf_cd(),
                                    response.body().getUser_data().get(i).getN_hf_id(),
                                    response.body().getUser_data().get(i).getN_hf_typ_id(),
                                    response.body().getUser_data().get(i).getN_oth_sanc_id(),
                                    response.body().getUser_data().get(i).getN_pc_sanc_id(),
                                    response.body().getUser_data().get(i).getN_pm_sanc_id(),
                                    response.body().getUser_data().get(i).getN_st_id(),
                                    response.body().getUser_data().get(i).getN_stf_sanc_id(),
                                    response.body().getUser_data().get(i).getN_tu_id(),
                                    response.body().getUser_data().get(i).getOth_lng(),
                                    response.body().getUser_data().get(i).getOth_nam(),
                                    response.body().getUser_data().get(i).getOth_staff_id(),
                                    response.body().getUser_data().get(i).getPc_code(),
                                    response.body().getUser_data().get(i).getPc_lng(),
                                    response.body().getUser_data().get(i).getPc_nam(),
                                    response.body().getUser_data().get(i).getPc_short(),
                                    response.body().getUser_data().get(i).getPc_staff_id(),
                                    response.body().getUser_data().get(i).getPm_code(),
                                    response.body().getUser_data().get(i).getPm_lng(),
                                    response.body().getUser_data().get(i).getPm_nam(),
                                    response.body().getUser_data().get(i).getPm_short(),
                                    response.body().getUser_data().get(i).getPm_staff_id(),
                                    response.body().getUser_data().get(i).getStf_code(),
                                    response.body().getUser_data().get(i).getStf_lng(),
                                    response.body().getUser_data().get(i).getStf_nam(),
                                    response.body().getUser_data().get(i).getStf_short(),
                                    response.body().getUser_data().get(i).getStf_staff_id()
                            );
                            Log.d("kiuij", "onResponse: " + roomPythologyLabResult.getC_hf_nam());
                            dataBase.customerDao().getPythologyLabsFromServer(roomPythologyLabResult);
                        }
                        Log.d("hytht", "onResponse: " + response.body().getStatus());
                        Log.d("hytht", "onResponse: " + response.body().getUser_data().size());
                        Log.d("hytht", "onResponse: " + response.body().getMessage());
                        getRoomPythologyLabs();

                        LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<PythologyLabResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomPythologyLabs() {
        Log.d("j8ijo", "getRoomTestResult: mkljui");
        LiveData<List<RoomPythologyLabResult>> roomPythologyLab = dataBase.customerDao().getSelectedPythologyLabFromRoom();
        roomPythologyLab.observe(FormSix.this, roomPythologyLabResults -> {
            parentDataPythology = roomPythologyLabResults;

            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataPythology.size(); i++) {
                if (!stringnames.contains(parentDataPythology.get(i).getC_hf_nam())) {
                    stringnames.add(parentDataPythology.get(i).getC_hf_nam());
                }
                setSpinnerAdapter(pythologyLab, stringnames);
            }


        });

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra("setRecycler")) {
       /*         final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms
                    getRoomDoctors();
                }, 1000);*/
                //   getRoomPreviousSamples();
            }
          /*  if (intent.hasExtra("setTestReportResult")){
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    //Do something after 1000ms

                }, 1000);
            }*/
        }
    };


    private void getRoomTestResult() {
        Log.d("j8ijo", "getRoomTestResult: mkljui");
        LiveData<List<RoomTestResult>> roomTest = dataBase.customerDao().getSelectedTestResultFromRoom();
        roomTest.observe(FormSix.this, roomTestResultList -> {
            parentDataTestReportResults = roomTestResultList;
            List<String> stringnames = new ArrayList<>();
            stringnames.clear();
            for (int i = 0; i < parentDataTestReportResults.size(); i++) {
                Log.d("huyui", "getRoomTestResult: " + parentDataTestReportResults.get(i).getC_val());
                if (!stringnames.contains(parentDataTestReportResults.get(i).getC_val())) {
                    stringnames.add(parentDataTestReportResults.get(i).getC_val());
                    Log.d("huyui  shobhit", "getRoomTestResult: " + parentDataTestReportResults.get(i).getC_val());
                }
                setSpinnerAdapter(testReportResult, stringnames);
            }

        });
    }

    private void setRecycler() {
        previousAdapter = new PreviousSampleAdapter(parentDataPreviousSamples, FormSix.this);
        previousSamplesRecycler.setLayoutManager(new LinearLayoutManager(this));
        previousSamplesRecycler.setAdapter(previousAdapter);
    }

    private void getPreviousSamples() {
        progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormSix.this)) {
            BaseUtils.showToast(FormSix.this, "Please Check your internet  Connectivity");            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            progressDialog.hideProgressBar();
            return;
        }


        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));


        Log.d("kopopi", "getPreviousSamples: " + getIntent().getStringExtra("tu_id"));

        String tuId = "51"/*getIntent().getStringExtra("tu_id")*/;

        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>" + getIntent().getStringExtra("tu_id") + "<<AND>>n_hf_id<<EQUALTO>>" + getIntent().getStringExtra("hf_id") + "<<AND>>n_doc_id<<EQUALTO>>" + getIntent().getStringExtra("doc_id") + "<<AND>>n_enroll_id<<EQUALTO>>" + getIntent().getStringExtra("enroll_id");
        //String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_rpt_delv&w=n_tu_id<<EQUALTO>>51<<AND>>n_hf_id<<EQUALTO>>31<<AND>>n_doc_id<<EQUALTO>>1<<AND>>n_enroll_id<<EQUALTO>>1";
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("tu_id"));
        Log.d("kopopi", "getPythologyLab: " + getIntent().getStringExtra("hf_id"));
        ApiClient.getClient().getPreviousSamples(url).enqueue(new Callback<PreviousSampleResponse>() {
            @Override
            public void onResponse(Call<PreviousSampleResponse> call, Response<PreviousSampleResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        //  parentDataPreviousSamples = response.body().getUser_data();
                        dataBase.customerDao().deletePreviousSampleFromRoom();
                        Log.d("koko", "onResponse: " + response.body().getUser_data().size());
                        for (int i = 0; i < response.body().getUser_data().size(); i++) {
                            RoomPreviousSamples roomPreviousSamples = new RoomPreviousSamples(
                                    response.body().getUser_data().get(i).getC_tr_bp_img(),
                                    response.body().getUser_data().get(i).getC_tr_fp_img(),
                                    response.body().getUser_data().get(i).getC_val(),
                                    response.body().getUser_data().get(i).getD_cdat(),
                                    response.body().getUser_data().get(i).getD_lpa_smpl(),
                                    response.body().getUser_data().get(i).getD_mdat(),
                                    response.body().getUser_data().get(i).getD_rpt_col(),
                                    response.body().getUser_data().get(i).getD_tst_rslt(),
                                    response.body().getUser_data().get(i).getId(),
                                    response.body().getUser_data().get(i).getN_dis_id(),
                                    response.body().getUser_data().get(i).getN_doc_id(),
                                    response.body().getUser_data().get(i).getN_enroll_id(),
                                    response.body().getUser_data().get(i).getN_hf_id(),
                                    response.body().getUser_data().get(i).getN_lat(),
                                    response.body().getUser_data().get(i).getN_lng(),
                                    response.body().getUser_data().get(i).getN_lpa_rslt(),
                                    response.body().getUser_data().get(i).getN_smpl_col_id(),
                                    response.body().getUser_data().get(i).getN_st_id(),
                                    response.body().getUser_data().get(i).getN_staff_info(),
                                    response.body().getUser_data().get(i).getN_tst_rpt(),
                                    response.body().getUser_data().get(i).getN_tu_id(),
                                    response.body().getUser_data().get(i).getN_user_id()
                            );
                            Log.d("kiuij", "onResponse: " + roomPreviousSamples.getId());

                            dataBase.customerDao().getPreviousSamplesFromServer(roomPreviousSamples);
                        }

                        getRoomPreviousSamples();
                        //LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<PreviousSampleResponse> call, Throwable t) {
                progressDialog.hideProgressBar();
            }
        });


    }

    private void getRoomPreviousSamples() {
        LiveData<List<RoomPreviousSamples>> roomPreviousSamples = dataBase.customerDao().getSelectedPreviousSampleFromRoom();
        roomPreviousSamples.observe(FormSix.this, previousSamples -> {
            Log.d("uiopo", "getRoomPreviousSamples: " + previousSamples.size());
            parentDataPreviousSamples = previousSamples;
           /* for (int a = 0; a < previousSamples.size(); a++) {
                RoomPreviousSamples item = previousSamples.get(a);
                if (!parentDataPreviousSamples.contains(item)) {
                    parentDataPreviousSamples.add(item);
                }
            }*/

            setRecycler();

        });
    }


    private void addLabTestReport() {
        BaseUtils.putSubmitLabReportStatus(this, "false");

        Log.d("frontImage",frontselectedImageUri.toString());
        Log.d("frontImage",backselectedImageUri.toString());
       /* if (!BaseUtils.isNetworkAvailable(FormSix.this)) {
            Toast.makeText(FormSix.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


          RoomFormSixData roomFormSixData =  new RoomFormSixData(
                    BaseUtils.getUserInfo(this).getnStCd(),
                    BaseUtils.getUserInfo(this).getnDisCd(),
                    getIntent().getStringExtra("tu_id"),
                    getIntent().getStringExtra("hf_id"),
                    getIntent().getStringExtra("doc_id"),
                    getIntent().getStringExtra("enroll_id"),
                    BaseUtils.getUserInfo(this).getnUserLevel(),
                    dTestReport,
                    ntst_rpt,
                    drpt_col,
                    new Imagee().getEncodedImage(frontselectedImageUri, FormSix.this),
                    new Imagee().getEncodedImage(backselectedImageUri, FormSix.this),
                    dlpa_smpl,
                    nlpa_rslt,
                    wayLatitude,
                    wayLongitude,
                    BaseUtils.getUserInfo(this).getnAccessRights(),
                    BaseUtils.getUserOtherInfo(this).getN_staff_sanc(),
                    String.valueOf(n_labid)
            );


            Toast.makeText(FormSix.this, "Data will be submitted when you are back online", Toast.LENGTH_SHORT).show();
            FormSix.this.startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            progressDialog.hideProgressBar();

            return;
        }
*/
        Log.d("ddedddas", "onItemSelected: " + ntst_rpt);

        progressDialog.showProgressBar();

//n_user_idd

        //    Toast.makeText(this,dTestReport.toString(),Toast.LENGTH_SHORT).show();
        mViewModel.submitLabReport(
                BaseUtils.getUserInfo(this).getnStCd(),
                BaseUtils.getUserInfo(this).getnDisCd(),
                getIntent().getStringExtra("tu_id"),
                getIntent().getStringExtra("hf_id"),
                getIntent().getStringExtra("doc_id"),
                getIntent().getStringExtra("enroll_id"),
                getIntent().getStringExtra("smpl_col_id"),
                dTestReport,
                ntst_rpt,
                drpt_col,
                frontselectedImageUri,
                backselectedImageUri,
                wayLatitude,
                wayLongitude,
                BaseUtils.getUserInfo(this).getnAccessRights(),
                BaseUtils.getUserInfo(this).getN_staff_sanc(),
                getApplicationContext(),
                n_labid,
                progressDialog,
                FormSix.this,
                false,
                null,
                null,
                true,
                nrpt_del
        );

        try {
            progressDialog.hideProgressBar();
        }catch ( Exception e){

        }



   /*     Log.d("TAGdok", "onActivityResult: " + frontselectedImageUri.getPath());

        File frontFile = new File(frontselectedImageUri.getPath());
        File backFile = new File(backselectedImageUri.getPath());

        // Create a file using the absolute path of the image
        RequestBody frontreqBody = RequestBody.create(MediaType.parse("multipart/form-file"), frontFile);
        MultipartBody.Part c_tr_fp_img = MultipartBody.Part.createFormData("c_tr_fp_img", frontFile.getName(), frontreqBody);

        // Create a file using the absolute path of the image
        RequestBody backreqBody = RequestBody.create(MediaType.parse("multipart/form-file"), backFile);
        MultipartBody.Part c_tr_bp_img = MultipartBody.Part.createFormData("c_tr_bp_img", backFile.getName(), backreqBody);

        RequestBody frontRequestFile = RequestBody.create(MediaType.parse("multipart/form-data"), frontFile);
        RequestBody backRequestFile = RequestBody.create(MediaType.parse("multipart/form-data"), backFile);

        Log.d("jduhd", "addLabTestReport: " + frontFile.getName());
        Log.d("jduhd", "addLabTestReport: " + frontFile.getPath());
        Log.d("jduhd", "addLabTestReport: " + backFile.getName());
        Log.d("jduhd", "addLabTestReport: " + backFile.getPath());

        RequestBody nTuId = RequestBody.create(getIntent().getStringExtra("tu_id"), MediaType.parse("text/plain"));
        RequestBody nHfid = RequestBody.create(getIntent().getStringExtra("hf_id"), MediaType.parse("text/plain"));
        RequestBody n_stid = RequestBody.create(BaseUtils.getUserInfo(this).getnStCd(), MediaType.parse("text/plain"));
        RequestBody n_disId = RequestBody.create(BaseUtils.getUserInfo(this).getnDisCd(), MediaType.parse("text/plain"));
        RequestBody n_docId = RequestBody.create(BaseUtils.getUserInfo(this).getId(), MediaType.parse("text/plain"));
        RequestBody neenrollID = RequestBody.create(getIntent().getStringExtra("enroll_id"), MediaType.parse("text/plain"));
        RequestBody dtestReport = RequestBody.create(dTestReport, MediaType.parse("text/plain"));
        RequestBody ntstrpt = RequestBody.create(ntst_rpt, MediaType.parse("text/plain"));
        RequestBody drptcol = RequestBody.create(drpt_col, MediaType.parse("text/plain"));
        RequestBody n_smpl_col_id = RequestBody.create(drpt_col, MediaType.parse("text/plain"));
        // MultipartBody.Part hoho = MultipartBody.Part.createFormData("c_tr_fp_img", frontFile.getName(), frontRequestFile);
        // MultipartBody.Part c_tr_bp_img = MultipartBody.Part.createFormData("c_tr_bp_img", backFile.getName(), backRequestFile);
        RequestBody d_lpa_smpl = RequestBody.create(dlpa_smpl, MediaType.parse("text/plain"));
        RequestBody n_lpa_rslt = RequestBody.create(nlpa_rslt, MediaType.parse("text/plain"));
        RequestBody n_lat = RequestBody.create(wayLatitude, MediaType.parse("text/plain"));
        RequestBody n_lng = RequestBody.create(wayLongitude, MediaType.parse("text/plain"));
        RequestBody user_id = RequestBody.create(BaseUtils.getUserInfo(this).getnUserLevel(), MediaType.parse("text/plain"));
        RequestBody staff_info = RequestBody.create(BaseUtils.getUserInfo(this).getnAccessRights(), MediaType.parse("text/plain"));

        ApiClient.getClient().addLabTestReport(n_stid, n_disId, nTuId, nHfid, n_docId, neenrollID, n_smpl_col_id, dtestReport, ntstrpt, drptcol, c_tr_fp_img, c_tr_bp_img, d_lpa_smpl, n_lpa_rslt, n_lat, n_lng, staff_info, user_id).enqueue(new Callback<AddDocResponse>() {
            @Override
            public void onResponse(Call<AddDocResponse> call, Response<AddDocResponse> response) {

                if (response.isSuccessful()) {
                    Log.d("gujoojig", "onResponse: " + response.body().isStatus());
                    Log.d("gujoojig", "onResponse: " + response.isSuccessful());
                    Toast.makeText(FormSix.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    if (response.body().isStatus()) {
                        //    parentData = response.body().getUser_data();


                        //for (int i = 0; i < parentData.size(); i++) {
                        RoomCounsellingTypes roomCounsellingTypes = new RoomCounsellingTypes(
                                response.body().isStatus(),
                                response.body().getMessage(),
                                response.body().getUser_data()
                        );
                        //       Log.d("kiuij", "onResponse: " + roomCounsellingTypes.getUser_data());
                        //   dataBase.customerDao().getCounselingTypes(roomCounsellingTypes);
                        List<String> stringnames = new ArrayList<>();
                        stringnames.clear();
                        for (int i = 0; i < parentData.size(); i++) {

                            stringnames.add(parentData.get(i).getC_val());
                            setSpinnerAdapter(counsellingType, stringnames);
                        }

                        //  }
                        for (int i = 0; i < parentData.size(); i++) {
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getTestReas());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSpecmTyp());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getdSpecmCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getcPlcSampCol());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSmplExt());
                            Log.d("kiuij", "onResponse: " + parentData.get(i).getSputmSampl());


                            RoomPatientList roomPatientList = new RoomPatientList(parentData.get(i).getdRegDat(), parentData.get(i).getnNkshId(),
                                    parentData.get(i).getcPatNam(), parentData.get(i).getnAge(), parentData.get(i).getcTyp(),
                                    parentData.get(i).getnWght(), parentData.get(i).getnHght(), parentData.get(i).getcAdd(), parentData.get(i).getcTaluka(), parentData.get(i).getcTown(), parentData.get(i).getcWard(), parentData.get(i).getcLndMrk(), parentData.get(i).getnPin(), parentData.get(i).getcDocNam(), parentData.get(i).getId(),
                                    parentData.get(i).getnStId(), parentData.get(i).getnDisId(), parentData.get(i).getnTuId(), parentData.get(i).getnHfId(), parentData.get(i).getnDocId(), parentData.get(i).getnUserId());
                            dataBase.customerDao().getPatientsFromServer(roomPatientList);
                        }

                        LocalBroadcastManager.getInstance(FormSix.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));


                    }
                } else {
                    Log.d("gujoojig", "onResponse: " + response.errorBody().toString());
                    Log.d("gujoojig", "onResponse: " + response.isSuccessful());
                }
                progressDialog.hideProgressBar();
            }

            @Override
            public void onFailure(Call<AddDocResponse> call, Throwable t) {
                Log.d("hduhd", "onFailure: " + t.getMessage().toString());
                //       progressDialog.hideProgressBar();
                progressDialog.hideProgressBar();
            }
        });
*/
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FormSix.this, values);
        spinner.setAdapter(spinnerAdapter);

    }


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(FormSix.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(FormSix.this, new String[] { permission }, requestCode);
        }
        else {
            cameraPermession=true;
            Log.d("camera permession","1712");
//            Toast.makeText(FormSix.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }




    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.




    // getRealPathFromUri

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(FormSix.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
                cameraPermession=true;
            }
            else {
                Toast.makeText(FormSix.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                storagePermession=true;
//                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FormSix.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



