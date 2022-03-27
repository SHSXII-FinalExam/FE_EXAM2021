package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Kelas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.KelasRepository;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class DataKelasAdapter extends RecyclerView.Adapter<DataKelasAdapter.ViewHolder> {
    private final Context context;
    private final List<Kelas> kelas;
    private String nama, jurusan_kelas;
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
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_customcard, popup.getMenu());

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
            Utils.preventTwoClick(v);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_kelas, v.findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ((TextView) view.findViewById(R.id.tvNamaKelas)).setText("Kelas         : " + kelas.getNama_kelas());
            ((TextView) view.findViewById(R.id.tvJurusan)).setText("Jurusan    : " + kelas.getJurusan());
            ((TextView) view.findViewById(R.id.tvAngkatan)).setText("Angkatan : " + kelas.getAngkatan());

            view.findViewById(R.id.buttonSiswa).setOnClickListener(new View.OnClickListener() {
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

            view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_tambah_kelas, v.findViewById(R.id.layoutDialogContainer));
                    builder.setView(view2);
                    AlertDialog alertDialog2 = builder.create();
                    alertDialog2.show();

                    TextView textTitle = view2.findViewById(R.id.textTitle);
                    Button nama_kelas = view2.findViewById(R.id.nama_kelas);
                    EditText namanomor = view2.findViewById(R.id.namanomor_kelas);
                    EditText angkatan = view2.findViewById(R.id.angkatan_kelas);
                    RadioGroup jurusan = view2.findViewById(R.id.jurusan_kelas);
                    TextView namajurusan = view2.findViewById(R.id.namajurusan_kelas);

                    nama = kelas.getNama_kelas().substring(0, kelas.getNama_kelas().indexOf(' '));

                    nama_kelas.setText(nama);
                    textTitle.setText("Edit Kelas");
                    angkatan.setText(kelas.getAngkatan());
                    namajurusan.setText(kelas.getJurusan());
                    namanomor.setText(kelas.getNama_kelas().replaceAll("[^0-9]", ""));

                    if (kelas.getJurusan().equals("RPL")) {
                        jurusan_kelas = "RPL";
                        jurusan.check(R.id.RPL);
                    } else if (kelas.getJurusan().equals("TKJ")) {
                        jurusan_kelas = "TKJ";
                        jurusan.check(R.id.TKJ);
                    }

                    jurusan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int id) {
                            switch (id) {
                                case R.id.RPL:
                                    jurusan_kelas = "RPL";
                                    namajurusan.setText(jurusan_kelas);
                                    break;
                                case R.id.TKJ:
                                    jurusan_kelas = "TKJ";
                                    namajurusan.setText(jurusan_kelas);
                                    break;
                            }
                        }
                    });

                    nama_kelas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu dropDownMenu = new PopupMenu(context, nama_kelas);
                            dropDownMenu.getMenuInflater().inflate(R.menu.dropdown_kelas, dropDownMenu.getMenu());
                            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    nama_kelas.setText(menuItem.getTitle().toString());
                                    nama = menuItem.getTitle().toString();
                                    return true;
                                }
                            });
                            dropDownMenu.show();
                        }
                    });

                    view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (nama != null && jurusan_kelas != null && angkatan.getText().toString().trim().length() > 0) {
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(url)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                ApiEndPoints api = retrofit.create(ApiEndPoints.class);

                                Call<KelasRepository> call = api.updateKelas(kelas.getId_kelas(), nama + " " + jurusan_kelas + " " + namanomor.getText().toString(), jurusan_kelas, angkatan.getText().toString());
                                call.enqueue(new Callback<KelasRepository>() {
                                    @Override
                                    public void onResponse(Call<KelasRepository> call, Response<KelasRepository> response) {
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
                                    public void onFailure(Call<KelasRepository> call, Throwable t) {
                                        alertDialog2.dismiss();
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
                            alertDialog2.dismiss();
                        }
                    });
                    if (alertDialog2.getWindow() != null) {
                        alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
        return kelas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteData;
        MaterialCardView detailKelas;
        TextView tvNamaKelas, tvJurusan, tvAngkatan;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaKelas = itemView.findViewById(R.id.Kelas);
            tvJurusan = itemView.findViewById(R.id.Jurusan);
            tvAngkatan = itemView.findViewById(R.id.Angkatan);
            deleteData = itemView.findViewById(R.id.deleteData);
            detailKelas = itemView.findViewById(R.id.detailKelas);
        }
    }
}
