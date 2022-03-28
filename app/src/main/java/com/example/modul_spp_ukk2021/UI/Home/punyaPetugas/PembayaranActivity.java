package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.InputFilterMinMax;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class PembayaranActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PembayaranAdapter adapter;
    private ProgressDialog progressbar;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        ImageView back = findViewById(R.id.back);
        ImageView refresh = findViewById(R.id.refresh);

        progressbar = new ProgressDialog(this);
        progressbar.setMessage("Loading...");
        progressbar.setCancelable(false);
        progressbar.setIndeterminate(true);
        progressbar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressbar.dismiss();

        recyclerView = findViewById(R.id.recyclerTagihanSiswa);
        adapter = new PembayaranAdapter(this, pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new PembayaranAdapter(this, new ArrayList<>()));
        runLayoutAnimation(recyclerView);

        adapter.setOnRecyclerViewItemClickListener((id_pembayaran, jumlah_bayar, nominal) -> {
            if (jumlah_bayar < nominal && jumlah_bayar > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                        Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, "0");
                        call.enqueue(new Callback<PembayaranRepository>() {
                            @Override
                            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                                String value = response.body().getValue();
                                if (value.equals("1")) {
                                    loadDataPembayaran();
                                }
                            }

                            @Override
                            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
                    }
                }, 500);
            } else {
                DialogUpdate(id_pembayaran, nominal);
            }
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });

        refresh.setOnClickListener(v -> {
            loadDataPembayaran();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void loadDataPembayaran() {
        progressbar.show();
        String nisnSiswa = this.getIntent().getStringExtra("nisnSiswa");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
                            progressbar.dismiss();
                            pembayaran = response.body().getResult();
                            adapter = new PembayaranAdapter(PembayaranActivity.this, pembayaran);
                            recyclerView.setAdapter(adapter);
                            runLayoutAnimation(recyclerView);
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        progressbar.dismiss();
                        Toast.makeText(PembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        }, 500);
    }

    private void DialogUpdate(String id_pembayaran, Integer nominal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PembayaranActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PembayaranActivity.this).inflate(R.layout.dialog_update_pembayaran, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        TextView maxInput = view.findViewById(R.id.maxInput);
        EditText jumlah_bayar = view.findViewById(R.id.jumlahBayar);
        final AlertDialog alertDialog = builder.create();

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        maxInput.setText("Max Input: " + format.format(nominal));
        jumlah_bayar.setFilters(new InputFilter[]{new InputFilterMinMax("0", nominal.toString())});

        view.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            alertDialog.dismiss();
                            loadDataPembayaran();
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}