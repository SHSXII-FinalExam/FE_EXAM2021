package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.Siswa;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomePetugasAdapter extends RecyclerView.Adapter<HomePetugasAdapter.ViewHolder> {
    private Context context;
    private List<Siswa> siswa;

    // data is passed into the constructor
    HomePetugasAdapter(Context context, List<Siswa> siswa) {
        this.siswa = siswa;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data_siswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Siswa siswa = this.siswa.get(position);

        holder.tvNama.setText(siswa.getNama());
        holder.tvKelas.setText(siswa.getNama_kelas());

        holder.btnSiswa.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, DataPembayaranActivity.class);
                    String id_petugas = ((HomePetugasActivity) v.getContext()).getIntent().getStringExtra("id_petugas");

                    intent.putExtra("nisnSiswa", siswa.getNisn());
                    intent.putExtra("id_petugas", id_petugas);
                    context.startActivity(intent);
                }
            }, 400);
        });
    }

    @Override
    public int getItemCount() {
        return siswa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView btnSiswa;
        TextView tvNama, tvKelas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvKelas = itemView.findViewById(R.id.kelas);
            tvNama = itemView.findViewById(R.id.namaSiswa);
            btnSiswa = itemView.findViewById(R.id.CardSiswa);
        }
    }
}
