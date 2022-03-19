package com.example.modul_spp_ukk2021.UI.DB;

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

    // Punya Petugas
    @FormUrlEncoded
    @POST("dbReadPetugas.php")
    Call<PetugasRepository> viewDataPetugas(
            @Field("username") String username);

    @GET("dbReadSiswa.php")
    Call<SiswaRepository> viewDataSiswa();

    @FormUrlEncoded
    @POST("dbSearchSiswa.php")
    Call<SiswaRepository> searchDataSiswa(
            @Field("search") String search);

    @FormUrlEncoded
    @POST("dbReadPembayaran.php")
    Call<PembayaranRepository> viewPembayaran(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbUpdatePembayaran.php")
    Call<PembayaranRepository> updatePembayaran(
            @Field("id_pembayaran") String id_pembayaran,
            @Field("jumlah_bayar") String jumlah_bayar);

    // Punya Admin
    @FormUrlEncoded
    @POST("dbDeleteSiswa.php")
    Call<SiswaRepository> deleteSiswa(
            @Field("nisn") String nisn);

    @FormUrlEncoded
    @POST("dbDeleteSPP.php")
    Call<SPPRepository> deleteSPP(
            @Field("id_spp") String id_spp);

    @FormUrlEncoded
    @POST("dbDeleteKelas.php")
    Call<KelasRepository> deleteKelas(
            @Field("id_kelas") String id_kelas);

    @FormUrlEncoded
    @POST("dbDeletePetugas.php")
    Call<PetugasRepository> deletePetugas(
            @Field("id_petugas") String id_petugas);

    @FormUrlEncoded
    @POST("dbUpdateSPP.php")
    Call<SPPRepository> updateSPP(
            @Field("id_spp") String id_spp,
            @Field("nominal") String nominal);

    @FormUrlEncoded
    @POST("dbUpdateKelas.php")
    Call<KelasRepository> updateKelas(
            @Field("id_kelas") String id_kelas,
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan,
            @Field("angkatan") String angkatan);

    @FormUrlEncoded
    @POST("dbCreateSPP.php")
    Call<SPPRepository> createSPP(
            @Field("angkatan") String angkatan,
            @Field("tahun") String tahun,
            @Field("nominal") String nominal);

    @FormUrlEncoded
    @POST("dbCreateKelas.php")
    Call<KelasRepository> createKelas(
            @Field("angkatan") String angkatan,
            @Field("nama_kelas") String nama_kelas,
            @Field("jurusan") String jurusan);

    @GET("dbReadSPP.php")
    Call<SPPRepository> viewDataSPP();

    @GET("dbReadKelas.php")
    Call<KelasRepository> viewDataKelas();

    @GET("dbReadAllPetugas.php")
    Call<PetugasRepository> viewDataAllPetugas();
}
