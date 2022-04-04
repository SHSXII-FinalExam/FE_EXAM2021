package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin.punyaAdmin;

import static com.example.modul_spp_ukk2021.UI.Home.punyaAdmin.punyaAdmin.LottieAnimationView.getVISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
import com.example.modul_spp_ukk2021.UI.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.DataKelasAdapter;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataKelasFragment extends Fragment {
    private ApiEndPoints api;
    private DataKelasAdapter adapter;
    private RecyclerView recyclerView;
    private String nama_kelas, jurusan_kelas;
    private View emptyTransaksi;
    private final List<Kelas> kelas = new ArrayList<>();

    public DataKelasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.pa_fragment_data_kelas, container, false);

        emptyTransaksi = view.findViewById(R.id.emptyTransaksi);

        adapter = new DataKelasAdapter(getActivity(), kelas);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recycler_kelas);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String url;
        url = null;
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
                            emptyTransaksi.setVisibility(View.GONE);
                            loadDataKelas();

                        } else {
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @SuppressLint("WrongConstant")
                    @Override
                    public void onFailure(Call<KelasRepository> call, Throwable t) {
                        recyclerView.setVisibility(View.GONE);
                        emptyTransaksi.setAnimation(R.raw.nointernet);
                        emptyTransaksi.playAnimation();
                        emptyTransaksi.setVisibility(getVISIBLE());
                        Toast.makeText(requireActivity(), "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
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
                Utils.Utils.preventTwoClick(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.pa_dialog_tambah_kelas, v.findViewById(R.id.layoutDialogContainer));
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button nama = view.findViewById(R.id.nama_kelas);
                EditText angkatan = view.findViewById(R.id.angkatan_kelas);
                RadioGroup jurusan = view.findViewById(R.id.jurusan_kelas);
                EditText namanomor = view.findViewById(R.id.namanomor_kelas);
                TextView namajurusan = view.findViewById(R.id.namajurusan_kelas);

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
                            default:
                                jurusan_kelas = null;
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
                            Call<KelasRepository> call = api.createKelas(angkatan.getText().toString(), nama_kelas + " " + jurusan_kelas + " " + namanomor.getText().toString(), jurusan_kelas.trim());
                            call.enqueue(new Callback<KelasRepository>() {
                                @Override
                                public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                                    String value = response.body().getValue();
                                    String message = response.body().getMessage();

                                    if (value.equals("1")) {
                                        alertDialog.dismiss();
                                        recyclerView.setVisibility(View.VISIBLE);
                                        emptyTransaksi.pauseAnimation();
                                        emptyTransaksi.setVisibility(View.GONE);
                                        loadDataKelas();

                                    } else {
                                        alertDialog.dismiss();
                                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<KelasRepository> call, Throwable t) {
                                    alertDialog.dismiss();
                                    recyclerView.setVisibility(View.GONE);
                                    emptyTransaksi.setAnimation(R.raw.nointernet);
                                    emptyTransaksi.playAnimation();
                                    emptyTransaksi.setVisibility(View.VISIBLE);
                                    Toast.makeText(requireActivity(), "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                                    Log.e("DEBUG", "Error: ", t);
                                }
                            });

                        } else {
                            Toast.makeText(requireActivity(), "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
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
        recyclerView.scheduleLayoutAnimation();
    }

    public void loadDataKelas() {
        Call<KelasRepository> call = api.viewDataKelas();
        call.enqueue(new Callback<KelasRepository>() {
            @Override
            public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Kelas> kelas = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyTransaksi.pauseAnimation();
                    emptyTransaksi.setVisibility(View.GONE);

                    adapter = new DataKelasAdapter(getActivity(), kelas);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyTransaksi.setAnimation(R.raw.nodata);
                    emptyTransaksi.playAnimation();
                    emptyTransaksi.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KelasRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyTransaksi.setAnimation(R.raw.nointernet);
                emptyTransaksi.playAnimation();
                emptyTransaksi.setVisibility(View.GONE);
                Toast.makeText(requireActivity(), "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}
