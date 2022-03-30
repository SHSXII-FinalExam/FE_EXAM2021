package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_container_data_spp, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SPP spp = this.spp.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        holder.tvTahun.setText("Tahun " + spp.getTahun());
        holder.tvAngkatan.setText("Angkatan " + spp.getAngkatan());
        holder.tvNominal.setText(format.format(spp.getNominal()));

//        holder.deleteData.setOnClickListener(v -> {
//            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
//            MenuInflater inflater = popup.getMenuInflater();
//            inflater.inflate(R.menu.menu_customcard, popup.getMenu());
//
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (item.getItemId() == R.id.action_delete) {
//                        mListener.onItemClicked(spp.getId_spp(), null);
//                    }
//                    return true;
//                }
//            });
//            popup.show();
//        });
//
//        holder.detailSPP.setOnClickListener(v -> {
//            Utils.preventTwoClick(v);
//            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
//            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_spp, v.findViewById(R.id.layoutDialogContainer));
//            builder.setView(view);
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//            ((TextView) view.findViewById(R.id.tvTahun)).setText("Tahun       : " + spp.getTahun());
//            ((TextView) view.findViewById(R.id.tvNominal)).setText("Nominal   : " + format.format(spp.getNominal()));
//            ((TextView) view.findViewById(R.id.tvAngkatan)).setText("Angkatan : " + spp.getAngkatan());
//
//            view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
//                    View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_edit_spp, v.findViewById(R.id.layoutDialogContainer));
//                    builder.setView(view2);
//                    AlertDialog alertDialog2 = builder.create();
//                    alertDialog2.show();
//
//                    TextView angkatan = view2.findViewById(R.id.angkatan_spp);
//                    TextView tahun = view2.findViewById(R.id.tahun_spp);
//                    EditText nominal = view2.findViewById(R.id.nominal_spp);
//
//                    angkatan.setText(" " + spp.getAngkatan());
//                    tahun.setText(" " + spp.getTahun());
//                    nominal.setText(spp.getNominal().toString());
//
//                    view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (nominal.getText().toString().trim().length() > 0) {
//                                Retrofit retrofit = new Retrofit.Builder()
//                                        .baseUrl(url)
//                                        .addConverterFactory(GsonConverterFactory.create())
//                                        .build();
//                                ApiEndPoints api = retrofit.create(ApiEndPoints.class);
//
//                                Call<SPPRepository> call = api.updateSPP(spp.getId_spp(), nominal.getText().toString());
//                                call.enqueue(new Callback<SPPRepository>() {
//                                    @Override
//                                    public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
//                                        String value = response.body().getValue();
//                                        String message = response.body().getMessage();
//
//                                        if (value.equals("1")) {
//                                            alertDialog2.dismiss();
//                                            mListener.onItemClicked(null, "1");
//
//                                        } else {
//                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<SPPRepository> call, Throwable t) {
//                                        alertDialog2.dismiss();
//                                        Toast.makeText(context, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
//                                        Log.e("DEBUG", "Error: ", t);
//                                    }
//                                });
//
//                            } else {
//                                Toast.makeText(context, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    view2.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            alertDialog2.dismiss();
//                        }
//                    });
//                    if (alertDialog2.getWindow() != null) {
//                        alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                    }
//                }
//            });
//
//            view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    alertDialog.dismiss();
//                }
//            });
//            if (alertDialog.getWindow() != null) {
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return spp.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView CardSiswa;
        TextView tvTahun, tvNominal, tvAngkatan;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTahun = itemView.findViewById(R.id.tahunSPP);
            tvNominal = itemView.findViewById(R.id.namaSiswa);
            tvAngkatan = itemView.findViewById(R.id.kelas);
            CardSiswa = itemView.findViewById(R.id.CardSiswa);
        }
    }
}