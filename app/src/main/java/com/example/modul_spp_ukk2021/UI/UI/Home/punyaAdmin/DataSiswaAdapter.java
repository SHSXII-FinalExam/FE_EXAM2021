package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.SPP;
import com.example.modul_spp_ukk2021.UI.Data.Model.Siswa;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SPPRepository;
import com.example.modul_spp_ukk2021.UI.Data.Repository.SiswaRepository;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.PembayaranActivity;
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

public class DataSiswaAdapter extends RecyclerView.Adapter<DataSiswaAdapter.ViewHolder> {
    private Integer id_spp;
    private final Context context;
    private final List<Siswa> siswa;
    private static OnRecyclerViewItemClickListener mListener;
    private String id_kelas, id_petugas, nama_kelas, angkatan;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pa_container_data_siswa, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Siswa siswa = this.siswa.get(position);

        SharedPreferences sharedprefs = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        id_petugas = sharedprefs.getString("idStaff", null);

        Intent intent = ((Activity) context).getIntent();
        id_kelas = intent.getStringExtra("id_kelas");
        angkatan = intent.getStringExtra("angkatan");
        nama_kelas = intent.getStringExtra("nama_kelas");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        holder.tvNama.setText(siswa.getNama());
        holder.tvNISN.setText(siswa.getNisn());
        holder.tvKelas.setText("Siswa " + siswa.getNama_kelas());

        holder.deleteData.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_customcard, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        mListener.onItemClicked(siswa.getNisn(), null);
                    }
                    return true;
                }
            });
            popup.show();
        });

        holder.detailSiswa.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_siswa, v.findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ((TextView) view.findViewById(R.id.tvFillNama)).setText(siswa.getNama());
            ((TextView) view.findViewById(R.id.tvNISN)).setText("NISN                 : " + siswa.getNisn());
            ((TextView) view.findViewById(R.id.tvNIS)).setText("NIS                    : " + siswa.getNis());
            ((TextView) view.findViewById(R.id.tvKelas)).setText("Kelas                 : " + siswa.getNama_kelas());
            ((TextView) view.findViewById(R.id.tvFillAlamat)).setText(siswa.getAlamat());
            ((TextView) view.findViewById(R.id.tvNoTelp)).setText("Nomor Ponsel : " + siswa.getNo_telp());

            view.findViewById(R.id.buttonTransaksi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.preventTwoClick(view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, PembayaranActivity.class);
                            intent.putExtra("nisnSiswa", siswa.getNisn());
                            context.startActivity(intent);
                        }
                    }, 400);
                }
            });

            view.findViewById(R.id.edit_siswa).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.preventTwoClick(v);
                    alertDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_tambah_siswa, v.findViewById(R.id.layoutDialogContainer));
                    builder.setView(view2);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView textTitle = view2.findViewById(R.id.textTitle);
                    EditText nama_siswa = view2.findViewById(R.id.nama_siswa);
                    EditText NISN_siswa = view2.findViewById(R.id.NISN_siswa);
                    EditText NIS_siswa = view2.findViewById(R.id.NIS_siswa);
                    TextView tvKelas = view2.findViewById(R.id.tvKelas);
                    Button spp_siswa = view2.findViewById(R.id.spp_siswa);
                    EditText alamat_siswa = view2.findViewById(R.id.alamat_siswa);
                    EditText ponsel_siswa = view2.findViewById(R.id.ponsel_siswa);
                    EditText password_siswa = view2.findViewById(R.id.password_siswa);

                    NISN_siswa.setEnabled(false);
                    NIS_siswa.setEnabled(false);

                    textTitle.setText("Edit Siswa");
                    nama_siswa.setText(siswa.getNama());
                    NISN_siswa.setText(siswa.getNisn());
                    NIS_siswa.setText(siswa.getNis());
                    tvKelas.setText("Kelas                  : " + nama_kelas);
                    alamat_siswa.setText(siswa.getAlamat());
                    ponsel_siswa.setText(siswa.getNo_telp());
                    password_siswa.setHint("New password");

                    PopupMenu dropDownMenu = new PopupMenu(context, spp_siswa);

                    Call<SPPRepository> call = api.viewDataSPPAngkatan(angkatan);
                    call.enqueue(new Callback<SPPRepository>() {
                        @Override
                        public void onResponse(Call<SPPRepository> call, Response<SPPRepository> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            List<SPP> results = response.body().getResult();

                            Locale localeID = new Locale("in", "ID");
                            NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                            format.setMaximumFractionDigits(0);

                            if (value.equals("1")) {
                                for (int i = 0; i < results.size(); i++) {
                                    dropDownMenu.getMenu().add(Menu.NONE, results.get(i).getId_spp(), Menu.NONE, results.get(i).getTahun() + " (" + format.format(results.get(i).getNominal()) + ")");
                                }

                            } else {
                                spp_siswa.setEnabled(false);
                                spp_siswa.setText("SPP Kosong");
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SPPRepository> call, Throwable t) {
                            alertDialog.dismiss();
                            Toast.makeText(context, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                            Log.e("DEBUG", "Error: ", t);
                        }
                    });

                    spp_siswa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dropDownMenu.show();
                            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    spp_siswa.setText(menuItem.getTitle().toString());
                                    id_spp = menuItem.getItemId();
                                    return true;
                                }
                            });
                        }
                    });

                    view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (id_spp != null && password_siswa.getText().toString().trim().length() > 0) {
                                Call<SiswaRepository> call = api.updateSiswa(
                                        NISN_siswa.getText().toString(),
                                        nama_siswa.getText().toString(),
                                        id_kelas,
                                        id_spp,
                                        alamat_siswa.getText().toString(),
                                        ponsel_siswa.getText().toString(),
                                        password_siswa.getText().toString(),
                                        id_petugas);
                                call.enqueue(new Callback<SiswaRepository>() {
                                    @Override
                                    public void onResponse(Call<SiswaRepository> call, Response<SiswaRepository> response) {
                                        String value = response.body().getValue();
                                        String message = response.body().getMessage();

                                        if (value.equals("1")) {
                                            alertDialog.dismiss();
                                            mListener.onItemClicked(null, "1");

                                        } else {
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SiswaRepository> call, Throwable t) {
                                        alertDialog.dismiss();
                                        Toast.makeText(context, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
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
                            alertDialog.dismiss();
                        }
                    });
                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                }
            });

            view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
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
        return siswa.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteData;
        MaterialCardView detailSiswa;
        TextView tvNama, tvNISN, tvKelas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNISN = itemView.findViewById(R.id.tvNISN);
            tvKelas = itemView.findViewById(R.id.tvKelas);
            deleteData = itemView.findViewById(R.id.deleteData);
            detailSiswa = itemView.findViewById(R.id.detailSiswa);
        }
    }
}
