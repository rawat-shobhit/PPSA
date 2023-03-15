package com.smit.ppsa.Adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.smit.ppsa.HospitalsList
import com.smit.ppsa.Response.HospitalList

class HospitalDiffUtil(
    private val oldList: List<HospitalList>,
    private val newList: List<HospitalList>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getnHfId().toInt()==newList[newItemPosition].getnHfId().toInt()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].isChecked != newList[newItemPosition].isChecked-> {
                Log.d("diff_util0987","notmatched")
                false
            }
            else->{
                Log.d("diff_util0987","matched")
                true
            }
        }
    }
}