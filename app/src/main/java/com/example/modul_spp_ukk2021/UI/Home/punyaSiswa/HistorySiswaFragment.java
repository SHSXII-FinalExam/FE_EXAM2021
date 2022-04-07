package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class HistorySiswaFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistorySiswaAdapter adapter;

    public HistorySiswaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.siswa_fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerHistory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        ((HomeSiswaActivity) getActivity()).setHistoryRefreshListener(new HomeSiswaActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataHistory();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataHistory();
    }

    private void loadDataHistory() {
        String nisnSiswa = requireActivity().getIntent().getStringExtra("nisnSiswa");

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
                    adapter = new HistorySiswaAdapter(getActivity(), results);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
