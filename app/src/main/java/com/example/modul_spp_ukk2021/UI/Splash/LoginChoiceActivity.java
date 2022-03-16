package com.example.modul_spp_ukk2021.UI.Splash;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.HomeSiswaActivity;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.google.android.material.button.MaterialButton;

public class LoginChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        MaterialButton btnLoginStaff = findViewById(R.id.btn_staff);
        MaterialButton btnLoginSiswa = findViewById(R.id.btn_siswa);

        btnLoginSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, LoginSiswaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, LoginStaffActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
