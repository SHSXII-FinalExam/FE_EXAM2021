package com.example.modul_spp_ukk2021.UI.Repository;

import com.example.modul_spp_ukk2021.UI.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Model.SPP;

import java.util.List;

public class KelasRepository {
    String value;
    String message;
    List<Kelas> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Kelas> getResult() {
        return result;
    }
}