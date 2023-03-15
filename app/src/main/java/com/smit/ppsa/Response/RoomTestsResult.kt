package com.smit.ppsa.Response

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity()
data class RoomTestsResult(
    @ColumnInfo(name = "id")
    private var id: String? = null,

    @ColumnInfo(name = "c_val")
    private val c_val: String? = null
)