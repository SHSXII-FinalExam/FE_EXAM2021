package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistorySiswaAdapter extends RecyclerView.Adapter<HistorySiswaAdapter.ViewHolder> {
    private Context context;
    private List<Pembayaran> pembayaran;

    // data is passed into the constructor
    public HistorySiswaAdapter(Context context, List<Pembayaran> pembayaran) {
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

        holder.tvStatus.setText(pembayaran.getStatus_bayar());

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        holder.tvNominal.setText(format.format(pembayaran.getNominal()));

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

        public ViewHolder(View itemView) {
            super(itemView);
            tvBulan = itemView.findViewById(R.id.namaBulan);
            tvNominal = itemView.findViewById(R.id.nominal);
            tvStatus = itemView.findViewById(R.id.status);
            tvTanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
