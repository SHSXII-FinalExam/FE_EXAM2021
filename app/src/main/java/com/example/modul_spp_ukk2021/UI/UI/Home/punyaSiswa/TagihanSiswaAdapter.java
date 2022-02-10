package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagihanSiswaAdapter extends RecyclerView.Adapter<TagihanSiswaAdapter.ViewHolder> {
    private Context context;
    private List<Pembayaran> pembayaran;

    // data is passed into the constructor
    public TagihanSiswaAdapter(Context context, List<Pembayaran> pembayaran) {
        this.context = context;
        this.pembayaran = pembayaran;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ps_container_data_tagihan, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pembayaran pembayaran = this.pembayaran.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        if (pembayaran.getKurang_bayar() == 0){
            holder.tvNominal.setText(format.format(pembayaran.getNominal()));
            holder.tvNominal.setTextColor(Color.parseColor("#F14D6F"));
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#F14D6F")));
        } else {
            holder.tvNominal.setText(format.format(pembayaran.getKurang_bayar()));
            holder.tvNominal.setTextColor(Color.parseColor("#FFC700"));
            holder.tvStatus.setText("Kurang");
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFC700")));
        }

        DateFormatSymbols symbols = new DateFormatSymbols(localeID);
        String[] monthNames = symbols.getMonths();
        holder.tvBulan.setText(monthNames[pembayaran.getBulan_bayar() - 1]);

        if (pembayaran.getTgl_bayar() != null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy", localeID);
            String strDt = simpleDate.format(pembayaran.getTgl_bayar());
            holder.tvTanggal.setText(strDt);
        } else {
            holder.tvTanggal.setText("--/--/----");
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return pembayaran.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBulan, tvNominal, tvStatus, tvTanggal;
        MaterialCardView materialCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            tvBulan = itemView.findViewById(R.id.namaBulan);
            tvNominal = itemView.findViewById(R.id.nominal);
            tvStatus = itemView.findViewById(R.id.status);
            tvTanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}