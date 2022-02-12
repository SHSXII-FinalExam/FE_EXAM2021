package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PembayaranAdapter extends RecyclerView.Adapter<PembayaranAdapter.ViewHolder> {
    private Context context;
    private List<Pembayaran> listPembayaran;

    // data is passed into the constructor
    public PembayaranAdapter(Context context, List<Pembayaran> pembayaran) {
        this.context = context;
        this.listPembayaran = pembayaran;
    }

    // Define listener member variable
    private static OnRecyclerViewItemClickListener mListener;

    // Define the listener interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String id_pembayaran, Integer jumlah_bayar, Integer nominal);
    }

    // Define the method that allows the parent activity or fragment to define the listener.
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mListener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pp_container_data_pembayaran, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pembayaran pembayaran = listPembayaran.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        if (pembayaran.getJumlah_bayar() < pembayaran.getNominal() && pembayaran.getJumlah_bayar() > 0) {
            holder.tvNominal.setText(format.format(pembayaran.getKurang_bayar()));
            holder.tvNominal.setTextColor(Color.parseColor("#FFC700"));
            holder.tvStatus.setText("Kurang");
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFC700")));
        } else if (pembayaran.getJumlah_bayar().equals(pembayaran.getNominal())) {
            holder.updateData.setVisibility(View.INVISIBLE);
            holder.updateData.setEnabled(false);
            holder.tvNominal.setText(format.format(pembayaran.getNominal()));
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#2EDCB5")));
        } else {
            holder.tvNominal.setText(format.format(pembayaran.getNominal()));
            holder.tvNominal.setTextColor(Color.parseColor("#F14D6F"));
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#F14D6F")));
        }

        DateFormatSymbols symbols = new DateFormatSymbols(localeID);
        String[] monthNames = symbols.getMonths();
        holder.tvBulan.setText(monthNames[pembayaran.getBulan_bayar() - 1]);

        holder.updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(pembayaran.getId_pembayaran(),pembayaran.getJumlah_bayar(),pembayaran.getNominal());
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listPembayaran.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBulan, tvNominal, tvStatus;
        MaterialCardView materialCardView;
        MaterialButton updateData;

        public ViewHolder(View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            updateData = itemView.findViewById(R.id.updateData);
            tvBulan = itemView.findViewById(R.id.namaBulan);
            tvNominal = itemView.findViewById(R.id.nominal);
            tvStatus = itemView.findViewById(R.id.status);
        }
    }
}
