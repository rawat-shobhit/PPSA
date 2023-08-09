package com.smit.ppsa.Network;

import android.annotation.SuppressLint;

import com.google.gson.JsonObject;
import com.smit.ppsa.BaseUtils;
import com.smit.ppsa.PatientsFollowFolder.PatientsFollowUpResponseModel;
import com.smit.ppsa.Response.AddDocResponse;
import com.smit.ppsa.Response.AllUserResponse;
import com.smit.ppsa.Response.AttendeceResponse;
import com.smit.ppsa.Response.AttendenceListResponse;
import com.smit.ppsa.Response.AttendenceTypeResponse;
import com.smit.ppsa.Response.CollectedBy.CollectedByResponse;
import com.smit.ppsa.Response.CollectedFrom.CollectedFromSamplesRes;
import com.smit.ppsa.Response.CounselingResponse;
import com.smit.ppsa.Response.DiagnosticTest.DiagnosticTestRes;
import com.smit.ppsa.Response.DoctorsResponse;
import com.smit.ppsa.Response.FilterResponse;
import com.smit.ppsa.Response.FormOneResponse;
import com.smit.ppsa.Response.GetTestResponse;
import com.smit.ppsa.Response.HospitalResponse;
import com.smit.ppsa.Response.MedicineResponse.MedicineResponse;
import com.smit.ppsa.Response.PatientResponse;
import com.smit.ppsa.Response.PrevVisitsCounselling.PreviousVisitsResponse;
import com.smit.ppsa.Response.PrevVisitsResponse;
import com.smit.ppsa.Response.PreviousSamplesCollection.PreviousSamplesCollection;
import com.smit.ppsa.Response.QualificationResponse;
import com.smit.ppsa.Response.RegisterParentResponse;
import com.smit.ppsa.Response.SampleResponse;
import com.smit.ppsa.Response.UserInfoResponse;
import com.smit.ppsa.Response.WeightResponse;
import com.smit.ppsa.Response.dispensationPatient.DiispensationPatientResponse;
import com.smit.ppsa.Response.fdcdispensationHf.DispensationHfResponse;
import com.smit.ppsa.Response.lpaTestResult.LpaTestResultResponse;
import com.smit.ppsa.Response.noOfCont.NoOfContResponse;
import com.smit.ppsa.Response.previoussamplesformsix.PreviousSampleResponse;
import com.smit.ppsa.Response.pythologylab.LabTypeResponse;
import com.smit.ppsa.Response.pythologylab.PythologyLabResponse;
import com.smit.ppsa.Response.testreport.TestreportResponse;
import com.smit.ppsa.Response.uom.UOMResponse;
import com.smit.ppsa.Response.userpassword.UserPasswordResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smit.ppsa.dailyVisitOutputFolder.DailyVisitResponseModel;
import com.smit.ppsa.healthFacilityFolder.HealthFacilityResponseModel;
import com.smit.ppsa.notificationRegisterFolder.NotificationRegisterResponse;
import com.smit.ppsa.patientNotificationDuplicacy.PatientNotificationDuplicacyResponseModel;
import com.smit.ppsa.providerStatusFolder.ProviderVistResponseModel;
import com.smit.ppsa.sampleCollectionVisitFolder.SampleCollectionVisitResponseModel;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class ApiClient {
    public static final String BASE_URL = "http://nikshayppsa.hlfppt.org/_api-v1_/";
//http://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_rpt_m_smpl_collect&w=prd<<GTEQ>>'2023-07-01'<<AND>>prd<<LTEQ>>'2023-07-23'<<AND>>n_user_id<<EQUALTO>>1380
 //      http://nikshayppsa.hlfppt.org/_api-v1_/_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf&w=n_hf_typ_id<<EQUALTO>>3<<AND>>n_govt_pvt<<EQUALTO>>2<<AND>>n_st_id<<EQUALTO>>6<<AND>>n_dis_id<<EQUALTO>>80
     public static APIInterface getClient() {
        // Create a trust manager that does not validate certificate chains
        @SuppressLint("TrustAllX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }


                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES)
                .writeTimeout(20, TimeUnit.MINUTES)
                .sslSocketFactory(Objects.requireNonNull(getSSLSocketFactory()), (X509TrustManager) trustAllCerts[0])
                .addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APIInterface.class);
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("TrustAllX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    public interface APIInterface {

        @FormUrlEncoded
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_a_user_cred&")
        Call<AllUserResponse> getAllUsers(@Field("w") String phoneNumber);

        @FormUrlEncoded
        @POST("_data_agentUPD.php?k=glgjieywgnfkg783hkd7tujavdjtykugd&u=ywgnfkg783h&p=j1v5jlyk5gf&t=_m_user_mst&cn=id&w=1")
        Call<AllUserResponse> submitPassword(@Field("w") String userID, @Field("c_password") String password);

        @FormUrlEncoded
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_user_oth_link&")
        Call<UserInfoResponse> getUsersInfo(@Field("w") String id);

        @FormUrlEncoded
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_doc")
        Call<DoctorsResponse> getDoctorsList(@Field("w") String id);


        /* @FormUrlEncoded
         @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&")
         Call<HospitalResponse> getHospitalList(@Field("w") String id);*/
        @GET()
        Call<HospitalResponse> getHospitalList(@Url String url);

        @GET()
        Call<RegisterParentResponse> getTUPatient(@Url String url);

        @GET()
        Call<HealthFacilityResponseModel> getHealthFaciliy(@Url String url);

        @GET
        Call<ProviderVistResponseModel>getProviderVisitResponse(@Url String url);

        @GET
        Call<PatientsFollowUpResponseModel>getPatientFollowUpResponse(@Url String url);

        @GET
        Call<NotificationRegisterResponse>getNotificationLedger(@Url String url);

        @GET()
        Call<DailyVisitResponseModel> getDailyVisit(@Url String url);

        @GET
        Call<SampleCollectionVisitResponseModel>getSampleCollectionVisitOuptut(@Url String url);


        @GET()
        Call<UserPasswordResponse> getUserPassword(@Url String url);

        @GET()
        Call<PatientNotificationDuplicacyResponseModel> getNotificationDuplicacy(@Url String url);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_doc_qual&w=id<<GT>>0")
        Call<QualificationResponse> getQualificationList();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_doc_spec&w=n_hf_typ_id<<EQUALTO>>1")
        Call<QualificationResponse> getQualificationSpeList();

        @GET()
        Call<QualificationResponse> getQualification(@Url String url);


        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_hf_doc")
        Call<AddDocResponse> addDoc(@Part("n_hf_id") RequestBody hfid,
                                    @Part("c_doc_nam") RequestBody docName,
                                    @Part("c_regno") RequestBody regnum,
                                    @Part("n_qual_id") RequestBody qualId,
                                    @Part("n_spec_id") RequestBody specId,
                                    @Part("c_mob") RequestBody mobNumber);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_hf_typ&w=id<<GT>>0")
        Call<QualificationResponse> getHFType();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_soe&w=id<<GT>>0")
        Call<QualificationResponse> getScope();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_hf_benf_id&w=id<<GT>>0")
        Call<QualificationResponse> getBenefeciary();

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_hf")
        Call<AddDocResponse> addHospital(@Part("n_st_id") RequestBody n_st_id,
                                         @Part("n_dis_id") RequestBody n_dis_id,
                                         @Part("n_tu_id") RequestBody n_tu_id,
                                         @Part("n_hf_cd") RequestBody n_hf_cd,
                                         @Part("c_hf_nam") RequestBody c_hf_nam,
                                         @Part("n_hf_typ_id") RequestBody n_hf_typ_id,
                                         @Part("c_hf_addr") RequestBody c_hf_addr,
                                         @Part("c_cont_per") RequestBody c_cont_per,
                                         @Part("c_cp_mob") RequestBody c_cp_mob,
                                         @Part("c_cp_email") RequestBody c_cp_email,
                                         @Part("n_sc_id") RequestBody n_sc_id,
                                         @Part("n_pp_idenr") RequestBody n_pp_idenr,
                                         @Part("c_tc_nam") RequestBody c_tc_nam,
                                         @Part("c_tc_mob") RequestBody c_tc_mob,
                                         @Part("n_bf_id") RequestBody n_bf_id,
                                         @Part("n_pay_status") RequestBody n_pay_status,
                                         @Part("n_user_id") RequestBody n_user_id,
                                         @Part("lat") RequestBody lat,
                                         @Part("lng") RequestBody lng);


        @Multipart
        @POST()
        Call<AddDocResponse> editHospital(@Part("n_st_id") RequestBody n_st_id,
                                         @Part("n_dis_id") RequestBody n_dis_id,
                                         @Part("n_tu_id") RequestBody n_tu_id,
                                         @Part("n_hf_cd") RequestBody n_hf_cd,
                                         @Part("c_hf_nam") RequestBody c_hf_nam,
                                         @Part("n_hf_typ_id") RequestBody n_hf_typ_id,
                                         @Part("c_hf_addr") RequestBody c_hf_addr,
                                         @Part("c_cont_per") RequestBody c_cont_per,
                                         @Part("c_cp_mob") RequestBody c_cp_mob,
                                         @Part("c_cp_email") RequestBody c_cp_email,
                                         @Part("n_sc_id") RequestBody n_sc_id,
                                         @Part("n_pp_idenr") RequestBody n_pp_idenr,
                                         @Part("c_tc_nam") RequestBody c_tc_nam,
                                         @Part("c_tc_mob") RequestBody c_tc_mob,
                                         @Part("n_bf_id") RequestBody n_bf_id,
                                         @Part("n_pay_status") RequestBody n_pay_status,
                                         @Part("n_user_id") RequestBody n_user_id,
                                         @Part("lat") RequestBody lat,
                                         @Part("lng") RequestBody lng,
                                          @Url String url);

        // @POST("https://nikshayppsa.hlfppt.org/_api-v1_/_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_hf&w=")
        @Multipart   //id<<EQUALTO>>142
        @POST()
        Call<AddDocResponse> editDoctorApi(@Part("c_doc_nam") RequestBody c_doc_nam,
                                           @Part("n_qual_id") RequestBody n_qual_id,
                                           @Part("n_spec_id") RequestBody n_spec_id,
                                           @Part("c_mob") RequestBody c_mob,
                                           @Part("c_regno") RequestBody c_regno,
                                           @Url String url);
//editDoctorApi(c_doc_nam, n_qual_id, n_spec_id, c_mob, c_regno, url).enqueue(new Callback<AddDocResponse>()
        // https://nikshayppsa.hlfppt.org/_api-v1_/_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_hf_doc&w=


        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_link")
        Call<AddDocResponse> hospitalSync(@Part("n_hf_id") RequestBody n_hf_id,
                                          @Part("n_pm_id") RequestBody n_pm_id,
                                          @Part("n_pc_id") RequestBody n_pc_id,
                                          @Part("n_sfta_id") RequestBody n_sfta_id,
                                          @Part("n_staff_id") RequestBody n_staff_id,
                                          @Part("n_user_id") RequestBody n_user_id,
                                          @Part("n_st_id") RequestBody n_st_id,
                                          @Part("n_dis_id") RequestBody n_dis_id,
                                          @Part("n_tu_id") RequestBody n_tu_id
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_couns")
        Call<AddDocResponse> addCounselling(@Part("n_st_id") RequestBody n_st_id,
                                            @Part("n_dis_id") RequestBody n_dis_id,
                                            @Part("n_tu_id") RequestBody n_tu_id,
                                            @Part("n_hf_id") RequestBody n_hf_id,
                                            @Part("n_doc_id") RequestBody n_doc_id,
                                            @Part("n_enroll_id") RequestBody n_enroll_id,
                                            @Part("d_coun") RequestBody d_coun,
                                            @Part("n_typ_coun") RequestBody n_typ_coun,
                                            @Part("n_lat") RequestBody n_lat,
                                            @Part("n_lng") RequestBody n_lng,
                                            @Part("n_staff_info") RequestBody n_staff_info,
                                            @Part("n_user_id") RequestBody n_user_id);

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_couns")
        Call<AddDocResponse> addLabTestReport(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_doc_id") RequestBody n_doc_id,
                @Part("n_enroll_id") RequestBody n_enroll_id,
                @Part("n_smpl_col_id") RequestBody d_coun,
                @Part("d_tst_rslt") RequestBody n_typ_coun,
                @Part("n_tst_rpt") RequestBody n_tst_rpt,
                @Part("d_rpt_col") RequestBody d_rpt_col,
                @Part MultipartBody.Part c_tr_fp_img,
                @Part MultipartBody.Part c_tr_bp_img,
                @Part("d_lpa_smpl") RequestBody d_lpa_smpl,
                @Part("n_lpa_rslt") RequestBody n_lpa_rslt,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_staff_info") RequestBody n_staff_info,
                @Part("n_user_id") RequestBody n_user_id
                                        /* @Part("n_staff_info") RequestBody n_staff_info,
                                         @Part("n_user_id") RequestBody n_user_id*/);


        @GET()
        Call<AttendeceResponse> getAttendence(@Url String url);

        @GET()
        Call<PreviousSampleResponse> getPreviousSamples(@Url String url);

        @GET()
        Call<PreviousSamplesCollection> getPreviousSamplesCollection(@Url String url);

        @GET()
        Call<PreviousVisitsResponse> getPreviousSamplesCounselling(@Url String url);

        @GET()
        Call<DispensationHfResponse> getPreviousSamplesfdcHf(@Url String url);

        @GET()
        Call<DiispensationPatientResponse> getPreviousSamplesfdcPatient(@Url String url);


        @GET()
        Call<CounselingResponse> getCounselSamples(@Url String url);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_attend_code&w=id<<GT>>0")
        Call<AttendenceTypeResponse> getAttendenceType();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_gender&w=id<<GT>>0")
        Call<FormOneResponse> getFormGender();

        @Multipart
        @POST("https://nikshayppsa.hlfppt.org/_api-v1_/_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_attend")
        Call<AddDocResponse> postAttendance(@Part("n_attend_typ") RequestBody n_attend_typ,
                                            @Part("d_rpt") RequestBody d_rpt,
                                            @Part("n_user_id") RequestBody n_user_id,
                                            @Part("n_sanc_id") RequestBody sanc_id,
                                            @Part("n_lat") RequestBody lat,
                                            @Part("n_lng") RequestBody lng);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_state&w=id<<GT>>0")
        Call<FormOneResponse> getFormState();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_st_dis&w=n_st_id<<EQUALTO>>3")
        Call<FormOneResponse> getFormDistrict();

        @GET()
        Call<FormOneResponse> getDistrictFromState(@Url String url);

        @GET()
        Call<FormOneResponse> getFormTU(@Url String url);

        //original url
        /*_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll*/
        @Multipart
        @POST("_enroll_INagent.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll")
        Call<AddDocResponse> postFormOne(@Part("n_st_id") RequestBody n_st_id,
                                         @Part("n_dis_id") RequestBody n_dis_id,
                                         @Part("n_tu_id") RequestBody n_tu_id,
                                         @Part("n_hf_id") RequestBody n_hf_id,
                                         @Part("n_doc_id") RequestBody n_doc_id,
                                         @Part("d_reg_dat") RequestBody d_reg_dat,
                                         @Part("n_nksh_id") RequestBody n_nksh_id,
                                         @Part("c_pat_nam") RequestBody c_pat_nam,
                                         @Part("n_age") RequestBody n_age,
                                         @Part("n_sex") RequestBody n_sex,
                                         @Part("n_wght") RequestBody n_wght,
                                         @Part("n_hght") RequestBody n_hght,
                                         @Part("c_add") RequestBody c_add,
                                         @Part("c_taluka") RequestBody c_taluka,
                                         @Part("c_town") RequestBody c_town,
                                         @Part("c_ward") RequestBody c_ward,
                                         @Part("c_lnd_mrk") RequestBody c_lnd_mrk,
                                         @Part("n_pin") RequestBody n_pin,
                                         @Part("n_st_id_res") RequestBody n_st_id_res,
                                         @Part("n_dis_id_res") RequestBody n_dis_id_res,
                                         @Part("n_tu_id_res") RequestBody n_tu_id_res,
                                         @Part("c_mob") RequestBody c_mob,
                                         @Part("c_mob_2") RequestBody c_mob_2,
                                         @Part("n_lat") RequestBody n_lat,
                                         @Part("n_lng") RequestBody n_lng,
                                         @Part("c_bnk_img") RequestBody c_bnk_img,
                                         @Part("c_not_img") RequestBody c_not_img,
                                         @Part("n_sac_id") RequestBody n_sac_id,
                                         @Part("n_user_id") RequestBody n_user_id);

        @Multipart
        @POST("_enroll_INagent.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll")
        Call<AddDocResponse> postFormOne(@Part("n_st_id") RequestBody n_st_id,
                                         @Part("n_dis_id") RequestBody n_dis_id,
                                         @Part("n_tu_id") RequestBody n_tu_id,
                                         @Part("n_hf_id") RequestBody n_hf_id,
                                         @Part("n_doc_id") RequestBody n_doc_id,
                                         @Part("d_reg_dat") RequestBody d_reg_dat,
                                         @Part("n_nksh_id") RequestBody n_nksh_id,
                                         @Part("c_pat_nam") RequestBody c_pat_nam,
                                         @Part("n_age") RequestBody n_age,
                                         @Part("n_sex") RequestBody n_sex,
                                         @Part("n_wght") RequestBody n_wght,
                                         @Part("n_hght") RequestBody n_hght,
                                         @Part("c_add") RequestBody c_add,
                                         @Part("c_taluka") RequestBody c_taluka,
                                         @Part("c_town") RequestBody c_town,
                                         @Part("c_ward") RequestBody c_ward,
                                         @Part("c_lnd_mrk") RequestBody c_lnd_mrk,
                                         @Part("n_pin") RequestBody n_pin,
                                         @Part("n_st_id_res") RequestBody n_st_id_res,
                                         @Part("n_dis_id_res") RequestBody n_dis_id_res,
                                         @Part("n_tu_id_res") RequestBody n_tu_id_res,
                                         @Part("c_mob") RequestBody c_mob,
                                         @Part("c_mob_2") RequestBody c_mob_2,
                                         @Part("n_lat") RequestBody n_lat,
                                         @Part("n_lng") RequestBody n_lng,
                                         @Part("c_not_img") RequestBody c_not_img,
                                         @Part("n_sac_id") RequestBody n_sac_id,
                                         @Part("n_user_id") RequestBody n_user_id,
                                         @Part("d_diag_dt") RequestBody d_diag_dt,
                                         @Part("n_cfrm") RequestBody n_cfrm);



        @Multipart
        @POST("_enroll_INagent.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll")
        Call<AddDocResponse> postFormOneSample(@Part("n_st_id") RequestBody n_st_id,
                                               @Part("n_dis_id") RequestBody n_dis_id,
                                               @Part("n_tu_id") RequestBody n_tu_id,
                                               @Part("n_hf_id") RequestBody n_hf_id,
                                               @Part("n_doc_id") RequestBody n_doc_id,
                                               @Part("d_reg_dat") RequestBody d_reg_dat,
                                               @Part("n_nksh_id") RequestBody n_nksh_id,
                                               @Part("c_pat_nam") RequestBody c_pat_nam,
                                               @Part("n_age") RequestBody n_age,
                                               @Part("n_sex") RequestBody n_sex,
                                               @Part("n_wght") RequestBody n_wght,
                                               @Part("n_hght") RequestBody n_hght,
                                               @Part("c_add") RequestBody c_add,
                                               @Part("c_taluka") RequestBody c_taluka,
                                               @Part("c_town") RequestBody c_town,
                                               @Part("c_ward") RequestBody c_ward,
                                               @Part("c_lnd_mrk") RequestBody c_lnd_mrk,
                                               @Part("n_pin") RequestBody n_pin,
                                               @Part("n_st_id_res") RequestBody n_st_id_res,
                                               @Part("n_dis_id_res") RequestBody n_dis_id_res,
                                               @Part("n_tu_id_res") RequestBody n_tu_id_res,
                                               @Part("c_mob") RequestBody c_mob,
                                               @Part("c_mob_2") RequestBody c_mob_2,
                                               @Part("n_lat") RequestBody n_lat,
                                               @Part("n_lng") RequestBody n_lng,
                                               @Part("c_not_img") RequestBody c_not_img,
                                               @Part("n_sac_id") RequestBody n_sac_id,
                                               @Part("n_user_id") RequestBody n_user_id,
                                               @Part("n_hiv") RequestBody n_hiv,
                                               @Part("n_diab") RequestBody n_diab
                                              /*  @Part("d_diag_dt") RequestBody d_diag_dt */);

        //d_reg_dat	date
//n_nksh_id	varchar(20)
//c_pat_nam	varchar(200)
//n_age	int(11)
//n_sex	int(11)	1-Male | 2-Female | 3-TG
//n_wght	int(11)
//n_hght	int(11)
//c_add	varchar(250)
//c_taluka	varchar(100)
//c_town	varchar(100)
//c_ward	varchar(100)
//c_lnd_mrk	varchar(150)
//n_pin	double
//n_st_id_res	int(11)
//n_dis_id_res	int(11)
//n_tu_id_res	varchar(200)
//c_mob	varchar(10)
//c_mob_2	varchar(10)
//c_not_img	varchar(50)
//c_bnk_img	varchar(50)
//d_diag_dt	date
        @Multipart
        @POST("_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll")
        Call<AddDocResponse> updateFormOne(@Part("d_reg_dat") RequestBody d_reg_dat,
                                           @Part("n_nksh_id") RequestBody n_nksh_id,
                                           @Part("c_pat_nam") RequestBody c_pat_nam,
                                           @Part("n_age") RequestBody n_age,
                                           @Part("n_sex") RequestBody n_sex,
                                           @Part("n_wght") RequestBody n_wght,
                                           @Part("n_hght") RequestBody n_hght,
                                           @Part("c_add") RequestBody c_add,
                                           @Part("c_taluka") RequestBody c_taluka,
                                           @Part("c_town") RequestBody c_town,
                                           @Part("c_ward") RequestBody c_ward,
                                           @Part("c_lnd_mrk") RequestBody c_lnd_mrk,
                                           @Part("n_pin") RequestBody n_pin,
                                           @Part("n_st_id_res") RequestBody n_st_id_res,
                                           @Part("n_dis_id_res") RequestBody n_dis_id_res,
                                           @Part("n_tu_id_res") RequestBody n_tu_id_res,
                                           @Part("c_mob") RequestBody c_mob,
                                           @Part("c_mob_2") RequestBody c_mob_2,
                                           @Part("c_not_img") RequestBody c_not_img,
                                           @Part("c_bnk_img") RequestBody c_bnk_img,
                                           @Part("d_diag_dt") RequestBody d_diag_dt,
                                           @Query("w") String patientId);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_reason&w=id<<GT>>0")
        Call<FormOneResponse> getTesting();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_cont_samp&w=id<<GT>>0")
        Call<NoOfContResponse> getNoOfContainers();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_diag_test&w=id<<GT>>0")
        Call<DiagnosticTestRes> getDiagnosticTestsSample();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_typ_specm&w=id<<GT>>0")
        Call<FormOneResponse> getSpecimen();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_smpl_ext&w=id<<GT>>0")
        Call<CollectedByResponse> getExtraction();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_sc_frm&w=id<<GT>>0")
        Call<CollectedFromSamplesRes> getCollectedFrom();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_sputm_typ&w=id<<GT>>0")
        Call<FormOneResponse> getType();

        @Multipart
        @POST("_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll&w=id")
        Call<AddDocResponse> postFormPartOne(@Part("n_st_id") RequestBody n_st_id,
                                             @Part("n_dis_id") RequestBody n_dis_id,
                                             @Part("n_tu_id") RequestBody n_tu_id,
                                             @Part("n_hf_id") RequestBody n_hf_id,
                                             @Part("n_doc_id") RequestBody n_doc_id,
                                             @Part("n_enroll_id") RequestBody n_enroll_id,
                                             @Part("d_specm_col") RequestBody d_specm_col,
                                             @Part("n_smpl_ext_id") RequestBody n_smpl_ext_id,
                                             @Part("n_test_reas_id") RequestBody n_test_reas_id,
                                             @Part("n_purp_vst") RequestBody n_purp_vst,
                                             @Part("n_typ_specm_id") RequestBody n_typ_specm_id,
                                             @Part("n_cont_smpl") RequestBody n_cont_smpl,
                                             @Part("c_plc_samp_col") RequestBody c_plc_samp_col,
                                             @Part("n_sputm_typ_id") RequestBody n_sputm_typ_id,
                                             @Part("n_diag_tst") RequestBody n_diag_tst,
                                             @Part("n_lab_id") RequestBody n_lab_id,
                                             @Part("n_staff_info") RequestBody n_staff_info,
                                             @Part("n_user_id") RequestBody n_user_id);


        /*
        (n_st_id, n_dis_id, n_tu_id, n_hf_id, n_doc_id, n_enroll_id, d_specm_col, n_smpl_ext_id, n_test_reas_id, n_purp_vst, n_typ_specm_id, n_cont_smpl, c_plc_samp_col, n_sputm_typ_id, n_diag_tst, n_lab_id, n_staff_info, n_user_id)
         */
        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_smpl_col_rpt")
        Call<AddDocResponse> addSampleApi(@Part("n_st_id") RequestBody n_st_id,
                                             @Part("n_dis_id") RequestBody n_dis_id,
                                             @Part("n_tu_id") RequestBody n_tu_id,
                                             @Part("n_hf_id") RequestBody n_hf_id,
                                             @Part("n_doc_id") RequestBody n_doc_id,
                                             @Part("n_enroll_id") RequestBody n_enroll_id,
                                             @Part("d_specm_col") RequestBody d_specm_col,
                                             @Part("n_smpl_ext_id") RequestBody n_smpl_ext_id,
                                             @Part("n_test_reas_id") RequestBody n_test_reas_id,
                                             @Part("n_purp_vst") RequestBody n_purp_vst,
                                             @Part("n_typ_specm_id") RequestBody n_typ_specm_id,
                                             @Part("n_cont_smpl") RequestBody n_cont_smpl,
                                             @Part("c_plc_samp_col") RequestBody c_plc_samp_col,
                                             @Part("n_sputm_typ_id") RequestBody n_sputm_typ_id,
                                             @Part("n_diag_tst") RequestBody n_diag_tst,
                                             @Part("n_lab_id") RequestBody n_lab_id,
                                             @Part("n_staff_info") RequestBody n_staff_info,
                                             @Part("n_user_id") RequestBody n_user_id);


        @Multipart
        @POST("_data_agentUPD.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll&w=id&n_cfrm<<EQUALTO>>0")
        Call<AddDocResponse> postFormPartOne2(@Part("n_st_id") RequestBody n_st_id,
                                              @Part("n_dis_id") RequestBody n_dis_id,
                                              @Part("n_tu_id") RequestBody n_tu_id,
                                              @Part("n_hf_id") RequestBody n_hf_id,
                                              @Part("n_doc_id") RequestBody n_doc_id,
                                              @Part("n_enroll_id") RequestBody n_enroll_id,
                                              @Part("d_specm_col") RequestBody d_specm_col,
                                              @Part("n_smpl_ext_id") RequestBody n_smpl_ext_id,
                                              @Part("n_test_reas_id") RequestBody n_test_reas_id,
                                              @Part("n_purp_vst") RequestBody n_purp_vst,
                                              @Part("n_typ_specm_id") RequestBody n_typ_specm_id,
                                              @Part("n_cont_smpl") RequestBody n_cont_smpl,
                                              @Part("c_plc_samp_col") RequestBody c_plc_samp_col,
                                              @Part("n_sputm_typ_id") RequestBody n_sputm_typ_id,
                                              @Part("n_diag_tst") RequestBody n_diag_tst,
                                              @Part("n_lab_id") RequestBody n_lab_id,
                                              @Part("n_staff_info") RequestBody n_staff_info,
                                              @Part("n_user_id") RequestBody n_user_id,
                                              @Part("d_diag_dt") RequestBody d_diag_dt,
                                              @Part("n_cfrm") RequestBody n_cfrm
        );


        @Multipart
        @POST()
        Call<JsonObject> reason(@Part("d_diag_dt") RequestBody d_diag_dt,
                                @Part("n_cfrm") RequestBody n_cfrm,


                                @Url String url

        );

//        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_hf_id<<EQUALTO>>32<<AND>>n_user_id<<EQUALTO>>18")
//        Call<>

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_diag_test&w=id<<GT>>0")
        Call<FormOneResponse> getDiagnosticTests();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_serv_typ&w=id<<GT>>0 ")
        Call<FormOneResponse> getPurpose();

        @GET()
        Call<RegisterParentResponse> getRegisterParent(@Url String url);

        @GET()
        Call<CounselingResponse> getCounsellingTypes(@Url String url);

        @GET()
        Call<FilterResponse> getCounsellingFilters(@Url String url);

        @GET()
        Call<TestreportResponse> getTestReportResults(@Url String url);

        @GET()
        Call<UOMResponse> getUOM(@Url String url);

        @GET()
        Call<WeightResponse> getWeight(@Url String url);

        @GET()
        Call<MedicineResponse> getMedicine(@Url String url);


        @GET()
        Call<HospitalResponse> getHospitalDetail(@Url String url);

        @Multipart
        @POST()
        Call<AddDocResponse> postFormOneSampleEdit_new(
                @Url String url,
                @Part("n_st_id") RequestBody n_st_id,
                                         @Part("n_dis_id") RequestBody n_dis_id,
                                         @Part("n_tu_id") RequestBody n_tu_id,
                                         @Part("n_hf_id") RequestBody n_hf_id,
                                         @Part("n_doc_id") RequestBody n_doc_id,
                                         @Part("d_reg_dat") RequestBody d_reg_dat,
                                         @Part("n_nksh_id") RequestBody n_nksh_id,
                                         @Part("c_pat_nam") RequestBody c_pat_nam,
                                         @Part("n_age") RequestBody n_age,
                                         @Part("n_sex") RequestBody n_sex,
                                         @Part("n_wght") RequestBody n_wght,
                                         @Part("n_hght") RequestBody n_hght,
                                         @Part("c_add") RequestBody c_add,
                                         @Part("c_taluka") RequestBody c_taluka,
                                         @Part("c_town") RequestBody c_town,
                                         @Part("c_ward") RequestBody c_ward,
                                         @Part("c_lnd_mrk") RequestBody c_lnd_mrk,
                                         @Part("n_pin") RequestBody n_pin,
                                         @Part("n_st_id_res") RequestBody n_st_id_res,
                                         @Part("n_dis_id_res") RequestBody n_dis_id_res,
                                         @Part("n_tu_id_res") RequestBody n_tu_id_res,
                                         @Part("c_mob") RequestBody c_mob,
                                         @Part("c_mob_2") RequestBody c_mob_2,
                                         @Part("n_lat") RequestBody n_lat,
                                         @Part("n_lng") RequestBody n_lng,
                                         @Part("c_bnk_img") RequestBody c_bnk_img,
                                         @Part("c_not_img") RequestBody c_not_img,
                                         @Part("n_sac_id") RequestBody n_sac_id,
                                         @Part("n_user_id") RequestBody n_user_id,
                                         @Part("d_diag_dt") RequestBody d_diag_dt,
                                         @Part("n_cfrm") RequestBody n_cfrm,
                                         @Part("n_hiv" )RequestBody n_hiv,
                                         @Part("n_diab") RequestBody n_diab);


        @Multipart
        @POST()
        Call<AddDocResponse> updateHospital(@Url String url, @Part("d_reg_dat") RequestBody d_reg_dat,
                                            @Part("n_nksh_id") RequestBody n_nksh_id,
                                            @Part("c_pat_nam") RequestBody c_pat_nam,
                                            @Part("n_age") RequestBody n_age,
                                            @Part("n_sex") RequestBody n_sex,
                                            @Part("n_wght") RequestBody n_wght,
                                            @Part("n_hght") RequestBody n_hght,
                                            @Part("c_add") RequestBody c_add,
                                            @Part("c_taluka") RequestBody c_taluka,
                                            @Part("c_town") RequestBody c_town,
                                            @Part("c_ward") RequestBody c_ward,
                                            @Part("c_lnd_mrk") RequestBody c_lnd_mrk,
                                            @Part("n_pin") RequestBody n_pin,
                                            @Part("n_st_id_res") RequestBody n_st_id_res,
                                            @Part("n_dis_id_res") RequestBody n_dis_id_res,
                                            @Part("n_tu_id_res") RequestBody n_tu_id_res,
                                            @Part("c_mob") RequestBody c_mob,
                                            @Part("c_mob_2") RequestBody c_mob_2,
                                            @Part("c_not_img") RequestBody c_not_img,
                                            @Part("c_bnk_img") RequestBody c_bnk_img,
                                            @Part("d_diag_dt") RequestBody d_diag_dt,
                                            @Part("n_cfrm") RequestBody n_cfrm);

        @GET()
        Call<DoctorsResponse> getDoctorDetails(@Url String url);

        @GET()
        Call<PatientResponse> getPateintDetail(@Url String url);

        @GET()
        Call<TestreportResponse> getFdcOpeningStockData(@Url String url);

        @GET()
        Call<LpaTestResultResponse> getLpaTestResults(@Url String url);

        @GET()
        Call<PythologyLabResponse> getPythologyLabs(@Url String url);

        @GET()
        Call<LabTypeResponse> getPythologyLabTypes(@Url String url);

        @GET()
        Call<SampleResponse> getSample(@Url String url);


        @FormUrlEncoded
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_t_attend&")
        Call<AttendenceListResponse> getAttendenceList(@Field("w") String userID);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_diag_test&w=id<<GT>>0")
        Call<FormOneResponse> getDiagonsticTest();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_dst&w=id<<GT>>0")
        Call<FormOneResponse> getDSTOffered();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_oth_dst&w=id<<GT>>0")
        Call<FormOneResponse> getOtherDST();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_case_typ&w=id<<GT>>0")
        Call<FormOneResponse> getInter();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_tst_rsult&w=id<<GT>>0")
        Call<FormOneResponse> getTestResult();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_fnl_int&w=id<<GT>>0")
        Call<FormOneResponse> getCaseType();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_pat_status&w=id<<GT>>0")
        Call<FormOneResponse> getPatientStatus();

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_prov_eng")
        Call<AddDocResponse> postProviderSubmit(@Part("n_st_id") RequestBody n_st_id,
                                                @Part("n_dis_id") RequestBody n_dis_id,
                                                @Part("n_tu_id") RequestBody n_tu_id,
                                                @Part("n_hf_id") RequestBody n_hf_id,
                                                @Part("d_reg_dat") RequestBody d_reg_dat,
                                                @Part("n_typ") RequestBody n_typ,
                                                @Part("n_user_id") RequestBody n_user_id,
                                                @Part("n_lat") RequestBody n_lat,
                                                @Part("n_lng") RequestBody n_lng);

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_smpl_tests")
        Call<AddDocResponse> postTest(@Part("n_st_id") RequestBody n_st_id,
                                      @Part("n_dis_id") RequestBody n_dis_id,
                                      @Part("n_tu_id") RequestBody n_tu_id,
                                      @Part("n_hf_id") RequestBody n_hf_id,
                                      @Part("n_enroll_id") RequestBody n_enroll_id,
                                      @Part("n_smpl_col_id") RequestBody n_smpl_col_id,
                                      @Part("d_smpl_recd") RequestBody d_smpl_recd,
                                      @Part("d_rpt_doc") RequestBody d_rpt_doc,
                                      @Part("n_diag_test") RequestBody n_diag_test,
                                      @Part("d_tst_diag") RequestBody d_tst_diag,
                                      @Part("d_tst_rpt_diag") RequestBody d_tst_rpt_diag,
                                      @Part("n_dst") RequestBody n_dst,
                                      @Part("d_tst_dst") RequestBody d_tst_dst,
                                      @Part("d_tst_rpt_dst") RequestBody d_tst_rpt_dst,
                                      @Part("n_oth_dst") RequestBody n_oth_dst,
                                      @Part("d_tst_rpt_oth_dst") RequestBody d_tst_rpt_oth_dst,
                                      @Part("d_tst_oth_dst") RequestBody d_tst_oth_dst,
                                      @Part("n_fnl_intp") RequestBody n_fnl_intp,
                                      @Part("n_tst_rsult") RequestBody n_tst_rsult,
                                      @Part("n_case_typ") RequestBody n_case_typ,
                                      @Part("n_pat_status") RequestBody n_pat_status,
                                      @Part("n_user_id") RequestBody n_user_id,
                                      @Part("n_lat") RequestBody n_lat,
                                      @Part("n_lng") RequestBody n_lng);

        @GET
        Call<GetTestResponse> getTestList(@Url String url);

        @GET
        Call<JsonObject> getAttendanceType(@Url String url);

        @GET
        Call<RegisterParentResponse> getPatientTestDone(@Url String url);

        @GET
        Call<PrevVisitsResponse> getPrevVisits(@Url String url);


        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_visit_typ")
        Call<AddDocResponse> postProvider(@Part("n_st_id") RequestBody n_st_id,
                                          @Part("n_dis_id") RequestBody n_dis_id,
                                          @Part("n_tu_id") RequestBody n_tu_id,
                                          @Part("n_hf_id") RequestBody n_hf_id,
                                          @Part("n_hf_typ") RequestBody n_hf_typ,
                                          @Part("d_visit") RequestBody d_visit,
                                          @Part("n_visit_id") RequestBody n_visit_id,
                                          @Part("n_sac_id") RequestBody n_sac_id,
                                          @Part("n_user_id") RequestBody n_user_id);

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_visit_typ")
        Call<AddDocResponse> postMultipleDateProvider(

                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_hf_typ") RequestBody n_hf_typ,
                @Part("d_visit") RequestBody d_visit,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_sac_id") RequestBody n_sac_id
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_visit_doc")
        Call<AddDocResponse> postMultipleDoctorsProvider(

                @Part("n_visit_id") RequestBody n_visit_id,
                @Part("n_sac_id") RequestBody n_sac_id,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_doc_id") RequestBody n_doc_id
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_visit_types")
        Call<AddDocResponse> postMultipleVisitsProvider(

                @Part("n_visit_id") RequestBody n_visit_id,
                @Part("n_typ") RequestBody n_typ,
                @Part("n_sac_id") RequestBody n_sac_id,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_obal")
        Call<AddDocResponse> postFdcOpBal(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_med_id") RequestBody n_med_id,
                @Part("n_uom_id") RequestBody n_uom_id,
                @Part("n_qty") RequestBody n_qty,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_staff_info") RequestBody n_staff_info,
                @Part("n_user_id") RequestBody n_user_id
        );

        @Multipart
        @POST("_data_agentupd.php?k=glgjieywgnfkg783hkd7tujavdjtykugd&u=ywgnfkg783h&p=j1v5jlyk5gf&t=_t_fdc_obal&cn=id&w=1<<AND>>n_med_id<<EQUALTO>>5")
        Call<AddDocResponse> postFdcOpBal(
                @Part("n_uom_id") RequestBody n_uom_id,
                @Part("n_qty") RequestBody n_qty
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_issue")
        Call<AddDocResponse> postFDCIssued(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_med_id") RequestBody n_med_id,
                @Part("n_uom_id") RequestBody n_uom_id,
                @Part("n_qty") RequestBody n_qty,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_staff_info") RequestBody n_staff_info,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("n_doc_id") RequestBody n_doc_id,
                @Part("d_issue") RequestBody d_issue
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_disp")
        Call<AddDocResponse> postFDCIssuedWeight(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_med_id") RequestBody n_med_id,
                @Part("n_uom_id") RequestBody n_uom_id,
                @Part("n_qty") RequestBody n_qty,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_staff_info") RequestBody n_staff_info,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("n_doc_id") RequestBody n_doc_id,
                @Part("d_issue") RequestBody d_issue,
                @Part("n_wght_bnd") RequestBody n_wght_bnd,
                @Part("n_enroll_id") RequestBody n_enroll_id,
                @Part("n_days") RequestBody n_day,
                @Part("d_refill") RequestBody d_refill
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_fdc_rec")
        Call<AddDocResponse> postFdcRec(
                @Part("n_med") RequestBody n_med_id,
                @Part("n_uom") RequestBody n_uom_id,
                @Part("n_qty") RequestBody n_qty,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_staff_info") RequestBody n_staff_info,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("d_rec") RequestBody d_rec,
                @Part("n_sanc") RequestBody n_sanc
        );

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_fds_obal&w=n_hf_id<<EQUALTO>>32<<AND>>n_tu_id<<EQUALTO>>15")
        Call<PrevVisitsResponse> getFdcFormOp();

        @GET()
        Call<PrevVisitsResponse> getFdcForm(@Url String url);

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_lpa_tst")
        Call<AddDocResponse> postLpaResult(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_doc_id") RequestBody n_doc_id,
                @Part("n_enroll_id") RequestBody n_enroll_id,
                @Part("n_smpl_col_id") RequestBody n_smpl_col_id,
                @Part("n_rpt_col_id") RequestBody n_rpt_col_id,
                @Part("d_lpa") RequestBody d_lpa,
//                @Part("n_lpa_rslt") RequestBody n_lpa_rslt,
//                @Part("c_lpa_img") RequestBody c_pla_img,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_sanc_id") RequestBody n_sanc_id,
                @Part("n_user_id") RequestBody n_user_id

        );

        @Multipart
        @POST("_pat_docs.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_docs")
        Call<AddDocResponse> uploadDocumentIfNoResult(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("n_hf_id") RequestBody n_hf_id,
                @Part("n_doc_id") RequestBody n_doc_id,
                @Part("n_enroll_id") RequestBody n_enroll_id,
                @PartMap() Map<String, RequestBody> partMap,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng,
                @Part("n_sanc_id") RequestBody n_sanc_id,
                @Part("n_user_id") RequestBody n_user_id
        );

        @Multipart
        @POST()
        Call<AddDocResponse> uploadDocumentIfResult(
                @Url String url,
                @PartMap() Map<String, RequestBody> partMap
        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_pat_transf")
        Call<AddDocResponse> transferPatient(
                @Part("n_st_id") RequestBody n_st_id,
                @Part("n_dis_id") RequestBody n_dis_id,
                @Part("n_tu_id") RequestBody n_tu_id,
                @Part("d_tranf") RequestBody d_tranf,
                @Part("n_enroll_id") RequestBody n_enroll_id,
                @Part("n_sanc_id") RequestBody n_sanc_id,
                @Part("n_user_id") RequestBody n_user_id,
                @Part("n_lat") RequestBody n_lat,
                @Part("n_lng") RequestBody n_lng

        );

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_user_loc")
        Call<AddDocResponse> postLoc(
                @Part("n_lat") RequestBody nLat,
                @Part("n_lng") RequestBody nLng,
                @Part("n_sanc") RequestBody nSanc,
                @Part("n_staff_info") RequestBody nStaffInfo,
                @Part("n_user_id") RequestBody nUserId

        );
    }
}
