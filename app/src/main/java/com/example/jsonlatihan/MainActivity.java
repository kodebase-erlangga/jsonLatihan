package com.example.jsonlatihan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView bookCoverImageView;
    private TextView errorTextView;
    private static final String URL = "https://ebook.erlanggaonline.co.id/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookCoverImageView = findViewById(R.id.bookCoverImageView);
        errorTextView = findViewById(R.id.errorTextView);

        fetchBookCover();
    }

    private void fetchBookCover() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String erlStatusId = jsonResponse.getString("erlStatusId");
                            if (erlStatusId.equals("true")) {
                                JSONArray gallery = jsonResponse.getJSONArray("data");
                                JSONObject book = gallery.getJSONObject(0); // getting the first book
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                String imageUrl = "https://e-library.erlanggaonline.co.id/upload/cover/" +
                                        book.getString("galery_cover"); // Assuming field name is "galery_cover"
                                loadImage(imageUrl);
                            } else {
                                showError("Error: " + jsonResponse.getString("message"));
                            }
                        } catch (JSONException e) {
                            showError("Parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError("Request error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadImage(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        bookCoverImageView.setImageBitmap(response);
                    }
                },
                0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError("Image load error: " + error.getMessage());
                    }
                });

        Volley.newRequestQueue(this).add(imageRequest);
    }

    private void showError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }
}
