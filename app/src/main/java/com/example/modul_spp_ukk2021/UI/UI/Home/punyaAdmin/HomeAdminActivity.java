package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerAdapter;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SimpleItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SpaceItem;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class HomeAdminActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_SISWA = 0;
    private static final int POS_KELAS = 1;
    private static final int POS_SPP = 2;
    private static final int POS_PETUGAS = 3;
    private static final int POS_LOGOUT = 5;

    private Toolbar toolbar;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private SharedPreferences sharedprefs;
    private boolean doubleBackToExitPressedOnce = false;
    private FragmentRefreshListener sppRefreshListener, kelasRefreshListener, petugasRefreshListener, siswaRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    public FragmentRefreshListener getSPPRefreshListener() {
        return sppRefreshListener;
    }

    public FragmentRefreshListener getKelasRefreshListener() {
        return kelasRefreshListener;
    }

    public FragmentRefreshListener getPetugasRefreshListener() {
        return petugasRefreshListener;
    }

    public FragmentRefreshListener getSiswaRefreshListener() {
        return siswaRefreshListener;
    }

    public void setSiswaRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.siswaRefreshListener = fragmentRefreshListener;
    }

    public void setPetugasRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.petugasRefreshListener = fragmentRefreshListener;
    }

    public void setSPPRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.sppRefreshListener = fragmentRefreshListener;
    }

    public void setKelasRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.kelasRefreshListener = fragmentRefreshListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_home);
        sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getSiswaRefreshListener() != null) {
                    getSiswaRefreshListener().onRefresh();
                }
                if (getKelasRefreshListener() != null) {
                    getKelasRefreshListener().onRefresh();
                }
                if (getSPPRefreshListener() != null) {
                    getSPPRefreshListener().onRefresh();
                }
                if (getPetugasRefreshListener() != null) {
                    getPetugasRefreshListener().onRefresh();
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

        slidingRootNav = new

                SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(true)
                .withContentClickableWhenMenuOpened(false)
                .withMenuLayout(R.layout.activity_sidenav)
                .withDragDistance(120)
                .withRootViewScale(0.7f)
                .withRootViewElevation(5)
                .inject();

        screenIcons =

                loadScreenIcons();

        screenTitles =

                loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_SISWA).setChecked(true),
                createItemFor(POS_KELAS),
                createItemFor(POS_SPP),
                createItemFor(POS_PETUGAS),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new

                LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_SISWA);
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
    public void onItemSelected(int position) {
        if (position == POS_SISWA) {
            Fragment selectedScreen = new DataSiswaFragment();
            showFragment(selectedScreen);
            toolbar.setTitle("Data Siswa");
        } else if (position == POS_KELAS) {
            Fragment selectedScreen = new DataKelasFragment();
            showFragment(selectedScreen);
            toolbar.setTitle("Data Kelas");
        } else if (position == POS_SPP) {
            Fragment selectedScreen = new DataSPPFragment();
            showFragment(selectedScreen);
            toolbar.setTitle("Data SPP");
        } else if (position == POS_PETUGAS) {
            Fragment selectedScreen = new DataPetugasFragment();
            showFragment(selectedScreen);
            toolbar.setTitle("Data Petugas");
        } else if (position == POS_LOGOUT) {
            sharedprefs.edit().clear().apply();
            Intent intent = new Intent(HomeAdminActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
        }
        slidingRootNav.closeMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.generate_data) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
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
        return getResources().getStringArray(R.array.pa_sideNavTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.pa_sideNavIcons);
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