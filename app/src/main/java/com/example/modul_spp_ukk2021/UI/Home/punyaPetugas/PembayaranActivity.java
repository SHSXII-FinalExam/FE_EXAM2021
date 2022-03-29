package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Helper.InputFilterMinMax;
import com.example.modul_spp_ukk2021.UI.Data.Helper.Utils;
import com.example.modul_spp_ukk2021.UI.Data.Model.Pembayaran;
import com.example.modul_spp_ukk2021.UI.Data.Repository.PembayaranRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class PembayaranActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ApiEndPoints api;
    private RecyclerView recyclerView;
    private PembayaranAdapter adapter;
    private String id_pembayaran, id_petugas;
    private LottieAnimationView emptyTransaksi;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final List<Pembayaran> pembayaran = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        id_petugas = getIntent().getStringExtra("idstaff");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        ImageView back = findViewById(R.id.back);
        ImageView refresh = findViewById(R.id.refresh);
        emptyTransaksi = findViewById(R.id.emptyTransaksi);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView = findViewById(R.id.recyclerTagihanSiswa);
        adapter = new PembayaranAdapter(this, pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiEndPoints.class);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refreshing();
            }
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });

        refresh.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            PopupMenu popup = new PopupMenu(this, v, Gravity.END, R.attr.popupMenuStyle, 0);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_refresh, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_refresh) {
                        Refreshing();
                    }
                    return true;
                }
            });
            popup.show();
        });

        adapter.setOnRecyclerViewItemClickListener((nama_siswa, nisn, id_pembayaran, jumlah_bayar, nominal, tanggalTagihan, tanggalBayar, status, nama_staff, nama_kelas, kurang_bayar, download) -> {
            if (download == null) {
                DialogUpdate(id_pembayaran, nominal, kurang_bayar);

            }
//            else {
//                GeneratePembayaran(nama_siswa, nisn, id_pembayaran, jumlah_bayar, tanggalTagihan, tanggalBayar, status, nama_staff, nama_kelas);
//            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataPembayaran();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void Refreshing() {
        swipeRefreshLayout.setRefreshing(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadDataPembayaran();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }, 1000);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    private void loadDataPembayaran() {
        String nisnSiswa = this.getIntent().getStringExtra("nisnSiswa");
        Call<PembayaranRepository> call = api.viewPembayaran(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Pembayaran> pembayaran = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyTransaksi.pauseAnimation();
                    emptyTransaksi.setVisibility(LottieAnimationView.GONE);

                    adapter = new PembayaranAdapter(PembayaranActivity.this, pembayaran);
                    recyclerView.setAdapter(adapter);
                    runLayoutAnimation(recyclerView);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                    emptyTransaksi.playAnimation();
                    Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyTransaksi.setVisibility(LottieAnimationView.VISIBLE);
                emptyTransaksi.playAnimation();
                Toast.makeText(PembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    private void DialogUpdate(String id_pembayaran, Integer nominal, Integer kurang_bayar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PembayaranActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PembayaranActivity.this).inflate(R.layout.dialog_update_pembayaran, findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        TextView maxInput = view.findViewById(R.id.maxInput);
        EditText jumlah_bayar = view.findViewById(R.id.jumlahBayar);
        final AlertDialog alertDialog = builder.create();

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        if (kurang_bayar == 0) {
            maxInput.setText("Max Input: " + format.format(nominal));
            jumlah_bayar.setFilters(new InputFilter[]{new InputFilterMinMax("0", nominal.toString())});
        } else {
            maxInput.setText("Max Input: " + format.format(kurang_bayar));
            jumlah_bayar.setFilters(new InputFilter[]{new InputFilterMinMax("0", kurang_bayar.toString())});
        }

        view.findViewById(R.id.buttonBatal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.buttonKirim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, jumlah_bayar.getText().toString(), id_petugas);
                call.enqueue(new Callback<PembayaranRepository>() {
                    @Override
                    public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            alertDialog.dismiss();
                            loadDataPembayaran();

                        } else {
                            Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        alertDialog.dismiss();
                        Toast.makeText(PembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });

        view.findViewById(R.id.buttonLunas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PembayaranRepository> call = api.updatePembayaran(id_pembayaran, nominal.toString(), id_petugas);
                call.enqueue(new Callback<PembayaranRepository>() {
                    @Override
                    public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("1")) {
                            alertDialog.dismiss();
                            loadDataPembayaran();

                        } else {
                            Toast.makeText(PembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                        alertDialog.dismiss();
                        Toast.makeText(PembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Error: ", t);
                    }
                });
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

//    private void GeneratePembayaran(String nama_siswa, String nisn, String id_pembayaran, Integer jumlah_bayar, String tanggalTagihan, String tanggalBayar, String status, String nama_staff, String nama_kelas) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(PembayaranActivity.this, R.style.AlertDialogTheme);
//        View view = LayoutInflater.from(PembayaranActivity.this).inflate(R.layout.dialog_generate_laporan, findViewById(R.id.layoutDialogContainer));
//        builder.setView(view);
//        final AlertDialog alertDialog = builder.create();
//
//        TextView tvFillNama = view.findViewById(R.id.tvFillNama);
//        TextView tvNISN = view.findViewById(R.id.tvNISN);
//        TextView nomor_pembayaran = view.findViewById(R.id.nomor_pembayaran);
//        TextView nominal = view.findViewById(R.id.nominal);
//        TextView tanggal_tagihan = view.findViewById(R.id.tanggal_tagihan);
//        TextView tanggal_bayar = view.findViewById(R.id.tanggal_bayar);
//        TextView status_pembayaran = view.findViewById(R.id.status_pembayaran);
//        TextView dilayaniOleh = view.findViewById(R.id.dilayaniOleh);
//        TextView kelas = view.findViewById(R.id.kelas);
//
//        Locale localeID = new Locale("in", "ID");
//        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
//        format.setMaximumFractionDigits(0);
//
//        tvFillNama.setText(nama_siswa);
//        tvNISN.setText("NISN    : " + nisn);
//        nomor_pembayaran.setText("#" + id_pembayaran.toUpperCase());
//        nominal.setText(format.format(jumlah_bayar));
//        tanggal_tagihan.setText(tanggalTagihan);
//        tanggal_bayar.setText(tanggalBayar);
//        status_pembayaran.setText(status);
//        dilayaniOleh.setText(nama_staff);
//        kelas.setText(nama_kelas);
//
//        this.id_pembayaran = id_pembayaran;
//
//        ConstraintLayout layoutToPdf = view.findViewById(R.id.layoutDialog);
//        view.findViewById(R.id.buttonDownload).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                bitmap = LoadBitmap(layoutToPdf, layoutToPdf.getWidth(), layoutToPdf.getHeight());
//                Log.e("size", "" + layoutToPdf.getWidth() + " " + layoutToPdf.getWidth());
//                createPdf();
//            }
//        });
//
//        if (alertDialog.getWindow() != null) {
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        alertDialog.show();
//    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void createPdf() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;
        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.drawColor(Color.WHITE);
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + id_pembayaran + ".pdf");
        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        document.close();
        openPdf();
    }

    private void openPdf() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + id_pembayaran + ".pdf");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Tidak ada aplikasi pembuka pdf...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
