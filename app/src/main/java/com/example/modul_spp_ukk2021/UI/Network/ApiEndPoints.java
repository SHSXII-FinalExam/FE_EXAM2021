package com.example.modul_spp_ukk2021.UI.Network;


import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiEndPoints {

    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewSiswa();
    @GET("dbReadPetugas.php")
    Call<PetugasRepository> viewPetugas();
    @FormUrlEncoded
    @POST("dbLoginSiswa.php")
    Call<LoginSiswaRepository> loginSiswa(
            @Field("nisn") String nisn,
            @Field("password") String password);
    @FormUrlEncoded
    @POST("dbLoginStaffLevel.php")
    Call<LoginStafRepository> loginStaf(
            @Field("username") String username,
            @Field("password") String password);
    @FormUrlEncoded
    @POST("dbReadTagihan.php")
    retrofit2.Call<PembayaranRepository> viewTagihan(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbReadHistory.php")
    retrofit2.Call<PembayaranRepository> viewHistory(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbReadPetugas.php")
    Call<PetugasRepository> viewDataPetugas(
            @Field("username") String username);
    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewDataSiswa();

    Call<PetugasRepository> viewDataPetugas();

    Call<SiswaRepository> updateSiswa(String toString, String toString1, String id_kelas, Integer id_spp, String toString2, String toString3, String toString4, String id_petugas);

    Call<KelasRepository> updateKelas(String id_kelas, String s, String jurusan_kelas, String toString);

    Call<SPPRepository> viewDataSPPngkatan(String angkatan);

    Call<KelasRepository> deleteKelas(String id_kelas);

    Call<KelasRepository> createKelas(String toString, String s, String trim);

    Call<KelasRepository> viewDataKelas();

    Call<SiswaRepository> viewDataSiswaKelas(String id_kelas);

    Call<SiswaRepository> searchDataSiswa(String trim);

    Call<SPPRepository> viewDataSPP();

    Call<PetugasRepository> deletePetugas(String id_petugas);

    Call<PetugasRepository> createPetugas(String petugas, String toString, String toString1, String toString2);

    Call<PetugasRepository> updatePetugas(String id_petugas, String petugas, String toString, String toString1, String toString2);

    Call<SPPRepository> viewDataSPPAngkatan(String angkatan);

    Call<SiswaRepository> createSiswa(String toString, String toString1, String toString2, String id_kelas, Integer id_spp, String toString3, String toString4, String toString5, String id_petugas);

    Call<SiswaRepository> deleteSiswa(String nisn);

    Call<SPPRepository> updateSPP(Integer id_spp, String toString);

    Call<SPPRepository> createSPP(String toString, String toString1, String toString2);

    Call<SPPRepository> deleteSPP(Integer id_spp);

    Call<PetugasRepository> viewPetugas(String username);

    Call<PembayaranRepository> viewPembayaran(String nisnSiswa);

    Call<PembayaranRepository> updatePembayaran(String id_pembayaran, String toString, String id_petugas);


}
