package com.smit.ppsa.healthFacilityFolder

import com.google.gson.annotations.SerializedName

data class HealthFacilityList(
    @SerializedName("c_hf_nam") var cHfNam: String? = null,
    @SerializedName("mnth") var mnth: String? = null,
    @SerializedName("yr") var yr: String? = null,
    @SerializedName("visit") var visit: String? = null,
    @SerializedName("nc") var nc: String? = null,
    @SerializedName("sc") var sc: String? = null,
    @SerializedName("fdc") var fdc: String? = null,
    @SerializedName("n_user_id") var nUserId: String? = null,
    @SerializedName("n_sac_id") var nSacId: String? = null
)