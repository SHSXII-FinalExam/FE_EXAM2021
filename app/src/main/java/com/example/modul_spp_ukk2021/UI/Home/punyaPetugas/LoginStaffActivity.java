package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.modul_spp_ukk2021.UI.Network.BaseURL.url;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.LoginStaff;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.Home.punyaAdmin.HomeAdminFragment;
import com.example.modul_spp_ukk2021.UI.Network.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginStaffActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    TextInputLayout textInputLayout2;
    private List<LoginStaff> loginStaff = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);

        LoginForm();
    }

    private void LoginForm() {
        edtUsername = findViewById(R.id.login_PetugasUsername);
        edtPassword = findViewById(R.id.login_PetugasPass);
        MaterialButton btnLoginStaff = findViewById(R.id.signin_petugas);
        ImageView btnBack = findViewById(R.id.imageView11);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        btnLoginStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStaffActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validateForm() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            edtUsername.setError("Username kosong/salah");
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
            loadDataPembayaran(username, password);
        }
    }

    private List<LoginStaff> fetchResults(Response<LoginStaffRepository> response) {
        LoginStaffRepository loginStaffRepository = response.body();
        return loginStaffRepository.getResult();
    }

    private void loadDataPembayaran(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIEndPoints api = retrofit.create(APIEndPoints.class);
        Call<LoginStaffRepository> call = api.loginStaff(username, password);
        call.enqueue(new Callback<LoginStaffRepository>() {
            @Override
            public void onResponse(Call<LoginStaffRepository> call, Response<LoginStaffRepository> response) {
                String value = response.body().getValue();
                List<LoginStaff> results = fetchResults(response);

                for (int i = 0; i < results.size(); i++) {
                    String level = results.get(i).getLevel();
                    Log.e("keshav", "Level ->" + level);

                    if (value.equals("1") && level.equals("Petugas")) {
                        Intent intent = new Intent(LoginStaffActivity.this, HomePetugasActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else if (value.equals("1") && level.equals("Admin")) {
                        Intent intent = new Intent(LoginStaffActivity.this, HomeAdminFragment.class);
                        intent.putExtra("level", level);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginStaffRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}