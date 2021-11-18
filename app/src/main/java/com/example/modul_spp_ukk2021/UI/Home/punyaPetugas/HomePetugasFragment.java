package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HomePetugasFragment extends Fragment implements HomePetugasAdapter.ItemClickListener {
    HomePetugasAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home_petugas, container, false);

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Gregorius Devon");
        animalNames.add("Fadlillah Bashir");
        animalNames.add("Ruqul Adam");
        animalNames.add("Christiany Kikyo");
        animalNames.add("Manggala Kagendra");
        animalNames.add("Kresna Mukti");

        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recyclerHomePetugas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomePetugasAdapter(getContext(), animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ScrollView scrollView = v.findViewById(R.id.scroll_homepetugas);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        MaterialButton logoutPetugas = v.findViewById(R.id.logoutPetugas);
        logoutPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), LoginChoiceActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
    }
}