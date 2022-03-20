package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
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
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
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

public class DataKelasFragment extends Fragment {
    private DataKelasAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressbar;
    private String nama_kelas, jurusan_kelas;
    private List<Kelas> kelas = new ArrayList<>();

    public DataKelasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_fragment_data_kelas, container, false);

        adapter = new DataKelasAdapter(getActivity(), kelas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_kelas);
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

        adapter.setOnRecyclerViewItemClickListener((id_kelas, refresh) -> {
            if (id_kelas != null && refresh == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                        Call<KelasRepository> call = api.deleteKelas(id_kelas);
                        call.enqueue(new Callback<KelasRepository>() {
                            @Override
                            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                                String value = response.body().getValue();
                                if (value.equals("1")) {
                                    loadDataKelas();
                                }
                            }

                            @Override
                            public void onFailure(Call<KelasRepository> call, Throwable t) {
                                Log.e("DEBUG", "Error: ", t);
                            }
                        });
                    }
                }, 500);
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

        ((HomeAdminActivity) getActivity()).setKelasRefreshListener(new HomeAdminActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataKelas();
            }
        });

        MaterialButton tambahKelas = view.findViewById(R.id.tambah_kelas);
        tambahKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pa_dialog_tambah_kelas, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                builder.setView(view);
                final Button nama = (Button) view.findViewById(R.id.nama_kelas);
                final EditText namanomor = (EditText) view.findViewById(R.id.namanomor_kelas);
                final EditText angkatan = (EditText) view.findViewById(R.id.angkatan_kelas);
                final RadioGroup jurusan = (RadioGroup) view.findViewById(R.id.jurusan_kelas);
                final TextView namajurusan = (TextView) view.findViewById(R.id.namajurusan_kelas);
                final AlertDialog alertDialog = builder.create();

                jurusan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        switch (id) {
                            case R.id.RPL:
                                jurusan_kelas = "RPL";
                                namajurusan.setText(jurusan_kelas);
                                break;
                            case R.id.TKJ:
                                jurusan_kelas = "TKJ";
                                namajurusan.setText(jurusan_kelas);
                                break;
                        }
                    }
                });

                nama.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu dropDownMenu = new PopupMenu(getContext(), nama);
                        dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_kelas, dropDownMenu.getMenu());
                        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                nama.setText(menuItem.getTitle().toString());
                                nama_kelas = menuItem.getTitle().toString();
                                return true;
                            }
                        });
                        dropDownMenu.show();
                    }
                });

                view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (nama_kelas != null && jurusan_kelas != null && angkatan.getText().toString().trim().length() > 0) {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(url)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                            Call<KelasRepository> call = api.createKelas(angkatan.getText().toString(), nama_kelas + " " + jurusan_kelas + " " + namanomor.getText().toString(), jurusan_kelas.trim());
                            call.enqueue(new Callback<KelasRepository>() {
                                @Override
                                public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                                    String value = response.body().getValue();
                                    String message = response.body().getMessage();
                                    if (value.equals("1")) {
                                        loadDataKelas();
                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<KelasRepository> call, Throwable t) {
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
        loadDataKelas();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadDataKelas() {
        progressbar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<KelasRepository> call = api.viewDataKelas();
        call.enqueue(new Callback<KelasRepository>() {
            @Override
            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    progressbar.dismiss();
                    kelas = response.body().getResult();
                    adapter = new DataKelasAdapter(getActivity(), kelas);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);
                }
            }

            @Override
            public void onFailure(Call<KelasRepository> call, Throwable t) {
                progressbar.dismiss();
                Toast.makeText(getContext(), "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
