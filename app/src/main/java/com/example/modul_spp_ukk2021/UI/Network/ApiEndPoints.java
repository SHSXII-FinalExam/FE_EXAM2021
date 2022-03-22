package com.example.modul_spp_ukk2021.UI.Network;

import android.telecom.Call;

public interface ApiEndPoints {
    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewSiswa();
    @GET("dbReadPetugas.php")
    Call<PetugasRepository> viewPetugas();
    @POST("dbLoginSiswa.php")
    Call<LoginSiswaRepository> loginSiswa(
            @Field("nisn") String nisn,
            @Field("password") String password);
    @POST("dbLoginStaffLevel.php")
    Call<LoginStafRepository> loginStaf(
            @Field("username") String username,
            @Field("password") String password);
    @POST("dbReadTagihan.php")
    Call<PembayaranRepository> viewTagihan(
            @Field("nisn") String nisn);

    @POST("dbReadHistory.php")
    Call<PembayaranRepository> viewHistory(
            @Field("nisn") String nisn);

    @POST("dbReadPetugas.php")
    Call<PetugasRepository> viewDataPetugas(
            @Field("username") String username);
    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewDataSiswa();
}
