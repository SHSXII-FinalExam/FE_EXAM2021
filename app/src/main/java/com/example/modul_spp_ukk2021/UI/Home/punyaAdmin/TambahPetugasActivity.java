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
import com.example.modul_spp_ukk2021.UI.Repository.PetugasRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class TambahPetugasActivity extends AppCompatActivity {
    private Call<PetugasRepository> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_tambah_petugas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        TextView textView11 = findViewById(R.id.textView11);
        EditText nama_petugas = findViewById(R.id.tambah_Nama);
        EditText username_petugas = findViewById(R.id.tambah_Username);
        EditText password_petugas = findViewById(R.id.tambah_Password);
        String id_petugas = getIntent().getStringExtra("id_petugas");

        if (getIntent().hasExtra("id_petugas")) {
            textView11.setText("Edit Petugas");
            nama_petugas.setEnabled(false);
            username_petugas.setEnabled(false);
            nama_petugas.setText(getIntent().getStringExtra("nama_petugas"));
            username_petugas.setText(getIntent().getStringExtra("username_petugas"));
            password_petugas.setHint("New password");
        } else {
            findViewById(R.id.hapus).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nama_petugas.getText().toString().trim().length() > 0 && username_petugas.getText().toString().trim().length() > 0 && password_petugas.getText().toString().trim().length() > 0) {

                    if (getIntent().hasExtra("id_petugas")) {
                        call = api.updatePetugas(
                                id_petugas,
                                "Petugas",
                                username_petugas.getText().toString(),
                                password_petugas.getText().toString(),
                                nama_petugas.getText().toString());
                    } else {
                        call = api.createPetugas(
                                "Petugas",
                                username_petugas.getText().toString(),
                                password_petugas.getText().toString(),
                                nama_petugas.getText().toString());
                    }
                    call.enqueue(new Callback<PetugasRepository>() {
                        @Override
                        public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if (value.equals("1")) {
                                Toast.makeText(TambahPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            } else {
                                Toast.makeText(TambahPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<PetugasRepository> call, Throwable t) {
                            Toast.makeText(TambahPetugasActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });

                } else {
                    Toast.makeText(TambahPetugasActivity.this, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PetugasRepository> call = api.deletePetugas(id_petugas);
                call.enqueue(new Callback<PetugasRepository>() {
                    @Override
                    public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            Toast.makeText(TambahPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            Toast.makeText(TambahPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PetugasRepository> call, Throwable t) {
                        Toast.makeText(TambahPetugasActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
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
