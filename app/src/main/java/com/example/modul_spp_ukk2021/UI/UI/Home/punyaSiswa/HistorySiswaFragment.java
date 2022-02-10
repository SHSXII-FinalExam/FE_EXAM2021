package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class HistorySiswaFragment extends Fragment {

    private HistorySiswaAdapter adapter;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    RecyclerView recyclerView;
    View view;

    TextView tagihan_count;

    public static HistorySiswaFragment newInstance() {
        return new HistorySiswaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.ps_fragment_history, container, false);
        ButterKnife.bind(getActivity());

        adapter = new HistorySiswaAdapter(getActivity(), pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerHistory);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tagihan_count = view.findViewById(R.id.tagihan_count);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    private List<Pembayaran> fetchResults(Response<PembayaranRepository> response) {
        PembayaranRepository pembayaranRepository = response.body();
        return pembayaranRepository.getResult();
    }

    private void loadDataPembayaran() {
        String nisnSiswa = getActivity().getIntent().getStringExtra("nisnSiswa");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PembayaranRepository> call = api.viewHistory(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                List<Pembayaran> results = response.body().getResult();

                Log.e("value", value);
                if (value.equals("1")) {
                    pembayaran = response.body().getResult();
                    adapter = new HistorySiswaAdapter(getActivity(), pembayaran);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    public void getVariable() {
        int total_sum = 0;
        String nama = "";

        for (int i = 0; i < pembayaran.size(); i++) {
            Pembayaran food_items = pembayaran.get(i);
            int price = food_items.getNominal();
            nama = food_items.getNama();
            total_sum += price;
        }

        TextView nominal = view.findViewById(R.id.nominal);
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        nominal.setText(format.format(total_sum) + ",00");

        SharedPreferences settings = getActivity().getSharedPreferences("totalTagihan", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("tagihanSiswa", total_sum);
        editor.apply();

        TextView Nama = view.findViewById(R.id.nama);
        Nama.setText(nama);

    }
}
