package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.MainActivity;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginSiswaActivity extends AppCompatActivity {
    private EditText edtNISN, edtPassword;
    TextInputLayout textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_siswa);

        LoginForm();
    }

    private void LoginForm() {
        edtNISN = findViewById(R.id.login_SiswaNISN);
        edtPassword = findViewById(R.id.login_siswaPass);
        MaterialButton btnSignInSiswa = findViewById(R.id.signin_siswa);
        ImageView btnBack = findViewById(R.id.imageView);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        btnSignInSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSiswaActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void validateForm() {
        String nisn = edtNISN.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (nisn.length() < 10) {
            edtNISN.setError("NISN kosong/salah");
        } else if (password.isEmpty()) {
            edtPassword.setError("Password kosong/salah");
            textInputLayout2.setEndIconVisible(false);

            edtPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputLayout2.setEndIconVisible(true);
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
                    Intent intent = new Intent(LoginSiswaActivity.this, MainActivity.class);
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