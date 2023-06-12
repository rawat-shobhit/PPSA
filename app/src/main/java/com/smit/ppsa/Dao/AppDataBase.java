package com.smit.ppsa.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.smit.ppsa.FdcReceivedModel;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.FormOneModel;
import com.smit.ppsa.Response.HospitalModel;
import com.smit.ppsa.Response.PostProviderFromRoom;
import com.smit.ppsa.Response.RoomAddSample;
import com.smit.ppsa.Response.RoomCounsellingData;
import com.smit.ppsa.Response.RoomCounsellingFilters;
import com.smit.ppsa.Response.RoomCounsellingTypes;
import com.smit.ppsa.Response.RoomDoctorsList;
import com.smit.ppsa.Response.RoomFdcDispensationHf;
import com.smit.ppsa.Response.RoomFdcDispensationPatient;
import com.smit.ppsa.Response.RoomFdcOpenStockBalance;
import com.smit.ppsa.Response.RoomFormSixData;
import com.smit.ppsa.Response.RoomLpaTestResult;
import com.smit.ppsa.Response.RoomMedicines;
import com.smit.ppsa.Response.RoomPatientList;
import com.smit.ppsa.Response.RoomPostProvider;
import com.smit.ppsa.Response.RoomPrevVisitsData;
import com.smit.ppsa.Response.RoomPreviousSamples;
import com.smit.ppsa.Response.RoomPreviousSamplesCollection;
import com.smit.ppsa.Response.RoomPreviousSamplesHf;
import com.smit.ppsa.Response.RoomPreviousSamplesPatient;
import com.smit.ppsa.Response.RoomPreviousVisits;
import com.smit.ppsa.Response.RoomPythologyLabResult;
import com.smit.ppsa.Response.RoomReportDelivered;
import com.smit.ppsa.Response.RoomSampleList;
import com.smit.ppsa.Response.RoomTestData;
import com.smit.ppsa.Response.RoomTestResult;
import com.smit.ppsa.Response.RoomUOM;
import com.smit.ppsa.Response.RoomWeightBand;

@Database(entities = {
        DoctorModel.class,
        HospitalModel.class,
        FormOneModel.class,
        RoomDoctorsList.class,
        RoomPatientList.class,
        RoomSampleList.class,
        RoomTestData.class,
        PostProviderFromRoom.class,
        RoomTestResult.class,
        RoomPythologyLabResult.class,
        RoomLpaTestResult.class,
        RoomPreviousSamples.class,
        RoomCounsellingTypes.class,
        RoomPreviousVisits.class,
        RoomPreviousSamplesHf.class,
        RoomPreviousSamplesPatient.class,
        RoomPreviousSamplesCollection.class,
        RoomPrevVisitsData.class,
        RoomFormSixData.class,
        RoomCounsellingData.class,
        RoomFdcDispensationHf.class,
        RoomFdcDispensationPatient.class,
        RoomFdcOpenStockBalance.class,
        RoomAddSample.class,
        RoomPostProvider.class,
        RoomReportDelivered.class,
        RoomUOM.class,
        RoomMedicines.class,
        RoomCounsellingFilters.class,
        RoomWeightBand.class,
        FdcReceivedModel.class
}, version = 26, exportSchema = true)
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
