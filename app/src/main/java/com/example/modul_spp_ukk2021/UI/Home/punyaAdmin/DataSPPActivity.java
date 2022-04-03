package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Repository.SPPRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class DataSPPActivity extends AppCompatActivity {

    private ApiEndPoints api;
    private DataSPPAdapter adapter;
    private RecyclerView recyclerView;
    private final List<SPP> spp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_data_spp);

        adapter = new DataSPPAdapter(DataSPPActivity.this, spp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DataSPPActivity.this);
        recyclerView = findViewById(R.id.recycler_spp);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        findViewById(R.id.tambahSPP).setOnClickListener(v -> {
            Intent intent = new Intent(DataSPPActivity.this, TambahSPPActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataSPP();
    }

    public void loadDataSPP() {
        Call<SPPRepository> call = api.viewDataSPP();
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<SPP> spp = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new DataSPPAdapter(DataSPPActivity.this, spp);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(DataSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(DataSPPActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
