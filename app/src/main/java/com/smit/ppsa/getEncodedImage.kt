package com.smit.ppsa

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Imagee{
//    fun getEncodedImage(uri: Uri, context: Context): String {
//        val inputStreamBack = context.contentResolver.openInputStream(uri)
//        val imageByteArrayBack = inputStreamBack?.readBytes()
//        var encodedStringBack: String? = null
//        return getBase64String().convertByteArray(imageByteArrayBack)
//    }

    fun getEncodedImage(uri: Uri, context: Context): String {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                // Load the image without any additional compression
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap: Bitmap? = BitmapFactory.decodeStream(input, null, options)

                // Encode the image as a Base64 string
                val outputStream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                val imageByteArray: ByteArray = outputStream.toByteArray()

                // Encode the compressed image as a Base64 string
                return getBase64String().convertByteArray(imageByteArray)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


}