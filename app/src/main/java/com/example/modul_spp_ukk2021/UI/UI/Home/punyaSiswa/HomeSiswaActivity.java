package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerAdapter;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SimpleItem;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class HomeSiswaActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_DASHBOARD = 0;
    private static final int POS_LOGOUT = 1;

    private int indicatorWidth;
    private TextView nama, kelas;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private SharedPreferences sharedprefs;
    private String nisnSiswa, passwordSiswa;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Siswa> siswa = new ArrayList<>();
    private boolean doubleBackToExitPressedOnce = false;
    private String tvFillNama, tvNIS, tvKelas, tvFillAlamat, tvNoTelp;
    private FragmentRefreshListener historyRefreshListener, tagihanRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    public FragmentRefreshListener getHistoryRefreshListener() {
        return historyRefreshListener;
    }

    public FragmentRefreshListener getTagihanRefreshListener() {
        return tagihanRefreshListener;
    }

    public void setTagihanRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.tagihanRefreshListener = fragmentRefreshListener;
    }

    public void setHistoryRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.historyRefreshListener = fragmentRefreshListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_activity_home);
        sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        nisnSiswa = sharedprefs.getString("nisnSiswa", null);
        passwordSiswa = sharedprefs.getString("passwordSiswa", null);

        nama = findViewById(R.id.nama);
        kelas = findViewById(R.id.kelas);
        TabLayout mTabs = findViewById(R.id.tab);
        View mIndicator = findViewById(R.id.indicator);
        ViewPager mViewPager = findViewById(R.id.viewPager);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getHistoryRefreshListener() != null && getTagihanRefreshListener() != null) {
                    getHistoryRefreshListener().onRefresh();
                    getTagihanRefreshListener().onRefresh();
                }

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

        SliderSetup(mTabs, mIndicator, mViewPager);
        SideNavSetup();
    }

    public void toggleRefreshing(boolean enabled) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enabled);
        }
    }

    public void SliderSetup(TabLayout mTabs, View mIndicator, ViewPager mViewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TagihanSiswaFragment(), "Tagihan");
        adapter.addFragment(new HistorySiswaFragment(), "Riwayat");

        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                toggleRefreshing(i == ViewPager.SCROLL_STATE_IDLE);
            }
        });
    }

    public void SideNavSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withMenuLayout(R.layout.activity_sidenav)
                .withDragDistance(120)
                .withRootViewScale(0.7f)
                .withRootViewElevation(5)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            sharedprefs.edit().clear().apply();
            Intent intent = new Intent(HomeSiswaActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_profile, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            ((TextView) view.findViewById(R.id.tvFillNama)).setText(tvFillNama);
            ((TextView) view.findViewById(R.id.tvNISN)).setText("NISN    : " + nisnSiswa);
            ((TextView) view.findViewById(R.id.tvNIS)).setText("NIS                    : " + tvNIS);
            ((TextView) view.findViewById(R.id.tvKelas)).setText("Kelas                 : " + tvKelas);
            ((TextView) view.findViewById(R.id.tvFillAlamat)).setText(tvFillAlamat);
            ((TextView) view.findViewById(R.id.tvNoTelp)).setText("Nomor Ponsel : " + tvNoTelp);
            ((TextView) view.findViewById(R.id.tvPassword)).setText("Password          : " + passwordSiswa);
            final AlertDialog alertDialog = builder.create();

            view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        } else if (id == R.id.action_refresh) {
            swipeRefreshLayout.setRefreshing(true);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (getHistoryRefreshListener() != null && getTagihanRefreshListener() != null) {
                            getHistoryRefreshListener().onRefresh();
                            getTagihanRefreshListener().onRefresh();
                        }
                    }
                }
            }, 1000);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        slidingRootNav.openMenu();
        if (slidingRootNav.isMenuOpened()) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Tekan lagi untuk keluar...", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPembayaran();
        loadProfil();
    }

    private void loadPembayaran() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PembayaranRepository> call = api.viewTagihan(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                List<Pembayaran> results = response.body().getResult();

                Log.e("value", value);
                if (value.equals("1")) {
                    loadProfil();

                    int i;
                    int total_sum = 0;
                    for (i = 0; i < results.size(); i++) {
                        int total_Kurang = results.get(i).getKurang_bayar();
                        int belum_Bayar = results.get(i).getNominal();
                        nama.setText(results.get(i).getNama());
                        kelas.setText("Siswa " + results.get(i).getNama_kelas());

                        if (results.get(i).getKurang_bayar() == 0) {
                            total_sum += belum_Bayar;
                        }
                        total_sum += total_Kurang;
                    }
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    private void loadProfil() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<SiswaRepository> call = api.viewProfile(nisnSiswa);
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                String value = response.body().getValue();
                List<Siswa> results = response.body().getResult();
                String message = response.body().getMessage();

                Log.e("value", value);
                if (value.equals("1")) {
                    siswa = response.body().getResult();

                    for (int i = 0; i < results.size(); i++) {
                        tvFillNama = results.get(i).getNama();
                        tvNIS = results.get(i).getNis();
                        tvKelas = results.get(i).getNama_kelas();
                        tvFillAlamat = results.get(i).getAlamat();
                        tvNoTelp = results.get(i).getNo_telp();
                    }
                } else {
                    Toast.makeText(HomeSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(android.R.color.darker_gray))
                .withTextTint(color(android.R.color.darker_gray))
                .withSelectedIconTint(color(R.color.colorPrimary))
                .withSelectedTextTint(color(R.color.colorPrimary));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ps_sideNavTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ps_sideNavIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}