package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

public class DataSpp {
    private String tahun, nominal;

    public DataSpp(String tahun, String nominal){
        this.setTahun(tahun);
        this.setNominal(nominal);
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
