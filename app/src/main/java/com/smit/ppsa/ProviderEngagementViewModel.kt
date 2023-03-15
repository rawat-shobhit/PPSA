package com.smit.ppsa

import androidx.lifecycle.ViewModel
import com.smit.ppsa.Response.RoomDoctorsList

class ProviderEngagementViewModel : ViewModel() {

    val list: ArrayList<RoomDoctorsList> = arrayListOf();

    fun addList(roomDoctorsList: RoomDoctorsList) {
        if (!list.contains(roomDoctorsList)) {
            list.add(roomDoctorsList)
        }
    }

    fun removeList(roomDoctorsList: RoomDoctorsList) {
        if (list.contains(roomDoctorsList)) {
            list.remove(roomDoctorsList)
        }
    }

    fun currentSelectedDoctorsList() : ArrayList<RoomDoctorsList>{
        return list
    }
}