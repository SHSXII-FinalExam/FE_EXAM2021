package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;

import java.util.List;

public class SPPRepository {
    String value;
    String message;
    List<SPP> result;

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
