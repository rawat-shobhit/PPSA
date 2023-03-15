package com.smit.ppsa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StockReceiving extends AppCompatActivity {

    TextView stockDate, submitBtn, selectmed;
 //   EditText medicineName, noOfUnits;
    //Spinner medicineType;
    String dateStock = "", medicineTypeStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_receiving);
      //  stockDate = findViewById(R.id.dateofdispensationtv);
    /*    medicineName = findViewById(R.id.mednameet);
        noOfUnits = findViewById(R.id.noofunitset);
        medicineType = findViewById(R.id.medtypeet);*/
     //   submitBtn = findViewById(R.id.submitbtnc);

        selectmed = findViewById(R.id.selectmed);
        selectmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectProductDialogFragment selectProductDialogFragment = new SelectProductDialogFragment();
                selectProductDialogFragment.show(getSupportFragmentManager(), "frag");

            }
        });
        //setUpCalender();

    /*    medicineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    medicineTypeStr = "";
                    Log.d("dded", "onItemSelected: " + medicineTypeStr);
                } else {
                    //setHospitalRecycler(tu.get(i - 1).getN_tu_id());
                    //   medicine = parentDataTestReportResults.get(i - 1).getId();
                    Log.d("dded", "onItemSelected: " + medicineTypeStr);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

 /*       submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });*/

    }

    private void submitData() {
        if (formValid()){
            BaseUtils.showToast(StockReceiving.this, "Clicked");

        }
    }

    private boolean formValid() {
       /* if (dateStock.equals("")){
            BaseUtils.showToast(StockReceiving.this, "date is empty");
            return false;
        }else{
            if (isNotEmpty(medicineName)){
                if (isNotEmpty(noOfUnits)){
                    if (!medicineTypeStr.equals("")){
                        Log.d("joiujjio", "formValid: not empty");
                    }else {
                        BaseUtils.showToast(StockReceiving.this, "Medicine type is empty");
                        return false;
                    }

                }else {

                    BaseUtils.showToast(StockReceiving.this, "No of units is empty");
                    return false;
                }
            }else {
                BaseUtils.showToast(StockReceiving.this, "Medicine name is empty");
                return false;
            }
        }*/
        return true;
    }

    private Boolean isNotEmpty(EditText editText) {
        Boolean state = false;
        if (!editText.getText().toString().isEmpty()) {
            state = true;
        }
        return state;
    }

    private void setUpCalender() {
        final Calendar trCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener trdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                trCalendar.set(Calendar.YEAR, year);
                trCalendar.set(Calendar.MONTH, monthOfYear);
                trCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateStock = sdf.format(trCalendar.getTime());
                stockDate.setText(sdf.format(trCalendar.getTime()));
            }
        };
/*        stockDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog m_date = new DatePickerDialog(StockReceiving.this, R.style.calender_theme, trdate, trCalendar
                        .get(Calendar.YEAR), trCalendar.get(Calendar.MONTH),
                        trCalendar.get(Calendar.DAY_OF_MONTH));


                m_date.show();
                m_date.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.BLACK);
                m_date.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.GRAY);
            }
        });*/
    }
}