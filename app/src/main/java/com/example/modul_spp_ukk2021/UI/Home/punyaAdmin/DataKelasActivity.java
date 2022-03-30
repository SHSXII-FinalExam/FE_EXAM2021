package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class DataKelasActivity extends AppCompatActivity {
    private APIEndPoints api;
    private DataKelasAdapter adapter;
    private RecyclerView recyclerView;
    private String nama_kelas, jurusan_kelas;
    private final List<Kelas> kelas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_data_kelas);

        adapter = new DataKelasAdapter(DataKelasActivity.this, kelas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DataKelasActivity.this);
        recyclerView = findViewById(R.id.recycler_kelas);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(APIEndPoints.class);

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataKelas();
    }

    public void loadDataKelas() {
        Call<KelasRepository> call = api.viewDataKelas();
        call.enqueue(new Callback<KelasRepository>() {
            @Override
            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Kelas> kelas = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new DataKelasAdapter(DataKelasActivity.this, kelas);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(DataKelasActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KelasRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(DataKelasActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
