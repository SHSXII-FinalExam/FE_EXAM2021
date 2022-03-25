package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static com.example.modul_spp_ukk2021.UI.Network.BaseURL.url;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.HomeSiswaActivity;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.Network.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePetugasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomePetugasAdapter adapter;
    private TextView nama, level;
    private List<Siswa> siswa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petugas_activity_home);

        nama = findViewById(R.id.nama);
        level = findViewById(R.id.level);
        EditText SearchSiswa = findViewById(R.id.searchSiswa);
        MaterialButton logout = findViewById(R.id.logoutPetugas);

        recyclerView = findViewById(R.id.recycler_siswa);
        adapter = new HomePetugasAdapter(this, siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        logout.setOnClickListener(v -> {
            Intent intent = new Intent(HomePetugasActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
        });

        SearchSiswa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    recyclerView.setVisibility(View.GONE);
                    String newText = s.toString().trim();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    APIEndPoints api = retrofit.create(APIEndPoints.class);
                    Call<SiswaRepository> call = api.searchDataSiswa(newText);
                    call.enqueue(new Callback<SiswaRepository>() {
                        @Override
                        public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                            String value = response.body().getValue();
                            List<Siswa> results = response.body().getResult();

                            recyclerView.setVisibility(View.VISIBLE);
                            if (value.equals("1")) {
                                siswa = response.body().getResult();
                                adapter = new HomePetugasAdapter(HomePetugasActivity.this, siswa);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<SiswaRepository> call, Throwable t) {
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });
                } else {
                    loadDataSiswa();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(HomePetugasActivity.this, LoginChoiceActivity.class);
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
        loadDataSiswa();
    }

    private void loadDataPetugas() {
        String username = getIntent().getStringExtra("username");
        String rank = getIntent().getStringExtra("level");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIEndPoints api = retrofit.create(APIEndPoints.class);
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
                    level.setText("Staff level: " + rank);
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
        APIEndPoints api = retrofit.create(APIEndPoints.class);
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
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}