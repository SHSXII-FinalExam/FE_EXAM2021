package com.example.modul_spp_ukk2021.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaAdmin.LoginAdminActivity;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.LoginPetugasActivity;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.google.android.material.button.MaterialButton;

public class LoginChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        MaterialButton btnLoginAdmin = findViewById(R.id.btn_admin);
        MaterialButton btnLoginPetugas = findViewById(R.id.btn_petugas);
        MaterialButton btnLoginSiswa = findViewById(R.id.btn_siswa);

        btnLoginSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginChoiceActivity.this, LoginSiswaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, LoginPetugasActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginChoiceActivity.this, LoginAdminActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void finish() {

    }

    private void startActivity(Intent intent) {

    }

    private MaterialButton findViewById(int btn_admin) {
        
    }

    private void setContentView(int activity_login_choice) {
        
    }
}
