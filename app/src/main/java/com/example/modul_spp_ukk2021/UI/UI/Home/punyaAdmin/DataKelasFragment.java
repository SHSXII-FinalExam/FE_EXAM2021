package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataKelasFragment extends Fragment {
    private ApiEndPoints api;
    private TextView title_count;
    private DataKelasAdapter adapter;
    private RecyclerView recyclerView;
    private LottieAnimationView emptyTransaksi;
    private final List<Kelas> kelas = new ArrayList<>();

    public DataKelasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_fragment_data_kelas, container, false);

        title_count = view.findViewById(R.id.title_count);
        emptyTransaksi = view.findViewById(R.id.emptyTransaksi);

        adapter = new DataKelasAdapter(getActivity(), kelas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_kelas);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        adapter.setOnRecyclerViewItemClickListener((id_kelas, refresh) -> {
            if (refresh == null) {
                Call<KelasRepository> call = api.deleteKelas(id_kelas);
                call.enqueue(new Callback<KelasRepository>() {
                    @Override
                    public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTransaksi.pauseAnimation();
                            emptyTransaksi.setVisibility(LottieAnimationView.GONE);
                            loadDataKelas();

                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KelasRepository> call, Throwable t) {
                        recyclerView.setVisibility(View.GONE);
                        emptyTransaksi.setAnimation(R.raw.nointernet);
                        emptyTransaksi.playAnimation();
                        emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                        Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });

            } else {
                loadDataKelas();
            }
        });

        NestedScrollView scrollView = view.findViewById(R.id.scroll_data);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        ((HomeAdminActivity) requireActivity()).setKelasRefreshListener(new HomeAdminActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataKelas();
            }
        });

        MaterialButton tambahKelas = view.findViewById(R.id.tambah_kelas);
        tambahKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.preventTwoClick(v);
                Intent intent = new Intent(requireActivity(), TambahKelasActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cekBundle();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    private void cekBundle() {
        if (this.getArguments() != null) {
            String searchData = this.getArguments().getString("searchData");
            Call<KelasRepository> call = api.searchKelas(searchData);
            call.enqueue(new Callback<KelasRepository>() {
                @Override
                public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                    String value = response.body().getValue();
                    List<Kelas> kelas = response.body().getResult();

                    if (value.equals("1")) {
                        title_count.setText("(" + kelas.size() + ")");
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyTransaksi.pauseAnimation();
                        emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                        adapter = new DataKelasAdapter(getActivity(), kelas);
                        recyclerView.setAdapter(adapter);
                        runLayoutAnimation(recyclerView);

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        emptyTransaksi.setAnimation(R.raw.nodata);
                        emptyTransaksi.playAnimation();
                        emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<KelasRepository> call, Throwable t) {
                    recyclerView.setVisibility(View.GONE);
                    emptyTransaksi.setAnimation(R.raw.nointernet);
                    emptyTransaksi.playAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                    Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Error: ", t);
                }
            });

        } else {
            loadDataKelas();
        }
    }

    public void loadDataKelas() {
        Call<KelasRepository> call = api.readKelas();
        call.enqueue(new Callback<KelasRepository>() {
            @Override
            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                String value = response.body().getValue();
                List<Kelas> kelas = response.body().getResult();

                if (value.equals("1")) {
                    title_count.setText("(" + kelas.size() + ")");
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyTransaksi.pauseAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                    adapter = new DataKelasAdapter(getActivity(), kelas);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyTransaksi.setAnimation(R.raw.nodata);
                    emptyTransaksi.playAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<KelasRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyTransaksi.setAnimation(R.raw.nointernet);
                emptyTransaksi.playAnimation();
                emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
