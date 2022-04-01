package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class HomeAdminActivity extends AppCompatActivity {
    private APIEndPoints api;
    private TextView nama, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_home);

        nama = findViewById(R.id.nama);
        level = findViewById(R.id.level);
        MaterialButton logout = findViewById(R.id.logoutAdmin);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        ConstraintLayout constraintLayout2 = findViewById(R.id.constraintLayout2);
        ConstraintLayout constraintLayout3 = findViewById(R.id.constraintLayout3);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(APIEndPoints.class);

        constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, DataSPPActivity.class);
            startActivity(intent);
        });

        constraintLayout2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, DataPetugasActivity.class);
            intent.putExtra("id_petugas", getIntent().getStringExtra("id_petugas"));
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        });

        constraintLayout3.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, DataKelasActivity.class);
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
        String rank = getIntent().getStringExtra("level");
        String username = getIntent().getStringExtra("username");

        Call<PetugasRepository> call = api.viewDataPetugas(username);
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
                    level.setText("Staff level: " + rank);
                }
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}