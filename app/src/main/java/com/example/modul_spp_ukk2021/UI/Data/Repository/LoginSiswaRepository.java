package com.example.modul_spp_ukk2021.UI.Data.Repository;

import com.example.modul_spp_ukk2021.UI.Data.Model.Login;

import java.util.List;

public class LoginSiswaRepository {
    String value;
    String message;
    List<Login> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<Login> getResult() {
        return result;
    }
}