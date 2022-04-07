package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class TambahKelasActivity extends AppCompatActivity {
    private ApiEndPoints api;
    private String id, jurusan, nama;
    private EditText angkatan, nomorkelas;
    private LottieAnimationView loadingProgress;
    private TextInputLayout boxNomorkelas, boxAngkatan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_tambah_kelas);

        angkatan = findViewById(R.id.angkatan);
        nomorkelas = findViewById(R.id.nomorkelas);
        RadioGroup radioJurusan = findViewById(R.id.jurusan);
        MaterialButton namakelas = findViewById(R.id.namakelas);
        TextView jurusankelas = findViewById(R.id.jurusankelas);

        boxAngkatan = findViewById(R.id.boxAngkatan);
        boxNomorkelas = findViewById(R.id.boxNomorkelas);

        TextView title = findViewById(R.id.title);
        MaterialButton kirim = findViewById(R.id.kirim);
        MaterialButton kembali = findViewById(R.id.kembali);
        loadingProgress = findViewById(R.id.loadingProgress);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        if (getIntent().getExtras() != null) {
            title.setText("Edit Data Kelas");

            id = getIntent().getStringExtra("id");
            nama = getIntent().getStringExtra("namakelas");
            jurusan = getIntent().getStringExtra("jurusan");
            nomorkelas.setText(getIntent().getStringExtra("nomorkelas"));
            angkatan.setText(getIntent().getStringExtra("angkatan"));

            namakelas.setText(nama);
            jurusankelas.setText(jurusan);

            if (jurusan.equals("RPL")) {
                jurusan = "RPL";
                radioJurusan.check(R.id.RPL);
            } else if (jurusan.equals("TKJ")) {
                jurusan = "TKJ";
                radioJurusan.check(R.id.TKJ);
            }
        }

        namakelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownMenu = new PopupMenu(TambahKelasActivity.this, namakelas);
                dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_kelas, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        namakelas.setText(menuItem.getTitle().toString());
                        nama = menuItem.getTitle().toString();
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

        radioJurusan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.RPL:
                        jurusan = "RPL";
                        jurusankelas.setText(jurusan);
                        break;
                    case R.id.TKJ:
                        jurusan = "TKJ";
                        jurusankelas.setText(jurusan);
                        break;
                    default:
                        jurusan = null;
                }
            }
        });

        kembali.setOnClickListener(v -> onBackPressed());
        kirim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateForm(angkatan.getText().toString(), nama + " " + jurusan + " " + nomorkelas.getText().toString(), jurusan);
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

    private void validateForm(String angkatan, String nama, String jurusan) {
        if (angkatan.isEmpty()) {
            boxAngkatan.setErrorEnabled(true);
            boxAngkatan.setError("Username salah");
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

        } else if (this.nomorkelas.getText().toString().isEmpty()) {
            boxNomorkelas.setErrorEnabled(true);
            boxNomorkelas.setError("Password kosong/salah");
            this.nomorkelas.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxNomorkelas.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            retrofitKelas(angkatan, nama, jurusan);

            loadingProgress.playAnimation();
            loadingProgress.setVisibility(LottieAnimationView.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void retrofitKelas(String angkatan, String nama, String jurusan) {
        Call<KelasRepository> call;
        if (getIntent().getExtras() != null) {
            call = api.updateKelas(id, nama, jurusan, angkatan);
        } else {
            call = api.createKelas(angkatan, nama, jurusan);
        }
        call.enqueue(new Callback<KelasRepository>() {
            @Override
            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
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
                            Toast.makeText(TambahKelasActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<KelasRepository> call, Throwable t) {
                loadingProgress.pauseAnimation();
                loadingProgress.setVisibility(LottieAnimationView.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(TambahKelasActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
