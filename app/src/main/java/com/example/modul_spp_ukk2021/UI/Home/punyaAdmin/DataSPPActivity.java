package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Network.APIEndPoints;

import java.util.ArrayList;
import java.util.List;

import static com.example.modul_spp_ukk2021.UI.Network.BaseURL.url;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataSPPActivity extends AppCompatActivity {

    final Context context = this;
    private TextView result;

    private DataSPPAdapter adapter;
    private List<SPP> spp = new ArrayList<>();

    @BindView(R.id.recycler_spp)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_data_spp);
        ButterKnife.bind(this);

        adapter = new DataSPPAdapter(this, spp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(new DataSPPAdapter(this, new ArrayList<SPP>()));

    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    private void loadDataPembayaran() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIEndPoints api = retrofit.create(APIEndPoints.class);
        Call<SPPRepository> call = api.viewDataSPP();
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    spp = response.body().getResult();
                    adapter = new DataSPPAdapter(DataSPPActivity.this, spp);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}