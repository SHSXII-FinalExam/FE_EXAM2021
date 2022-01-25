package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

public class DataSiswa2 {

    private String nama, nama_kelas;

    public DataSiswa2(String nama, String nama_kelas) {
        this.setNama(nama);
        this.setNama_kelas(nama_kelas);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String kelas) {
        this.nama_kelas = kelas;
    }
}
