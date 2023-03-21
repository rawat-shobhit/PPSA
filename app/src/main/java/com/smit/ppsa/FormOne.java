package com.smit.ppsa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smit.ppsa.Adapter.CustomSpinnerAdapter;
import com.smit.ppsa.Dao.AppDataBase;
import com.smit.ppsa.Network.ApiClient;
import com.smit.ppsa.Network.NetworkCalls;
import com.smit.ppsa.Response.FormOneData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.smit.ppsa.Response.FormOneResponse;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.RoomMedicines;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormOne extends AppCompatActivity implements View.OnClickListener {
    private CardView proceedbtn;
    private ImageView backBtn;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<FormOneData> genders = new ArrayList<>();
    private List<FormOneData> state = new ArrayList<>();
    private List<FormOneData> district = new ArrayList<>();

    List<String> genderStrings = new ArrayList<>();
    List<String> stateStrings = new ArrayList<>();
    List<String> distri = new ArrayList<>();
    List<String> tuStrings = new ArrayList<>();
    private List<FormOneData> tu = new ArrayList<>();
    String lat = "0", lng = "0";
    private AppDataBase dataBase;
    private String[] st_id;
    private String st_id_res="";
    private String[] dis_id;
    private String dis_id_res="";
    private String[] tu_id;
    private String tu_id_res="", type = "normal";
    private String gender;
    private ImageView patientNotificationImg, patientBankImg;
    private String imageType = "front";
    private TextView EnrollmentDate;
    private EditText EnrollHealthFacilitySector, EnrollmentFaciltyPHI, EnrollmentFaciltyHFcode, UserIDEnrollment, EnrolmentId, PatientName, Age, Weight, Height, Address,
            Taluka, Town, Ward, Landmark, Pincode, PrimaryPhoneNumber, SecondaryPhoneNumber;
    private Spinner EnrollmentFaciltyState, EnrollmentFaciltyDistrict, EnrollmentFaciltyTBU, Gender, ResidentialState, ResidentialDistrict, ResidentialTU;
    Uri notificationImageUri = null;
    Uri bankImageUri = null;
    int SELECT_PICTURE = 200;
    int PIC_CROP = 500;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_one);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).registerReceiver(broadcastReceiver, new IntentFilter(""));
        verifyStoragePermissions(FormOne.this);
        initViews();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void initViews() {
        dataBase = AppDataBase.getDatabase(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        proceedbtn = findViewById(R.id.bt_proceedone);
        backBtn = findViewById(R.id.backbtn);
        EnrollmentDate = findViewById(R.id.f1_enrollment);
        patientNotificationImg = findViewById(R.id.addpatientfrontimg);
        patientBankImg = findViewById(R.id.addpatientBankimg);

        EnrolmentId = findViewById(R.id.f1_enrollID);
        PatientName = findViewById(R.id.f1_patientName);
        Age = findViewById(R.id.f1_age);
        Gender = findViewById(R.id.f1_gender);
        Weight = findViewById(R.id.f1_weight);
        Height = findViewById(R.id.f1_height);
        Address = findViewById(R.id.f1_address);
        Taluka = findViewById(R.id.f1_taluka);
        Town = findViewById(R.id.f1_town);
        Ward = findViewById(R.id.f1_ward);
        Landmark = findViewById(R.id.f1_landmark);
        Pincode = findViewById(R.id.f1_pincode);
        ResidentialState = findViewById(R.id.f1_residentialstate);
        ResidentialDistrict = findViewById(R.id.f1_residentialdistrict);
        ResidentialTU = findViewById(R.id.f1_residentialTu);
        PrimaryPhoneNumber = findViewById(R.id.f1_primaryphonenumber);
        SecondaryPhoneNumber = findViewById(R.id.f1_secondaryphonenumber);
        SecondaryPhoneNumber = findViewById(R.id.f1_secondaryphonenumber);
        SecondaryPhoneNumber = findViewById(R.id.f1_secondaryphonenumber);

        // handle the Choose Image button to trigger
        // the image chooser function
        patientNotificationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "front";
                Log.d("dnun", "onActivityResult:" + imageType);
                /*if (notificationImageUri != null) {
                    notificationImageUri = null;
                }*/

                chooseImage(FormOne.this);
            }
        });

        patientBankImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "back";
                Log.d("dnun", "onActivityResult:" + imageType);
                /*if (bankImageUri != null) {
                    bankImageUri = null;
                }*/

                chooseImage(FormOne.this);
            }
        });

        setUpCalender();
        NetworkCalls.getGender(this);
        NetworkCalls.getState(this);
