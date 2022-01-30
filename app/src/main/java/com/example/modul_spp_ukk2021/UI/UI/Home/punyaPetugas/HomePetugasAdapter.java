package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePetugasAdapter extends RecyclerView.Adapter<HomePetugasAdapter.ViewHolder> {
    private Context context;
    private List<Pembayaran> pembayaran;

    // data is passed into the constructor
    public HomePetugasAdapter(Context context, List<Pembayaran> pembayaran) {
        this.context = context;
        this.pembayaran = pembayaran;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data_history, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pembayaran pembayaran = this.pembayaran.get(position);

        holder.textViewNama.setText(pembayaran.getNama());

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        holder.textViewNominal.setText(format.format(pembayaran.getNominal()));

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd MMMM yyyy", localeID);
        String strDt = simpleDate.format(pembayaran.getTgl_bayar());
        holder.textViewTahun.setText(strDt);

        if (position == this.pembayaran.size() - 1) {
            HomePetugasFragment homePetugasFragment = new HomePetugasFragment();
            homePetugasFragment.getVariable();
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return pembayaran.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.namaSiswa)
        TextView textViewNama;
        @BindView(R.id.tahun)
        TextView textViewTahun;
        @BindView(R.id.nominal)
        TextView textViewNominal;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
