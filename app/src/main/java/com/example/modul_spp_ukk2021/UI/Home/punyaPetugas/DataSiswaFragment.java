package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;

import java.util.ArrayList;

public class DataSiswaFragment extends Fragment implements DataSiswaAdapter.ItemClickListener {
    DataSiswaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_data_siswa, container, false);

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Gregorius Devon");
        animalNames.add("Fadlillah Bashir");
        animalNames.add("Ruqul Adam");
        animalNames.add("Christiany Kikyo");
        animalNames.add("Manggala Kagendra");
        animalNames.add("Kresna Mukti");

        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recyclerDataSiswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DataSiswaAdapter(getContext(), animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ScrollView scrollView = v.findViewById(R.id.scroll_datasiswa);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        return v;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(requireContext(), KonfirmasiPetugasActivity.class);
        startActivity(intent);
    }
}