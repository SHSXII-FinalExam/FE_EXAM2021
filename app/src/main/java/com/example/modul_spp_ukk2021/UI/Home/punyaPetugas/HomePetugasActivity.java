package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HomePetugasActivity extends AppCompatActivity implements HomePetugasAdapter.ItemClickListener {
    HomePetugasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petugas_home_activity);

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Gregorius Devon");
        animalNames.
                add("Fadlillah Bashir");
        animalNames.add("Ruqul Adam");
        animalNames.add("Christiany Kikyo");
        animalNames.add("Manggala Kagendra");
        animalNames.add("Kresna Mukti");

        //set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerHomePetugas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomePetugasAdapter(HomePetugasActivity.this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

//        ScrollView scrollView = v.findViewById(R.id.scroll_homepetugas);
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.scrollTo(0, 0);
//            }
//        });

        MaterialButton logoutPetugas = v.findViewById(R.id.logoutPetugas);
        logoutPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePetugasActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
            }
        });

//        MaterialButton pembayaran = v.findViewById(R.id.pembayaran);
//        pembayaran.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = requireActivity(), DataSiswaFragment.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(HomePetugasActivity.this, "You clicked " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}