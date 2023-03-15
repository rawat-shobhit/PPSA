package com.smit.ppsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class Manager extends AppCompatActivity implements View.OnClickListener {
    private ImageView backbtn,menuBtn;
    private CardView nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        initViews(Manager.this);

    }

    private void initViews(Manager manager) {
        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.bt_proceedmanager);
        menuBtn = findViewById(R.id.menubtn);
        setOnclick();
    }


    private void setOnclick() {
        backbtn.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                super.onBackPressed();
                break;
            case R.id.bt_proceedmanager:
                startActivity(new Intent(Manager.this, WorkerForm.class));
                break;
            case R.id.menubtn:
                    PopupMenu popupMenu = new PopupMenu(Manager.this, menuBtn);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        switch (menuItem.getItemId()){
                            case R.id.add_hf:
                                startActivity(new Intent(Manager.this,AddHospitalFacilty.class));
                                break;
                            case R.id.view_hf:
                                startActivity(new Intent(Manager.this,HospitalsList.class));
                                break;
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
                break;
        }
    }
}