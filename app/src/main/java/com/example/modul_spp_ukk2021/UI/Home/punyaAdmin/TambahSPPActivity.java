package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Repository.SPPRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class TambahSPPActivity extends AppCompatActivity {
    private Call<SPPRepository> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tambah_spp);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        TextView textView11 = findViewById(R.id.textView11);
        EditText tambah_Tahun = findViewById(R.id.tambah_Tahun);
        EditText tambah_Nominal = findViewById(R.id.tambah_Nominal);
        EditText tambah_Angkatan = findViewById(R.id.tambah_Angkatan);
        Integer id_spp = getIntent().getIntExtra("id_spp", 0);

        if (getIntent().hasExtra("id_spp")) {
            textView11.setText("Edit SPP");
            tambah_Tahun.setEnabled(false);
            tambah_Angkatan.setEnabled(false);
            tambah_Tahun.setText(getIntent().getStringExtra("tahun_spp"));
            tambah_Nominal.setText(getIntent().getStringExtra("nominal_spp"));
            tambah_Angkatan.setText(getIntent().getStringExtra("angkatan_spp"));
        } else {
            findViewById(R.id.hapus).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tambah_Tahun.getText().toString().trim().length() > 0 && tambah_Nominal.getText().toString().trim().length() > 0 && tambah_Angkatan.getText().toString().trim().length() > 0) {

                    if (getIntent().hasExtra("id_spp")) {
                        call = api.updateSPP(
                                id_spp,
                                tambah_Nominal.getText().toString());
                    } else {
                        call = api.createSPP(
                                tambah_Angkatan.getText().toString(),
                                tambah_Tahun.getText().toString(),
                                tambah_Nominal.getText().toString());
                    }
                    call.enqueue(new Callback<SPPRepository>() {
                        @Override
                        public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if (value.equals("1")) {
                                Toast.makeText(TambahSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            } else {
                                Toast.makeText(TambahSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SPPRepository> call, Throwable t) {
                            Toast.makeText(TambahSPPActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });

                } else {
                    Toast.makeText(TambahSPPActivity.this, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SPPRepository> call = api.deleteSPP(id_spp);
                call.enqueue(new Callback<SPPRepository>() {
                    @Override
                    public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            Toast.makeText(TambahSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(TambahSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SPPRepository> call, Throwable t) {
                        Toast.makeText(TambahSPPActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });

        findViewById(R.id.textView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
