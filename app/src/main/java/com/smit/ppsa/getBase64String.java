package com.smit.ppsa;

import android.util.Base64;

public class getBase64String {

    public String convertByteArray(byte [] bytes) {
       return   Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
