package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Repository.SiswaRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataSiswaFragment extends Fragment {
    private DataSiswaAdapter adapter;
    private List<Siswa> siswa = new ArrayList<>();

    @BindView(R.id.recyclerDataSiswa)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.admin_fragment_data_siswa, container, false);
        ButterKnife.bind(v);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerDataSiswa);
        adapter = new DataSiswaAdapter(getContext(), siswa);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        ScrollView scrollView = v.findViewById(R.id.scroll_datasiswa);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

//        EditText SearchSiswa = (EditText) v.findViewById(R.id.searchSiswa);
//        SearchSiswa.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() >= 1) {
//                    recyclerView.setVisibility(View.GONE);
//                    String newText = s.toString().trim();
//
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(url)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
//                    Call<SiswaRepository> call = api.search(newText);
//                    call.enqueue(new Callback<SiswaRepository>() {
//                        @Override
//                        public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
//                            String value = response.body().getValue();
//                            recyclerView.setVisibility(View.VISIBLE);
//                            if (value.equals("1")) {
//                                siswa = response.body().getResult();
//                                adapter = new DataSiswaAdapter(getActivity(), siswa);
//                                recyclerView.setAdapter(adapter);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<SiswaRepository> call, Throwable t) {
//                            Log.e("DEBUG", "Error: ", t);
//                        }
//                    });
//                } else {
//                    loadDataPembayaran();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    private void loadDataPembayaran() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<SiswaRepository> call = api.viewDataSiswa();
        call.enqueue(new Callback<SiswaRepository>() {
            @Override
            public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    siswa = response.body().getResult();
                    adapter = new DataSiswaAdapter(getActivity(), siswa);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}