package com.smit.ppsa

import androidx.lifecycle.ViewModel
import com.smit.ppsa.Response.RoomMedicines

class MedicinesViewModel : ViewModel() {

    val list = arrayListOf<RoomMedicines>()

    fun addList(roomMedicines: RoomMedicines){
        if (!list.contains(roomMedicines)){
            list.add(roomMedicines)
        }
    }

    fun removeList(roomMedicines: RoomMedicines){
        if (list.contains(roomMedicines)){
            list.remove(roomMedicines)
        }
    }



}