package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.PembayaranActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.PembayaranAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

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
        void onItemClicked(String id_spp, String refresh);
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

        holder.deleteData.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.cardmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(spp.getId_spp(), null);
                    }
                    return true;
                }
            });
            popup.show();
        });

        holder.detailSPP.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                    builder.setView(view);
                    ((TextView) view.findViewById(R.id.tvTahun)).setText("Tahun       : " + spp.getTahun());
                    ((TextView) view.findViewById(R.id.tvNominal)).setText("Nominal   : " + format.format(spp.getNominal()));
                    ((TextView) view.findViewById(R.id.tvAngkatan)).setText("Angkatan : " + spp.getAngkatan());
                    final AlertDialog alertDialog = builder.create();
                    view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_edit_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                            builder.setView(view2);
                            final TextView angkatan = (TextView) view2.findViewById(R.id.angkatan_spp);
                            final TextView tahun = (TextView) view2.findViewById(R.id.tahun_spp);
                            final EditText nominal = (EditText) view2.findViewById(R.id.nominal_spp);
                            final AlertDialog alertDialog2 = builder.create();

                            angkatan.setText(" " + spp.getAngkatan());
                            tahun.setText(" " + spp.getTahun());
                            nominal.setText(spp.getNominal().toString());

                            view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (nominal.getText().toString().trim().length() > 0) {
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(url)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                                        Call<SPPRepository> call = api.updateSPP(spp.getId_spp(), nominal.getText().toString());
                                        call.enqueue(new Callback<SPPRepository>() {
                                            @Override
                                            public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                                                String value = response.body().getValue();
                                                String message = response.body().getMessage();
                                                if (value.equals("1")) {
                                                    alertDialog2.dismiss();
                                                    mListener.onItemClicked(null, "1");
                                                } else {
                                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<SPPRepository> call, Throwable t) {
                                                Log.e("DEBUG", "Error: ", t);
                                            }
                                        });
                                    } else {
                                        Toast.makeText(context, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                                    }
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
        ImageView deleteData;
        MaterialCardView detailSPP;
        TextView tvTahun, tvNominal, tvAngkatan;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTahun = itemView.findViewById(R.id.Tahun);
            tvNominal = itemView.findViewById(R.id.Nominal);
            tvAngkatan = itemView.findViewById(R.id.Angkatan);
            detailSPP = itemView.findViewById(R.id.detailSPP);
            deleteData = itemView.findViewById(R.id.deleteData);
        }
    }
}
