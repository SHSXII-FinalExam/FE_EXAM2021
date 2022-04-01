package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Home.Helper.InputFilterMinMax;
import com.example.modul_spp_ukk2021.UI.Repository.PembayaranRepository;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class PembayaranActivity extends AppCompatActivity {
    private ApiEndPoints api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petugas_activity_pembayaran);
        String id_petugas = getIntent().getStringExtra("id_petugas");
        String id_pembayaran = getIntent().getStringExtra("id_pembayaran");
        Integer nominal = getIntent().getIntExtra("nominal", 0);
        int kurang_bayar = getIntent().getIntExtra("kurang_bayar", 0);

        EditText nominal_bayar = findViewById(R.id.nominal);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        if (kurang_bayar == 0) {
            nominal_bayar.setHint("Nominal max. " + format.format(nominal));
            nominal_bayar.setFilters(new InputFilter[]{new InputFilterMinMax("0", nominal.toString())});
        } else {
            nominal_bayar.setHint("Nominal max. " + format.format(kurang_bayar));
            nominal_bayar.setFilters(new InputFilter[]{new InputFilterMinMax("0", Integer.toString(kurang_bayar))});
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.btn_simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, nominal_bayar.getText().toString(), id_petugas);
                call.enqueue(new Callback<PembayaranRepository>() {
                    @Override
                    public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        Toast.makeText(PembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });

        findViewById(R.id.btn_lunas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, nominal.toString(), id_petugas);
                call.enqueue(new Callback<PembayaranRepository>() {
                    @Override
                    public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        Toast.makeText(PembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });
    }
}
