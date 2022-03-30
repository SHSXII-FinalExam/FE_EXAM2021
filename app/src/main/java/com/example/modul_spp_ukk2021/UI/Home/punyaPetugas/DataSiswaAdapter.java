package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.ViewHolder> {
    private Context context;
    private List<Siswa> siswa;
    private DataSiswaFragment clickListener;

    // data is passed into the constructor
    public DataSiswaAdapter(Context context, List<Siswa> siswa) {
        this.context = context;
        this.siswa = siswa;
    }

    public DataSiswaAdapter(Context context, ArrayList<String> animalNames) {

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data_siswa, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Siswa siswa = this.siswa.get(position);

        holder.textViewNama.setText(siswa.getNama());
        holder.textViewKelas.setText(siswa.getNama_kelas());

        holder.cardSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KonfirmasiPetugasActivity.class);
                intent.putExtra("siswaNama", siswa.getNama());
                context.startActivity(intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return siswa.size();
    }

    public void setClickListener(DataSiswaFragment clickListener) {
        this.clickListener = clickListener;
    }

    public DataSiswaFragment getClickListener() {
        return clickListener;
    }

    public String getItem(int position) {
        return null;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.namaSiswa)
        TextView textViewNama;
        @BindView(R.id.textView3)
        TextView textViewKelas;
        @BindView(R.id.CardSiswa)
        MaterialCardView cardSiswa;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}