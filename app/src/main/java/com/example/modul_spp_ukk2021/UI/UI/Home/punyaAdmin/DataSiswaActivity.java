package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.HomeSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataSiswaActivity extends AppCompatActivity {
    private DataSiswaAdapter adapter;
    private RecyclerView recyclerView;
    private List<Siswa> siswa = new ArrayList<>();

    public DataSiswaActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_data_siswa);

        ImageView IvBack = findViewById(R.id.back);
        ImageView IvRefresh = findViewById(R.id.refresh);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataSiswa();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
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

        ProgressDialog progressbar = new ProgressDialog(this);
        progressbar.setMessage("Loading...");
        progressbar.setCancelable(false);
        progressbar.setIndeterminate(true);
        progressbar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressbar.dismiss();

        adapter.setOnRecyclerViewItemClickListener((nisn, refresh) -> {
            if (nisn != null && refresh == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
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

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
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

//        MaterialButton tambahPetugas = view.findViewById(R.id.tambah_petugas);
//        tambahPetugas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.pa_dialog_tambah_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
//                builder.setView(view);
//                final EditText angkatan = (EditText) view.findViewById(R.id.angkatan_spp);
//                final EditText tahun = (EditText) view.findViewById(R.id.tahun_spp);
//                final EditText nominal = (EditText) view.findViewById(R.id.nominal_spp);
//                final AlertDialog alertDialog = builder.create();
//                view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Retrofit retrofit = new Retrofit.Builder()
//                                .baseUrl(url)
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
//                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
//                        Call<SPPRepository> call = api.createSPP(angkatan.getText().toString(), tahun.getText().toString(), nominal.getText().toString());
//                        call.enqueue(new Callback<PetugasRepository>() {
//                            @Override
//                            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
//                                String value = response.body().getValue();
//                                String message = response.body().getMessage();
//                                if (value.equals("1")) {
//                                    loadDataPetugas();
//                                    alertDialog.dismiss();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<PetugasRepository> call, Throwable t) {
//                                Log.e("DEBUG", "Error: ", t);
//                            }
//                        });
//                    }
//                });
//                view.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
//                if (alertDialog.getWindow() != null) {
//                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                }
//                alertDialog.show();
//            }
//        });
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
        Intent intent = getIntent();
        String id_kelas = intent.getStringExtra("idKelas");

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
                Toast.makeText(DataSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
