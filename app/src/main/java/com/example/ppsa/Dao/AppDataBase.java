package com.example.ppsa.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ppsa.Response.DoctorModel;
import com.example.ppsa.Response.FormOneModel;
import com.example.ppsa.Response.HospitalModel;
import com.example.ppsa.Response.RoomDoctorsList;

@Database(entities = {
        DoctorModel.class,
        HospitalModel.class,
        FormOneModel.class,
        RoomDoctorsList.class

}, version = 5, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public static String DATABASE_NAME = "hospital_facility";
    private static AppDataBase INSTANCE;
    public abstract CustomerDaoInterface customerDao();

    public static AppDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDataBase.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
