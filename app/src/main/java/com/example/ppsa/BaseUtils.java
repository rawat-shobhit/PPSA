package com.example.ppsa;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ppsa.Dao.AppDataBase;
import com.example.ppsa.Response.DoctorModel;
import com.example.ppsa.Response.DoctorsList;
import com.example.ppsa.Response.FormOneData;
import com.example.ppsa.Response.HospitalList;
import com.example.ppsa.Response.QualificationList;
import com.example.ppsa.Response.UserInfoList;
import com.example.ppsa.Response.UserList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseUtils {

    private AppDataBase dataBase;
    private static final String PREF_NAME = "sfa_pref";
    static AlertDialog number_dialog;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    // check network status (Available or not)
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Network Not Available !!!");
        }
        return false;
    }

    public static boolean getUserLogIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean("is_login", false);
    }

    public static List<UserList> getUsers(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<UserList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void putUserLogIn(Context context, boolean status) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_login", status);
        editor.apply();
    }

    public static void savedUserData(Context context, UserList mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_data", json);
        editor.apply();
    }

    public static void savedUsers(Context context, List<UserList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_list", json);
        editor.apply();
    }

    public static void saveHospitalList(Context context, List<HospitalList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_hospital_list", json);
        editor.apply();
    }
    public static List<HospitalList> getHospital(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<HospitalList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_hospital_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<HospitalList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveUserInfo(Context context, UserList userInfo) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString("user_info", json);
        editor.apply();
    }
    public static UserList getUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserList arrayItems = new UserList();
        String serializedObject = preferences.getString("user_info", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserList>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }
    public static void saveUserOtherInfo(Context context, UserInfoList userInfo) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString("user_other_info", json);
        editor.apply();
    }
    public static UserInfoList getUserOtherInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserInfoList arrayItems = new UserInfoList();
        String serializedObject = preferences.getString("user_other_info", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserInfoList>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }


    public static void saveDocList(Context context, List<DoctorsList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_doc_list", json);
        editor.apply();
    }
    public static List<DoctorsList> getDoc(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<DoctorsList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_doc_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<DoctorsList>>() {
            }.getType();

            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }

    public static void saveQualList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_qual_list", json);
        editor.apply();
    }
    public static List<QualificationList> getQual(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_qual_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveQualSpeList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_qualspe_list", json);
        editor.apply();
    }
    public static List<QualificationList> getQualSpe(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_qualspe_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void savehfTypeList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_hfType_list", json);
        editor.apply();
    }
    public static List<QualificationList> gethfType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_hfType_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveScopeList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_scope_list", json);
        editor.apply();
    }
    public static List<QualificationList> getScope(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_scope_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveBenefeciaryList(Context context, List<QualificationList> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("user_benefeciary_list", json);
        editor.apply();
    }
    public static List<QualificationList> getBenefeciary(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<QualificationList> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("user_benefeciary_list", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<QualificationList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveGender(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("gender", json);
        editor.apply();
    }
    public static List<FormOneData> getGender(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("gender", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveSpecimen(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("specimen", json);
        editor.apply();
    }
    public static List<FormOneData> getSpecimen(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("specimen", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveTesting(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("testing", json);
        editor.apply();
    }
    public static List<FormOneData> getTesting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("testing", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void saveExtraction(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("extraction", json);
        editor.apply();
    }
    public static List<FormOneData> getExtraction(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("extraction", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
    public static void savetype(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("sputumTyp", json);
        editor.apply();
    }
    public static List<FormOneData> gettype(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("sputumTyp", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveState(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("state", json);
        editor.apply();
    }
    public static List<FormOneData> getState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("state", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveDistrict(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("district", json);
        editor.apply();
    }
    public static List<FormOneData> getDistrict(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("district", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public static void saveTU(Context context, List<FormOneData> mList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mList);
        editor.putString("tu", json);
        editor.apply();
    }
    public static List<FormOneData> getTU(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<FormOneData> arrayItems = new ArrayList<>();
        String serializedObject = preferences.getString("tu", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FormOneData>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }
}
