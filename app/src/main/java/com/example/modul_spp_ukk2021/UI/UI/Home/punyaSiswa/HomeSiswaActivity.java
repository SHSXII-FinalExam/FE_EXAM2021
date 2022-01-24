package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class HomeSiswaActivity extends AppCompatActivity {

    private HomeSiswaAdapter adapter;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    @BindView(R.id.recyclerHomeSiswa)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_siswa);
        ButterKnife.bind(this);

        adapter = new HomeSiswaAdapter(this, pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ScrollView scrollView = findViewById(R.id.scroll_homesiswa);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
        MaterialButton logoutPetugas = findViewById(R.id.logoutSiswa);
        logoutPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeSiswaActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    public void getVariable() {
        int total_sum = 0;
        String nama = "";

        for (int i = 0; i < pembayaran.size(); i++) {
            Pembayaran food_items = pembayaran.get(i);
            int price = food_items.getNominal();
            nama = food_items.getNama();
            total_sum += price;
        }

        TextView nominal = findViewById(R.id.textView13);
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        nominal.setText(format.format(total_sum));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("totalTagihan", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("tagihanSiswa", total_sum);
        editor.apply();

        TextView Nama = findViewById(R.id.textView);
        if (nama.contains(" ")) {
            nama = nama.substring(0, nama.indexOf(" "));
            Nama.setText("Halo, " + nama);
        }

    }

    private void loadDataPembayaran() {
        String nisnSiswa = getIntent().getStringExtra("nisnSiswa");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PembayaranRepository> call = api.viewHistoryNISN(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                Log.e("value", value);
                if (value.equals("1")) {
                    pembayaran = response.body().getResult();
                    adapter = new HomeSiswaAdapter(HomeSiswaActivity.this, pembayaran);
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
