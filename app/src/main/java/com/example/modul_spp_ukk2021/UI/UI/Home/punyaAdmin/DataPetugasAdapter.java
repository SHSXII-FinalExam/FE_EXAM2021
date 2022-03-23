package com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Petugas;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PetugasRepository;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pa_container_data_petugas, parent, false);
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
        holder.tvUsername.setText("Username: " + petugas.getUsername());

        if (petugas.getUsername().equals(usernameStaff)) {
            holder.tvNamaPetugas.setText("Anda");
            holder.deleteData.setVisibility(View.GONE);
            holder.container_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_grey));
        } else if (petugas.getLevel().equals("Admin")) {
            holder.deleteData.setVisibility(View.GONE);
        }

        holder.deleteData.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(context, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_customcard, popup.getMenu());
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
            Utils.preventTwoClick(v);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View view = LayoutInflater.from(context).inflate(R.layout.pa_dialog_view_petugas, v.findViewById(R.id.layoutDialogContainer));
            builder.setView(view);
            ((TextView) view.findViewById(R.id.tvFillNama)).setText(petugas.getNama_petugas());
            ((TextView) view.findViewById(R.id.tvFillUsername)).setText(petugas.getUsername());
            ((TextView) view.findViewById(R.id.tvLevel)).setText("Level          : " + petugas.getLevel());
            final AlertDialog alertDialog = builder.create();

            view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            if (petugas.getUsername().equals(usernameStaff)) {
                final TextView tvPassword = view.findViewById(R.id.tvPassword);
                tvPassword.setText("Password  : " + passwordStaff);

            } else if (petugas.getLevel().equals("Admin")) {
                view.findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);

            } else {
                view.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        View view2 = LayoutInflater.from(context).inflate(R.layout.pa_dialog_tambah_petugas, v.findViewById(R.id.layoutDialogContainer));
                        builder.setView(view2);
                        final TextView textTitle = view2.findViewById(R.id.textTitle);
                        final EditText nama_petugas = view2.findViewById(R.id.nama_petugas);
                        final EditText username_petugas = view2.findViewById(R.id.username_petugas);
                        final EditText password_petugas = view2.findViewById(R.id.password_petugas);
                        final AlertDialog alertDialog2 = builder.create();

                        textTitle.setText("Edit Petugas");
                        nama_petugas.setText(petugas.getNama_petugas());
                        username_petugas.setText(petugas.getUsername());
                        password_petugas.setHint("New password");

                        view2.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (password_petugas.getText().toString().trim().length() > 0) {
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(url)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    ApiEndPoints api = retrofit.create(ApiEndPoints.class);
                                    Call<PetugasRepository> call = api.updatePetugas(petugas.getId_petugas(), "Petugas", username_petugas.getText().toString(), password_petugas.getText().toString(), nama_petugas.getText().toString());
                                    call.enqueue(new Callback<PetugasRepository>() {
                                        @Override
                                        public void onResponse(Call<PetugasRepository> call, Response<PetugasRepository> response) {
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
                                        public void onFailure(Call<PetugasRepository> call, Throwable t) {
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
            }
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return petugas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView deleteData;
        MaterialCardView detailStaff;
        ConstraintLayout container_layout;
        TextView tvNamaPetugas, tvLevel, tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaPetugas = itemView.findViewById(R.id.nama_petugas);
            tvLevel = itemView.findViewById(R.id.Level);
            tvUsername = itemView.findViewById(R.id.Username);
            detailStaff = itemView.findViewById(R.id.detailStaff);
            deleteData = itemView.findViewById(R.id.deleteData);
            container_layout = itemView.findViewById(R.id.container_layout);
        }
    }
}
