package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataPetugasAdapter extends RecyclerView.Adapter<DataPetugasAdapter.ViewHolder> {

    private Context context;
    private List<Petugas> petugas;

    // data is passed into the constructor
    public DataPetugasAdapter(Context context, List<Petugas> petugas) {
        this.context = context;
        this.petugas = petugas;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_data_petugas, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Petugas petugas = this.petugas.get(position);

        holder.textViewNama.setText(petugas.getNama_petugas());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return petugas.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.namaPetugas)
        TextView textViewNama;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
