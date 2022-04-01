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
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DataSPPAdapter extends RecyclerView.Adapter<DataSPPAdapter.ViewHolder> {
    private final List<SPP> spp;
    private final Context context;

    public DataSPPAdapter(Context context, List<SPP> spp) {
        this.context = context;
        this.spp = spp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_container_data_spp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SPP spp = this.spp.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        holder.tvTahun.setText("Tahun " + spp.getTahun());
        holder.tvAngkatan.setText("Angkatan " + spp.getAngkatan());
        holder.tvNominal.setText(format.format(spp.getNominal()));

        holder.editSPP.setOnClickListener(v -> {
            Intent intent = new Intent(context, TambahSPPActivity.class);
            intent.putExtra("angkatan_spp", spp.getAngkatan());
            intent.putExtra("tahun_spp", spp.getTahun());
            intent.putExtra("nominal_spp", spp.getNominal().toString());
            intent.putExtra("id_spp", spp.getId_spp());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return spp.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView editSPP;
        TextView tvTahun, tvNominal, tvAngkatan;

        public ViewHolder(View itemView) {
            super(itemView);
            editSPP = itemView.findViewById(R.id.edit);
            tvTahun = itemView.findViewById(R.id.tahunSPP);
            tvAngkatan = itemView.findViewById(R.id.angkatan);
            tvNominal = itemView.findViewById(R.id.namaSiswa);
        }
    }
}