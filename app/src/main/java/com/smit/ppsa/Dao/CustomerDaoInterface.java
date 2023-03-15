package com.smit.ppsa.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smit.ppsa.FdcReceivedModel;
import com.smit.ppsa.Response.DoctorModel;
import com.smit.ppsa.Response.FormOneModel;
import com.smit.ppsa.Response.HospitalModel;
import com.smit.ppsa.Response.PostProviderFromRoom;
/*import com.example.ppsa.Response.RoomCounsellingTypes;*/
import com.smit.ppsa.Response.RoomAddSample;
import com.smit.ppsa.Response.RoomCounsellingData;
import com.smit.ppsa.Response.RoomCounsellingFilters;
import com.smit.ppsa.Response.RoomCounsellingTypes;
import com.smit.ppsa.Response.RoomFdcDispensationHf;
import com.smit.ppsa.Response.RoomFdcDispensationPatient;
import com.smit.ppsa.Response.RoomFdcOpenStockBalance;
import com.smit.ppsa.Response.RoomFormSixData;
import com.smit.ppsa.Response.RoomLpaTestResult;
import com.smit.ppsa.Response.RoomMedicines;
import com.smit.ppsa.Response.RoomPostProvider;
import com.smit.ppsa.Response.RoomPrevVisitsData;
import com.smit.ppsa.Response.RoomPreviousSamples;
import com.smit.ppsa.Response.RoomPreviousSamplesCollection;
import com.smit.ppsa.Response.RoomPreviousSamplesHf;
import com.smit.ppsa.Response.RoomPreviousSamplesPatient;
import com.smit.ppsa.Response.RoomPreviousVisits;
import com.smit.ppsa.Response.RoomPythologyLabResult;
import com.smit.ppsa.Response.RoomReportDelivered;
import com.smit.ppsa.Response.RoomTestResult;
import com.smit.ppsa.Response.RoomDoctorsList;
import com.smit.ppsa.Response.RoomPatientList;
import com.smit.ppsa.Response.RoomSampleList;
import com.smit.ppsa.Response.RoomTestData;
import com.smit.ppsa.Response.RoomUOM;
import com.smit.ppsa.Response.RoomWeightBand;

import java.util.List;

