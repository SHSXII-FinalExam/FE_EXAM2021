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
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.DataPembayaranActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.ViewHolder> {
    private final Context context;
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

        String id_petugas = ((DataSiswaActivity) context).getIntent().getStringExtra("id_petugas");

        holder.tvNama.setText(siswa.getNama());
        holder.tvNISN.setText(siswa.getNisn());
        holder.tvAngkatan.setText("Angkatan " + siswa.getAngkatan());

        holder.editSiswa.setOnClickListener(v -> {
            Intent intent = new Intent(context, TambahSiswaActivity.class);
            intent.putExtra("id_petugas", id_petugas);
            intent.putExtra("id_kelas", siswa.getId_kelas());
            intent.putExtra("angkatan", siswa.getAngkatan());
            intent.putExtra("nama_kelas", siswa.getNama_kelas());

            intent.putExtra("nama_siswa", siswa.getNama());
            intent.putExtra("NISN_siswa", siswa.getNisn());
            intent.putExtra("NIS_siswa", siswa.getNis());
            intent.putExtra("tvKelas", siswa.getNama_kelas());
            intent.putExtra("alamat_siswa", siswa.getAlamat());
            intent.putExtra("ponsel_siswa", siswa.getNo_telp());
            context.startActivity(intent);
        });

        holder.CardSiswa.setOnClickListener(v -> {
            Intent intent = new Intent(context, DataPembayaranActivity.class);
            intent.putExtra("nisnSiswa", siswa.getNisn());
            intent.putExtra("id_petugas", id_petugas);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return siswa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView CardSiswa;
        TextView tvNama, tvNISN, tvAngkatan, editSiswa;

        public ViewHolder(View itemView) {
            super(itemView);
            CardSiswa = itemView.findViewById(R.id.CardSiswa);
            editSiswa = itemView.findViewById(R.id.edit);
            tvNama = itemView.findViewById(R.id.namaSiswa);
            tvNISN = itemView.findViewById(R.id.siswaNISN);
            tvAngkatan = itemView.findViewById(R.id.angkatan);
        }
    }
}
