package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.DataSiswaFragment;
import com.example.modul_spp_ukk2021.UI.Splash.AppCompatActivity;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class HomeAdminFragment extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_admin);

        MaterialButton logoutAdmin = logoutAdmin.findViewById();
        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminFragment.this, LoginChoiceActivity.class);
                fragment.startActivity();
            }
        });

        MaterialCardView dataSiswa = dataSiswa.findViewById();
        dataSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new DataSiswaFragment()).commit();
            }
        });

        MaterialCardView dataPetugas = dataPetugas.findViewById();
        dataPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminFragment.this, DataPetugasActivity.class);
                fragment.startActivity();
            }
        });

        MaterialCardView dataKelas = dataKelas.findViewById();
        dataKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminFragment.this, DataKelasActivity.class);
                fragment.startActivity();
            }
        });

        MaterialCardView dataSPP = dataSPP.findViewById();
        dataSPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAdminFragment.this, DataSPPActivity.class);
                fragment.startActivity();
            }
        });
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }

    private void setContentView(int fragment_home_admin) {

    }
}