package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Repository.KelasRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class TambahKelasActivity extends AppCompatActivity {
    private Call<KelasRepository> call;
    private String nama, jurusan_kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tambah_kelas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        TextView textTitle = findViewById(R.id.textView11);
        Button nama_kelas = findViewById(R.id.pilihKelas);
        EditText angkatan = findViewById(R.id.inputAngkatan);
        RadioGroup jurusan = findViewById(R.id.jurusan_kelas);
        EditText namanomor = findViewById(R.id.inputKelas);
        TextView namajurusan = findViewById(R.id.textView21);

        String id_kelas = getIntent().getStringExtra("id_kelas");
        String jurusanCheck = getIntent().getStringExtra("jurusan");

        if (getIntent().hasExtra("id_kelas")) {
            String substringNama = getIntent().getStringExtra("nama_kelas");
            nama = substringNama.substring(0, substringNama.indexOf(' '));
            nama_kelas.setText(nama);
            textTitle.setText("Edit Kelas");
            angkatan.setText(getIntent().getStringExtra("angkatan"));
            namajurusan.setText(getIntent().getStringExtra("jurusan"));
            namanomor.setText(getIntent().getStringExtra("nama_kelas").replaceAll("[^0-9]", ""));

            if (jurusanCheck.equalsIgnoreCase("RPL")) {
                jurusan_kelas = "RPL";
                jurusan.check(R.id.RPL);
            } else if (jurusanCheck.equalsIgnoreCase("TKJ")) {
                jurusan_kelas = "TKJ";
                jurusan.check(R.id.TKJ);
            }

        } else {
            findViewById(R.id.hapus).setVisibility(View.INVISIBLE);
        }

        jurusan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.RPL:
                        jurusan_kelas = "RPL";
                        namajurusan.setText(jurusan_kelas);
                        break;
                    case R.id.TKJ:
                        jurusan_kelas = "TKJ";
                        namajurusan.setText(jurusan_kelas);
                        break;
                    default:
                        jurusan_kelas = null;
                }
            }
        });

        nama_kelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownMenu = new PopupMenu(TambahKelasActivity.this, nama_kelas);
                dropDownMenu.getMenuInflater().inflate(R.menu.kelas_menu, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        nama_kelas.setText(menuItem.getTitle().toString());
                        nama = menuItem.getTitle().toString();
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nama != null && jurusan_kelas != null && angkatan.getText().toString().trim().length() > 0) {

                    if (getIntent().hasExtra("id_kelas")) {
                        call = api.updateKelas(id_kelas,
                                nama + " " + jurusan_kelas + " " + namanomor.getText().toString(),
                                jurusan_kelas,
                                angkatan.getText().toString());
                    } else {
                        call = api.createKelas(angkatan.getText().toString(),
                                nama + " " + jurusan_kelas + " " + namanomor.getText().toString(),
                                jurusan_kelas.trim());
                    }
                    call.enqueue(new Callback<KelasRepository>() {
                        @Override
                        public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if (value.equals("1")) {
                                Toast.makeText(TambahKelasActivity.this, message, Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            } else {
                                Toast.makeText(TambahKelasActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<KelasRepository> call, Throwable t) {
                            Toast.makeText(TambahKelasActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });

                } else {
                    Toast.makeText(TambahKelasActivity.this, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<KelasRepository> call = api.deleteKelas(id_kelas);
                call.enqueue(new Callback<KelasRepository>() {
                    @Override
                    public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            Toast.makeText(TambahKelasActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(TambahKelasActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KelasRepository> call, Throwable t) {
                        Toast.makeText(TambahKelasActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
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
