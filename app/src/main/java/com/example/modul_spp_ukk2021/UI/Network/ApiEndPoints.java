package com.example.modul_spp_ukk2021.UI.Network;

import com.example.modul_spp_ukk2021.UI.Repository.SiswaRepository;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiEndPoints {

    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewSiswa();
}
