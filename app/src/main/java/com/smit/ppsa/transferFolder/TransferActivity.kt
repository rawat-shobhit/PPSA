package com.smit.ppsa.transferFolder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.smit.ppsa.BaseUtils
import com.smit.ppsa.R
import com.smit.ppsa.Response.FormOneData

class TransferActivity : AppCompatActivity() {

    var tuStrings: MutableList<String> = ArrayList()
    private var tu: MutableList<FormOneData> = ArrayList()
    var tuString = ""

    private lateinit var etSearchEdit:EditText
    private lateinit var search:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        etSearchEdit=findViewById(R.id.search)
        search=findViewById(R.id.searchbtn)

        search.setOnClickListener(){
            if(etSearchEdit.text.toString().length<4){
                Toast.makeText(this,"Please enter atleast 4 letter",Toast.LENGTH_SHORT).show()
            }else{
                callSearchApi(etSearchEdit.text.toString())
            }
        }

        init()
    }



    private  fun init(){
        tu = BaseUtils.getTU(this@TransferActivity)
        Log.d("mijop", "onReceive: " + tu.size)



        for (a in tu.indices) {
            if (!tuStrings.contains(tu[a].getcTuName())) {
                tuStrings.add(tu[a].getcTuName())
                Log.d("tuitems ${a}", "onReceive: " + tu[a].getcTuName())
                tuString = if (a < tu.size - 1) {
                    tuString + "n_tu_id<<EQUALTO>>" + tu[a].n_tu_id + "<<OR>>"
                } else {
                    tuString + "n_tu_id<<EQUALTO>>" + tu[a].n_tu_id
                }
            }
        }

        Log.d("TUSTRING", tuString)
    }


    private fun callSearchApi(word: String) {

    }
}