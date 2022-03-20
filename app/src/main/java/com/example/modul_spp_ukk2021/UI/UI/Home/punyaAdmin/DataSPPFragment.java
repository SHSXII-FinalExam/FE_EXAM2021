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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.PembayaranActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.HomeSiswaActivity;
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
    private DataSPPAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressbar;
    private List<SPP> spp = new ArrayList<>();

    public DataSPPFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_fragment_data_spp, container, false);

        adapter = new DataSPPAdapter(getActivity(), spp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_spp);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        runLayoutAnimation(recyclerView);

        progressbar = new ProgressDialog(getContext());
        progressbar.setMessage("Loading...");
        progressbar.setCancelable(false);
        progressbar.setIndeterminate(true);
        progressbar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressbar.dismiss();

        adapter.setOnRecyclerViewItemClickListener((id_spp, refresh) -> {
            if (id_spp != null && refresh == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                        Call<SPPRepository> call = api.deleteSPP(id_spp);
                        call.enqueue(new Callback<SPPRepository>() {
                            @Override
                            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                                String value = response.body().getValue();
                                if (value.equals("1")) {
                                    loadDataSPP();
                                }
                            }

                            @Override
                            public void onFailure(Call<SPPRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
                    }
                }, 500);
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

        ((HomeAdminActivity) getActivity()).setSPPRefreshListener(new HomeAdminActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataSPP();
            }
        });

        MaterialButton tambahSPP = view.findViewById(R.id.tambah_spp);
        tambahSPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pa_dialog_tambah_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                builder.setView(view);
                final EditText angkatan = (EditText) view.findViewById(R.id.angkatan_spp);
                final EditText tahun = (EditText) view.findViewById(R.id.tahun_spp);
                final EditText nominal = (EditText) view.findViewById(R.id.nominal_spp);
                final AlertDialog alertDialog = builder.create();
                view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                        Call<SPPRepository> call = api.createSPP(angkatan.getText().toString(), tahun.getText().toString(), nominal.getText().toString());
                        call.enqueue(new Callback<SPPRepository>() {
                            @Override
                            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();
                                if (value.equals("1")) {
                                    loadDataSPP();
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SPPRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
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
        loadDataSPP();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadDataSPP() {
        progressbar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<SPPRepository> call = api.viewDataSPP();
        call.enqueue(new Callback<SPPRepository>() {
            @Override
            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    progressbar.dismiss();
                    spp = response.body().getResult();
                    adapter = new DataSPPAdapter(getActivity(), spp);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                progressbar.dismiss();
                Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
