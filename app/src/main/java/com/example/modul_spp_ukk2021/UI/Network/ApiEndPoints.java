package com.example.modul_spp_ukk2021.UI.Network;

import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
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

public interface ApiEndPoints {

    // Login
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

    // Punya Siswa
    @FormUrlEncoded
    @POST("dbReadTagihan.php")
    Call<PembayaranRepository> viewTagihan(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbReadHistory.php")
    Call<PembayaranRepository> viewHistory(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbReadPembayaran.php")
    Call<PembayaranRepository> viewPembayaran(
            @Field("nisn") String nisn);

    // Punya Petugas & Admin
    @FormUrlEncoded
    @POST("dbCreatePetugas.php")
    Call<PetugasRepository> createPetugas(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_petugas") String nama_petugas,
            @Field("level") String level);

    @FormUrlEncoded
    @POST("dbUpdatePembayaran.php")
    Call<PembayaranRepository> updatePembayaran(
            @Field("id_pembayaran") String id_pembayaran,
            @Field("jumlah_bayar") String jumlah_bayar);

    @FormUrlEncoded
    @POST("dbCreateSPP.php")
    Call<SPPRepository> createSPP(
            @Field("angkatan") String angkatan,
            @Field("tahun") String tahun,
            @Field("nominal") String nominal);

    @FormUrlEncoded
    @POST("dbSearchSiswa.php")
    Call<SiswaRepository> searchDataSiswa(
            @Field("search") String search);

    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewDataSiswa();

    @GET("dbReadSPP.php")
    Call<SPPRepository> viewDataSPP();

    @GET("dbReadKelas.php")
    Call<KelasRepository> viewDataKelas();

    @GET("dbReadPetugas.php")
    Call<PetugasRepository> viewDataPetugas();
}
