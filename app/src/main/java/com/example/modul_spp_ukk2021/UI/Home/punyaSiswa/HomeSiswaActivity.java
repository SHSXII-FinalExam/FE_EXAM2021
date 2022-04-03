package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class HomeSiswaActivity extends AppCompatActivity {
    private int indicatorWidth;
    private MaterialButton logout;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        setContentView(R.layout.siswa_home_activity);

        logout = findViewById(R.id.logoutSiswa);
        TabLayout mTabs = findViewById(R.id.tab);
        View mIndicator = findViewById(R.id.indicator);
        ViewPager mViewPager = findViewById(R.id.viewPager);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refreshing();
            }
        });

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TagihanSiswaFragment(), "Tagihan");
        adapter.addFragment(new HistorySiswaFragment(), "History");

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

        logout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeSiswaActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
        });
    }

    public void toggleRefreshing(boolean enabled) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enabled);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeSiswaActivity.this, logout, ViewCompat.getTransitionName(logout));
                        Intent intent = new Intent(HomeSiswaActivity.this, LoginChoiceActivity.class);
                        startActivity(intent, options.toBundle());
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    public void Refreshing() {
        swipeRefreshLayout.setRefreshing(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (getHistoryRefreshListener() != null && getTagihanRefreshListener() != null) {
            getHistoryRefreshListener().onRefresh();
            getTagihanRefreshListener().onRefresh();
        }

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
}