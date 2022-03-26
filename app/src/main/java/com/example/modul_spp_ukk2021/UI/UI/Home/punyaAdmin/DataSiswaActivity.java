package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataSiswaActivity extends AppCompatActivity {
    private Integer id_spp;
    private DataSiswaAdapter adapter;
    private RecyclerView recyclerView;
    private List<Siswa> siswa = new ArrayList<>();
    private String id_kelas, id_petugas, nama_kelas, angkatan;

    public DataSiswaActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_data_siswa);
        SharedPreferences sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        id_petugas = sharedprefs.getString("idStaff", null);

        id_kelas = getIntent().getStringExtra("id_kelas");
        angkatan = getIntent().getStringExtra("angkatan");
        nama_kelas = getIntent().getStringExtra("nama_kelas");

        ImageView IvBack = findViewById(R.id.back);
        ImageView IvRefresh = findViewById(R.id.refresh);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataSiswa();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 500);
            }
        });

        adapter = new DataSiswaAdapter(this, siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_siswa);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        adapter.setOnRecyclerViewItemClickListener((nisn, refresh) -> {
            if (nisn != null && refresh == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<SiswaRepository> call = api.deleteSiswa(nisn);
                        call.enqueue(new Callback<SiswaRepository>() {
                            @Override
                            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                                String value = response.body().getValue();
                                if (value.equals("1")) {
                                    loadDataSiswa();
                                }
                            }

                            @Override
                            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
                    }
                }, 500);
            } else {
                loadDataSiswa();
            }
        });

        IvBack.setOnClickListener(v -> {
            onBackPressed();
        });

        IvRefresh.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(this, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_refresh, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_refresh) {
                        swipeRefreshLayout.setRefreshing(true);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                    loadDataSiswa();
                                }
                            }
                        }, 1000);
                    }
                    return true;
                }
            });
            popup.show();
        });

        HomeAdminActivity homeAdminActivity = new HomeAdminActivity();
        homeAdminActivity.setSiswaRefreshListener(new HomeAdminActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataSiswa();
            }
        });

        MaterialButton tambahSiswa = findViewById(R.id.tambah_siswa);
        tambahSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DataSiswaActivity.this, R.style.AlertDialogTheme);
                View view = LayoutInflater.from(DataSiswaActivity.this).inflate(R.layout.pa_dialog_tambah_siswa, findViewById(R.id.layoutDialogContainer));
                builder.setView(view);
                AlertDialog alertDialog = builder.create();

                EditText nama_siswa = view.findViewById(R.id.nama_siswa);
                EditText NISN_siswa = view.findViewById(R.id.NISN_siswa);
                EditText NIS_siswa = view.findViewById(R.id.NIS_siswa);
                TextView tvKelas = view.findViewById(R.id.tvKelas);
                Button spp_siswa = view.findViewById(R.id.spp_siswa);
                EditText alamat_siswa = view.findViewById(R.id.alamat_siswa);
                EditText ponsel_siswa = view.findViewById(R.id.ponsel_siswa);
                EditText password_siswa = view.findViewById(R.id.password_siswa);

                tvKelas.setText("Kelas                  : " + nama_kelas);

                spp_siswa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu dropDownMenu = new PopupMenu(DataSiswaActivity.this, spp_siswa);

                        Call<SPPRepository> call = api.viewDataSPPAngkatan(angkatan);
                        call.enqueue(new Callback<SPPRepository>() {
                            @Override
                            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();
                                List<SPP> results = response.body().getResult();

                                if (value.equals("1")) {
                                    for (int i = 0; i < results.size(); i++) {
                                        dropDownMenu.getMenu().add(Menu.NONE, results.get(i).getId_spp(), Menu.NONE, results.get(i).getTahun() + "[" + format.format(results.get(i).getNominal()) + "]");
                                    }
                                } else {
                                    Toast.makeText(DataSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                                }

                                dropDownMenu.show();
                            }

                            @Override
                            public void onFailure(Call<SPPRepository> call, Throwable t) {
                                Toast.makeText(DataSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });

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

                view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<SiswaRepository> call = api.createSiswa(
                                NISN_siswa.getText().toString(),
                                NIS_siswa.getText().toString(),
                                nama_siswa.getText().toString(),
                                id_kelas,
                                id_spp,
                                alamat_siswa.getText().toString(),
                                ponsel_siswa.getText().toString(),
                                password_siswa.getText().toString(),
                                id_petugas);
                        call.enqueue(new Callback<SiswaRepository>() {
                            @Override
                            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();

                                if (value.equals("1")) {
                                    loadDataSiswa();
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(DataSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
                    }
                });

                view.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
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
        loadDataSiswa();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadDataSiswa() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<SiswaRepository> call = api.viewDataSiswaKelas(id_kelas);
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    siswa = response.body().getResult();
                    adapter = new DataSiswaAdapter(DataSiswaActivity.this, siswa);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                Toast.makeText(DataSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
