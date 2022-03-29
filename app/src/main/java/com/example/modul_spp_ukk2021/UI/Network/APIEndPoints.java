package com.example.modul_spp_ukk2021.UI.Network;

import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIEndPoints {

    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewSiswa();

    @FormUrlEncoded
    @POST("dbLoginSiswa.php")
    Call<LoginSiswaRepository> loginSiswa(
            @Field("nisn") String nisn,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("dbLoginStaffLevel.php")
    Call<LoginStaffRepository> loginStaff(
            @Field("username") String username,
            @Field("password") String password);

    @GET("dbReadPetugas.php")
    Call<PetugasRepository> viewPetugas();

    @FormUrlEncoded
    @POST("dbCreatePetugas.php")
    Call<PetugasRepository> createStaff(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level);

    @GET("dbReadSPP.php")
    Call<SPPRepository> viewDataSPP();

    @FormUrlEncoded
    @POST("dbReadTagihan.php")
    Call<PembayaranRepository> viewTagihan(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbReadHistory.php")
    Call<PembayaranRepository> viewHistory(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbSearchSiswa.php")
    Call<SiswaRepository> searchDataSiswa(
            @Field("search") String search);

    @GET("dbReadAllSiswa.php")
    Call<SiswaRepository> viewDataSiswa();

    @FormUrlEncoded
    @POST("dbReadPetugas.php")
    Call<PetugasRepository> viewDataPetugas(
            @Field("username") String username);
}
