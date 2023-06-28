package com.smit.ppsa.dailyVisitOutputFolder

import com.google.gson.annotations.SerializedName

data class DailyVisitList(
    @SerializedName("prd") var prd: String? = null,
    @SerializedName("mnth") var mnth: String? = null,
    @SerializedName("yr") var yr: String? = null,
    @SerializedName("n_user_id") var nUserId: String? = null,
    @SerializedName("dv") var dv: String? = null,
    @SerializedName("pf") var pf: String? = null,
    @SerializedName("sc") var sc: String? = null,
    @SerializedName("nc") var nc: String? = null,
    @SerializedName("ne") var ne: String? = null,
    @SerializedName("be") var be: String? = null
)