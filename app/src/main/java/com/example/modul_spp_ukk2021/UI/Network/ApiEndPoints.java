package com.example.modul_spp_ukk2021.UI.Network;

import com.example.modul_spp_ukk2021.UI.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Repository.SiswaRepository;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiEndPoints {

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
    Call<PetugasRepository> viewDataPetugas();
}
