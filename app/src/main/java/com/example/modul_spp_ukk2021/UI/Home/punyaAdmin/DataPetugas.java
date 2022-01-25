package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

public class DataPetugas {

    private String nama_petugas, level, jam_kerja;

    public DataPetugas(String nama_petugas, String level, String jam_kerja){
        this.setNama_petugas(nama_petugas);
        this.setLevel(level);
        this.setJam_kerja(jam_kerja);

    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public void setNama_petugas(String nama_petugas) {
        this.nama_petugas = nama_petugas;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getJam_kerja() {
        return jam_kerja;
    }

    public void setJam_kerja(String jam_kerja) {
        this.jam_kerja = jam_kerja;
    }
}
