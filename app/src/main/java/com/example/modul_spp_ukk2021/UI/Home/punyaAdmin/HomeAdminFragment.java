package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.DataSiswaFragment;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.DataSiswa;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class HomeAdminFragment extends Fragment {
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home_admin, container, false);


        MaterialButton logoutAdmin = v.findViewById(R.id.logoutAdmin);
        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), LoginChoiceActivity.class);
                startActivity(intent);
            }
        });

        MaterialCardView dataSiswa =v.findViewById(R.id.dataSiswa);
        dataSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), DataSiswa.class);
                startActivity(intent);
            }
        });

        MaterialCardView dataPetugas = v.findViewById(R.id.dataPetugas);
        dataPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), DataPetugasActivity.class);
                startActivity(intent);
            }
        });

        MaterialCardView dataKelas = v.findViewById(R.id.dataKelas);
        dataKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), DataKelasActivity.class);
                startActivity(intent);
            }
        });

        MaterialCardView dataSPP = v.findViewById(R.id.dataSPP);
        dataSPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), DataSPPActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}