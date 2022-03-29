package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.ViewHolder> {
    private Context context;
    private final List<Siswa> siswa;

    public DataSiswaAdapter(Context context, List<Siswa> siswa) {
        this.context = context;
        this.siswa = siswa;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_container_data_siswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Siswa siswa = this.siswa.get(position);

        holder.textViewNama.setText(siswa.getNama());
        holder.textViewKelas.setText(siswa.getNama_kelas());

        holder.cardSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PembayaranActivity.class);
                intent.putExtra("siswaNama", siswa.getNama());
                context.startActivity(intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return siswa.size();
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardSiswa;
        TextView textViewNama, textViewKelas;

        public ViewHolder(View itemView) {
            super(itemView);
            cardSiswa = itemView.findViewById(R.id.tanggal);
            textViewKelas = itemView.findViewById(R.id.nominal);
            textViewNama = itemView.findViewById(R.id.namaBulan);
        }
    }
}