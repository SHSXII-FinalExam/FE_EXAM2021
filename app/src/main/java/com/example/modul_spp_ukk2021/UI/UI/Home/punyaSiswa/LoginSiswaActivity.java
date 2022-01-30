package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Login;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.DataSiswaAdapter;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class LoginSiswaActivity extends AppCompatActivity {
    private EditText editNISN, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_siswa);

        Fade fade = new Fade();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        editNISN = findViewById(R.id.nisn);
        editPassword = findViewById(R.id.password);
        MaterialButton btnSignInSiswa = findViewById(R.id.signin_siswa);

        btnSignInSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        String nisn = editNISN.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (nisn.length() < 10) {
            editNISN.setError("NISN kosong/salah");
        } else if (password.isEmpty()) {
            editPassword.setError("Password kosong/salah");

            editPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            loadDataPembayaran(nisn, password);
        }
    }

    private void loadDataPembayaran(String nisn, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<LoginSiswaRepository> call = api.loginSiswa(nisn, password);
        call.enqueue(new Callback<LoginSiswaRepository>() {
            @Override
            public void onResponse(Call<LoginSiswaRepository> call, Response<LoginSiswaRepository> response) {
                String value = response.body().getValue();

                if (value.equals("1")) {
                    Intent intent = new Intent(LoginSiswaActivity.this, HomeSiswaActivity.class);
                    intent.putExtra("nisnSiswa", nisn);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginSiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

}