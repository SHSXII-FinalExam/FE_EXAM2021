package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.Pembayaran;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistorySiswaAdapter extends RecyclerView.Adapter<HistorySiswaAdapter.ViewHolder> {
    private final Context context;
    private final List<Pembayaran> pembayaran;

    public HistorySiswaAdapter(Context context, List<Pembayaran> pembayaran) {
        this.context = context;
        this.pembayaran = pembayaran;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siswa_container_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pembayaran pembayaran = this.pembayaran.get(position);

        holder.tvStatus.setText(pembayaran.getStatus_bayar());

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        DateFormatSymbols symbols = new DateFormatSymbols(localeID);
        String[] monthNames = symbols.getMonths();
        holder.tvBulan.setText(monthNames[pembayaran.getBulan_bayar() - 1]);

        holder.tvNominal.setText("+" + format.format(pembayaran.getNominal()));

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy", localeID);
        String strDt = simpleDate.format(pembayaran.getTgl_bayar());
        holder.tvTanggal.setText(strDt);
    }

    @Override
    public int getItemCount() {
        return pembayaran.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView layout_container;
        TextView tvBulan, tvNominal, tvStatus, tvTanggal;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.status);
            tvBulan = itemView.findViewById(R.id.namaBulan);
            tvNominal = itemView.findViewById(R.id.nominal);
            tvTanggal = itemView.findViewById(R.id.tanggal);
            layout_container = itemView.findViewById(R.id.layout_container);
        }
    }
}
