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
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
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

public class DataKelasAdapter extends RecyclerView.Adapter<DataKelasAdapter.ViewHolder> {
    private List<Kelas> kelas;
    private Context context;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pa_container_data_kelas, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Kelas kelas = this.kelas.get(position);

        holder.tvNamaKelas.setText(kelas.getNama_kelas());
        holder.tvAngkatan.setText("Angkatan " + kelas.getAngkatan());
        holder.tvJurusan.setText(kelas.getJurusan());

        holder.deleteData.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.cardmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(kelas.getId_kelas(), null);
                    }
                    return true;
                }
            });
            popup.show();
        });

        holder.detailKelas.setOnClickListener(v -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_kelas, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                    builder.setView(view);
                    ((TextView) view.findViewById(R.id.tvNamaKelas)).setText("Kelas         : " + kelas.getNama_kelas());
                    ((TextView) view.findViewById(R.id.tvJurusan)).setText("Jurusan    : " + kelas.getJurusan());
                    ((TextView) view.findViewById(R.id.tvAngkatan)).setText("Angkatan : " + kelas.getAngkatan());
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
                            View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_tambah_kelas, (ConstraintLayout) v.findViewById(R.id.layoutDialogContainer));
                            builder.setView(view2);
                            final EditText angkatan = (EditText) view2.findViewById(R.id.angkatan_kelas);
                            final EditText nama_kelas = (EditText) view2.findViewById(R.id.nama_kelas);
                            final EditText jurusan = (EditText) view2.findViewById(R.id.jurusan_kelas);
                            final TextView textTitle = (TextView) view2.findViewById(R.id.textTitle);
                            textTitle.setText("Edit Kelas");
                            angkatan.setText(kelas.getAngkatan());
                            nama_kelas.setText(kelas.getNama_kelas());
                            jurusan.setText(kelas.getJurusan());
                            final AlertDialog alertDialog2 = builder.create();
                            view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(url)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                                    Call<KelasRepository> call = api.updateKelas(kelas.getId_kelas(), nama_kelas.getText().toString(), jurusan.getText().toString(), angkatan.getText().toString());
                                    call.enqueue(new Callback<KelasRepository>() {
                                        @Override
                                        public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
                                            String value = response.body().getValue();
                                            String message = response.body().getMessage();
                                            if (value.equals("1")) {
                                                alertDialog2.dismiss();
                                                mListener.onItemClicked(null, "1");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<KelasRepository> call, Throwable t) {
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
        return kelas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteData;
        MaterialCardView detailKelas;
        TextView tvNamaKelas, tvJurusan, tvAngkatan;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaKelas = itemView.findViewById(R.id.Kelas);
            tvJurusan = itemView.findViewById(R.id.Jurusan);
            tvAngkatan = itemView.findViewById(R.id.Angkatan);
            detailKelas = itemView.findViewById(R.id.detailKelas);
            deleteData = itemView.findViewById(R.id.deleteData);
        }
    }
}
