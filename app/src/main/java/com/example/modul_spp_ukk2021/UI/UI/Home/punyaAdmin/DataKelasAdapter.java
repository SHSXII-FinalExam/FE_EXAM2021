package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataKelasAdapter extends RecyclerView.Adapter<DataKelasAdapter.ViewHolder> {
    private final Context context;
    private final List<Kelas> kelas;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String id_spp, String refresh);
    }

    public void setOnRecyclerViewItemClickListener(DataKelasAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public DataKelasAdapter(Context context, List<Kelas> kelas) {
        this.context = context;
        this.kelas = kelas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pp_container_data, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Kelas kelas = this.kelas.get(position);

        holder.tvKelas.setText(kelas.getNama_kelas());
        holder.tvAngkatan.setText("Angkatan " + kelas.getAngkatan());
        holder.tvJurusan.setText(kelas.getJurusan());

        if (kelas.getJurusan().equalsIgnoreCase("rpl")) {
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey700));
        } else if (kelas.getJurusan().equalsIgnoreCase("tkj")) {
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red900));
        }

        holder.ivMore.setVisibility(View.VISIBLE);
        holder.ivMore.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_customcard, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(kelas.getId_kelas(), null);
                    } else if (item.getItemId() == R.id.action_edit) {
                        Intent intent = new Intent(context, TambahKelasActivity.class);
                        intent.putExtra("id", kelas.getId_kelas());
                        intent.putExtra("namakelas", kelas.getNama_kelas().substring(0, kelas.getNama_kelas().indexOf(' ')));
                        intent.putExtra("jurusan", kelas.getJurusan());
                        intent.putExtra("nomorkelas", kelas.getNama_kelas().replaceAll("[^0-9]", ""));
                        intent.putExtra("angkatan", kelas.getAngkatan());
                        context.startActivity(intent);
                    }
                    return true;
                }
            });
            popup.show();
        });

        holder.cardView.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_detail, v.findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ((TextView) view.findViewById(R.id.tvKelas)).setText("Kelas          : " + kelas.getNama_kelas());
            ((TextView) view.findViewById(R.id.tvJurusan)).setText("Jurusan     : " + kelas.getJurusan());
            ((TextView) view.findViewById(R.id.tvAngkatan2)).setText("Angkatan  : " + kelas.getAngkatan());

            view.findViewById(R.id.detailSiswa).setVisibility(View.GONE);
            view.findViewById(R.id.detailSPP).setVisibility(View.GONE);
            view.findViewById(R.id.detailStaff).setVisibility(View.GONE);
            view.findViewById(R.id.detailKelas).setVisibility(View.VISIBLE);
            view.findViewById(R.id.clear3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            view.findViewById(R.id.btnSiswa).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    Intent intent = new Intent(context, DataSiswaActivity.class);
                    intent.putExtra("id_kelas", kelas.getId_kelas());
                    intent.putExtra("nama_kelas", kelas.getNama_kelas());
                    intent.putExtra("angkatan", kelas.getAngkatan());
                    context.startActivity(intent);
                }
            });

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        });
    }

    @Override
    public int getItemCount() {
        return kelas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMore;
        MaterialCardView cardView, cardInisial;
        TextView tvKelas, tvAngkatan, tvJurusan;

        public ViewHolder(View itemView) {
            super(itemView);
            tvKelas = itemView.findViewById(R.id.tv1);
            tvAngkatan = itemView.findViewById(R.id.tv2);
            ivMore = itemView.findViewById(R.id.more);
            cardView = itemView.findViewById(R.id.cardView);
            tvJurusan = itemView.findViewById(R.id.inisial);
            cardInisial = itemView.findViewById(R.id.cardInisial);
        }
    }
}
