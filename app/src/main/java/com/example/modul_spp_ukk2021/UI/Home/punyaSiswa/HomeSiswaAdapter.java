package com.example.modul_spp_ukk2021.UI.Home.punyaSiswa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.example.modul_spp_ukk2021.R;
import java.util.ArrayList;

public class HomeSiswaAdapter extends RecyclerView.Adapter<HomeSiswaAdapter.ViewhHolder>{

    private ArrayList<DataSiswa> arrayList = new ArrayList<>();

    public HomeSiswaAdapter(ArrayList<DataSiswa> arrayList){
        this.arrayList = arrayList;
    }


    @Override
    public ViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_nama_tagihan, parent, false);
        return new ViewhHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewhHolder holder, int position) {
        holder.nama.setText(arrayList.get(position).getNama());
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd MMMM yyyy", localeID);
        holder.tgl_bayar.setText(simpleDate.format(arrayList.get(position).getTgl_bayar()));

        holder.nominal.setText(format.format(arrayList.get(position).getNominal()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewhHolder extends RecyclerView.ViewHolder {

        TextView nama, nominal, tgl_bayar;

        public ViewhHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.textView);
            nominal = (TextView) itemView.findViewById(R.id.textView2);
            tgl_bayar = (TextView) itemView.findViewById(R.id.textView3);
        }
    }
}
