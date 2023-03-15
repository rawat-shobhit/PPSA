package com.example.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView Providerengagement,Reportdelivery,Dcbtn,UploadDocument,SampleCollections,Conselling,usermainbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        Providerengagement = findViewById(R.id.providerangagementbtn);
        Reportdelivery = findViewById(R.id.reportdeliverybtn);
        Dcbtn = findViewById(R.id.dcbtn);
        UploadDocument = findViewById(R.id.uploaddocument);
        SampleCollections = findViewById(R.id.samplecollection);
        usermainbtn = findViewById(R.id.usermainbtn);
        Conselling = findViewById(R.id.conselling);
        setOnclick();

    }
    private void setOnclick() {
        Providerengagement.setOnClickListener(this);
        Reportdelivery.setOnClickListener(this);
        SampleCollections.setOnClickListener(this);
        usermainbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.providerangagementbtn:
                startActivity(new Intent(MainActivity.this, HospitalsList.class));
                break;
            case R.id.samplecollection:
                startActivity(new Intent(MainActivity.this, HospitalsList.class));
                break;
            case R.id.uploaddocument:
//                startActivity(new Intent(MainActivity.this, HospitalsList.class));
                break;
            case R.id.conselling:
//                startActivity(new Intent(MainActivity.this, HospitalsList.class));
                break;
            case R.id.usermainbtn:
                startActivity(new Intent(MainActivity.this,WorkerForm.class));
                break;

        }
    }
}