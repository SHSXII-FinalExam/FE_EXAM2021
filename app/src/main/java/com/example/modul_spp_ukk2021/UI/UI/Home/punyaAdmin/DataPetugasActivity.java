package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.DataSiswaAdapter;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.HomePetugasAdapter;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

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

public class DataPetugasActivity extends AppCompatActivity {

    final Context context = this;
    private TextView result;

    private DataPetugasAdapter adapter;
    private List<Petugas> petugas = new ArrayList<>();

    @BindView(R.id.dataPetugas)
    RecyclerView recyclerView;

    @BindView(R.id.tambah_petugas)
    MaterialCardView tambahPetugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_petugas);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.dataPetugas);
        adapter = new DataPetugasAdapter(this, petugas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(new DataPetugasAdapter(this, new ArrayList<Petugas>()));

        ScrollView scrollView = findViewById(R.id.scroll_datapetugas);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        tambahPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog_tambah_petugas, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Dialog dialg = (Dialog) dialog;

                                        final EditText username = (EditText) dialg.findViewById(R.id.username_petugas);
                                        final EditText password = (EditText) dialg.findViewById(R.id.password_petugas);
                                        final EditText nama_petugas = (EditText) dialg.findViewById(R.id.nama_petugas);
                                        final EditText level = (EditText) dialg.findViewById(R.id.level_petugas);

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(url)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                                        Call<PetugasRepository> call = api.createPetugas(username.getText().toString(), password.getText().toString(), nama_petugas.getText().toString(), level.getText().toString());
                                        call.enqueue(new Callback<PetugasRepository>() {
                                            @Override
                                            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                                                String value = response.body().getValue();
                                                String message = response.body().getMessage();
                                                if (value.equals("1")) {
                                                    loadDataPembayaran();
                                                    Toast.makeText(DataPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(DataPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                                                Log.e("DEBUG", "Error: ", t);
                                            }
                                        });

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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
        Call<PetugasRepository> call = api.viewDataPetugas();
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    petugas = response.body().getResult();
                    adapter = new DataPetugasAdapter(DataPetugasActivity.this, petugas);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