//        NetworkCalls.getTU(this);
//        NetworkCalls.getDistrict(this);


        //spinnerSelect(st_id,state,EnrollmentFaciltyState);
        //  spinnerSelect(dis_id,district,EnrollmentFaciltyDistrict);
        //spinnerSelect(tu_id,tu,EnrollmentFaciltyTBU);
        //spinnerSelect(st_id_res, state, ResidentialState);

        ResidentialState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    st_id_res = "";
                } else {
                    st_id_res = state.get(i - 1).getId();
                    getDistrict(FormOne.this,st_id_res);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ResidentialTU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    tu_id_res = "";
                } else {
                    tu_id_res = tu.get(/*i - 1*/i).getN_tu_id();//selecting the second value in list first value is null
                    //tu_id_res = tu.get(i - 2).getN_tu_id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ResidentialDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    dis_id_res = "";
                } else {
                    dis_id_res = district.get(i - 1).getId();
                    getTU(FormOne.this,st_id_res,dis_id_res);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    gender = "";
                } else {
                    gender = genders.get(i - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setOnclick();
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
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
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

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        verifyStoragePermissions(FormOne.this);
        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(intent, "Open Gallery"), SELECT_PICTURE);
        //   CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(FormSix.this);
    }

    private void spinnerSelect(final String type[], List<FormOneData> formOneData, Spinner spinner) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                } else {
                    type[0] = formOneData.get(i - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setOnclick() {
        proceedbtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_proceedone:
                if (isValidate()) {
                    sendForm();
                }
                break;
            case R.id.backbtn:
                super.onBackPressed();
                break;
        }
    }

    private boolean emptyText(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private boolean isValidate() {
        if (EnrollmentDate.getText().toString().isEmpty()) {
            BaseUtils.showToast(this, "Enter Enrollment date");
            return false;
        } else if (emptyText(EnrolmentId)) {
            BaseUtils.showToast(this, "Enter Enrollment ID");
            return false;
        } else if (emptyText(PatientName)) {
            BaseUtils.showToast(this, "Enter patient name");
            return false;
        }else if(notificationImageUri==null){
            BaseUtils.showToast(this,"Select notification form image");
            return false;
        } else if (emptyText(Age)) {
            BaseUtils.showToast(this, "Enter age");
            return false;
        } else if (Integer.parseInt(Age.getText().toString()) < 1 || Integer.parseInt(Age.getText().toString()) > 100) {
            BaseUtils.showToast(this, "Enter age between 0 to 100");
            return false;
        } else if (TextUtils.isEmpty(gender)) {
            BaseUtils.showToast(this, "Select gender");
            return false;
        } else if (emptyText(PrimaryPhoneNumber)) {
            BaseUtils.showToast(this, "Enter primary phone number");
            return false;
        }
        return true;
    }

    /*private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("localGender")) {
                genders = BaseUtils.getGender(FormOne.this);
                //for (int a = 0;a<genders.size(),)
            }
        }
    };*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    notificationImageUri = data.getData();
                    patientNotificationImg.setImageURI(notificationImageUri);
                    startCrop(notificationImageUri);
                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);
                } else {
                    // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //backselectedImageUri = data.getData();                                                         // Get the image file URI
                    //   performCrop(backselectedImageUri);
                    //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                    //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                    //  testReportBackImg.setImageURI(backselectedImageUri);
                    bankImageUri = data.getData();
                    patientBankImg.setImageURI(bankImageUri);
                    startCrop(bankImageUri);
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
                    //CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    // frontselectedImageUri = data.getData();                                                         // Get the image file URI

                    //   frontselectedImageUri = result.getUri();                                                         // Get the image file URI
                    /*   if (frontselectedImageUri != null) {*/
                    //  Picasso.with(this).load(frontselectedImageUri).into(testReportFrontImg);
                    //    testReportFrontImg.setImageURI(frontselectedImageUri);
                 /*   } else {
                        testReportFrontImg.setImageBitmap(result.getBitmap());
                    }*/
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    notificationImageUri = getImageUri(getApplicationContext(), selectedImage);
                    patientNotificationImg.setImageURI(notificationImageUri);
                    startCrop(notificationImageUri);
                    /*performCrop(frontselectedImageUri);*/
                    //  testReportFrontImg.setImageURI(frontselectedImageUri);
                } else {
                    // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    //backselectedImageUri = data.getData();                                                         // Get the image file URI
                    //   performCrop(backselectedImageUri);
                    //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                    //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                    //  testReportBackImg.setImageURI(backselectedImageUri);
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    bankImageUri = getImageUri(getApplicationContext(), selectedImage);
                    patientBankImg.setImageURI(bankImageUri);
                    startCrop(bankImageUri);
                    //testReportBackImg.setImageURI(backselectedImageUri);
                }

                // }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                if (data != null) {
                    if (imageType.equals("front")) {
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
                        Uri uri = UCrop.getOutput(data);
                        notificationImageUri = uri;
                        patientNotificationImg.setImageURI(notificationImageUri);
                    } else {
                /*        // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        backselectedImageUri = data.getData();                                                         // Get the image file URI

                        //backselectedImageUri = result.getUri();                                                         // Get the image file URI
                        //  Picasso.with(this).load(backselectedImageUri).into(testReportBackImg);
                        testReportBackImg.setImageURI(backselectedImageUri);*/

                        //testReportBackImg.setImageURI(selectedImageUri);
                        // get the returned data
                        Uri uri = UCrop.getOutput(data);
                        bankImageUri = uri;
                        patientBankImg.setImageURI(bankImageUri);

                    }

                }
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCrop(Uri uri) {
        String destinationFileName = "";
        destinationFileName += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        //uCrop.withAspectRatio(1, 1);
//        uCrop.withAspectRatio(3, 4);
//        uCrop.withAspectRatio();
        uCrop.withAspectRatio(1, 2);
//        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getUcropOptions());
        uCrop.start(FormOne.this);
    }
    private UCrop.Options getUcropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);

        //compress type
