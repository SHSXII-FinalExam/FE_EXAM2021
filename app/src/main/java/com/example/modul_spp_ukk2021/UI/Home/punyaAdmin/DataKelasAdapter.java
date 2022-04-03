package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.Kelas;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataKelasAdapter extends RecyclerView.Adapter<DataKelasAdapter.ViewHolder> {
    private final Context context;
    private final List<Kelas> kelas;

    public DataKelasAdapter(Context context, List<Kelas> kelas) {
        this.context = context;
        this.kelas = kelas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_container_data_kelas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Kelas kelas = this.kelas.get(position);

        holder.tvNamaKelas.setText(kelas.getNama_kelas());
        holder.tvJurusan.setText("Jurusan " + kelas.getJurusan());
        holder.tvAngkatan.setText("Angkatan " + kelas.getAngkatan());

        holder.tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, TambahKelasActivity.class);
            intent.putExtra("id_kelas", kelas.getId_kelas());
            intent.putExtra("jurusan", kelas.getJurusan());
            intent.putExtra("angkatan", kelas.getAngkatan());
            intent.putExtra("nama_kelas", kelas.getNama_kelas());
            context.startActivity(intent);
        });

        holder.CardKelas.setOnClickListener(v -> {
            Intent intent = new Intent(context, DataSiswaActivity.class);
            intent.putExtra("id_petugas", ((DataKelasActivity) context).getIntent().getStringExtra("id_petugas"));
            intent.putExtra("id_kelas", kelas.getId_kelas());
            intent.putExtra("nama_kelas", kelas.getNama_kelas());
            intent.putExtra("angkatan", kelas.getAngkatan());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return kelas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView CardKelas;
        TextView tvAngkatan, tvNamaKelas, tvJurusan, tvEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEdit = itemView.findViewById(R.id.edit);
            CardKelas = itemView.findViewById(R.id.CardKelas);
            tvNamaKelas = itemView.findViewById(R.id.namaKelas);
            tvJurusan = itemView.findViewById(R.id.jurusan);
            tvAngkatan = itemView.findViewById(R.id.angkatan);
        }
    }
}