package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
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
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.TransaksiActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.ViewHolder> {
    private final Context context;
    private final List<Siswa> siswa;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String nisn, String refresh);
    }

    public void setOnRecyclerViewItemClickListener(DataSiswaAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public DataSiswaAdapter(Context context, List<Siswa> siswa) {
        this.context = context;
        this.siswa = siswa;
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
        Siswa siswa = this.siswa.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        String[] lastWord = siswa.getNama_kelas().split(" ");
        holder.tvInisial.setText(lastWord[lastWord.length - 2] + " " + lastWord[lastWord.length - 1]);
        holder.tvNama.setText(siswa.getNama());
        holder.tvKelas.setText("NISN: " + siswa.getNisn());

        if (siswa.getJurusan().equalsIgnoreCase("rpl")) {
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey700));
        } else if (siswa.getJurusan().equalsIgnoreCase("tkj")) {
            holder.cardInisial.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red900));
        }

        holder.ivMore.setVisibility(View.VISIBLE);
        holder.ivMore.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_container, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(siswa.getNisn(), null);
                    } else if (item.getItemId() == R.id.action_edit) {
                        Intent intent = new Intent(context, TambahSiswaActivity.class);
                        intent.putExtra("nisn", siswa.getNisn());
                        intent.putExtra("nis", siswa.getNis());
                        intent.putExtra("nama", siswa.getNama());
                        intent.putExtra("alamat", siswa.getAlamat());
                        intent.putExtra("ponsel", siswa.getNo_telp());

                        intent.putExtra("id_kelas", siswa.getId_kelas());
                        intent.putExtra("angkatan", siswa.getAngkatan());
                        intent.putExtra("nama_kelas", siswa.getNama_kelas());
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

            ((TextView) view.findViewById(R.id.tvFillNama)).setText(siswa.getNama());
            ((TextView) view.findViewById(R.id.tvNISN)).setText("NISN    : " + siswa.getNisn());
            ((TextView) view.findViewById(R.id.tvNIS)).setText("NIS                    : " + siswa.getNis());
            ((TextView) view.findViewById(R.id.tvKelas2)).setText("Kelas                 : " + siswa.getNama_kelas());
            ((TextView) view.findViewById(R.id.tvFillAlamat)).setText(siswa.getAlamat());
            ((TextView) view.findViewById(R.id.tvNoTelp)).setText("Nomor Ponsel  : " + siswa.getNo_telp());

            view.findViewById(R.id.detailSPP).setVisibility(View.GONE);
            view.findViewById(R.id.detailStaff).setVisibility(View.GONE);
            view.findViewById(R.id.detailKelas).setVisibility(View.GONE);
            view.findViewById(R.id.detailSiswa).setVisibility(View.VISIBLE);
            view.findViewById(R.id.clear4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            view.findViewById(R.id.btnTransaksi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.preventTwoClick(view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, TransaksiActivity.class);
                            intent.putExtra("nisnSiswa", siswa.getNisn());
                            context.startActivity(intent);
                        }
                    }, 400);
                }
            });

            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        });
    }

    @Override
    public int getItemCount() {
        return siswa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMore;
        TextView tvNama, tvKelas, tvInisial;
        MaterialCardView cardView, cardInisial;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv1);
            tvKelas = itemView.findViewById(R.id.tv2);
            ivMore = itemView.findViewById(R.id.more);
            cardView = itemView.findViewById(R.id.cardView);
            tvInisial = itemView.findViewById(R.id.inisial);
            cardInisial = itemView.findViewById(R.id.cardInisial);
        }
    }
}
