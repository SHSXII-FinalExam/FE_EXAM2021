package com.example.modul_spp_ukk2021.UI.Network;

import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiEndPoints {
    @FormUrlEncoded
    @POST("dbSearchSiswa.php")
    Call<SiswaRepository> search(@Field("search") String search);

    @GET("dbReadPembayaran.php")
    Call<PembayaranRepository> viewPembayaran();

    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewSiswa();
}
