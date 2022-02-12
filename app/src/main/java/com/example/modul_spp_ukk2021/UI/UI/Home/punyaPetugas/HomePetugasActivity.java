package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class HomePetugasActivity extends AppCompatActivity {
    private HomePetugasAdapter adapter;
    private List<Siswa> siswa = new ArrayList<>();
    private TextView tagihan_count, nama;

    @BindView(R.id.recyclerDataSiswa)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pp_activity_home);
        ButterKnife.bind(this);

        nama = findViewById(R.id.nama);
        tagihan_count = findViewById(R.id.dataSiswa_count);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerDataSiswa);
        adapter = new HomePetugasAdapter(this, siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        MaterialButton logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomePetugasActivity.this, logout, ViewCompat.getTransitionName(logout));
                Intent intent = new Intent(HomePetugasActivity.this, LoginPetugasActivity.class);
                startActivity(intent, options.toBundle());
                finish();
            }
        });

        EditText SearchSiswa = (EditText) findViewById(R.id.searchSiswa);
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
                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
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

                                int i = 0;
                                for (i = 0; i < results.size(); i++) {
                                    String a = results.get(i).getNisn();
                                }
                                tagihan_count.setText("(" + String.valueOf(i) + ")");
                            }
                        }

                        @Override
                        public void onFailure(Call<SiswaRepository> call, Throwable t) {
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });
                } else {
                    loadDataPembayaran();
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
        loadDataPembayaran();
    }

    private void loadDataPembayaran() {
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

                    int i = 0;
                    String getNama = "";
                    for (i = 0; i < results.size(); i++) {
                        String a = results.get(i).getNisn();
                        getNama = results.get(i).getNama();
                    }
                    nama.setText(getNama);
                    tagihan_count.setText("(" + String.valueOf(i) + ")");
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}