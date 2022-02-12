package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.google.android.material.card.MaterialCardView;

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

public class DataKelasActivity extends AppCompatActivity {

    final Context context = this;
    private TextView result;

    private DataKelasAdapter adapter;
    private List<Kelas> kelas = new ArrayList<>();

    @BindView(R.id.recycler_kelas)
    RecyclerView recyclerView;

    @BindView(R.id.tambah_spp)
    MaterialCardView tambahPetugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_data_kelas);
        ButterKnife.bind(this);

        adapter = new DataKelasAdapter(this, kelas);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new DataKelasAdapter(this, new ArrayList<Kelas>()));
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
        Call<KelasRepository> call = api.viewDataKelas();
        call.enqueue(new Callback<KelasRepository>() {
            @Override
            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    kelas = response.body().getResult();
                    adapter = new DataKelasAdapter(DataKelasActivity.this, kelas);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<KelasRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
