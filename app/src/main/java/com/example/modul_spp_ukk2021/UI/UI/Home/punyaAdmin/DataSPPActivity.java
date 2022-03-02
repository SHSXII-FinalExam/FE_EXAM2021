package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class DataSPPActivity extends AppCompatActivity {

    final Context context = this;
    private TextView result;

    private DataSPPAdapter adapter;
    private List<SPP> spp = new ArrayList<>();

    RecyclerView recyclerView;

    MaterialCardView tambahPetugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_data_spp);

        recyclerView = findViewById(R.id.recycler_spp);
        adapter = new DataSPPAdapter(this, spp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(new DataSPPAdapter(this, new ArrayList<SPP>()));

        ScrollView scrollView = findViewById(R.id.scroll_dataspp);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        tambahPetugas = findViewById(R.id.tambah_petugas);
        tambahPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.pa_dialog_tambah_spp, null);

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

                                        final EditText angkatan = (EditText) dialg.findViewById(R.id.angkatan_spp);
                                        final EditText tahun = (EditText) dialg.findViewById(R.id.tahun_spp);
                                        final EditText nominal = (EditText) dialg.findViewById(R.id.nominal_spp);

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(url)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                                        Call<SPPRepository> call = api.createSPP(angkatan.getText().toString(), tahun.getText().toString(), nominal.getText().toString());
                                        call.enqueue(new Callback<SPPRepository>() {
                                            @Override
                                            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                                                String value = response.body().getValue();
                                                String message = response.body().getMessage();
                                                if (value.equals("1")) {
                                                    loadDataPembayaran();
                                                    Toast.makeText(DataSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(DataSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<SPPRepository> call, Throwable t) {
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
        Call<SPPRepository> call = api.viewDataSPP();
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    spp = response.body().getResult();
                    adapter = new DataSPPAdapter(DataSPPActivity.this, spp);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
