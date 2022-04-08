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

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class TambahPetugasActivity extends AppCompatActivity {
    private String id;
    private ApiEndPoints api;
    private EditText nama, username, password;
    private LottieAnimationView loadingProgress;
    private TextInputLayout boxNama, boxUsername, boxPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_tambah_petugas);

        nama = findViewById(R.id.nama);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        boxNama = findViewById(R.id.boxNama);
        boxUsername = findViewById(R.id.boxUsername);
        boxPassword = findViewById(R.id.boxPassword);
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
            password.setHint("Masukkan password baru");
            id = getIntent().getStringExtra("id");
            nama.setText(getIntent().getStringExtra("nama"));
            username.setText(getIntent().getStringExtra("username"));
        }

        kembali.setOnClickListener(v -> onBackPressed());
        kirim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateForm(nama.getText().toString(), username.getText().toString(), password.getText().toString());
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

    private void validateForm(String nama, String username, String password) {
        if (nama.isEmpty()) {
            boxNama.setErrorEnabled(true);
            boxNama.setError("Username salah");
            this.nama.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxNama.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (username.isEmpty()) {
            boxUsername.setErrorEnabled(true);
            boxUsername.setError("Password kosong/salah");
            this.username.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxUsername.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (password.isEmpty()) {
            boxPassword.setErrorEnabled(true);
            boxPassword.setError("Password kosong/salah");
            this.password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxPassword.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            retrofitPetugas(nama, username, password);

            loadingProgress.playAnimation();
            loadingProgress.setVisibility(LottieAnimationView.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void retrofitPetugas(String nama, String username, String password) {
        Call<PetugasRepository> call;
        if (getIntent().getExtras() != null) {
            call = api.updatePetugas(id, username, password, nama);
        } else {
            call = api.createPetugas(username, password, nama);
        }
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
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
                            Toast.makeText(TambahPetugasActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                loadingProgress.pauseAnimation();
                loadingProgress.setVisibility(LottieAnimationView.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(TambahPetugasActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
