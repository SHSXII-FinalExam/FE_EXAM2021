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
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class TagihanSiswaFragment extends Fragment {

    private TagihanSiswaAdapter adapter;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    RecyclerView recyclerView;
    View view;

    ImageView profile_frame, profile_pict, minimize, minimize2;
    TextView nama, kelas, profile;
    MaterialCardView cardView;

    public static TagihanSiswaFragment newInstance() {
        return new TagihanSiswaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.ps_fragment_tagihan, container, false);

        adapter = new TagihanSiswaAdapter(getActivity(), pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerTagihan);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        minimize = view.findViewById(R.id.minimize);
        minimize2 = view.findViewById(R.id.minimize2);
        profile_frame = view.findViewById(R.id.profile_frame);
        profile_pict = view.findViewById(R.id.profile_pict);
        nama = view.findViewById(R.id.nama);
        kelas = view.findViewById(R.id.kelas);
        cardView = view.findViewById(R.id.materialCardView5);
        profile = view.findViewById(R.id.profile);

        minimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minimize();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minimize();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

//    public void getVariable() {
//        int total_sum = 0;
//        String nama = "";
//
//        for (int i = 0; i < pembayaran.size(); i++) {
//            Pembayaran food_items = pembayaran.get(i);
//            int price = food_items.getNominal();
//            nama = food_items.getNama();
//            total_sum += price;
//        }
//
//        TextView nominal = view.findViewById(R.id.nominal);
//        Locale localeID = new Locale("in", "ID");
//        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
//        format.setMaximumFractionDigits(0);
//        nominal.setText(format.format(total_sum)+",00");
//
//        SharedPreferences settings = getActivity().getSharedPreferences("totalTagihan", 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putInt("tagihanSiswa", total_sum);
//        editor.apply();
//
//        TextView Nama = view.findViewById(R.id.nama);
//        Nama.setText(nama);
//
//    }

    private void minimize() {
        int status = profile_pict.getVisibility();

        if (status == View.VISIBLE) {
            profile_frame.setVisibility(View.GONE);
            profile_pict.setVisibility(View.GONE);
            nama.setVisibility(View.GONE);
            kelas.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            minimize.setAlpha(0f);
            minimize2.setVisibility(View.VISIBLE);
        } else {
            profile_frame.setVisibility(View.VISIBLE);
            profile_pict.setVisibility(View.VISIBLE);
            nama.setVisibility(View.VISIBLE);
            kelas.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            minimize.setAlpha(1f);
            minimize2.setVisibility(View.GONE);
        }

    }

    private void loadDataPembayaran() {
        String nisnSiswa = getActivity().getIntent().getStringExtra("nisnSiswa");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PembayaranRepository> call = api.viewHistoryNISN(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                Log.e("value", value);
                if (value.equals("1")) {
                    pembayaran = response.body().getResult();
                    adapter = new TagihanSiswaAdapter(getActivity(), pembayaran);
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
