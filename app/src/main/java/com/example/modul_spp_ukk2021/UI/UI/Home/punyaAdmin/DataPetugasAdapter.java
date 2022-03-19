package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
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

public class DataPetugasAdapter extends RecyclerView.Adapter<DataPetugasAdapter.ViewHolder> {
    private List<Petugas> petugas;
    private Context context;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pa_container_data_petugas, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Petugas petugas = this.petugas.get(position);

        holder.tvNamaPetugas.setText(petugas.getNama_petugas());
        holder.tvLevel.setText("Staff level: " + petugas.getLevel());
        holder.tvUsername.setText("Username: " + petugas.getUsername());

        holder.deleteData.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.cardmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(petugas.getId_petugas(), null);
                    }
                    return true;
                }
            });
            popup.show();
        });

        holder.detailStaff.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_petugas, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                    builder.setView(view);
                    ((TextView) view.findViewById(R.id.tvNama)).setText("Nama         : " + petugas.getNama_petugas());
                    ((TextView) view.findViewById(R.id.tvUsername)).setText("Username : " + petugas.getUsername());
                    ((TextView) view.findViewById(R.id.tvLevel)).setText("Level          : " + petugas.getLevel());
                    final AlertDialog alertDialog = builder.create();
                    view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
//                    view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
//                            View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_edit_spp, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
//                            builder.setView(view2);
//                            final TextView angkatan = (TextView) view2.findViewById(R.id.angkatan_spp);
//                            final TextView tahun = (TextView) view2.findViewById(R.id.tahun_spp);
//                            final EditText nominal = (EditText) view2.findViewById(R.id.nominal_spp);
//                            angkatan.setText(" " + spp.getAngkatan());
//                            tahun.setText(" " + spp.getTahun());
//                            nominal.setText(spp.getNominal().toString());
//                            final AlertDialog alertDialog2 = builder.create();
//                            view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Retrofit retrofit = new Retrofit.Builder()
//                                            .baseUrl(url)
//                                            .addConverterFactory(GsonConverterFactory.create())
//                                            .build();
//                                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
//                                    Call<SPPRepository> call = api.updateSPP(spp.getId_spp(), nominal.toString());
//                                    call.enqueue(new Callback<SPPRepository>() {
//                                        @Override
//                                        public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
//                                            String value = response.body().getValue();
//                                            String message = response.body().getMessage();
//                                            if (value.equals("1")) {
//                                                alertDialog2.dismiss();
//                                                mListener.onItemClicked(null, "1");
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<SPPRepository> call, Throwable t) {
//                                            Log.e("DEBUG", "Error: ", t);
//                                        }
//                                    });
//                                }
//                            });
//                            view2.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    alertDialog2.dismiss();
//                                }
//                            });
//                            if (alertDialog2.getWindow() != null) {
//                                alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                            }
//                            alertDialog2.show();
//                        }
//                    });
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
        return petugas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteData;
        MaterialCardView detailStaff;
        TextView tvNamaPetugas, tvLevel, tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaPetugas = itemView.findViewById(R.id.nama_petugas);
            tvLevel = itemView.findViewById(R.id.Level);
            tvUsername = itemView.findViewById(R.id.Username);
            detailStaff = itemView.findViewById(R.id.detailStaff);
            deleteData = itemView.findViewById(R.id.deleteData);
        }
    }
}
