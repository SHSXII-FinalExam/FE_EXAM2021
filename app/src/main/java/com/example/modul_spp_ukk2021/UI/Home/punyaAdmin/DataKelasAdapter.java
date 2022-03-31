package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataKelasAdapter extends RecyclerView.Adapter<DataKelasAdapter.ViewHolder> {
    private final Context context;
    private final List<Kelas> kelas;
    private String nama, jurusan_kelas;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String id_spp, String refresh);
    }

    public void setOnRecyclerViewItemClickListener(DataKelasAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Kelas kelas = this.kelas.get(position);

        holder.tvNamaKelas.setText(kelas.getNama_kelas());
    }

    @Override
    public int getItemCount() {
        return kelas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaKelas;
        MaterialCardView tambahPetugas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaKelas = itemView.findViewById(R.id.textView24);
            tambahPetugas = itemView.findViewById(R.id.tambahPetugas);
        }
    }
}