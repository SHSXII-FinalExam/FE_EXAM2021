package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Home.punyaSiswa.HomeSiswaAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataPetugasAdapter extends RecyclerView.Adapter<DataPetugasAdapter.ViewhHolder> {

    private ArrayList<DataPetugas> arrayList = new ArrayList<>();

    public DataPetugasAdapter(ArrayList<DataPetugas> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public ViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_nama_tagihan, parent, false);
        return new ViewhHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewhHolder holder, int position) {
        holder.nama_petugas.setText(arrayList.get(position).getNama_petugas());
        holder.level.setText(arrayList.get(position).getLevel());
        holder.jam_kerja.setText(arrayList.get(position).getJam_kerja());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewhHolder extends RecyclerView.ViewHolder {

        TextView nama_petugas, level, jam_kerja;

        public ViewhHolder(View itemView) {
            super(itemView);
            nama_petugas = (TextView) itemView.findViewById(R.id.textView);
            level = (TextView) itemView.findViewById(R.id.textView2);
            jam_kerja = (TextView) itemView.findViewById(R.id.textView3);
        }
    }
}
