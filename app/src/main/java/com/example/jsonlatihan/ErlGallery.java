package com.example.jsonlatihan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErlGallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private static final String URL = "https://ebook.erlanggaonline.co.id/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erl_gallery);

        // Inisialisasi RecyclerView dan menggunakan GridLayoutManager dengan 3 kolom
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Memanggil method untuk mengambil gambar dari API
        fetchGallery();
    }

    private void fetchGallery() {
        // Membuat request dengan metode POST ke URL API
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing response JSON
                            JSONObject jsonResponse = new JSONObject(response);
                            String erlStatusId = jsonResponse.getString("erlStatusId");

                            // Mengecek jika status dari API adalah "true"
                            if (erlStatusId.equals("true")) {
                                JSONArray gallery = jsonResponse.getJSONArray("data");

                                // Ambil maksimal 20 gambar dari array JSON
                                for (int i = 0; i < gallery.length() && i < 20; i++) {
                                    JSONObject book = gallery.getJSONObject(i);
                                    String galery_cover = book.getString("galery_cover");
                                    String imageUrl = "https://e-library.erlanggaonline.co.id/upload/cover/" + galery_cover;
                                    imageUrls.add(imageUrl);
                                }

                                // Setelah data diambil, set adapter ke RecyclerView
                                imageAdapter = new ImageAdapter(ErlGallery.this, imageUrls);
                                recyclerView.setAdapter(imageAdapter);
                            }
                        } catch (JSONException e) {
                            // Menangkap kesalahan JSON jika terjadi
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani kesalahan response dari Volley
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Menyediakan parameter POST yang diperlukan oleh API
                Map<String, String> params = new HashMap<>();
                params.put("user_email", "mihsanrahman19@gmail.com");
                params.put("user_password", "ihsan111");
                params.put("user_device_id", "fae3876e39143557");
                params.put("user_version", "proteksi");
                params.put("user_jenjang", "SEMUA");
                params.put("halaman", "1");
                params.put("id", "14");
                params.put("aksi", "ambilgalery");
                return params;
            }
        };

        // Menambahkan request ke Volley queue untuk dieksekusi
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
