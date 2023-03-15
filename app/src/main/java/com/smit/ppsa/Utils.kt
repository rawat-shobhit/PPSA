package com.smit.ppsa

import android.content.Context
import android.net.Uri
import android.widget.Spinner
import androidx.core.net.toFile
import com.smit.ppsa.Adapter.CustomSpinnerAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)


public fun setSpinnerAdapter(spinner: Spinner, values: List<String>, context: Context) {
    val spinnerAdapter = CustomSpinnerAdapter(context, values)
    spinner.adapter = spinnerAdapter
}


public fun getUrireturnFile(context: Context, uri: Uri): File {
    val file = Uri.fromFile(
        File(
            context.cacheDir,
            context.contentResolver.getType(uri)
        )
    ).toFile()
    return file
}

public fun getRequestBody(file: File): RequestBody {
   return file.asRequestBody()
}