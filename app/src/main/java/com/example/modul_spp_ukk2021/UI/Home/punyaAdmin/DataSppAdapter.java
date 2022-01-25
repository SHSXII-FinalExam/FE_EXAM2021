package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaPetugas.DataSiswaAdapter;

import java.util.ArrayList;

public class DataSppAdapter extends RecyclerView.Adapter<DataSppAdapter.ViewhHolder> {
    private ArrayList<DataSpp> arrayList = new ArrayList<>();

    public DataSppAdapter(ArrayList<DataSpp> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public ViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_tahun, parent, false);
        return new DataSppAdapter.ViewhHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewhHolder holder, int position) {
        holder.tahun.setText(arrayList.get(position).getTahun());

        holder.nominal.setText(arrayList.get(position).getNominal());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewhHolder extends RecyclerView.ViewHolder {

        TextView tahun, nominal;

        public ViewhHolder(View itemView) {
            super(itemView);
            tahun = (TextView) itemView.findViewById(R.id.textView);
            nominal = (TextView) itemView.findViewById(R.id.nominal);

        }
    }
}
