package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_container_data_petugas, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Petugas petugas = this.petugas.get(position);

        SharedPreferences sharedprefs = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String usernameStaff = sharedprefs.getString("usernameStaff", null);
        String passwordStaff = sharedprefs.getString("passwordStaff", null);

        holder.tvNamaPetugas.setText(petugas.getNama_petugas());
        holder.tvLevel.setText("Staff level: " + petugas.getLevel());

        if (petugas.getUsername().equals(usernameStaff)) {
            holder.tvNamaPetugas.setText("Anda");
        } else if (petugas.getLevel().equals("Admin")) {
        }

//        holder.deleteData.setOnClickListener(v -> {
//            Utils.preventTwoClick(v);
//            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
//            MenuInflater inflater = popup.getMenuInflater();
//            inflater.inflate(R.menu.menu_customcard, popup.getMenu());
//
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (item.getItemId() == R.id.action_delete) {
//                        mListener.onItemClicked(petugas.getId_petugas(), null);
//                    }
//                    return true;
//                }
//            });
//            popup.show();
//        });
//
//        holder.detailStaff.setOnClickListener(v -> {
//            Utils.preventTwoClick(v);
//            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
//            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_petugas, v.findViewById(R.id.layoutDialogContainer));
//            builder.setView(view);
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//            ((TextView) view.findViewById(R.id.tvFillNama)).setText(petugas.getNama_petugas());
//            ((TextView) view.findViewById(R.id.tvFillUsername)).setText(petugas.getUsername());
//            ((TextView) view.findViewById(R.id.tvLevel)).setText("Level          : " + petugas.getLevel());
//
//            if (petugas.getUsername().equals(usernameStaff)) {
//                TextView tvPassword = view.findViewById(R.id.tvPassword);
//                tvPassword.setText("Password  : " + passwordStaff);
//                view.findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
//
//            } else if (petugas.getLevel().equals("Admin")) {
//                view.findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
//
//            } else {
//                view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
//                        View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_tambah_petugas, v.findViewById(R.id.layoutDialogContainer));
//                        builder.setView(view2);
//                        AlertDialog alertDialog2 = builder.create();
//                        alertDialog2.show();
//
//                        TextView textTitle = view2.findViewById(R.id.textTitle);
//                        EditText nama_petugas = view2.findViewById(R.id.nama_petugas);
//                        EditText username_petugas = view2.findViewById(R.id.username_petugas);
//                        EditText password_petugas = view2.findViewById(R.id.password_petugas);
//
//                        textTitle.setText("Edit Petugas");
//                        nama_petugas.setText(petugas.getNama_petugas());
//                        username_petugas.setText(petugas.getUsername());
//                        password_petugas.setHint("New password");
//
//                        view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (nama_petugas.getText().toString().trim().length() > 0 && username_petugas.getText().toString().trim().length() > 0 && password_petugas.getText().toString().trim().length() > 0) {
//                                    Retrofit retrofit = new Retrofit.Builder()
//                                            .baseUrl(url)
//                                            .addConverterFactory(GsonConverterFactory.create())
//                                            .build();
//                                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
//
//                                    Call<PetugasRepository> call = api.updatePetugas(petugas.getId_petugas(), "Petugas", username_petugas.getText().toString(), password_petugas.getText().toString(), nama_petugas.getText().toString());
//                                    call.enqueue(new Callback<PetugasRepository>() {
//                                        @Override
//                                        public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
//                                            String value = response.body().getValue();
//                                            String message = response.body().getMessage();
//
//                                            if (value.equals("1")) {
//                                                alertDialog2.dismiss();
//                                                mListener.onItemClicked(null, "1");
//
//                                            } else {
//                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<PetugasRepository> call, Throwable t) {
//                                            alertDialog2.dismiss();
//                                            Toast.makeText(context, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
//                                            Log.e("DEBUG", "Error: ", t);
//                                        }
//                                    });
//
//                                } else {
//                                    Toast.makeText(context, "Data belum lengkap, silahkan coba lagi", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                        view2.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                alertDialog2.dismiss();
//                            }
//                        });
//                        if (alertDialog2.getWindow() != null) {
//                            alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//                        }
//                    }
//                });
//            }
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
        return petugas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaPetugas, tvLevel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLevel = itemView.findViewById(R.id.levelStaff);
            tvNamaPetugas = itemView.findViewById(R.id.namaPetugas);
        }
    }
}
