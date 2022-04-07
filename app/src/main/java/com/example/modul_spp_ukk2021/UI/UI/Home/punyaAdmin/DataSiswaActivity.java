package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataSiswaActivity extends AppCompatActivity {
    private ApiEndPoints api;
    private DataSiswaAdapter adapter;
    private RecyclerView recyclerView;
    private LottieAnimationView emptyTransaksi;
    private String id_kelas, nama_kelas, angkatan;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final List<Siswa> siswa = new ArrayList<>();

    public DataSiswaActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_data_siswa);
        id_kelas = getIntent().getStringExtra("id_kelas");
        nama_kelas = getIntent().getStringExtra("nama_kelas");
        angkatan = getIntent().getStringExtra("angkatan");

        ImageView IvBack = findViewById(R.id.back);
        ImageView IvRefresh = findViewById(R.id.refresh);
        emptyTransaksi = findViewById(R.id.emptyTransaksi);
        EditText SearchSiswa = findViewById(R.id.searchSiswa);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        adapter = new DataSiswaAdapter(this, siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_siswa);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refreshing();
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
                        Refreshing();
                    }
                    return true;
                }
            });
            popup.show();
        });

        adapter.setOnRecyclerViewItemClickListener((nisn, refresh) -> {
            if (refresh == null) {
                Call<SiswaRepository> call = api.deleteSiswa(nisn);
                call.enqueue(new Callback<SiswaRepository>() {
                    @Override
                    public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTransaksi.pauseAnimation();
                            emptyTransaksi.setVisibility(LottieAnimationView.GONE);
                            loadDataSiswa();

                        } else {
                            Toast.makeText(DataSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SiswaRepository> call, Throwable t) {
                        recyclerView.setVisibility(View.GONE);
                        emptyTransaksi.setAnimation(R.raw.nointernet);
                        emptyTransaksi.playAnimation();
                        emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                        Toast.makeText(DataSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });

            } else {
                loadDataSiswa();
            }
        });

        SearchSiswa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);

                    Call<SiswaRepository> call = api.searchSiswaKelas(s.toString().trim(), id_kelas);
                    call.enqueue(new Callback<SiswaRepository>() {
                        @Override
                        public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                            String value = response.body().getValue();
                            List<Siswa> results = response.body().getResult();

                            if (value.equals("1")) {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyTransaksi.pauseAnimation();
                                emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                                adapter = new DataSiswaAdapter(DataSiswaActivity.this, results);
                                recyclerView.setAdapter(adapter);
                                runLayoutAnimation(recyclerView);

                            } else {
                                recyclerView.setVisibility(View.GONE);
                                emptyTransaksi.setAnimation(R.raw.nodata);
                                emptyTransaksi.playAnimation();
                                emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<SiswaRepository> call, Throwable t) {
                            emptyTransaksi.setAnimation(R.raw.nointernet);
                            emptyTransaksi.playAnimation();
                            emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });

                } else {
                    loadDataSiswa();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        MaterialButton tambahSiswa = findViewById(R.id.tambah_siswa);
        tambahSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.preventTwoClick(v);
                Intent intent = new Intent(DataSiswaActivity.this, TambahSiswaActivity.class);
                intent.putExtra("id_kelas", id_kelas);
                intent.putExtra("nama_kelas", nama_kelas);
                intent.putExtra("angkatan", angkatan);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataSiswa();
    }

    public void Refreshing() {
        swipeRefreshLayout.setRefreshing(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadDataSiswa();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }, 1000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadDataSiswa() {
        Call<SiswaRepository> call = api.readSiswaKelas(id_kelas);
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Siswa> siswa = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyTransaksi.pauseAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                    adapter = new DataSiswaAdapter(DataSiswaActivity.this, siswa);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyTransaksi.setAnimation(R.raw.nodata);
                    emptyTransaksi.playAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                    Toast.makeText(DataSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyTransaksi.setAnimation(R.raw.nointernet);
                emptyTransaksi.playAnimation();
                emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                Toast.makeText(DataSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
