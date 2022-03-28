package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomePetugasActivity extends AppCompatActivity {
    private MaterialButton logout;
    private RecyclerView recyclerView;
    private HomePetugasAdapter adapter;
    private TextView tagihan_count, nama, level;
    private List<Siswa> siswa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_petugas);

        logout = findViewById(R.id.logoutPetugas);
        nama = findViewById(R.id.textView);
        EditText SearchSiswa = findViewById(R.id.searchSiswa);

        recyclerView = findViewById(R.id.recyclerHomePetugas);
        adapter = new HomePetugasAdapter(this, siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        logout.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomePetugasActivity.this, logout, ViewCompat.getTransitionName(logout));
            Intent intent = new Intent(HomePetugasActivity.this, LoginPetugasActivity.class);
            startActivity(intent, options.toBundle());
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        loadDataPetugas();
        loadDataSiswa();
    }

    private void loadDataPetugas() {
        String username = getIntent().getStringExtra("username");
        String rank = getIntent().getStringExtra("level");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PetugasRepository> call = api.viewDataPetugas(username);
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                String value = response.body().getValue();
                List<Petugas> results = response.body().getResult();

                if (value.equals("1")) {
                    adapter = new HomePetugasAdapter(HomePetugasActivity.this, siswa);
                    recyclerView.setAdapter(adapter);

                    String nama_petugas = "";
                    for (int i = 0; i < results.size(); i++) {
                        nama_petugas = results.get(i).getNama_petugas();
                    }
                    nama.setText(nama_petugas);
                }
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
    private void loadDataSiswa() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<SiswaRepository> call = api.viewDataSiswa();
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                String value = response.body().getValue();
                List<Siswa> results = response.body().getResult();

                if (value.equals("1")) {
                    siswa = response.body().getResult();
                    adapter = new HomePetugasAdapter(HomePetugasActivity.this, siswa);
                    recyclerView.setAdapter(adapter);

                    int i;
                    for (i = 0; i < results.size(); i++) {
                        String a = results.get(i).getNisn();
                    }

                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

}