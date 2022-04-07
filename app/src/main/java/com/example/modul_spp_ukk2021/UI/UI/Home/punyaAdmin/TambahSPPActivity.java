package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class TambahSPPActivity extends AppCompatActivity {
    private Integer id;
    private ApiEndPoints api;
    private EditText tahun, nominal, angkatan;
    private LottieAnimationView loadingProgress;
    private TextInputLayout boxTahun, boxNominal, boxAngkatan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_tambah_spp);

        tahun = findViewById(R.id.tahun);
        nominal = findViewById(R.id.nominal);
        angkatan = findViewById(R.id.angkatan);

        boxTahun = findViewById(R.id.boxTahun);
        boxNominal = findViewById(R.id.boxNominal);
        boxAngkatan = findViewById(R.id.boxAngkatan);
        loadingProgress = findViewById(R.id.loadingProgress);

        TextView title = findViewById(R.id.title);
        MaterialButton kirim = findViewById(R.id.kirim);
        MaterialButton kembali = findViewById(R.id.kembali);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        if (getIntent().getExtras() != null) {
            title.setText("Edit Data Petugas");
            tahun.setEnabled(false);
            angkatan.setEnabled(false);
            id = getIntent().getIntExtra("id", 0);
            tahun.setText(getIntent().getStringExtra("tahun"));
            nominal.setText(getIntent().getStringExtra("nominal"));
            angkatan.setText(getIntent().getStringExtra("angkatan"));
            tahun.setTextColor(ContextCompat.getColor(this, R.color.grey400));
            angkatan.setTextColor(ContextCompat.getColor(this, R.color.grey400));
        }

        kembali.setOnClickListener(v -> onBackPressed());
        kirim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateForm(tahun.getText().toString(), nominal.getText().toString(), angkatan.getText().toString());
            }
        });
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

    private void validateForm(String tahun, String nominal, String angkatan) {
        if (tahun.isEmpty()) {
            boxTahun.setErrorEnabled(true);
            boxTahun.setError("Username salah");
            this.tahun.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxTahun.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (nominal.isEmpty()) {
            boxNominal.setErrorEnabled(true);
            boxNominal.setError("Password kosong/salah");
            this.nominal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxNominal.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (angkatan.isEmpty()) {
            boxAngkatan.setErrorEnabled(true);
            boxAngkatan.setError("Password kosong/salah");
            this.angkatan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxAngkatan.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            retrofitSPP(tahun, nominal, angkatan);

            loadingProgress.playAnimation();
            loadingProgress.setVisibility(LottieAnimationView.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void retrofitSPP(String tahun, String nominal, String angkatan) {
        Call<SPPRepository> call;
        if (getIntent().getExtras() != null) {
            call = api.updateSPP(id, nominal);
        } else {
            call = api.createSPP(angkatan, tahun, nominal);
        }
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (value.equals("1")) {
                            onBackPressed();
                        } else {
                            loadingProgress.pauseAnimation();
                            loadingProgress.setVisibility(LottieAnimationView.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(TambahSPPActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                loadingProgress.pauseAnimation();
                loadingProgress.setVisibility(LottieAnimationView.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(TambahSPPActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
