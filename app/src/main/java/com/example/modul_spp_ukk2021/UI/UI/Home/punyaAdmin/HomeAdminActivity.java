package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerAdapter;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SimpleItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SpaceItem;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class HomeAdminActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_SISWA = 0;
    private static final int POS_KELAS = 1;
    private static final int POS_SPP = 2;
    private static final int POS_TRANSAKSI = 3;
    private static final int POS_PETUGAS = 4;
    private static final int POS_LOGOUT = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private FragmentRefreshListener sppRefreshListener, kelasRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    public FragmentRefreshListener getSPPRefreshListener() {
        return sppRefreshListener;
    }

    public FragmentRefreshListener getKelasRefreshListener() {
        return kelasRefreshListener;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(true)
                .withContentClickableWhenMenuOpened(false)
                .withMenuLayout(R.layout.activity_sidenav)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_SISWA).setChecked(true),
                createItemFor(POS_KELAS),
                createItemFor(POS_SPP),
                createItemFor(POS_TRANSAKSI),
                createItemFor(POS_PETUGAS),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_SISWA);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(HomeAdminActivity.this, LoginStaffActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_SPP) {
            Fragment selectedScreen = new DataSPPFragment();
            showFragment(selectedScreen);
        } else if (position == POS_KELAS) {
            Fragment selectedScreen = new DataKelasFragment();
            showFragment(selectedScreen);
        } else if (position == POS_LOGOUT) {
            Intent intent = new Intent(HomeAdminActivity.this, LoginStaffActivity.class);
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
        if (id == R.id.action_refresh) {
            if (getSPPRefreshListener() != null && getKelasRefreshListener() != null) {
                getSPPRefreshListener().onRefresh();
                getKelasRefreshListener().onRefresh();
            }
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