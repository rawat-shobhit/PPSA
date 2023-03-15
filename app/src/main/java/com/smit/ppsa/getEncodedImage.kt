package com.smit.ppsa

import android.content.Context
import android.net.Uri
import android.util.Log

class Imagee{
    fun getEncodedImage(uri: Uri, context: Context): String {
        val inputStreamBack = context.contentResolver.openInputStream(uri)
        val imageByteArrayBack = inputStreamBack?.readBytes()
        var encodedStringBack: String? = null
        return getBase64String().convertByteArray(imageByteArrayBack)
    }
}