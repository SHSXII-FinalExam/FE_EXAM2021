package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class DataSiswaActivity extends AppCompatActivity {
    private String id_kelas;
    private APIEndPoints api;
    private DataSiswaAdapter adapter;
    private RecyclerView recyclerView;
    private final List<Siswa> siswa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_data_siswa);

        EditText SearchSiswa = findViewById(R.id.searchSiswa);
        id_kelas = getIntent().getStringExtra("id_kelas");

        adapter = new DataSiswaAdapter(this, siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_siswa);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(APIEndPoints.class);

        findViewById(R.id.tambahSiswa).setOnClickListener(v -> {
            Intent intent = new Intent(DataSiswaActivity.this, TambahSiswaActivity.class);
            intent.putExtra("id_petugas", getIntent().getStringExtra("id_petugas"));
            intent.putExtra("id_kelas", getIntent().getStringExtra("id_kelas"));
            intent.putExtra("angkatan", getIntent().getStringExtra("angkatan"));
            intent.putExtra("nama_kelas", getIntent().getStringExtra("nama_kelas"));
            startActivity(intent);
        });

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SearchSiswa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    Call<SiswaRepository> call = api.searchDataSiswa(s.toString().trim());
                    call.enqueue(new Callback<SiswaRepository>() {
                        @Override
                        public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                            String value = response.body().getValue();
                            List<Siswa> results = response.body().getResult();

                            if (value.equals("1")) {
                                recyclerView.setVisibility(View.VISIBLE);
                                adapter = new DataSiswaAdapter(DataSiswaActivity.this, results);
                                recyclerView.setAdapter(adapter);
                            } else {
                                recyclerView.setVisibility(View.GONE);
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
    public void onResume() {
        super.onResume();
        loadDataSiswa();
    }

    public void loadDataSiswa() {
        Call<SiswaRepository> call = api.viewDataSiswaKelas(id_kelas);
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Siswa> siswa = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new DataSiswaAdapter(DataSiswaActivity.this, siswa);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(DataSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(DataSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
