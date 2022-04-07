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
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataSPPFragment extends Fragment {
    private ApiEndPoints api;
    private TextView title_count;
    private DataSPPAdapter adapter;
    private RecyclerView recyclerView;
    private LottieAnimationView emptyTransaksi;
    private final List<SPP> spp = new ArrayList<>();

    public DataSPPFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_fragment_data_spp, container, false);

        title_count = view.findViewById(R.id.title_count);
        emptyTransaksi = view.findViewById(R.id.emptyTransaksi);

        adapter = new DataSPPAdapter(getActivity(), spp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_spp);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        adapter.setOnRecyclerViewItemClickListener((id_spp, refresh) -> {
            if (refresh == null) {
                Call<SPPRepository> call = api.deleteSPP(id_spp);
                call.enqueue(new Callback<SPPRepository>() {
                    @Override
                    public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTransaksi.pauseAnimation();
                            emptyTransaksi.setVisibility(LottieAnimationView.GONE);
                            loadDataSPP();

                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SPPRepository> call, Throwable t) {
                        recyclerView.setVisibility(View.GONE);
                        emptyTransaksi.setAnimation(R.raw.nointernet);
                        emptyTransaksi.playAnimation();
                        emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                        Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });

            } else {
                loadDataSPP();
            }
        });

        NestedScrollView scrollView = view.findViewById(R.id.scroll_data);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        ((HomeAdminActivity) requireActivity()).setSPPRefreshListener(new HomeAdminActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataSPP();
            }
        });

        MaterialButton tambahSPP = view.findViewById(R.id.tambah_spp);
        tambahSPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.preventTwoClick(v);
                Intent intent = new Intent(requireActivity(), TambahSPPActivity.class);
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
            Call<SPPRepository> call = api.searchSPP(searchData);
            call.enqueue(new Callback<SPPRepository>() {
                @Override
                public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                    String value = response.body().getValue();
                    List<SPP> spp = response.body().getResult();

                    if (value.equals("1")) {
                        title_count.setText("(" + spp.size() + ")");
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyTransaksi.pauseAnimation();
                        emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                        adapter = new DataSPPAdapter(getActivity(), spp);
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
                public void onFailure(Call<SPPRepository> call, Throwable t) {
                    recyclerView.setVisibility(View.GONE);
                    emptyTransaksi.setAnimation(R.raw.nointernet);
                    emptyTransaksi.playAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                    Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                    Log.e("DEBUG", "Error: ", t);
                }
            });

        } else {
            loadDataSPP();
        }
    }

    public void loadDataSPP() {
        Call<SPPRepository> call = api.readSPP();
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                List<SPP> spp = response.body().getResult();

                if (value.equals("1")) {
                    title_count.setText("(" + spp.size() + ")");
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyTransaksi.pauseAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                    adapter = new DataSPPAdapter(getActivity(), spp);
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
            public void onFailure(Call<SPPRepository> call, Throwable t) {
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
