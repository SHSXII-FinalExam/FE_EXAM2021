package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class TambahSiswaActivity extends AppCompatActivity {
    private Integer id_spp;
    private ApiEndPoints api;
    private LottieAnimationView loadingProgress;
    private String id_petugas, id_kelas;
    private EditText nama, nisn, nis, kelas, alamat, ponsel, password;
    private TextInputLayout boxNama, boxNisn, boxNis, boxKelas, boxAlamat, boxPonsel, boxPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pa_activity_tambah_siswa);
        SharedPreferences sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        id_petugas = sharedprefs.getString("idStaff", null);
        id_kelas = getIntent().getStringExtra("id_kelas");
        String angkatan = getIntent().getStringExtra("angkatan");
        String nama_kelas = getIntent().getStringExtra("nama_kelas");

        nama = findViewById(R.id.nama);
        nisn = findViewById(R.id.nisn);
        nis = findViewById(R.id.nis);
        kelas = findViewById(R.id.kelas);
        Button btnSpp = findViewById(R.id.btnSpp);
        alamat = findViewById(R.id.alamat);
        ponsel = findViewById(R.id.ponsel);
        password = findViewById(R.id.password);

        boxNama = findViewById(R.id.boxNama);
        boxNisn = findViewById(R.id.boxNisn);
        boxNis = findViewById(R.id.boxNis);
        boxKelas = findViewById(R.id.boxKelas);
        boxAlamat = findViewById(R.id.boxAlamat);
        boxPonsel = findViewById(R.id.boxPonsel);
        boxPassword = findViewById(R.id.boxPassword);

        loadingProgress = findViewById(R.id.loadingProgress);
        TextView title = findViewById(R.id.title);
        MaterialButton kirim = findViewById(R.id.kirim);
        MaterialButton kembali = findViewById(R.id.kembali);

        kelas.setEnabled(false);
        kelas.setText(nama_kelas);
        kelas.setTextColor(ContextCompat.getColor(this, R.color.grey400));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        PopupMenu dropDownMenu = new PopupMenu(this, btnSpp);
        Call<SPPRepository> call = api.readSPPAngkatan(angkatan);
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<SPP> results = response.body().getResult();

                Locale localeID = new Locale("in", "ID");
                NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                format.setMaximumFractionDigits(0);

                if (value.equals("1")) {
                    for (int i = 0; i < results.size(); i++) {
                        dropDownMenu.getMenu().add(Menu.NONE, results.get(i).getId_spp(), Menu.NONE, results.get(i).getTahun() + " (" + format.format(results.get(i).getNominal()) + ")");
                    }

                } else {
                    btnSpp.setEnabled(false);
                    btnSpp.setText("SPP Kosong");
                    kirim.setEnabled(false);
                    kirim.setBackgroundColor(ContextCompat.getColor(TambahSiswaActivity.this, R.color.red200));
                    Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                Toast.makeText(TambahSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });

        btnSpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        btnSpp.setText(menuItem.getTitle().toString());
                        id_spp = menuItem.getItemId();
                        return true;
                    }
                });
            }
        });

        if (getIntent().getStringExtra("nisn") != null) {
            title.setText("Edit Data Siswa");
            nisn.setEnabled(false);
            nis.setEnabled(false);
            nisn.setTextColor(ContextCompat.getColor(this, R.color.grey400));
            nis.setTextColor(ContextCompat.getColor(this, R.color.grey400));

            nisn.setText(getIntent().getStringExtra("nisn"));
            nis.setText(getIntent().getStringExtra("nis"));
            nama.setText(getIntent().getStringExtra("nama"));
            kelas.setText(nama_kelas);
            alamat.setText(getIntent().getStringExtra("alamat"));
            ponsel.setText(getIntent().getStringExtra("ponsel"));
            password.setHint("Buat password baru");
        }

        kembali.setOnClickListener(v -> onBackPressed());
        kirim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateForm(nama.getText().toString(),
                        nisn.getText().toString(),
                        nis.getText().toString(),
                        kelas.getText().toString(),
                        alamat.getText().toString(),
                        ponsel.getText().toString(),
                        password.getText().toString());
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

    private void validateForm(String nama, String nisn, String nis, String kelas, String alamat, String ponsel, String password) {
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

        } else if (nisn.length() < 10) {
            boxNisn.setErrorEnabled(true);
            boxNisn.setError("Password kosong/salah");
            this.nisn.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxNisn.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (nis.length() < 8) {
            boxNis.setErrorEnabled(true);
            boxNis.setError("Password kosong/salah");
            this.nis.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxNis.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (kelas.isEmpty()) {
            boxKelas.setErrorEnabled(true);
            boxKelas.setError("Password kosong/salah");
            this.kelas.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxKelas.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (alamat.isEmpty()) {
            boxAlamat.setErrorEnabled(true);
            boxAlamat.setError("Password kosong/salah");
            this.alamat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxAlamat.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (ponsel.isEmpty()) {
            boxPonsel.setErrorEnabled(true);
            boxPonsel.setError("Password kosong/salah");
            this.ponsel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boxPonsel.setErrorEnabled(false);
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
            retrofitSiswa(nama, nisn, nis, kelas, alamat, ponsel, password);

            loadingProgress.playAnimation();
            loadingProgress.setVisibility(LottieAnimationView.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void retrofitSiswa(String nama, String nisn, String nis, String kelas, String alamat, String ponsel, String password) {
        Call<SiswaRepository> call;
        if (getIntent().getStringExtra("nisn") != null) {
            call = api.updateSiswa(nisn, nama, id_kelas, id_spp, alamat, ponsel, password, id_petugas);
        } else {
            call = api.createSiswa(nisn, nis, nama, id_kelas, id_spp, alamat, ponsel, password, id_petugas);
        }
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
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
                            Toast.makeText(TambahSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                loadingProgress.pauseAnimation();
                loadingProgress.setVisibility(LottieAnimationView.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(TambahSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
