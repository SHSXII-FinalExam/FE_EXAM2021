package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HomePetugasAdapter extends RecyclerView.Adapter<HomePetugasAdapter.ViewHolder> {
    private final Context context;
    private final List<Siswa> siswa;

    public HomePetugasAdapter(Context context, List<Siswa> siswa) {
        this.siswa = siswa;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pp_container_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Siswa siswa = this.siswa.get(position);
        String romawiKelas = siswa.getNama_kelas().substring(0, siswa.getNama_kelas().indexOf(' '));

        holder.tvInisial.setText(romawiKelas);
        holder.tvNama.setText(siswa.getNama());
        holder.tvKelas.setText(siswa.getNama_kelas());
        holder.ivMore.setVisibility(View.GONE);

        if (romawiKelas.equalsIgnoreCase("x")) {
            holder.tvInisial.setText(romawiKelas + "  ");
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color200));
        } else if (romawiKelas.equalsIgnoreCase("xi")) {
            holder.tvInisial.setText(romawiKelas + " ");
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color300));
        } else if (romawiKelas.equalsIgnoreCase("xii")) {
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color400));
        }

        holder.cardView.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            Intent intent = new Intent(context, TransaksiActivity.class);
            intent.putExtra("nisnSiswa", siswa.getNisn());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return siswa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMore;
        TextView tvNama, tvKelas, tvInisial;
        MaterialCardView cardView, cardInisial;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv1);
            ivMore = itemView.findViewById(R.id.more);
            tvKelas = itemView.findViewById(R.id.tv2);
            tvInisial = itemView.findViewById(R.id.inisial);
            cardView = itemView.findViewById(R.id.cardView);
            cardInisial = itemView.findViewById(R.id.cardInisial);
        }
    }
}
