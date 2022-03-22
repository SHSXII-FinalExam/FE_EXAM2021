package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Network.LoginStaf;

import java.util.List;

public class LoginStafRepository {
    String value;
    String message;
    List<LoginStaf> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<LoginStaf> getResult() {
        return result;
    }
}
