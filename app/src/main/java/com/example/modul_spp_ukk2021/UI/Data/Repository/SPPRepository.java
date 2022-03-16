package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;

import java.util.List;

public class SPPRepository {
    String value, message;
    List <SPP> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<SPP> getResult() {
        return result;
    }
}
