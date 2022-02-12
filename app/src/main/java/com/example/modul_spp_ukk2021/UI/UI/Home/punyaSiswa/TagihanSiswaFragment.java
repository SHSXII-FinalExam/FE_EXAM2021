package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class TagihanSiswaFragment extends Fragment {
    private RecyclerView recyclerView;
    private TagihanSiswaAdapter adapter;
    private TextView nama, kelas, nominal, tagihan_count;
    private List<Pembayaran> pembayaran = new ArrayList<>();
    private ImageView profile_frame, profile_pict, minimize, minimize2;

    public TagihanSiswaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.ps_fragment_tagihan, container, false);

        nama = view.findViewById(R.id.nama);
        kelas = view.findViewById(R.id.kelas);
        nominal = view.findViewById(R.id.nominal);
        minimize = view.findViewById(R.id.minimize);
        minimize2 = view.findViewById(R.id.minimize2);
        TextView profile = view.findViewById(R.id.profile);
        profile_pict = view.findViewById(R.id.profile_pict);
        profile_frame = view.findViewById(R.id.profile_frame);
        tagihan_count = view.findViewById(R.id.tagihan_count);

        adapter = new TagihanSiswaAdapter(getActivity(), pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerTagihan);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        minimize.setOnClickListener(v -> minimize());
        profile.setOnClickListener(v -> minimize());

        return view;
    }

    private void minimize() {
        int status = profile_pict.getVisibility();

        if (status == View.VISIBLE) {
            minimize.setAlpha(0f);
            nama.setVisibility(View.GONE);
            kelas.setVisibility(View.GONE);
            profile_frame.setVisibility(View.GONE);
            profile_pict.setVisibility(View.GONE);
            minimize2.setVisibility(View.VISIBLE);
        } else {
            minimize.setAlpha(1f);
            nama.setVisibility(View.VISIBLE);
            kelas.setVisibility(View.VISIBLE);
            profile_frame.setVisibility(View.VISIBLE);
            profile_pict.setVisibility(View.VISIBLE);
            minimize2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataTagihan();
    }

    private void loadDataTagihan() {
        String nisnSiswa = getActivity().getIntent().getStringExtra("nisnSiswa");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PembayaranRepository> call = api.viewTagihan(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                List<Pembayaran> results = response.body().getResult();

                Log.e("value", value);
                if (value.equals("1")) {
                    pembayaran = response.body().getResult();
                    adapter = new TagihanSiswaAdapter(getActivity(), pembayaran);
                    recyclerView.setAdapter(adapter);

                    int i;
                    int total_sum = 0;
                    for (i = 0; i < results.size(); i++) {
                        int total_Kurang = results.get(i).getKurang_bayar();
                        int belum_Bayar = results.get(i).getNominal();
                        nama.setText(results.get(i).getNama());
                        kelas.setText("Siswa " + results.get(i).getNama_kelas());

                        if (results.get(i).getKurang_bayar() == 0) {
                            total_sum += belum_Bayar;
                        }
                        total_sum += total_Kurang;
                    }

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                    format.setMaximumFractionDigits(0);
                    nominal.setText(format.format(total_sum) + ",00");
                    tagihan_count.setText("(" + String.valueOf(i) + ")");
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
