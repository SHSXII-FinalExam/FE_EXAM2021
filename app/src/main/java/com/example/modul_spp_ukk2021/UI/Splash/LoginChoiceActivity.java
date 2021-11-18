package com.example.modul_spp_ukk2021.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.BottomNavigation;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.HomeSiswaActivity;
import com.google.android.material.button.MaterialButton;

public class LoginChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        MaterialButton btnLoginPetugas = findViewById(R.id.btn_petugas);
        MaterialButton btnLoginSiswa = findViewById(R.id.btn_siswa);

        btnLoginPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, BottomNavigation.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, HomeSiswaActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
