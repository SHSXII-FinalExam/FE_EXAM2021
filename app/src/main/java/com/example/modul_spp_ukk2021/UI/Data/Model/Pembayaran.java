package com.example.modul_spp_ukk2021.UI.Data.Model;

import java.util.Date;

public class

Pembayaran {
    String id_pembayaran;
    String nama_petugas;
    String nisn;
    String nama;
    String nama_kelas;
    Date tgl_bayar;
    Integer nominal;
    String tahun_bayar;
    String bulan_bayar;

    public String getId_pembayaran() {
        return id_pembayaran;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public String getNisn() {
        return nisn;
    }

    public String getNama() {
        return nama;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public Date getTgl_bayar() {
        return tgl_bayar;
    }

    public Integer getNominal() {
        return nominal;
    }

    public String getTahun_bayar() {
        return tahun_bayar;
    }

    public String getBulan_bayar() {
        return bulan_bayar;
    }
}
