package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.modul_spp_ukk2021.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class HomeSiswaActivity extends AppCompatActivity {
    private MaterialButton logout;
    private int indicatorWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ps_activity_home);

        TabLayout mTabs = findViewById(R.id.tab);
        View mIndicator = findViewById(R.id.indicator);
        logout = findViewById(R.id.logout);
        ViewPager mViewPager = findViewById(R.id.viewPager);

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
            }
        });

        logout.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeSiswaActivity.this, logout, ViewCompat.getTransitionName(logout));
            Intent intent = new Intent(HomeSiswaActivity.this, LoginSiswaActivity.class);
            startActivity(intent, options.toBundle());
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeSiswaActivity.this, logout, ViewCompat.getTransitionName(logout));
                        Intent intent = new Intent(HomeSiswaActivity.this, LoginSiswaActivity.class);
                        startActivity(intent, options.toBundle());
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}