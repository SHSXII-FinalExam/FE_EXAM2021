package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HomeSiswaActivity extends AppCompatActivity implements HomeSiswaAdapter.ItemClickListener {
    HomeSiswaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_siswa);

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Alfian Nurditariyono");
        animalNames.add("Alfian Nurditariyono");
        animalNames.add("Alfian Nurditariyono");
        animalNames.add("Alfian Nurditariyono");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerHomeSiswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomeSiswaAdapter(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ScrollView scrollView = findViewById(R.id.scroll_homesiswa);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        MaterialButton logoutPetugas = findViewById(R.id.logoutSiswa);
        logoutPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeSiswaActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}
