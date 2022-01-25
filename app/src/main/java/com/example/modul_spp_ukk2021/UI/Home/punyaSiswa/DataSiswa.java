package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import java.util.Date;

public class DataSiswa {

    private String nama;
    private Date tgl_bayar;
    private Integer nominal;


    public DataSiswa(String nama, Integer nominal, Date tgl_bayar) {
        this.setNama(nama);
        this.setNominal(nominal);
        this.setTgl_bayar(tgl_bayar);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public Date getTgl_bayar() {
        return tgl_bayar;
    }

    public void setTgl_bayar(Date tgl_bayar) {
        this.tgl_bayar = tgl_bayar;
    }
}
