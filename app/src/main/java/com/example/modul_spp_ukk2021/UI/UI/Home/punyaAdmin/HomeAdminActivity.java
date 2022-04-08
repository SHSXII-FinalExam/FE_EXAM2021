package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerAdapter;
import com.example.modul_spp_ukk2021.UI.Data.Helper.DrawerItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SimpleItem;
import com.example.modul_spp_ukk2021.UI.Data.Helper.SpaceItem;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.example.modul_spp_ukk2021.UI.UI.Splash.OnboardingActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class HomeAdminActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_KELAS = 0;
    private static final int POS_SPP = 1;
    private static final int POS_PETUGAS = 2;
    private static final int POS_LOGOUT = 4;

    private EditText etSearch;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;
    private SharedPreferences sharedprefs;
    private LottieAnimationView loadingProgress;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean doubleBackToExitPressedOnce = false;
    private String username, password, nama_petugas, rank;
    private FragmentRefreshListener sppRefreshListener, kelasRefreshListener, petugasRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
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

    public FragmentRefreshListener getSPPRefreshListener() {
        return sppRefreshListener;
    }

    public FragmentRefreshListener getKelasRefreshListener() {
        return kelasRefreshListener;
    }

    public FragmentRefreshListener getPetugasRefreshListener() {
        return petugasRefreshListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_home);
        sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        rank = sharedprefs.getString("levelStaff", null);
        username = sharedprefs.getString("usernameStaff", null);
        password = sharedprefs.getString("passwordStaff", null);

        etSearch = findViewById(R.id.etSearch);
        loadingProgress = findViewById(R.id.loadingProgress);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refreshing();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {

                    Bundle b = new Bundle();
                    if (getKelasRefreshListener() != null) {
                        Fragment selectedScreen = new DataKelasFragment();

                        b.putString("searchData", s.toString().trim());
                        selectedScreen.setArguments(b);
                        showFragment(selectedScreen);
                    }

                    if (getSPPRefreshListener() != null) {
                        Fragment selectedScreen = new DataSPPFragment();

                        b.putString("searchData", s.toString().trim());
                        selectedScreen.setArguments(b);
                        showFragment(selectedScreen);
                    }

                    if (getPetugasRefreshListener() != null) {
                        Fragment selectedScreen = new DataPetugasFragment();

                        b.putString("searchData", s.toString().trim());
                        selectedScreen.setArguments(b);
                        showFragment(selectedScreen);
                    }

                } else {
                    Refreshing();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        SideNavSetup();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingProgress.playAnimation();
        loadingProgress.setVisibility(LottieAnimationView.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadProfil();
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

    public void Refreshing() {
        etSearch.getText().clear();
        swipeRefreshLayout.setRefreshing(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (getKelasRefreshListener() != null) {
            getKelasRefreshListener().onRefresh();
        }
        if (getSPPRefreshListener() != null) {
            getSPPRefreshListener().onRefresh();
        }
        if (getPetugasRefreshListener() != null) {
            getPetugasRefreshListener().onRefresh();
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

    public void SideNavSetup() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(true)
                .withContentClickableWhenMenuOpened(false)
                .withMenuLayout(R.layout.activity_sidenav)
                .withDragDistance(120)
                .withRootViewScale(0.7f)
                .withRootViewElevation(5)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_KELAS).setChecked(true),
                createItemFor(POS_SPP),
                createItemFor(POS_PETUGAS),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);
        adapter.setSelected(POS_KELAS);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_KELAS) {
            etSearch.getText().clear();
            etSearch.setHint("Cari Nama Kelas");
            etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            setEditTextMaxLength(etSearch, 10);

            Fragment selectedScreen = new DataKelasFragment();
            showFragment(selectedScreen);
            slidingRootNav.closeMenu();
            setSPPRefreshListener(null);
            setPetugasRefreshListener(null);

        } else if (position == POS_SPP) {
            etSearch.getText().clear();
            etSearch.setHint("Cari Tahun SPP");
            etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
            setEditTextMaxLength(etSearch, 4);

            Fragment selectedScreen = new DataSPPFragment();
            showFragment(selectedScreen);
            slidingRootNav.closeMenu();
            setKelasRefreshListener(null);
            setPetugasRefreshListener(null);

        } else if (position == POS_PETUGAS) {
            etSearch.getText().clear();
            etSearch.setHint("Cari Nama Petugas");
            etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            setEditTextMaxLength(etSearch, 35);

            Fragment selectedScreen = new DataPetugasFragment();
            showFragment(selectedScreen);
            slidingRootNav.closeMenu();
            setSPPRefreshListener(null);
            setKelasRefreshListener(null);

        } else if (position == POS_LOGOUT) {
            sharedprefs.edit().clear().apply();
            Intent intent = new Intent(HomeAdminActivity.this, OnboardingActivity.class);
            intent.putExtra("skipBoarding", "skipBoarding");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_profile, findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ((TextView) view.findViewById(R.id.tvFillNama2)).setText(nama_petugas);
            ((TextView) view.findViewById(R.id.tvLevel)).setText("Staff level : " + rank);
            ((TextView) view.findViewById(R.id.tvUsername)).setText("Username : " + username);
            ((TextView) view.findViewById(R.id.tvPassword2)).setText("Password  : " + password);

            view.findViewById(R.id.layoutDialog).setVisibility(View.GONE);
            view.findViewById(R.id.layoutDialog2).setVisibility(View.VISIBLE);
            view.findViewById(R.id.clear2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

        } else if (id == R.id.action_refresh) {
            Refreshing();
        }
        return super.onOptionsItemSelected(item);
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

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void loadProfil() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        Call<PetugasRepository> call = api.readProfilPetugas(username);
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                String value = response.body().getValue();
                List<Petugas> results = response.body().getResult();

                if (value.equals("1")) {
                    for (int i = 0; i < results.size(); i++) {
                        nama_petugas = results.get(i).getNama_petugas();
                    }

                    loadingProgress.pauseAnimation();
                    loadingProgress.setVisibility(LottieAnimationView.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                loadingProgress.pauseAnimation();
                loadingProgress.setVisibility(LottieAnimationView.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.grey300))
                .withTextTint(color(R.color.grey300))
                .withSelectedIconTint(color(R.color.red500))
                .withSelectedTextTint(color(R.color.red500));
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