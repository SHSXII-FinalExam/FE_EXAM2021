package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.DataSiswa2;


import java.util.ArrayList;

public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.ViewhHolder> {
    private ArrayList<DataSiswa2> arrayList = new ArrayList<>();

    public DataSiswaAdapter(ArrayList<DataSiswa2> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public ViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data_siswa, parent, false);
        return new DataSiswaAdapter.ViewhHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewhHolder holder, int position) {
        holder.nama.setText(arrayList.get(position).getNama());
        holder.nama_kelas.setText(arrayList.get(position).getNama_kelas());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewhHolder extends RecyclerView.ViewHolder {

        TextView nama, nama_kelas;

        public ViewhHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.textView);
            nama_kelas = (TextView) itemView.findViewById(R.id.textView3);

        }
    }
}
