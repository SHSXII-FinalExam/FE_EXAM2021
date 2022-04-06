package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class HomeAdminActivity extends AppCompatActivity {
    private ApiEndPoints api;
    private TextView nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_activity);

        nama = findViewById(R.id.nama);
        MaterialButton logout = findViewById(R.id.logoutAdmin);
        MaterialCardView dataSPP = findViewById(R.id.dataSPP);
        MaterialCardView dataKelas = findViewById(R.id.dataKelas);
        MaterialCardView dataPetugas = findViewById(R.id.dataPetugas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        dataSPP.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, DataSPPActivity.class);
            startActivity(intent);
        });

        dataKelas.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, DataKelasActivity.class);
            intent.putExtra("id_petugas", getIntent().getStringExtra("id_petugas"));
            startActivity(intent);
        });
        dataPetugas.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, DataPetugasActivity.class);
            intent.putExtra("id_petugas", getIntent().getStringExtra("id_petugas"));
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(HomeAdminActivity.this, LoginChoiceActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPetugas();
    }

    private void loadDataPetugas() {
        String username = getIntent().getStringExtra("username");

        Call<PetugasRepository> call = api.viewPetugas(username);
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                String value = response.body().getValue();
                List<Petugas> results = response.body().getResult();

                if (value.equals("1")) {
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
}
