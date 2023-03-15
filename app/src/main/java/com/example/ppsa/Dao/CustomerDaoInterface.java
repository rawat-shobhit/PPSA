package com.example.ppsa.Dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ppsa.Response.DoctorModel;
import com.example.ppsa.Response.FormOneModel;
import com.example.ppsa.Response.HospitalList;
import com.example.ppsa.Response.HospitalModel;
import com.example.ppsa.Response.RoomDoctorsList;

import java.util.List;

@Dao
public interface CustomerDaoInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertDoctor(DoctorModel doctorModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getDoctorsFromServer(RoomDoctorsList roomDoctorsList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFormOne(FormOneModel formOneModel);

    @Query("SELECT * FROM formonemodel")
    LiveData<List<FormOneModel>> fetchFormOne();

    @Delete
    void deleteFormOne(FormOneModel formOneModel);

    @Query("SELECT * FROM roomdoctorslist where hf_id =:n_hf_id")
    LiveData<List<RoomDoctorsList>> getSelectedDoctorsFromRoom(String n_hf_id);

    @Query("SELECT * FROM doctormodel where n_hf_id = :n_hf_id")
    LiveData<List<DoctorModel>> fetchSelectedDoctor(String n_hf_id);

    @Query("SELECT * FROM doctormodel")
    LiveData<List<DoctorModel>> fetchAllDoctor();

    @Delete
    void deleteDoctor(DoctorModel doctorModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertHospital(HospitalModel hospitalModel);

    @Query("SELECT * FROM hospitalmodel")
    LiveData<List<HospitalModel>> fetchHospital();

    @Delete
    void deleteHospital(HospitalModel hospitalModel);

}
