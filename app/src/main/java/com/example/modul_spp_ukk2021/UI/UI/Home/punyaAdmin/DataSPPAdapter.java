package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.PembayaranActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.PembayaranAdapter;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataSPPAdapter extends RecyclerView.Adapter<DataSPPAdapter.ViewHolder> {
    private List<SPP> spp;
    private Context context;
    private static OnRecyclerViewItemClickListener mListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(String id_spp);
    }

    public void setOnRecyclerViewItemClickListener(DataSPPAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    public DataSPPAdapter(Context context, List<SPP> spp) {
        this.context = context;
        this.spp = spp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pa_container_data_spp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SPP spp = this.spp.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        holder.tvTahun.setText("Tahun " + spp.getTahun());
        holder.tvAngkatan.setText("Angkatan " + spp.getAngkatan());
        holder.tvNominal.setText(format.format(spp.getNominal()) + ",00");

        holder.detailSPP.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                    builder.setView(view);
                    ((TextView) view.findViewById(R.id.tvTahun)).setText("Tahun       : " + spp.getTahun());
                    ((TextView) view.findViewById(R.id.tvNominal)).setText("Nominal   : " + format.format(spp.getNominal()) + ",00");
                    ((TextView) view.findViewById(R.id.tvAngkatan)).setText("Angkatan : " + spp.getAngkatan());
                    final AlertDialog alertDialog = builder.create();
                    view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    view.findViewById(R.id.deleteData).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(context)
                                    .setMessage("Apakah anda yakin menghapus?")
                                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mListener.onItemClicked(spp.getId_spp());
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            alertDialog.show();
                                        }
                                    })
                                    .show();
                        }
                    });
                    view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_tambah_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                            builder.setView(view2);
                            final EditText angkatan = (EditText) view2.findViewById(R.id.angkatan_spp);
                            final EditText tahun = (EditText) view2.findViewById(R.id.tahun_spp);
                            final EditText nominal = (EditText) view2.findViewById(R.id.nominal_spp);
                            angkatan.setText(spp.getAngkatan());
                            tahun.setText(spp.getTahun());
                            nominal.setText(spp.getNominal().toString());
                            final AlertDialog alertDialog2 = builder.create();
                            view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(url)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                                    Call<SPPRepository> call = api.updateSPP(spp.getId_spp(), tahun.getText().toString(), nominal.getText().toString(), angkatan.getText().toString());
                                    call.enqueue(new Callback<SPPRepository>() {
                                        @Override
                                        public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                                            String value = response.body().getValue();
                                            String message = response.body().getMessage();
                                            if (value.equals("1")) {
                                                alertDialog2.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SPPRepository> call, Throwable t) {
                                            Log.e("DEBUG", "Error: ", t);
                                        }
                                    });
                                }
                            });
                            view2.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog2.dismiss();
                                }
                            });
                            if (alertDialog2.getWindow() != null) {
                                alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            }
                            alertDialog2.show();
                        }
                    });
                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }
            }, 400);
        });
    }

    @Override
    public int getItemCount() {
        return spp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton detailSPP;
        TextView tvTahun, tvNominal, tvAngkatan;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTahun = itemView.findViewById(R.id.Tahun);
            tvNominal = itemView.findViewById(R.id.Nominal);
            tvAngkatan = itemView.findViewById(R.id.Angkatan);
            detailSPP = itemView.findViewById(R.id.detailSPP);
        }
    }
}
