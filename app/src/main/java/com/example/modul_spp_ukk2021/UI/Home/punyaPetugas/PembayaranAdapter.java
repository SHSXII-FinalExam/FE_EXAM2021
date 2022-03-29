package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PembayaranAdapter extends RecyclerView.Adapter<PembayaranAdapter.ViewHolder> {
    private String strDt;
    private final Context context;
    private final List<Pembayaran> listPembayaran;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String nama_siswa,
                           String nisn,
                           String id_pembayaran,
                           Integer jumlah_bayar,
                           Integer nominal,
                           String tanggalTagihan,
                           String tanggalBayar,
                           String status,
                           String staff,
                           String nama_kelas,
                           Integer kurang_bayar,
                           Integer download);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public PembayaranAdapter(Context context, List<Pembayaran> pembayaran) {
        this.context = context;
        this.listPembayaran = pembayaran;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data_pembayaran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pembayaran pembayaran = listPembayaran.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        DateFormatSymbols symbols = new DateFormatSymbols(localeID);
        String[] monthNames = symbols.getMonths();
        holder.tvBulan.setText(monthNames[pembayaran.getBulan_bayar() - 1] + " " + pembayaran.getTahun_bayar());

        if (pembayaran.getTgl_bayar() != null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("d MMMM yyyy", localeID);
            strDt = simpleDate.format(pembayaran.getTgl_bayar());
        }

        if (pembayaran.getJumlah_bayar() < pembayaran.getNominal() && pembayaran.getJumlah_bayar() > 0) {
            holder.tvNominal.setText(format.format(pembayaran.getKurang_bayar()));
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.kurang));
            holder.tvStatus.setText("Kurang");
            holder.updateData.setBackgroundResource(R.drawable.gradient_yellow);
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.kurang)));
        } else if (pembayaran.getJumlah_bayar().equals(pembayaran.getNominal())) {
            holder.download.setVisibility(View.VISIBLE);
            holder.updateData.setVisibility(View.GONE);
            holder.updateData.setEnabled(false);
            holder.tvNominal.setText("+" + format.format(pembayaran.getNominal()));
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.lunas));
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.cyan)));
        } else {
            holder.tvNominal.setText(format.format(pembayaran.getNominal()));
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.belum));
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.materialCardView.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.belum)));
        }

        holder.updateData.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            mListener.onItemClicked(null,
                    null,
                    pembayaran.getId_pembayaran(),
                    pembayaran.getJumlah_bayar(),
                    pembayaran.getNominal(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    pembayaran.getKurang_bayar(),
                    null);
        });

        holder.download.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            mListener.onItemClicked(
                    pembayaran.getNama(),
                    pembayaran.getNisn(),
                    monthNames[pembayaran.getBulan_bayar() - 1].substring(0, 3) + pembayaran.getTahun_bayar() + pembayaran.getId_pembayaran(),
                    pembayaran.getJumlah_bayar(),
                    null,
                    "SPP " + monthNames[pembayaran.getBulan_bayar() - 1] + " " + pembayaran.getTahun_bayar(),
                    strDt,
                    pembayaran.getStatus_bayar(),
                    pembayaran.getNama_petugas(),
                    pembayaran.getNama_kelas(),
                    pembayaran.getKurang_bayar(),
                    1);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listPembayaran.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView download;
        MaterialButton updateData;
        MaterialCardView materialCardView;
        TextView tvBulan, tvNominal, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.status);
            tvNominal = itemView.findViewById(R.id.nominal);
            download = itemView.findViewById(R.id.download);
            tvBulan = itemView.findViewById(R.id.namaBulan);
            updateData = itemView.findViewById(R.id.updateData);
            materialCardView = itemView.findViewById(R.id.materialCardView);
        }
    }
}
