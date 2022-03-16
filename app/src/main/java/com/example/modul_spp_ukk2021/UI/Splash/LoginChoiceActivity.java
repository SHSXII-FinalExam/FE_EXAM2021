package com.example.modul_spp_ukk2021.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.google.android.material.button.MaterialButton;

public class LoginChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        MaterialButton btnLoginAdmin = findViewById(R.id.btn_admin);
        MaterialButton btnLoginSiswa = findViewById(R.id.btn_siswa);

        btnLoginSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, LoginSiswaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, LoginStaffActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
