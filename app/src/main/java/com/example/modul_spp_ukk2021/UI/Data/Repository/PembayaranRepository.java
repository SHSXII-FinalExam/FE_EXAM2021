package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;

import java.util.List;

public class PembayaranRepository {
    String value;
    String message;
    List<Pembayaran> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Pembayaran> getResult() {
        return result;
    }
}