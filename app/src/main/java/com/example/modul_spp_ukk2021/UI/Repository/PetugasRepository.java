package com.example.modul_spp_ukk2021.UI.Repository;

import com.example.modul_spp_ukk2021.UI.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Model.Siswa;

import java.util.List;

public class PetugasRepository {

    String value;
    String message;
    List<Petugas> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Petugas> getResult() {
        return result;
    }
}
