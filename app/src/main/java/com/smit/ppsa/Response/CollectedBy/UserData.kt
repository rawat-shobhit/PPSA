package com.smit.ppsa.Response.CollectedBy

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("c_smpl_ext")
    val c_smpl_ext: String?,
    @SerializedName("d_cdat")
    val d_cdat: Any?,
    @SerializedName("d_mdat")
    val d_mdat: Any?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("n_act_flg")
    val n_act_flg: Any?,
    @SerializedName("n_user_id")
    val n_user_id: Any?
)