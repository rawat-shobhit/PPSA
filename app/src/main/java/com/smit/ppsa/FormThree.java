package com.smit.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FormThree extends AppCompatActivity implements View.OnClickListener{
    private CardView proceedbtn;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_three);
        initViews();
    }
    private void initViews() {
        proceedbtn = findViewById(R.id.bt_proceedthree);
        backBtn = findViewById(R.id.backbtn);

        setOnclick();
    }


    private void setOnclick() {
        proceedbtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_proceedthree:
                startActivity(new Intent(FormThree.this, FormFour.class));
                break;
            case R.id.backbtn:
                super.onBackPressed();
                break;
        }
    }
}