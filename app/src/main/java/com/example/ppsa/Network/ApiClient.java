package com.example.ppsa.Network;

import android.annotation.SuppressLint;

import com.example.ppsa.R;
import com.example.ppsa.Response.AddDocResponse;
import com.example.ppsa.Response.AllUserResponse;
import com.example.ppsa.Response.AttendeceResponse;
import com.example.ppsa.Response.AttendenceTypeResponse;
import com.example.ppsa.Response.DoctorsResponse;
import com.example.ppsa.Response.FormOneResponse;
import com.example.ppsa.Response.FormTwoSpinnerResponse;
import com.example.ppsa.Response.HospitalResponse;
import com.example.ppsa.Response.PatientTypeResponse;
import com.example.ppsa.Response.QualificationList;
import com.example.ppsa.Response.QualificationResponse;
import com.example.ppsa.Response.RegisterParentResponse;
import com.example.ppsa.Response.UserInfoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public class ApiClient {
    public static final String BASE_URL = "http://nikshayppsa.hlfppt.org/_api-v1_/";

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
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_user_oth_link&")
        Call<UserInfoResponse> getUsersInfo(@Field("w") String id);

        @FormUrlEncoded
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_doc")
        Call<DoctorsResponse> getDoctorsList(@Field("w") String id);

        @FormUrlEncoded
        @POST("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_hf_link&")
        Call<HospitalResponse> getHospitalList(@Field("w") String id);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_doc_qual&w=id<<GT>>0")
        Call<QualificationResponse> getQualificationList();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_doc_spec&w=n_hf_typ_id<<EQUALTO>>1")
        Call<QualificationResponse> getQualificationSpeList();

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_hf_doc")
        Call<AddDocResponse> addDoc(@Part("n_hf_id") RequestBody hfid,
                                    @Part("c_doc_nam") RequestBody docName,
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
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_m_link")
        Call<AddDocResponse> hospitalSync(@Part("n_hf_id") RequestBody n_hf_id,
                                          @Part("n_pm_id") RequestBody n_pm_id,
                                          @Part("n_pc_id") RequestBody n_pc_id,
                                          @Part("n_sfta_id") RequestBody n_sfta_id,
                                          @Part("n_staff_id") RequestBody n_staff_id,
                                          @Part("n_user_id") RequestBody n_user_id);





        @GET()
        Call<AttendeceResponse> getAttendence(@Url String url);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_attend_code&w=id<<GT>>0")
        Call<AttendenceTypeResponse> getAttendenceType();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_gender&w=id<<GT>>0")
        Call<FormOneResponse> getFormGender();

        @Multipart
        @POST("https://nikshayppsa.hlfppt.org/_api-v1_/_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_attend")
        Call<AddDocResponse> postAttendance(@Part("n_attend_typ") RequestBody n_attend_typ,
                                            @Part("d_rpt") RequestBody d_rpt,
                                            @Part("n_user_id") RequestBody n_user_id);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_state&w=id<<GT>>0")
        Call<FormOneResponse> getFormState();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_st_dis&w=n_st_id<<EQUALTO>>3")
        Call<FormOneResponse> getFormDistrict();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_tu&w=n_st_id<<EQUALTO>>5<<AND>>n_dis_id<<EQUALTO>>70")
        Call<FormOneResponse> getFormTU();

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_enroll")
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
                                         @Part("n_user_id") RequestBody n_user_id);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_reason&w=id<<GT>>0")
        Call<FormOneResponse> getTesting();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_typ_specm&w=id<<GT>>0")
        Call<FormOneResponse> getSpecimen();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_smpl_ext&w=id<<GT>>0")
        Call<FormOneResponse> getExtraction();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f2_sputm_typ&w=id<<GT>>0")
        Call<FormOneResponse> getType();

        @Multipart
        @POST("_data_agentUSS.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&t=_t_smpl_col_rpt")
        Call<AddDocResponse> postFormPartOne(@Part("n_st_id") RequestBody n_st_id,
                                             @Part("n_dis_id") RequestBody n_dis_id,
                                             @Part("n_tu_id") RequestBody n_tu_id,
                                             @Part("n_hf_id") RequestBody n_hf_id,
                                             @Part("n_enroll_id") RequestBody n_enroll_id,
                                             @Part("n_test_reas_id") RequestBody n_test_reas_id,
                                             @Part("n_typ_specm_id") RequestBody n_typ_specm_id,
                                             @Part("d_specm_col") RequestBody d_specm_col,
                                             @Part("c_plc_samp_col") RequestBody c_plc_samp_col,
                                             @Part("n_smpl_ext_id") RequestBody n_smpl_ext_id,
                                             @Part("n_sputm_typ_id") RequestBody n_sputm_typ_id,
                                             @Part("n_user_id") RequestBody n_user_id);

//        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_v_enroll&w=n_hf_id<<EQUALTO>>32<<AND>>n_user_id<<EQUALTO>>18")
//        Call<>

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_diag_test&w=id<<GT>>0")
        Call<FormOneResponse> getDiagnosticTests();

        @GET()
        Call<RegisterParentResponse> getRegisterParent(@Url String url);

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_diag_test&w=id<<GT>>0")
        Call<FormTwoSpinnerResponse> getDiagnosticTest();
        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_dst&w=id<<GT>>0")
        Call<FormTwoSpinnerResponse> getDstOfferd();
        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_oth_dst&w=id<<GT>>0")
        Call<FormTwoSpinnerResponse> getOtherDstOfferd();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_case_typ&w=id<<GT>>0")
        Call<FormTwoSpinnerResponse> getFinalInterpretaion();

        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_tst_rsult&w=id<<GT>>0")
        Call<FormTwoSpinnerResponse> getTestResult();
        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_f21_fnl_int&w=id<<GT>>0")
        Call<FormTwoSpinnerResponse> getCaseType();
        @GET("_get_.php?k=glgjieyWGNfkg783hkd7tujavdjTykUgd&u=yWGNfkg783h&p=j1v5Jlyk5Gf&v=_m_pat_status")
        Call<PatientTypeResponse> getParentalStatus();

    }
}
