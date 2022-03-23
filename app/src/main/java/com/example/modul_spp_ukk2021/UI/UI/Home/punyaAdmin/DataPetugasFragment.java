package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataPetugasFragment extends Fragment {
    private RecyclerView recyclerView;
    private DataPetugasAdapter adapter;
    private List<Petugas> petugas = new ArrayList<>();

    public DataPetugasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_fragment_data_petugas, container, false);

        adapter = new DataPetugasAdapter(getActivity(), petugas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_petugas);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView);

        ProgressDialog progressbar = new ProgressDialog(getContext());
        progressbar.setMessage("Loading...");
        progressbar.setCancelable(false);
        progressbar.setIndeterminate(true);
        progressbar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressbar.dismiss();

        adapter.setOnRecyclerViewItemClickListener((id_petugas, refresh) -> {
            if (id_petugas != null && refresh == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                        Call<PetugasRepository> call = api.deletePetugas(id_petugas);
                        call.enqueue(new Callback<PetugasRepository>() {
                            @Override
                            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                                String value = response.body().getValue();
                                if (value.equals("1")) {
                                    loadDataPetugas();
                                }
                            }

                            @Override
                            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
                    }
                }, 500);
            } else {
                loadDataPetugas();
            }
        });

        NestedScrollView scrollView = view.findViewById(R.id.scroll_data);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        ((HomeAdminActivity) getActivity()).setPetugasRefreshListener(new HomeAdminActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataPetugas();
            }
        });

        MaterialButton tambahPetugas = view.findViewById(R.id.tambah_petugas);
        tambahPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.preventTwoClick(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pa_dialog_tambah_petugas, v.findViewById(R.id.layoutDialogContainer));
                builder.setView(view);
                final EditText nama_petugas = view.findViewById(R.id.nama_petugas);
                final EditText username_petugas = view.findViewById(R.id.username_petugas);
                final EditText password_petugas = view.findViewById(R.id.password_petugas);
                final AlertDialog alertDialog = builder.create();

                view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (password_petugas.getText().toString().trim().length() > 0) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(url)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                            Call<PetugasRepository> call = api.createPetugas("Petugas", username_petugas.getText().toString(), password_petugas.getText().toString(), nama_petugas.getText().toString());
                            call.enqueue(new Callback<PetugasRepository>() {
                                @Override
                                public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                                    String value = response.body().getValue();
                                    String message = response.body().getMessage();
                                    if (value.equals("1")) {
                                        loadDataPetugas();
                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PetugasRepository> call, Throwable t) {
                                    Log.e("DEBUG", "Error: ", t);
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                view.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPetugas();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadDataPetugas() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<PetugasRepository> call = api.viewDataAllPetugas();
        call.enqueue(new Callback<PetugasRepository>() {
            @Override
            public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    petugas = response.body().getResult();
                    adapter = new DataPetugasAdapter(getActivity(), petugas);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);
                }
            }

            @Override
            public void onFailure(Call<PetugasRepository> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
