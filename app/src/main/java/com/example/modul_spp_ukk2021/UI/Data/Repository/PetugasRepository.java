package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;

import java.util.List;

public class PetugasRepository {
    String value, message;
    List <Petugas> result;

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
