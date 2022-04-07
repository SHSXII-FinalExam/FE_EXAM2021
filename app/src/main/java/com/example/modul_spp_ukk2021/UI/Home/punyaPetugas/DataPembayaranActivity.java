package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
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

public class DataPembayaranActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ApiEndPoints api;
    private String id_pembayaran;
    private RecyclerView recyclerView;
    private DataPembayaranAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final List<Pembayaran> pembayaran = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petugas_activity_data_pembayaran);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        ImageView back = findViewById(R.id.back);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView = findViewById(R.id.data_pembayaran);
        adapter = new DataPembayaranAdapter(DataPembayaranActivity.this, pembayaran);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

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

        adapter.setOnRecyclerViewItemClickListener((nama_siswa, nisn, id_pembayaran, jumlah_bayar, nominal, tanggalTagihan, tanggalBayar, status, nama_staff, nama_kelas, kurang_bayar, download) -> {
            if (download == null) {
                Intent intent = new Intent(DataPembayaranActivity.this, PembayaranActivity.class);
                intent.putExtra("id_petugas", getIntent().getStringExtra("id_petugas"));
                intent.putExtra("id_pembayaran", id_pembayaran);
                intent.putExtra("nominal", nominal);
                intent.putExtra("kurang_bayar", kurang_bayar);
                startActivity(intent);

            } else {
                GeneratePembayaran(nama_siswa, nisn, id_pembayaran, jumlah_bayar, tanggalTagihan, tanggalBayar, status, nama_staff, nama_kelas);
            }
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

    private void loadDataPembayaran() {
        String nisnSiswa = getIntent().getStringExtra("nisnSiswa");
        Call<PembayaranRepository> call = api.viewPembayaran(nisnSiswa);
        call.enqueue(new Callback<PembayaranRepository>() {
            @Override
            public void onResponse(Call<PembayaranRepository> call, Response<PembayaranRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<Pembayaran> pembayaran = response.body().getResult();

                if (value.equals("1")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new DataPembayaranAdapter(DataPembayaranActivity.this, pembayaran);
                    recyclerView.setAdapter(adapter);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(DataPembayaranActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PembayaranRepository> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(DataPembayaranActivity.this, "Gagal koneksi sistem, silahkan coba lagi..." + " [" + t.toString() + "]", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }

    private void GeneratePembayaran(String nama_siswa, String nisn, String id_pembayaran, Integer jumlah_bayar, String tanggalTagihan, String tanggalBayar, String status, String nama_staff, String nama_kelas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataPembayaranActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(DataPembayaranActivity.this).inflate(R.layout.activity_generate_laporan, findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        TextView tvFillNama = view.findViewById(R.id.tvFillNama);
        TextView tvNISN = view.findViewById(R.id.tvNISN);
        TextView nomor_pembayaran = view.findViewById(R.id.nomor_pembayaran);
        TextView nominal = view.findViewById(R.id.nominal);
        TextView tanggal_tagihan = view.findViewById(R.id.tanggal_tagihan);
        TextView tanggal_bayar = view.findViewById(R.id.tanggal_bayar);
        TextView status_pembayaran = view.findViewById(R.id.status_pembayaran);
        TextView dilayaniOleh = view.findViewById(R.id.dilayaniOleh);
        TextView kelas = view.findViewById(R.id.kelas);

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);

        tvFillNama.setText(nama_siswa);
        tvNISN.setText("NISN    : " + nisn);
        nomor_pembayaran.setText("#" + id_pembayaran.toUpperCase());
        nominal.setText(format.format(jumlah_bayar));
        tanggal_tagihan.setText(tanggalTagihan);
        tanggal_bayar.setText(tanggalBayar);
        status_pembayaran.setText(status);
        dilayaniOleh.setText(nama_staff);
        kelas.setText(nama_kelas);

        this.id_pembayaran = id_pembayaran;

        ConstraintLayout layoutToPdf = view.findViewById(R.id.layoutDialog);
        view.findViewById(R.id.buttonDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                bitmap = LoadBitmap(layoutToPdf, layoutToPdf.getWidth(), layoutToPdf.getHeight());
                Log.e("size", "" + layoutToPdf.getWidth() + " " + layoutToPdf.getWidth());
                createPdf();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

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
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + id_pembayaran + ".pdf");
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
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + id_pembayaran + ".pdf");
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
