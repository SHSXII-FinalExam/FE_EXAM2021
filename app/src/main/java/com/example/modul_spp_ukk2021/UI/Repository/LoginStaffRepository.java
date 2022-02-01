package com.example.modul_spp_ukk2021.UI.Repository;

import com.example.modul_spp_ukk2021.UI.Model.LoginSiswa;
import com.example.modul_spp_ukk2021.UI.Model.LoginStaff;

import java.util.List;

public class LoginStaffRepository {
    String value;
    String message;
    List<LoginStaff> result;

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public List<LoginStaff> getResult() {
        return result;
    }
}
