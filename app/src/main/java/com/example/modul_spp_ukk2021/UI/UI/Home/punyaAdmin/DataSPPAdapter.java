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
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DataSPPAdapter extends RecyclerView.Adapter<DataSPPAdapter.ViewHolder> {
    private final List<SPP> spp;
    private final Context context;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(Integer id_spp, String refresh);
    }

    public void setOnRecyclerViewItemClickListener(DataSPPAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public DataSPPAdapter(Context context, List<SPP> spp) {
        this.context = context;
        this.spp = spp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pa_container_data_spp, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SPP spp = this.spp.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        holder.tv1.setText("Tahun " + spp.getTahun());
        holder.tv3.setText("Angkatan " + spp.getAngkatan());
        holder.tv2.setText(format.format(spp.getNominal()));

        holder.ivMore.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_container, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(spp.getId_spp(), null);
                    } else if (item.getItemId() == R.id.action_edit) {
                        Intent intent = new Intent(context, TambahSPPActivity.class);
                        intent.putExtra("id", spp.getId_spp());
                        intent.putExtra("tahun", spp.getTahun());
                        intent.putExtra("angkatan", spp.getAngkatan());
                        intent.putExtra("nominal", spp.getNominal().toString());
                        context.startActivity(intent);
                    }
                    return true;
                }
            });
            popup.show();
        });

        holder.detailSPP.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_detail, v.findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ((TextView) view.findViewById(R.id.tvTahun)).setText("Tahun SPP : " + spp.getTahun());
            ((TextView) view.findViewById(R.id.tvNominal)).setText("Nominal      : " + format.format(spp.getNominal()));
            ((TextView) view.findViewById(R.id.tvAngkatan)).setText("Angkatan    : " +  spp.getAngkatan());

            view.findViewById(R.id.detailSiswa).setVisibility(View.GONE);
            view.findViewById(R.id.detailKelas).setVisibility(View.GONE);
            view.findViewById(R.id.detailStaff).setVisibility(View.GONE);
            view.findViewById(R.id.detailSPP).setVisibility(View.VISIBLE);
            view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
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
        return spp.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMore;
        TextView tv1, tv2, tv3;
        MaterialCardView detailSPP;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            ivMore = itemView.findViewById(R.id.ivMore);
            detailSPP = itemView.findViewById(R.id.detail);
        }
    }
}
