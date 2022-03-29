package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class TagihanSiswaFragment extends Fragment {
    private TextView nama, kelas;
    private RecyclerView recyclerView;
    private TagihanSiswaAdapter adapter;

    public TagihanSiswaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.siswa_fragment_tagihan, container, false);

        nama = view.findViewById(R.id.nama);
        kelas = view.findViewById(R.id.kelas);

        recyclerView = view.findViewById(R.id.recyclerTagihan);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        ((HomeSiswaActivity) getActivity()).setTagihanRefreshListener(new HomeSiswaActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataTagihan();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataTagihan();
    }

    private void loadDataTagihan() {
        String nisnSiswa = requireActivity().getIntent().getStringExtra("nisnSiswa");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIEndPoints api = retrofit.create(APIEndPoints.class);

        Call<PembayaranRepository> call = api.viewTagihan(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Pembayaran> results = response.body().getResult();

                Log.e("value", value);
                if (value.equals("1")) {
                    adapter = new TagihanSiswaAdapter(getActivity(), results);
                    recyclerView.setAdapter(adapter);

                    for (int i = 0; i < results.size(); i++) {
                        nama.setText(results.get(i).getNama());
                        kelas.setText("Siswa " + results.get(i).getNama_kelas());
                    }

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                    format.setMaximumFractionDigits(0);

                } else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