//        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
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
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra("localGender")) {
                genders = BaseUtils.getGender(context);
                for (FormOneData gender : genders) {
                    genderStrings.add(gender.getcTyp());
                }

                setSpinnerAdapter(Gender, genderStrings);


            } else if (intent.hasExtra("notifyGender")) {
                genders = BaseUtils.getGender(FormOne.this);
                for (FormOneData gender : genders) {
                    genderStrings.add(gender.getcTyp());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setSpinnerAdapter(Gender, genderStrings);
                    }
                }, 500);
            }

            if (intent.hasExtra("localDistrict")) {
                district = BaseUtils.getDistrict(FormOne.this);
                if (distri.size()>0){
                    distri.clear();
                }

                for (FormOneData district : district) {
                    distri.add(district.getcDisNam());
                }
                //setSpinnerAdapter(EnrollmentFaciltyDistrict,distri);
                setSpinnerAdapter(ResidentialDistrict, distri);
            }
            if (intent.hasExtra("localTU")) {
                tu = BaseUtils.getTU(FormOne.this);
                if (tuStrings.size()>0){
                    tuStrings.clear();
                }
                for (int a = 0; a < tu.size(); a++) {
                    if (a > 0) {
                        if (!tuStrings.contains(tu.get(a).getcTuName())) {
                            tuStrings.add(tu.get(a).getcTuName());
                        }
                    }
                }
                /*for (FormOneData tu : tu) {
                    tuStrings.add(tu.getcTuName());
                }*/
                //setSpinnerAdapter(EnrollmentFaciltyTBU,tuStrings);
                setSpinnerAdapter(ResidentialTU, tuStrings);
            }
            if (intent.hasExtra("localState")) {
                state = BaseUtils.getState(FormOne.this);
                for (FormOneData stat : state) {
                    stateStrings.add(stat.getcStNam());
                }
                setSpinnerAdapter(ResidentialState, stateStrings);
            }
        }
    };


    private void setSpinnerAdapter(Spinner spinner, List<String> values) {

        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(FormOne.this, values);
        spinner.setAdapter(spinnerAdapter);


    }


    private void sendForm() {


        validMobile(PrimaryPhoneNumber.getText().toString());


    }

    private void validMobile(String mob) {
        //progressDialog.showProgressBar();
        if (!BaseUtils.isNetworkAvailable(FormOne.this)) {
            Toast.makeText(FormOne.this, "Please Check your internet  Connectivity", Toast.LENGTH_SHORT).show();
            //   LocalBroadcastManager.getInstance(CounsellingForm.this).sendBroadcast(new Intent().setAction("").putExtra("setRecycler", ""));
            return;
        }
        BaseUtils.putPatientName(FormOne.this, getIntent().getStringExtra("patient_name"));

        //  Log.d("dkl9", "getPatientdd: " + getIntent().getStringExtra("hf_id"));
        Log.d("dkl9", "getPatiena: " + BaseUtils.getUserInfo(FormOne.this).getnUserLevel());

        String url = "_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enrol&w=n_tu_id<<GT>>0<<AND>><<SBRK>>c_mob<<SLIKE>>"+mob+"<<ELIKE>><<OR>>c_mob_2<<SLIKE>>"+mob+"<<ELIKE>><<EBRK>>";
        ApiClient.getClient().getMedicine(url).enqueue(new Callback<MedicineResponse>() {
            @Override
            public void onResponse(Call<MedicineResponse> call, Response<MedicineResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("false")) {
                        //  parentDataTestReportResults = response.body().getUser_data();
                        String noti="";
                        String bank="";

                        if (notificationImageUri!=null){
                            noti = new Imagee().getEncodedImage(notificationImageUri,FormOne.this);
                        }
                        if (bankImageUri!=null){
                            bank = new Imagee().getEncodedImage(bankImageUri,FormOne.this);
                        }

                        NetworkCalls.sendForm(
                                FormOne.this,
                                BaseUtils.getUserOtherInfo(FormOne.this).getnStId(),
                                BaseUtils.getUserOtherInfo(FormOne.this).getnDisId(),
                                BaseUtils.getUserOtherInfo(FormOne.this).getnTuId(),
                                getIntent().getStringExtra("hf_id"),
                                getIntent().getStringExtra("doc_id"),
                                EnrollmentDate.getText().toString(),
                                EnrolmentId.getText().toString(),
                                PatientName.getText().toString(),
                                Age.getText().toString(),
                                genders.get(Gender.getSelectedItemPosition() - 1).getId(),
                                Weight.getText().toString(),
                                Height.getText().toString(),
                                Address.getText().toString(),
                                Taluka.getText().toString(),
                                Town.getText().toString(),
                                Ward.getText().toString(),
                                Landmark.getText().toString(),
                                Pincode.getText().toString(),
                                st_id_res,
                                dis_id_res,
                                tu_id_res,
                                PrimaryPhoneNumber.getText().toString(),
                                SecondaryPhoneNumber.getText().toString(),
                                lat,lng,
                                BaseUtils.getUserInfo(FormOne.this).getId(),
                                type,
                                true,
                                noti,
                                bank,
                                BaseUtils.getUserInfo(FormOne.this).getN_staff_sanc()

                        );
                    }else{
                        BaseUtils.showToast(FormOne.this,"Patient Already Registered with Programme");
                    }
                }
            }

            @Override
            public void onFailure(Call<MedicineResponse> call, Throwable t) {
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

                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(FormOne.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(FormOne.this)
                            .setMessage("Please Grant Permission first")
                            .setCancelable(false)
                            .setPositiveButton("GO TO SETTING", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData(uri);
                                    getApplicationContext().startActivity(intent);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
            }
        }
    }

    private void setUpCalender() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                EnrollmentDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        EnrollmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(FormOne.this, R.style.calender_theme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }

    public static void getDistrict(Context context,String state_id) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));

            return;
        }
        String url = "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_dist&w=n_st_id<<EQUALTO>>"+state_id;
        ApiClient.getClient().getDistrictFromState(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        BaseUtils.saveDistrict(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));

                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localDistrict", ""));
            }
        });
    }

    public static void getTU(Context context,String st_id,String dis_id) {

        if (!BaseUtils.isNetworkAvailable(context)) {
            BaseUtils.showToast(context, "Please Check your internet  Connectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

            return;
        }

        Log.d("ihsi", "getTU: W  " + BaseUtils.getUserInfo(context).getnAccessRights());
        Log.d("ihsi", "getTU: sanc  " + BaseUtils.getUserInfo(context).getN_staff_sanc());

        String url = "https://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_tu&w=n_st_id<<EQUALTO>>"+st_id+"<<AND>>n_dis_id<<EQUALTO>>"+dis_id;
        //String url = "_sptu_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_sp_tu&w=" + BaseUtils.getUserInfo(context).getnAccessRights() + "&sanc=" + BaseUtils.getUserOtherInfo(context).getN_staff_sanc();
        Log.d("ihsi", "getTU: sanc  " + url);
        List<FormOneData> TuList = new ArrayList<>();
        ApiClient.getClient().getFormTU(url).enqueue(new Callback<FormOneResponse>() {
            @Override
            public void onResponse(Call<FormOneResponse> call, Response<FormOneResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        Log.d("uhuhoy", "onResponse: " + response.body().getUserData().size());
                        BaseUtils.saveTU(context, response.body().getUserData());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));

                    } else {
                        BaseUtils.saveTU(context, TuList);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
                }
            }

            @Override
            public void onFailure(Call<FormOneResponse> call, Throwable t) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction("").putExtra("localTU", ""));
            }
        });
    }
}