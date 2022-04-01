package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Model.Pembayaran;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DataPembayaranAdapter extends RecyclerView.Adapter<DataPembayaranAdapter.ViewHolder> {
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

    public DataPembayaranAdapter(Context context, List<Pembayaran> pembayaran) {
        this.context = context;
        this.listPembayaran = pembayaran;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petugas_container_data_pembayaran, parent, false);
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

        if (pembayaran.getJumlah_bayar() < pembayaran.getNominal() && pembayaran.getJumlah_bayar() > 0) {
            holder.tvStatus.setText("Kurang");
            holder.tvNominal.setText(format.format(pembayaran.getKurang_bayar()));
        } else if (pembayaran.getJumlah_bayar().equals(pembayaran.getNominal())) {
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.tvNominal.setText(format.format(pembayaran.getNominal()));
            holder.tvNominal.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvBulan.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvImage.setBackground(ContextCompat.getDrawable(context, R.drawable.data_sppimage));
            holder.layout_container.setBackground(ContextCompat.getDrawable(context, R.drawable.container2));
        } else {
            holder.tvStatus.setText(pembayaran.getStatus_bayar());
            holder.tvNominal.setText(format.format(pembayaran.getNominal()));
        }

        if (pembayaran.getJumlah_bayar().equals(pembayaran.getNominal())) {
            holder.layout_container.setOnClickListener(null);
        } else {
            holder.layout_container.setOnClickListener(v -> {
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
        }


//        holder.download.setOnClickListener(v -> {
//            mListener.onItemClicked(
//                    pembayaran.getNama(),
//                    pembayaran.getNisn(),
//                    monthNames[pembayaran.getBulan_bayar() - 1].substring(0, 3) + pembayaran.getTahun_bayar() + pembayaran.getId_pembayaran(),
//                    pembayaran.getJumlah_bayar(),
//                    null,
//                    "SPP " + monthNames[pembayaran.getBulan_bayar() - 1] + " " + pembayaran.getTahun_bayar(),
//                    strDt,
//                    pembayaran.getStatus_bayar(),
//                    pembayaran.getNama_petugas(),
//                    pembayaran.getNama_kelas(),
//                    pembayaran.getKurang_bayar(),
//                    1);
//        });
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
        ImageView tvImage;
        ConstraintLayout layout_container;
        TextView tvBulan, tvNominal, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.textView25);
            tvNominal = itemView.findViewById(R.id.textView24);
            tvBulan = itemView.findViewById(R.id.textView23);
            tvImage = itemView.findViewById(R.id.imageView12);
            layout_container = itemView.findViewById(R.id.layout_container);
        }
    }
}
