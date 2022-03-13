package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;
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
    private TextView result;
    private DataSPPAdapter adapter;
    private RecyclerView recyclerView;
    private List<SPP> spp = new ArrayList<>();

    public DataSPPFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_activity_data_spp, container, false);

        adapter = new DataSPPAdapter(getActivity(), spp);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_spp);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener((id_spp) -> {
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
        });

        ScrollView scrollView = view.findViewById(R.id.scroll_dataspp);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

        MaterialButton tambahPetugas = view.findViewById(R.id.tambah_spp);
        tambahPetugas.setOnClickListener(new View.OnClickListener() {
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

    public void loadDataSPP() {
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
                    spp = response.body().getResult();
                    adapter = new DataSPPAdapter(getActivity(), spp);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SPPRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
