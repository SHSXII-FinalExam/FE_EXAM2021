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
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class DataPetugasActivity extends AppCompatActivity {
    private APIEndPoints api;
    private RecyclerView recyclerView;
    private DataPetugasAdapter adapter;
    private final List<Petugas> petugas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_data_petugas);

        adapter = new DataPetugasAdapter(DataPetugasActivity.this, petugas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DataPetugasActivity.this);
        recyclerView = findViewById(R.id.recycler_petugas);
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
        loadDataPetugas();
    }

    public void loadDataPetugas() {
        Call<PetugasRepository> call = api.viewDataAllPetugas();
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Petugas> petugas = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new DataPetugasAdapter(DataPetugasActivity.this, petugas);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(DataPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(DataPetugasActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}