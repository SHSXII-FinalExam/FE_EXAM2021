package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.HomeSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
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

    private TextView Nama;
    private HomePetugasAdapter adapter;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    @BindView(R.id.recyclerTagihanSiswa)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pp_activity_datasiswa);
        ButterKnife.bind(this);

        Nama = findViewById(R.id.textView);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerTagihanSiswa);
        adapter = new HomePetugasAdapter(this, pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new HomePetugasAdapter(this, new ArrayList<Pembayaran>()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    private void loadDataPembayaran() {
        String nisnSiswa = this.getIntent().getStringExtra("nisnSiswa");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PembayaranRepository> call = api.viewPembayaran(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();

                if (value.equals("1")) {
                    pembayaran = response.body().getResult();
                    adapter = new HomePetugasAdapter(HomePetugasActivity.this, pembayaran);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}