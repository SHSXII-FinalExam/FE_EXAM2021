package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Helper.InputFilterMinMax;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
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

public class PembayaranActivity extends AppCompatActivity {

    TextView Nama;
    private PembayaranAdapter adapter;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    @BindView(R.id.recyclerTagihanSiswa)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pp_activity_pembayaran);
        ButterKnife.bind(this);

        Nama = findViewById(R.id.textView);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerTagihanSiswa);
        adapter = new PembayaranAdapter(this, pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new PembayaranAdapter(this, new ArrayList<Pembayaran>()));

        // Interface implementation.
        adapter.setOnRecyclerViewItemClickListener(new PembayaranAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(String id_pembayaran, Integer jumlah_bayar, Integer nominal) {
                if (jumlah_bayar < nominal && jumlah_bayar > 0) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                    Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, nominal.toString());
                    call.enqueue(new Callback<PembayaranRepository>() {
                        @Override
                        public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                            String value = response.body().getValue();
                            if (value.equals("1")) {
                                loadPembayaran();
                            }
                        }

                        @Override
                        public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });
                } else {
                    DialogUpdate(id_pembayaran, nominal.toString());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPembayaran();
    }

    private void loadPembayaran() {
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
                    adapter = new PembayaranAdapter(PembayaranActivity.this, pembayaran);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    private void DialogUpdate(String id_pembayaran, String nominal) {
        dialog = new AlertDialog.Builder(PembayaranActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.pp_dialog_update_pembayaran, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final EditText jumlah_bayar = (EditText) dialogView.findViewById(R.id.jumlahBayar);
        jumlah_bayar.setFilters(new InputFilter[]{ new InputFilterMinMax("0", nominal)});
        final MaterialButton txt_usia = (MaterialButton) dialogView.findViewById(R.id.submit);

        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, jumlah_bayar.getText().toString());
                call.enqueue(new Callback<PembayaranRepository>() {
                    @Override
                    public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                        String value = response.body().getValue();
                        if (value.equals("1")) {
                            loadPembayaran();
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}