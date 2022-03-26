package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class TagihanSiswaFragment extends Fragment {
    private String nisnSiswa;
    private RecyclerView recyclerView;
    private TagihanSiswaAdapter adapter;
    private TextView nominal, tagihan_count;
    private LottieAnimationView emptyTransaksi;
    private MaterialCardView materialCardView5;
    private List<Pembayaran> pembayaran = new ArrayList<>();

    public TagihanSiswaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.ps_fragment_tagihan, container, false);
        SharedPreferences sharedprefs = requireActivity().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        nisnSiswa = sharedprefs.getString("nisnSiswa", null);

        nominal = view.findViewById(R.id.nominal);
        tagihan_count = view.findViewById(R.id.tagihan_count);
        emptyTransaksi = view.findViewById(R.id.emptyTransaksi);
        materialCardView5 = view.findViewById(R.id.materialCardView5);

        adapter = new TagihanSiswaAdapter(getActivity(), pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerTagihan);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    private void loadDataTagihan() {
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

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    materialCardView5.setVisibility(View.VISIBLE);
                    emptyTransaksi.pauseAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                    pembayaran = response.body().getResult();
                    adapter = new TagihanSiswaAdapter(getActivity(), pembayaran);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);

                    int i;
                    int total_sum = 0;
                    for (i = 0; i < results.size(); i++) {
                        int total_Kurang = results.get(i).getKurang_bayar();
                        int belum_Bayar = results.get(i).getNominal();

                        if (results.get(i).getKurang_bayar() == 0) {
                            total_sum += belum_Bayar;
                        }
                        total_sum += total_Kurang;
                    }

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                    format.setMaximumFractionDigits(0);
                    nominal.setText(format.format(total_sum));
                    tagihan_count.setText("(" + String.valueOf(i) + ")");

                } else {
                    tagihan_count.setText("(0)");
                    recyclerView.setVisibility(View.GONE);
                    materialCardView5.setVisibility(View.GONE);
                    emptyTransaksi.playAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                tagihan_count.setText("(0)");
                recyclerView.setVisibility(View.GONE);
                materialCardView5.setVisibility(View.GONE);
                emptyTransaksi.playAnimation();
                emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                Toast.makeText(requireActivity(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
