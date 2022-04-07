package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DataPetugasAdapter extends RecyclerView.Adapter<DataPetugasAdapter.ViewHolder> {
    private final Context context;
    private final List<Petugas> petugas;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String id_petugas, String refresh);
    }

    public void setOnRecyclerViewItemClickListener(DataPetugasAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public DataPetugasAdapter(Context context, List<Petugas> petugas) {
        this.context = context;
        this.petugas = petugas;
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
        Petugas petugas = this.petugas.get(position);

        SharedPreferences sharedprefs = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String usernameStaff = sharedprefs.getString("usernameStaff", null);
        String passwordStaff = sharedprefs.getString("passwordStaff", null);

        holder.tvLevel.setText(petugas.getLevel());
        holder.tvNama.setText(petugas.getNama_petugas());
        holder.tvUsername.setText("Username: " + petugas.getUsername());
        holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey500));

        if (petugas.getLevel().equalsIgnoreCase("admin")) {
            if (petugas.getUsername().equals(usernameStaff)) {
                holder.tvLevel.setText(" Anda ");
            }

            holder.ivMore.setVisibility(View.GONE);
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey700));
        }

        holder.ivMore.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_customcard, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(petugas.getId_petugas(), null);

                    } else if (item.getItemId() == R.id.action_edit) {
                        Intent intent = new Intent(context, TambahPetugasActivity.class);
                        intent.putExtra("id", petugas.getId_petugas());
                        intent.putExtra("nama", petugas.getNama_petugas());
                        intent.putExtra("username", petugas.getUsername());
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

            ((TextView) view.findViewById(R.id.tvFillNama2)).setText(petugas.getNama_petugas());
            ((TextView) view.findViewById(R.id.tvLevel)).setText("Staff level : " + petugas.getLevel());
            ((TextView) view.findViewById(R.id.tvUsername)).setText("Username : " + petugas.getUsername());
            ((TextView) view.findViewById(R.id.tvPassword2)).setText("Password  : Encrypted");

            if (petugas.getUsername().equals(usernameStaff)) {
                ((TextView) view.findViewById(R.id.tvPassword2)).setText("Password  : " + passwordStaff);
            }

            view.findViewById(R.id.detailSiswa).setVisibility(View.GONE);
            view.findViewById(R.id.detailSPP).setVisibility(View.GONE);
            view.findViewById(R.id.detailKelas).setVisibility(View.GONE);
            view.findViewById(R.id.detailStaff).setVisibility(View.VISIBLE);
            view.findViewById(R.id.clear2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        });
    }

    @Override
    public int getItemCount() {
        return petugas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMore;
        TextView tvNama, tvUsername, tvLevel;
        MaterialCardView cardView, cardInisial;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv1);
            tvUsername = itemView.findViewById(R.id.tv2);
            ivMore = itemView.findViewById(R.id.more);
            tvLevel = itemView.findViewById(R.id.inisial);
            cardView = itemView.findViewById(R.id.cardView);
            cardInisial = itemView.findViewById(R.id.cardInisial);
        }
    }
}
