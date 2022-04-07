package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Data.Model.Transaksi;

import java.util.List;

public class TransaksiRepository {
    String value;
    String message;
    List<Transaksi> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Transaksi> getResult() {
        return result;
    }
}
