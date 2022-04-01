package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class TambahSiswaActivity extends AppCompatActivity {
    private Integer id_spp;
    private Call<SiswaRepository> call;
    private String nisn, id_petugas, id_kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tambah_siswa);

        nisn = getIntent().getStringExtra("NISN_siswa");
        id_kelas = getIntent().getStringExtra("id_kelas");
        id_petugas = getIntent().getStringExtra("id_petugas");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIEndPoints api = retrofit.create(APIEndPoints.class);

        TextView textTitle = findViewById(R.id.textView11);
        EditText nama_siswa = findViewById(R.id.tambah_SiswaNama);
        EditText NISN_siswa = findViewById(R.id.tambah_SiswaNISN);
        EditText NIS_siswa = findViewById(R.id.tambah_SiswaNIS);
        EditText tvKelas = findViewById(R.id.tambah_SiswaKelas);
        Button spp_siswa = findViewById(R.id.pilihSPP);
        EditText alamat_siswa = findViewById(R.id.tambah_SiswaAlamat);
        EditText ponsel_siswa = findViewById(R.id.tambah_SiswaTelp);
        EditText password_siswa = findViewById(R.id.tambah_SiswaPass);

        tvKelas.setEnabled(false);
        tvKelas.setText(getIntent().getStringExtra("nama_kelas"));

        if (getIntent().hasExtra("NISN_siswa")) {
            NISN_siswa.setEnabled(false);
            NIS_siswa.setEnabled(false);

            textTitle.setText("Edit Siswa");
            nama_siswa.setText(getIntent().getStringExtra("nama_siswa"));
            NISN_siswa.setText(nisn);
            NIS_siswa.setText(getIntent().getStringExtra("NIS_siswa"));
            alamat_siswa.setText(getIntent().getStringExtra("alamat_siswa"));
            ponsel_siswa.setText(getIntent().getStringExtra("ponsel_siswa"));
            password_siswa.setHint("New password");

        } else {
            findViewById(R.id.hapus).setVisibility(View.INVISIBLE);
        }

        PopupMenu dropDownMenu = new PopupMenu(TambahSiswaActivity.this, spp_siswa);
        Call<SPPRepository> call2 = api.viewDataSPPAngkatan(getIntent().getStringExtra("angkatan"));
        call2.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<SPP> results = response.body().getResult();

                Locale localeID = new Locale("in", "ID");
                NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                format.setMaximumFractionDigits(0);

                if (value.equals("1")) {
                    for (int i = 0; i < results.size(); i++) {
                        dropDownMenu.getMenu().add(Menu.NONE, results.get(i).getId_spp(), Menu.NONE, results.get(i).getTahun() + " (" + format.format(results.get(i).getNominal()) + ")");
                    }

                } else {
                    spp_siswa.setEnabled(false);
                    spp_siswa.setText("SPP Kosong");
                    Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                Toast.makeText(TambahSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });

        spp_siswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        spp_siswa.setText(menuItem.getTitle().toString());
                        id_spp = menuItem.getItemId();
                        return true;
                    }
                });
            }
        });

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_spp != null && password_siswa.getText().toString().trim().length() > 0) {

                    if (getIntent().hasExtra("NISN_siswa")) {
                        call = api.updateSiswa(
                                NISN_siswa.getText().toString(),
                                nama_siswa.getText().toString(),
                                id_kelas,
                                id_spp,
                                alamat_siswa.getText().toString(),
                                ponsel_siswa.getText().toString(),
                                password_siswa.getText().toString(),
                                id_petugas);
                    } else {
                        call = api.createSiswa(
                                NISN_siswa.getText().toString(),
                                NIS_siswa.getText().toString(),
                                nama_siswa.getText().toString(),
                                id_kelas,
                                id_spp,
                                alamat_siswa.getText().toString(),
                                ponsel_siswa.getText().toString(),
                                password_siswa.getText().toString(),
                                id_petugas);
                    }
                    call.enqueue(new Callback<SiswaRepository>() {
                        @Override
                        public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if (value.equals("1")) {
                                Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            } else {
                                Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SiswaRepository> call, Throwable t) {
                            Toast.makeText(TambahSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });

                } else {
                    Toast.makeText(TambahSiswaActivity.this, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SiswaRepository> call = api.deleteSiswa(nisn);
                call.enqueue(new Callback<SiswaRepository>() {
                    @Override
                    public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SiswaRepository> call, Throwable t) {
                        Toast.makeText(TambahSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
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
