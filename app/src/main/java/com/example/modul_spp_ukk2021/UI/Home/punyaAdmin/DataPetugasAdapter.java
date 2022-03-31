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
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataPetugasAdapter extends RecyclerView.Adapter<DataPetugasAdapter.ViewHolder> {
    private final Context context;
    private final List<Petugas> petugas;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String id_petugas, String refresh);
    }

    public void setOnRecyclerViewItemClickListener(DataPetugasAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public DataPetugasAdapter(Context context, List<Petugas> petugas) {
        this.context = context;
        this.petugas = petugas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_container_data_petugas, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Petugas petugas = this.petugas.get(position);

        holder.tvNamaPetugas.setText(petugas.getNama_petugas());
        holder.tvLevel.setText("Staff level: " + petugas.getLevel());

//        if (petugas.getUsername().equals(usernameStaff)) {
//            holder.tvNamaPetugas.setText("Anda");
//        }
    }

    @Override
    public int getItemCount() {
        return petugas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaPetugas, tvLevel;
        MaterialCardView tambahPetugas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLevel = itemView.findViewById(R.id.levelStaff);
            tvNamaPetugas = itemView.findViewById(R.id.namaPetugas);
        }
    }
}
