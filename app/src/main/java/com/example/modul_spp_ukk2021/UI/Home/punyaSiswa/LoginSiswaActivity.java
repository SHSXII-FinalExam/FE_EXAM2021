package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Repository.LoginSiswaRepository;
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
    private TextInputLayout textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_siswa);

        edtNISN = findViewById(R.id.login_SiswaNISN);
        edtPassword = findViewById(R.id.login_siswaPass);
        ImageView btnBack = findViewById(R.id.back);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        MaterialButton btnSignInSiswa = findViewById(R.id.signin_siswa);

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
            }
        });
    }

    private void validateForm() {
        String nisn = edtNISN.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (nisn.length() < 10) {
            edtNISN.setError("NISN Kurang, silahkan coba lagi...");
        } else if (password.isEmpty()) {
            edtPassword.setError("Password Kurang, silahkan coba lagi...");
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
            loginSiswa(nisn, password);
        }
    }

    private void loginSiswa(String nisn, String password) {
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
                String message = response.body().getMessage();

                if (value.equals("1")) {
                    Intent intent = new Intent(LoginSiswaActivity.this, HomeSiswaActivity.class);
                    intent.putExtra("nisnSiswa", nisn);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginSiswaActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginSiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}