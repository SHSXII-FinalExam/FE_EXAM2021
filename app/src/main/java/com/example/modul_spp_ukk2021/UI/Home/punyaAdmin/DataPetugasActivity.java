package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
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

public class DataPetugasActivity extends AppCompatActivity {

    final Context context = this;
    private TextView result;

    private DataPetugasAdapter adapter;
    private List<Petugas> petugas = new ArrayList<>();

    @BindView(R.id.recycler_petugas)
    RecyclerView recyclerView;

    @BindView(R.id.tambahPetugas)
    MaterialCardView tambahPetugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_data_petugas);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_petugas);
        adapter = new DataPetugasAdapter(this, petugas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(new DataPetugasAdapter(this, new ArrayList<Petugas>()));


//        tambahPetugas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // get prompts.xml view
//                LayoutInflater li = LayoutInflater.from(context);
//                View promptsView = li.inflate(R.layout., null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);
//
//                // set dialog message
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        Dialog dialg = (Dialog) dialog;
//
//                                        final EditText username = (EditText) dialg.findViewById(R.id.username_petugas);
//                                        final EditText password = (EditText) dialg.findViewById(R.id.password_petugas);
//                                        final EditText nama_petugas = (EditText) dialg.findViewById(R.id.nama_petugas);
//                                        final EditText level = (EditText) dialg.findViewById(R.id.level_petugas);
//
//                                        Retrofit retrofit = new Retrofit.Builder()
//                                                .baseUrl(url)
//                                                .addConverterFactory(GsonConverterFactory.create())
//                                                .build();
//                                        APIEndPoints api = retrofit.create(APIEndPoints.class);
//                                        Call<PetugasRepository> call = api.createStaff(username.getText().toString(), password.getText().toString(), nama_petugas.getText().toString(), level.getText().toString());
//                                        call.enqueue(new Callback<PetugasRepository>() {
//                                            @Override
//                                            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
//                                                String value = response.body().getValue();
//                                                String message = response.body().getMessage();
//                                                if (value.equals("1")) {
//                                                    loadDataPembayaran();
//                                                    Toast.makeText(DataPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(DataPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<PetugasRepository> call, Throwable t) {
//                                                Log.e("DEBUG", "Error: ", t);
//                                            }
//                                        });
//
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();
//            }
//        });

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
        APIEndPoints api = retrofit.create(APIEndPoints.class);
        Call<PetugasRepository> call = api.viewPetugas();
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