@Dao
public interface CustomerDaoInterface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertDoctor(DoctorModel doctorModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getDoctorsFromServer(RoomDoctorsList roomDoctorsList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveNewProviderDetail(PostProviderFromRoom postProviderFromRoom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getTestFromServer(RoomTestData roomDoctorsList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPatientsFromServer(RoomPatientList roomPatientList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getTestReportResultFromServer(RoomTestResult roomTestResult);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getUOMFromServer(RoomUOM roomUOM);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getWeightBandsFromServer(RoomWeightBand roomWeightBand);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getMedicinesFromServer(RoomMedicines roomMedicines);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getLpaTestResultFromServer(RoomLpaTestResult roomLpaTestResult);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getRoomReportDeliveredFromServer(RoomReportDelivered roomReportDelivered);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getCounsellingTypeFromServer(RoomCounsellingTypes roomCounsellingTypes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getCounsellingFilterFromServer(RoomCounsellingFilters roomCounsellingFilters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPreviousVisitsFromServer(RoomPreviousVisits roomPreviousVisits);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPrevVisitsProviderFromServer(RoomPrevVisitsData roomPrevVisitsData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPreviousSamplesHfFromServer(RoomPreviousSamplesHf roomPreviousSamplesHf);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPreviousSamplesPatientFromServer(RoomPreviousSamplesPatient roomPreviousSamplesPatient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPreviousSamplesFromServer(RoomPreviousSamples roomPreviousSamples);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPreviousSamplesCollectionFromServer(RoomPreviousSamplesCollection roomPreviousSamplesCollection);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getPythologyLabsFromServer(RoomPythologyLabResult roomPythologyLabResult);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long getSampleFromServer(RoomSampleList roomSampleList);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFormOne(FormOneModel formOneModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFdcReceived(FdcReceivedModel fdcReceivedModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFormSix(RoomFormSixData roomFormSixData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFdcDispensationHf(RoomFdcDispensationHf roomFdcDispensationHf);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFdcDispensationPatient(RoomFdcDispensationPatient roomFdcDispensationPatient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFdcOpenStockBalance(RoomFdcOpenStockBalance roomFdcOpenStockBalance);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPostProvider(RoomPostProvider roomPostProvider);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertAddSample(RoomAddSample roomAddSample);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertCounsellingForm(RoomCounsellingData roomCounsellingData);

    @Query("SELECT * FROM formonemodel")
    LiveData<List<FormOneModel>> fetchFormOne();

    @Query("SELECT * FROM fdcreceivedmodel")
    LiveData<List<FdcReceivedModel>> fetchFdcReceived();

    @Query("SELECT * FROM roomAddSample")
    LiveData<List<RoomAddSample>> fetchAddSample();

    @Query("SELECT * FROM roomPostProvider")
    LiveData<List<RoomPostProvider>> fetchPostProvider();

    @Query("SELECT * FROM roomFormSixData")
    LiveData<List<RoomFormSixData>> fetchFormSix();

    @Query("SELECT * FROM roomCounsellingData")
    LiveData<List<RoomCounsellingData>> fetchCounsellingFormData();

    @Query("SELECT * FROM roomFdcDispensationHf")
    LiveData<List<RoomFdcDispensationHf>> fetchFdcDispensationHfData();

    @Query("SELECT * FROM roomFdcDispensationPatient")
    LiveData<List<RoomFdcDispensationPatient>> fetchFdcDispensationPatientData();

    @Query("SELECT * FROM roomFdcOpenStockBalance")
    LiveData<List<RoomFdcOpenStockBalance>> fetchFdcOpenStockData();

    @Query("SELECT * FROM postproviderfromroom")
    LiveData<List<PostProviderFromRoom>> fetchproviders();

    @Delete
    void deleteFormOne(FormOneModel formOneModel);

    @Query("SELECT * FROM roomdoctorslist where hf_id =:n_hf_id")
    LiveData<List<RoomDoctorsList>> getSelectedDoctorsFromRoom(String n_hf_id);

    @Query("SELECT * FROM roompatientlist where n_hf_id =:n_hf_id")
    LiveData<List<RoomPatientList>> getSelectedPatientFromRoom(String n_hf_id);

    @Query("SELECT * FROM roomTestResult")
    LiveData<List<RoomTestResult>> getSelectedTestResultFromRoom();

    @Query("SELECT * FROM roomPythologyLabResult")
    LiveData<List<RoomPythologyLabResult>> getSelectedPythologyLabFromRoom();

    @Query("SELECT * FROM roomLpaTestResult")
    LiveData<List<RoomLpaTestResult>> getSelectedLpaResultFromRoom();

    @Query("SELECT * FROM roomReportDelivered")
    LiveData<List<RoomReportDelivered>> getSelectedReportDeliveredFromRoom();

    @Query("SELECT * FROM roomUOM")
    LiveData<List<RoomUOM>> getSelectedUOMFromRoom();

    @Query("SELECT * FROM roomWeightBand")
    LiveData<List<RoomWeightBand>> getSelectedWeightBandFromRoom();

   @Query("SELECT * FROM roomMedicines")
    LiveData<List<RoomMedicines>> getSelectedMedicinesFromRoom();

    @Query("SELECT * FROM roomPreviousVisits")
    LiveData<List<RoomPreviousVisits>> getSelectedRoomPreviousVisitFromRoom();

    @Query("SELECT * FROM roomCounsellingTypes")
    LiveData<List<RoomCounsellingTypes>> getSelectedCounsellingTypeFromRoom();

    @Query("SELECT * FROM roomCounsellingFilters")
    LiveData<List<RoomCounsellingFilters>> getSelectedCounsellingFilterFromRoom();

    @Query("SELECT * FROM roomPrevVisitsData")
    LiveData<List<RoomPrevVisitsData>> getSelectedRoomPrevVisitProviderFromRoom();

    @Query("SELECT * FROM roomPreviousSamplesCollection")
    LiveData<List<RoomPreviousSamplesCollection>> getSelectedRoomPreviousCollectionFromRoom();

    @Query("SELECT * FROM roomPreviousSamplesHf")
    LiveData<List<RoomPreviousSamplesHf>> getSelectedRoomPreviousSamplesHfFromRoom();

    @Query("SELECT * FROM roomPreviousSamplesPatient")
    LiveData<List<RoomPreviousSamplesPatient>> getSelectedRoomPreviousSamplesPatientFromRoom();

    @Query("SELECT * FROM roomPreviousSamples")
    LiveData<List<RoomPreviousSamples>> getSelectedPreviousSampleFromRoom();

    @Query("DELETE FROM roomPreviousSamplesCollection")
    int deletePreviousSampleCollectionFromRoom();

    @Query("DELETE FROM roomPreviousSamples")
    int deletePreviousSampleFromRoom();


    @Query("DELETE FROM roomPreviousVisits")
    int deletePreviousVisitFromRoom();

    @Query("DELETE FROM roomPrevVisitsData")
    int deletePrevVisitProviderFromRoom();

    @Query("DELETE FROM roomPreviousSamplesHf")
    int deletePreviousSamplesHfFromRoom();

    @Query("DELETE FROM roomPreviousSamplesPatient")
    int deletePreviousSamplesPatientFromRoom();

    @Query("DELETE FROM roomMedicines")
    int deleteRoomAllMedicines();

    @Query("SELECT * FROM roomsamplelist where n_enroll_id =:n_hf_id and n_user_id =:n_user_id")
    LiveData<List<RoomSampleList>> getSelectedSampleFromRoom(String n_hf_id,String n_user_id);

    @Query("SELECT * FROM roomtestdata where n_smpl_col_id =:n_smpl_col_id and n_user_id =:n_user_id")
    LiveData<List<RoomTestData>> getSelectedTestFromRoom(String n_smpl_col_id,String n_user_id);


    @Query("SELECT * FROM doctormodel where n_hf_id = :n_hf_id")
    LiveData<List<DoctorModel>> fetchSelectedDoctor(String n_hf_id);

    @Query("SELECT * FROM doctormodel")
    LiveData<List<DoctorModel>> fetchAllDoctor();

    @Delete
    void deleteDoctor(DoctorModel doctorModel);

    @Delete
    void deletePatient(FormOneModel formOneModel);

    @Delete
    void deleteFormSix(RoomFormSixData roomFormSixData);

    @Delete
    void deleteFdcDispensationHf(RoomFdcDispensationHf roomFdcDispensationHf);

    @Delete
    void deleteFdcDispensationPatient(RoomFdcDispensationPatient roomFdcDispensationPatient);

    @Delete
    void deleteFdcOpenStockBalance(RoomFdcOpenStockBalance roomFdcOpenStockBalance);

    @Delete
    void deletePostProvider(RoomPostProvider roomPostProvider);

    @Delete
    void deleteAddSample(RoomAddSample roomAddSample);

    @Delete
    void deleteCounsellingForm(RoomCounsellingData roomCounsellingData);

    @Delete
    void deleteProvider(PostProviderFromRoom providerFromRoom);


    @Delete
    void deleteSample(RoomSampleList roomSampleList);
    @Query("DELETE FROM roomsamplelist")
    public void deleteAllSample();
    @Query("DELETE FROM roomtestdata")
    public void deleteAllTest();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertHospital(HospitalModel hospitalModel);

    @Query("SELECT * FROM hospitalmodel")
    LiveData<List<HospitalModel>> fetchHospital();

    @Delete
    void deleteHospital(HospitalModel hospitalModel);

}
